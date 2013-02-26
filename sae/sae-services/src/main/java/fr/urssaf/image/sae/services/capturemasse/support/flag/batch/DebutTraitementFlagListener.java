/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.batch;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

/**
 * Listener pour l'écriture du fichier fin_traitement.flag
 * 
 */
@Component
public class DebutTraitementFlagListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DebutTraitementFlagListener.class);

   /**
    * Action réalisée après le step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus end(StepExecution stepExecution) {

      if (CollectionUtils.isNotEmpty(stepExecution.getFailureExceptions())) {

         for (Throwable throwable : stepExecution.getFailureExceptions()) {
            LOGGER
                  .warn(
                        "Erreur lors de l'étape d'écriture du fichier debut_traitement.flag",
                        throwable);
         }
      }

      return stepExecution.getExitStatus();

   }

}
