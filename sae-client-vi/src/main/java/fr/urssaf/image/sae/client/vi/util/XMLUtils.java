package fr.urssaf.image.sae.client.vi.util;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;

/**
 * Méthodes utilitaires liées à la gestion du XML
 */
public class XMLUtils {

   /**
    * Il s'agit d'une classe statique
    */
   private XMLUtils() {

   }

   /**
    * Parse une chaîne XML en renvoie un Document XML
    * 
    * @param xmlString
    *           : la chaîne XML
    * @return : le document XML
    */
   public static Document createXMLDocumentFromString(final String xmlString) {

      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      // true si l'analyseur généré prend en charge les espaces de noms XML
      factory.setNamespaceAware(true);
      // API to obtain DOM Document instance
      DocumentBuilder builder = null;
      try {
         // Create DocumentBuilder with default configuration
         builder = factory.newDocumentBuilder();

         // Parse the content to Document object
         return builder.parse(new InputSource(new StringReader(xmlString)));
      }
      catch (final Exception e) {
         throw new ViSignatureException(e);
      }
   }

}
