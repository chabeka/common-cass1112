package fr.urssaf.image.commons.webservice.axis.client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.XmlStreamWriter;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Classe de manipulation de {@link Transformer}
 * 
 * 
 */
public final class TransformerUtils {

   private TransformerUtils() {

   }

   private static Element parse(String xml) {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);

      try {
         DocumentBuilder builder = factory.newDocumentBuilder();
         InputStream input = new ByteArrayInputStream(xml.getBytes());
         return builder.parse(input).getDocumentElement();

      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      } catch (ParserConfigurationException e) {
         throw new IllegalArgumentException(e);
      } catch (SAXException e) {
         throw new IllegalArgumentException(e);
      }
   }

   /**
    * indentation du XML sous forme d'une chaine de caractère
    * 
    * @param xml
    *           le XML non indenté
    * @return le même XML mais indenté
    */
   public static String print(String xml) {

      TransformerFactory factory = TransformerFactory.newInstance();

      Transformer transformer;
      try {
         transformer = factory.newTransformer();
      } catch (TransformerConfigurationException e) {
         throw new IllegalArgumentException(e);
      }

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(
            "{http://xml.apache.org/xslt}indent-amount", "4");

      try {

         ByteArrayOutputStream out = new ByteArrayOutputStream();

         transformer.transform(new DOMSource(parse(xml)), new StreamResult(
               new XmlStreamWriter(out, "UTF-8")));

         return out.toString();
      } catch (TransformerException e) {
         throw new IllegalStateException(e);
      }

   }
}
