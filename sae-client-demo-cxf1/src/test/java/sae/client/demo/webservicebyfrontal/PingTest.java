package sae.client.demo.webservicebyfrontal;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.PingRequest;
import sae.client.demo.webservice.modele.PingResponse;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class PingTest {

   /**
    * Exemple de consommation de l'opération Ping du service web SaeService
    *
    * @throws IOException
    */
   @Test
   public void ping() throws IOException {

      // construction du Stub
      final SaeService saeService = StubFactory.createStubSansAuthentification();

      // Appel de l'opération Ping

      final SaeServicePortType port = saeService.getSaeServicePort();
      final PingResponse reponsePing = port.ping(new PingRequest());
      // sysout
      System.out.println(reponsePing);

      // Assertion JUnit
      assertEquals(
                   "La réponse de l'opération Ping est incorrecte",
                   "Les services frontaux sont en ligne",
                   reponsePing.getPingString());

   }

}
