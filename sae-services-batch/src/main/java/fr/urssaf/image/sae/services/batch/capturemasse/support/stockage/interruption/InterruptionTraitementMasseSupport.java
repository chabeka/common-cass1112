/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption;

import org.joda.time.DateTime;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;


/**
 * Composant pour l'interruption programmée des traitements de capture de masse
 * 
 */
public interface InterruptionTraitementMasseSupport {

   /**
    * L'interruption du service DFCE est parfois nécessaire pour des questions
    * de maintenance, et programmée à des moments précis pour être redémarré
    * ensuite. Cette méthode permet de mettre en pause le
    * {@link java.lang.Thread} courant pendant cette période puis de se
    * reconnecter au service DFCE
    * 
    * @param currentDate
    *           date courante
    * @param conf
    *           configuration de l'interruption
    * @throws InterruptionTraitementException
    *            une exception a été levée lors de la tentative de reconnexion à
    *            DFCE
    */
   void interruption(DateTime currentDate,
         InterruptionTraitementConfig conf)
         throws InterruptionTraitementException;

   /**
    * Vérifie si une date courante est dans une période d'interruption
    * 
    * @param currentDate
    *           date courante
    * @param conf
    *           configuration de l'interruption
    * @return <code>true</code> si <code>currentDate</code> est situé dans la
    *         période d'interruption, <code>false</code> sinon
    */
   boolean hasInterrupted(DateTime currentDate,
         InterruptionTraitementConfig conf);
}
