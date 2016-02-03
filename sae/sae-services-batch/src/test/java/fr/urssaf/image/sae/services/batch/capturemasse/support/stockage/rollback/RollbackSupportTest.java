/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml" })
public class RollbackSupportTest {

   @Autowired
   @Qualifier("storageDocumentService")
   @SuppressWarnings("PMD.LongVariable")
   private StorageDocumentService storageDocumentService;

   @Autowired
   private RollbackSupport support;

   @After
   public void end() {

      EasyMock.reset(storageDocumentService);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testIdentifiantObligatoire() throws DeletionServiceEx,
         InterruptionTraitementException {

      support.rollback(null);

   }

   @Test(expected = DeletionServiceEx.class)
   public void testFailureRollBack() throws DeletionServiceEx,
         InterruptionTraitementException {
      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));

      EasyMock.expectLastCall().andThrow(
            new DeletionServiceEx("erreur de suppression"));

      EasyMock.replay(storageDocumentService);

      support.rollback(UUID.randomUUID());
   }

   @Test
   public void testSuccessRollBack() throws DeletionServiceEx,
         InterruptionTraitementException {

      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));

      EasyMock.expectLastCall().anyTimes();

      EasyMock.replay(storageDocumentService);

      support.rollback(UUID.randomUUID());

   }
}
