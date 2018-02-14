/**
 * 
 */
package fr.urssaf.image.sae.services.batch.reprise.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Listener pour l'écriture du fichier fin_traitement.flag
 * 
 */
@Component
public class RepriseCaptureMasseInitListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RepriseCaptureMasseInitListener.class);

   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {
      ExitStatus status = stepExecution.getExitStatus();

      if (CollectionUtils.isNotEmpty(stepExecution.getFailureExceptions())) {
         for (Throwable throwable : stepExecution.getFailureExceptions()) {
            getExceptionErreurListe(stepExecution).add((Exception) throwable);
            LOGGER.warn(getLogMessage(), throwable);
         }

         status = ExitStatus.FAILED;
      }

      return status;

   }

   /**
    * Retourne la liste des exceptions contenus dans le context de Spring batch.
    * 
    * @param chunkContext
    *           le contexte du chunk
    * @return la liste des exceptions des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Exception> getExceptionErreurListe(
         StepExecution stepExecution) {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.DOC_EXCEPTION);
   }

   /**
    * Getter message
    * 
    * @return le message d'erreur
    */
   protected final String getLogMessage() {
      return "Erreur lors de l'étape d'initialisation de la reprise de l'archivage de masse";
   }

}
