package fr.urssaf.image.sae.storage.util;

import java.util.List;

import org.apache.commons.lang.Validate;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe utilitaire pour la manipulation des instances de
 * {@link StorageMetadata}
 * 
 * 
 */
public final class StorageMetadataUtils {

   private StorageMetadataUtils() {

   }

   /**
    * Récupére la valeur de la métadonnée.
    * 
    * @param storageMetadatas
    *           : liste des métadonnées.
    * @param shortCode
    *           : Code court.
    * @return Valeur de la métadonnée, null si aucune métadonnée n'est trouvée.
    */
   public static String valueMetadataFinder(
         List<StorageMetadata> storageMetadatas, String shortCode) {

      Validate.notEmpty(storageMetadatas);
      Validate.notEmpty(shortCode);

      String metadataValue = null;
      for (StorageMetadata storageMetadata : storageMetadatas) {
         if (shortCode.equals(storageMetadata.getShortCode())) {
            metadataValue = (String) storageMetadata.getValue();
            break;
         }
      }
      return metadataValue;
   }
   
   /**
    * Récupére la valeur de la métadonnée.
    * 
    * @param storageMetadatas
    *           : liste des métadonnées.
    * @param shortCode
    *           : Code court.
    * @return Valeur de la métadonnée, null si aucune métadonnée n'est trouvée.
    */
   public static Object valueObjectMetadataFinder(
         List<StorageMetadata> storageMetadatas, String shortCode) {

      Validate.notEmpty(storageMetadatas);
      Validate.notEmpty(shortCode);

      Object metadataValue = null;
      for (StorageMetadata storageMetadata : storageMetadatas) {
         if (shortCode.equals(storageMetadata.getShortCode())) {
            metadataValue = storageMetadata.getValue();
            break;
         }
      }
      return metadataValue;
   }
}
