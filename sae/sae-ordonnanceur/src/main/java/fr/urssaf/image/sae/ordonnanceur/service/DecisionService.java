package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.List;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Service de décision pour sélectionner les traitements en masse à exécuter
 * 
 * 
 */
public interface DecisionService {

   /**
    * Selectionne la liste des jobs à lancer à partir de la liste fournie.
    * 
    * @param jobsEnAttente
    *           liste des travaux en attente, en clé le nom du job et en valeur
    *           la liste des travaux associés
    * @param jobsEnCours
    *           liste des travaux en cours
    * @return liste de job à lancer
    * @throws AucunJobALancerException
    *            Exception levée si aucun job n'est à lancer
    */
   List<JobQueue> trouverListeJobALancer(List<JobQueue> jobsEnAttente,
         List<JobRequest> jobsEnCours) throws AucunJobALancerException;

   /**
    * Controle la disponibilité de l'ECDE pour les opérations de traitement de
    * masse devant utilisé l'ECDE.
    * 
    * @param jobAlancer
    *           Job qui doit être lancé
    * @throws AucunJobALancerException
    *            @{@link AucunJobALancerException}
    */
   void controleDispoEcdeTraitementMasse(JobQueue jobAlancer)
         throws AucunJobALancerException;
         
}
