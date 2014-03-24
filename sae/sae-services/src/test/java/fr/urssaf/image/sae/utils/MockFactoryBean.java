/**
 * 
 */
package fr.urssaf.image.sae.utils;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe de factory pour créer les mocks
 * 
 */
public class MockFactoryBean {

   /**
    * Création d'un mock de storageDocumentService
    * 
    * @return un mock StorageDocumentService
    */
   public final StorageDocumentService createStorageDocumentService() {
      return EasyMock.createMock(StorageDocumentService.class);
   }

   /**
    * 
    * @return instance de {@link DeletionService}
    */
   public final DeletionService createDeletionService() {

      DeletionService service = EasyMock.createMock(DeletionService.class);

      return service;
   }

   /**
    * création d'un mock de {@link SAEEnrichmentMetadataService}
    * 
    * @return un mock SAEEnrichmentMetadataService
    */
   public final SAEEnrichmentMetadataService createEnrichmentMetaDataService() {

      return EasyMock.createMock(SAEEnrichmentMetadataService.class);
   }

   /**
    * création d'un mock de {@link StorageServiceProvider}
    * 
    * @return un mock StorageServiceProvider
    */
   public final StorageServiceProvider createStorageServiceProvider() {

      return EasyMock.createMock(StorageServiceProvider.class);
   }

   /**
    * création d'un mock de {@link DFCEServicesManager}
    * 
    * @return un mock DFCEServicesManager
    */
   public final DFCEServicesManager createServicesManager() {
      return EasyMock.createMock(DFCEServicesManager.class);
   }

   /**
    * création d'un mock {@link SAEDocumentService}
    * 
    * @return un mock SAEDocumentService
    */
   public final SAEDocumentService createSaeDocumentService() {
      return EasyMock.createMock(SAEDocumentService.class);
   }

}
