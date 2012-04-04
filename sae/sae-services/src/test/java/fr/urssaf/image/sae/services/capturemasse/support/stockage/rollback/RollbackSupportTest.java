/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback;

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

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-test.xml",
      "/applicationContext-sae-services-mock-storagedocument.xml" })
public class RollbackSupportTest {

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Autowired
   private RollbackSupport support;

   @After
   public void end() {

      EasyMock.reset(storageDocumentService);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testIdentifiantObligatoire()
         throws CaptureMasseSommaireFormatValidationException {

      support.rollback(null);
      Assert.fail("sortie aspect attendue");

   }

   @Test(expected = CaptureMasseRuntimeException.class)
   public void testFailureRollBack() throws DeletionServiceEx {
      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));

      EasyMock.expectLastCall().andThrow(
            new DeletionServiceEx("erreur de suppression"));

      EasyMock.replay(storageDocumentService);

      support.rollback(UUID.randomUUID());
   }

   @Test
   public void testSuccessRollBack() {

      try {
         storageDocumentService.deleteStorageDocument(EasyMock
               .anyObject(UUID.class));

         EasyMock.expectLastCall().anyTimes();

         EasyMock.replay(storageDocumentService);

         support.rollback(UUID.randomUUID());

      } catch (DeletionServiceEx e) {
         Assert.fail("attente d'un succès");
      }
   }
}
