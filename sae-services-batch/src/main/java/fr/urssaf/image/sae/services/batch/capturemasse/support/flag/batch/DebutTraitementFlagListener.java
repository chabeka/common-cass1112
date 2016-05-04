/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.flag.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractAfterStepLogListener;

/**
 * Listener pour l'écriture du fichier fin_traitement.flag
 * 
 */
@Component
public class DebutTraitementFlagListener extends AbstractAfterStepLogListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DebutTraitementFlagListener.class);

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getLogMessage() {
      return "Erreur lors de l'étape d'écriture du fichier debut_traitement.flag";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

}
