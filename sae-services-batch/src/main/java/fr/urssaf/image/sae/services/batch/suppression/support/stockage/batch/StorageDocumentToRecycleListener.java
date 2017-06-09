package fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.exception.SuppressionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.suppression.support.stockage.multithreading.SuppressionPoolThreadExecutor;

/**
 * Listener permettant de vérifier le step de mise à la corbeille.
 * 
 */
@Component
public class StorageDocumentToRecycleListener {
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentToRecycleListener.class);

   private StepExecution stepExecution;
   
   @Autowired
   private SuppressionPoolThreadExecutor executor;
   
   /**
    * Méthode déclenché au début du step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void beforeStep(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
   }
   
   /**
    * Méthode déclenchée lorsqu'il y a une erreur dans le reader
    * @param ex exception
    */
   @OnReadError
   public void onReadError(Exception ex) {
      getExceptionErreurListe().add(ex);
   }
   
   /**
    * Méthode déclenchée à la fin du step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      
      ExitStatus exitStatus = stepExecution.getExitStatus();
      if (!ExitStatus.FAILED.equals(exitStatus)) {
         
         ConcurrentLinkedQueue<Exception> exceptions = getExceptionErreurListe();

         if (CollectionUtils.isNotEmpty(exceptions)) {
            exitStatus = ExitStatus.FAILED;

         } else {

            exitStatus = ExitStatus.COMPLETED;

            executor.shutdown();

            executor.waitFinishSuppression();
            
            final SuppressionMasseRuntimeException exception = executor
                  .getSuppressionMasseException();

            if (exception != null) {

               exitStatus = stockageToRecycleListener(exception);

            }
         }
      } 
      
      // ajout dans le contexte du nombre de docs supprimés 
      setNombreDocsSupprimes(executor.getNombreSupprimes());

      return exitStatus;
   }
   
   /**
    * @return la liste des exceptions des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Exception> getExceptionErreurListe() {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.SUPPRESSION_EXCEPTION);
   }
   
   /**
    * @param nombre de documents supprimés.
    */
   protected final void setNombreDocsSupprimes(int nbDocsSupprimes) {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      jobExecution
            .putInt(Constantes.NB_DOCS_SUPPRIMES, nbDocsSupprimes);
   }
   
   /**
    * Methode permettant de gerer le statut d'execution en fonction de l'exception recue.
    * @param exception exception
    * @return ExitStatus
    */
   private ExitStatus stockageToRecycleListener(
         SuppressionMasseRuntimeException exception) {

      String trcPrefix = "stockageToRecycleListener()";

      ConcurrentLinkedQueue<Exception> exceptions = getExceptionErreurListe();

      ExitStatus status;

      try {

         throw exception.getCause();

      } catch (Exception e) {

         LOGGER.warn("{} - " + e.getMessage(), trcPrefix, e);

         String message;
         if (exception.getCause() == null) {
            message = exception.getMessage();
         } else {
            message = exception.getCause().getMessage();
         }

         exceptions.add(new Exception(message, e));

         status = ExitStatus.FAILED;

      }

      return status;
   }
}
