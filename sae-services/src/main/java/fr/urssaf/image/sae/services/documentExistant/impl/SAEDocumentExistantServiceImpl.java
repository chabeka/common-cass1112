package fr.urssaf.image.sae.services.documentExistant.impl;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@Service
public class SAEDocumentExistantServiceImpl implements
SAEDocumentExistantService {

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;


   @Override
   public boolean documentExistant(final UUID idGed) throws
   SearchingServiceEx, ConnectionServiceEx {
      final UUIDCriteria uuidCrit = new UUIDCriteria(idGed,
                                                     new ArrayList<StorageMetadata>());
      final StorageDocument document = storageDocumentService
            .searchMetaDatasByUUIDCriteria(uuidCrit);
      if (document == null || (document != null && document.getUuid() == null)) {
         return false;
      }

      return true;
   }

}
