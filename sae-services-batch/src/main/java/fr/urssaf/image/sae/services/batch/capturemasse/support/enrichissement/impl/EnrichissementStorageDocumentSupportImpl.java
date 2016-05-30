/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Implémentation du support {@link EnrichissementStorageDocumentSupport}
 */
@Component
public class EnrichissementStorageDocumentSupportImpl implements
      EnrichissementStorageDocumentSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument enrichirDocument(StorageDocument document,
         String uuid) {

      addUuidToMetadatas(document.getMetadatas(), uuid);
      document.setProcessId(uuid);

      return document;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final VirtualStorageDocument enrichirVirtualDocument(
         VirtualStorageDocument document, String uuid) {

      addUuidToMetadatas(document.getMetadatas(), uuid);
      document.setProcessUuid(uuid);

      return document;
   }

   private void addUuidToMetadatas(List<StorageMetadata> metadatas, String uuid) {
      metadatas.add(new StorageMetadata("iti", uuid));
   }

}
