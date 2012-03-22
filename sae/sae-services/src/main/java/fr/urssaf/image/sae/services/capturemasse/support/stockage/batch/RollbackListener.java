/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;

/**
 * Ecouteur pour la partie rollback
 * 
 */
@Component
public class RollbackListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackListener.class);

   private static final String TRC_ROLLBACK = "rollbacktasklet()";

   /**
    * 
    * réalisé après le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @AfterStep
   public final void logProcessError(final StepExecution stepExecution) {

      final ExecutionContext mapJob = stepExecution.getJobExecution()
            .getExecutionContext();

      final String idTraitement = (String) mapJob.get(Constantes.ID_TRAITEMENT);

      final String errorMessage = MessageFormat.format(
            "{0} - Une exception a été levée lors du rollback : {1}",
            TRC_ROLLBACK, idTraitement);

      final CaptureMasseSommaireDocumentException exception = (CaptureMasseSommaireDocumentException) mapJob
            .get(Constantes.DOC_EXCEPTION);

      LOGGER.error(errorMessage, exception.getCause());

      LOGGER
            .error(

                  "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                  idTraitement);
   }

}
