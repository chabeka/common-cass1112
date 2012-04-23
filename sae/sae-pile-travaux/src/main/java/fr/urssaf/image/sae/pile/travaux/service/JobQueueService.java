package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Service de la pile des travaux
 * 
 * 
 */
public interface JobQueueService {

   /**
    * Ajoute un traitement dans la pile des travaux
    * 
    * @param jobRequest
    *           ensemble des données nécessaires à la création du job
    */
   void addJob(JobRequest jobRequest);

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
    */
   @SuppressWarnings("PMD.LongVariable")
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
    */
   void endingJob(UUID idJob, boolean succes, Date dateFinTraitement);

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
    */
   void endingJob(UUID idJob, boolean succes, Date dateFinTraitement,
         String message);

}
