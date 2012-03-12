package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

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
    */
   void getJobRequest(UUID jobRequestUUID);

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
   Iterator<JobRequest> getUnreservedJobRequestIterator();

   /**
    * Récupère la liste des traitements en cours d'exécution contenus dans la
    * pile par serveur
    * 
    * @param hostname
    *           nom du serveur
    * @return liste des traitements en cours d'exécution
    */
   List<JobRequest> getNonTerminatedJobs(String hostname);
}
