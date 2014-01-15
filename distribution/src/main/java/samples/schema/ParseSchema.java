/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package samples.schema;

import com.predic8.schema.Appinfo;
import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexContent;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Derivation;
import com.predic8.schema.Documentation;
import com.predic8.schema.Element;
import com.predic8.schema.Import;
import com.predic8.schema.Include;
import com.predic8.schema.ModelGroup;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.SchemaParser;
import com.predic8.schema.SimpleType;

public class ParseSchema {

	public static void main(String[] args) {
		SchemaParser parser = new SchemaParser();
		Schema schema = parser.parse("samples/xsd/human-resources.xsd");

		out("-------------- Schema Information --------------");
		out("  Schema TargetNamespace: " + schema.getTargetNamespace());
		out("  AttributeFormDefault: " + schema.getAttributeFormDefault());
		out("  ElementFormDefault: " + schema.getElementFormDefault());
		out("");

		if (schema.getImports().size() > 0) {
			out("  Schema Imports: ");
			for (Import imp : schema.getImports()) {
				out("    Import Namespace: " + imp.getNamespace());
				out("    Import Location: " + imp.getSchemaLocation());
			}
			out("");
		}

		if (schema.getIncludes().size() > 0) {
			out("  Schema Includes: ");
			for (Include inc : schema.getIncludes()) {
				out("    Include Location: " + inc.getSchemaLocation());
			}
			out("");
		}

		out("  Schema Elements: ");
		for (Element e : schema.getAllElements()) {
			out("    Element Name: " + e.getName());
			if (e.getType() != null) {
				/*
				 * schema.getType() delivers a TypeDefinition (SimpleType orComplexType)
				 * object.
				 */
				out("    Element Type Name: " + schema.getType(e.getType()).getName());
				out("    Element minOccurs: " + e.getMinOccurs());
				out("    Element maxOccurs: " + e.getMaxOccurs());
				if (e.getAnnotation() != null)
					annotationOut(e);
			}
		}
		out("");

		out("  Schema ComplexTypes: ");
		for (ComplexType ct : schema.getComplexTypes()) {
			out("    ComplexType Name: " + ct.getName());
			if (ct.getAnnotation() != null)
				annotationOut(ct);
			if (ct.getAttributes().size() > 0) {
				out("    ComplexType Attributes: ");
				/*
				 * If available, attributeGroup could be read as same as attribute in
				 * the following.
				 */
				for (Attribute attr : ct.getAttributes()) {
					out("      Attribute Name: " + attr.getName());
					out("      Attribute Form: " + attr.getForm());
					out("      Attribute ID: " + attr.getId());
					out("      Attribute Use: " + attr.getUse());
					out("      Attribute FixedValue: " + attr.getFixedValue());
					out("      Attribute DefaultValue: " + attr.getDefaultValue());
				}
			}
			/*
			 * ct.getModel() delivers the child element used in complexType. In case
			 * of 'sequence' you can also use the getSequence() method.
			 */
			out("    ComplexType Model: " + ct.getModel().getClass().getSimpleName());
			if (ct.getModel() instanceof ModelGroup) {
				out("    Model Particles: ");
				for (SchemaComponent sc : ((ModelGroup) ct.getModel()).getParticles()) {
					out("      Particle Kind: " + sc.getClass().getSimpleName());
					out("      Particle Name: " + sc.getName() + "\n");
				}
			}

			if (ct.getModel() instanceof ComplexContent) {
				Derivation der = ((ComplexContent) ct.getModel()).getDerivation();
				out("      ComplexConten Derivation: " + der.getClass().getSimpleName());
				out("      Derivation Base: " + der.getBase());
			}

			if (ct.getAbstractAttr() != null)
				out("    ComplexType AbstractAttribute: " + ct.getAbstractAttr());
			if (ct.getAnyAttribute() != null)
				out("    ComplexType AnyAttribute: " + ct.getAnyAttribute());

			out("");
		}

		if (schema.getSimpleTypes().size() > 0) {
			out("  Schema SimpleTypes: ");
			for (SimpleType st : schema.getSimpleTypes()) {
				out("    SimpleType Name: " + st.getName());
				out("    SimpleType Restriction: " + st.getRestriction());
				out("    SimpleType Union: " + st.getUnion());
				out("    SimpleType List: " + st.getList());
			}
		}
	}

	private static void annotationOut(SchemaComponent sc) {
		if (sc.getAnnotation().getAppinfos().size() > 0) {
			System.out
			    .print("    Annotation (appinfos) available with the content: ");
			for (Appinfo appinfo : sc.getAnnotation().getAppinfos()) {
				out(appinfo.getContent());
			}
		} else {
			System.out
			    .print("    Annotation (documentation) available with the content: ");
			for (Documentation doc : sc.getAnnotation().getDocumentations()) {
				out(doc.getContent());
			}
		}
	}

	private static void out(String str) {
		System.out.println(str);
	}
}
