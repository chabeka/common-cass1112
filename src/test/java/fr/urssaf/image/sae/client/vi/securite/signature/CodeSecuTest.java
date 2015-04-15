package fr.urssaf.image.sae.client.vi.securite.signature;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.junit.Test;

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.beans.ViMessageContext;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

public class CodeSecuTest {

   @Test
   public void genereTest() {
      VIHandler handler = new VIHandler();
      MessageContext context = new MessageContext();
      Assert.assertNotNull("le message retourné doit être non null", handler
            .genererEnTeteWsse(context));

   }

   @Test
   public void genereTestAttributes() {
      KeyStoreInterface keystore = DefaultKeystore.getInstance();
      List<String> pagms = Arrays.asList("ROLE_TOUS;FULL");
      String issuer = "PNR";
      String login = "LOGIN";
      VIHandler handler = new VIHandler(keystore, pagms, issuer, login);
      MessageContext context = new MessageContext();
      Assert.assertNotNull("le message retourné doit être non null", handler
            .genererEnTeteWsse(context));

   }

   @Test
   public void genereTestFileParameters() throws AxisFault {

      VIHandler handler = new VIHandler();

      Assert.assertNotNull("le message retourné doit être non null", handler
            .genererEnTeteWsse(new ViMessageContext()));

   }
}
