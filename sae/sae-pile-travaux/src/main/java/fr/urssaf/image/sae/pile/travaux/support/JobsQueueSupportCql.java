package fr.urssaf.image.sae.pile.travaux.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;

/**
 * Support pour l'utilisation de {@link JobsQueueDao}
 */
@Component
public class JobsQueueSupportCql {

  /**
   * Valeur de la clé pour les jobs en attente de réservation
   */
  private static final String JOBS_WAITING_KEY = "jobsWaiting";

  private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours

  @Autowired
  private IJobsQueueDaoCql jobsQueueDaoCql;

  /**
   * @param jobsQueueDao
   *          DAO de la colonne famille JobsQueue
   */
  public JobsQueueSupportCql() {

    // this.jobsQueueDaoCql = jobsQueueDaoCql;

  }

  /**
   * Ajoute un job en attente.
   *
   * @param idJob
   *          identifiant du job
   * @param type
   *          type de job
   * @param jobParameters
   *          Objet contenant tous les paramètres du job
   * @param clock
   *          horloge de l'ajout du job en attente
   */
  public final void ajouterJobDansJobQueuesEnWaiting(final UUID idJob, final String type,
                                                     final Map<String, String> jobParameters, final long clock) {

    final JobQueueCql jobQueue = createNewJobQueue(idJob, type, jobParameters, JOBS_WAITING_KEY);

    this.jobsQueueDaoCql.saveWithMapper(jobQueue, TTL, clock);
  }

  /**
   * Réservation du job : suppression de la file d'attente et ajout dans la
   * file du serveur qui a réservé le job.
   *
   * @param idJob
   *          identifiant du job
   * @param reservedBy
   *          Hostname ou IP du serveur qui réservé le job
   * @param type
   *          type du job
   * @param jobParameters
   *          Objet contenant tous les paramètres du job
   * @param clock
   *          horloge de réservation du job
   */
  public final void reserverJobDansJobQueues(final UUID idJob, final String reservedBy,
                                             final String type, final Map<String, String> jobParameters, final long clock) {

    // Dans la CF JobQueues, on "switch" le job entre :
    // - la clé "jobsWaiting" (suppression)
    // - la clé "valeur de reservedBy" (création)
    final JobQueueCql jobQueue = createNewJobQueue(idJob, type, jobParameters, reservedBy);
    this.jobsQueueDaoCql.saveWithMapper(jobQueue, TTL, clock);

  }

  /**
   * Suppression du job de la file d'exécution/réservation du job.
   *
   * @param idJob
   *          identifiant du job
   * @param reservedBy
   *          Hostname ou IP du serveur qui a réservé/exécuté le job
   * @param clock
   *          horloge de suppression du job de la file d'exécution/réservation
   */
  public final void supprimerJobDeJobsAllQueues(final UUID idJob, final String reservedBy,
                                                final long clock) {

    // Opération 1: Suppression du job de la liste de la file d'attente
    this.jobsQueueDaoCql.deleteByIdAndIndexColumn(idJob, reservedBy, clock);

    // Opération 2: Suppression du job de la liste des jobs non réservé JOBS_WAITING_KEY
    this.jobsQueueDaoCql.deleteByIdAndIndexColumn(idJob, JOBS_WAITING_KEY, clock);

  }

  /**
   * Suppression du job de la liste des jobs reservés
   *
   * @param idJob
   * @param reservedBy
   * @param clock
   */
  public final void deleteJobFromJobsReserved(final UUID idJob, final String reservedBy,
                                              final long clock) {

    // Opération 1: Suppression du job de la liste de la file d'attente
    if (StringUtils.isNotEmpty(reservedBy)) {
      this.jobsQueueDaoCql.deleteByIdAndIndexColumn(idJob, reservedBy, clock);
    }
  }

  /**
   * Met à jour le Job afin qu’il soit à nouveau éligible au lancement par
   * l’ordonnanceur en le replaçant dans la liste des jobs en attente
   *
   * @param idJob
   *          Identifiant du document à mettre à jour
   * @param jobParameters
   *          Objet contenant tous les paramètres du job
   * @param type
   *          type du job
   * @param hote
   *          Nom de l’hôte ayant lancé le traitement
   * @param clock
   *          horloge de déréservation
   */
  public final void unreservedJob(final UUID idJob, final String type,
                                  final Map<String, String> jobParameters, final String hote, final long clock) {

    final JobQueueCql jobQueue = createNewJobQueue(idJob, type, jobParameters, JOBS_WAITING_KEY);
    this.jobsQueueDaoCql.saveWithMapper(jobQueue, TTL, clock);

  }

  /**
   * Recupere la liste des serveurs en cours de traitement du job
   *
   * @return List<String> liste des serveurs
   */
  public final List<String> getHosts() {
    final List<String> hosts = new ArrayList<String>();

    final Iterator<JobQueueCql> it = this.jobsQueueDaoCql.findAll();

    while (it.hasNext()) {
      final JobQueueCql jq = it.next();
      if (!JOBS_WAITING_KEY.equals(jq.getJobsituation())) {
        hosts.add(jq.getJobsituation());
      }
    }

    return hosts;
  }

  /**
   * Suppression du job portant le code traitement (sémaphore) de la file
   * d'exécution/réservation du job.
   *
   * @param idJob
   *          identifiant du job
   * @param succes
   *          True si job en succes, false sinon
   * @param codeTraitement
   *          Code traitement
   * @param clock
   *          horloge de suppression du job de la file d'exécution/réservation
   */
  public void supprimerCodeTraitementDeJobsQueues(final UUID idJob, final boolean succes,
                                                  final String codeTraitement, final long clock) {
    // Si le traitement du job est en succès, on supprime la colonne avec code traitement.
    // Si le traitement du job est en erreur, on laisse la colonne avec code traitement.
    if (succes && (codeTraitement != null && !codeTraitement.isEmpty())) {

      // Opération 1 : Suppression du job de la liste de la file d'attente
      this.jobsQueueDaoCql.deleteByIdAndIndexColumn(idJob, Constantes.PREFIXE_SEMAPHORE_JOB + codeTraitement, clock);

    }
  }

  public Iterator<JobQueueCql> getUnreservedJobRequest() {
    return jobsQueueDaoCql.getUnreservedJobRequest();
  }

  public List<JobQueueCql> getNonTerminatedSimpleJobs(final String hostname) {

    final List<JobQueueCql> listJQ = new ArrayList<>();
    final Iterator<JobQueueCql> it = jobsQueueDaoCql.getNonTerminatedSimpleJobs(hostname);
    while (it.hasNext()) {
      listJQ.add(it.next());
    }
    return listJQ;
  }

  /**
   * @param idJob
   * @param type
   * @param jobParameters
   * @return
   */
  private JobQueueCql createNewJobQueue(final UUID idJob, final String type, final Map<String, String> jobParameters, final String situation) {
    final JobQueueCql jobQueue = new JobQueueCql();
    jobQueue.setIdJob(idJob);
    jobQueue.setJobsituation(situation);
    jobQueue.setType(type);
    jobQueue.setJobParameters(jobParameters);
    return jobQueue;
  }

  public void supprimerJobDeJobsQueues(final UUID id) {
    this.jobsQueueDaoCql.deleteById(id);
  }

}
