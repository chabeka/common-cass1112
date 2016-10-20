package fr.urssaf.image.sae.pile.travaux.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.Keyspace;
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

   /**
    * Récupère l'ensemble des jobs présents dans la pile des travaux 
    * (non démarrés, démarrés, terminés)
    * Cette fonction retourne par défaut 200 jobs (par ordre des UUID)
    * @param keyspace Keyspace
    * @return liste des traitements
    */
   List<JobRequest> getAllJobs(Keyspace keyspace);
   
   /**
    * Récupère l'ensemble des jobs présents dans la pile des travaux 
    * (non démarrés, démarrés, terminés)
    * @param keyspace Keyspace
    * @param maxKeysToRead Nombre de job max à retourner
    * @return liste des traitements
    */
   List<JobRequest> getAllJobs(Keyspace keyspace, int maxKeysToRead);
   
   /**
    * Récupère l'ensemble des jobs présents dans la pile des travaux (non
    * démarrés, démarrés, terminés) dont la date de creation est inferieure ou
    * egale à la date max, afin de pouvoir etre supprimer
    * 
    * @param keyspace
    *           Keyspace
    * @param dateMax
    *           date maximum
    * @return liste des traitements
    */
   List<JobRequest> getJobsToDelete(Keyspace keyspace, Date dateMax);
   
   /**
    * Teste si le job peut être réinitialisé
    * (ie : si le job est à l'état RESERVED ou STARTING) 
    * @param job le job à tester
    * @return true si le job peut être réinitialisé
    */
   boolean isJobResettable(JobRequest job);
   
   /**
    * Teste si le job peut être supprimé
    * (ie : si le job est à l'état CREATED, STARTING ou RESERVED)
    * @param job le job à tester
    * @return true si le job peut être supprimé
    */
   boolean isJobRemovable(JobRequest job);
}
