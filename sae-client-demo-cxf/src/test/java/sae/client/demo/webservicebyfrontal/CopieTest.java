package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;

import sae.client.demo.util.ArchivageUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.CopieRequestType;
import sae.client.demo.webservice.modele.CopieResponseType;
import sae.client.demo.webservice.modele.ListeMetadonneeType;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class CopieTest {

   /**
    * Exemple de consommation de l'opération copie du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    *
    * @throws IOException
    */
   @Test
   public void copie_success() throws IOException {

      // Identifiant unique d'archivage de l'archive que l'on veut copier
      // On part ici du principe que le document existe, un autre test permet
      // d'illuster le cas où le document n'existe pas
      // final String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117193";
      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      final String idArchive = ArchivageUtils.archivageUnitairePJ();

      // construction du Stub
      final SaeService saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération copie,
      // avec les objets modèle générés par Axis2.
      final CopieRequestType request = new CopieRequestType();
      request.setIdGed(idArchive);
      final ListeMetadonneeType metadonneeTypeListe = new ListeMetadonneeType();
      request.setMetadonnees(metadonneeTypeListe);

      // appel de l'opération Copie
      final SaeServicePortType port = saeService.getSaeServicePort();

      final CopieResponseType reponse = port.copie(request);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueCopie = reponse.getIdGed();
      System.out.println(idUniqueCopie);
   }

   @Test
   public void copie_failure() {

      final Map<String, String> metadonnees = new HashMap<String, String>();
      // Identifiant unique d'archivage de l'archive que l'on veut copier
      // On part ici du principe que le document n'existe pas et qu'une erreur
      // nous soit renvoyé
      // final String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117192";

      try {
         final String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117192";

         // construction du Stub
         final SaeService saeService = StubFactory.createStubAvecAuthentification();

         // Construction du paramètre d'entrée de l'opération copie,
         // avec les objets modèle générés par Axis2.
         final CopieRequestType request = new CopieRequestType();
         request.setIdGed(idArchive);
         final ListeMetadonneeType metadonneeTypeListe = new ListeMetadonneeType();
         request.setMetadonnees(metadonneeTypeListe);

         // appel de l'opération Copie
         final SaeServicePortType port = saeService.getSaeServicePort();

         final CopieResponseType reponse = port.copie(request);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      }
      catch (final SOAPFaultException fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:frontal:faultcodes",
                                   "ns1",
                                   "ArchiveNonTrouvee",
                                   "L'archive 991d7027-6b1b-43a3-b0a3-b22cdf117192 n'a été trouvée dans aucune des instances de la GED.");

      }
      catch (final IOException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}
