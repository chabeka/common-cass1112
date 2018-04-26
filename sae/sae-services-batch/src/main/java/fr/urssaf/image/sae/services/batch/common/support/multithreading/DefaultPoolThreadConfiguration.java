package fr.urssaf.image.sae.services.batch.common.support.multithreading;

public interface DefaultPoolThreadConfiguration {

   public final static int DEFAULT_CORE_POOL_SIZE = 20;

   /**
    * Retourne le pool size du manager de thread
    * 
    * @return core pool size
    */
   public int loadCorePoolSize();
}
