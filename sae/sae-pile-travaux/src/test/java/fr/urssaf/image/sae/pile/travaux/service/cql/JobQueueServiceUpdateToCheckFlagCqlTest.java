package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.model.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceUpdateToCheckFlagCqlTest {

   @Autowired
   private JobQueueCqlService jobQueueService;

   @Autowired
   private JobLectureCqlService jobLectureService;

   private UUID idJob;

   private void setJob(final UUID idJob) {
      this.idJob = idJob;
   }

   @Before
   public void before() {

      setJob(null);
   }

   @After
   public void after() {

      // suppression du traitement de masse
      if (idJob != null) {

         jobQueueService.deleteJob(idJob);

      }
   }

   @Test
   public void updateToCheckFlag_success() throws JobInexistantException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      createJob(idJob);

      jobQueueService.updateToCheckFlag(idJob, true, "raison de mettre à vrai");

      // vérification de JobRequest
      JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertTrue("le toCheckFlag de traitement est inattendu",
                        jobRequest.getToCheckFlag());

      Assert.assertEquals("le toCheckFlagRaison de traitement est inattendu",
                          "raison de mettre à vrai",
                          jobRequest.getToCheckFlagRaison());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 2, histories.get(0).getTrace().size());

      boolean isPositioned = false;

      Map<UUID, String> map2 = histories.get(0).getTrace();
      for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
         if ("TOCHECKFLAG POSITIONNE A true AVEC LA RAISON raison de mettre à vrai".equals(entry.getValue())) {
            isPositioned = true;
         }
      }
      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isPositioned);

      // on renseigne une seconde fois le toCheckToFlag

      jobQueueService.updateToCheckFlag(idJob,
                                        false,
                                        "raison de mettre à false");

      // vérification de JobRequest
      jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertFalse("le toCheckFlag de traitement est inattendu",
                         jobRequest.getToCheckFlag());

      Assert.assertEquals("le toCheckFlagRaison de traitement est inattendu",
                          "raison de mettre à false",
                          jobRequest.getToCheckFlagRaison());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      histories = jobLectureService.getJobHistory(idJob);

      map2 = histories.get(0).getTrace();

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 3, histories.get(0).getTrace().size());

      for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
         if ("TOCHECKFLAG POSITIONNE A true AVEC LA RAISON raison de mettre à vrai".equals(entry.getValue())) {
            isPositioned = true;
         }
      }
      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isPositioned);

   }

   @Test
   public void renseignerPidJob_failure_jobInexistantException() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.updateToCheckFlag(idJob, true, "blabla");
         Assert.fail("Une exception JobInexistantException devrait être lever");
      }
      catch (final JobInexistantException e) {

         Assert.assertEquals("l'identifiant du job est inattendu", idJob, e
                                                                           .getInstanceId());
         Assert.assertEquals("le message de l'exception est inattendu",
                             "Impossible de lancer, de modifier ou de réserver le traitement n°" + idJob
                                   + " car il n'existe pas.",
                             e.getMessage());
      }
   }

   private void createJob(final UUID idJob) {

      final Date dateCreation = new Date();
      final Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put("parameters", "param");

      final JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setClientHost("clientHost");
      job.setDocCount(100);
      job.setSaeHost("saeHost");
      job.setCreationDate(dateCreation);
      final String jobKey = new String("jobKey");
      job.setJobKey(jobKey.getBytes());

      jobQueueService.addJob(job);
   }

}
