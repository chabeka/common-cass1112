package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
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
    * @throws JobInexistantException
    *            le traitement n'existe pas
    */
   void endingJob(UUID idJob, boolean succes, Date dateFinTraitement,
         String message) throws JobInexistantException;

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

}
