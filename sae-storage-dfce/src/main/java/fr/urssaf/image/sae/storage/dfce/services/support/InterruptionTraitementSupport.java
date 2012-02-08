package fr.urssaf.image.sae.storage.dfce.services.support;

import org.joda.time.DateTime;

import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

/**
 * Service pour permettre d'interrompre un traitement
 * 
 * 
 */
public interface InterruptionTraitementSupport {

   // TODO voir comment ne pas passer indicateur JMX dans les arguments
   /**
    * L'interruption du service DFCE est parfois nécessaire pour des questions
    * de maintenance, et programmée à des moments précis pour être redémarré
    * ensuite. Cette méthode permet de mettre en pause le
    * {@link java.lang.Thread} courant pendant cette période puis de se
    * reconnecter au service DFCE
    * 
    * @param currentDate
    *           date courante
    * @param interruptionConfig
    *           configuration de l'interruption
    * @param indicator
    *           indicateur JMX pour le traitement
    * @throws InterruptionTraitementException
    *            une exception a été levée lors de la tentative de reconnexion à
    *            DFCE
    */
   void interruption(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig, JmxIndicator indicator)
         throws InterruptionTraitementException;

   /**
    * Vérifie si une date courante est dans une période d'interruption
    * 
    * @param currentDate
    *           date courante
    * @param interruptionConfig
    *           configuration de l'interruption
    * @return <code>true</code> si <code>currentDate</code> est situé dans la
    *         période d'interruption, <code>false</code> sinon
    */
   boolean hasInterrupted(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig);

}
