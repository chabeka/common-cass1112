package fr.urssaf.image.sae.storage.dfce.messages;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;

/**
 * Test de récupération des messages.
 * 
 */
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
