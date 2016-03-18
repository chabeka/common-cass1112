/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading;

import java.util.Date;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.StorageDocumentWriter;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml",
      "/applicationContext-sae-services-batch-test.xml" })
public class InsertionRunnableTest {

   @Autowired
   @Qualifier("storageDocumentService")
   @SuppressWarnings("PMD.LongVariable")
   private StorageDocumentService storageDocumentService;

   @Autowired
   private StorageDocumentWriter writer;

   @After
   public void end() {
      EasyMock.reset(storageDocumentService);
   }

   @Test(expected = InsertionMasseRuntimeException.class)
   public void testRunRetourErreur() throws InsertionServiceEx,
         InsertionIdGedExistantEx {

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andThrow(
            new InsertionServiceEx());

      EasyMock.replay(storageDocumentService);

      InsertionRunnable insertionRunnable = new InsertionRunnable(0,
            new StorageDocument(), writer);

      insertionRunnable.run();
   }

   @Test
   public void testRunSuccess() throws InsertionServiceEx,
         InsertionIdGedExistantEx {

      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      StorageDocument aIntegrer = new StorageDocument();
      aIntegrer.setCreationDate(new Date());
      aIntegrer.setFilePath("/home");

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument);

      EasyMock.replay(storageDocumentService);

      InsertionRunnable insertionRunnable = new InsertionRunnable(0, aIntegrer,
            writer);
      insertionRunnable.run();

      Assert.assertNotNull("l'uuid du document doit être renseigné",
            insertionRunnable.getStorageDocument().getUuid());
   }

}
