package fr.urssaf.image.sae.services.batch.suppression.support.lucene.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-suppressionmasse-test.xml" })
public class CheckRequeteLuceneTaskletTest {

   @Autowired
   @Qualifier("suppressionLauncher")
   private JobLauncherTestUtils launcher;
   
   private ExecutionContext context;
   
   @Before
   public void init() {
      context = new ExecutionContext();
      context.put(Constantes.SUPPRESSION_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
   }
   
   /**
    * Lancer un test avec une requete lucene valide
    * 
    */
   @Test
   public void testRequeteLuceneCorrecte() {

      String requete = "Siret:123456";
      
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.REQ_LUCENE_SUPPRESSION, new JobParameter(requete));

      JobParameters parameters = new JobParameters(mapParameter);
      
      JobExecution execution = launcher.launchStep(
            "controleRequeteLuceneStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      Assert.assertNotNull("La requete lucene doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_LUCENE_SUPPRESSION));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
   }
   
   /**
    * Lancer un test avec une requete lucene invalide
    * 
    */
   @Test
   public void testRequeteLuceneInvalide() {

      String requete = "Siret:123456 AND IdTraitementMasse:41882:050200023";
      
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.REQ_LUCENE_SUPPRESSION, new JobParameter(requete));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleRequeteLuceneStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      Assert.assertNull("La requete lucene ne doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_LUCENE_SUPPRESSION));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
      
      int nbDocsSupprimes = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_SUPPRIMES);
      Assert.assertEquals("Aucun document n'aurait du être supprimé", 0,
            nbDocsSupprimes);
   }
}
