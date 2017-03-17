package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Service des traitements de masse contenus dans la pile des travaux
 * 
 * 
 */
public interface JobService {

   /**
    * Renvoie une liste des travaux actuellement en cours ou réservés sur le
    * serveur courant
    * 
    * @return liste des travaux en cours d'exécution
    */
   List<JobRequest> recupJobEnCours();

   /**
    * Renvoie une liste des travaux qui n'ont pas été lancés
    * 
    * @return liste des travaux en attente d'exécution
    */
   List<JobQueue> recupJobsALancer();

   /**
    * Met à jour le nom de la machine dans la table des jobs à lancer afin de
    * signifier la prise en compte du traitement.
    * 
    * @param idJob
    *           identifiant du travail à mettre à jour
    * @throws JobInexistantException
    *            Exception levée si le job correspondant à l'idJob passé en
    *            paramètre n'existe pas
    * @throws JobDejaReserveException
    *            Exception levée en cas d'exception technique d'accès à la base
    *            de données
    */
   void reserveJob(UUID idJob) throws JobDejaReserveException,
         JobInexistantException;

   /**
    * Met à jour le job en mettant à jour le flag signifiant une erreur et la
    * description du problème.
    * 
    * @param idJob
    *           identifiant du job
    * @param flag
    *           valeur du flag (true/false)
    * @param description
    *           description du problème rencontré
    * @throws JobInexistantException
    *            le job à mettre à jour n'existe pas
    */
   void updateToCheckFlag(UUID idJob, Boolean flag, String description)
         throws JobInexistantException;
      
   /**
    * Renvoie true si des travaux avec le même code traitement 
    * sont actuellement en cours ou en failure tous serveurs confondus, false sinon.
    * 
    * @return True si les travaux avec le même code traitement sont actuellement 
    * en cours ou en failure tous serveurs confondus, false sinon.
    */
   boolean isJobCodeTraitementEnCoursOuFailure(JobQueue jobQueue);

   /**
    * Confirme que le job trouvé est bien lancable et on créer le sémaphore pour
    * ce job.
    * 
    * @param jobQueue
    *           {@link JobQueue}
    * @return Le job à lancer.
    */
   JobQueue confirmerJobALancer(JobQueue jobQueue);
}
