package fr.urssaf.image.sae.ecde.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType;
import fr.urssaf.image.sae.ecde.service.SommaireXmlService;
/**
 * Classe permettant de tester l'implémentation
 * des méthodes de la classe SommaireXmlServiceImpl
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings({"PMD.MethodNamingConventions", "PMD.UncommentedEmptyMethod", "PMD.AvoidDuplicateLiterals"})
public class SommaireXmlServiceImplTest {

   @Autowired
   private SommaireXmlService service;
   
   @BeforeClass
   public static void init() throws IOException {
   }
   // Test avec succes -- lecture du fichier sommaire-test001.xml 
   @Test
   public void readSommaireXml_success_file() throws EcdeXsdException, JAXBException, IOException {
      ClassPathResource classPath = new ClassPathResource("sommaire/sommaire-test001.xml");
      File file = classPath.getFile();
      SommaireType sommaire = service.readSommaireXml(file);
      assertEquals("message inattendu", BatchModeType.TOUT_OU_RIEN, sommaire.getBatchMode());
      assertEquals("message inattendu", "La description du traitement", sommaire.getDescription());
      assertEquals("message inattendu", 2, sommaire.getDocuments().getDocument().size());
      assertEquals("message inattendu", 2, sommaire.getDocumentsVirtuels().getDocumentVirtuel().size());
   }
   // Test avec succes -- lecture du fichier sommaire-test001.xml 
   @Test
   public void readSommaireXml_success_inputStream() throws EcdeXsdException, JAXBException, IOException {
      ClassPathResource classPath = new ClassPathResource("sommaire/sommaire-test001.xml");
      InputStream input = classPath.getInputStream();
      SommaireType sommaire = service.readSommaireXml(input);
      assertEquals("message inattendu", BatchModeType.TOUT_OU_RIEN, sommaire.getBatchMode());
      assertEquals("message inattendu", "La description du traitement", sommaire.getDescription());
      assertEquals("message inattendu", 2, sommaire.getDocuments().getDocument().size());
      assertEquals("message inattendu", 2, sommaire.getDocumentsVirtuels().getDocumentVirtuel().size());
   }
   //------------------------ FAILURE
   // Erreur de strucutre dans sommaire.xml - absence de l'element batchMode par conséquent ne respecte pas sommaire.xsd
   @Test
   public void readSommaireXml_failure_file() throws JAXBException, IOException {
     try{
         ClassPathResource classPath = new ClassPathResource("sommaire/sommaire-test002.xml");
         File file = classPath.getFile();
         @SuppressWarnings("unused")
         SommaireType sommaire = service.readSommaireXml(file);
         fail("Une exception était attendue! " + EcdeXsdException.class);
      }catch (EcdeXsdException e) {
         assertEquals("message inattendu","Une erreur de structure a été détectée sur le 'sommaire.xml'.",e.getMessage());
      }
   }
   @Test
   public void readSommaireXml_failure_inputStream() throws JAXBException, IOException {
     try{
         ClassPathResource classPath = new ClassPathResource("sommaire/sommaire-test002.xml");
         InputStream input = classPath.getInputStream();
         @SuppressWarnings("unused")
         SommaireType sommaire = service.readSommaireXml(input);
         fail("Une exception était attendue! " + EcdeXsdException.class);
      }catch (EcdeXsdException e) {
         assertEquals("message inattendu","Une erreur de structure a été détectée sur le 'sommaire.xml'.",e.getMessage());
      }
   }
}
