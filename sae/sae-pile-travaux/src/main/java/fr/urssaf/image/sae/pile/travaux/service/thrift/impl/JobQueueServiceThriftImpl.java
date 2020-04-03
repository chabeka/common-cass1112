package fr.urssaf.image.sae.pile.travaux.service.thrift.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobQueueServiceImpl;
import fr.urssaf.image.sae.pile.travaux.service.thrift.JobQueueThriftService;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupport;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupport;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupport;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.beans.HColumn;

/**
 * Implémentation du service {@link JobQueueService}
 */
@Service
public class JobQueueServiceThriftImpl implements JobQueueThriftService {

   private final CuratorFramework curatorClient;

   private final JobClockSupport jobClockSupport;

   private final JobsQueueSupport jobsQueueSupport;

   private final JobHistorySupport jobHistorySupport;

   private final JobLectureService jobLectureService;

   private final JobRequestSupport jobRequestSupport;

   private static final Logger LOG = LoggerFactory
         .getLogger(JobQueueServiceImpl.class);

   /**
    * @param jobRequestDao
    *          DAO de la colonne famille JobRequest
    * @param jobsQueueDao
    *          DAO de la colonne famille JobsQueue
    * @param jobClockSupport
    *          Support pour le calcul de l'horloge sur Cassandra
    * @param jobHistoryDao
    *          DAO de la colonne famille JobHistory
    * @param jobLectureService
    *          service de lecture de la pile des travaux
    * @param curatorClient
    *          support pour l'utilisation de {@link ZookeeperMutex}
    */
   @Autowired
   public JobQueueServiceThriftImpl(final JobRequestSupport jobRequestSupport,
                                    final JobsQueueSupport jobsQueueSupport, final JobClockSupport jobClockSupport,
                                    final JobHistorySupport jobHistorySupport, final JobLectureService jobLectureService,
                                    final CuratorFramework curatorClient) {

      this.curatorClient = curatorClient;
      this.jobClockSupport = jobClockSupport;
      this.jobLectureService = jobLectureService;
      // this.jobRequestSupport = jobRequestDao;

      this.jobRequestSupport = jobRequestSupport;
      this.jobsQueueSupport = jobsQueueSupport;
      this.jobHistorySupport = jobHistorySupport;

   }

   /**
    * Time-out du lock, en secondes
    */
   private static final int LOCK_TIME_OUT = 20;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addJob(final JobToCreate jobToCreate) {

      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      final long clock = jobClockSupport.currentCLock();

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.ajouterJobDansJobRequest(jobToCreate, clock);

      // Ecriture dans la CF "JobQueues"
      addJobQueue(jobToCreate, clock);

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "CREATION DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(jobToCreate.getIdJob(),
                                     timestampTrace,
                                     messageTrace,
                                     clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void endingJob(final UUID idJob, final boolean succes,
                               final Date dateFinTraitement, final String message, final String codeTraitement)
                                     throws JobInexistantException {
      this.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, -1);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement,
                         final String message, final String codeTraitement, final int nbDocumentTraite)
                               throws JobInexistantException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // TODO: Vérifier que le job est à l'état STARTING

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      final long clock = jobClockSupport.currentCLock(columnState);

      // Lecture des propriétés du job dont on a besoin
      final String reservedBy = jobRequest.getReservedBy();

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.passerEtatTermineJobRequest(idJob,
                                                    dateFinTraitement,
                                                    succes,
                                                    message,
                                                    nbDocumentTraite,
                                                    clock);

      // Gestion du succès de la reprise de masse
      if (jobRequest.getType().equals(Constantes.REPRISE_MASSE_JN)) {
         final String idTraitementAReprendre = jobRequest.getJobParameters().get(
                                                                                 Constantes.ID_TRAITEMENT_A_REPRENDRE);
         final UUID idJobAReprendre = UUID.fromString(idTraitementAReprendre);
         if (succes) {
            final JobRequest jobAReprendre = jobLectureService
                  .getJobRequest(idJobAReprendre);
            final String cdTraitement = jobAReprendre.getJobParameters().get(
                                                                             Constantes.CODE_TRAITEMENT);

            // Passer le job à l'état REPLAY_SUCCESS
            final Date dateReprise = new Date();
            changerEtatJobRequest(idJobAReprendre,
                                  JobState.REPLAY_SUCCESS.name(),
                                  dateReprise,
                                  "Repris avec succes");
            // Supprimer le sémaphore du traitement repris
            jobsQueueSupport.supprimerCodeTraitementDeJobsQueues(
                                                                 idJobAReprendre, succes, cdTraitement, clock);
         }

         // Renseigne le nombre de documents traités par le traitement de masse
         if (nbDocumentTraite > 0) {
            renseignerDocCountTraiteJob(idJobAReprendre, nbDocumentTraite);
         }
      }

      // Ecriture dans la CF "JobQueues" pour hostname
      jobsQueueSupport.supprimerJobDeJobsQueues(idJob, reservedBy, clock);

      // Ecriture dans la CF "JobQueues" pour semaphore code traitement
      jobsQueueSupport.supprimerCodeTraitementDeJobsQueues(idJob,
                                                           succes,
                                                           codeTraitement,
                                                           clock);

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "FIN DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(idJob,
                                     timestampTrace,
                                     messageTrace,
                                     clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void endingJob(final UUID idJob, final boolean succes,
                               final Date dateFinTraitement)
                                     throws JobInexistantException {
      endingJob(idJob, succes, dateFinTraitement, null, null);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(final UUID idJob, final String hostname,
                                final Date dateReservation)
                                      throws JobDejaReserveException,
                                      JobInexistantException, LockTimeoutException {

      Assert.notNull(idJob, "L'id du job ne doit pas être null");

      final ZookeeperMutex mutex = new ZookeeperMutex(curatorClient, "/JobRequest/"
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
         final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
         // Vérifier que le job existe
         if (jobRequest == null) {
            throw new JobInexistantException(idJob);
         }

         // Vérifier que le job n'est pas déjà réservé
         // on s'appuie sur la date de réservation qui dans ce cas est renseigné
         if (jobRequest.getReservationDate() != null) {
            throw new JobDejaReserveException(idJob, jobRequest.getReservedBy());
         }

         // TODO: Vérifier que le job est à l'état CREATED

         // Lecture du job
         final ColumnFamilyResult<UUID, String> result = jobRequestSupport
               .getJobRequestTmpl().queryColumns(idJob);

         // Récupération de la colonne "state"
         final HColumn<?, ?> columnState = result
               .getColumn(JobRequestDao.JR_STATE_COLUMN);

         // Timestamp de l'opération
         // Il faut vérifier le décalage de temps
         final long clock = jobClockSupport.currentCLock(columnState);

         // Lecture des propriétés du job dont on a besoin
         final String type = jobRequest.getType();
         final String parameters = jobRequest.getParameters();

         // Ecriture dans la CF "JobRequest"
         jobRequestSupport.reserverJobDansJobRequest(idJob,
                                                     hostname,
                                                     dateReservation,
                                                     clock);

         // Ecriture dans la CF "JobQueues"
         if (parameters == null) {
            reserverJobDansJobQueues(idJob,
                                     hostname,
                                     type,
                                     jobRequest.getJobParameters(),
                                     clock);
         } else {
            jobsQueueSupport.reserverJobDansJobQueues(idJob,
                                                      hostname,
                                                      type,
                                                      parameters,
                                                      clock);
         }

         // Ecriture dans la CF "JobHistory"
         final String messageTrace = "RESERVATION DU JOB";
         final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
         jobHistorySupport.ajouterTrace(idJob,
                                        timestampTrace,
                                        messageTrace,
                                        clock);

         // On vérifie qu'on a toujours le lock. Si oui, la réservation a
         // réellement fonctionné
         checkLock(mutex, idJob, hostname);

      }
      finally {
         mutex.release();
      }
   }

   /**
    * Réserver un traitement de type JobsQueue dans la pile des travaux.
    *
    * @param idJob
    *          identifiant du traitement
    * @param hostname
    *          nom du serveur
    * @param type
    *          type du job
    * @param jobParameters
    *          Parametres du job
    * @param clock
    *          horloge
    */

   private void reserverJobDansJobQueues(final UUID idJob, final String hostname,
                                         final String type, final Map<String, String> jobParameters, final Long clock) {
      jobsQueueSupport.reserverJobDansJobQueues(idJob,
                                                hostname,
                                                type,
                                                jobParameters,
                                                clock);
   }

   /**
    * Après la réservation d'un job, on vérifie que le lock est encore valide
    *
    * @param mutex
    *          Le mutex utilisé pour le lock
    * @param idJob
    *          Id du job réservé
    * @param hostname
    *          Nom du serveur qui tente la réservation
    * @throws JobDejaReserveException
    *           Si le lock n'est plus valide et qu'on s'est fait subtilisé le
    *           job
    */
   private void checkLock(final ZookeeperMutex mutex, final UUID idJob, final String hostname)
         throws JobDejaReserveException {
      // On vérifie qu'on a toujours le lock. Si oui, la réservation a
      // réellement fonctionné
      if (mutex.isObjectStillLocked(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
         // C'est bon, le job est réellement réservé
         return;
      } else {
         // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne
         // devrait jamais arriver.
         final String message = "Erreur lors de la tentative d'acquisition du lock pour le jobRequest "
               + idJob + ". Problème de connexion zookeeper ?";
         LOG.error(message);

         // On regarde si le job a été réservé par un autre serveur
         final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
         final String currentHostname = jobRequest.getReservedBy();
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
   public final void startingJob(final UUID idJob, final Date dateDebutTraitement)
         throws JobInexistantException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // TODO: Vérifier que le job est à l'état RESERVED

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      final long clock = jobClockSupport.currentCLock(columnState);

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.passerEtatEnCoursJobRequest(idJob,
                                                    dateDebutTraitement,
                                                    clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "DEMARRAGE DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(idJob,
                                     timestampTrace,
                                     messageTrace,
                                     clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {

      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      final long clock = jobClockSupport.currentCLock();

      // Ecriture dans la CF "JobRequest"
      // rien à écrire

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      jobHistorySupport
      .ajouterTrace(jobUuid, timeUuid, description, clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void renseignerPidJob(final UUID idJob, final Integer pid)
         throws JobInexistantException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération du timestamp courant de la colonne "pid", si elle est
      // présente

      // Récupération de la colonne "pid"
      final HColumn<?, ?> columnPid = result.getColumn(JobRequestDao.JR_PID);

      long clock;

      if (columnPid == null) {

         clock = jobClockSupport.currentCLock();

      } else {

         clock = jobClockSupport.currentCLock(columnPid);

      }

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.renseignerPidDansJobRequest(idJob, pid, clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "PID RENSEIGNE";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(idJob,
                                     timestampTrace,
                                     messageTrace,
                                     clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void renseignerDocCountJob(final UUID idJob, final Integer nbDocs)
         throws JobInexistantException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération du timestamp courant de la colonne "docCount", si elle est
      // présente

      // Récupération de la colonne "docCount"
      final HColumn<?, ?> columnDocCount = result.getColumn(JobRequestDao.JR_DOC_COUNT);

      long clock;

      if (columnDocCount == null) {

         clock = jobClockSupport.currentCLock();

      } else {

         clock = jobClockSupport.currentCLock(columnDocCount);

      }

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.renseignerDocCountDansJobRequest(idJob, nbDocs, clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "DOC_COUNT RENSEIGNE";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(idJob,
                                     timestampTrace,
                                     messageTrace,
                                     clock);

   }

   /**
    * Renseigne le nombre de docs traités par le traitement de masse dans la
    * pile des travaux.
    *
    * @param idJob
    *          identifiant du job
    * @param nbDocs
    *          Nombre de docs traités
    * @throws JobInexistantException
    *           le traitement n'existe pas
    */
   private final void renseignerDocCountTraiteJob(final UUID idJob, final Integer nbDocs)
         throws JobInexistantException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération du timestamp courant de la colonne "docCount", si elle est
      // présente

      // Récupération de la colonne "docCount"
      final HColumn<?, ?> columnDocCount = result
            .getColumn(JobRequestDao.JR_DOC_COUNT_TRAITE);

      long clock;

      if (columnDocCount == null) {

         clock = jobClockSupport.currentCLock();

      } else {

         clock = jobClockSupport.currentCLock(columnDocCount);

      }

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.renseignerDocCountTraiteDansJobRequest(idJob,
                                                               nbDocs,
                                                               clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "DOC_COUNT_TRAITE RENSEIGNE";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(idJob,
                                     timestampTrace,
                                     messageTrace,
                                     clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag,
                                       final String raison)
                                             throws JobInexistantException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération du timestamp courant de la colonne "toCheckFlag", si elle
      // est
      // présente

      // Récupération de la colonne "toCheckFlag"
      final HColumn<?, ?> columnToCheckFlag = result
            .getColumn(JobRequestDao.JR_TO_CHECK_FLAG);

      long clock;

      if (columnToCheckFlag == null) {

         clock = jobClockSupport.currentCLock();

      } else {

         clock = jobClockSupport.currentCLock(columnToCheckFlag);

      }

      // Ecriture dans la CF "JobRequest"
      jobRequestSupport.renseignerCheckFlagDansJobRequest(idJob,
                                                          toCheckFlag,
                                                          raison,
                                                          clock);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String message = "TOCHECKFLAG POSITIONNE A {0} AVEC LA RAISON {1}";
      final String messageTrace = MessageFormat.format(message, toCheckFlag, raison);
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupport.ajouterTrace(idJob,
                                     timestampTrace,
                                     messageTrace,
                                     clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void deleteJob(final UUID idJob) {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      final long clock = jobClockSupport.currentCLock(columnState);

      // Suppression de la CF "JobRequest"
      jobRequestSupport.deleteJobRequest(idJob, clock);

      // Suppression de la CF "JobQueues"
      final String reservedBy = jobRequest.getReservedBy();

      jobsQueueSupport.supprimerJobDeJobsAllQueues(idJob,
                                                   reservedBy,
                                                   clock);

      // Suppression de la CF "JobHistory"
      jobHistorySupport.supprimerHistorique(idJob, clock);

   }

   /**
    * {@inheritDoc}
    *
    * @throws JobNonReinitialisableException
    *           Exception levé si l'état du job ne permet pas de le relancer
    *           (différent de RESERVED ou STARTING)
    */
   @Override
   public final void resetJob(final UUID idJob) throws JobNonReinitialisableException {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      final String etat = jobRequest.getState().toString();
      if ("RESERVED".equals(etat) || "STARTING".equals(etat)) {

         // Lecture des propriétés du job dont on a besoin
         final String type = jobRequest.getType();
         final String parameters = jobRequest.getParameters();
         final String reservedBy = jobRequest.getReservedBy();

         // Timestamp de l'opération
         // Il faut vérifier le décalage de temps
         final long clock = jobClockSupport.currentCLock(columnState);

         // Ecriture dans la CF "JobRequest"
         jobRequestSupport.resetJob(idJob, etat, clock);

         if (parameters == null) {
            jobsQueueSupport.unreservedJob(idJob,
                                           type,
                                           jobRequest
                                           .getJobParameters(),
                                           reservedBy,
                                           clock);

         } else {
            jobsQueueSupport.unreservedJob(idJob,
                                           type,
                                           parameters,
                                           reservedBy,
                                           clock);
         }

         // Ecriture dans la CF "JobHistory"
         final String messageTrace = "RESET DU JOB";
         final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
         jobHistorySupport.ajouterTrace(idJob,
                                        timestampTrace,
                                        messageTrace,
                                        clock);

      } else {
         throw new JobNonReinitialisableException(idJob);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> getHosts() {
      return jobsQueueSupport.getHosts();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addJobsQueue(final JobToCreate jobToCreate) {
      addJobQueue(jobToCreate, null);
   }

   /**
    * Ajouter un job de type JobsQueue dans la pile des travaux.
    *
    * @param jobToCreate
    *          Job à créer.
    * @param clock
    *          horloge.
    */
   private void addJobQueue(final JobToCreate jobToCreate, Long clock) {

      if (clock == null) {
         // Timestamp de l'opération
         // Pas besoin de gérer le décalage ici : on ne fait que la création
         clock = jobClockSupport.currentCLock();
      }

      if (StringUtils.isNotBlank(jobToCreate.getParameters())) {
         jobsQueueSupport.ajouterJobDansJobQueuesEnWaiting(
                                                           jobToCreate.getIdJob(),
                                                           jobToCreate.getType(),
                                                           jobToCreate.getParameters(),
                                                           clock);
      } else {
         jobsQueueSupport.ajouterJobDansJobQueuesEnWaiting(
                                                           jobToCreate.getIdJob(),
                                                           jobToCreate.getType(),
                                                           jobToCreate.getJobParameters(),
                                                           clock);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void reserverJobDansJobsQueues(final UUID idJob, final String hostname,
                                         final String type, final Map<String, String> jobParameters) {
      final long clock = jobClockSupport.currentCLock();
      reserverJobDansJobQueues(idJob, hostname, type, jobParameters, clock);
   }

   @Override
   public final void deleteJobFromJobsQueues(final UUID idJob) {

      final JobRequest jobRequest = jobLectureService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      final long clock = jobClockSupport.currentCLock(columnState);

      // Suppression de la CF "JobQueues"
      final String reservedBy = jobRequest.getReservedBy();
      jobsQueueSupport.supprimerJobDeJobsAllQueues(idJob, reservedBy, clock);
   }

   @Override
   public final void deleteJobAndSemaphoreFromJobsQueues(final UUID uuidJob, final String codeTraitement) {

      final JobRequest jobRequest = jobLectureService.getJobRequest(uuidJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(uuidJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      final long clock = jobClockSupport.currentCLock(columnState);

      // Suppression de la CF "JobQueues"
      final String reservedBy = jobRequest.getReservedBy();
      jobsQueueSupport.supprimerJobDeJobsAllQueues(uuidJob, reservedBy, clock);

      final String SemaphoreReserved = Constantes.PREFIXE_SEMAPHORE_JOB + codeTraitement;
      jobsQueueSupport.supprimerJobDeJobsAllQueues(uuidJob, SemaphoreReserved, clock);

   }

   @Override
   public final void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate,
                                           final String message) {
      // Lecture du job
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport
            .getJobRequestTmpl().queryColumns(idJob);

      // Récupération de la colonne "state"
      final HColumn<?, ?> columnState = result
            .getColumn(JobRequestDao.JR_STATE_COLUMN);

      // Timestamp de l'opération
      final long clock = jobClockSupport.currentCLock(columnState);
      jobRequestSupport.changerEtatJobRequest(idJob, stateJob, endingDate, message, clock);
   }

}
