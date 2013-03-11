/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service;

import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Service permettant de réaliser les traitements
 * 
 */
public interface TraitementService {

   /**
    * Réalise la purge d'une table de traces
    * 
    * @param purgeType
    *           purge à lancer
    */
   void purger(PurgeType purgeType);

   /**
    * Réalise la journalisation des traces
    * 
    * @param typeJournalisation
    *           type de journalisation à lancer
    * @throws TraceExecutableException
    *            en cas de problème lors de la journalisation
    */
   void journaliser(JournalisationType typeJournalisation)
         throws TraceExecutableException;

}
