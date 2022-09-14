package com.predic8.wsdl.ns;

import com.predic8.schema.Schema;
import com.predic8.soamodel.XMLElement;

import org.apache.commons.lang3.StringUtils;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NSContext {

	private static final Pattern SEED = Pattern.compile("[0-9]+$");

	private Map<XMLElement, Stack<Map<String, String>>> stack = new HashMap<>();

	private static boolean enabled = true;

	/**
	 * Just for test
	 */
	public static void enable(boolean value) {
		NSContext.enabled = value;
	}

	/**
	 * Verifies element namespaces and returns next possible entry to avoid override
	 */
	public Map.Entry<String, String> namespace(XMLElement element, String prefix, String namespace) {
		if (enabled) {
			Map<String, String> refs = reference(element, true, false);
			if (refs != null) {
				Map<String, String> namespaces = element.namespaces();
				String ns = namespaces.get(prefix);
				if (ns != null && !Objects.equals(ns, namespace)) {
					String next = next(namespaces.keySet(), prefix);
					if (!next.equals(prefix)) {
						refs.put(prefix, next);
						prefix = next;
					}
				}
			}
		}
		return new AbstractMap.SimpleEntry<>(prefix, namespace);
	}

	/**
	 * Returns new prefix value from stack of current loading session
	 */
	public String prefix(XMLElement element, String prefix) {
		if (!enabled) {
			return prefix;
		}
		return reference(element).getOrDefault(prefix, prefix);
	}

	private Map<String, String> reference(XMLElement element) {
		return reference(element, false, true);
	}

	private Map<String, String> reference(XMLElement element, boolean returnNull, boolean useParents) {
		Map<String, String> result = returnNull ? null : Collections.emptyMap();
		if (stack.containsKey(element)) {
			Stack<Map<String, String>> st = stack.get(element);
			if (!st.isEmpty()) {
				result = st.get(st.size() - 1);
			}
		} else if (element != null && useParents) {
			result = reference(element.getParent(), returnNull, useParents);
		}
		return result;
	}

	/**
	 * Pushes previous schema namespace references (or creates new if previous missed) in stack. Must be called BEFORE schema.parse
	 */
	public void push(Schema schema) {
		if (!enabled) {
			return;
		}

		stack.computeIfAbsent(
			schema, (k) -> new Stack<>()
		).push(new HashMap<>());
	}

	/**
	 * Pops current schema namespace references from stack. Must be called AFTER schema.parse
	 */
	public void pop(Schema schema) {
		if (!enabled) {
			return;
		}

		if (stack.containsKey(schema)) {
			stack.get(schema).pop();
		}
	}

	private static String next(Collection<String> prefixes, String prefix) {
		if (isEmpty(prefixes) || StringUtils.isEmpty(prefix)) {
			return prefix;
		}
		Integer seed = seed(prefix);
		String pfx = seed == null ? prefix : StringUtils.substringBeforeLast(prefix, String.valueOf(seed));
		String searchPattern = String.format("^%s$", seed != null ? String.format("%s[0-9]*", pfx) : pfx);
		boolean changed = false;
		for (String p : prefixes) {
			if (p == null) {
				continue;
			}
			if (!p.matches(searchPattern)) {
				continue;
			}
			Integer sd = seed(p);
			if (sd == null) {
				continue;
			}
			if (seed == null || sd >= seed) {
				changed = true;
				seed = sd;
			}
		}
		if (seed == null) {
			changed = true;
			seed = -1;
		}
		if (changed) {
			seed++;
		}
		return String.format("%s%d", pfx, seed);
	}

	private static Integer seed(String value) {
		Matcher matcher = SEED.matcher(value);
		return matcher.find() ? Integer.parseInt(matcher.group()) : null;
	}

	private static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}
}
