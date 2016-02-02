/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.EcdePermissionException;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Listener permettant de vérifier que les droits sur l'ECDE sont présents
 * 
 */
@Component
public class CheckEcdePermissionAfterStepListener {

   /**
    * Vérification pour la fin de step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {

      ExitStatus exitStatus = stepExecution.getExitStatus();
      List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
      if (!CollectionUtils.isEmpty(failureExceptions)
            && failureExceptions.size() == 1
            && !ecdePermissionValid(failureExceptions.get(0))) {

         EcdePermissionException exception = (EcdePermissionException) failureExceptions
               .get(0).getCause().getCause();
         addException(stepExecution, exception.getCause());

         exitStatus = new ExitStatus("FAILED_FIN_BLOQUANT");

      }

      return exitStatus;
   }

   private void addException(StepExecution stepExecution,
         Throwable paramException) {
      final Exception exception = new Exception(paramException.getMessage());

      getExceptions(stepExecution).add(exception);

   }

   private boolean ecdePermissionValid(Throwable exception) {
      boolean valid = true;

      if (exception instanceof IllegalArgumentException
            && exception.getCause() instanceof InvocationTargetException
            && exception.getCause().getCause() instanceof EcdePermissionException) {
         valid = false;
      }

      return valid;
   }

   @SuppressWarnings("unchecked")
   private ConcurrentLinkedQueue<Exception> getExceptions(
         StepExecution stepExecution) {
      return (ConcurrentLinkedQueue<Exception>) stepExecution.getJobExecution()
            .getExecutionContext().get(Constantes.DOC_EXCEPTION);
   }
}
