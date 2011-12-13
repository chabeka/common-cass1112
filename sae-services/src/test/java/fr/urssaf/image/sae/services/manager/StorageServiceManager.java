package fr.urssaf.image.sae.services.manager;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;

/**
 * Implémentation des Mocks de la couche sae-storage
 * 
 * 
 */
public final class StorageServiceManager {

   private StorageServiceManager() {

   }

   /**
    * 
    * @return instance de {@link InterruptionTraitementSupport}
    */
   public static InterruptionTraitementSupport createInterruptionTraitementSupport() {

      InterruptionTraitementSupport service = EasyMock
            .createMock(InterruptionTraitementSupport.class);

      return service;
   }

}
