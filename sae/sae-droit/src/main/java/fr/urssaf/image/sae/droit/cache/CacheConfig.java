/**
 * 
 */
package fr.urssaf.image.sae.droit.cache;

/**
 * Bean de configuration du cache
 * 
 */
public class CacheConfig {

   private static final int CACHE_DURATION = 30;

   private int droitsCacheDuration = CACHE_DURATION;
   
   private boolean initCacheOnStartupDroits = false;

   /**
    * @return the droitsCacheDuration
    */
   public final int getDroitsCacheDuration() {
      return droitsCacheDuration;
   }

   /**
    * @param droitsCacheDuration
    *           the droitsCacheDuration to set
    */
   public final void setDroitsCacheDuration(int droitsCacheDuration) {
      this.droitsCacheDuration = droitsCacheDuration;
   }

   /**
    * @return the initCacheOnStartupDroits
    */
   public final boolean isInitCacheOnStartupDroits() {
      return initCacheOnStartupDroits;
   }
   
   /**
    * @param initCacheOnStartupDroits
    *           the initCacheOnStartupDroits to set
    */
   public final void setInitCacheOnStartupDroits(boolean initCacheOnStartupDroits) {
      this.initCacheOnStartupDroits = initCacheOnStartupDroits;
   }
}
