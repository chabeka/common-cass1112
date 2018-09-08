package fr.urssaf.image.sae.services.batch.transfertmasse.support.resultats;

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
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.common.Constantes.BATCH_MODE;
import fr.urssaf.image.sae.services.batch.transfert.support.resultats.ResultatFileSuccessTransfertSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class ResultatsFileSuccessTransfertSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ResultatFileSuccessTransfertSupport support;

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

   @Test
   public void testWriteFileSuccess() {

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      support.writeResultatsFile(ecdeDirectory,
            new ConcurrentLinkedQueue<TraitementMasseIntegratedDocument>(), 10,
            false, null, BATCH_MODE.PARTIEL.name());

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
         ClassPathResource resSommaire = new ClassPathResource(
               "sommaire/sommaire_success_transfert.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         // Liste des documents intégrés
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> clq = new ConcurrentLinkedQueue<TraitementMasseIntegratedDocument>();
         TraitementMasseIntegratedDocument doc1 = new TraitementMasseIntegratedDocument();
         doc1.setDocumentFile(null);
         doc1.setIdentifiant(UUID.randomUUID());
         doc1.setIndex(0);
         clq.add(doc1);
         TraitementMasseIntegratedDocument doc2 = new TraitementMasseIntegratedDocument();
         doc2.setDocumentFile(null);
         doc2.setIdentifiant(UUID.randomUUID());
         doc2.setIndex(1);
         clq.add(doc2);
         TraitementMasseIntegratedDocument doc3 = new TraitementMasseIntegratedDocument();
         doc3.setDocumentFile(null);
         doc3.setIdentifiant(UUID.randomUUID());
         doc3.setIndex(2);
         clq.add(doc3);

         support.writeResultatsFile(ecdeDirectory, clq, 10, true, sommaire,
               BATCH_MODE.PARTIEL.name());

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
