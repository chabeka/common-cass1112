/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class EnrichissementStorageDocumentSupportTest {

   @Autowired
   private EnrichissementStorageDocumentSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testDocumentNull() {

      support.enrichirDocument(null, UUID.randomUUID().toString());

   }

   @Test(expected = IllegalArgumentException.class)
   public void testUuidNull() {

      support.enrichirDocument(new StorageDocument(), null);

   }

   @Test(expected = IllegalArgumentException.class)
   public void testUuidVide() {

      support.enrichirDocument(new StorageDocument(), "");

   }

   @Test(expected = IllegalArgumentException.class)
   public void testUuidBlanc() {

      support.enrichirDocument(new StorageDocument(), " ");

   }

   @Test
   public void testSuccess() {

      StorageDocument document = new StorageDocument();
      String uuid = UUID.randomUUID().toString();

      StorageDocument result = support.enrichirDocument(document, uuid);

      Assert.assertNotNull("la liste des metadonnées ne doit pas etre nulle",
            result.getMetadatas());

      boolean metaExist = false;
      int index = 0;
      while (!metaExist && index < result.getMetadatas().size()) {

         if ("iti".equalsIgnoreCase(result.getMetadatas().get(index)
               .getShortCode())) {
            metaExist = true;
         }

         index++;
      }

      Assert.assertTrue("la metadata iti doit exister", metaExist);

      Assert.assertNotNull("l'id de traitement doit etre renseigné", result
            .getProcessId());
   }

   @Test
   public void testEnrichirVirtualDocumentObligatoire() {

      try {
         support.enrichirVirtualDocument(null, null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("document virtuel"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

   @Test
   public void testEnrichirVirtualUuidObligatoire() {

      try {
         support.enrichirVirtualDocument(new VirtualStorageDocument(), null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("identifiant unique du process"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

}
