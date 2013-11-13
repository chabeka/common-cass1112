package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Suppression;

public class SuppressionTest {

   
   /**
    * Exemple de consommation de l'opération suppression du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException 
    */
   @Test
   public void suppression_success() throws RemoteException {
      
      // Identifiant unique d'archivage de l'archive que l'on veut supprimer
      // On part ici du principe que le document existe, un autre test permet
      // d'illuster le cas où le document n'existe pas
      String idArchive = "C5DFE657-730F-4EA9-8DA0-DC18D0EDA38E";
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération suppression, 
      //  avec les objets modèle générés par Axis2.
      Suppression paramsEntree = Axis2ObjectFactory.contruitParamsEntreeSuppression(idArchive);
      
      // Appel du service web de suppression
      saeService.suppression(paramsEntree);
      
      // Trace
      System.out.println("Le document " + idArchive + " a été supprimé");
      
   }
   
   
   /**
    * Exemple de consommation de l'opération suppression du service web SaeService<br>
    * <br>
    * Cas avec erreur : On tente de supprimer un document qui n'existe pas dans le SAE<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:SuppressionArchiveNonTrouvee</li>
    *    <li>Message : Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000</li>
    * </ul>
    */
   @Test
   public void suppression_failure() {
      
      // Identifiant unique d'archivage inexistant
      String idArchive = "00000000-0000-0000-0000-000000000000";
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération suppression, 
      //  avec les objets modèle générés par Axis2.
      Suppression paramsEntree = Axis2ObjectFactory.contruitParamsEntreeSuppression(idArchive);
      
      // Appel de l'opération suppression
      try {
         
         // Appel de l'opération suppression
         saeService.suppression(paramsEntree);
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      } catch (AxisFault fault) {
         
         // sysout
         TestUtils.sysoutAxisFault(fault);
         
         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "urn:sae:faultcodes",
               "sae",
               "SuppressionArchiveNonTrouvee",
               "Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000");
       
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
}
