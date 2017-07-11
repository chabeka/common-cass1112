package fr.urssaf.image.sae.services.batch.restore.support.stockage.multithreading;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.restore.support.stockage.exception.RestoreMasseRuntimeException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Pool de thread pour la restore de masse dans DFCE
 * 
 */
@Component
public class RestorePoolThreadExecutor extends ThreadPoolExecutor implements
      Serializable, DisposableBean {

   private static final long serialVersionUID = 1L;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RestorePoolThreadExecutor.class);

   private Boolean isInterrupted;
   private final InterruptionTraitementMasseSupport support;
   private final InterruptionTraitementConfig config;

   private RestoreMasseRuntimeException exception;

   private static final String PREFIX_TRACE = "RestorePoolThreadExecutor()";
   
   /**
    * Nombre de documents restorés.
    */
   private int nombreRestores;

   /**
    * Constructeur
    * 
    * @param poolConfiguration
    *           configuration du pool d'insertion des documents dans DFCE
    * @param support
    *           support pour l'arrêt du traitement de la restore en masse
    * @param config
    *           configuration pour l'arrêt du traitement de la restore en masse
    */
   @Autowired
   public RestorePoolThreadExecutor(
         RestorePoolConfiguration poolConfiguration,
         InterruptionTraitementMasseSupport support,
         InterruptionTraitementConfig config) {

      super(poolConfiguration.getCorePoolSize(), poolConfiguration
            .getCorePoolSize(), 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      Assert.notNull(support, "'support' is required");
      
      LOGGER
      .debug(
            "{} - Taille du pool de threads pour la restore de masse dans DFCE: {}",
            new Object[] { PREFIX_TRACE, this.getCorePoolSize() });

      this.config = config;
      this.support = support;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void destroy() throws Exception {
      this.shutdownNow();
   }

   /**
    * Attend que l'ensemble des threads aient bien terminé leur travail
    */
   public final void waitFinishRestore() {

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
   protected final void setRestoreException(
         final RestoreMasseRuntimeException exception) {

      if (getRestoreMasseException() == null) {
         setRestoreMasseRuntimeException(exception);
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
    * 
    * @return l'exception levée lors du traitement de restore en masse
    */
   public final synchronized RestoreMasseRuntimeException getRestoreMasseException() {
      return this.exception;
   }

   /**
    * @param exception
    *           l'exception
    */
   protected void setRestoreMasseRuntimeException(
         RestoreMasseRuntimeException exception) {
      this.exception = exception;
   }

   /**
    * @param runnable
    *           le traitement en cours
    * @return le document concerné
    */
   protected StorageDocument getDocumentFromRunnable(RestoreRunnable runnable) {
      return runnable.getStorageDocument();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void beforeExecute(Thread thread, Runnable runnable) {

      super.beforeExecute(thread, runnable);

      StorageDocument document = getDocumentFromRunnable((RestoreRunnable) runnable);

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
               this.setRestoreException(new RestoreMasseRuntimeException(
                     document, e));

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
    * Après chaque restore, plusieurs cas possibles : <br>
    * <ol>
    * <li>la restore a réussi : on incremente un compteur de document restores</li>
    * <li>la restore a échouée : on shutdown le pool de restore</li>
    * </ol>
    * 
    * @param runnable
    *           le thread de restore d'un document
    * @param throwable
    *           l'exception éventuellement levée lors de la restore du document
    * 
    */
   @Override
   protected final void afterExecute(final Runnable runnable,
         final Throwable throwable) {

      String trcPrefix = "afterExecute()";

      super.afterExecute(runnable, throwable);
      
      synchronized (this) {
         
         final StorageDocument document = getDocumentFromRunnable((RestoreRunnable) runnable);
   
         if (throwable == null) {
   
            LOGGER.info("{} - Restore de la corbeille du document (uuid:{})",
                  new Object[] { trcPrefix, document.getUuid().toString() });
            
            nombreRestores++;
   
         } else {
   
            setRestoreException((RestoreMasseRuntimeException) throwable);
            // dès le premier échec les autres Threads en exécution ou pas sont
            // interrompus définitivement
            this.shutdownNow();
   
         }
      }
   }
   
   /**
    * Permet de récupérer le nombre de documents restorés.
    * 
    * @return int
    */
   public final int getNombreRestores() {
      return nombreRestores;
   }

   /**
    * Permet de modifier le nombre de documents restorés.
    * 
    * @param nombreRestores
    *           nombre de documents restorés
    */
   public final void setNombreRestores(final int nombreRestores) {
      this.nombreRestores = nombreRestores;
   }
}
