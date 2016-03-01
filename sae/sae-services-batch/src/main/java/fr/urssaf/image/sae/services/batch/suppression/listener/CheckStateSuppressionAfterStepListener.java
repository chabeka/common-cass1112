/**
 * 
 */
package fr.urssaf.image.sae.services.batch.suppression.listener;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Component;

/**
 * Listener d'etat d'excetion de tasklet de suppression.
 * 
 */
@Component
public class CheckStateSuppressionAfterStepListener extends AbstractSuppressionListener {

   /**
    * {@inheritDoc}
    * <ul>
    * <li>Vérification que le traitement s'est déroulé avec succès</li>
    * <li>Redirection vers la bonne étape</li>
    * </ul>
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      ExitStatus exitStatus = getStepExecution().getExitStatus();

      // recupere le statut du step, si celui ci est complete, on va quand même regarder la liste des d'exception
      if (!ExitStatus.FAILED.equals(exitStatus)) {
         
         if (CollectionUtils.isNotEmpty(getExceptionErreurListe())) {
            exitStatus = ExitStatus.FAILED;
         }
      } 

      return exitStatus;
   }

   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
