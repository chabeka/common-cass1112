/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Classe mère des listeners réalisant un log des erreurs rencontrées pendant le
 * traitement dans l'étape @AfterStep
 * 
 */
public abstract class AbstractAfterStepLogListener {

   /**
    * Action réalisée après le step
    * @param stepExecution
    *           le stepExecution
    * @return le status de sortie
    */
   @SuppressWarnings("unchecked")
   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {
      ExitStatus status = stepExecution.getExitStatus();

      if (CollectionUtils.isNotEmpty(stepExecution.getFailureExceptions())) {
         for (Throwable throwable : stepExecution.getFailureExceptions()) {
            getLogger().warn(getLogMessage(), throwable);
         }
      }

      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();

      ConcurrentLinkedQueue<String> messageExceptionList = (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.DOC_EXCEPTION);

      if (CollectionUtils.isEmpty(messageExceptionList)) {
         status = ExitStatus.COMPLETED;
      } else {
         status = ExitStatus.FAILED;
      }
      return status;
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
