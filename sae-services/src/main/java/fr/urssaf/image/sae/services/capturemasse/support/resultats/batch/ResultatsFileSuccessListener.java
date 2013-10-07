/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

/**
 * Ecouteur pour l'écriture du fichier resultats.xml quand le traitement est en
 * succes
 * 
 */
@Component
public class ResultatsFileSuccessListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileSuccessListener.class);

   /**
    * méthode executée parès le step
    * 
    * @param stepExecution
    *           le step execution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus end(StepExecution stepExecution) {
      if (CollectionUtils.isNotEmpty(stepExecution.getFailureExceptions())) {

         for (Throwable throwable : stepExecution.getFailureExceptions()) {
            LOGGER
                  .warn(
                        "Erreur lors de l'étape d'écriture du fichier resultats.xml",
                        throwable);
         }
      }

      return stepExecution.getExitStatus();
   }

}
