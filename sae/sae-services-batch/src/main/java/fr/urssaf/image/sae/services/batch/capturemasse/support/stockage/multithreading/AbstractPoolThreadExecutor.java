/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.AbstractInsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.common.support.multithreading.DefaultPoolThreadConfiguration;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;

/**
 * classe mère des pools d'execution
 *
 * @param <BOT>
 *           classe backoffice
 * @param <CAPT>
 *           classe de capture
 */
public abstract class AbstractPoolThreadExecutor<BOT, CAPT> extends ThreadPoolExecutor {

   private static final long serialVersionUID = 1L;

   private final InterruptionTraitementMasseSupport support;

   private final InterruptionTraitementConfig config;

   /**
    * Constructeur
    *
    * @param poolConfiguration configuration du pool d'insertion des documents dans
    *                          DFCE
    * @param support           support pour l'arrêt du traitement de la capture en
    *                          masse
    * @param config            configuration pour l'arrêt du traitement de la
    *                          capture en masse
    */
   public AbstractPoolThreadExecutor(final DefaultPoolThreadConfiguration poolConfiguration,
                                     final InterruptionTraitementMasseSupport support, final InterruptionTraitementConfig config) {

      super(poolConfiguration.loadCorePoolSize(), poolConfiguration.loadCorePoolSize(), 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      Assert.notNull(support, "'support' is required");

      this.config = config;
      this.support = support;
   }

   /**
    * Attend que l'ensemble des threads aient bien terminé leur travail
    */
   public final void waitFinishInsertion() {
      try {
         awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
      } catch (final InterruptedException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * @param exception Mise à jour de la première erreur
    */
   protected final void setInsertionException(final AbstractInsertionMasseRuntimeException exception) {
      synchronized (this) {

         // deux cas :
         // 1 - si aucune exception n'a juste à présent été levée alors on
         // stocke cette exception
         // 2 - sinon on stocke l'exception la plus récente dans l'ordre du
         // lancement des insertions soit dans l'ordre où sont les documents
         // dans le sommaire
         if (getInsertionMasseException() == null) {
            setInsertionMasseRuntimeException(exception);
         } else if (getInsertionMasseException().getIndex() > exception.getIndex()) {
            setInsertionMasseRuntimeException(exception);
         }
      }
   }

   /**
    * @return the isInterrupted
    */
   public final Boolean isInterrupted() {
      return support.isInterrupted();
   }

   /**
    * @return le support d'interruption des traitements de masse
    */
   protected final InterruptionTraitementMasseSupport getSupport() {
      return support;
   }

   /**
    * @return la config d'interruption de traitement
    */
   protected final InterruptionTraitementConfig getConfig() {
      return config;
   }

   /**
    * @return l'exception
    */
   public abstract AbstractInsertionMasseRuntimeException getInsertionMasseException();

   /**
    * @param exception l'exception
    */
   protected abstract void setInsertionMasseRuntimeException(AbstractInsertionMasseRuntimeException exception);

   /**
    * @param runnable le traitement en cours
    * @return le document concerné
    */
   protected abstract BOT getDocumentFromRunnable(Runnable runnable);

   /**
    * @param runnable le traitement en cours
    * @return l'index du document concerné
    */
   protected abstract int getIndexFromRunnable(Runnable runnable);

   /**
    * Retourne l'erreur spécifique
    *
    * @param index     index du document en erreur
    * @param document  le document en erreur
    * @param exception exception source
    * @return l'erreur spécifique
    */
   protected abstract AbstractInsertionMasseRuntimeException createError(int index, BOT document,
                                                                         InterruptionTraitementException exception);

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void beforeExecute(final Thread thread, final Runnable runnable) {

      super.beforeExecute(thread, runnable);

      final BOT document = getDocumentFromRunnable(runnable);
      final int indexDocument = getIndexFromRunnable(runnable);

      try {

         this.getSupport().verifyInterruptedProcess(getConfig());

      } catch (final InterruptionTraitementException e) {

         // en cas d'échec de la reconnexion

         // levée d'une exception pour le document chargé de la
         // reconnexion
         final AbstractInsertionMasseRuntimeException except = createError(indexDocument, document, e);

         this.setInsertionException(except);

         // les autres Threads en attente sont interrompus définitivement
         shutdownNow();

      }
   }

   /**
    * Après chaque insertion, plusieurs cas possibles : <br>
    * <ol>
    * <li>l'insertion a réussi : on ajoute le résultat à liste des documents
    * persistés</li>
    * <li>l'insertion a échouée : MODE TOUT OU RIEN : on shutdown le pool
    * d'insertion</li> MODE PARTIEL : l'exception a été catchée en amont, et on
    * continue sur les autres documents
    * <li>l'insertion a réussi et était la dernière : on ajoute le résultat à liste
    * des documents persistés et on shutdown le pool d'insertion</li>
    * </ol>
    *
    * @param runnable  le thread d'insertion d'un document
    * @param throwable l'exception éventuellement levée lors de l'insertion du
    *                  document
    */
   @Override
   protected void afterExecute(final Runnable runnable, final Throwable throwable) {

      final String trcPrefix = "afterExecute()";

      super.afterExecute(runnable, throwable);

      final BOT document = getDocumentFromRunnable(runnable);
      final int indexDocument = getIndexFromRunnable(runnable);

      if (throwable == null) {

         traitementAfterExecute(trcPrefix, document, indexDocument);

      } else if (throwable != null && throwable.getCause() instanceof TraitementRepriseAlreadyDoneException) {
         addDocumentToIntegratedList(document, indexDocument);

         getLogger().debug("{} - Stockage du document #{} ({}) uuid:{} pour la reprise du traitement de masse",
                           new Object[] { trcPrefix, indexDocument + 1, getPathName(document), getUuid(document) });
      } else {

         setInsertionException((AbstractInsertionMasseRuntimeException) throwable);
         // dès le premier échec les autres Threads en exécution ou pas sont
         // interrompus définitivement
         shutdownNow();

      }

   }

   /**
    * Traitement lors de la fin d'execution du thread.
    *
    * @param trcPrefix     Trace préfixe
    * @param document      Document
    * @param indexDocument Index du document
    */
   protected void traitementAfterExecute(final String trcPrefix, final BOT document, final int indexDocument) {
      if (StringUtils.isBlank(getPathName(document))) {
         throw new CaptureMasseRuntimeException("le nom de fichier est vide.");
      }

      // On test si le document a été intégré ou non (en mode PARTIEL, si erreur
      // lors du stockage, l'exception est catchée et on renvoie un UUID null)
      if (getUuid(document) != null) {
         addDocumentToIntegratedList(document, indexDocument);

         getLogger().debug("{} - Stockage du document #{} ({}) uuid:{}",
                           new Object[] { trcPrefix, indexDocument + 1, getPathName(document), getUuid(document) });
      }
   }

   /**
    * Ajoute le document concerné dans la liste des documents intégrés
    *
    * @param document      le document concerné
    * @param indexDocument l'index du document
    */
   protected abstract void addDocumentToIntegratedList(BOT document, int indexDocument);

   /**
    * @return le logger concerné
    */
   protected abstract Logger getLogger();

   /**
    * @param document le document concerné
    * @return le nom ou le chemin du fichier
    */
   protected abstract String getPathName(BOT document);

   /**
    * @param document le document concerné
    * @return l'UUID ou le chemin du fichier
    */
   protected abstract UUID getUuid(BOT document);

   /**
    * @return la liste des documents intégrés
    */
   public abstract ConcurrentLinkedQueue<CAPT> getIntegratedDocuments();
}
