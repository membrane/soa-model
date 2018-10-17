package com.predic8.wstool.creator;

import java.util.ArrayList;
import java.util.List;

public class TemplateElementInfo {

	public String type;
	public List<String> possibleValues = new ArrayList<String>();
	public String pattern;

	public String maxLength;
	public String maxDigits;
	public String fractionDigits;
	public String minLength;

	public String minInclusive;
	public String minExclisive;
	public String maxInclusive;
	public String maxExclusive;
	public String minOccurs;
	public String maxOccurs;

}
