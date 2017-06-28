package fr.urssaf.image.sae.services.batch.modification.support.stockage.multithreading;

import fr.urssaf.image.sae.services.batch.common.support.multithreading.DefaultPoolThreadConfiguration;

/**
 * Configuration spécifique du Pool de Threads pour la modification des documents
 * dans DFCE.<br>
 * <ul>
 * <li>
 * <code>corePoolSize</code> : taille du pool, c'est à dire le nombre de
 * documents qui peuvent être injectés simultanément dans DFCE</li>
 * </ul>
 * 
 * 
 */
public class ModificationPoolConfiguration implements
      DefaultPoolThreadConfiguration {

   /**
    * Core pool size
    */
   private int corePoolSize;

   /**
    * 
    * @param corePoolSize
    *           taille du pool
    */
   public final void setCorePoolSize(int corePoolSize) {
      this.corePoolSize = corePoolSize;
   }

   /**
    * 
    * @return taille du pool
    */
   public final int getCorePoolSize() {
      return corePoolSize;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int loadCorePoolSize() {
      return corePoolSize > 0 ? corePoolSize : DEFAULT_CORE_POOL_SIZE;
   }

}
