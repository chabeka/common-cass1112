package fr.urssaf.image.sae.webservices.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageRequestType;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.service.WSDeblocageService;

/**
 * Classe de test du ws de déblocage de traitement de masse en erreur.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings({ "PMD.MethodNamingConventions",
      "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
public class WSDeblocageServiceImplTest {

   @Autowired
   private WSDeblocageService wsDeblocage;

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   private static final String IP_VALUE = "127.0.0.1";

   private JobToCreate job;

   @Before
   public void before() {
      // Création du job
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("jobTest");
      Map<String, String> jobParameters = new HashMap<String, String>();
      jobParameters.put(Constantes.CODE_TRAITEMENT, Constantes.CODE_TRAITEMENT);
      job.setJobParameters(jobParameters);
      jobQueueService.addJob(job);
   }

   @After
   public void after() {
      // suppression du traitement
      if (job != null) {
         jobQueueService.deleteJob(job.getIdJob());
      }
   }

   /**
    * Teste si le job passe bien à l'état Failure après son déblocage
    * 
    * @throws DeblocageAxisFault
    * @throws JobInexistantException
    * @throws JobDejaReserveException
    * @throws LockTimeoutException
    */
   @Test
   public void testDeblocageModificationMasseReserved_success()
         throws DeblocageAxisFault, JobInexistantException,
         JobDejaReserveException, LockTimeoutException {
      jobQueueService.reserveJob(job.getIdJob(), "hostname", new Date());
      mockDeblocageJob(job.getIdJob().toString());
      JobRequest jobRequest = jobLectureService.getJobRequest(job.getIdJob());
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.FAILURE, jobRequest.getState());
   }

   /**
    * Teste si le job passe bien à l'état ABORT après son déblocage
    * 
    * @throws DeblocageAxisFault
    * @throws JobInexistantException
    * @throws JobDejaReserveException
    * @throws LockTimeoutException
    */
   @Test
   public void testDeblocageModificationMasseFailure_success()
         throws DeblocageAxisFault, JobInexistantException,
         JobDejaReserveException, LockTimeoutException {
      jobQueueService.reserveJob(job.getIdJob(), "hostname", new Date());
      Date failureDate = new Date();
      // Passer le job à l'état Failure
      jobQueueService.changerEtatJobRequest(job.getIdJob(),
            JobState.FAILURE.name(), failureDate, null);
      // Similation du déblocage
      mockDeblocageJob(job.getIdJob().toString());
      JobRequest jobRequest = jobLectureService.getJobRequest(job.getIdJob());
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.ABORT, jobRequest.getState());
   }

   /**
    * Lance le déblocage du job associé à l'id passé en paramètre
    * 
    * @param uuid
    * @throws DeblocageAxisFault
    * @throws JobInexistantException
    */
   private void mockDeblocageJob(String uuid) throws DeblocageAxisFault,
         JobInexistantException {
      Deblocage request = new Deblocage();
      request.setDeblocage(new DeblocageRequestType());
      request.getDeblocage().setUuid(uuid);
      wsDeblocage.deblocage(request, IP_VALUE);
   }

}
