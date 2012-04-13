/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;

/**
 * Ecouteur pour la partie persistance des documents du fichier sommaire.xml
 * 
 */
@Component
public class StockageListener {

   private StepExecution stepExecution;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StockageListener.class);

   @Autowired
   private InsertionPoolThreadExecutor executor;

   /**
    * réalisé avant le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      this.stepExecution.getExecutionContext().put(Constantes.CTRL_INDEX, -1);
   }

   /**
    * réalisé sur erreur de lecture
    * 
    * @param exception
    *           exception levée par la lecture
    */
   @OnReadError
   @SuppressWarnings("unchecked")
   public final void logReadError(final Exception exception) {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.CODE_EXCEPTION);
      ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_EXCEPTION);
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.DOC_EXCEPTION);

      codes.add(Constantes.ERR_BUL001);
      index.add(stepExecution.getReadCount());
      exceptions.add(new Exception(exception.getMessage()));

      LOGGER.warn("erreur lors de la lecture du fichier", exception);
   }

   /**
    * Action executée avant le chunk pour vérifier que l'on peut continuer le
    * traitement
    */
   @BeforeChunk
   public void beforeChunk() {

      while (Boolean.TRUE.equals(executor.getIsInterrupted())) {
         try {
            LOGGER.debug("en attente de reprise de travail");
            Thread.sleep(30000);
         } catch (InterruptedException e) {
            LOGGER.info("Impossible de traiter l'interruption", e);
         }
      }

   }

   /**
    * réalisé sur erreur de transformation
    * 
    * @param documentType
    *           document en erreur
    * @param exception
    *           exception levée
    */
   @OnProcessError
   @SuppressWarnings("unchecked")
   public final void logProcessError(final Object documentType,
         final Exception exception) {

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

      LOGGER.warn("erreur lors du traitement de persistance", exception);
   }

   /**
    * @see réalisé après le chunk
    */
   @AfterChunk
   public final void afterChunk() {

      final InsertionMasseRuntimeException exception = executor
            .getInsertionMasseException();

      if (exception != null) {
         throw exception;
      }
   }

   /**
    * réalisé après le step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return un status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(final StepExecution stepExecution) {

      ExitStatus status = ExitStatus.COMPLETED;

      final JobExecution jobExecution = stepExecution.getJobExecution();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
            .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      if (CollectionUtils.isNotEmpty(exceptions)) {

         status = ExitStatus.FAILED;

      } else {

         executor.shutdown();

         executor.waitFinishInsertion();

         final ConcurrentLinkedQueue<UUID> list = getIntegratedDocuments();

         jobExecution.getExecutionContext().put(Constantes.INTEG_DOCS, list);

         jobExecution.getExecutionContext().remove(Constantes.THREAD_POOL);

         stepExecution.setWriteCount(list.size());

         final InsertionMasseRuntimeException exception = executor
               .getInsertionMasseException();

         if (exception != null) {

            status = stockageListener(exception, jobExecution);

         }
      }

      return status;
   }

   private ExitStatus stockageListener(
         InsertionMasseRuntimeException exception, JobExecution jobExecution) {

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) jobExecution
            .getExecutionContext().get(Constantes.CODE_EXCEPTION);

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) jobExecution
            .getExecutionContext().get(Constantes.INDEX_EXCEPTION);

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
            .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      ExitStatus status;

      try {

         throw exception.getCause();

      } catch (InterruptionTraitementException e) {

         String messageError = "La capture de masse en mode 'Tout ou rien' a été interrompue. Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.";

         codes.add(Constantes.ERR_BUL003);
         index.add(exception.getIndex());
         exceptions.add(new Exception(messageError));

         status = new ExitStatus("FAILED_NO_ROLLBACK");

      } catch (Exception e) {

         String message;
         if (exception.getCause() != null) {
            message = exception.getCause().getMessage();
         } else {
            message = exception.getMessage();
         }

         codes.add(Constantes.ERR_BUL001);
         index.add(exception.getIndex());
         exceptions.add(new Exception(message));

         status = ExitStatus.FAILED;

      }

      return status;

   }

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(
         final JAXBElement<UntypedDocument> untypedType) {

      ExecutionContext context = stepExecution.getExecutionContext();

      int valeur = context.getInt(Constantes.CTRL_INDEX);
      valeur++;

      context.put(Constantes.CTRL_INDEX, valeur);

   }

   private ConcurrentLinkedQueue<UUID> getIntegratedDocuments() {

      final ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> list = executor
            .getIntegratedDocuments();

      final ConcurrentLinkedQueue<UUID> listUuid = new ConcurrentLinkedQueue<UUID>();
      if (CollectionUtils.isNotEmpty(list)) {
         for (CaptureMasseIntegratedDocument document : list) {
            listUuid.add(document.getIdentifiant());
         }
      }

      return listUuid;
   }

}
