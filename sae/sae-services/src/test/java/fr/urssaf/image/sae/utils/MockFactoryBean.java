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

   public StorageDocumentService createStorageServiceProvider() {
      System.out.println("CREATION BEAN StorageDocumentService");
      return EasyMock.createMock(StorageDocumentService.class);
   }

}
