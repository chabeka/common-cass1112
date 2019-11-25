
package sae.integration.manual;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.XMLHelper;
import sae.integration.xml.modele.ResultatsType;

/**
 * Tests de parsing XML
 */
public class XMLTest {
   private static final Logger LOGGER = LoggerFactory.getLogger(XMLTest.class);

   @Test
   public void parsingTest() throws Exception {
      final String xml = getResultatsXML();

      LOGGER.info("Contenu du fichier resultats.xml :\r\n{}", xml);
      final ResultatsType resultats = XMLHelper.parseResultatsXML(xml);
      Assert.assertEquals(3, (int) resultats.getIntegratedDocumentsCount());
   }

   public String getResultatsXML() {
      return "<resultats xmlns=\"http://www.cirtil.fr/sae/resultatsXml\" xmlns:ns2=\"http://www.cirtil.fr/sae/commun_sommaire_et_resultat\">\r\n" +
            "    <batchMode>PARTIEL</batchMode>\r\n" +
            "    <initialDocumentsCount>3</initialDocumentsCount>\r\n" +
            "    <integratedDocumentsCount>3</integratedDocumentsCount>\r\n" +
            "    <nonIntegratedDocumentsCount>0</nonIntegratedDocumentsCount>\r\n" +
            "    <initialVirtualDocumentsCount>0</initialVirtualDocumentsCount>\r\n" +
            "    <integratedVirtualDocumentsCount>0</integratedVirtualDocumentsCount>\r\n" +
            "    <nonIntegratedVirtualDocumentsCount>0</nonIntegratedVirtualDocumentsCount>\r\n" +
            "    <nonIntegratedDocuments/>\r\n" +
            "    <nonIntegratedVirtualDocuments/>\r\n" +
            "</resultats>";
   }

}