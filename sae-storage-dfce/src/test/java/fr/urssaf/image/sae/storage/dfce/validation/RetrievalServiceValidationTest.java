package fr.urssaf.image.sae.storage.dfce.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;

/**
 * Test les aspects pour la validation.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class RetrievalServiceValidationTest {

   @Autowired
   private CommonsServices commonsServices;

   @Before
   public void init() throws ConnectionServiceEx {
      commonsServices.initServicesParameters();
   }

   @After
   public void end() {
      commonsServices.closeServicesParameters();
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.RetrievalServiceValidation#retrieveStorageDocumentByUUID(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
    * retrieveStorageDocumentContentByUUID} <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void retrieveStorageDocumentByUUID() throws RetrievalServiceEx {
      commonsServices.getRetrievalService().retrieveStorageDocumentByUUID(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.RetrievalServiceValidation#retrieveStorageDocumentContentByUUID(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
    * retrieveStorageDocumentContentByUUID} <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void retrieveStorageDocumentContentByUUID() throws RetrievalServiceEx {
      commonsServices.getRetrievalService()
            .retrieveStorageDocumentContentByUUID(null);
   }

   /**
    * {@link fr.urssaf.image.sae.storage.dfce.RetrievalServiceValidation#retrieveStorageDocumentMetaDatasByUUID(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument)
    * retrieveStorageDocumentMetaDatasByUUID} <br>
    */
   @Test(expected = IllegalArgumentException.class)
   public void retrieveStorageDocumentMetaDatasByUUID()
         throws RetrievalServiceEx {
      commonsServices.getRetrievalService()
            .retrieveStorageDocumentMetaDatasByUUID(null);
   }
}
