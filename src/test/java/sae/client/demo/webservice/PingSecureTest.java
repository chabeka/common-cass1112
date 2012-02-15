package sae.client.demo.webservice;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.PingSecureRequest;


public class PingSecureTest {

   
   /**
    * Exemple de consommation de l'opération PingSecure du service web SaeService<br>
    * <br>
    * Cas normal (réussite)
    * 
    * @throws RemoteException 
    */
   @Test
   public void pingSecure_success() throws RemoteException {
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Appel de l'opération PingSecure
      String reponsePingSecure = saeService.pingSecure(new PingSecureRequest()).getPingString();

      // sysout
      System.out.println(reponsePingSecure);
      
      // Assertion JUnit
      assertEquals(
            "La réponse de l'opération Ping est incorrecte",
            "Les services du SAE sécurisés par authentification sont en ligne",
            reponsePingSecure);
      
   }
   
   
   /**
    * Exemple de consommation de l'opération PingSecure du service web SaeService<br>
    * <br>
    * Cas avec erreur : le Vecteur d'Identification est omis<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : wsse:SecurityTokenUnavailable</li>
    *    <li>Message : La référence au jeton de sécurité est introuvable</li>
    * </ul>
    * 
    * @throws RemoteException 
    */
   @Test
   public void pingSecure_failure() {
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubSansAuthentification();
      
      // Appel de l'opération PingSecure
      try {
         
         // Appel de l'opération PingSecure
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.pingSecure(new PingSecureRequest()).getPingString();
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      } catch (AxisFault fault) {
         
         // sysout
         TestUtils.sysoutAxisFault(fault);
         
         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
               "wsse",
               "SecurityTokenUnavailable",
               "La référence au jeton de sécurité est introuvable");
         
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }

   }
   
}
