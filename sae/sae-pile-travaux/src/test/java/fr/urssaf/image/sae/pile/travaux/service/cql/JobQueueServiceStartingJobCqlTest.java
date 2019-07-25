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

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceStartingJobCqlTest {

   @Autowired
   private JobQueueCqlService jobQueueService;

   @Autowired
   private JobLectureCqlService jobLectureService;
   
   @Autowired
   private CassandraServerBean cassandraServer;

   private UUID idJob;

   private void setJob(final UUID idJob) {
      this.idJob = idJob;
   }

   @Before
   public void before() throws Exception {
      cassandraServer.resetData(true, MODE_API.DATASTAX);
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
   public void startingJob_success() throws JobInexistantException,
         JobDejaReserveException, LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      jobQueueService.reserveJob(idJob, "hostname", new Date());

      final Date dateDebutTraitement = new Date();
      jobQueueService.startingJob(idJob, dateDebutTraitement);

      // vérification de JobRequest
      final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.STARTING.name(), jobRequest
                                                                                      .getState());
      Assert.assertEquals("la date de démarrage est inattendue",
                          dateDebutTraitement,
                          jobRequest.getStartingDate());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 3, histories.get(0).getTrace().size());

      boolean isStartingJob = false;

      final Map<UUID, String> map2 = histories.get(0).getTrace();
      for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
         if ("DEMARRAGE DU JOB".equals(entry.getValue())) {
            isStartingJob = true;
         }
      }
      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isStartingJob);

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

   @Test
   public void startingJob_failure_jobInexistantException() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.startingJob(idJob, new Date());
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
}
