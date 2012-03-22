/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
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

   /**
    * réalisé avant le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
   }

   /**
    * réalisé sur erreur de lecture
    * 
    * @param exception
    *           exception levée par la lecture
    */
   @OnReadError
   public final void logReadError(final Exception exception) {
      LOGGER.error("Une erreur interne à l'application est survenue "
            + "lors du traitement de la capture de masse", exception);
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
   public final void logProcessError(final DocumentType documentType,
         final Exception exception) {
      final CaptureMasseSommaireDocumentException documentException = new CaptureMasseSommaireDocumentException(
            stepExecution.getReadCount(), exception);
      stepExecution.getJobExecution().getExecutionContext().put(
            Constantes.DOC_EXCEPTION, documentException);
   }

   /**
    * @see réalisé après le chunk
    */
   @AfterChunk
   public final void logAfterChunk() {
      final JobExecution jobExecution = stepExecution.getJobExecution();

      final InsertionPoolThreadExecutor executor = (InsertionPoolThreadExecutor) jobExecution
            .getExecutionContext().get(Constantes.THREAD_POOL);

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

      final JobExecution jobExecution = stepExecution.getJobExecution();

      final InsertionPoolThreadExecutor executor = (InsertionPoolThreadExecutor) jobExecution
            .getExecutionContext().get(Constantes.THREAD_POOL);

      executor.shutdown();

      executor.waitFinishInsertion();

      final List<UUID> list = getIntegratedDocuments();

      jobExecution.getExecutionContext().put(Constantes.INTEG_DOCS, list);

      jobExecution.getExecutionContext().remove(Constantes.THREAD_POOL);

      stepExecution.setWriteCount(list.size());

      final InsertionMasseRuntimeException exception = executor
            .getInsertionMasseException();

      ExitStatus status = ExitStatus.COMPLETED;

      if (exception != null) {

         final CaptureMasseSommaireDocumentException erreur = new CaptureMasseSommaireDocumentException(
               exception.getIndex(), exception.getCause());

         stepExecution.getJobExecution().getExecutionContext().put(
               Constantes.DOC_EXCEPTION, erreur);

         status = ExitStatus.FAILED;
      }

      return status;
   }

   private List<UUID> getIntegratedDocuments() {
      final JobExecution jobExecution = stepExecution.getJobExecution();

      final InsertionPoolThreadExecutor executor = (InsertionPoolThreadExecutor) jobExecution
            .getExecutionContext().get(Constantes.THREAD_POOL);
      final List<CaptureMasseIntegratedDocument> list = executor
            .getIntegratedDocuments();

      final List<UUID> listUuid = new ArrayList<UUID>();
      if (CollectionUtils.isNotEmpty(list)) {
         for (CaptureMasseIntegratedDocument document : list) {
            listUuid.add(document.getIdentifiant());
         }
      }

      return listUuid;
   }

}
