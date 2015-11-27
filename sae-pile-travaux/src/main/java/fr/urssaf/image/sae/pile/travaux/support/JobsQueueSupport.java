package fr.urssaf.image.sae.pile.travaux.support;

import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

/**
 * Support pour l'utilisation de {@link JobsQueueDao}
 * 
 * 
 */
public class JobsQueueSupport {

   /**
    * Valeur de la clé pour les jobs en attente de réservation
    */
   private static final String JOBS_WAITING_KEY = "jobsWaiting";

   private final JobsQueueDao jobsQueueDao;

   /**
    * 
    * @param jobsQueueDao
    *           DAO de la colonne famille JobsQueue
    */
   public JobsQueueSupport(JobsQueueDao jobsQueueDao) {

      this.jobsQueueDao = jobsQueueDao;

   }

   /**
    * Ajoute un job en attente.
    * 
    * @param idJob
    *           identifiant du job
    * @param type
    *           type de job
    * @param parameters
    *           paramètres du job
    * @param clock
    *           horloge de l'ajout du job en attente
    */
   public final void ajouterJobDansJobQueuesEnWaiting(UUID idJob, String type,
         String parameters, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, UUID> updaterJobQueues = this.jobsQueueDao
            .getJobsQueueTmpl().createUpdater(JOBS_WAITING_KEY);

      // Ecriture des colonnes
      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setParameters(parameters);
      this.jobsQueueDao.ecritColonneJobQueue(updaterJobQueues, idJob, jobQueue,
            clock);

      // Ecrit en base
      this.jobsQueueDao.getJobsQueueTmpl().update(updaterJobQueues);

   }

   /**
    * Ajoute un job en attente.
    * 
    * @param idJob
    *           identifiant du job
    * @param type
    *           type de job
    * @param jobParameters
    *           Objet contenant tous les paramètres du job
    * @param clock
    *           horloge de l'ajout du job en attente
    */
   public final void ajouterJobDansJobQueuesEnWaiting(UUID idJob, String type,
         Map<String, String> jobParameters, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, UUID> updaterJobQueues = this.jobsQueueDao
            .getJobsQueueTmpl().createUpdater(JOBS_WAITING_KEY);

      // Ecriture des colonnes
      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setJobParameters(jobParameters);
      this.jobsQueueDao.ecritColonneJobQueue(updaterJobQueues, idJob, jobQueue,
            clock);

      // Ecrit en base
      this.jobsQueueDao.getJobsQueueTmpl().update(updaterJobQueues);

   }

   /**
    * Réservation du job : suppression de la file d'attente et ajout dans la
    * file du serveur qui a réservé le job.
    * 
    * @param idJob
    *           identifiant du job
    * @param reservedBy
    *           Hostname ou IP du serveur qui réservé le job
    * @param type
    *           type du job
    * @param parameters
    *           paramètres du job
    * @param clock
    *           horloge de réservation du job
    */
   public final void reserverJobDansJobQueues(UUID idJob, String reservedBy,
         String type, String parameters, long clock) {

      // Dans la CF JobQueues, on "switch" le job entre :
      // - la clé "jobsWaiting" (suppression)
      // - la clé "valeur de reservedBy" (création)

      // Pour cela, on utilise un Mutator pour réaliser en "batch" les
      // deux opérations

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      // Opération 1: Ajout du job pour le serveur qui l'a réservé
      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setParameters(parameters);
      this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator, reservedBy,
            jobQueue, clock);

      // Opération 2: Suppression du job de la liste des jobs non réservé
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
            JOBS_WAITING_KEY, idJob, clock);

      // Exécution des 2 opérations
      mutator.execute();

   }

   /**
    * Réservation du job : suppression de la file d'attente et ajout dans la
    * file du serveur qui a réservé le job.
    * 
    * @param idJob
    *           identifiant du job
    * @param reservedBy
    *           Hostname ou IP du serveur qui réservé le job
    * @param type
    *           type du job
    * @param jobParameters
    *           Objet contenant tous les paramètres du job
    * @param clock
    *           horloge de réservation du job
    */
   public final void reserverJobDansJobQueues(UUID idJob, String reservedBy,
         String type, Map<String, String> jobParameters, long clock) {

      // Dans la CF JobQueues, on "switch" le job entre :
      // - la clé "jobsWaiting" (suppression)
      // - la clé "valeur de reservedBy" (création)

      // Pour cela, on utilise un Mutator pour réaliser en "batch" les
      // deux opérations

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      // Opération 1: Ajout du job pour le serveur qui l'a réservé
      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setJobParameters(jobParameters);
      this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator, reservedBy,
            jobQueue, clock);

      // Opération 2: Suppression du job de la liste des jobs non réservé
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
            JOBS_WAITING_KEY, idJob, clock);

      // Exécution des 2 opérations
      mutator.execute();

   }

   /**
    * Suppression du job de la file d'exécution/réservation du job.
    * 
    * @param idJob
    *           identifiant du job
    * @param reservedBy
    *           Hostname ou IP du serveur qui a réservé/exécuté le job
    * @param clock
    *           horloge de suppression du job de la file d'exécution/réservation
    */
   public final void supprimerJobDeJobsQueues(UUID idJob, String reservedBy,
         long clock) {

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      // Opération 1: Suppression du job de la liste de la file d'attente
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator, reservedBy,
            idJob, clock);

      // Opération 2: Suppression du job de la liste des jobs non réservé
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
            JOBS_WAITING_KEY, idJob, clock);

      // Exécution de l'opération
      mutator.execute();

   }

   /**
    * Suppression du job de toutes les files
    * 
    * @param idJob
    *           identifiant du job
    * @param reservedBy
    *           Hostname ou IP du serveur qui a réservé/exécuté le job,
    *           peut-être null
    * @param clock
    *           horloge de suppression du job de la file d'exécution/réservation
    */
   public final void supprimerJobDeJobsAllQueues(UUID idJob, String reservedBy,
         long clock) {

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      // Opération 1: Suppression du job de la liste de la file d'attente
      if (StringUtils.isNotEmpty(reservedBy)) {
         this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
               reservedBy, idJob, clock);
      }

      // Opération 2: Suppression du job de la liste des jobs non réservé
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator,
            JOBS_WAITING_KEY, idJob, clock);

      // Exécution de l'opération
      mutator.execute();

   }

   /**
    * Met à jour le travail afin qu’il soit à nouveau éligible au lancement par
    * l’ordonnanceur en le replaçant dans la liste des jobs en attente
    * 
    * @param idJob
    *           Identifiant du document à mettre à jour
    * @param hote
    *           Nom de l’hôte ayant lancé le traitement
    * @param parameters
    *           parametres du job
    * @param type
    *           type de job
    * @param clock
    *           horloge de la réservation
    */
   public final void unreservedJob(UUID idJob, String type, String parameters,
         String hote, long clock) {

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setParameters(parameters);

      // On ajoute le job dans jobsWaiting
      this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator,
            JOBS_WAITING_KEY, jobQueue, clock);

      // On supprime le job dans le reservedBy correspondant
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator, hote, idJob,
            clock);

      mutator.execute();

   }

   /**
    * Met à jour le travail afin qu’il soit à nouveau éligible au lancement par
    * l’ordonnanceur en le replaçant dans la liste des jobs en attente
    * 
    * @param idJob
    *           Identifiant du document à mettre à jour
    * @param jobParameters
    *           Objet contenant tous les paramètres du job
    * @param type
    *           type du job
    * @param hote
    *           Nom de l’hôte ayant lancé le traitement
    * @param clock
    *           horloge de déréservation
    */
   public final void unreservedJob(UUID idJob, String type,
         Map<String, String> jobParameters, String hote, long clock) {

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setJobParameters(jobParameters);

      // On ajoute le job dans jobsWaiting
      this.jobsQueueDao.mutatorAjouterInsertionJobQueue(mutator,
            JOBS_WAITING_KEY, jobQueue, clock);

      // On supprime le job dans le reservedBy correspondant
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator, hote, idJob,
            clock);

      mutator.execute();

   }

}