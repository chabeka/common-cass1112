package fr.urssaf.image.sae.services.capturemasse.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.util.XmlReadUtils;

public class XmlReadUtilsTest {

   @Test
   public void validateSuccess() throws ParserConfigurationException,
         SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      int nbre = XmlReadUtils.compterElements(xmlFile, "document");

      Assert.assertEquals("il doit y avoir 3 documents", 3, nbre);

   }

   @Test(expected = CaptureMasseRuntimeException.class)
   public void validateFichierInexistant() throws ParserConfigurationException,
         SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_inexistant.xml");

      int nbre = XmlReadUtils.compterElements(xmlFile, "document");

      Assert.assertEquals("il doit y avoir 3 documents", 3, nbre);

   }

   @Test
   public void validate_baliseInexistante()
         throws ParserConfigurationException, SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      int nbre = XmlReadUtils.compterElements(xmlFile, "balise");

      Assert.assertEquals("il doit y avoir 0 documents", 0, nbre);

   }

   @Test
   public void validateSuccessGetElementValue()
         throws ParserConfigurationException, SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      String res = XmlReadUtils.getElementValue(xmlFile, "batchMode");

      Assert.assertEquals("Le résultat doit être TOUT_OU_RIEN", "TOUT_OU_RIEN",
            res);
   }
   
   @Test(expected = CaptureMasseRuntimeException.class)
   public void validateFichierInexistantGetElementValue()
         throws ParserConfigurationException, SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_inexistant.xml");

      XmlReadUtils.getElementValue(xmlFile, "batchMode");

   }
   
   @Test
   public void validate_baliseInexistanteGetElementValue()
         throws ParserConfigurationException, SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      String res = XmlReadUtils.getElementValue(xmlFile, "balise");

      Assert.assertEquals("Le résultat doit être null", null, res);

   }

   @Test
   public void validate_baliseNonTexteGetElementValue()
         throws ParserConfigurationException, SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      String res = XmlReadUtils.getElementValue(xmlFile, "objetNumerique");

      Assert.assertEquals("Le résultat doit être null", null, res);

   }   
   
}
