/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Ecouteur pour la partie rollback
 * 
 */
@Component
public class RollbackListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackListener.class);

   private static final String TRC_ROLLBACK = "rollbacktasklet()";

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   /**
    * Ouverture de la connexion à DFCE au début du rollback
    * 
    * @param stepExecution
    *           étape de rollback
    * @throws ConnectionServiceEx
    *            exception levée si la connexion échoue
    */
   @BeforeStep
   public final void beforeRollback(StepExecution stepExecution)
         throws ConnectionServiceEx {

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<UUID> integDocs = (ConcurrentLinkedQueue<UUID>) stepExecution
            .getJobExecution().getExecutionContext().get(Constantes.INTEG_DOCS);

      try {
         serviceProvider.openConnexion();

      } catch (ConnectionServiceEx e) {

         if (CollectionUtils.isNotEmpty(integDocs)) {
            String idTraitement = (String) stepExecution.getJobParameters()
                  .getString(Constantes.ID_TRAITEMENT);

            String errorMessage = MessageFormat.format(
                  "{0} - Une exception a été levée lors du rollback : {1}",
                  TRC_ROLLBACK, idTraitement);

            LOGGER.error(errorMessage, e);

            LOGGER
                  .error(

                        "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                        idTraitement);
         }

         throw e;
      }

      LOGGER.debug("{} - ouverture de la connexion DFCE", TRC_ROLLBACK);

      // insertion du nombre d'éléments à supprimer dans une variable de step
      int countRollback = 0;

      if (CollectionUtils.isNotEmpty(integDocs)) {
         countRollback = integDocs.size();
      }

      stepExecution.getExecutionContext().putInt(Constantes.COUNT_ROLLBACK,
            countRollback);
   }

   /**
    * Fermeture de la connexion à DFCE à la fin du rollback
    * 
    * @param stepExecution
    *           étape de rollback
    */
   @AfterStep
   public final void afterRollback(StepExecution stepExecution) {

      serviceProvider.closeConnexion();
      LOGGER.debug("{} - fermeture de la connexion DFCE", TRC_ROLLBACK);

   }

}
