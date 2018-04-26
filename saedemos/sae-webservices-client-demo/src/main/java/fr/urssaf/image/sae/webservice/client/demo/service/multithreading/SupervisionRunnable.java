/**
 * 
 */
package fr.urssaf.image.sae.webservice.client.demo.service.multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.prettyprint.hector.api.Keyspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobLectureServiceImpl;

/**
 * Thread d'insertion d'un document dans DFCE dans les traitements de capture de
 * masse
 * 
 */
public class SupervisionRunnable implements Runnable {

   private static final Logger LOG = LoggerFactory
         .getLogger(SupervisionRunnable.class);

   private static final String TRC_RUNNABLE = "SupervisionRunnable.run()";
   private static final String TRC_SUPV_JOB = "SupervisionRunnable.superviserJobList()";

   private final ConcurrentLinkedQueue<String> listUUIDJobs;

   private final Keyspace keyspace;

   private List<String> listUUIDJobsSupervisionFin;

   private long timeDelaySupervision;


   /**
    * 
    * @param timeDelaySupervision
    * @param keyspc
    * @param indexDocument
    *           index du document dans le sommaire, commence à 0
    * @param storageDocument
    *           document à insérer dans DFCE
    * @param service
    *           service d'insertion
    */
   public SupervisionRunnable(ConcurrentLinkedQueue<String> listUUIDJobs,
         long timeDelaySupervision, Keyspace keyspace) {

      this.listUUIDJobs = listUUIDJobs;
      this.timeDelaySupervision = timeDelaySupervision;
      this.keyspace = keyspace;
      this.listUUIDJobsSupervisionFin = new ArrayList<String>();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {
      if (listUUIDJobs == null) {
         LOG.debug("{} - La liste d'UUID de job est null", TRC_RUNNABLE);
      }

      try {
         superviserJobList();
      } catch (InterruptedException e) {
         LOG.debug("{} - Arrêt thread demandé", TRC_RUNNABLE);
      }
   }

   private void superviserJobList() throws InterruptedException {
      JobRequestDao jobRequestDao = new JobRequestDao(keyspace);
      JobsQueueDao jobsQueueDao = new JobsQueueDao(keyspace);
      JobHistoryDao jobHistoryDao = new JobHistoryDao(keyspace);
      JobLectureService jobsLecture = new JobLectureServiceImpl(jobRequestDao,
            jobsQueueDao, jobHistoryDao);

      List<JobRequest> jobs = new ArrayList<JobRequest>();
      boolean stop = false;

      System.out.println("== Début analyse Job ==");
      int incr = 1;
      while (!stop) {
         for (String jobUUID : listUUIDJobs) {
            try {
               UUID uuidJobCourant = UUID.fromString(jobUUID);
               JobRequest job = jobsLecture.getJobRequest(uuidJobCourant);
               jobs.add(job);

            } catch (Exception e) {
               LOG.debug(
                     "{} - Erreur lors de la récupération des job à supperviser : {}",
                     TRC_SUPV_JOB, e.getMessage());
            }
         }

         if (!jobs.isEmpty()) {
            System.out.println(">> Analyse sequence n° " + incr);
            stop = verifyConditionArret(jobs);

            writeJobListOutput(jobs);

            System.out.println(">> Analyse sequence n° " + incr + " terminée");
            // Pause
            System.out.println("Attente de " + timeDelaySupervision / 1000
                  + " sec");
            Thread.sleep(timeDelaySupervision);
            incr++;
            jobs.clear();
            System.out.println(System.getProperty("line.separator"));
         }
      }
      System.out.println("== Fin analyse Job ==");


   }

   private boolean verifyConditionArret(List<JobRequest> jobs) {
      List<JobState> listJobStateNonAcceptable = Arrays.asList(
            JobState.CREATED, JobState.RESERVED, JobState.STARTING);
      for (JobRequest job : jobs) {
         if (job != null
               && !listUUIDJobsSupervisionFin.contains(job.getIdJob()
                     .toString())) {
            if (!listJobStateNonAcceptable.contains(job.getState())) {
               listUUIDJobsSupervisionFin.add(job.getIdJob().toString());
            }
         }
      }
      return !jobs.isEmpty()
            && jobs.size() == listUUIDJobsSupervisionFin.size();
   }

   private void writeJobListOutput(List<JobRequest> jobs) {
      StringBuilder builder = new StringBuilder();
      for (JobRequest job : jobs) {
         if (job != null) {
            builder.append("Job UUID : " + job.getIdJob() + " - Type : "
                  + job.getType() + " - Etat : " + job.getState().name());
            builder.append(System.getProperty("line.separator"));
         }
      }

      if (builder.length() > 1) {
         System.out.println(builder.toString());
      }

      System.out.println("Nombre de job en fin de supervision : "
            + listUUIDJobsSupervisionFin.size() + "/" + listUUIDJobs.size());
   }

   /**
    * @return the listUUIDJobs
    */
   public ConcurrentLinkedQueue<String> getListUUIDJobs() {
      return listUUIDJobs;
   }


}
