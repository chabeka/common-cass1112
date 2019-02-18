package sae.client.demo.webservicebyfrontal;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;

import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.PingSecureRequest;
import sae.client.demo.webservice.modele.PingSecureResponse;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class PingSecureTest {

   /**
    * Exemple de consommation de l'opération PingSecure du service web SaeService<br>
    * <br>
    * Cas normal (réussite)
    *
    * @throws IOException
    */
   @Test
   public void pingSecure_success() throws IOException {

      // construction du Stub
      final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Appel de l'opération PingSecure
      final SaeServicePortType port = saeService.getSaeServicePort();
      final PingSecureResponse reponsePingSecure = port.pingSecure(new PingSecureRequest());

      // sysout
      System.out.println(reponsePingSecure);

      // Assertion JUnit
      assertEquals(
                   "La réponse de l'opération Ping est incorrecte",
                   "Les services du SAE sécurisés par authentification sont en ligne",
                   reponsePingSecure.getPingString());

   }

   /**
    * Exemple de consommation de l'opération PingSecure du service web SaeService<br>
    * <br>
    * Cas avec erreur : le Vecteur d'Identification est omis<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    * <li>Code : wsse:SecurityTokenUnavailable</li>
    * <li>Message : La référence au jeton de sécurité est introuvable</li>
    * </ul>
    *
    * @throws RemoteException
    */
   @Test
   public void pingSecure_failure() {

      // Appel de l'opération PingSecure
      try {

         // Construction du Stub
         final SaeService saeService = SaeServiceStubFactory.createStubSansAuthentification();
         // Appel de l'opération PingSecure
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         final SaeServicePortType port = saeService.getSaeServicePort();
         final String msg = port.pingSecure(new PingSecureRequest()).getPingString();

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      }
      catch (final SOAPFaultException fault) {

         // sysout
         TestUtils.sysoutSoapFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:frontal:faultcodes",
                                   "ns1",
                                   "InvalidPAGM",
                                   "Le PAGM est invalide");

      }
      catch (final IOException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}
