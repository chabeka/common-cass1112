package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.junit.BeforeClass;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.utils.ArchivageUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.ArchivageUnitairePJTest;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Transfert;

public class TransfertTest {

   /**
    * Nom du fichier properties contenant l'URL du service web SAE
    */
   private static final String NOM_FICHIER_PROP = "sae-client-demo-frontal.properties";

   private final static Properties prop = new Properties();

   @BeforeClass
   public static void setUpBeforeClass() {

      try {
         prop.load(ResourceUtils.loadResource(new ArchivageUnitairePJTest(), NOM_FICHIER_PROP));
      }
      catch (final IOException e) {
         throw new DemoRuntimeException(e);
      }
   }

   /**
    * Exemple de consommation de l'opération transfert du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    *
    * @throws RemoteException
    */
   @Test
   public void transfert_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut supprimer
      // On part ici du principe que le document existe, un autre test permet
      // d'illuster le cas où le document n'existe pas
      // String idArchive = "C5DFE657-730F-4EA9-8DA0-DC18D0EDA38E";
      final String idArchive = ArchivageUtils.archivageUnitairePJ();

      // Construction du Stub
      final SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération transfert,
      // avec les objets modèle générés par Axis2.
      final Transfert paramsEntree = Axis2ObjectFactory.contruitParamsEntreeTransfert(idArchive);

      // Appel du service web de transfert
      saeService.transfert(paramsEntree);

      // Trace
      System.out.println("Le document " + idArchive + " a été transféré");

   }

   /**
    * Exemple de consommation de l'opération transfert du service web SaeService<br>
    * <br>
    * Cas avec erreur : On tente de supprimer un document qui n'existe pas dans le SAE<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    * <li>Code : sae:TransfertArchiveNonTrouvee</li>
    * <li>Message : Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000</li>
    * </ul>
    */
   @Test
   public void transfert_failure() {

      // Identifiant unique d'archivage inexistant
      final String idArchive = "00000000-0000-0000-0000-000000000000";

      // Construction du Stub
      final SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération transfert,
      // avec les objets modèle générés par Axis2.
      final Transfert paramsEntree = Axis2ObjectFactory.contruitParamsEntreeTransfert(idArchive);

      // Appel de l'opération transfert
      try {

         // Appel de l'opération transfert
         saeService.transfert(paramsEntree);

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
                                   "ns1",
                                   "ArchiveNonTrouvee",
                                   "L'archive 00000000-0000-0000-0000-000000000000 n'a été trouvée dans aucune des instances de la GED.");

      }
      catch (final RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}
