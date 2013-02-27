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
import fr.urssaf.image.sae.services.capturemasse.support.stockage.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;

/**
 * Ecouteur pour la partie persistance des documents du fichier sommaire.xml
 * 
 */
@Component
public class StockageListener {

   private static final String UNCHECKED = "unchecked";
   private static final int THREAD_SLEEP = 30000;

   private StepExecution stepExecution;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StockageListener.class);

   private static final String FAILED_NO_RB = "FAILED_NO_ROLLBACK";

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
   @SuppressWarnings(UNCHECKED)
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
   public final void beforeChunk() {

      while (Boolean.TRUE.equals(executor.getIsInterrupted())) {
         try {
            LOGGER.debug("en attente de reprise de travail");
            Thread.sleep(THREAD_SLEEP);
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
   @SuppressWarnings(UNCHECKED)
   public final void logProcessError(final Object documentType,
         final Exception exception) {

      LOGGER.warn("erreur lors du traitement de persistance", exception);

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

   /**
    * Réalisé après le chunk
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

      ExitStatus status = stepExecution.getExitStatus();

      final JobExecution jobExecution = stepExecution.getJobExecution();

      @SuppressWarnings(UNCHECKED)
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
            .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      if (CollectionUtils.isNotEmpty(exceptions)) {
         // on peut être en cas d'erreur sans rollback : exemple erreur de
         // connexion à la base
         if (!FAILED_NO_RB.equals(status.getExitCode())) {
            status = ExitStatus.FAILED;
         }

      } else {

         status = ExitStatus.COMPLETED;

         executor.shutdown();

         executor.waitFinishInsertion();

         final ConcurrentLinkedQueue<UUID> list = getIntegratedDocuments();

         jobExecution.getExecutionContext().put(Constantes.NB_INTEG_DOCS,
               executor.getIntegratedDocuments().size());

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

      @SuppressWarnings(UNCHECKED)
      ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) jobExecution
            .getExecutionContext().get(Constantes.CODE_EXCEPTION);

      @SuppressWarnings(UNCHECKED)
      ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) jobExecution
            .getExecutionContext().get(Constantes.INDEX_EXCEPTION);

      @SuppressWarnings(UNCHECKED)
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
            .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      ExitStatus status;

      try {

         throw exception.getCause();

      } catch (InterruptionTraitementException e) {

         LOGGER.warn("{} - " + e.getMessage(), "stockageListener()");

         String idTraitement = (String) stepExecution.getJobParameters()
               .getString(Constantes.ID_TRAITEMENT);
         LOGGER
               .error(

                     "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                     idTraitement);

         stepExecution.getJobExecution().getExecutionContext().put(
               Constantes.FLAG_BUL003, Boolean.TRUE);

         String messageError = "La capture de masse en mode 'Tout ou rien' a été interrompue. Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.";

         codes.add(Constantes.ERR_BUL003);
         index.add(exception.getIndex());
         exceptions.add(new Exception(messageError));

         status = new ExitStatus("FAILED_NO_ROLLBACK");

      } catch (Exception e) {

         LOGGER.warn("{} - " + e.getMessage(), "stockageListener()", e);

         String message;
         if (exception.getCause() == null) {
            message = exception.getMessage();
         } else {
            message = exception.getCause().getMessage();
         }

         codes.add(Constantes.ERR_BUL001);
         index.add(exception.getIndex());
         exceptions.add(new Exception(message, e));

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
