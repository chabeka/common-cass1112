/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.AbstractInsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.InsertionMasseVirtualRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
@Component
public class InsertionPoolThreadVirtualExecutor
      extends
      AbstractPoolThreadExecutor<VirtualStorageDocument, CaptureMasseVirtualDocument>
      implements Serializable, DisposableBean {

   private static final long serialVersionUID = 1L;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionPoolThreadVirtualExecutor.class);

   private final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> integDocs;

   private InsertionMasseVirtualRuntimeException exception;

   private static final String PREFIX_TRACE = "InsertionPoolThreadExecutor()";

   /**
    * instanciation d'un {@link AbstractPoolThreadExecutor} avec comme arguments
    * : <br>
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
   public InsertionPoolThreadVirtualExecutor(
         InsertionPoolConfiguration poolConfiguration,
         final InterruptionTraitementMasseSupport support,
         final InterruptionTraitementConfig config) {

      super(poolConfiguration, support, config);

      LOGGER
            .debug(
                  "{} - Taille du pool de threads pour l'insertion en masse dans DFCE: {}",
                  new Object[] { PREFIX_TRACE, this.getCorePoolSize() });

      this.integDocs = new ConcurrentLinkedQueue<CaptureMasseVirtualDocument>();

   }

   /**
    * 
    * @return l'insertion levée lors du traitement de capture en masse
    */
   public final InsertionMasseVirtualRuntimeException getInsertionMasseException() {
      return this.exception;
   }

   /**
    * 
    * @return liste des documents persistés dans DFCE
    */
   public final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> getIntegratedDocuments() {
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
   protected final void addDocumentToIntegratedList(
         VirtualStorageDocument storageDocument, int indexDocument) {
      CaptureMasseVirtualDocument document = new CaptureMasseVirtualDocument();
      document.setIndex(indexDocument);
      document.setUuid(storageDocument.getUuid());
      document.setReferenceUUID(storageDocument.getReferenceFile().getUuid());

      integDocs.add(document);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final InsertionMasseVirtualRuntimeException createError(int index,
         VirtualStorageDocument document,
         InterruptionTraitementException exception) {
      return new InsertionMasseVirtualRuntimeException(index, document,
            exception);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final VirtualStorageDocument getDocumentFromRunnable(
         Runnable runnable) {
      InsertionVirtualRunnable virtualRunnable = (InsertionVirtualRunnable) runnable;
      return virtualRunnable.getStorageDocument();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final int getIndexFromRunnable(Runnable runnable) {
      InsertionVirtualRunnable virtualRunnable = (InsertionVirtualRunnable) runnable;
      return virtualRunnable.getIndexDocument();
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
   protected final String getPathName(VirtualStorageDocument document) {
      return document.getFileName();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final UUID getUuid(VirtualStorageDocument document) {
      return document.getUuid();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void setInsertionMasseRuntimeException(
         AbstractInsertionMasseRuntimeException exception) {
      this.exception = (InsertionMasseVirtualRuntimeException) exception;

   }
}
