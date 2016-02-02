/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractAfterStepLogListener;

/**
 * Ecouteur pour l'écriture du fichier resultats.xml quand le traitement est en
 * échec
 * 
 */
@Component
public class ResultatsFileFailureListener extends AbstractAfterStepLogListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileFailureErrorListener.class);

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getLogMessage() {
      return "Erreur lors de l'étape d'écriture du fichier resultats.xml";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

}
