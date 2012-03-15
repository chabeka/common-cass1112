package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

/**
 * DAO de la pile des travaux
 * 
 * 
 */
public interface JobQueueDao {

   /**
    * Persiste un traitement dans la pile des travaux
    * 
    * @param jobRequest
    *           traitement à persister
    */
   void saveJobRequest(JobRequest jobRequest);

   /**
    * Récupère un traitement dans la pile des travaux
    * 
    * @param jobRequestUUID
    *           identifiant du traitement persisté
    * @return le traitement trouvé
    */
   JobRequest getJobRequest(UUID jobRequestUUID);

   /**
    * Modifie un traitement dans la pile des travaux
    * 
    * @param jobRequest
    *           traitement persisté à modifier
    */
   void updateJobRequest(JobRequest jobRequest);

   /***
    * récupère la liste des traitements non réservés contenus dans la pile
    * 
    * @return liste des traitements non réservés
    */
   Iterator<SimpleJobRequest> getUnreservedJobRequestIterator();

   /**
    * Récupère la liste des traitements réservés ou en cours d'exécution 
    * sur un serveur donné
    * 
    * @param hostname
    *           nom du serveur concerné
    * @return liste des traitements réservés ou en cours d'exécution
    */
   List<JobRequest> getNonTerminatedJobs(String hostname);

   /**
    * Réserve un job. Attention, aucun lock n'est fait. Le lock
    * doit être fait en amont.
    * @param jobRequest       Le jobRequest à réserver
    * @param hostname         Le hostname du serveur qui fait la réservation
    * @param reservationDate  La date de réservation (normalement : maintenant)
    */
   void reserveJobRequest(JobRequest jobRequest, String hostname, Date reservationDate);
   
   /**
    * Supprime un jobRequest
    * 
    * @param jobRequest
    *           jobRequest à supprimer
    */
   void deleteJobRequest(JobRequest jobRequest);
   
}
