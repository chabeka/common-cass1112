package fr.urssaf.image.sae.storage.dfce.services.support;

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
    * @param interruptionConfig
    *           configuration de l'interruption
    * @param indicator
    *           indicateur JMX pour le traitement
    */
   void interruption(InterruptionTraitementConfig interruptionConfig,
         JmxIndicator indicator);

}
