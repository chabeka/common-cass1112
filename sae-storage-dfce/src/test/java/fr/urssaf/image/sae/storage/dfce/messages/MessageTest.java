package fr.urssaf.image.sae.storage.dfce.messages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import org.junit.Assert;

/**
 * Test de récupération des messages.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-storage-dfce-test.xml"})
public class MessageTest extends CommonsServices {
   /**
    * Test la récupération du message à partir de la clé.
    */
   @Test
   public final void getMessageFromFile() {
      Assert.assertNotNull(StorageMessageHandler
            .getMessage("insertion.document.required"));
   }
}
