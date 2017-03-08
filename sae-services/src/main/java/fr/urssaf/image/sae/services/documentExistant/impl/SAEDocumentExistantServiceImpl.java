package fr.urssaf.image.sae.services.documentExistant.impl;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

@Service
public class SAEDocumentExistantServiceImpl implements
      SAEDocumentExistantService {
   
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;
   

   @Override
   public final boolean documentExistant(UUID idGed) throws 
          SearchingServiceEx, ConnectionServiceEx {
      // TODO Auto-generated method stub

      serviceProvider.openConnexion();
      UUIDCriteria uuidCrit = new UUIDCriteria(idGed,
            new ArrayList<StorageMetadata>());
      StorageDocument document = serviceProvider.getStorageDocumentService()
            .searchStorageDocumentByUUIDCriteria(uuidCrit);
      if (document == null){
         return false;
      }
         else
            return true;
   }

}
