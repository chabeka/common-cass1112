package fr.urssaf.image.sae.client.vi.securite.signature;

import java.util.Arrays;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.junit.Test;

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.beans.ViMessageContext;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import junit.framework.Assert;

public class CodeSecuTest {

  @Test
  public void genereTest() {
    final VIHandler handler = new VIHandler();
    final MessageContext context = new MessageContext();
    Assert.assertNotNull("le message retourné doit être non null",
                         handler
                                .genererEnTeteWsse(context));

  }

  @Test
  public void genereTestAttributes() {
    final KeyStoreInterface keystore = DefaultKeystore.getInstance();
    final List<String> pagms = Arrays.asList("PAGM_TOUTES_ACTIONS");
    final String issuer = "CS_DEV_TOUTES_ACTIONS";
    final String login = "NON_RENSEIGNE";
    final VIHandler handler = new VIHandler(keystore, pagms, issuer, login);
    final MessageContext context = new MessageContext();
    Assert.assertNotNull("le message retourné doit être non null",
                         handler
                                .genererEnTeteWsse(context));

  }

  @Test
  public void genereTestFileParameters() throws AxisFault {

    final VIHandler handler = new VIHandler();

    Assert.assertNotNull("le message retourné doit être non null",
                         handler
                                .genererEnTeteWsse(new ViMessageContext()));

  }
}
