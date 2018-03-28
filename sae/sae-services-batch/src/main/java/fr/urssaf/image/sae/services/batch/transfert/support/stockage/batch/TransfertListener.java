package fr.urssaf.image.sae.services.batch.transfert.support.stockage.batch;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.AbstractInsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.transfert.support.stockage.multithreading.TransfertPoolThreadExecutor;

/**
 * Classe d'écoute du transfert de masse
 * 
 *
 */
@Component
public class TransfertListener extends AbstractListener {

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(TransfertListener.class);

   private static final int THREAD_SLEEP = 30000;

   /**
    * classe d'execution des threads
    */
   @Autowired
   private TransfertPoolThreadExecutor executor;

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(
         final JAXBElement<UntypedDocument> untypedType) {

      incrementCount();
   }

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
    * @return la liste identifiants des documents traités
    */
   protected final ConcurrentLinkedQueue<UUID> getIntegratedDocuments() {

      final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> list = executor
            .getIntegratedDocuments();

      final ConcurrentLinkedQueue<UUID> listUuid = new ConcurrentLinkedQueue<UUID>();
      if (CollectionUtils.isNotEmpty(list)) {
         for (TraitementMasseIntegratedDocument document : list) {
            listUuid.add(document.getIdentifiant());
         }
      }

      return listUuid;
   }

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
      getErrorMessageList().add(exception.toString());
      LOGGER.warn("Erreur lors de la lecture du fichier", exception);
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
      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexErreurListe().add(
            getStepExecution().getExecutionContext().getInt(
                  Constantes.CTRL_INDEX));
      LOGGER.warn("Erreur lors du transfert de document", exception);
      getErrorMessageList().add(exception.toString());
   }

   /**
    * Vérifie que le traitement est interrompu. Boucle tant que c'est le cas
    */
   @BeforeChunk
   protected final void beforeChunk() {
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
    * Réalisé après le chunk
    */
   @AfterChunk
   public final void afterChunk() {
      AbstractInsertionMasseRuntimeException exception = executor
            .getInsertionMasseException();

      if (exception != null) {
         throw exception;
      }
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
      ConcurrentLinkedQueue<String> errorMessageList = getErrorMessageList();
      if (CollectionUtils.isEmpty(errorMessageList)) {
         status = ExitStatus.COMPLETED;
      } else {
         status = ExitStatus.FAILED;
      }

      executor.shutdown();
      executor.waitFinishInsertion();

      final ConcurrentLinkedQueue<UUID> list = this.getIntegratedDocuments();

      jobExecution.getExecutionContext().put(Constantes.NB_INTEG_DOCS,
            executor.getIntegratedDocuments().size());

      jobExecution.getExecutionContext().remove(Constantes.THREAD_POOL);

      getStepExecution().setWriteCount(list.size());

      final AbstractInsertionMasseRuntimeException exception = executor
            .getInsertionMasseException();

      if (exception != null) {

         status = gestionException(exception);

      }

      return status;
   }

   private ExitStatus gestionException(
         final AbstractInsertionMasseRuntimeException exception) {

      String trcPrefix = "gestionException()";

      ConcurrentLinkedQueue<String> codes = getCodesErreurListe();
      ConcurrentLinkedQueue<Integer> index = getIndexErreurListe();
      ConcurrentLinkedQueue<String> errorMessageList = getErrorMessageList();

      ExitStatus status;

      try {
         throw exception.getCause();
      } catch (InterruptionTraitementException e) {
         
         String messageError = "Le transfert de masse en mode 'Partiel' a été interrompue. "
               + "Une procédure d'exploitation doit être initialisée afin de rejouer le traitement en echec.";
         LOGGER.warn("{} - " + messageError, trcPrefix);
         LOGGER.warn("{} - " + e.toString(), trcPrefix);

         getStepExecution().getJobExecution().getExecutionContext()
               .put(Constantes.FLAG_BUL003, Boolean.TRUE);

         codes.add(Constantes.ERR_TR_BUL001);
         index.add(exception.getIndex());
         errorMessageList.add(new Exception(messageError, e).toString());

         status = ExitStatus.FAILED;

      } catch (Exception e) {

         LOGGER.warn("{} - " + e.getMessage(), trcPrefix, e);

         String message;
         if (exception.getCause() == null) {
            message = exception.getMessage();
         } else {
            message = exception.getCause().getMessage();
         }

         LOGGER.warn("{} - " + e.getMessage(), trcPrefix, e);
         
         codes.add(Constantes.ERR_BUL001);
         index.add(exception.getIndex());
         errorMessageList.add(new Exception(message, e).toString());

         status = ExitStatus.FAILED;
      }

      return status;
   }

}
