package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

/**
 * Implémentation du service {@link JobService}.<br>
 * <br>
 * la persistance des traitements s'appuie sur Spring Batch.<br>
 * L'implémentation de la persistance est fournié par Cassandra.
 * 
 * 
 * 
 */
public class JobServiceImpl implements JobService {

   /**
    * Logger.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(JobServiceImpl.class);

   /**
    * Prefixe pour la clef zookeeper.
    */
   private static final String PREFIXE_SEMAPHORE = "/Semaphore/";

   /**
    * Prefixe pour la clef cassandra.
    */
   private static final String PREFIXE_SEMAPHORE_JOB = "semaphore_";

   /**
    * Le code du traitement
    */
   private static final String CODE_TRAITEMENT = "codeTraitement";

   /**
    * Service de lecture des Jobs.
    */
   private final JobLectureService jobLectureService;

   /**
    * Service de queue des Jobs.
    */
   private final JobQueueService jobQueueService;

   /**
    * Zookeeper curator.
    */
   private final CuratorFramework curator;

   /**
    * 
    * @param jobLectureService
    *           service de lecture de la pile des jobs
    * @param jobQueueService
    *           service de la pile des jobs
    */
   @Autowired
   public JobServiceImpl(JobLectureService jobLectureService,
         JobQueueService jobQueueService, CuratorFramework curatorFramework) {

      this.jobLectureService = jobLectureService;
      this.jobQueueService = jobQueueService;
      this.curator = curatorFramework;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> recupJobEnCours() {

      String hostname;
      try {
         hostname = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new OrdonnanceurRuntimeException(e);
      }
      List<JobRequest> jobRequests = jobLectureService
            .getNonTerminatedJobs(hostname);

      return jobRequests;

   }

   /**
    * {@inheritDoc}
    */

   @Override
   public final List<JobQueue> recupJobsALancer() {

      @SuppressWarnings("unchecked")
      List<JobQueue> jobRequests = IteratorUtils.toList(jobLectureService
            .getUnreservedJobRequestIterator());

      return jobRequests;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(UUID idJob) throws JobDejaReserveException,
         JobInexistantException {

      // récupération du nom de la machine
      String hostname;
      try {
         hostname = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

      try {
         jobQueueService.reserveJob(idJob, hostname, new Date());
      } catch (LockTimeoutException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateToCheckFlag(UUID idJob, Boolean flag, String description)
         throws JobInexistantException {
      jobQueueService.updateToCheckFlag(idJob, flag, description);
   }

   @Override
   public boolean isJobCodeTraitementEnCoursOuFailure(JobQueue jobQueue) {
      boolean isJobCodeTraitementEnCoursOuFailure = false;
      String traceMethod = "isJobCodeTraitementEnCoursOuFailure";
      // Si on n'a pas de code de traitement, c'est qu'on est pas sur un job qui
      // est géré par semaphore.
      if (jobQueue.getJobParameters() != null
            && jobQueue.getJobParameters().get(CODE_TRAITEMENT) != null) {
         String codeTraitement = jobQueue.getJobParameters().get(
               CODE_TRAITEMENT);

         String semaphore = PREFIXE_SEMAPHORE + codeTraitement;

         // Création du mutex
         ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator, semaphore);

         LOGGER.debug("{} - Vérification que le Job avec le code traitement "
               + codeTraitement + " est libre", traceMethod);
         if (!ZookeeperUtils.isLock(mutex)) {
            List<JobRequest> jobRequests = jobLectureService
                  .getNonTerminatedJobs(codeTraitement);

            isJobCodeTraitementEnCoursOuFailure = jobRequests != null
                  && !jobRequests.isEmpty();

            jobRequests = jobLectureService.getJobsFailureWithParameters(null,
                  codeTraitement);

            isJobCodeTraitementEnCoursOuFailure = isJobCodeTraitementEnCoursOuFailure
                  && jobRequests != null && !jobRequests.isEmpty();

         } else {
            isJobCodeTraitementEnCoursOuFailure = true;
         }
      }

      return isJobCodeTraitementEnCoursOuFailure;
   }

   @Override
   public JobQueue confirmerJobALancer(JobQueue jobQueue) {
      String traceMethod = "confirmerJobALancer";

      if (jobQueue.getJobParameters() != null
            && jobQueue.getJobParameters().get(CODE_TRAITEMENT) != null) {
         String codeTraitement = jobQueue.getJobParameters().get(
               CODE_TRAITEMENT);

         String semaphore = PREFIXE_SEMAPHORE + codeTraitement;

         // Création du mutex
         ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator, semaphore);

         try {
            LOGGER.debug(
                  "{} - Vérification que le Job avec le code traitement "
                        + codeTraitement + " est disponible", traceMethod);
            if (!ZookeeperUtils.isLock(mutex)) {
               LOGGER.debug("{} - Lock Zookeeper", traceMethod);
               ZookeeperUtils.acquire(mutex, semaphore);

               // Job à créer.
               JobToCreate job = new JobToCreate();
               job.setIdJob(jobQueue.getIdJob());
               job.setType(jobQueue.getType());
               job.setParameters(jobQueue.getParameters());
               job.setJobParameters(jobQueue.getJobParameters());

               // Ajouter un job en waiting
               jobQueueService.addJobsQueue(job);

               // Reserver le job avec le semaphore.
               jobQueueService.reserverJobDansJobsQueues(jobQueue.getIdJob(),
                     PREFIXE_SEMAPHORE_JOB + codeTraitement,
                     jobQueue.getType(),
                     jobQueue.getJobParameters());

            }

         } finally {
            mutex.release();
         }
      }

      return jobQueue;
   }

}
