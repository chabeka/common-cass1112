package fr.urssaf.image.sae.services.capturemasse.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlValidationUtils;

@SuppressWarnings("PMD.MethodNamingConventions")
public class XmlValidationUtilsTest {

   private static final URL XSD_FILE;

   static {

      try {

         XSD_FILE = FileUtils.toURLs(new File[] { new File(
               "src/main/resources/xsd_som_res/sommaire.xsd") })[0];

      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void validate_success() throws ParserConfigurationException,
         SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");
      XmlValidationUtils.parse(xmlFile, XSD_FILE);
   }

   @Test
   public void validate_failure() throws ParserConfigurationException,
         IOException, SAXException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_format_failure.xml");
      try {
         XmlValidationUtils.parse(xmlFile, XSD_FILE);

         Assert.fail("une SAXParseException doit être levée");

      } catch (SAXParseException e) {

         Assert.assertEquals("le fichier " + xmlFile
               + " est incorrect sur la ligne 24", 24, e.getLineNumber());
      }
   }
}
