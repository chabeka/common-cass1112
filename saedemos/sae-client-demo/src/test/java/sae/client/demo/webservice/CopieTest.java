package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
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
      String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117193";

      // construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération copie,
      // avec les objets modèle générés par Axis2.
      Copie paramsEntree = Axis2ObjectFactory.contruitParamsEntreeCopie(
            idArchive, null);

      //appel de l'opération Copie
      CopieResponse reponse = saeService.copie(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueCopie = reponse.getCopieResponse().getIdGed().toString();
      System.out.println(idUniqueCopie);
   }

   @Test
   public void copie_failure() {

      // Identifiant unique d'archivage de l'archive que l'on veut copier
      // On part ici du principe que le document n'existe pas et qu'une erreur
      // nous soit renvoyé
      String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117192";

      // construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération copie,
      // avec les objets modèle générés par Axis2.
      Copie paramsEntree = Axis2ObjectFactory.contruitParamsEntreeCopie(
            idArchive, null);
      
      try {
         
       //appel de l'opération Copie
         saeService.copie(paramsEntree);
         
      // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      } catch (AxisFault fault){
         
      // sysout
         TestUtils.sysoutAxisFault(fault);
         
         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "urn:sae:faultcodes",
               "sae",
               "ArchiveNonTrouvee",
               "Il n'existe aucun document pour l'identifiant d'archivage '991d7027-6b1b-43a3-b0a3-b22cdf117192'");
         
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }     

   }

}
