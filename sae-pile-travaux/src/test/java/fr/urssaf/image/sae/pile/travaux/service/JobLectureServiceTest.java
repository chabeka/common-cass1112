package fr.urssaf.image.sae.pile.travaux.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.exception.PileTravauxRuntimeException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class JobLectureServiceTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   @Autowired
   private JobRequestDao jobRequestDao;

   @Autowired
   private JobClockSupport jobClockSupport;

   private UUID idJob;

   private UUID otherJob;

   private void setJob(UUID idJob) {
      this.idJob = idJob;
      this.otherJob = idJob;
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

      if (otherJob != null) {

         jobQueueService.deleteJob(otherJob);

      }
   }

   @Test
   public void getUnreservedJobRequestIterator() {

      // création d'un job
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      Iterator<JobQueue> jobs = jobLectureService
            .getUnreservedJobRequestIterator();

      Assert.assertTrue(
            "il doit exister au moins un traitement en cours ou réservé", jobs
                  .hasNext());

   }

   @Test
   public void getNonTerminatedJobs() throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      String hostname = "myHostname";

      // création d'un job
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // réservation d'un job
      jobQueueService.reserveJob(idJob, hostname, new Date());

      List<JobRequest> jobs = jobLectureService.getNonTerminatedJobs(hostname);

      Assert.assertFalse(
            "il doit exister au moins un traitement en cours ou réservé", jobs
                  .isEmpty());

   }

   @Test
   public void getJobHistoryByUUID() {

      // création d'un job
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // création d'un autre job
      otherJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(otherJob);

      // ajout des traces dans le premier job

      Date date = new Date();
      long timestamp = date.getTime();

      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getTimeUUID(timestamp + 1), "message n°1");
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getTimeUUID(timestamp + 2), "message n°2");
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getTimeUUID(timestamp + 3), "message n°3");
      jobQueueService.addHistory(idJob, TimeUUIDUtils
            .getTimeUUID(timestamp + 4), "message n°4");

      // ajout des traces dans le second job
      jobQueueService.addHistory(otherJob, TimeUUIDUtils
            .getTimeUUID(timestamp + 1), "message n°5");
      jobQueueService.addHistory(otherJob, TimeUUIDUtils
            .getTimeUUID(timestamp + 2), "message n°6");

      // test sur le premier job
      List<JobHistory> histories = jobLectureService.getJobHistory(idJob);

      // 5 à cause de la trace laissée par la création du job
      Assert.assertEquals("la taille de l'historique est inattendue", 5,
            histories.size());

      // Assert.assertEquals(TRACE_MESSAGE, "CREATION DU JOB",
      // histories.get(0));
      assertHistory(histories.get(1), "message n°1", date, 1);
      assertHistory(histories.get(2), "message n°2", date, 2);
      assertHistory(histories.get(3), "message n°3", date, 3);
      assertHistory(histories.get(4), "message n°4", date, 4);

      // test sur le second job
      List<JobHistory> otherhistories = jobLectureService
            .getJobHistory(otherJob);

      // 3 à cause de la trace laissée par la création du job
      Assert.assertEquals("la taille de l'historique est inattendue", 3,
            otherhistories.size());

      // Assert.assertEquals(TRACE_MESSAGE, "CREATION DU JOB",
      // histories.get(0));
      assertHistory(otherhistories.get(1), "message n°5", date, 1);
      assertHistory(otherhistories.get(2), "message n°6", date, 2);

   }

   @Test
   public void testIsResettable() {
      // création d'un job
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      JobRequest job = new JobRequest();

      // Le job est à l'état CREATED, sa réinitialisation doit être possible
      job.setState(JobState.RESERVED);
      Assert.assertTrue(
            "Le job est à l'état RESERVED, il doit pouvoir être réinitialisé",
            jobLectureService.isJobResettable(job));

      // Le job est dans un état différente de CREATED, RESERVED ou STARTING, sa
      // réinitialisation est impossible
      job.setState(JobState.FAILURE);
      Assert
            .assertFalse(
                  "Le job est à l'état FAILURE, il ne doit pas pouvoir être réinitialisé",
                  jobLectureService.isJobResettable(job));
   }

   @Test
   public void testIsRemovable() {
      // création d'un job
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      JobRequest job = new JobRequest();

      // Le job est à l'état CREATED, sa suppression doit être possible
      job.setState(JobState.CREATED);
      Assert.assertTrue(
            "Le job est à l'état CREATED, il doit pouvoir être supprimé",
            jobLectureService.isJobRemovable(job));

      // Le job est dans un état différente de CREATED, RESERVED ou STARTING, sa
      // suppression est impossible
      job.setState(JobState.FAILURE);
      Assert
            .assertFalse(
                  "Le job est à l'état FAILURE, il ne doit pas pouvoir être supprimé",
                  jobLectureService.isJobRemovable(job));
   }

   private void assertHistory(JobHistory history, String expectedTrace,
         Date expectedDate, int decalage) {

      Assert.assertEquals("la trace est inattendue", expectedTrace, history
            .getTrace());

      String pattern = "dd/MM/yyyy HH:mm:ss.SSS";
      DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

      Assert.assertEquals("la date est inattendue", dateFormat.format(DateUtils
            .addMilliseconds(expectedDate, decalage)), dateFormat
            .format(history.getDate()));
   }

   private void createJob(UUID idJob) {
      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put("parameters", "param");

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setCreationDate(new Date());

      jobQueueService.addJob(job);

   }

   /**
    * TU pour les Redmine 3406 et 3407 Si la colonne dateCreation d'un
    * JobRequest est vide => erreur technique
    */
   @Test
   public void getJobRequest_DateCreationVide() {

      // Création d'un job standard
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createJob(idJob);

      // Suppression manuelle de la colonne dateCreation
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = jobRequestDao
            .getJobRequestTmpl().createUpdater(idJob);
      updaterJobRequest.deleteColumn(JobRequestDao.JR_CREATION_DATE_COLUMN);
      jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

      // Lecture du job
      try {

         jobLectureService.getJobRequest(idJob);

         Assert.fail("On attendait une exception");

      } catch (PileTravauxRuntimeException ex) {

         Assert
               .assertEquals(
                     "Le message de l'exception obtenue est incorrect",
                     String
                           .format(
                                 "Erreur technique : la colonne creationDate pour le job %s est vide ou absente (Column family JobRequest)",
                                 idJob), ex.getMessage());

         // Remet le job dans un état correct pour que le after() fonctionne
         updaterJobRequest = jobRequestDao.getJobRequestTmpl().createUpdater(
               idJob);
         long clock = jobClockSupport.currentCLock();
         jobRequestDao.ecritColonneCreationDate(updaterJobRequest, new Date(),
               clock);
         jobRequestDao.getJobRequestTmpl().update(updaterJobRequest);

      }

   }

}
