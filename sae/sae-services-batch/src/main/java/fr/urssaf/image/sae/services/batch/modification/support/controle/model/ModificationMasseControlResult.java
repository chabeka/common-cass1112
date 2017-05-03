package fr.urssaf.image.sae.services.batch.modification.support.controle.model;

import java.io.Serializable;
import java.util.List;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Objet contenant le résultat de la capture des controles sur la capture de
 * masse.
 */
public class ModificationMasseControlResult implements Serializable {

   /**
    * SUID
    */
   private static final long serialVersionUID = 923492891165360497L;
   
   /**
    * Storage document {@link StorageDocument}
    */
   private List<StorageMetadata> storageMetadatasList;

   /**
    * @return the storageMetadatasList
    */
   public List<StorageMetadata> getStorageMetadatasList() {
      return storageMetadatasList;
   }

   /**
    * @param storageMetadatasList the storageMetadatasList to set
    */
   public void setStorageMetadatasList(List<StorageMetadata> storageMetadatasList) {
      this.storageMetadatasList = storageMetadatasList;
   }


}
