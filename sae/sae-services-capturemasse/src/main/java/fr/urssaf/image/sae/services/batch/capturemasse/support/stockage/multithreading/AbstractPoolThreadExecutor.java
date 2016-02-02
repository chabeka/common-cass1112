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
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.AbstractInsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;

/**
 * classe mère des pools d'execution
 * 
 * @param <BOT>
 *           classe backoffice
 * @param <CAPT>
 *           classe de capture
 * 
 */
public abstract class AbstractPoolThreadExecutor<BOT, CAPT> extends
      ThreadPoolExecutor {

   private static final long serialVersionUID = 1L;
   private Boolean isInterrupted;
   private final InterruptionTraitementMasseSupport support;
   private final InterruptionTraitementConfig config;

   /**
    * Constructeur
    * 
    * @param poolConfiguration
    *           configuration du pool d'insertion des documents dans DFCE
    * @param support
    *           support pour l'arrêt du traitement de la capture en masse
    * @param config
    *           configuration pour l'arrêt du traitement de la capture en masse
    */
   public AbstractPoolThreadExecutor(
         InsertionPoolConfiguration poolConfiguration,
         InterruptionTraitementMasseSupport support,
         InterruptionTraitementConfig config) {

      super(poolConfiguration.getCorePoolSize(), poolConfiguration
            .getCorePoolSize(), 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      Assert.notNull(support, "'support' is required");

      this.config = config;
      this.support = support;
   }

   /**
    * Attend que l'ensemble des threads aient bien terminé leur travail
    */
   public final void waitFinishInsertion() {

      synchronized (this) {

         while (!this.isTerminated()) {

            try {

               this.wait();

            } catch (InterruptedException e) {

               throw new IllegalStateException(e);
            }
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void terminated() {
      super.terminated();
      synchronized (this) {
         this.notifyAll();
      }

   }

   /**
    * @param exception
    *           Mise à jour de la première erreur
    */
   protected final void setInsertionException(
         final AbstractInsertionMasseRuntimeException exception) {

      synchronized (this) {

         // deux cas :
         // 1 - si aucune exception n'a juste à présent été levée alors on
         // stocke cette exception
         // 2 - sinon on stocke l'exception la plus récente dans l'ordre du
         // lancement des insertions soit dans l'ordre où sont les documents
         // dans le sommaire
         if (getInsertionMasseException() == null) {
            setInsertionMasseRuntimeException(exception);
         } else if (getInsertionMasseException().getIndex() > exception
               .getIndex()) {
            setInsertionMasseRuntimeException(exception);
         }
      }
   }

   /**
    * @return the isInterrupted
    */
   public final Boolean getIsInterrupted() {
      return isInterrupted;
   }

   /**
    * @param isInterrupted
    *           indicateur de traitement interrompu
    */
   protected final void setIsInterrupted(Boolean isInterrupted) {
      this.isInterrupted = isInterrupted;
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
    * @param exception
    *           l'exception
    */
   protected abstract void setInsertionMasseRuntimeException(
         AbstractInsertionMasseRuntimeException exception);

   /**
    * @param runnable
    *           le traitement en cours
    * @return le document concerné
    */
   protected abstract BOT getDocumentFromRunnable(Runnable runnable);

   /**
    * @param runnable
    *           le traitement en cours
    * @return l'index du document concerné
    */
   protected abstract int getIndexFromRunnable(Runnable runnable);

   /**
    * Retourne l'erreur spécifique
    * 
    * @param index
    *           index du document en erreur
    * @param document
    *           le document en erreur
    * @param exception
    *           exception source
    * @return l'erreur spécifique
    */
   protected abstract AbstractInsertionMasseRuntimeException createError(
         int index, BOT document, InterruptionTraitementException exception);

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void beforeExecute(Thread thread, Runnable runnable) {

      super.beforeExecute(thread, runnable);

      BOT document = getDocumentFromRunnable(runnable);
      int indexDocument = getIndexFromRunnable(runnable);

      // on vérifie que le traitement ne doit pas s'interrompre
      final DateTime currentDate = new DateTime();

      if (getConfig() != null
            && getSupport().hasInterrupted(currentDate, getConfig())) {

         synchronized (this) {

            // un seul thread est chargé de la reconnexion
            // les autre attendent
            while (Boolean.TRUE.equals(getIsInterrupted())) {

               try {
                  this.wait();
               } catch (InterruptedException e) {
                  throw new IllegalStateException(e);
               }

            }

            // isInterrupted == null signifie que c'est le premier passage
            // c'est donc ce thread là qui sera chargé de la reconnexion
            if (getIsInterrupted() == null) {

               setIsInterrupted(Boolean.TRUE);
            }

         }

         if (Boolean.TRUE.equals(getIsInterrupted())) {

            try {

               // appel de la méthode de reconnexion
               getSupport().interruption(currentDate, getConfig());

            } catch (InterruptionTraitementException e) {

               // en cas d'échec de la reconnexion

               // levée d'une exception pour le document chargé de la
               // reconnexion
               AbstractInsertionMasseRuntimeException except = createError(
                     indexDocument, document, e);

               this.setInsertionException(except);

               // les autres Threads en attente sont interrompus définitivement
               this.shutdownNow();

            } finally {

               // de toutes les façons il faut libérer l'ensemble des Threads en
               // attente
               setIsInterrupted(Boolean.FALSE);
               synchronized (this) {
                  this.notifyAll();
               }
            }

         }

      }
   }

   /**
    * 
    * 
    * Après chaque insertion, plusieurs cas possibles : <br>
    * <ol>
    * <li>l'insertion a réussi : on ajoute le résultat à liste des documents
    * persistés</li>
    * <li>l'insertion a échouée : on shutdown le pool d'insertion</li>
    * <li>l'insertion a réussi et était la dernière : on ajoute le résultat à
    * liste des documents persistés et on shutdown le pool d'insertion</li>
    * </ol>
    * 
    * @param runnable
    *           le thread d'insertion d'un document
    * @param throwable
    *           l'exception éventuellement levée lors de l'insertion du document
    * 
    */
   @Override
   protected final void afterExecute(final Runnable runnable,
         final Throwable throwable) {

      String trcPrefix = "afterExecute()";

      super.afterExecute(runnable, throwable);

      final BOT document = getDocumentFromRunnable(runnable);
      int indexDocument = getIndexFromRunnable(runnable);

      if (throwable == null) {

         if (StringUtils.isBlank(getPathName(document))) {
            throw new CaptureMasseRuntimeException(
                  "le nom de fichier est vide.");
         }

         addDocumentToIntegratedList(document, indexDocument);

         getLogger().debug(
               "{} - Stockage du document #{} ({}) uuid:{}",
               new Object[] { trcPrefix, (indexDocument + 1),
                     getPathName(document), getUuid(document) });

      } else {

         setInsertionException((AbstractInsertionMasseRuntimeException) throwable);
         // dès le premier échec les autres Threads en exécution ou pas sont
         // interrompus définitivement
         this.shutdownNow();

      }

   }

   /**
    * Ajoute le document concerné dans la liste des documents intégrés
    * 
    * @param document
    *           le document concerné
    * @param indexDocument
    *           l'index du document
    */
   protected abstract void addDocumentToIntegratedList(BOT document,
         int indexDocument);

   /**
    * @return le logger concerné
    */
   protected abstract Logger getLogger();

   /**
    * @param document
    *           le document concerné
    * @return le nom ou le chemin du fichier
    */
   protected abstract String getPathName(BOT document);

   /**
    * @param document
    *           le document concerné
    * @return l'UUID ou le chemin du fichier
    */
   protected abstract UUID getUuid(BOT document);

   /**
    * @return la liste des documents intégrés
    */
   public abstract ConcurrentLinkedQueue<CAPT> getIntegratedDocuments();
}
