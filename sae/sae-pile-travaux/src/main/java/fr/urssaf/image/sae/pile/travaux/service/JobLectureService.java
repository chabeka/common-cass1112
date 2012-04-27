package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Service de lecture de la pile des travaux
 * 
 * 
 */
public interface JobLectureService {

   /**
    * Récupère un traitement dans la pile des travaux
    * 
    * @param jobRequestUUID
    *           identifiant du traitement persisté
    * @return le traitement trouvé
    */
   JobRequest getJobRequest(UUID jobRequestUUID);

   /***
    * récupère la liste des traitements non réservés contenus dans la pile
    * 
    * @return liste des traitements non réservés
    */
   Iterator<JobQueue> getUnreservedJobRequestIterator();

   /**
    * Récupère la liste des traitements réservés ou en cours d'exécution sur un
    * serveur donné.
    * 
    * @param hostname
    *           nom du serveur concerné
    * @return liste des traitements réservés ou en cours d'exécution
    */
   List<JobQueue> getNonTerminatedSimpleJobs(String hostname);

   /**
    * Récupère la liste des traitements réservés ou en cours d'exécution sur un
    * serveur donné.
    * 
    * @param hostname
    *           nom du serveur concerné
    * @return liste des traitements réservés ou en cours d'exécution
    */
   List<JobRequest> getNonTerminatedJobs(String hostname);

   /**
    * Récupère l'historique d'un traitement
    * 
    * @param idJob
    *           identifiant du traitement
    * @return liste trié de l'historique du traitement
    */
   List<JobHistory> getJobHistory(UUID idJob);
}
