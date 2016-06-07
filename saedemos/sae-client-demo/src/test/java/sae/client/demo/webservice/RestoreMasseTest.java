package sae.client.demo.webservice;

import java.rmi.RemoteException;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.RestoreMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.RestoreMasseResponse;

public class RestoreMasseTest {

   
   /**
    * Exemple de consommation de l'opération restoreMasse du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void restoreMasse_success() throws RemoteException {
      
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération restoreMasse, 
      //  avec les objets modèle générés par Axis2.
      String idTraitementSuppression = "6bf1ee10-0ae3-11e6-a85f-005056bf4357";
      RestoreMasse paramsEntree = Axis2ObjectFactory.contruitParamsEntreeRestoreMasse(
            idTraitementSuppression);
      
      // Appel de l'opération restoreMasse
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      RestoreMasseResponse reponse = saeService.restoreMasse(paramsEntree);
      String idTraitementSae = reponse.getRestoreMasseResponse().getUuid();
      
      // sysout
      System.out.println("La demande de prise en compte de la suppression de masse a été envoyée");
      System.out.println("ID Traitement de masse suppression : " + idTraitementSuppression);
      System.out.println("Identifiant unique du traitement de masse affecté par le SAE : " + idTraitementSae);
      
   }
   
   
   
}
