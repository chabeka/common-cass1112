/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.batch.StorageDocumentWriter;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class InsertionRunnableTest {

   @Test(expected = InsertionMasseRuntimeException.class)
   public void testRunRetourErreur() {

      StorageDocumentWriter writer = EasyMock
            .createMock(StorageDocumentWriter.class);

      try {
         EasyMock.expect(
               writer.insertStorageDocument(EasyMock
                     .anyObject(StorageDocument.class))).andThrow(
               new InsertionServiceEx());
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      // EasyMock.replay(writer);

      InsertionRunnable insertionRunnable = new InsertionRunnable(0,
            new StorageDocument(), writer);

      insertionRunnable.run();
   }
}
