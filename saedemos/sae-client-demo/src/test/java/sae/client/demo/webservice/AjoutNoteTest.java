package sae.client.demo.webservice;

import java.rmi.RemoteException;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.AjoutNote;

public class AjoutNoteTest {

   /**
    * Exemple de consommation de l'opération ajoutNote du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    *
    * @throws RemoteException
    */
   @Test
   public void ajoutNote_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut modifier
      // On part ici du principe que le document existe
      final String idArchive = "271669AA-07ED-4385-828F-F2F5DDD3A872";

      // La note à ajouter à un document (Pour info, une note peut être ajouter
      // dès l'archivage d'un document, en utilisant la métadonnée Note)
      final String contenuNote = "Contenu de la note";

      // Construction du paramètre d'entrée de l'opération ajoutNote,
      // avec les objets modèle générés par Axis2.
      final AjoutNote paramsEntree = Axis2ObjectFactory
                                                       .contruitParamsEntreeAjoutNote(idArchive, contenuNote);

      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Appel du service web de modification
      saeService.ajoutNote(paramsEntree);

      // Trace
      System.out.println("Une note a été ajouté au document " + idArchive);

   }

}
