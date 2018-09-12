package fr.urssaf.image.sae.services.batch.transfert.support.stockage.multithreading;

import fr.urssaf.image.sae.services.batch.common.support.multithreading.DefaultPoolThreadConfiguration;

/**
 * Configuration spécifique du Pool de Threads pour le transfert ou la suppression des documents
 * <br>
 * <ul>
 * <li>
 * <code>corePoolSize</code> : taille du pool, c'est à dire le nombre de
 * documents qui peuvent être transférés simultanément dans DFCE</li>
 * </ul>
 * 
 * 
 */
public class TransfertPoolConfiguration implements
      DefaultPoolThreadConfiguration {
   
   /**
    * Core pool size
    */
   private int corePoolSize;

   /**
    * @return corePoolSize (taille du pool)
    */
   public int getCorePoolSize() {
      return corePoolSize;
   }

   /**
    * @param corePoolSize
    */
   public void setCorePoolSize(final int corePoolSize) {
      this.corePoolSize = corePoolSize;
   }

   @Override
   public int loadCorePoolSize() {
      return corePoolSize > 0 ? corePoolSize : DEFAULT_CORE_POOL_SIZE;
   }
   
   

}
