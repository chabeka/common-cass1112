package fr.urssaf.image.sae.services.batch.support;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

/**
 * Support pour l'exécution des traitements de masse
 * 
 * 
 */
public interface TraitementExecutionSupport {

   /**
    * Exécute un traitement de masse à partir d'un job contenu dans la pile des
    * travaux
    * 
    * @param job
    *           traitement de la pile des travaux
    * @return résultat final du traitement
    */
   ExitTraitement execute(JobRequest job);

}
