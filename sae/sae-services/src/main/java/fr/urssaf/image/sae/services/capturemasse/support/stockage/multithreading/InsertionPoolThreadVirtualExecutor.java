/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.exception.InsertionMasseVirtualRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
@Component
public class InsertionPoolThreadVirtualExecutor extends ThreadPoolExecutor
      implements Serializable, DisposableBean {

   private static final long serialVersionUID = 1L;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionPoolThreadExecutor.class);

   private final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> integDocs;

   private InsertionMasseVirtualRuntimeException exception;

   private static final String PREFIX_TRACE = "InsertionPoolThreadExecutor()";

   private Boolean isInterrupted = null;

   @Autowired
   private final InterruptionTraitementMasseSupport support;

   @Autowired
   @Qualifier("interruption_capture_masse")
   private final InterruptionTraitementConfig config;

   /**
    * instanciation d'un {@link ThreadPoolExecutor} avec comme arguments : <br>
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
    * <code>workQueue</code> : {@link LinkedBlockingQueue}</li>
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

      super(poolConfiguration.getCorePoolSize(), poolConfiguration
            .getCorePoolSize(), 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      LOGGER
            .debug(
                  "{} - Taille du pool de threads pour l'insertion en masse dans DFCE: {}",
                  new Object[] { PREFIX_TRACE, this.getCorePoolSize() });

      this.integDocs = new ConcurrentLinkedQueue<CaptureMasseVirtualDocument>();

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

      final InsertionVirtualRunnable insertionRunnable = (InsertionVirtualRunnable) runnable;

      final VirtualStorageDocument storageDocument = insertionRunnable
            .getStorageDocument();
      final int indexDocument = insertionRunnable.getIndexDocument();

      if (throwable == null) {

         if (StringUtils.isBlank(storageDocument.getFileName())) {
            throw new CaptureMasseRuntimeException(
                  "le nom de fichier est vide.");
         }

         CaptureMasseVirtualDocument document = new CaptureMasseVirtualDocument();
         document.setIndex(indexDocument);
         document.setUuid(storageDocument.getUuid());
         document
               .setReferenceUUID(storageDocument.getReferenceFile().getUuid());

         integDocs.add(document);

         LOGGER
               .debug("{} - Stockage du document #{} ({}) uuid:{}",
                     new Object[] { PREFIX_TRACE, (indexDocument + 1),
                           storageDocument.getFileName(),
                           storageDocument.getUuid() });

      } else {

         setInsertionException((InsertionMasseVirtualRuntimeException) throwable);
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

      final InsertionVirtualRunnable insertionRunnable = (InsertionVirtualRunnable) runnable;

      final VirtualStorageDocument storageDocument = insertionRunnable
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
               final InsertionMasseVirtualRuntimeException except = new InsertionMasseVirtualRuntimeException(
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
         final InsertionMasseVirtualRuntimeException exception) {

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
    * @return the isInterrupted
    */
   public final Boolean getIsInterrupted() {
      return isInterrupted;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void destroy() throws Exception {
      this.shutdownNow();

   }

}
