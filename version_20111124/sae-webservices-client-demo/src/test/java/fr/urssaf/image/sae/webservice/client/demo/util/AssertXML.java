package fr.urssaf.image.sae.webservice.client.demo.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe d'assertion pour les tests sur les reponses des web services
 * 
 * 
 */
public final class AssertXML {

   private AssertXML() {

   }

   /**
    * 
    * @param expected
    *           balise à vérifier
    * @param namespaceURI
    *           namespaceURI de la balise
    * @param localName
    *           localName de la balise
    * @param actual
    *           reponse du web service
    */
   public static void assertElementContent(String expected, String namespaceURI,
         String localName, String actual) {

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      try {
         Document doc = dbf.newDocumentBuilder().parse(
               IOUtils.toInputStream(actual));

         assertEquals(expected, doc.getElementsByTagNameNS(namespaceURI,
               localName).item(0).getTextContent());

      } catch (SAXException e) {
         throw new IllegalStateException(e);
      } catch (ParserConfigurationException e) {
         throw new IllegalStateException(e);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }

   }
}
