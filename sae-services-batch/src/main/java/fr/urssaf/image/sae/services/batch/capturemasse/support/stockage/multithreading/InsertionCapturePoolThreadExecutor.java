/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.AbstractInsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
@Component
public class InsertionCapturePoolThreadExecutor
      extends
      AbstractPoolThreadExecutor<StorageDocument, TraitementMasseIntegratedDocument>
      implements Serializable, DisposableBean {

   // FIXME - Trouver une solution plus propre afin de ne pas partager les
   // ressources

   private static final long serialVersionUID = 1L;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionCapturePoolThreadExecutor.class);

   private final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs;

   private InsertionMasseRuntimeException exception;

   private static final String PREFIX_TRACE = "InsertionPoolThreadExecutor()";

   /**
    * instanciation d'un {@link AbstractPoolThreadExecutor} avec comme arguments : <br>
    * <ul>
    * <li>
    * <code>corePoolSize</code> :
    * {@link InsertionPoolConfiguration#getCorePoolSize()}</li>
    * <li>
    * <code>maximumPoolSize</code> :
    * {@link InsertionPoolConfiguration#getCorePoolSize()}</li>
    * <li>
    * <code>keepAliveTime</code> : 0L</li>
    * <li>
    * <code>TimeUnit</code> : TimeUnit.MILLISECONDS</li>
    * <li>
    * <code>workQueue</code> : LinkedBlockingQueue</li>
    * <li><code>policy</code> :
    * {@link java.util.concurrent.ThreadPoolExecutor.DiscardPolicy}</li>
    * </ul>
    * 
    * Le pool accepte un nombre fixe de threads configurable<br>
    * Les threads en plus sont stockés dans une liste non bornée<br>
    * Le temps de vie d'un thread n'est pas prise en compte ici
    * 
    * @param poolConfiguration
    *           configuration du pool d'insertion des documents dans DFCE
    * @param support
    *           support pour l'arrêt du traitement de la capture en masse
    * @param config
    *           configuration pour l'arrêt du traitement de la capture en masse
    */
   @Autowired
   public InsertionCapturePoolThreadExecutor(
         InsertionPoolConfiguration poolConfiguration,
         final InterruptionTraitementMasseSupport support,
         final InterruptionTraitementConfig config) {

      super(poolConfiguration, support, config);

      LOGGER
            .debug(
                  "{} - Taille du pool de threads pour l'insertion en masse dans DFCE: {}",
                  new Object[] { PREFIX_TRACE, this.getCorePoolSize() });

      this.integDocs = new ConcurrentLinkedQueue<TraitementMasseIntegratedDocument>();

   }

   /**
    * 
    * @return l'insertion levée lors du traitement de capture en masse
    */
   public final InsertionMasseRuntimeException getInsertionMasseException() {
      return this.exception;
   }

   /**
    * 
    * @return liste des documents persistés dans DFCE
    */
   public final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> getIntegratedDocuments() {
      return this.integDocs;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void destroy() throws Exception {
      this.shutdownNow();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void setInsertionMasseRuntimeException(
         AbstractInsertionMasseRuntimeException exception) {
      this.exception = (InsertionMasseRuntimeException) exception;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final AbstractInsertionMasseRuntimeException createError(
         int index, StorageDocument document,
         InterruptionTraitementException exception) {
      return new InsertionMasseRuntimeException(index, document, exception);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final StorageDocument getDocumentFromRunnable(Runnable runnable) {
      InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;
      return insertionRunnable.getStorageDocument();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final int getIndexFromRunnable(Runnable runnable) {
      InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;
      return insertionRunnable.getIndexDocument();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void addDocumentToIntegratedList(
         StorageDocument storageDocument, int indexDocument) {

      TraitementMasseIntegratedDocument document = new TraitementMasseIntegratedDocument();
      File file = new File(storageDocument.getFilePath());
      document.setDocumentFile(file);
      document.setIdentifiant(storageDocument.getUuid());
      document.setIndex(indexDocument);
      integDocs.add(document);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getPathName(StorageDocument document) {
      return document.getFilePath();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final UUID getUuid(StorageDocument document) {
      return document.getUuid();
   }

}
