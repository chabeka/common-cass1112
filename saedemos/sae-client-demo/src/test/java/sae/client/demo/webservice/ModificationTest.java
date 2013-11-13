package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Modification;

public class ModificationTest {

   
   /**
    * Exemple de consommation de l'opération modification du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException 
    */
   @Test
   public void modification_success() throws RemoteException {
      
      // Identifiant unique d'archivage de l'archive que l'on veut modifier
      // On part ici du principe que le document existe
      String idArchive = "5A06E1C2-048A-4E46-B7F2-9A93D48300AB";
      
      // Les métadonnées que l'on veut modifier
      // On renseigne une liste de paire clés/valeur
      // Les métadonnées qui ont une valeur vide seront supprimés du document
      // Celles qui ont une valeur renseignée seront soient ajoutées, 
      // soit modifiées.
      Map<String, String> metadonnees = new HashMap<String, String>();
      metadonnees.put("NumeroIntControle", "1234");
      metadonnees.put("Denomination", "Mr TOTO");
      metadonnees.put("PseudoSiret", "");
      metadonnees.put("Siren", "");
      metadonnees.put("Siret", "1186768767");
      
      // Construction du paramètre d'entrée de l'opération modification, 
      //  avec les objets modèle générés par Axis2.
      Modification paramsEntree = Axis2ObjectFactory.contruitParamsEntreeModification(
            idArchive, metadonnees);
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Appel du service web de modification
      saeService.modification(paramsEntree);
      
      // Trace
      System.out.println("Les métadonnées du document " + idArchive + " ont été modifiées");
            
   }
   
   
   
   
   /**
    * Exemple de consommation de l'opération modification du service web SaeService<br>
    * <br>
    * Cas avec erreur : On demande un identifiant unique d'archivage qui n'existe pas dans le SAE<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:ModificationArchiveNonTrouvee</li>
    *    <li>Message : Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000</li>
    * </ul>
    */
   @Test
   public void consultation_failure_archiveNonTrouvee() {
      
      // Identifiant unique d'archivage inexistant
      String idArchive = "00000000-0000-0000-0000-000000000000";
      
      // Les métadonnées que l'on veut modifier
      Map<String, String> metadonnees = new HashMap<String, String>();
      metadonnees.put("NumeroIntControle", "1234");
      metadonnees.put("Denomination", "Mr TOTO");
      metadonnees.put("PseudoSiret", "");
      metadonnees.put("Siren", "");
      metadonnees.put("Siret", "1186768767");
      
      // Construction du paramètre d'entrée de l'opération modification, 
      //  avec les objets modèle générés par Axis2.
      Modification paramsEntree = Axis2ObjectFactory.contruitParamsEntreeModification(
            idArchive, metadonnees);
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Appel de l'opération modification
      try {
         
         // Appel de l'opération modification
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.modification(paramsEntree);
         
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
               "ModificationArchiveNonTrouvee",
               "Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000");
       
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
   
   /**
    * Exemple de consommation de l'opération modification du service web SaeService<br>
    * <br>
    * Cas avec erreur : On essaye de modifier une métadonnée qui n'est pas modifiable (ApplicationProductrice)<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:ModificationMetadonneeNonModifiable</li>
    *    <li>Message : La ou les métadonnées suivantes ne sont pas modifiables : ApplicationProductrice</li>
    * </ul>
    */
   @Test
   public void modification_failure_metadonneeNonModifiable() {
      
      // Identifiant unique d'archivage de l'archive que l'on veut modifier
      // On part ici du principe que le document existe
      String idArchive = "5A06E1C2-048A-4E46-B7F2-9A93D48300AB";
      
      // Les métadonnées que l'on veut modifier
      Map<String, String> metadonnees = new HashMap<String, String>();
      metadonnees.put("ApplicationProductrice", "Toto");
      
      // Construction du paramètre d'entrée de l'opération modification, 
      //  avec les objets modèle générés par Axis2.
      Modification paramsEntree = Axis2ObjectFactory.contruitParamsEntreeModification(
            idArchive, metadonnees);
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Appel de l'opération modification
      try {
         
         // Appel de l'opération modification
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.modification(paramsEntree);
         
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
               "ModificationMetadonneeNonModifiable",
               "La ou les métadonnées suivantes ne sont pas modifiables : ApplicationProductrice");
       
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
   
}
