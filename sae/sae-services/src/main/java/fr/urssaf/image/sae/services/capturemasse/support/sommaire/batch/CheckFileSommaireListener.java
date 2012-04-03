/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

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
      
      @SuppressWarnings("unchecked")
      List<Exception> exceptions = (List<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      if (CollectionUtils.isNotEmpty(exceptions)) {

         exitStatus = ExitStatus.FAILED;
      }

      return exitStatus;
   }
}
