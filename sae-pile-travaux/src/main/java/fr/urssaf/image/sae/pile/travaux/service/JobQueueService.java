package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;

/**
 * Service de la pile des travaux
 * 
 * 
 */
public interface JobQueueService {

   /**
    * Ajoute un traitement dans la pile des travaux
    * 
    * @param idJob
    *           identifiant du traitement
    * @param type
    *           type de traitement
    * @param parametres
    *           paramètres du traitement
    * @param dateDemande
    *           date d'ajout dans la pile des travaux
    */
   void addJob(UUID idJob, String type, String parametres, Date dateDemande);

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
    */
   void reserveJob(UUID idJob, String hostname, Date dateReservation)
         throws JobDejaReserveException, JobInexistantException;

   /**
    * Met à jour un traitement avant de l'exécuter.<br>
    * 
    * @param idJob
    *           identifiant du traitement
    * @param dateDebutTraitement
    *           date d'exécution du traitement
    */
   void startingJob(UUID idJob, Date dateDebutTraitement);

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

}
