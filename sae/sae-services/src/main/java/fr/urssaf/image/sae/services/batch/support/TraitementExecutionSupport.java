package fr.urssaf.image.sae.services.batch.support;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Support pour l'exécution des traitements de masse
 * 
 * 
 */
public interface TraitementExecutionSupport {

   /**
    * Exécute une traitement de masse à partir d'un job contenu dans la pile des
    * travaux
    * 
    * @param job
    *           traitement de la pile des travaux
    * @return <code>true</code> si le traitement a réussi, <code>false</code>
    *         sinon
    */
   boolean execute(JobRequest job);

}
