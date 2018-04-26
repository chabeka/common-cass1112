package sae.client.demo.webservice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeDispoType;
import sae.client.demo.webservice.modele.SaeServiceStub.RecuperationMetadonnees;
import sae.client.demo.webservice.modele.SaeServiceStub.RecuperationMetadonneesResponseType;

public class RecuperationMetadonneesTest {

   /**
    * Exemple de consommation de l'opération RecuperationMetadonnees du service
    * web SaeService<br>
    * <br>
    * Cas normal (réussite)
    * 
    * @throws RemoteException
    */
   @Test
   public void recuperationMetadonnees_success() throws RemoteException {

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Appel de l'opération RecuperationMetadonnees
      RecuperationMetadonneesResponseType response = saeService
            .recuperationMetadonnees(new RecuperationMetadonnees())
            .getRecuperationMetadonneesResponse();

      assertNotNull("La réponse ne doit pas être nulle", response);

      assertNotNull("La liste des métadonnées ne doit pas être nulle", response
            .getMetadonnees());

      assertNotNull("La liste des métadonnées ne doit pas être nulle", response
            .getMetadonnees().getMetadonnee());

      assertTrue("Le contenu de la réponse ne doit pas être nulle", response
            .getMetadonnees().getMetadonnee().length > 0);

      // Affichage de la liste des métadonnées
      afficheListeMetadonnees(response);

   }

   /**
    * Exemple de consommation de l'opération RecuperationMetadonnees du service
    * web SaeService<br>
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
   public void recuperationMetadonnees_failure() {

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubSansAuthentification();

      // Appel de l'opération RecuperationMetadonnees
      try {

         // Appel de l'opération RecuperationMetadonnees
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé
         // obtenir une SoapFault
         saeService.recuperationMetadonnees(new RecuperationMetadonnees());

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils
               .assertSoapFault(
                     fault,
                     "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
                     "wsse", "SecurityTokenUnavailable",
                     "La référence au jeton de sécurité est introuvable");

      } catch (RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n"
               + exception);

      }

   }

   private void afficheListeMetadonnees(
         RecuperationMetadonneesResponseType response) {

      for (MetadonneeDispoType metadonnee : response.getMetadonnees()
            .getMetadonnee()) {

         System.out.println(metadonnee.getCodeLong());
         System.out.println("Libellé: " + metadonnee.getLibelle());
         System.out.println("Description: " + metadonnee.getDescription());
         System.out.println("Format: " + metadonnee.getFormat());
         System.out.println("Formatage: " + metadonnee.getFormatage());
         System.out.println("Spécifiable à l'archivage: "
               + metadonnee.getSpecifiableArchivage());
         System.out.println("Obligatoire à l'archivage: "
               + metadonnee.getObligatoireArchivage());
         System.out.println("Taille max: " + metadonnee.getTailleMax());
         System.out.println("Utilisable dans une requête de recherche: "
               + metadonnee.getCritereRecherche());
         System.out.println("Est indexée: " + metadonnee.getIndexation());
         System.out.println("La valeur est modifiable (SAI): "
               + metadonnee.getModifiable());

         System.out.println();

      }

   }

}
