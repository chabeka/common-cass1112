/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * Listener de la tasklet de vérification du fichier sommaire.xml
 * 
 */
@Component
public class CountSommaireDocumentsListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CountSommaireDocumentsListener.class);

   /**
    * réalisé après le step
    * 
    * @param stepExecution
    *           le step Execution
    * @return un status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(final StepExecution stepExecution) {

      ExitStatus exitStatus;

      if (CollectionUtils.isNotEmpty(stepExecution.getFailureExceptions())) {

         for (Throwable throwable : stepExecution.getFailureExceptions()) {
            LOGGER
                  .warn(
                        "Erreur lors de l'étape de comptage des éléments du fichier sommaire.xml",
                        throwable);
         }

         exitStatus = ExitStatus.FAILED;

      } else {
         String redirect = stepExecution.getExecutionContext().getString(
               Constantes.COUNT_DIRECTION);

         exitStatus = new ExitStatus(redirect);
      }

      return exitStatus;
   }
}
