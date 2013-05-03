/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.services.CommonsServices;

/**
 * Classe permettant de tester le service de contrôle.
 * 
 */
public class SaeControleMetadataServiceTest extends CommonsServices {

   @Autowired
   SaeControleMetadataService service;

   @Test
   public void testStorageMetaObligatoire() {
      try {
         service.checkMetadataForStorage(null);
         Assert.fail(IllegalArgumentException.class.getName() + " attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
               .getMessage().contains("liste des métadonnées"));

      } catch (Exception exception) {
         Assert.fail(IllegalArgumentException.class.getName() + " attendue");
      }
   }

   @Test
   public void testCaptureMetaObligatoire() {
      try {
         service.checkMetadataForStorage(null);
         Assert.fail(IllegalArgumentException.class.getName() + " attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
               .getMessage().contains("liste des métadonnées"));

      } catch (Exception exception) {
         Assert.fail(IllegalArgumentException.class.getName() + " attendue");
      }
   }

   @Test
   public void testUntypedMetaObligatoire() {
      try {
         service.checkUntypedMetadatas(null);
         Assert.fail(IllegalArgumentException.class.getName() + " attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit etre correct", exception
               .getMessage().contains("liste des métadonnées"));

      } catch (Exception exception) {
         Assert.fail(IllegalArgumentException.class.getName() + " attendue");
      }
   }

}
