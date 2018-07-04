package fr.urssaf.image.sae.pile.travaux.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Support pour l'utilisation de {@link JobsQueueDao}
 */
@Component
public class JobsQueueSupport {

  /**
   * Valeur de la clé pour les jobs en attente de réservation
   */
  private static final String JOBS_WAITING_KEY = "jobsWaiting";

  @Autowired
  private JobsQueueDao jobsQueueDao;

  /**
   * @param jobsQueueDao
   *          DAO de la colonne famille JobsQueue
   */
  public JobsQueueSupport() {

    // this.jobsQueueDao = jobsQueueDao;

  }

  /**
   * Ajoute un job en attente.
   *
   * @param idJob
   *          identifiant du job
   * @param type
   *          type de job
   * @param parameters
   *          paramètres du job
   * @param clock
   *          horloge de l'ajout du job en attente
   */
  public final void ajouterJobDansJobQueuesEnWaiting(final UUID idJob, final String type,
                                                     final String parameters, final long clock) {

    // On utilise un ColumnFamilyUpdater, et on renseigne
    // la valeur de la clé dans la construction de l'updater
    final ColumnFamilyUpdater<String, UUID> updaterJobQueues = this.jobsQueueDao
                                                                                .getJobsQueueTmpl().createUpdater(JOBS_WAITING_KEY);

    // Ecriture des colonnes
    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(idJob);
    jobQueue.setType(type);
    jobQueue.setParameters(parameters);
    this.jobsQueueDao.ecritColonneJobQueue(updaterJobQueues,
                                           idJob,
                                           jobQueue,
                                           clock);

    // Ecrit en base
    this.jobsQueueDao.getJobsQueueTmpl().update(updaterJobQueues);

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

    // On utilise un ColumnFamilyUpdater, et on renseigne
    // la valeur de la clé dans la construction de l'updater
    final ColumnFamilyUpdater<String, UUID> updaterJobQueues = this.jobsQueueDao
                                                                                .getJobsQueueTmpl().createUpdater(JOBS_WAITING_KEY);

    // Ecriture des colonnes
    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(idJob);
    jobQueue.setType(type);
    jobQueue.setJobParameters(jobParameters);
    this.jobsQueueDao.ecritColonneJobQueue(updaterJobQueues,
                                           idJob,
                                           jobQueue,
                                           clock);

    // Ecrit en base
    this.jobsQueueDao.getJobsQueueTmpl().update(updaterJobQueues);

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
   * @param parameters
   *          paramètres du job
   * @param clock
   *          horloge de réservation du job
   */
  public final void reserverJobDansJobQueues(final UUID idJob, final String reservedBy,
                                             final String type, final String parameters, final long clock) {

    // Dans la CF JobQueues, on "switch" le job entre :
    // - la clé "jobsWaiting" (suppression)
    // - la clé "valeur de reservedBy" (création)

    // Pour cela, on utilise un Mutator pour réaliser en "batch" les
    // deux opérations

    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    // Opération 1: Ajout du job pour le serveur qui l'a réservé
    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(idJob);
    jobQueue.setType(type);
    jobQueue.setParameters(parameters);
    this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator,
                                                      reservedBy,
                                                      jobQueue,
                                                      clock);

    // Opération 2: Suppression du job de la liste des jobs non réservé
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        JOBS_WAITING_KEY,
                                                        idJob,
                                                        clock);

    // Exécution des 2 opérations
    mutator.execute();

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

    // Pour cela, on utilise un Mutator pour réaliser en "batch" les
    // deux opérations

    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    // Opération 1: Ajout du job pour le serveur qui l'a réservé
    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(idJob);
    jobQueue.setType(type);
    jobQueue.setJobParameters(jobParameters);
    this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator,
                                                      reservedBy,
                                                      jobQueue,
                                                      clock);

    // Opération 2: Suppression du job de la liste des jobs non réservé
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        JOBS_WAITING_KEY,
                                                        idJob,
                                                        clock);

    // Exécution des 2 opérations
    mutator.execute();

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
  public final void supprimerJobDeJobsQueues(final UUID idJob, final String reservedBy,
                                             final long clock) {

    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    // Opération 1: Suppression du job de la liste de la file d'attente
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        reservedBy,
                                                        idJob,
                                                        clock);

    // Opération 2: Suppression du job de la liste des jobs non réservé
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        JOBS_WAITING_KEY,
                                                        idJob,
                                                        clock);

    // Exécution de l'opération
    mutator.execute();

  }

  /**
   * Suppression du job de toutes les files
   *
   * @param idJob
   *          identifiant du job
   * @param reservedBy
   *          Hostname ou IP du serveur qui a réservé/exécuté le job,
   *          peut-être null
   * @param clock
   *          horloge de suppression du job de la file d'exécution/réservation
   */
  public final void supprimerJobDeJobsAllQueues(final UUID idJob, final String reservedBy,
                                                final long clock) {

    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    // Opération 1: Suppression du job de la liste de la file d'attente
    if (StringUtils.isNotEmpty(reservedBy)) {
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                          reservedBy,
                                                          idJob,
                                                          clock);
    }

    // Opération 2: Suppression du job de la liste des jobs non réservé
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        JOBS_WAITING_KEY,
                                                        idJob,
                                                        clock);

    // Exécution de l'opération
    mutator.execute();

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
    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    // Opération 1: Suppression du job de la liste de la file d'attente
    if (StringUtils.isNotEmpty(reservedBy)) {
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                          reservedBy,
                                                          idJob,
                                                          clock);
    }
  }

  /**
   * Met à jour le travail afin qu’il soit à nouveau éligible au lancement par
   * l’ordonnanceur en le replaçant dans la liste des jobs en attente
   *
   * @param idJob
   *          Identifiant du document à mettre à jour
   * @param hote
   *          Nom de l’hôte ayant lancé le traitement
   * @param parameters
   *          parametres du job
   * @param type
   *          type de job
   * @param clock
   *          horloge de la réservation
   */
  public final void unreservedJob(final UUID idJob, final String type, final String parameters,
                                  final String hote, final long clock) {

    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(idJob);
    jobQueue.setType(type);
    jobQueue.setParameters(parameters);

    // On ajoute le job dans jobsWaiting
    this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator,
                                                      JOBS_WAITING_KEY,
                                                      jobQueue,
                                                      clock);

    // On supprime le job dans le reservedBy correspondant
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        hote,
                                                        idJob,
                                                        clock);

    mutator.execute();

  }

  /**
   * Met à jour le travail afin qu’il soit à nouveau éligible au lancement par
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

    // Création du Mutator
    final Mutator<String> mutator = this.jobsQueueDao.createMutator();

    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(idJob);
    jobQueue.setType(type);
    jobQueue.setJobParameters(jobParameters);

    // On ajoute le job dans jobsWaiting
    this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator,
                                                      JOBS_WAITING_KEY,
                                                      jobQueue,
                                                      clock);

    // On supprime le job dans le reservedBy correspondant
    this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                        hote,
                                                        idJob,
                                                        clock);

    mutator.execute();

  }

  /**
   * Recupere la liste des serveurs qui ont deja traite au moins un job.
   *
   * @return List<String> liste des serveurs
   */
  public final List<String> getHosts() {
    final List<String> hosts = new ArrayList<String>();

    final RangeSlicesQuery<String, UUID, String> query = this.jobsQueueDao.createRangeSlicesQuery();
    query.setReturnKeysOnly();

    final QueryResult<OrderedRows<String, UUID, String>> resultat = query.execute();
    if (resultat.get() != null && resultat.get().getCount() > 0) {
      for (final Row<String, UUID, String> row : resultat.get().getList()) {
        if (!row.getKey().equals(JOBS_WAITING_KEY)) {
          hosts.add(row.getKey());
        }
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
      // Création du Mutator
      final Mutator<String> mutator = this.jobsQueueDao.createMutator();

      // Opération 1 : Suppression du job de la liste de la file d'attente
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
                                                          Constantes.PREFIXE_SEMAPHORE_JOB + codeTraitement,
                                                          idJob,
                                                          clock);

      // Exécution de l'opération
      mutator.execute();
    }
  }

  /**
   * @return
   */
  public SliceQuery<String, UUID, String> createSliceQuery() {
    return jobsQueueDao.createSliceQuery();
  }

  /**
   * @return
   */
  public ColumnFamilyTemplate<String, UUID> getJobsQueueTmpl() {
    // TODO Auto-generated method stub
    return jobsQueueDao.getJobsQueueTmpl();
  }
}
