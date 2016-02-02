/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

/**
 * Classe mère des listeners réalisant un log des erreurs rencontrées pendant le
 * traitement dans l'étape @AfterStep
 * 
 */
public abstract class AbstractAfterStepLogListener {

   /**
    * Action réalisée après le step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {

      if (CollectionUtils.isNotEmpty(stepExecution.getFailureExceptions())) {

         for (Throwable throwable : stepExecution.getFailureExceptions()) {
            getLogger().warn(getLogMessage(), throwable);
         }
      }

      return stepExecution.getExitStatus();

   }

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

   /**
    * @return le message indiqué dans les logs
    */
   protected abstract String getLogMessage();

}
