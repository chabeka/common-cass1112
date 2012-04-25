package fr.urssaf.image.sae.pile.travaux.support;

import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;
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

   public JobsQueueSupport(JobsQueueDao jobsQueueDao) {

      this.jobsQueueDao = jobsQueueDao;

   }

   public void ajouterJobDansJobQueuesEnWaiting(UUID idJob, String type,
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

   public void reserverJobDansJobQueues(UUID idJob, String reservedBy,
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

   public void supprimerJobDeJobsQueues(UUID idJob, String reservedBy,
         long clock) {

      // Création du Mutator
      Mutator<String> mutator = this.jobsQueueDao.createMutator();

      // Opération unique : on supprime le job
      this.jobsQueueDao.mutatorAjouterSuppressionJobQueue(mutator, reservedBy,
            idJob, clock);

      // Exécution de l'opération
      mutator.execute();

   }

}
