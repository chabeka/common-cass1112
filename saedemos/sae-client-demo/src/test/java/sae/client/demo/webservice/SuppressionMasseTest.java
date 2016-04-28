package sae.client.demo.webservice;

import java.rmi.RemoteException;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasseResponse;

public class SuppressionMasseTest {

   
   /**
    * Exemple de consommation de l'opération suppressionMasse du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void suppressioneMasse_success() throws RemoteException {
      
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération suppressionMasse, 
      //  avec les objets modèle générés par Axis2.
      String requete = "requete";
      SuppressionMasse paramsEntree = Axis2ObjectFactory.contruitParamsEntreeSuppressionMasse(
            requete);
      
      // Appel de l'opération archivageMasse
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      SuppressionMasseResponse reponse = saeService.suppressionMasse(paramsEntree);
      String idTraitementSae = reponse.getSuppressionMasseResponse().getUuid();
      
      // sysout
      System.out.println("La demande de prise en compte de la suppression de masse a été envoyée");
      System.out.println("Requete : " + requete);
      System.out.println("Identifiant unique du traitement de masse affecté par le SAE : " + idTraitementSae);
      
   }
   
   
   
}
