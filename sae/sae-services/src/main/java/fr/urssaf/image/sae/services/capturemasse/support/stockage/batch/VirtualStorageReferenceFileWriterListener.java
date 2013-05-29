/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Composant permettant de capter les erreurs d'écriture des fichiers de
 * référence
 * 
 */
@Component
public class VirtualStorageReferenceFileWriterListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualStorageReferenceFileWriterListener.class);
   
   private static final String UNCHECKED = "unchecked";
   
   private static final String CATCH = "AvoidCatchingThrowable";

   private StepExecution stepExecution;

   @Autowired
   private StorageServiceProvider storageServiceProvider;

   /**
    * Initialisation du step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   @SuppressWarnings({UNCHECKED, CATCH})
   public final void init(StepExecution stepExecution) {

      String trcPrefix = "init()";

      try {
         storageServiceProvider.openConnexion();
      
      /* on doit catcher les throwables à cause de DFCE */
      } catch (Throwable throwable) {
         LOGGER.warn("{} - erreur de connexion à DFCE", trcPrefix, throwable);

         ExecutionContext jobExecution = stepExecution.getJobExecution()
               .getExecutionContext();
         ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) jobExecution
               .get(Constantes.CODE_EXCEPTION);
         ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) jobExecution
               .get(Constantes.INDEX_EXCEPTION);
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
               .get(Constantes.DOC_EXCEPTION);

         codes.add(Constantes.ERR_BUL001);
         index.add(0);
         exceptions.add(new Exception(throwable.getMessage()));

         stepExecution.setExitStatus(new ExitStatus("FAILED_NO_ROLLBACK"));

         throw new CaptureMasseRuntimeException(throwable);
      }

      this.stepExecution = stepExecution;
   }

   /**
    * Action executée après le step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return un status de sortie
    */
   @AfterStep
   @SuppressWarnings({UNCHECKED, CATCH})
   public final ExitStatus end(final StepExecution stepExecution) {

      String trcPrefix = "end()";
      ExitStatus exitStatus = stepExecution.getExitStatus();

      try {
         storageServiceProvider.closeConnexion();

         /* on doit catcher les throwables à cause de DFCE */
      } catch (Throwable e) {

         LOGGER.warn("{} - erreur lors de la fermeture de la base de données",
               trcPrefix, e);

         ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) stepExecution
               .getJobExecution().getExecutionContext().get(
                     Constantes.CODE_EXCEPTION);

         ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) stepExecution
               .getJobExecution().getExecutionContext().get(
                     Constantes.INDEX_EXCEPTION);

         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) stepExecution
               .getJobExecution().getExecutionContext().get(
                     Constantes.DOC_EXCEPTION);

         codes.add(Constantes.ERR_BUL001);
         index.add(0);
         exceptions.add(new Exception(e.getMessage()));

         exitStatus = ExitStatus.FAILED;
      }

      return exitStatus;

   }

   /**
    * Méthode déclenchée lors d'une erreur d'écriture
    * 
    * @param exception
    *           exception levée lors de l'écriture
    * @param references
    *           la liste des fichiers de référence en cours d'écriture
    */
   @SuppressWarnings(UNCHECKED)
   @OnWriteError
   public final void onWriteError(Exception exception,
         List<StorageReferenceFile> references) {

      String trcPrefix = "onWriteError()";
      LOGGER.debug("{} - erreur lors de l'écriture des données", trcPrefix);

      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.CODE_EXCEPTION);
      ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_EXCEPTION);
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.DOC_EXCEPTION);

      codes.add(Constantes.ERR_BUL002);
      index.add(stepExecution.getExecutionContext().getInt(
            Constantes.CTRL_INDEX));
      exceptions.add(new Exception(exception.getMessage()));
   }
}
