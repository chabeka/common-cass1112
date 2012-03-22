/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-test.xml" })
public class CheckFileSommaireTaskletTest {

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
         // rien à faire
      }
   }

   /**
    * Lancer un test avec une URI dont le format incorrect
    * 
    * @throws Exception
    */
   @Test
   public void testUriFormatIncorrecte() throws Exception {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put(Constantes.SOMMAIRE, new JobParameter("abc"));
      parameters.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution execution = launcher.launchStep("controleSommaireStep",
            jobParameters);
      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED, step
            .getExitStatus());

      ExecutionContext context = execution.getExecutionContext();

      CaptureMasseSommaireDocumentException erreur = (CaptureMasseSommaireDocumentException) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertTrue("exception format incorrect attendue", (erreur
            .getCause() instanceof CaptureMasseSommaireEcdeURLException));
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testUriDomaineNonConnue() throws Exception {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters
            .put(
                  Constantes.SOMMAIRE,
                  new JobParameter(
                        "ecde://cnp07devecde.cer69.recouv/SAE_INTEGRATION/20110822/"
                              + "CaptureUnitaire-102-CaptureUnitaire-OK-EnrichissementEcrasement/"
                              + "documents/doc1.PDF"));
      parameters.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution execution = launcher.launchStep("controleSommaireStep",
            jobParameters);
      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED, step
            .getExitStatus());

      ExecutionContext context = execution.getExecutionContext();

      CaptureMasseSommaireDocumentException erreur = (CaptureMasseSommaireDocumentException) context
            .get(Constantes.DOC_EXCEPTION);

      Assert
            .assertTrue(
                  "exception URI incorrecte attendue",
                  (erreur.getCause() instanceof CaptureMasseSommaireEcdeURLException));
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testUriDomaineConnuFichierInexistant() throws Exception {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      parameters.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution execution = launcher.launchStep("controleSommaireStep",
            jobParameters);
      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED, step
            .getExitStatus());

      ExecutionContext context = execution.getExecutionContext();

      CaptureMasseSommaireDocumentException erreur = (CaptureMasseSommaireDocumentException) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertTrue("exception fichier inexistant attendue", (erreur
            .getCause() instanceof CaptureMasseSommaireFileNotFoundException));
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   @Ignore
   public void testUriDomaineConnuFichierExistantEcritureImpossible()
         throws Exception {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put(Constantes.SOMMAIRE, new JobParameter(
            "ecde://ecde.testunitaire.recouv/SAE_INTEGRATION/20110822"
                  + "/lectureSeule/sommaire.xml"));
      parameters.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution execution = launcher.launchStep("controleSommaireStep",
            jobParameters);
      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED, step
            .getExitStatus());

      ExecutionContext context = execution.getExecutionContext();

      CaptureMasseSommaireDocumentException erreur = (CaptureMasseSommaireDocumentException) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertTrue("exception ecriture impossible attendue", (erreur
            .getCause() instanceof CaptureMasseEcdeWriteFileException));
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testUriDomaineConnuFichierExistant() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);
      
      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put(Constantes.SOMMAIRE, new JobParameter(
            ecdeTestSommaire.getUrlEcde().toString()));
      parameters.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution execution = launcher.launchStep("controleSommaireStep",
            jobParameters);
      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());

   }
}
