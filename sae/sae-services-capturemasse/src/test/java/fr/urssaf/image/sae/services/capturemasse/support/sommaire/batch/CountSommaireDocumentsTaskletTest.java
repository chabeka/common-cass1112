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
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-test.xml" })
public class CountSommaireDocumentsTaskletTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   EcdeServices services;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private ExecutionContext context;

   private EcdeTestSommaire ecdeTestSommaire;

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Before
   public void init() {
      context = new ExecutionContext();
      context.put(Constantes.ID_TRAITEMENT, UUID.randomUUID().toString());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testFichierSommaireAucunDocument() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_sans_document.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      mapParameter.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep("compteElements",
            parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED
            .getExitCode(), step.getExitStatus().getExitCode());

   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testFichierSommaireFormatIncorrect() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_format_failure.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleFormatSommaireStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED, step
            .getExitStatus());

      ExecutionContext executionContext = execution.getExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) executionContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testFichierSommaireInexistant() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleFormatSommaireStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED_FIN_BLOQUANT attendu",
            "FAILED_FIN_BLOQUANT", step.getExitStatus().getExitCode());

      ExecutionContext executionContext = execution.getExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) executionContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testFichierSommaireDocumentEtDocumentVirtuel() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_avec_document_et_virtuel.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      mapParameter.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep("compteElements",
            parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED
            .getExitCode(), step.getExitStatus().getExitCode());

   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testFichierSommaireDocumentVirtuel() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      mapParameter.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep("compteElements",
            parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status VDOCS attendu", new ExitStatus("VDOCS")
            .getExitCode(), step.getExitStatus().getExitCode());

   }

}
