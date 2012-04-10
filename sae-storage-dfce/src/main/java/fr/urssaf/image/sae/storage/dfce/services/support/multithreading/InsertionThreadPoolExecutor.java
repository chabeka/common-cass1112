package fr.urssaf.image.sae.storage.dfce.services.support.multithreading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Pool de {@link Thread} pour l'insertion en masse de DFCE
 * 
 * 
 */
public class InsertionThreadPoolExecutor extends ThreadPoolExecutor {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionThreadPoolExecutor.class);

   private List<StorageDocument> storageDocDone;

   private InsertionMasseRuntimeException insertionException;

   // on limite le nombre simultané de Thread à 20
   private static final int CORE_POOL_SIZE = 20;

   private static final String PREFIX_TRACE = "bulkInsertStorageDocument()";

   private Boolean isInterrupted = null;

   private final InterruptionTraitementSupport interruptionSupport;

   private final InterruptionTraitementConfig interruptionConfig;

   private final JmxIndicator jmxIndicator;

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
    * @param interruptionSupport
    *           support pour l'arrêt du traitement de la capture en masse
    * @param interruptionConfig
    *           configuration pour l'arrêt du traitement de la capture en masse
    * @param jmxIndicator
    *           JMX indicator
    */
   public InsertionThreadPoolExecutor(
         InterruptionTraitementSupport interruptionSupport,
         InterruptionTraitementConfig interruptionConfig,
         JmxIndicator jmxIndicator) {

      super(CORE_POOL_SIZE, CORE_POOL_SIZE, 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      this.storageDocDone = Collections
            .synchronizedList(new ArrayList<StorageDocument>());

      Assert.notNull(interruptionSupport, "'interruptionSupport' is required");
      Assert.notNull(jmxIndicator, "'jmxIndicator' is required");

      this.interruptionSupport = interruptionSupport;
      this.interruptionConfig = interruptionConfig;
      this.jmxIndicator = jmxIndicator;

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
   protected final void afterExecute(Runnable runnable, Throwable throwable) {

      super.afterExecute(runnable, throwable);

      InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;

      StorageDocument storageDocument = insertionRunnable.getStorageDocument();
      int indexDocument = insertionRunnable.getIndexDocument();

      // renseignement de l'index du document inséré pour l'indicateur JMX
      this.jmxIndicator.setJmxStorageIndex(indexDocument);

      if (throwable == null) {

         storageDocDone.add(storageDocument);

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

      // la solution est assez fragile étant donnée qu'il est important que la
      // propriété jmxCountDocument soit bien renseigné
      if (this.getCompletedTaskCount() == this.jmxIndicator
            .getJmxCountDocument() - 1) {

         this.shutdown();
      }

   }

   /**
    * 
    * 
    * Avant chaque insertion on vérifie si il ne faut pas interrompre le
    * traitement <br>
    * Auquel cas seul un seul Thread sera chargé de rétablir la connexiob.<br>
    * 
    * @param thread
    *           le thread d'insertion d'un document
    * @param runnable
    *           l'exécutable d'insertion de type {@link InsertionRunnable}
    * 
    */
   @Override
   protected final void beforeExecute(Thread thread, Runnable runnable) {

      super.beforeExecute(thread, runnable);

      InsertionRunnable insertionRunnable = (InsertionRunnable) runnable;

      StorageDocument storageDocument = insertionRunnable.getStorageDocument();
      int indexDocument = insertionRunnable.getIndexDocument();

      // on vérifie que le traitement ne doit pas s'interrompre
      DateTime currentDate = new DateTime();

      if (this.interruptionConfig != null
            && interruptionSupport.hasInterrupted(currentDate,
                  interruptionConfig)) {

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
               interruptionSupport.interruption(currentDate,
                     this.interruptionConfig, this.jmxIndicator);

            } catch (InterruptionTraitementException e) {

               // en cas d'échec de la reconnexion

               // levée d'une exception pour le document chargé de la
               // reconnexion
               InsertionMasseRuntimeException exception = new InsertionMasseRuntimeException(
                     indexDocument, storageDocument, e);
               this.setInsertionException(exception);

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

   private void setInsertionException(InsertionMasseRuntimeException exception) {

      synchronized (this) {

         // deux cas :
         // 1 - si aucune exception n'a juste à présent été levée alors on
         // stocke cette exception
         // 2 - sinon on stocke l'exception la plus récente dans l'ordre du
         // lancement des insertions soit dans l'ordre où sont les documents
         // dans le sommaire
         if (insertionException == null) {
            this.insertionException = exception;
         } else if (this.insertionException.getIndex() > exception.getIndex()) {
            this.insertionException = exception;
         }
      }
   }

   /**
    * 
    * @return l'insertion levée lors du traitement de capture en masse
    */
   public final InsertionMasseRuntimeException getInsertionMasseException() {
      return this.insertionException;
   }

   /**
    * 
    * @return liste des documents persistés dans DFCE
    */
   public final List<StorageDocument> getStorageDocDone() {
      return this.storageDocDone;
   }

   /**
    * vide la liste des documents persistés dans DFCE <br>
    * <br>
    * Attention : clear() ne garantit pas que les la liste soit vide à cause de
    * la synchronization on préfère donc instancier une liste vide
    * 
    */
   public final void clearStorageDocDone() {

      this.storageDocDone = Collections.emptyList();
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
