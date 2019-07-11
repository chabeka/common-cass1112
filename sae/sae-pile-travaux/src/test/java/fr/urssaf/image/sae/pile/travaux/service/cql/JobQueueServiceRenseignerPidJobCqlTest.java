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

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceRenseignerPidJobCqlTest {

   @Autowired
   private JobQueueCqlService jobQueueService;

   @Autowired
   private JobLectureCqlService jobLectureService;

   @Autowired
   private CassandraServerBeanCql serverBean;
   
   private UUID idJob;

   private void setJob(final UUID idJob) {
      this.idJob = idJob;
   }

   @Before
   public void before() throws Exception {
     serverBean.resetData(true);
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
   public void renseignerPidJob_success() throws JobInexistantException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      createJob(idJob);

      final Integer pid = 100;
      jobQueueService.renseignerPidJob(idJob, pid);

      // vérification de JobRequest
      JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertEquals("le pid de traitement est inattendu", pid, jobRequest
                                                                               .getPid());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

      // Assert.assertEquals("le nombre de message est inattendu", 2, histories.size());

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 2, histories.get(0).getTrace().size());

      boolean isPidRenseigne = false;

      Map<UUID, String> map2 = histories.get(0).getTrace();
      for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
         if ("PID RENSEIGNE".equals(entry.getValue())) {
            isPidRenseigne = true;
         }
      }
      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isPidRenseigne);

      /*
       * Assert.assertEquals(
       * "le message de l'ajout d'un traitement est inattendu",
       * "PID RENSEIGNE",
       * histories.get(1).getTrace());
       */

      // on renseigne une seconde fois le pid
      final Integer otherpid = 150;
      jobQueueService.renseignerPidJob(idJob, otherpid);

      // vérification de JobRequest
      jobRequest = jobLectureService.getJobRequest(idJob);

      Assert.assertEquals("le pid de traitement est inattendu",
                          otherpid,
                          jobRequest.getPid());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      histories = jobLectureService.getJobHistory(idJob);

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 3, histories.get(0).getTrace().size());

      isPidRenseigne = false;
      map2 = histories.get(0).getTrace();
      for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
         if ("PID RENSEIGNE".equals(entry.getValue())) {
            isPidRenseigne = true;
         }
      }
      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isPidRenseigne);

   }

   @Test
   public void renseignerPidJob_failure_jobInexistantException() {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.renseignerPidJob(idJob, 100);
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