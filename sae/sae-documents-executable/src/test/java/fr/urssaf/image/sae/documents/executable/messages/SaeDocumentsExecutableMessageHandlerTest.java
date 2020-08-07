package fr.urssaf.image.sae.documents.executable.messages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.utils.messages.SaeDocumentsExecutableMessageHandler;
import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-documents-executable-test.xml"})
@SuppressWarnings("all")
public class SaeDocumentsExecutableMessageHandlerTest {

   @Test
   public void getMessageTest() throws Exception {
      final String value = SaeDocumentsExecutableMessageHandler.getMessage("erreur.param.obligatoire.null", "toto");
      System.out.println(value);
      Assert.assertEquals("La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : toto.", value);
   }
}
