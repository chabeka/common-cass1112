/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
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

   private static final String TRC_AFTER = "afterRollback()";

   private static final String TRC_BEFORE = "beforeRollback()";

   private static final String CATCH = "AvoidCatchingThrowable";

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   @Autowired
   private InsertionPoolThreadExecutor executor;

   /**
    * Ouverture de la connexion à DFCE au début du rollback
    * 
    * @param stepExecution
    *           étape de rollback
    */
   @BeforeStep
   @SuppressWarnings(CATCH)
   public final void beforeRollback(StepExecution stepExecution) {

      int nbDocsIntegres = executor.getIntegratedDocuments().size();

      try {
         serviceProvider.openConnexion();

         /* on catch car DFCE renvoie des throwables */
      } catch (Throwable e) {

         String idTraitement = (String) stepExecution.getJobParameters()
               .getString(Constantes.ID_TRAITEMENT);

         String errorMessage = MessageFormat.format(
               "{0} - Une exception a été levée lors du rollback : {1}",
               TRC_BEFORE, idTraitement);

         LOGGER.warn(errorMessage, e);

         if (nbDocsIntegres > 0) {

            LOGGER
                  .error(

                        "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                        idTraitement);
            stepExecution.getJobExecution().getExecutionContext().put(
                  Constantes.FLAG_BUL003, Boolean.TRUE);
         }

         throw new CaptureMasseRuntimeException(e);
      }

      LOGGER.debug("{} - ouverture de la connexion DFCE", TRC_ROLLBACK);

      // insertion du nombre d'éléments à supprimer dans une variable de step
      int countRollback = 0;

      if (nbDocsIntegres > 0) {
         countRollback = nbDocsIntegres;
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
   @SuppressWarnings(CATCH)
   public final void afterRollback(StepExecution stepExecution) {

      // pour l'instant nous avons fait le choix de propager l'erreur
      // pour ne pas la cacher et attérir dans un état en erreur

      try {
         // On stocke le nombre de document intégrés
         stepExecution.getJobExecution().getExecutionContext().put(
               Constantes.NB_INTEG_DOCS,
               executor.getIntegratedDocuments().size());

         serviceProvider.closeConnexion();

         /* on catch car DFCE renvoie des throwables */
      } catch (Throwable e) {

         LOGGER.warn("{} - Fermeture de la base impossible", TRC_AFTER, e);
         throw new CaptureMasseRuntimeException(e);
      }

      LOGGER.debug("{} - fermeture de la connexion DFCE", TRC_ROLLBACK);

   }

}
