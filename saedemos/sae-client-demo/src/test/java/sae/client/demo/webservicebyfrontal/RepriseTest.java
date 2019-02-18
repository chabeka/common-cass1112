package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import junit.framework.Assert;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Reprise;
import sae.client.demo.webservice.modele.SaeServiceStub.RepriseResponse;

public class RepriseTest {

   /**
    * Exemple de consommation de l'opération copie du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException
    */
   @Test
   public void reprise_success() throws RemoteException {

      // Identifiant uniquedu job que l'on veut relancer en mode "Reprise".
      final String idJobAReprendre = "b694b060-60be-11e7-af04-005056bf7e9a";

      // construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération reprise,
      // avec les objets modèle générés par Axis2.
      final Reprise paramsEntree = Axis2ObjectFactory
                                                     .contruitParamsEntreeReprise(idJobAReprendre);

      // appel de l'opération reprise
      final RepriseResponse reponse = saeService.reprise(paramsEntree);

      // Affichage de l'identifiant unique du job de reprise créé
      final String idJobReprise = reponse.getRepriseResponse().getUuid().toString();
      Assert.assertNotNull(idJobReprise);
      System.out.println(idJobReprise);
   }

   @Test
   public void reprise_failure() {

      // Identifiant uniquedu job que l'on veut relancer en mode "Reprise".
      final String idJobAReprendre = "991d7027-6b1b-43a3-b0a3-b22cdf117192";

      // construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération reprise,
      // avec les objets modèle générés par Axis2.
      final Reprise paramsEntree = Axis2ObjectFactory
                                                     .contruitParamsEntreeReprise(idJobAReprendre);

      try {

         // appel de l'opération Copie
         saeService.reprise(paramsEntree);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      }
      catch (final AxisFault fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:frontal:faultcodes",
                                   "TraitementMasseNonTrouve",
                                   "Le traitement de masse 991d7027-6b1b-43a3-b0a3-b22cdf117192 n'a été trouvé dans aucunes des instances de la GED.");

      }
      catch (final RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}
