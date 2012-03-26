/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAECaptureMasseTest {

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools tools;

   private EcdeTestSommaire testSommaire;

   @Before
   public void init() {
      testSommaire = tools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         tools.cleanEcdeTestSommaire(testSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test
   @Ignore
   public void testLancementService() {

      try {
         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         File repertoireEcdeDocuments = new File(testSommaire.getRepEcde(),
               "documents");
         ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
         File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
         fos = new FileOutputStream(fileAttestation1);
         IOUtils.copy(resAttestation1.getInputStream(), fos);

         ExitTraitement exitTraitement = service.captureMasse(testSommaire
               .getUrlEcde(), UUID.randomUUID());

         Assert.assertFalse("l'opération doit etre en erreur", exitTraitement
               .isSucces());

      } catch (Exception e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
}
