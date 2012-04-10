package fr.urssaf.image.sae.utils;

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
   public void validate_success() throws ParserConfigurationException,
         SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      int nbre = XmlReadUtils.compterElements(xmlFile, "document");

      Assert.assertEquals("il doit y avoir 3 documents", 3, nbre);

   }
   
   @Test(expected=CaptureMasseRuntimeException.class)
   public void validate_fichier_inexistant() throws ParserConfigurationException,
         SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_inexistant.xml");

      int nbre = XmlReadUtils.compterElements(xmlFile, "document");

      Assert.assertEquals("il doit y avoir 3 documents", 3, nbre);

   }
   
   
   @Test
   public void validate_baliseInexistante() throws ParserConfigurationException,
         SAXException, IOException {

      File xmlFile = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      int nbre = XmlReadUtils.compterElements(xmlFile, "balise");

      Assert.assertEquals("il doit y avoir 0 documents", 0, nbre);

   }
}
