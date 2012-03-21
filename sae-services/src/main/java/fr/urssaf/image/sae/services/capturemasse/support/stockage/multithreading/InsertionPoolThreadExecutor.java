/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Pool de thread pour l'insertion en masse dans DFCE
 * 
 */
public class InsertionPoolThreadExecutor extends ThreadPoolExecutor {

   // FIXME FBON - Implémentation de InsertionPoolThreadExecutor

   /**
    * @param corePoolSize
    * @param maximumPoolSize
    * @param keepAliveTime
    * @param unit
    * @param workQueue
    */
   public InsertionPoolThreadExecutor(int corePoolSize, int maximumPoolSize,
         long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
      // TODO Auto-generated constructor stub
   }

}
