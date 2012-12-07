/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatFileSuccessSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ResultatFileSuccessSupport support;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   // Vérification Ecde non null
   @Test(expected = IllegalArgumentException.class)
   public void testEcdeDirectoryObligatoire() {
      support.writeResultatsFile(null,
            new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>(), 0,
            false, null);
      Assert.fail("Vérification aspect doit se déclencher");
   }

   // Vérification nombre de documents positif
   @Test(expected = IllegalArgumentException.class)
   public void testDocumentsCountObligatoire() {
      support.writeResultatsFile(new File("fichier"),
            new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>(), -1,
            false, null);
      Assert.fail("Vérification aspect doit se déclencher");
   }

   // Vérifie que sommaireFile est renseigné si restitutionUuids est à true
   @Test(expected = IllegalArgumentException.class)
   public void testSommaireFileObligatorie() {
      support.writeResultatsFile(new File("fichier"),
            new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>(), 10,
            true, null);
      Assert.fail("Vérification aspect doit se déclencher");

   }

   @Test
   public void testWriteFileSuccess() {

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      support.writeResultatsFile(ecdeDirectory,
            new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>(), 10,
            false, null);

      File resultatsFile = new File(ecdeDirectory, "resultats.xml");

      Assert.assertTrue("le fichier resultats.xml doit exister", resultatsFile
            .exists());
      Assert.assertTrue("Le fichier doit avoir une taille > 0", resultatsFile
            .length() > 0);

   }

   @Test
   public void testWriteFileWithUuidsSuccess() {
      try {
         File ecdeDirectory = ecdeTestSommaire.getRepEcde();
         
         // Fichier sommaire.xml
         File sommaire = new File(ecdeDirectory, "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire/sommaire_success.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);
         
         // Liste des documents intégrés
         ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> clq = new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>();
         CaptureMasseIntegratedDocument doc1 = new CaptureMasseIntegratedDocument();
         doc1.setDocumentFile(null);
         doc1.setIdentifiant(UUID.randomUUID());
         doc1.setIndex(0);
         clq.add(doc1);
         CaptureMasseIntegratedDocument doc2 = new CaptureMasseIntegratedDocument();
         doc2.setDocumentFile(null);
         doc2.setIdentifiant(UUID.randomUUID());
         doc2.setIndex(1);
         clq.add(doc2);
         CaptureMasseIntegratedDocument doc3 = new CaptureMasseIntegratedDocument();
         doc3.setDocumentFile(null);
         doc3.setIdentifiant(UUID.randomUUID());
         doc3.setIndex(2);
         clq.add(doc3);
         
         support.writeResultatsFile(ecdeDirectory,
               clq, 10,
               true, sommaire);

         File resultatsFile = new File(ecdeDirectory, "resultats.xml");

         Assert.assertTrue("le fichier resultats.xml doit exister",
               resultatsFile.exists());
         Assert.assertTrue("Le fichier doit avoir une taille > 0",
               resultatsFile.length() > 0);
      } catch (FileNotFoundException e) {
         Assert.fail("le fichier sommaire.xml doit être valide");
      } catch (IOException e) {
         Assert.fail("le fichier sommaire.xml doit être valide");
      }

   }

}
