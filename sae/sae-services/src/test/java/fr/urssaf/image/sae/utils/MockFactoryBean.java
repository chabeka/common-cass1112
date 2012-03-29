/**
 * 
 */
package fr.urssaf.image.sae.utils;

import org.easymock.EasyMock;

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

}
