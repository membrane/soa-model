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

import java.util.List;
import com.predic8.schema.*;
import static com.predic8.schema.Schema.*;
import com.predic8.soamodel.Consts;

public class ListSchemaElements {

  Schema schema;
  
  public static void main(String[] args) {
    
    (new ListSchemaElements()).dump();
    
  }

  private void dump() {
    SchemaParser parser = new SchemaParser();
    schema = parser.parse("samples/xsd/human-resources.xsd");
    System.out.println(schema.getTargetNamespace());
    for(Element element : schema.getElements()){
      dump(element);
    }
    for(ComplexType ct : schema.getComplexTypes()){
      dump(ct);
    }
    
    Element ssize = schema.newElement("shoesize", INT);
    ComplexType ct = schema.getComplexType("PersonType");
    Sequence seq = ct.getSequence();
    if(seq == null) throw new RuntimeException("No Sequence in ComplexType PersonType!");
    seq.getParticles().add(ssize);
    dump(ct);
  }

  private void dump(ComplexType ct) {
    System.out.println(ct.getName());
    System.out.print("Attributes: ");
    for(Attribute attr : ct.getAttributes()){
      System.out.println(attr.getName());
    }
    if(ct.getModel() instanceof Sequence){
      Sequence seq = (Sequence) ct.getModel();
      
      for(Element em : (List<Element>) seq.getElements()){
        dump(em);
      }
    }
  }

  void dump(TypeDefinition td){
    System.out.println(td.getName());
  }
  
  private void dump(Element em) {
    System.out.print(em.getName());
    if(em.getEmbeddedType() != null){
      dump(em.getEmbeddedType());
    } else {
      if(em.getType().getNamespaceURI() == Consts.SCHEMA_NS) {
        System.out.println(", type= 'xsd:"+em.getType().getLocalPart()+"'");
        } else {
          System.out.println(", type= 'tns:"+em.getType().getLocalPart()+"'");
        }
    }
  }
}
