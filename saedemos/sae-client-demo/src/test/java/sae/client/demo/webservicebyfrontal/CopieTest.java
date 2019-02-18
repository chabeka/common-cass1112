package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.utils.ArchivageUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Copie;
import sae.client.demo.webservice.modele.SaeServiceStub.CopieResponse;

public class CopieTest {

   /**
    * Exemple de consommation de l'opération copie du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    *
    * @throws RemoteException
    */
   @Test
   public void copie_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut copier
      // On part ici du principe que le document existe, un autre test permet
      // d'illuster le cas où le document n'existe pas
      // ArchivageUtils.archivageUnitairePJ()
      final String idArchive = ArchivageUtils.archivageUnitairePJ();

      // construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération copie,
      // avec les objets modèle générés par Axis2.
      final Copie paramsEntree = Axis2ObjectFactory.contruitParamsEntreeCopie(
                                                                              idArchive,
                                                                              new HashMap<String, String>());

      // appel de l'opération Copie
      final CopieResponse reponse = saeService.copie(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueCopie = reponse.getCopieResponse().getIdGed().toString();
      System.out.println(idUniqueCopie);
   }

   @Test
   public void copie_failure() {

      final Map<String, String> metadonnees = new HashMap<>();
      // Identifiant unique d'archivage de l'archive que l'on veut copier
      // On part ici du principe que le document n'existe pas et qu'une erreur
      // nous soit renvoyé
      final String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117192";

      // construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération copie,
      // avec les objets modèle générés par Axis2.
      final Copie paramsEntree = Axis2ObjectFactory.contruitParamsEntreeCopie(
                                                                              idArchive,
                                                                              metadonnees);

      try {

         // appel de l'opération Copie
         saeService.copie(paramsEntree);

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
                                   "ArchiveNonTrouvee",
                                   "L'archive 991d7027-6b1b-43a3-b0a3-b22cdf117192 n'a été trouvée dans aucune des instances de la GED.");

      }
      catch (final RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}
