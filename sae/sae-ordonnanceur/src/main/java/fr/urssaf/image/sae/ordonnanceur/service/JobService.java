package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

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
   List<SimpleJobRequest> recupJobEnCours();

   /**
    * Renvoie une liste des travaux qui n'ont pas été lancés
    * 
    * @return liste des travaux en attente d'exécution
    */
   List<SimpleJobRequest> recupJobsALancer();

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
}
