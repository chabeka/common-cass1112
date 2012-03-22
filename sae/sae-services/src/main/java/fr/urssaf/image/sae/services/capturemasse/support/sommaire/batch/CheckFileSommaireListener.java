/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;

/**
 * Listener de la tasklet de vérification du fichier sommaire.xml
 * 
 */
@Component
public class CheckFileSommaireListener {

   /**
    * réalisé après le step
    * 
    * @param stepExecution
    *           le step Execution
    * @return un status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(final StepExecution stepExecution) {

      ExitStatus exitStatus = ExitStatus.COMPLETED;

      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      final CaptureMasseSommaireDocumentException exception = (CaptureMasseSommaireDocumentException) context
            .get(Constantes.DOC_EXCEPTION);

      if (exception != null) {

         exitStatus = ExitStatus.FAILED;
      }

      return exitStatus;
   }
}
