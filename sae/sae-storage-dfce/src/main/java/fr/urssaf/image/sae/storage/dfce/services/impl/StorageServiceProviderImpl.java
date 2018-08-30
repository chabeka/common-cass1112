package fr.urssaf.image.sae.storage.dfce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Fournit la façade des implementations des services
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.StorageDocumentServiceImpl}
 */
@Service("storageServiceProvider")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class StorageServiceProviderImpl implements StorageServiceProvider {

   @Autowired
   @Qualifier("storageDocumentService")
   @SuppressWarnings("PMD.LongVariable")
   private StorageDocumentService storageDocumentService;

   @Autowired
   @Qualifier("dfceServices")
   private DFCEServices dfceServices;


   /**
    * @return Les services d'insertion ,de recherche, de suppression,de
    *         récupération
    */
   @Override
   public StorageDocumentService getStorageDocumentService() {
      return storageDocumentService;
   }

   /**
    * Initialise la façade des services d'insertion ,de recherche,récupération
    *
    * @param storageDocumentService
    *           : la façade des services d'insertion ,de recherche,récupération
    */
   @SuppressWarnings("PMD.LongVariable")
   public void setStorageDocumentService(
                                         final StorageDocumentService storageDocumentService) {
      this.storageDocumentService = storageDocumentService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void openConnexion() {
      dfceServices.connectTheFistTime();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void closeConnexion() {
      dfceServices.closeConnexion();
   }

}
