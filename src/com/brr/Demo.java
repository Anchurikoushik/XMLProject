package com.brr;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Scanner;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.stream.StreamSource;

public class Demo {

   public static void main(String[] args) {
      try {
         Scanner scanner = new Scanner(System.in);

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc;

         File xmlFile = new File("src/Cars.xml");
         String xsdFilePath = "src/Cars.xsd";

        
         if (xmlFile.exists()) {
           
            doc = dBuilder.parse(xmlFile);
         } else {
            
            doc = dBuilder.newDocument();

            
            Element rootElement = doc.createElement("cars");
            doc.appendChild(rootElement);
         }

        
         System.out.print("Enter the company name: ");
         String company = scanner.nextLine();

        
         Element supercar;
         if (doc.getElementsByTagName("supercars").getLength() > 0) {
            supercar = (Element) doc.getElementsByTagName("supercars").item(0);
         } else {
            supercar = doc.createElement("supercars");
            doc.getDocumentElement().appendChild(supercar);
         }

       
         Attr attr = doc.createAttribute("company");
         attr.setValue(company);
         supercar.setAttributeNode(attr);

        
         for (int i = 1; i <= 2; i++) {
            Element carname = doc.createElement("carname");
            Attr attrType = doc.createAttribute("type");

            System.out.print("Enter carname " + i + ": ");
            String carNameValue = scanner.nextLine();

            System.out.print("Enter car type for carname " + i + ": ");
            String carTypeValue = scanner.nextLine();

            attrType.setValue(carTypeValue);
            carname.setAttributeNode(attrType);
            carname.appendChild(doc.createTextNode(carNameValue));
            supercar.appendChild(carname);
         }

       
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(xmlFile);
         transformer.transform(source, result);

         System.out.println("XML data appended to 'Cars.xml.'");

         
         SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
         Schema schema = schemaFactory.newSchema(new File(xsdFilePath));
         Validator validator = schema.newValidator();
         validator.validate(new StreamSource(xmlFile));
         System.out.println("Validation successful: Cars.xml is valid against the schema.");

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
