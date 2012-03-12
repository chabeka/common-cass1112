package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.Collection;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;

/**
 * Service de décision pour sélectionner les traitements en masse à exécuter
 * 
 * 
 */
public interface DecisionService {

   /**
    * Choisit un des jobs à lancer à partir de la liste fournie, et renvoie son
    * identifiant
    * 
    * @param jobsEnAttente
    *           liste des travaux en attente, en clé le nom du job et en valeur
    *           la liste des travaux associés
    * @param jobsEnCours
    *           liste des travaux en cours
    * @return job à lancer
    * @throws AucunJobALancerException
    *            Exception levée si aucun job n'est à lancer
    */
   JobInstance trouverJobALancer(List<JobInstance> jobsEnAttente,
         Collection<JobExecution> jobsEnCours) throws AucunJobALancerException;
}
