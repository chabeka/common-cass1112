package fr.urssaf.image.sae.commons.xml;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXParseException;

public class StaxValidateUtilsTest {

   @Test
   public void testSucces() {

      boolean succes = false;
      try {
         ClassPathResource xmlResource = new ClassPathResource(
               "xml/fichierLecture.xml");
         File xmlFile = xmlResource.getFile();

         ClassPathResource xsdResource = new ClassPathResource("xml/schema.xsd");
         URL xsdURL = xsdResource.getURL();

         StaxValidateUtils.parse(xmlFile, xsdURL);
         succes = true;

      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Assert.assertTrue("le traitement doit etre un succes", succes);

   }

   @Test
   public void testErreur() {

      boolean traitementOk = false;
      try {
         ClassPathResource xmlResource = new ClassPathResource(
               "xml/fichierErreur.xml");
         File xmlFile = xmlResource.getFile();

         ClassPathResource xsdResource = new ClassPathResource("xml/schema.xsd");
         URL xsdURL = xsdResource.getURL();

         StaxValidateUtils.parse(xmlFile, xsdURL);
         Assert.fail("une SaxParseException est attendue");

      } catch (SAXParseException exception) {
         traitementOk = true;

      } catch (Exception exception) {
         Assert.fail("une SAXParseException est attendue");
      }

      Assert.assertTrue("le traitement doit avoir echoué en SaxParseException",
            traitementOk);

   }

}
