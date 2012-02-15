package sae.client.demo.webservice;

import static junit.framework.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.Test;

import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.PingRequest;


public class PingTest {

   
   /**
    * Exemple de consommation de l'opération Ping du service web SaeService 
    * @throws RemoteException 
    */
   @Test
   public void ping() throws RemoteException {
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubSansAuthentification();
      
      // Appel de l'opération Ping
      String reponsePing = saeService.ping(new PingRequest()).getPingString();

      // sysout
      System.out.println(reponsePing);
      
      // Assertion JUnit
      assertEquals(
            "La réponse de l'opération Ping est incorrecte",
            "Les services SAE sont en ligne",
            reponsePing);
      
   }
   
}
