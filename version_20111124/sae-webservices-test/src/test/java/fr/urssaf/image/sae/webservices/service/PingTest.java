package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingResponse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class PingTest {

   @Autowired
   private PingService service;

   private static final Logger LOG = Logger.getLogger(PingTest.class);

   @Test
   public void ping_success() throws RemoteException {

      PingResponse response = service.ping();

      LOG.debug(response.getPingString());

      assertEquals("Test du ping", "Les services SAE sont en ligne", response
            .getPingString());
   }

   @Test
   public void pingSecure_success() throws RemoteException {

      AuthenticateUtils.authenticate("ROLE_TOUS");

      PingSecureResponse response = service.pingSecure();

      LOG.debug(response.getPingString());

      assertEquals("Test du ping securisé",
            "Les services du SAE sécurisés par authentification sont en ligne",
            response.getPingString());

   }
}
