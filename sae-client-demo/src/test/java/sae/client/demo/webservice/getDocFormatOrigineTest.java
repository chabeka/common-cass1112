package sae.client.demo.webservice;

import java.rmi.RemoteException;
import java.util.UUID;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.GetDocFormatOrigine;
import sae.client.demo.webservice.modele.SaeServiceStub.GetDocFormatOrigineResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;

public class getDocFormatOrigineTest {

   /**
    * Exemple de consommation de l'opération getDocFormatOrigine du service web
    * SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void getDocFormatOrigine_success() throws RemoteException {

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      UUID uuidDocParent = UUID
            .fromString("EA06783F-59A0-4B81-8524-1C945759565C");

      // Construction du paramètre d'entrée de l'opération getDocFormatOrigine,
      // avec les objets modèle générés par Axis2.
      GetDocFormatOrigine paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeGetDocFormatOrigine(uuidDocParent);

      // Appel de l'opération getDocFormatOrigine
      GetDocFormatOrigineResponse reponse = saeService
            .getDocFormatOrigine(paramsEntree);

      // Affichage des métadonnées de retour
      ListeMetadonneeType listeMeta = reponse.getGetDocFormatOrigineResponse()
            .getMetadonnees();

      for (MetadonneeType metadonneeType : listeMeta.getMetadonnee()) {
         System.out.println(metadonneeType.getCode() + " : " + metadonneeType.getValeur());
      }
     

   }

}
