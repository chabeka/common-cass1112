/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.cql.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobQueueServiceImpl;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupportCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

/**
 * Implémentation du service {@link JobQueueCqlService}
 */
@Service
public class JobQueueServiceCqlImpl implements JobQueueCqlService {

   private final CuratorFramework curatorClient;

   private final JobClockSupport jobClockSupport;

   private final JobsQueueSupportCql jobsQueueSupportCql;

   private final JobHistorySupportCql jobHistorySupportCql;

   private final JobLectureCqlService jobLectureCqlService;

   private final JobRequestSupportCql jobRequestSupportCql;

   /**
    * Time-out du lock, en secondes
    */
   private static final int LOCK_TIME_OUT = 20;

   private static final Logger LOG = LoggerFactory
                                                  .getLogger(JobQueueServiceImpl.class);

   /**
    * @param jobRequestSupportCql
    * @param jobsQueueSupportCql
    * @param jobClockSupport
    * @param jobHistorySupportCql
    * @param jobLectureCqlService
    * @param curatorClient
    */
   @Autowired
   public JobQueueServiceCqlImpl(final JobRequestSupportCql jobRequestSupportCql,
                                 final JobsQueueSupportCql jobsQueueSupportCql, final JobClockSupport jobClockSupport,
                                 final JobHistorySupportCql jobHistorySupportCql, final JobLectureCqlService jobLectureCqlService,
                                 final CuratorFramework curatorClient) {
      super();
      this.curatorClient = curatorClient;
      this.jobClockSupport = jobClockSupport;
      this.jobLectureCqlService = jobLectureCqlService;
      this.jobRequestSupportCql = jobRequestSupportCql;
      this.jobsQueueSupportCql = jobsQueueSupportCql;
      this.jobHistorySupportCql = jobHistorySupportCql;

   }

   @Override
   public void addJob(final JobToCreate jobToCreate) {
      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      final long clock = jobClockSupport.currentCLock();

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.ajouterJobDansJobRequest(jobToCreate, clock);

      // Ecriture dans la CF "JobQueues"
      this.addJobsQueue(jobToCreate);

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "CREATION DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(jobToCreate.getIdJob(),
                                             timestampTrace,
                                             messageTrace);
   }

   @Override
   public void reserveJob(final UUID idJob, final String hostname, final Date dateReservation)
         throws JobDejaReserveException, JobInexistantException, LockTimeoutException {
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
         final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
         // Vérifier que le job existe
         if (jobRequest == null) {
            throw new JobInexistantException(idJob);
         }

         // Vérifier que le job n'est pas déjà réservé
         // on s'appuie sur la date de réservation qui dans ce cas est renseigné
         if (jobRequest.getReservationDate() != null) {
            throw new JobDejaReserveException(idJob, jobRequest.getReservedBy());
         }

         // Lecture des propriétés du job dont on a besoin
         final String type = jobRequest.getType();
         final String parameters = jobRequest.getParameters();

         // Ecriture dans la CF "JobRequest"
         this.jobRequestSupportCql.reserverJobDansJobRequest(idJob,
                                                             hostname,
                                                             dateReservation,
                                                             0);

         this.jobsQueueSupportCql.reserverJobDansJobQueues(idJob,
                                                           hostname,
                                                           type,
                                                           jobRequest.getJobParameters(),
                                                           0);

         // Ecriture dans la CF "JobHistory"
         final String messageTrace = "RESERVATION DU JOB";
         final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
         this.jobHistorySupportCql.ajouterTrace(idJob,
                                                timestampTrace,
                                                messageTrace);

         // On vérifie qu'on a toujours le lock. Si oui, la réservation a
         // réellement fonctionné
         checkLock(mutex, idJob, hostname);
      }
      finally {
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
         final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
         // Vérifier que le job existe
         if (jobRequest == null) {
            return;
         }
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

   @Override
   public void startingJob(final UUID idJob, final Date dateDebutTraitement) throws JobInexistantException {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }
      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.passerEtatEnCoursJobRequest(idJob,
                                                            dateDebutTraitement,
                                                            0);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "DEMARRAGE DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(idJob,
                                             timestampTrace,
                                             messageTrace);
   }

   @Override
   public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement) throws JobInexistantException {
      endingJob(idJob, succes, dateFinTraitement, null, null);
   }

   @Override
   public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement)
         throws JobInexistantException {
      this.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, -1);
   }

   @Override
   public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement,
                         final int nbDocumentTraite)
         throws JobInexistantException {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // TODO: Vérifier que le job est à l'état STARTING

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.passerEtatTermineJobRequest(idJob,
                                                            dateFinTraitement,
                                                            succes,
                                                            message,
                                                            nbDocumentTraite,
                                                            0);

      // Gestion du succès de la reprise de masse
      if (jobRequest.getType().equals(Constantes.REPRISE_MASSE_JN)) {
         final String idTraitementAReprendre = jobRequest.getJobParameters().get(
                                                                                 Constantes.ID_TRAITEMENT_A_REPRENDRE);
         final UUID idJobAReprendre = UUID.fromString(idTraitementAReprendre);
         if (succes) {
            final JobRequestCql jobAReprendre = this.jobLectureCqlService
                                                                         .getJobRequest(idJobAReprendre);
            final String cdTraitement = jobAReprendre.getJobParameters().get(
                                                                             Constantes.CODE_TRAITEMENT);

            // Passer le job à l'état REPLAY_SUCCESS
            final Date dateReprise = new Date();
            this.changerEtatJobRequest(idJobAReprendre,
                                       JobState.REPLAY_SUCCESS.name(),
                                       dateReprise,
                                       "Repris avec succes");
            // Supprimer le sémaphore du traitement repris
            this.jobsQueueSupportCql.supprimerCodeTraitementDeJobsQueues(
                                                                         idJobAReprendre, succes, cdTraitement, 0);
         }

         // Renseigne le nombre de documents traités par le traitement de masse
         if (nbDocumentTraite > 0) {
            this.renseignerDocCountTraiteJob(idJobAReprendre, nbDocumentTraite);
         }
      }

      // Lecture des propriétés du job dont on a besoin
      final String reservedBy = jobRequest.getReservedBy();
      // Ecriture dans la CF "JobQueues" pour hostname
      this.jobsQueueSupportCql.supprimerJobDeJobsQueues(idJob, reservedBy, 0);

      // Ecriture dans la CF "JobQueues" pour semaphore code traitement
      this.jobsQueueSupportCql.supprimerCodeTraitementDeJobsQueues(idJob,
                                                                   succes,
                                                                   codeTraitement,
                                                                   0);

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "FIN DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(idJob,
                                             timestampTrace,
                                             messageTrace);
   }

   /**
    * Renseigne le nombre de docs traités par le traitement de masse dans la
    * pile des travaux.
    *
    * @param idJob
    *           identifiant du job
    * @param nbDocs
    *           Nombre de docs traités
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   private final void renseignerDocCountTraiteJob(final UUID idJob, final Integer nbDocs)
         throws JobInexistantException {

      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.renseignerDocCountTraiteDansJobRequest(idJob,
                                                                       nbDocs,
                                                                       0);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "DOC_COUNT_TRAITE RENSEIGNE";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(idJob,
                                             timestampTrace,
                                             messageTrace);

   }

   @Override
   public void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {
      // Ecriture dans la CF "JobHistory"
      this.jobHistorySupportCql
                               .ajouterTrace(jobUuid, timeUuid, description);
   }

   @Override
   public void renseignerPidJob(final UUID idJob, final Integer pid) throws JobInexistantException {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.renseignerPidDansJobRequest(idJob, pid, 0);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "PID RENSEIGNE";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(idJob,
                                             timestampTrace,
                                             messageTrace);
   }

   @Override
   public void renseignerDocCountJob(final UUID idJob, final Integer nbDocs) throws JobInexistantException {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.renseignerDocCountDansJobRequest(idJob, nbDocs, 0);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "DOC_COUNT RENSEIGNE";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(idJob,
                                             timestampTrace,
                                             messageTrace);
   }

   @Override
   public void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag, final String raison) throws JobInexistantException {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         throw new JobInexistantException(idJob);
      }

      // Ecriture dans la CF "JobRequest"
      this.jobRequestSupportCql.renseignerCheckFlagDansJobRequest(idJob,
                                                                  toCheckFlag,
                                                                  raison,
                                                                  0);

      // Ecriture dans la CF "JobQueues"
      // rien à écrire

      // Ecriture dans la CF "JobHistory"
      final String message = "TOCHECKFLAG POSITIONNE A {0} AVEC LA RAISON {1}";
      final String messageTrace = MessageFormat.format(message, toCheckFlag, raison);
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      this.jobHistorySupportCql.ajouterTrace(idJob,
                                             timestampTrace,
                                             messageTrace);
   }

   @Override
   public void deleteJob(final UUID idJob) {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Suppression de la CF "JobRequest"
      this.jobRequestSupportCql.deleteJobRequest(idJob, 0);

      // Suppression de la CF "JobQueues"
      final String reservedBy = jobRequest.getReservedBy();

      // if the job is reserved

      this.jobsQueueSupportCql.supprimerJobDeJobsAllQueues(idJob);

      // Suppression de la CF "JobHistory"
      this.jobHistorySupportCql.supprimerHistorique(idJob, 0);
   }

   @Override
   public void resetJob(final UUID idJob) throws JobNonReinitialisableException {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      final String etat = jobRequest.getState().toString();
      if ("RESERVED".equals(etat) || "STARTING".equals(etat)) {

         // Lecture des propriétés du job dont on a besoin
         final String type = jobRequest.getType();
         final String reservedBy = jobRequest.getReservedBy();

         // Ecriture dans la CF "JobRequest"
         this.jobRequestSupportCql.resetJob(idJob, etat, 0);

         this.jobsQueueSupportCql.unreservedJob(idJob,
                                                type,
                                                jobRequest
                                                          .getJobParameters(),
                                                reservedBy,
                                                0);

         // Ecriture dans la CF "JobHistory"
         final String messageTrace = "RESET DU JOB";
         final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
         this.jobHistorySupportCql.ajouterTrace(idJob,
                                                timestampTrace,
                                                messageTrace);

      } else {
         throw new JobNonReinitialisableException(idJob);
      }
   }

   @Override
   public List<String> getHosts() {
      return this.jobsQueueSupportCql.getHosts();
   }

   @Override
   public void addJobsQueue(final JobToCreate jobToCreate) {
      this.addJobQueue(jobToCreate, null);
   }

   /**
    * Ajouter un job de type JobsQueue dans la pile des travaux.
    *
    * @param jobToCreate
    *           Job à créer.
    * @param clock
    *           horloge.
    */
   private void addJobQueue(final JobToCreate jobToCreate, Long clock) {

      if (clock == null) {
         clock = jobClockSupport.currentCLock();
      }

      this.jobsQueueSupportCql.ajouterJobDansJobQueuesEnWaiting(jobToCreate.getIdJob(),
                                                                jobToCreate.getType(),
                                                                jobToCreate.getJobParameters(),
                                                                0);
   }

   @Override
   public void reserverJobDansJobsQueues(final UUID idJob, final String hostname, final String type, final Map<String, String> jobParameters) {
      this.reserverJobDansJobQueues(idJob, hostname, type, jobParameters, null);
   }

   /**
    * Réserver un traitement de type JobsQueue dans la pile des travaux.
    *
    * @param idJob
    *           identifiant du traitement
    * @param hostname
    *           nom du serveur
    * @param type
    *           type du job
    * @param jobParameters
    *           Parametres du job
    * @param clock
    *           horloge
    */
   private void reserverJobDansJobQueues(final UUID idJob, final String hostname,
                                         final String type, final Map<String, String> jobParameters, final Long clock) {
      this.jobsQueueSupportCql.reserverJobDansJobQueues(idJob,
                                                        hostname,
                                                        type,
                                                        jobParameters,
                                                        clock);
   }

   @Override
   public void deleteJobFromJobsQueues(final UUID idJob) {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Suppression de la CF "JobQueues"
      final String reservedBy = jobRequest.getReservedBy();
      this.jobsQueueSupportCql.supprimerJobDeJobsAllQueues(idJob);
   }

   @Override
   public void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate, final String message) {
      this.jobRequestSupportCql.changerEtatJobRequest(idJob, stateJob, endingDate, message, 0);
   }

   @Override
   public void deleteJobAndSemaphoreFromJobsQueues(final UUID uuidJob, final String codeTraitement) {
      final JobRequestCql jobRequest = this.jobLectureCqlService.getJobRequest(uuidJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
         return;
      }

      // Suppression de la CF "JobQueues"
      final String reservedBy = jobRequest.getReservedBy();
      this.jobsQueueSupportCql.supprimerJobDeJobsAllQueues(uuidJob);

      final String SemaphoreReserved = Constantes.PREFIXE_SEMAPHORE_JOB + codeTraitement;
      this.jobsQueueSupportCql.supprimerJobDeJobsAllQueues(uuidJob);
   }

   @Override
   public JobQueueCql getJobQueueByIndexedColumn(final UUID idjob) {
      final Optional<JobQueueCql> job = jobsQueueSupportCql.getJobQueueByIndexedColumn(idjob);
      if (job.isPresent()) {
         return job.get();
      }
      return null;
   }

}
