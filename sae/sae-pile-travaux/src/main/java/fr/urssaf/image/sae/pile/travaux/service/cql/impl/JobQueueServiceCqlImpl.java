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

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.impl.JobHistoryDaoCqlImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.impl.JobRequestDaoCqlImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.impl.JobsQueueDaoCqlImpl;
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



  private static final String PID2 = "pid";
  private static final String DOC_COUNT = "docCount";
  private static final String TO_CHECK_FLAG = "toCheckFlag";
  private static final String DOC_COUNT_TRAITE = "docCountTraite";

  /**
   * TODO (AC75095028) Description du champ
   */
  private static final String STATE = "state";

  private final CuratorFramework curatorClient;

  private final JobClockSupport jobClockSupport;

  private final  JobsQueueSupportCql jobsQueueSupportCql;

  private final JobHistorySupportCql jobHistorySupportCql;

  private final  JobLectureCqlService jobLectureCqlService;

  private final  JobRequestSupportCql jobRequestSupportCql;

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
    this.jobLectureCqlService = jobLectureCqlService;
    this.jobRequestSupportCql = jobRequestSupportCql;
    this.jobsQueueSupportCql = jobsQueueSupportCql;
    this.jobHistorySupportCql = jobHistorySupportCql;
    this.jobClockSupport = jobClockSupport;

  }

  public JobQueueServiceCqlImpl(final CassandraCQLClientFactory ccf, final JobClockSupport jobClockSupport, final CuratorFramework curatorClient) {

    final IJobHistoryDaoCql jobHistoryDaoCql = new JobHistoryDaoCqlImpl(ccf);
    jobHistoryDaoCql.setCcf(ccf);
    final JobHistorySupportCql jobHistorySupportCql = new JobHistorySupportCql();
    jobHistorySupportCql.setJobHistoryDaoCql(jobHistoryDaoCql);

    final IJobRequestDaoCql jobRequestDaoCql = new JobRequestDaoCqlImpl(ccf);
    jobRequestDaoCql.setCcf(ccf);
    final JobRequestSupportCql jobRequestSupportCql = new JobRequestSupportCql();
    jobRequestSupportCql.setJobRequestDaoCql(jobRequestDaoCql);

    final IJobsQueueDaoCql jobsQueueDaoCql = new JobsQueueDaoCqlImpl(ccf);
    jobsQueueDaoCql.setCcf(ccf);
    final JobsQueueSupportCql jobsQueueSupportCql = new JobsQueueSupportCql();
    jobsQueueSupportCql.setJobsQueueDaoCql(jobsQueueDaoCql);

    this.jobClockSupport = jobClockSupport;
    this.curatorClient = curatorClient ;
    this.jobHistorySupportCql = jobHistorySupportCql;
    this.jobRequestSupportCql = jobRequestSupportCql;
    this.jobsQueueSupportCql = jobsQueueSupportCql;
    jobLectureCqlService = new JobLectureServiceCqlImpl(ccf);

  }

  @Override
  public void addJob(final JobToCreate jobToCreate) {

    // Timestamp de l'opération
    // Pas besoin de gérer le décalage ici : on ne fait que la création
    final long clock = jobClockSupport.currentCLock();

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.ajouterJobDansJobRequest(jobToCreate, clock);

    // Ecriture dans la CF "JobQueues"
    addJobsQueue(jobToCreate);

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "CREATION DU JOB";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(jobToCreate.getIdJob(),
                                      timestampTrace,
                                      messageTrace,
                                      clock);
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
      final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
      // Vérifier que le job existe
      if (jobRequest == null) {
        throw new JobInexistantException(idJob);
      }

      // Vérifier que le job n'est pas déjà réservé
      // on s'appuie sur la date de réservation qui dans ce cas est renseigné
      if (jobRequest.getReservationDate() != null) {
        throw new JobDejaReserveException(idJob, jobRequest.getReservedBy());
      }

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      // calcul du clock
      final long clock = getCurrentColumnClock(jobRequest, STATE);

      // Lecture des propriétés du job dont on a besoin
      final String type = jobRequest.getType();

      // Ecriture dans la CF "JobRequest"
      jobRequestSupportCql.reserverJobDansJobRequest(idJob,
                                                     hostname,
                                                     dateReservation,
                                                     clock);

      jobsQueueSupportCql.reserverJobDansJobQueues(idJob,
                                                   hostname,
                                                   type,
                                                   jobRequest.getJobParameters(),
                                                   clock);

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "RESERVATION DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupportCql.ajouterTrace(idJob,
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
      final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
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
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      throw new JobInexistantException(idJob);
    }

    final long clock = getCurrentColumnClock(jobRequest, STATE);

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.passerEtatEnCoursJobRequest(idJob,
                                                     dateDebutTraitement,
                                                     clock);

    // Ecriture dans la CF "JobQueues"
    // rien à écrire

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "DEMARRAGE DU JOB";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(idJob,
                                      timestampTrace,
                                      messageTrace,
                                      clock);
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
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      throw new JobInexistantException(idJob);
    }

    // TODO: Vérifier que le job est à l'état STARTING

    final long clock = getCurrentColumnClock(jobRequest, STATE);

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.passerEtatTermineJobRequest(idJob,
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
        final JobRequestCql jobAReprendre = jobLectureCqlService
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
        jobsQueueSupportCql.supprimerCodeTraitementDeJobsQueues(
                                                                idJobAReprendre,
                                                                succes,
                                                                cdTraitement,
                                                                clock);
      }

      // Renseigne le nombre de documents traités par le traitement de masse
      if (nbDocumentTraite > 0) {
        renseignerDocCountTraiteJob(idJobAReprendre, nbDocumentTraite);
      }
    }

    // Lecture des propriétés du job dont on a besoin
    final String reservedBy = jobRequest.getReservedBy();
    // Ecriture dans la CF "JobQueues" pour hostname
    jobsQueueSupportCql.supprimerJobDeJobsQueues(idJob, reservedBy, clock);

    // Ecriture dans la CF "JobQueues" pour semaphore code traitement
    jobsQueueSupportCql.supprimerCodeTraitementDeJobsQueues(idJob,
                                                            succes,
                                                            codeTraitement,
                                                            clock);

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "FIN DU JOB";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(idJob,
                                      timestampTrace,
                                      messageTrace,
                                      clock);
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

    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      throw new JobInexistantException(idJob);
    }

    // Timestamp de l'opération
    // Il faut vérifier le décalage de temps
    // calcul du clock
    final long clock = getCurrentColumnClock(jobRequest, DOC_COUNT_TRAITE);

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.renseignerDocCountTraiteDansJobRequest(idJob,
                                                                nbDocs,
                                                                clock);

    // Ecriture dans la CF "JobQueues"
    // rien à écrire

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "DOC_COUNT_TRAITE RENSEIGNE";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(idJob,
                                      timestampTrace,
                                      messageTrace,
                                      clock);

  }

  @Override
  public void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {

    // Timestamp de l'opération
    // Pas besoin de gérer le décalage ici : on ne fait que la création
    final long clock = jobClockSupport.currentCLock();

    // Ecriture dans la CF "JobHistory"
    jobHistorySupportCql
    .ajouterTrace(jobUuid, timeUuid, description, clock);
  }

  @Override
  public void renseignerPidJob(final UUID idJob, final Integer pid) throws JobInexistantException {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      throw new JobInexistantException(idJob);
    }

    // Timestamp de l'opération
    // Il faut vérifier le décalage de temps
    // calcul du clock
    final long clock = getCurrentColumnClock(jobRequest, PID2);

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.renseignerPidDansJobRequest(idJob, pid, clock);

    // Ecriture dans la CF "JobQueues"
    // rien à écrire

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "PID RENSEIGNE";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(idJob,
                                      timestampTrace,
                                      messageTrace,
                                      clock);
  }

  @Override
  public void renseignerDocCountJob(final UUID idJob, final Integer nbDocs) throws JobInexistantException {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      throw new JobInexistantException(idJob);
    }

    // Timestamp de l'opération
    // Il faut vérifier le décalage de temps
    // calcul du clock
    final long clock = getCurrentColumnClock(jobRequest, DOC_COUNT);

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.renseignerDocCountDansJobRequest(idJob, nbDocs, clock);

    // Ecriture dans la CF "JobQueues"
    // rien à écrire

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "DOC_COUNT RENSEIGNE";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(idJob,
                                      timestampTrace,
                                      messageTrace,
                                      clock);
  }

  @Override
  public void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag, final String raison) throws JobInexistantException {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      throw new JobInexistantException(idJob);
    }

    // Timestamp de l'opération
    // Il faut vérifier le décalage de temps
    // calcul du clock
    final long clock = getCurrentColumnClock(jobRequest, TO_CHECK_FLAG);

    // Ecriture dans la CF "JobRequest"
    jobRequestSupportCql.renseignerCheckFlagDansJobRequest(idJob,
                                                           toCheckFlag,
                                                           raison,
                                                           clock);

    // Ecriture dans la CF "JobQueues"
    // rien à écrire

    // Ecriture dans la CF "JobHistory"
    final String message = "TOCHECKFLAG POSITIONNE A {0} AVEC LA RAISON {1}";
    final String messageTrace = MessageFormat.format(message, toCheckFlag, raison);
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    jobHistorySupportCql.ajouterTrace(idJob,
                                      timestampTrace,
                                      messageTrace,
                                      clock);
  }

  @Override
  public void deleteJob(final UUID idJob) {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      return;
    }

    final long clock = getCurrentColumnClock(jobRequest, STATE);

    // Suppression de la CF "JobRequest"
    jobRequestSupportCql.deleteJobRequest(idJob, clock);

    // Suppression de la CF "JobQueues"
    final String reservedBy = jobRequest.getReservedBy();

    // if the job is reserved

    jobsQueueSupportCql.supprimerJobDeJobsAllQueues(idJob, clock);

    // Suppression de la CF "JobHistory"
    jobHistorySupportCql.supprimerHistorique(idJob, clock);
  }

  @Override
  public void resetJob(final UUID idJob) throws JobNonReinitialisableException {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      return;
    }

    final String etat = jobRequest.getState().toString();
    if ("RESERVED".equals(etat) || "STARTING".equals(etat)) {

      // Lecture des propriétés du job dont on a besoin
      final String type = jobRequest.getType();
      final String reservedBy = jobRequest.getReservedBy();

      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      // calcul du clock
      final long clock = getCurrentColumnClock(jobRequest, STATE);

      // Ecriture dans la CF "JobRequest"
      jobRequestSupportCql.resetJob(idJob, etat, clock);

      jobsQueueSupportCql.unreservedJob(idJob,
                                        type,
                                        jobRequest
                                        .getJobParameters(),
                                        reservedBy,
                                        clock);

      // Ecriture dans la CF "JobHistory"
      final String messageTrace = "RESET DU JOB";
      final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobHistorySupportCql.ajouterTrace(idJob,
                                        timestampTrace,
                                        messageTrace,
                                        clock);

    } else {
      throw new JobNonReinitialisableException(idJob);
    }
  }

  @Override
  public List<String> getHosts() {
    return jobsQueueSupportCql.getHosts();
  }

  @Override
  public void addJobsQueue(final JobToCreate jobToCreate) {
    addJobQueue(jobToCreate, null);
  }

  /**
   * Ajouter un job de type JobsQueue dans la pile des travaux.
   *
   * @param jobToCreate
   *           Job à créer.
   * @param clock
   *           horloge.
   */
  private void addJobQueue(final JobToCreate jobToCreate, final Long clock) {

    jobsQueueSupportCql.ajouterJobDansJobQueuesEnWaiting(jobToCreate.getIdJob(),
                                                         jobToCreate.getType(),
                                                         jobToCreate.getJobParameters(),
                                                         clock);
  }

  @Override
  public void reserverJobDansJobsQueues(final UUID idJob, final String hostname, final String type, final Map<String, String> jobParameters) {
    final long clock = jobClockSupport.currentCLock();
    reserverJobDansJobQueues(idJob, hostname, type, jobParameters, clock);
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
    jobsQueueSupportCql.reserverJobDansJobQueues(idJob,
                                                 hostname,
                                                 type,
                                                 jobParameters,
                                                 clock);
  }

  @Override
  public void deleteJobFromJobsQueues(final UUID idJob) {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      return;
    }

    final long clock = getCurrentColumnClock(jobRequest, STATE);

    // Suppression de la CF "JobQueues"
    final String reservedBy = jobRequest.getReservedBy();
    jobsQueueSupportCql.supprimerJobDeJobsAllQueues(idJob, clock);
  }

  @Override
  public void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate, final String message) {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(idJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      return;
    }
    final long clock = getCurrentColumnClock(jobRequest, STATE);

    jobRequestSupportCql.changerEtatJobRequest(idJob, stateJob, endingDate, message, clock);
  }

  @Override
  public void deleteJobAndSemaphoreFromJobsQueues(final UUID uuidJob, final String codeTraitement) {
    final JobRequestCql jobRequest = jobLectureCqlService.getJobRequest(uuidJob);
    // Vérifier que le job existe
    if (jobRequest == null) {
      return;
    }

    final long clock = getCurrentColumnClock(jobRequest, STATE);

    // Suppression de la CF "JobQueues"
    final String reservedBy = jobRequest.getReservedBy();
    jobsQueueSupportCql.supprimerJobDeJobsAllQueues(uuidJob, clock);

    final String SemaphoreReserved = Constantes.PREFIXE_SEMAPHORE_JOB + codeTraitement;
    jobsQueueSupportCql.supprimerJobDeJobsAllQueues(uuidJob, clock);
  }

  private long getCurrentColumnClock(final JobRequestCql jobRequest, final String columnName) {
    // Timestamp de l'opération
    // Il faut vérifier le décalage de temps
    // calcul du clock
    final long actualServerClock = jobClockSupport.currentCLock();
    final long columnClock = jobLectureCqlService.getJobRequestColunmWriteTime(jobRequest.getIdJob(), columnName);
    final long clock = jobClockSupport.getClock(actualServerClock, columnClock);
    return clock;
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
