/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * Listener de la vérification de format du fichier sommaire.xml
 * 
 */
@Component
public class CheckFormatFileSommaireListener {

   /**
    * réalisé après le step
    * 
    * @param execution
    *           le stepExecution
    * @return un status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(final StepExecution execution) {

      ExitStatus exitStatus = ExitStatus.COMPLETED;

      final ExecutionContext context = execution.getJobExecution()
            .getExecutionContext();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      if (CollectionUtils.isNotEmpty(exceptions)) {
         exitStatus = ExitStatus.FAILED;
      }

      return exitStatus;

   }

}
