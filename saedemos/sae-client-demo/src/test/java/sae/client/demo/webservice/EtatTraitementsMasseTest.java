package sae.client.demo.webservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.EtatTraitementsMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.EtatTraitementsMasseResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.TraitementMasseType;

public class EtatTraitementsMasseTest {

   /**
    * Exemple de consommation de l'opération etatTraitementsMasse du service web
    * SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void etatTraitementsMasse_success() throws RemoteException {

      // Construction du Stub (sans authentification)
      SaeServiceStub saeService = StubFactory.createStubSansAuthentification();

      // Construction du paramètre d'entrée de l'opération etatTraitementsMasse,
      // avec les objets modèle générés par Axis2.
      List<String> listeUuid = new ArrayList<String>();
      listeUuid.add("acf4e750-2898-11e6-942b-f8b156a864b3");
      listeUuid.add("90c7404c-2bb7-11e6-b67b-9e71128cae77");

      EtatTraitementsMasse paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeEtatTraitementsMasse(listeUuid);

      // Appel de l'opération etatTraitement
      // => en attendu, l'identifiant unique de traitement de masse affecté par
      // le SAE
      EtatTraitementsMasseResponse reponse = saeService
            .etatTraitementsMasse(paramsEntree);

      TraitementMasseType[] listeJobs = reponse
            .getEtatTraitementsMasseResponse().getTraitementsMasse()
            .getTraitementMasse();

      // sysout
      System.out.println("Détails des traitements demandés");

      for (TraitementMasseType job : listeJobs) {
         System.out.println("Id Job : " + job.getIdJob());
         System.out.println("Etat : " + job.getEtat());
         System.out.println("Type : " + job.getType());
         System.out.println("Date de création : " + job.getDateCreation());
         System.out.println("Date de réservation : " + job.getDateReservation());
         System.out.println("Date de début : " + job.getDateDebut());
         System.out.println("Date de fin : " + job.getDateFin());
         System.out.println("Nombre de documents : " + job.getNombreDocuments());
         System.out.println("Message : " + job.getMessage());
         
      }

   }

}
