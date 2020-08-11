/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.stockage.multithreading;

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
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionPoolConfiguration;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
@Component
public class ModificationPoolThreadExecutor
extends
AbstractPoolThreadExecutor<StorageDocument, TraitementMasseIntegratedDocument>
                                            implements
                                            DisposableBean {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ModificationPoolThreadExecutor.class);

  private final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs;

  private InsertionMasseRuntimeException exception;

  private static final String PREFIX_TRACE = "InsertionPoolModificationThreadExecutor()";

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
  public ModificationPoolThreadExecutor(
                                        final ModificationPoolConfiguration poolConfiguration,
                                        final InterruptionTraitementMasseSupport support,
                                        final InterruptionTraitementConfig config) {

    super(poolConfiguration, support, config);

    LOGGER.debug(
                 "{} - Taille du pool de threads pour la modification en masse dans DFCE: {}",
                 new Object[] { PREFIX_TRACE, getCorePoolSize() });

    integDocs = new ConcurrentLinkedQueue<>();

  }

  /**
   * 
   * @return l'insertion levée lors du traitement de capture en masse
   */
  @Override
  public final InsertionMasseRuntimeException getInsertionMasseException() {
    return exception;
  }

  /**
   * 
   * @return liste des documents persistés dans DFCE
   */
  @Override
  public final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> getIntegratedDocuments() {
    return integDocs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void destroy() throws Exception {
    shutdownNow();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void setInsertionMasseRuntimeException(
                                                         final AbstractInsertionMasseRuntimeException exception) {
    this.exception = (InsertionMasseRuntimeException) exception;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final AbstractInsertionMasseRuntimeException createError(
                                                                     final int index, final StorageDocument document,
                                                                     final InterruptionTraitementException exception) {
    return new InsertionMasseRuntimeException(index, document, exception);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final StorageDocument getDocumentFromRunnable(final Runnable runnable) {
    final InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;
    return insertionRunnable.getStorageDocument();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final int getIndexFromRunnable(final Runnable runnable) {
    final InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;
    return insertionRunnable.getIndexDocument();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void addDocumentToIntegratedList(
                                                   final StorageDocument storageDocument, final int indexDocument) {
    final TraitementMasseIntegratedDocument document = new TraitementMasseIntegratedDocument();
    document.setIdentifiant(storageDocument.getUuid());
    document.setIndex(indexDocument);
    integDocs.add(document);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void traitementAfterExecute(final String trcPrefix,
                                        final StorageDocument document, final int indexDocument) {
    // On test si le document a été modifié ou non (en mode PARTIEL, si erreur
    // lors de la modification, l'exception est catchée et on renvoie un UUID
    // null)
    if (document.getUuid() != null) {
      addDocumentToIntegratedList(document, indexDocument);
      getLogger()
      .debug(
             "{} - Modification du document #{} uuid:{}",
             new Object[] { trcPrefix, indexDocument + 1,
                            getUuid(document) });
    }
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
  protected final String getPathName(final StorageDocument document) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final UUID getUuid(final StorageDocument document) {
    return document.getUuid();
  }

}
