/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.AbstractInsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Classe mère des listeners pour le stockage des documents
 * 
 * @param <BOT>
 *           Classe backoffice
 * @param <CAPT>
 *           Classe de capture
 */
public abstract class AbstractStockageListener<BOT, CAPT> extends
      AbstractListener {

   private static final int THREAD_SLEEP = 30000;
   private static final String FAILED_NO_RB = "FAILED_NO_ROLLBACK";

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void specificInitOperations() {
      getStepExecution().getExecutionContext().put(Constantes.CTRL_INDEX, -1);
   }

   /**
    * ajout d'erreurs dans la liste stockée dans le contexte d'execution du job
    * 
    * @param exception
    *           exception levée par la lecture
    */
   @OnReadError
   public final void logReadError(final Exception exception) {

      getCodesErreurListe().add(Constantes.ERR_BUL001);
      getIndexErreurListe().add(getStepExecution().getReadCount());
      getExceptionErreurListe().add(new Exception(exception.getMessage()));

      getLogger().warn("erreur lors de la lecture du fichier", exception);
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
   public final void logProcessError(final Object documentType,
         final Exception exception) {

      getLogger().warn("erreur lors du traitement de persistance", exception);

      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexErreurListe().add(
            getStepExecution().getExecutionContext().getInt(
                  Constantes.CTRL_INDEX));
      getExceptionErreurListe().add(new Exception(exception.getMessage()));
   }

   /**
    * Vérifie que le traitement est interrompu. Boucle tant que c'est le cas
    */
   @BeforeChunk
   protected final void beforeChunk() {
      while (Boolean.TRUE.equals(getExecutor().getIsInterrupted())) {
         try {
            getLogger().debug("en attente de reprise de travail");
            Thread.sleep(THREAD_SLEEP);
         } catch (InterruptedException e) {
            getLogger().info("Impossible de traiter l'interruption", e);
         }
      }
   }

   /**
    * Réalisé après le chunk
    */
   @AfterChunk
   public final void afterChunk() {

      AbstractInsertionMasseRuntimeException exception = getExecutor()
            .getInsertionMasseException();

      if (exception != null) {
         throw exception;
      }
   }

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

   /**
    * @return le pool d'execution des traitements
    */
   protected abstract AbstractPoolThreadExecutor<BOT, CAPT> getExecutor();

   /**
    * Incrémente le nombre de document traité de 1
    */
   protected final void incrementCount() {
      ExecutionContext context = getStepExecution().getExecutionContext();

      int valeur = context.getInt(Constantes.CTRL_INDEX);
      valeur++;

      context.put(Constantes.CTRL_INDEX, valeur);
   }

   /**
    * {@inheritDoc}
    * <ul>
    * <li>Vérification du traitement réalisé avec succès</li>
    * <li>débranchement vers la bonne étape suivante</li>
    * </ul>
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      ExitStatus status = getStepExecution().getExitStatus();

      final JobExecution jobExecution = getStepExecution().getJobExecution();

      ConcurrentLinkedQueue<Exception> exceptions = getExceptionErreurListe();

      if (CollectionUtils.isNotEmpty(exceptions)) {
         // on peut être en cas d'erreur sans rollback : exemple erreur de
         // connexion à la base
         if (!FAILED_NO_RB.equals(status.getExitCode())) {
            status = ExitStatus.FAILED;
         }

      } else {

         status = ExitStatus.COMPLETED;

         getExecutor().shutdown();

         getExecutor().waitFinishInsertion();

         final ConcurrentLinkedQueue<UUID> list = getIntegratedDocuments();

         jobExecution.getExecutionContext().put(Constantes.NB_INTEG_DOCS,
               getExecutor().getIntegratedDocuments().size());

         jobExecution.getExecutionContext().remove(Constantes.THREAD_POOL);

         getStepExecution().setWriteCount(list.size());

         final AbstractInsertionMasseRuntimeException exception = getExecutor()
               .getInsertionMasseException();

         if (exception != null) {

            status = stockageListener(exception);

         }
      }

      return status;
   }

   private ExitStatus stockageListener(
         AbstractInsertionMasseRuntimeException exception) {

      String trcPrefix = "stockageListener()";

      ConcurrentLinkedQueue<String> codes = getCodesErreurListe();
      ConcurrentLinkedQueue<Integer> index = getIndexErreurListe();
      ConcurrentLinkedQueue<Exception> exceptions = getExceptionErreurListe();

      ExitStatus status;

      try {

         throw exception.getCause();

      } catch (InterruptionTraitementException e) {

         getLogger().warn("{} - " + e.getMessage(), "stockageListener()");

         String idTraitement = (String) getStepExecution().getJobParameters()
               .getString(Constantes.ID_TRAITEMENT);
         getLogger()
               .error(

                     "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                     idTraitement);

         getStepExecution().getJobExecution().getExecutionContext().put(
               Constantes.FLAG_BUL003, Boolean.TRUE);

         String messageError = "La capture de masse en mode 'Tout ou rien' a été interrompue. "
               + "Une procédure d'exploitation a été initialisée pour supprimer les données "
               + "qui auraient pu être stockées.";

         codes.add(Constantes.ERR_BUL003);
         index.add(exception.getIndex());
         exceptions.add(new Exception(messageError));

         status = new ExitStatus("FAILED_NO_ROLLBACK");

      } catch (Exception e) {

         getLogger().warn("{} - " + e.getMessage(), trcPrefix, e);

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
    * @return la liste identifiants des documents traités
    */
   protected abstract ConcurrentLinkedQueue<UUID> getIntegratedDocuments();
}
