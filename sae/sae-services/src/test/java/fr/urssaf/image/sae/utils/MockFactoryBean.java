/**
 * 
 */
package fr.urssaf.image.sae.utils;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport;
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
   public final StorageDocumentService createStorageServiceProvider() {
      return EasyMock.createMock(StorageDocumentService.class);
   }

   /**
    * création d'un mock de CaptureMasseControleSupport
    * 
    * @return un mock CaptureMasseControleSupport
    */
   public final CaptureMasseControleSupport createCaptureMasseControleSupport() {
      return EasyMock.createMock(CaptureMasseControleSupport.class);
   }

   /**
    * 
    * @return instance de {@link DeletionService}
    */
   public final DeletionService createDeletionService() {

      DeletionService service = EasyMock.createMock(DeletionService.class);

      return service;
   }
}
