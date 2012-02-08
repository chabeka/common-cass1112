package fr.urssaf.image.sae.ecde.service.validation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.service.SommaireXmlService;
/**
 * Classe permettant de tester l'aspect sur la validation des paramètres d'entree
 * des méthodes de la classe SommaireXmlServiceValidation
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings({"PMD.MethodNamingConventions", "PMD.UncommentedEmptyMethod","PMD.AvoidDuplicateLiterals"})
public class SommaireXmlServiceValidationTest {

   @Autowired
   private SommaireXmlService service;
   
   @BeforeClass
   public static void init() throws URISyntaxException, FileNotFoundException {
   }
   
   // inputStream à null
   @Test
   public void readSommaireXml_failure_inputstream() throws EcdeXsdException {
      try {
         InputStream input1 = null;
         service.readSommaireXml(input1);
      }catch (IllegalArgumentException e) {
         assertEquals("message inattendu", "L'argument flux doit être renseigné.", e.getMessage());
      }
   }
   
   // inputFile à null
   @Test
   public void readSommaireXml_failure_file() throws EcdeXsdException {
      try {
         File inputFile = null;
         service.readSommaireXml(inputFile);
      }catch (IllegalArgumentException e) {
         assertEquals("message inattendu", "L'argument fichier doit être renseigné.", e.getMessage());
      }
   }   
   
   
}
