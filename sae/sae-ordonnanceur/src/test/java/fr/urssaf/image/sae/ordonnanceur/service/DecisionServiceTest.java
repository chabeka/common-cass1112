package fr.urssaf.image.sae.ordonnanceur.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class DecisionServiceTest {

   private static final String CAPTURE_MASSE_JN = "capture_masse";

   private static final String CER69_SOMMAIRE = "ecde://ecde.cer69.recouv/sommaire.xml";

   private static final String CER44_SOMMAIRE = "ecde://ecde.cer44.recouv/sommaire.xml";

   private static final String CONTEXT_HOST = "serveur";

   private static final String HOST_VALUE;

   static {

      try {
         HOST_VALUE = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new NestableRuntimeException(e);
      }
   }

   @Autowired
   private DecisionService decisionService;

   @Autowired
   private DFCESupport dfceSuppport;

   @After
   public void after() {

      EasyMock.reset(dfceSuppport);
   }

   @Test
   public void decisionService_success() throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(true);

      EasyMock.replay(dfceSuppport);

      List<JobExecution> jobsEnCours = new ArrayList<JobExecution>();

      jobsEnCours.add(createJobExecutionCaptureMasse(528, CAPTURE_MASSE_JN,
            CONTEXT_HOST, "ecde.cer44.recouv"));
      jobsEnCours.add(createJobExecutionCaptureMasse(435, "autre_traitement",
            CONTEXT_HOST, "ecde.cer69.recouv"));

      List<JobInstance> jobsEnAttente = new ArrayList<JobInstance>();

      jobsEnAttente.add(createJobCaptureMasse(458, CAPTURE_MASSE_JN,
            CER69_SOMMAIRE));
      jobsEnAttente.add(createJobCaptureMasse(202, CAPTURE_MASSE_JN,
            CER44_SOMMAIRE));
      jobsEnAttente.add(createJobCaptureMasse(256, CAPTURE_MASSE_JN,
            CER69_SOMMAIRE));
      jobsEnAttente.add(createJobCaptureMasse(607, CAPTURE_MASSE_JN,
            CER44_SOMMAIRE));

      JobInstance job = decisionService.trouverJobALancer(jobsEnAttente,
            jobsEnCours);

      Assert.assertEquals("l'identifiant du job à lancer est inattendu", Long
            .valueOf(256), job.getId());

      EasyMock.verify(dfceSuppport);

   }

   /**
    * DFCE est down
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_dfceIsDown()
         throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(false);

      EasyMock.replay(dfceSuppport);

      List<JobExecution> jobsEnCours = new ArrayList<JobExecution>();

      List<JobInstance> jobsEnAttente = new ArrayList<JobInstance>();

      jobsEnAttente.add(createJobCaptureMasse(458, CAPTURE_MASSE_JN,
            CER69_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement en attente
    * 
    */
   @Test
   public void decisionService_failure_noJobEnAttente_noJob() {

      List<JobExecution> jobsEnCours = new ArrayList<JobExecution>();

      try {

         decisionService.trouverJobALancer(null, jobsEnCours);

         Assert
               .fail("une exception de type AucunJobALancerException doit être levée");

      } catch (AucunJobALancerException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Il n'y a aucun traitement à lancer", e.getMessage());
      }

   }

   /**
    * aucun traitement de capture en masse en attente
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_nojobCaptureMasse()
         throws AucunJobALancerException {

      List<JobExecution> jobsEnCours = new ArrayList<JobExecution>();
      List<JobInstance> jobsEnAttente = new ArrayList<JobInstance>();

      JobInstance job = new JobInstance(Long.valueOf(100), new JobParameters(),
            "OTHER_TRAITEMENT");

      jobsEnAttente.add(job);

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement de capture en masse en local en attente
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_nojobCaptureMasseLocal()
         throws AucunJobALancerException {

      List<JobExecution> jobsEnCours = new ArrayList<JobExecution>();
      List<JobInstance> jobsEnAttente = new ArrayList<JobInstance>();

      jobsEnAttente.add(createJobCaptureMasse(202, CAPTURE_MASSE_JN,
            CER44_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * traitement de capture en masse en local en cours
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_executionJobCaptureMasse()
         throws AucunJobALancerException {

      List<JobExecution> jobsEnCours = new ArrayList<JobExecution>();
      jobsEnCours.add(createJobExecutionCaptureMasse(202, CAPTURE_MASSE_JN,
            CONTEXT_HOST, HOST_VALUE));

      List<JobInstance> jobsEnAttente = new ArrayList<JobInstance>();

      jobsEnAttente.add(createJobCaptureMasse(458, CAPTURE_MASSE_JN,
            CER69_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   private JobInstance createJobCaptureMasse(long instanceId, String jobName,
         String urlECDE) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("capture.masse.sommaire", new JobParameter(urlECDE));
      parameters.put("id", new JobParameter(UUID.randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobInstance job = new JobInstance(instanceId, jobParameters, jobName);

      return job;
   }

   private JobExecution createJobExecutionCaptureMasse(long instanceId,
         String jobName, String contextName, String contextValue) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters jobParameters = new JobParameters(parameters);

      JobInstance job = new JobInstance(instanceId, jobParameters, jobName);

      JobExecution execution = new JobExecution(job);
      execution.getExecutionContext().put(contextName, contextValue);

      return execution;
   }
}
