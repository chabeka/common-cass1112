package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;

/**
 * Service de la pile des travaux
 * 
 * 
 */
public interface JobQueueService {

   /**
    * Ajoute un traitement dans la pile des travaux
    * 
    * @param jobToCreate
    *           ensemble des données nécessaires à la création du job
    */
   void addJob(JobToCreate jobToCreate);

   /**
    * Réserve un traitement dans la pile des travaux
    * 
    * @param idJob
    *           identifiant du traitement
    * @param hostname
    *           nom du serveur
    * @param dateReservation
    *           date de réservation du traitement
    * @throws JobDejaReserveException
    *            le traitement est déjà réservé
    * @throws JobInexistantException
    *            le traitement n'existe pas
    * @throws LockTimeoutException
    *            timeout lors du lock
    */
   void reserveJob(UUID idJob, String hostname, Date dateReservation)
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException;

   /**
    * Met à jour un traitement avant de l'exécuter.<br>
    * 
    * @param idJob
    *           identifiant du traitement
    * @param dateDebutTraitement
    *           date d'exécution du traitement
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void startingJob(UUID idJob, Date dateDebutTraitement)
         throws JobInexistantException;

   /**
    * Met à jour le traitement après son exécution.<br>
    * 
    * @param idJob
    *           identifiant du traitement
    * @param succes
    *           valeur de retour de l'exécution du traitement
    * @param dateFinTraitement
    *           date de fin du traitement
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void endingJob(UUID idJob, boolean succes, Date dateFinTraitement)
         throws JobInexistantException;

   /**
    * Met à jour le traitement après son exécution.<br>
    * 
    * @param idJob
    *           identifiant du traitement
    * @param succes
    *           valeur de retour de l'exécution du traitement
    * @param dateFinTraitement
    *           date de fin du traitement
    * @param message
    *           message de compte-rendu du traitement (ex : message d'erreur)
    * @param codeTraitement
    *           Code traitement
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void endingJob(UUID idJob, boolean succes, Date dateFinTraitement,
         String message, String codeTraitement) throws JobInexistantException;

   /**
    * Ajoute une trace d'execution dans l'historique du job
    * 
    * @param jobUuid
    *           identifiant du job
    * @param timeUuid
    *           représentation du temps en uuid
    * @param description
    *           description de l'événement
    */
   void addHistory(UUID jobUuid, UUID timeUuid, String description);

   /**
    * Renseigne le PID du processus du traitement de masse dans la pile des
    * travaux
    * 
    * @param idJob
    *           identifiant du job
    * @param pid
    *           PID du processus
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void renseignerPidJob(UUID idJob, Integer pid) throws JobInexistantException;
   
   /**
    * Renseigne le nombre de docs traités par le traitement de masse dans la pile des
    * travaux
    * 
    * @param idJob
    *           identifiant du job
    * @param nbDocs
    *           Nombre de docs traités
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void renseignerDocCountJob(UUID idJob, Integer nbDocs) throws JobInexistantException;

   /**
    * Renseigne le flag de vérification du traitement de masse.<br>
    * 
    * @param idJob
    *           identifiant du job
    * @param toCheckFlag
    *           <code>true</code> le traitement doit être vérifié
    * @param raison
    *           message pour indiquer la raison de la vérification
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void updateToCheckFlag(UUID idJob, Boolean toCheckFlag, String raison)
         throws JobInexistantException;

   /**
    * Supprime un traitement de la pile des travaux
    * 
    * @param idJob
    *           identifiant du job
    */
   void deleteJob(UUID idJob);

   /**
    * Met à jour le travail afin qu'il soit à nouveau éligible au lancement par
    * l'ordonnanceur
    * 
    * @param idJob
    *           identifiant du job
    * @throws JobNonReinitialisableException
    *            exception levée lorsque le travail n'est pas réinitialisable
    */
   void resetJob(UUID idJob) throws JobNonReinitialisableException;
   
   /**
    * Methode permettant de recuperer la liste des serveurs qui ont deja traites
    * au moins un job.
    * 
    * @return List<String>
    */
   List<String> getHosts();
   
   /**
    * Ajouter un job de type JobsQueue dans la pile des travaux
    * @param jobToCreate Job à créer.
    */
   public void addJobsQueue(JobToCreate jobToCreate);

   /**
    * Réserver un traitement de type JobsQueue dans la pile des travaux
    * 
    * @param idJob
    *           identifiant du traitement
    * @param hostname
    *           nom du serveur
    * @param type
    *           type du job
    * @param jobParameters
    *           Parametres du job
    */
   public void reserverJobDansJobsQueues(UUID idJob, String hostname,
         String type, Map<String, String> jobParameters);

   
   /**
    * Supprimer le job de la pile des jobsQueue
    * @param idJob
    */
   void deleteJobFromJobsQueues(UUID idJob);   

   /**
    * Passer le jobRequest à l'état stateJob passé en paramètre
    * @param idJob
    *           identifiant du job
    * @param stateJob
    *           l'état cible du job
    * @param endingDate
    *           date de fin du job
    * @param message
    *           message de conclusion du job
    */
   void changerEtatJobRequest(UUID idJob, String stateJob, Date endingDate,
         String message);

   
   /**
    * Supprime le job et le sémaphore associé si il existe de la pile des jobsQueue
    * @param idJob
    *          identifiant du job
    * @param codeTraitement
    *          le code traitement du sémaphore
    */
   public void deleteJobAndSemaphoreFromJobsQueues(UUID idJob, String codeTraitement);

  }
