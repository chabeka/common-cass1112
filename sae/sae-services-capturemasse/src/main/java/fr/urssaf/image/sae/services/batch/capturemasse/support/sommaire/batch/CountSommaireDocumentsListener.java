/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Listener de la tasklet de vérification du fichier sommaire.xml
 * 
 */
@Component
public class CountSommaireDocumentsListener extends AbstractListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CountSommaireDocumentsListener.class);

   /**
    * {@inheritDoc}
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      ExitStatus exitStatus;

      if (CollectionUtils.isNotEmpty(getStepExecution().getFailureExceptions())) {

         for (Throwable throwable : getStepExecution().getFailureExceptions()) {
            LOGGER
                  .warn(
                        "Erreur lors de l'étape de comptage des éléments du fichier sommaire.xml",
                        throwable);
         }

         exitStatus = ExitStatus.FAILED;

      } else {
         String redirect = getStepExecution().getExecutionContext().getString(
               Constantes.COUNT_DIRECTION);

         exitStatus = new ExitStatus(redirect);
      }

      return exitStatus;
   }

   @Override
   protected void specificInitOperations() {
      // rien à faire

   }
}
