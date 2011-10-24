package fr.urssaf.image.sae.webservice.client.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.XmlStreamWriter;

/**
 * Classe utilitaire des fichiers xml
 * 
 * 
 */
public final class XMLUtils {

   private XMLUtils() {

   }

   /**
    * Affichage d'un contenu XML<br>
    * L'affichage est indenté
    * 
    * @param input
    *           contenu XML
    * @return chaine de caractère du contenu du fichier XML
    */
   public static String print(InputStream input) {

      try {
         TransformerFactory factory = TransformerFactory.newInstance();

         Transformer transformer = factory.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty(
               "{http://xml.apache.org/xslt}indent-amount", "4");
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         transformer.transform(new StreamSource(input), new StreamResult(
               new XmlStreamWriter(out, "UTF-8")));

         return out.toString();
      } catch (TransformerException e) {

         throw new IllegalStateException(e);
      }
   }
}
