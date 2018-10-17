package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceReserveJobCqlTest {

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

         // jobQueueService.deleteJob(idJob);

      }
   }

   @Test
   public void reserveJob_success() throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      final Date dateReservation = new Date();
      final String reservedBy = "hostname";
      jobQueueService.reserveJob(idJob, reservedBy, dateReservation);

      // vérification de JobRequest
      final JobRequestCql jobRequest = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals("l'état est inattendu", JobState.RESERVED.name(), jobRequest
                                                                                      .getState());
      Assert.assertEquals("le ReservedBy est inattendu", reservedBy, jobRequest
                                                                               .getReservedBy());
      Assert.assertEquals("la date de réservation est inattendue",
                          dateReservation,
                          jobRequest.getReservationDate());

      // On vérifie qu'on ne peut plus le réserver
      try {
         jobQueueService.reserveJob(idJob, "hostname2", new Date());
         Assert.fail("On ne devrait pas pouvoir réserver 2 fois le même job");
      }
      catch (final JobDejaReserveException e) {

         Assert.assertEquals("le nom du serveur est inattendu", reservedBy, e
                                                                             .getServer());
         Assert.assertEquals("le job est inattendu", idJob, e.getInstanceId());
         Assert.assertEquals("le message de l'exception est inattendu",
                             "Le traitement n°" + idJob
                                   + " est déjà réservé par le serveur 'hostname'.",
                             e
                              .getMessage());

      }

      // vérification de JobsQueues

      final Iterator<JobQueueCql> jobQueuesEnAttente = jobLectureService.getUnreservedJobRequestIterator();

      JobQueueCql jobQueueEnAttente = null;
      while (jobQueuesEnAttente.hasNext() && jobQueueEnAttente == null) {
         final JobQueueCql jobQueueElement = jobQueuesEnAttente.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueueEnAttente = jobQueueElement;
         }
      }

      Assert.assertNull("le job ne doit plus exister dans la file d'attente",
                        jobQueueEnAttente);

      final Iterator<JobQueueCql> jobQueues = jobLectureService
                                                               .getNonTerminatedSimpleJobs(reservedBy).iterator();

      JobQueueCql jobQueue = null;
      while (jobQueues.hasNext() && jobQueue == null) {
         final JobQueueCql jobQueueElement = jobQueues.next();
         if (jobQueueElement.getIdJob().equals(idJob)) {
            jobQueue = jobQueueElement;
         }
      }

      Assert.assertNotNull(
                           "le job doit exister dans la file de réservation de " + reservedBy,
                           jobQueue);
      Assert.assertEquals("le type de traitement est inattendu",
                          "ArchivageMasse",
                          jobQueue.getType());
      Assert.assertEquals("les paramètres sont inattendus",
                          "param",
                          jobQueue.getJobParameters().get("parameters"));

      // vérification de JobHistory
      final List<JobHistoryCql> histories = jobLectureService.getJobHistory(idJob);

      Assert.assertNotNull(histories.get(0));
      Assert.assertEquals("le nombre de message est inattendu", 2, histories.get(0).getTrace().size());

      boolean isReservationJob = false;

      final Map<UUID, String> map2 = histories.get(0).getTrace();
      for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
         if ("RESERVATION DU JOB".equals(entry.getValue())) {
            isReservationJob = true;
         }
      }
      Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isReservationJob);

   }

   @Test(expected = JobDejaReserveException.class)
   public void reserveJob_failure_reservation() throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // réservation du job
      jobQueueService.reserveJob(idJob, "serveur", new Date());

      // on démarre le job
      jobQueueService.startingJob(idJob, new Date());

      // seconde tentative de résevation du job
      jobQueueService.reserveJob(idJob, "other_serveur", new Date());
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
   public void reserveJob_failure_jobInexistantException()
         throws JobDejaReserveException, LockTimeoutException {

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      // on s'assure qu'il n'existe pas
      jobQueueService.deleteJob(idJob);

      try {
         jobQueueService.reserveJob(idJob, "hostname", new Date());
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
