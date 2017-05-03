/**
 * 
 */
package fr.urssaf.image.sae.webservice.client.demo.service.multithreading;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
public class SupervisionPoolThreadExecutor extends ThreadPoolExecutor implements
      Serializable {

   private static final long serialVersionUID = 1L;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SupervisionPoolThreadExecutor.class);


   private static final String PREFIX_TRACE = "SupervisionPoolThreadExecutor()";

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
   public SupervisionPoolThreadExecutor() {

      super(20, 20, 1, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new DiscardPolicy());

      LOGGER.debug(
            "{} - Taille du pool de threads : {}",
            new Object[] { PREFIX_TRACE, this.getCorePoolSize() });


   }

   /**
    * Attend que l'ensemble des threads aient bien terminé leur travail
    */
   public final void waitFinishSupervision() {

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
    * {@inheritDoc}
    */
   @Override
   protected final void beforeExecute(Thread thread, Runnable runnable) {

      super.beforeExecute(thread, runnable);
   }

   /**
    * @param runnable
    *           le thread d'insertion d'un document
    * @param throwable
    *           l'exception éventuellement levée lors de l'insertion du document
    * 
    */
   @Override
   protected void afterExecute(final Runnable runnable,
         final Throwable throwable) {
      super.afterExecute(runnable, throwable);
   }

}
