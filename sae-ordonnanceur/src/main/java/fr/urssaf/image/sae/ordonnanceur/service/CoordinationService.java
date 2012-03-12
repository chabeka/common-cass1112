package fr.urssaf.image.sae.ordonnanceur.service;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;

/**
 * service de coordination des différents traitements en masse
 * 
 * 
 */
public interface CoordinationService {

   /**
    * Méthode réalisant les opérations suivantes :
    * <ul>
    * <li>récupération de la liste des jobs (délégué)</li>
    * <li>vérification si un job doit être lancé ou non (délégué)</li>
    * <li>appel de l'exécution du job si nécessaire</li>
    * </ul>
    * 
    * @return identifiant du traitement exécuté
    * @throws AucunJobALancerException
    *            aucun traitement n'est à lancer
    */
   long lancerTraitement() throws AucunJobALancerException;
}
