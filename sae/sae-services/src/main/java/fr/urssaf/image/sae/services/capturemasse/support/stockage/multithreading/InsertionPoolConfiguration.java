package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

/**
 * Configuration spécifique du Pool de Threads pour l'insertion des documents
 * dans DFCE.<br>
 * <ul>
 * <li>
 * <code>corePoolSize</code> : taille du pool, c'est à dire le nombre de
 * documents qui peuvent être injectés simultanément dans DFCE</li>
 * </ul>
 * 
 * 
 */
public class InsertionPoolConfiguration {

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

}
