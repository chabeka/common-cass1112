package sae.client.demo.webservicebyfrontal;

import java.io.IOException;

import org.junit.Test;

import sae.client.demo.util.ArchivageUtils;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.CopieRequestType;
import sae.client.demo.webservice.modele.CopieResponseType;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class CopieTest {

   /**
    * Exemple de consommation de l'opération copie du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    *
    * @throws IOException
    */
   @Test
   public void copie_success() throws IOException {

      // Identifiant unique d'archivage de l'archive que l'on veut copier
      // On part ici du principe que le document existe, un autre test permet
      // d'illuster le cas où le document n'existe pas
      // final String idArchive = "991d7027-6b1b-43a3-b0a3-b22cdf117193";
      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      final String idArchive = ArchivageUtils.archivageUnitairePJ();

      // construction du Stub
      final SaeService saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération copie,
      // avec les objets modèle générés par Axis2.
      final CopieRequestType request = new CopieRequestType();
      request.setIdGed(idArchive);

      // appel de l'opération Copie
      final SaeServicePortType port = saeService.getSaeServicePort();

      final CopieResponseType reponse = port.copie(request);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueCopie = reponse.getIdGed();
      System.out.println(idUniqueCopie);
   }

}
