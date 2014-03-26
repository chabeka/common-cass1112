/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.nfs;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class ControleStepsNFSTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

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
   public void debutTraitementTest() {
      nfsConnexionTest("debutTraitement");
   }

   @Test
   public void controleFormatSommaireStepTest() {
      nfsConnexionTest("controleFormatSommaireStep");
   }

   @Test
   public void compteElementsTest() {
      nfsConnexionTest("compteElements");
   }

   @Test
   public void controleDocumentsTest() {
      nfsConnexionTest("controleDocuments");
   }

   @Test
   public void persistanceDocumentsTest() {
      nfsConnexionTest("persistanceDocuments");
   }

   @Test
   public void finSuccesTest() {
      nfsConnexionTest("finSucces");
   }

   @Test
   public void finTraitementTest() {
      nfsConnexionTest("finTraitement");
   }

   @Test
   public void controleFichiersReferenceTest() {
      nfsConnexionTest("controleFichiersReference");
   }

   @Test
   public void controleDocumentsVirtuelsTest() {
      nfsConnexionTest("controleDocumentsVirtuels");
   }

   @Test
   public void persistanceFichiersReferenceTest() {
      nfsConnexionTest("persistanceFichiersReference");
   }

   @Test
   public void finErreurTest() {
      nfsConnexionTest("finErreur");
   }

   @Test
   public void persistanceDocumentsVirtuelsTest() {
      nfsConnexionTest("persistanceDocumentsVirtuels");
   }

   @Test
   public void finErreurVirtuelTest() {
      nfsConnexionTest("finErreurVirtuel");
   }

   @Test
   public void finSuccesVirtuelTest() {
      nfsConnexionTest("finSuccesVirtuel");
   }

   private void nfsConnexionTest(String stepToFail) {

      ExecutionContext context = new ExecutionContext();
      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());

      JobExecution execution = launcher.launchStep(stepToFail, context);

      context = execution.getExecutionContext();

      Assert.assertNotNull("Une exception doit etre presente dans le context",
            context.get(Constantes.DOC_EXCEPTION));

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

   }
}
