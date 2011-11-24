package fr.urssaf.image.sae.services.factory;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe d'instanciation du modele
 * <code>fr.urssaf.image.sae.storage.model.storagedocument</code>
 * 
 * 
 */
public final class SAEStorageFactory {

   private SAEStorageFactory() {

   }

   /**
    * 
    * @param shortCode
    *           code court de la métadonnée
    * @return instance de {@link StorageMetadata}
    */
   public static StorageMetadata createStorageMetadata(String shortCode) {

      return new StorageMetadata(shortCode);
   }

}
