/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.impl;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

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
   public StorageDocument enrichirDocument(StorageDocument document, String uuid) {

      document.getMetadatas().add(new StorageMetadata("iti", uuid));
      document.setProcessId(uuid);

      return document;
      
   }

}
