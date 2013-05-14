/**
 * 
 */
package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Mise à jour des archives
 * 
 */
public interface UpdateService {

   /**
    * Réalise la mise à jour du document dans DFCE. Ecrase l'intégralité du
    * document : toute métadonnée non renseignée sera écrasée
    * 
    * @param uuid
    *           identifiant unique du document
    * @param modifiedMetadatas
    *           Liste des métadonnées à modifier
    * @param deletedMetadatas
    *           Liste des métadonnées à supprimer
    * @throws UpdateServiceEx
    *            Exception levée lorsque la modification du document est en
    *            erreur
    */
   void updateStorageDocument(UUID uuid,
         List<StorageMetadata> modifiedMetadatas,
         List<StorageMetadata> deletedMetadatas) throws UpdateServiceEx;

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link InsertionService}
    */
   <T> void setUpdateServiceParameter(T parameter);
}
