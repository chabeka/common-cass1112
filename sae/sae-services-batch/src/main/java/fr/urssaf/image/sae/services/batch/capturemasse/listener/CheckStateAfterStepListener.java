/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Component;

/**
 * Listener de la tasklet de vérification du fichier sommaire.xml
 * 
 */
@Component
public class CheckStateAfterStepListener extends AbstractListener {

   /**
    * {@inheritDoc}
    * <ul>
    * <li>Vérification que le traitement s'est déroulé avec succès</li>
    * <li>Redirection vers la bonne étape</li>
    * </ul>
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      ExitStatus exitStatus = ExitStatus.FAILED;

      if (CollectionUtils.isEmpty(getErrorMessageList())
            || (isModePartielBatch() && isControleModePartielActif())) {
         exitStatus = ExitStatus.COMPLETED;
      }

      return exitStatus;
   }

   @Override
   protected void specificInitOperations() {
      // rien à faire
   }

   /**
    * Methode permettant d'activer le controle sur le mode Partiel.
    * 
    * @return True si le controle sur le mode Partiel doit être actif, false
    *         sinon.
    */
   protected boolean isControleModePartielActif() {
      return false;
   }

}
