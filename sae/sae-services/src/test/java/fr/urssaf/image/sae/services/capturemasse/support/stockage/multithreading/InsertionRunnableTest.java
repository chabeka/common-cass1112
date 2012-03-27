/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.batch.StorageDocumentWriter;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class InsertionRunnableTest {

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Autowired
   private StorageDocumentWriter writer;

   @Before
   public void init() {
      storageDocumentService = EasyMock
            .createMock(StorageDocumentService.class);
   }

   @After
   public void end() {
      EasyMock.reset(storageDocumentService);
   }

   @Test(expected = InsertionMasseRuntimeException.class)
   public void testRunRetourErreur() throws InsertionServiceEx {

      EasyMock
            .expect(
                  storageDocumentService
                        .insertStorageDocument(new StorageDocument()))
            .andThrow(new InsertionServiceEx());

      EasyMock.replay(storageDocumentService);

      InsertionRunnable insertionRunnable = new InsertionRunnable(0,
            new StorageDocument(), writer);

      insertionRunnable.run();
   }
   
   
}
