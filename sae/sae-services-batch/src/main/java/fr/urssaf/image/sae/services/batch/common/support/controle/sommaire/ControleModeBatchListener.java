/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.support.controle.sommaire;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;

/**
 * Listener de la tasklet de contrôle du mode de traitement du batch
 * 
 */
@Component
public class ControleModeBatchListener extends AbstractListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleModeBatchListener.class);

   /**
    * {@inheritDoc}
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      ExitStatus exitStatus;

      if (CollectionUtils.isNotEmpty(getStepExecution().getFailureExceptions())
            && !isModePartielBatch()) {

         for (Throwable throwable : getStepExecution().getFailureExceptions()) {
            LOGGER
            .warn(
                  "Erreur lors de l'étape de controle du mode de traitement du batch sommaire.xml",
                  throwable);
         }

         exitStatus = ExitStatus.FAILED;

      } else {
         exitStatus = new ExitStatus(this.getBatchMode());
      }

      return exitStatus;
   }

   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
