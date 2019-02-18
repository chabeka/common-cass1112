package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.junit.Test;

import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Deblocage;
import sae.client.demo.webservice.modele.SaeServiceStub.DeblocageResponse;

/**
 * Classe de test du ws de déblocage de traitement de masse en erreur.
 * 
 */
public class DeblocageServiceTest {

   /**
    * Exemple de consommation de l'opération deblocage du service web SaeService<br>
    * <br>
    * Cas en erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void testDeblocageTraitementInexistant_success()
         throws RemoteException {

      // Pré-requis pour le déblocage :
      // RAS: l'uuid n'existe pas en base afin de vérifier qu'une soapFault est
      // générée dans ce cas
      String uuidJob = "af7a6dd0-54f0-11e7-9375-f8b156992ed7";

      // Construction du Stub
      SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération deblocage,
      // avec les objets modèle générés par Axis2.
      Deblocage paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeDeblocage(uuidJob);

      // Appel de l'opération modificationMasse
      try {
         // Appel au service de deblocage
         DeblocageResponse reponse = saeService.deblocage(paramsEntree);
         System.out
               .println("La demande de prise en compte du deblocage a été envoyée");

         String idTraitementSae = reponse.getDeblocageResponse().getUuid();
         System.out
               .println("La demande de prise en compte de déblocage a été envoyée pour le job "
                     + idTraitementSae);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {
         TestUtils.sysoutAxisFault(fault);
      } catch (RemoteException exception) {
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n"
               + exception);
      }

   }

   /**
    * Exemple de consommation de l'opération de deblocage du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void testDeblocageModificationMasse_success() throws RemoteException {

      // Pré-requis pour ce cas de déblocage :
      // Choisir un traitement de modification de masse à l'état
      // "Failure" existant pour vérifier son déblocage 
      String uuidJob = "aefe8ce0-5bdc-11e7-89df-f8b156992ed7";

      // Construction du Stub
      SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération deblocage,
      // avec les objets modèle générés par Axis2.
      Deblocage paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeDeblocage(uuidJob);

      // Appel de l'opération modificationMasse
      try {
         // Appel au service de deblocage
         DeblocageResponse reponse = saeService.deblocage(paramsEntree);
         System.out
               .println("La demande de prise en compte du deblocage a été envoyée");

         Assert.assertNotNull("La réponse au service de deblocage a été envoyée", reponse);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {
         TestUtils.sysoutAxisFault(fault);
      } catch (RemoteException exception) {
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n"
               + exception);
      }

   }

}
