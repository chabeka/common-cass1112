/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.dfce.services.support.multithreading.InsertionThreadPoolExecutor;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
public class InsertionPoolThreadExecutor extends ThreadPoolExecutor implements
      Serializable {

   private static final long serialVersionUID = 1L;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionThreadPoolExecutor.class);

   private List<CaptureMasseIntegratedDocument> integDocs;

   private InsertionMasseRuntimeException exception;

   // on limite le nombre simultané de Thread à 20
   private static final int CORE_POOL_SIZE = 20;

   private static final String PREFIX_TRACE = "InsertionPoolThreadExecutor()";

   private Boolean isInterrupted = null;

   private final InterruptionTraitementMasseSupport support;

   private final InterruptionTraitementConfig config;

   /**
    * instanciation d'un {@link ThreadPoolExecutor} avec comme arguments : <br>
    * <ul>
    * <li>
    * <code>corePoolSize</code> : {@value #CORE_POOL_SIZE}</li>
    * <li>
    * <code>maximumPoolSize</code> : {@value #CORE_POOL_SIZE}</li>
    * <li>
    * <code>keepAliveTime</code> : 0L</li>
    * <li>
    * <code>TimeUnit</code> : TimeUnit.MILLISECONDS</li>
    * <li>
    * <code>workQueue</code> : {@link LinkedBlockingQueue}</li>
    * <li><code>policy</code> : {@link DiscardPolicy}</li>
    * </ul>
    * 
    * le pool accepte un nombre fixe de threads de {@value #CORE_POOL_SIZE}
    * maximum.<br>
    * Les threads en plus sont stockés dans une liste non bornée<br>
    * Le temps de vie d'un thread n'est pas prise en compte ici
    * 
    * 
    * @param support
    *           support pour l'arrêt du traitement de la capture en masse
    * @param config
    *           configuration pour l'arrêt du traitement de la capture en masse
    */
   public InsertionPoolThreadExecutor(
         final InterruptionTraitementMasseSupport support,
         final InterruptionTraitementConfig config) {

      super(CORE_POOL_SIZE, CORE_POOL_SIZE, 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      this.integDocs = Collections
            .synchronizedList(new ArrayList<CaptureMasseIntegratedDocument>());

      Assert.notNull(support, "'support' is required");

      this.support = support;
      this.config = config;

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

      super.afterExecute(runnable, throwable);

      final InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;

      final StorageDocument storageDocument = insertionRunnable
            .getStorageDocument();
      final int indexDocument = insertionRunnable.getIndexDocument();

      // renseignement de l'index du document inséré pour l'indicateur JMX

      if (throwable == null) {

         if (StringUtils.isBlank(storageDocument.getFilePath())) {
            throw new CaptureMasseRuntimeException(
                  "le chemin de fichier est vide.");
         }

         final CaptureMasseIntegratedDocument document = new CaptureMasseIntegratedDocument();
         final File file = new File(storageDocument.getFilePath());
         document.setDocumentFile(file);
         document.setIdentifiant(storageDocument.getUuid());

         integDocs.add(document);

         LOGGER
               .debug("{} - Stockage du document #{} ({}) uuid:{}",
                     new Object[] { PREFIX_TRACE, indexDocument,
                           storageDocument.getFilePath(),
                           storageDocument.getUuid() });

      } else {

         setInsertionException((InsertionMasseRuntimeException) throwable);

         // dès le premier échec les autres Threads en exécution ou pas sont
         // interrompus définitivement
         this.shutdownNow();

      }

   }

   /**
    * 
    * 
    * Avant chaque insertion on vérifie si il ne faut pas interrompre le
    * traitement <br>
    * Auquel cas seul un seul Thread sera chargé de rétablir la connexion.<br>
    * 
    * @param thread
    *           le thread d'insertion d'un document
    * @param runnable
    *           l'exécutable d'insertion de type {@link InsertionRunnable}
    * 
    */
   @Override
   protected final void beforeExecute(final Thread thread,
         final Runnable runnable) {

      super.beforeExecute(thread, runnable);

      final InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;

      final StorageDocument storageDocument = insertionRunnable
            .getStorageDocument();
      final int indexDocument = insertionRunnable.getIndexDocument();

      // on vérifie que le traitement ne doit pas s'interrompre
      final DateTime currentDate = new DateTime();

      if (this.config != null && support.hasInterrupted(currentDate, config)) {

         synchronized (this) {

            // un seul thread est chargé de la reconnexion
            // les autre attendent
            while (Boolean.TRUE.equals(isInterrupted)) {

               try {
                  this.wait();
               } catch (InterruptedException e) {
                  throw new IllegalStateException(e);
               }

            }

            // isInterrupted == null signifie que c'est le premier passage
            // c'est donc ce thread là qui sera chargé de la reconnexion
            if (isInterrupted == null) {

               isInterrupted = Boolean.TRUE;
            }

         }

         if (Boolean.TRUE.equals(isInterrupted)) {

            try {

               // appel de la méthode de reconnexion
               support.interruption(currentDate, this.config);

            } catch (InterruptionTraitementException e) {

               // en cas d'échec de la reconnexion

               // levée d'une exception pour le document chargé de la
               // reconnexion
               final InsertionMasseRuntimeException except = new InsertionMasseRuntimeException(
                     indexDocument, storageDocument, e);
               this.setInsertionException(except);

               // les autres Threads en attente sont interrompus définitivement
               this.shutdownNow();

            } finally {

               // de toutes les façons il faut libérer l'ensemble des Threads en
               // attente
               isInterrupted = Boolean.FALSE;
               synchronized (this) {
                  this.notifyAll();
               }
            }

         }

      }

   }

   @Override
   protected final void terminated() {
      super.terminated();
      synchronized (this) {
         this.notifyAll();
      }

   }

   private void setInsertionException(
         final InsertionMasseRuntimeException exception) {

      synchronized (this) {

         // deux cas :
         // 1 - si aucune exception n'a juste à présent été levée alors on
         // stocke cette exception
         // 2 - sinon on stocke l'exception la plus récente dans l'ordre du
         // lancement des insertions soit dans l'ordre où sont les documents
         // dans le sommaire
         if (this.exception == null) {
            this.exception = exception;
         } else if (this.exception.getIndex() > exception.getIndex()) {
            this.exception = exception;
         }
      }
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
   public final List<CaptureMasseIntegratedDocument> getIntegratedDocuments() {
      return this.integDocs;
   }

   /**
    * vide la liste des documents persistés dans DFCE <br>
    * <br>
    * Attention : clear() ne garantit pas que les la liste soit vide à cause de
    * la synchronization on préfère donc instancier une liste vide
    * 
    */
   public final void clearStorageDocDone() {

      this.integDocs = Collections.emptyList();
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

}
