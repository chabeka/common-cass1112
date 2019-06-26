package fr.urssaf.image.sae.client.vi;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe d'assertion pour les tests sur les reponses des web services
 */
public final class AssertXML {

   private AssertXML() {

   }

   /**
    * @param expected
    *           balise à vérifier
    * @param namespaceURI
    *           namespaceURI de la balise
    * @param localName
    *           localName de la balise
    * @param actual
    *           reponse du web service
    */
   public static void assertElementContent(final String expected,
                                           final String namespaceURI, final String localName, final String actual) {

      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      try {
         final Document doc = dbf.newDocumentBuilder()
                                 .parse(
                                        IOUtils.toInputStream(actual));

         assertEquals(expected,
                      doc.getElementsByTagNameNS(namespaceURI,
                                                 localName)
                         .item(0)
                         .getTextContent());

      }
      catch (final SAXException e) {
         throw new IllegalStateException(e);
      }
      catch (final ParserConfigurationException e) {
         throw new IllegalStateException(e);
      }
      catch (final IOException e) {
         throw new IllegalStateException(e);
      }

   }

}
