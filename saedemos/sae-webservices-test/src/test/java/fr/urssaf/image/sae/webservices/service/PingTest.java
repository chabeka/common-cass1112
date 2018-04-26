package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class PingTest {

   @Autowired
   private PingService service;

   private static final Logger LOG = LoggerFactory.getLogger(PingTest.class);

   @Test
   public void ping_success() throws RemoteException {

      PingResponse response = service.ping();

      LOG.debug(response.getPingString());

      assertEquals("Test du ping", "Les services SAE sont en ligne", response
            .getPingString());
   }

}
