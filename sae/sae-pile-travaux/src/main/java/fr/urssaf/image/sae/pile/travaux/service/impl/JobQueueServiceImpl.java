package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.beans.HColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupport;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupport;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupport;

/**
 * Implémentation du service {@link JobQueueService}
 * 
 * 
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

   private final CuratorFramework curatorClient;

   private final JobClockSupport jobClockSupport;

   private final JobsQueueSupport jobsQueueSupport;

   private final JobHistorySupport jobHistorySupport;

   private final JobLectureService jobLectureService;

   private final JobRequestDao jobRequestDao;

   private final JobRequestSupport jobRequestSupport;

   private static final Logger LOG = LoggerFactory
         .getLogger(JobQueueServiceImpl.class);

   /**
    * 
    * @param jobRequestDao
    *           DAO de la colonne famille JobRequest
    * @param jobsQueueDao
    *           DAO de la colonne famille JobsQueue
    * @param jobClockSupport
    *           Support pour le calcul de l'horloge sur Cassandra
    * @param jobHistoryDao
    *           DAO de la colonne famille JobHistory
    * @param jobLectureService
    *           service de lecture de la pile des travaux
    * @param curatorClient
    *           support pour l'utilisation de {@link ZookeeperMutex}
    */
   @Autowired
   public JobQueueServiceImpl(JobRequestDao jobRequestDao,
         JobsQueueDao jobsQueueDao, JobClockSupport jobClockSupport,
         JobHistoryDao jobHistoryDao, JobLectureService jobLectureService,
         CuratorFramework curatorClient) {

      this.curatorClient = curatorClient;
      this.jobClockSupport = jobClockSupport;
      this.jobLectureService = jobLectureService;
      this.jobRequestDao = jobRequestDao;

      this.jobRequestSupport = new JobRequestSupport(jobRequestDao);
      this.jobsQueueSupport = new JobsQueueSupport(jobsQueueDao);
      this.jobHistorySupport = new JobHistorySupport(jobHistoryDao);

   }

   /**
    * Time-out du lock, en secondes
    */
   private static final int LOCK_TIME_OUT = 20;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addJob(JobToCreate jobToCreate) {

      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      long clock = jobClockSupport.currentCLock();

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupport.ajouterJobDansJobRequest(jobToCreate, clock);

      // Ecriture dans la CF "JobQueues"
      this.jobsQueueSupport.ajouterJobDansJobQueuesEnWaiting(jobToCreate
            .getIdJob(), jobToCreate.getType(), jobToCreate.getParameters(),
            clock);

      // Ecriture dans la CF "JobHistory"
      String messageTrace = "CREATION DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupport.ajouterTrace(jobToCreate.getIdJob(),
            timestampTrace, messageTrace, clock);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void endingJob(UUID idJob, boolean succes,
         Date dateFinTraitement, String message) throws JobInexistantException {

      JobRequest jobRequest = this.jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // TODO: Vérifier que le job est à l'état STARTING

      // Lecture du job
      ColumnFamilyResult<UUID, String> result = this.jobRequestDao
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      long clock = jobClockSupport.currentCLock(columnState);

      // Lecture des propriétés du job dont on a besoin
      String reservedBy = jobRequest.getReservedBy();

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupport.passerEtatTermineJobRequest(idJob,
            dateFinTraitement, succes, message, clock);

      // Ecriture dans la CF "JobQueues"
      this.jobsQueueSupport.supprimerJobDeJobsQueues(idJob, reservedBy, clock);

      // Ecriture dans la CF "JobHistory"
      String messageTrace = "FIN DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupport.ajouterTrace(idJob, timestampTrace, messageTrace,
            clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void endingJob(UUID idJob, boolean succes,
         Date dateFinTraitement) throws JobInexistantException {
      endingJob(idJob, succes, dateFinTraitement, null);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(UUID idJob, String hostname,
         Date dateReservation) throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {

      Assert.notNull(idJob, "L'id du job ne doit pas être null");

      ZookeeperMutex mutex = new ZookeeperMutex(curatorClient, "/JobRequest/"
            + idJob);
      try {
         if (!mutex.acquire(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
            throw new LockTimeoutException(
                  "Erreur lors de la tentative d'acquisition du lock pour le jobRequest "
                        + idJob + " : on n'a pas obtenu le lock au bout de "
                        + LOCK_TIME_OUT + " secondes.");
         }
         // On a le lock.
         // Récupération du jobRequest
         JobRequest jobRequest = this.jobLectureService.getJobRequest(idJob);
         // Vérifier que le job existe
         if (jobRequest == null) {
            throw new JobInexistantException(idJob);
         }

         // Vérifier que le job n'est pas déjà RESERVED
         if (JobState.RESERVED.equals(jobRequest.getState())) {
            throw new JobDejaReserveException(idJob, jobRequest.getReservedBy());
         }

         // TODO: Vérifier que le job est à l'état CREATED

         // Lecture du job
         ColumnFamilyResult<UUID, String> result = this.jobRequestDao
               .getJobRequestTmpl().queryColumns(idJob);

         // Récupération de la colonne "state"
         HColumn<?, ?> columnState = result
               .getColumn(JobRequestDao.JR_STATE_COLUMN);

         // Timestamp de l'opération
         // Il faut vérifier le décalage de temps
         long clock = jobClockSupport.currentCLock(columnState);

         // Lecture des propriétés du job dont on a besoin
         String type = jobRequest.getType();
         String parameters = jobRequest.getParameters();

         // Ecriture dans la CF "JobRequest"
         this.jobRequestSupport.reserverJobDansJobRequest(idJob, hostname,
               dateReservation, clock);

         // Ecriture dans la CF "JobQueues"
         this.jobsQueueSupport.reserverJobDansJobQueues(idJob, hostname, type,
               parameters, clock);

         // Ecriture dans la CF "JobHistory"
         String messageTrace = "RESERVATION DU JOB";
         UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
         this.jobHistorySupport.ajouterTrace(idJob, timestampTrace,
               messageTrace, clock);

         // On vérifie qu'on a toujours le lock. Si oui, la réservation a
         // réellement fonctionné
         checkLock(mutex, idJob, hostname);

      } finally {
         mutex.release();
      }
   }

   /**
    * Après la réservation d'un job, on vérifie que le lock est encore valide
    * 
    * @param mutex
    *           Le mutex utilisé pour le lock
    * @param idJob
    *           Id du job réservé
    * @param hostname
    *           Nom du serveur qui tente la réservation
    * @throws JobDejaReserveException
    *            Si le lock n'est plus valide et qu'on s'est fait subtilisé le
    *            job
    */
   private void checkLock(ZookeeperMutex mutex, UUID idJob, String hostname)
         throws JobDejaReserveException {
      // On vérifie qu'on a toujours le lock. Si oui, la réservation a
      // réellement fonctionné
      if (mutex.isObjectStillLocked(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
         // C'est bon, le job est réellement réservé
         return;
      } else {
         // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne
         // devrait jamais arriver.
         String message = "Erreur lors de la tentative d'acquisition du lock pour le jobRequest "
               + idJob + ". Problème de connexion zookeeper ?";
         LOG.error(message);

         // On regarde si le job a été réservé par un autre serveur
         JobRequest jobRequest = this.jobLectureService.getJobRequest(idJob);
         String currentHostname = jobRequest.getReservedBy();
         if (currentHostname != null && currentHostname.equals(hostname)) {
            // On a été déconnecté de zookeeper, mais pour autant, le job nous a
            // été attribué.
            return;
         } else {
            throw new JobDejaReserveException(idJob, currentHostname);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void startingJob(UUID idJob, Date dateDebutTraitement)
         throws JobInexistantException {

      JobRequest jobRequest = this.jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // TODO: Vérifier que le job est à l'état RESERVED

      // Lecture du job
      ColumnFamilyResult<UUID, String> result = this.jobRequestDao
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      long clock = jobClockSupport.currentCLock(columnState);

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupport.passerEtatEnCoursJobRequest(idJob,
            dateDebutTraitement, clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      String messageTrace = "DEMARRAGE DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupport.ajouterTrace(idJob, timestampTrace, messageTrace,
            clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addHistory(UUID jobUuid, UUID timeUuid, String description) {

      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      long clock = jobClockSupport.currentCLock();

      // Ecriture dans la CF "JobRequest"
      // rien à écrire

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupport.ajouterTrace(jobUuid, timestampTrace, description,
            clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void renseignerPidJob(UUID idJob, Integer pid)
         throws JobInexistantException {

      JobRequest jobRequest = this.jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Lecture du job
      ColumnFamilyResult<UUID, String> result = this.jobRequestDao
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération du timestamp courant de la colonne "pid", si elle est
      // présente

      // Récupération de la colonne "pid"
      HColumn<?, ?> columnPid = result.getColumn(JobRequestDao.JR_PID);

      long clock;

      if (columnPid == null) {

         clock = jobClockSupport.currentCLock();

      } else {

         clock = jobClockSupport.currentCLock(columnPid);

      }

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupport.renseignerPidDansJobRequest(idJob, pid, clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      String messageTrace = "PID RENSEIGNE";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupport.ajouterTrace(idJob, timestampTrace, messageTrace,
            clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateToCheckFlag(UUID idJob, Boolean toCheckFlag,
         String raison) throws JobInexistantException {
      // TODO Auto-generated method stub

      JobRequest jobRequest = this.jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Lecture du job
      ColumnFamilyResult<UUID, String> result = this.jobRequestDao
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération du timestamp courant de la colonne "toCheckFlag", si elle
      // est
      // présente

      // Récupération de la colonne "toCheckFlag"
      HColumn<?, ?> columnToCheckFlag = result
            .getColumn(JobRequestDao.JR_TO_CHECK_FLAG);

      long clock;

      if (columnToCheckFlag == null) {

         clock = jobClockSupport.currentCLock();

      } else {

         clock = jobClockSupport.currentCLock(columnToCheckFlag);

      }

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupport.renseignerCheckFlagDansJobRequest(idJob,
            toCheckFlag, raison, clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      String message = "TOCHECKFLAG POSITIONNE A {0} AVEC LA RAISON (1)";
      String messageTrace = MessageFormat.format(message, toCheckFlag, raison);
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupport.ajouterTrace(idJob, timestampTrace, messageTrace,
            clock);

   }

}
