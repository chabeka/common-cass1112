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

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class PingSecureTest {

   @Autowired
   private PingSecureService service;

   private static final Logger LOG = LoggerFactory.getLogger(PingSecureTest.class);


   @Test
   public void pingSecure_success() throws RemoteException {

      PingSecureResponse response = service.pingSecure();

      LOG.debug(response.getPingString());

      assertEquals("Test du ping securisé",
            "Les services du SAE sécurisés par authentification sont en ligne",
            response.getPingString());

   }
   
}
