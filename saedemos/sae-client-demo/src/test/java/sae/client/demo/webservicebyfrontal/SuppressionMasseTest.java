package sae.client.demo.webservicebyfrontal;

import java.rmi.RemoteException;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasseResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasseV2;

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
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération suppressionMasse,
      // avec les objets modèle générés par Axis2.
      final String requete = "Denomination:MOD_07-modificationMasse-OK-5";
      final SuppressionMasseV2 params = Axis2ObjectFactory.contruitParamsEntreeSuppressionMasseV2(requete);
      // SuppressionMasse paramsEntree = Axis2ObjectFactory.contruitParamsEntreeSuppressionMasse(requete);

      // Appel de l'opération suppressionMasse
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      final SuppressionMasseResponse reponse = saeService.suppressionMasseV2(params);
      final String idTraitementSae = reponse.getSuppressionMasseResponse().getUuid();

      // sysout
      System.out.println("La demande de prise en compte de la suppression de masse a été envoyée");
      System.out.println("Requete : " + requete);
      System.out.println("Identifiant unique du traitement de masse affecté par le SAE : " + idTraitementSae);

   }

}
