/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * classe mère des listeners de rollback
 * 
 * @param <BOT>
 *           classe backoffice
 * @param <CAPT>
 *           classe de capture
 * 
 */
public abstract class AbstractRollbackListener<BOT, CAPT> {

   private static final String CATCH = "AvoidCatchingThrowable";

   /**
    * Ouverture de la connexion à DFCE au début du rollback
    * 
    * @param stepExecution
    *           étape de rollback
    */
   @SuppressWarnings(CATCH)
   @BeforeStep
   public final void beforeRollback(StepExecution stepExecution) {

      String trcPrefix = "beforeRollback()";
      int nbDocsIntegres = getExecutor().getIntegratedDocuments().size();

      try {
         getServiceProvider().openConnexion();

         /* on catch les throwable de DFCE */
      } catch (Throwable e) {

         String idTraitement = (String) stepExecution.getJobParameters()
               .getString(Constantes.ID_TRAITEMENT);

         String errorMessage = MessageFormat.format(
               "{0} - Une exception a été levée lors du rollback : {1}",
               trcPrefix, idTraitement);

         getLogger().warn(errorMessage, e);

         if (nbDocsIntegres > 0) {

            getLogger()
                  .error(

                        "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                        idTraitement);
            stepExecution.getJobExecution().getExecutionContext().put(
                  Constantes.FLAG_BUL003, Boolean.TRUE);
         }

         throw new CaptureMasseRuntimeException(e);
      }

      getLogger().debug("{} - ouverture de la connexion DFCE", trcPrefix);

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

      String trcPrefix = "afterRollback()";

      try {
         // On stocke le nombre de document intégrés
         stepExecution.getJobExecution().getExecutionContext().put(
               Constantes.NB_INTEG_DOCS,
               getExecutor().getIntegratedDocuments().size());

         getServiceProvider().closeConnexion();

         /* on catch car DFCE renvoie des throwables */
      } catch (Throwable e) {

         getLogger().warn("{} - Fermeture de la base impossible", trcPrefix, e);
         throw new CaptureMasseRuntimeException(e);
      }

      getLogger().debug("{} - fermeture de la connexion DFCE", trcPrefix);

   }

   /**
    * @return l'executorO
    */
   protected abstract AbstractPoolThreadExecutor<BOT, CAPT> getExecutor();

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

   /**
    * @return le serviceProvider
    */
   protected abstract StorageServiceProvider getServiceProvider();

}
