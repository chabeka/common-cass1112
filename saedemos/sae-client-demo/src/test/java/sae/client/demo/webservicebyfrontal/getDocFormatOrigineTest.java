package sae.client.demo.webservicebyfrontal;

import java.rmi.RemoteException;
import java.util.UUID;

import org.junit.Test;

import sae.client.demo.utils.ArchivageUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
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

      final String idArchie = ArchivageUtils.archivageUnitairePJ();
      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      final UUID uuidDocParent = UUID
                                     .fromString("75FD52D4-BD7B-4528-B14B-BCD2D1CF9A27");

      // Construction du paramètre d'entrée de l'opération getDocFormatOrigine,
      // avec les objets modèle générés par Axis2.
      final GetDocFormatOrigine paramsEntree = Axis2ObjectFactory
                                                                 .contruitParamsEntreeGetDocFormatOrigine(uuidDocParent);

      // Appel de l'opération getDocFormatOrigine
      final GetDocFormatOrigineResponse reponse = saeService
                                                            .getDocFormatOrigine(paramsEntree);

      // Affichage des métadonnées de retour
      final ListeMetadonneeType listeMeta = reponse.getGetDocFormatOrigineResponse()
                                                   .getMetadonnees();

      for (final MetadonneeType metadonneeType : listeMeta.getMetadonnee()) {
         System.out.println(metadonneeType.getCode() + " : " + metadonneeType.getValeur());
      }

   }

}
