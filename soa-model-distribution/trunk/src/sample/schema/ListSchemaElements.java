package sample.schema;

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
    schema = parser.parse("resources/human-resources.xsd");
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
