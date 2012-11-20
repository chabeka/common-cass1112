package fr.urssaf.image.sae.webservices.service;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJ;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;

/**
 * Service client d'archivage unitaire du SAE
 * 
 * 
 */
@Service
public class ArchivageUnitairePJService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ArchivageUnitairePJService(
         @Qualifier("secureStub") SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service d'archivage unitaire du SAE
    * 
    * @param urlEcde
    *           URL ECDE du fichier à archiver
    * @param metadatas
    *           liste des métadonnées pour l'archive
    * @return réponse de l'archivage unitaire
    * @throws RemoteException
    *            levée par le web service
    */
   public final ArchivageUnitairePJResponseType archivageUnitairePJ(
         URI urlEcde, Collection<Metadata> metadatas) throws RemoteException {

      ArchivageUnitairePJ request = RequestServiceFactory
            .createArchivagePJUnitaire(urlEcde, metadatas);

      return service.archivageUnitairePJ(request)
            .getArchivageUnitairePJResponse();
   }

   /**
    * appel du service d'archivage unitaire du SAE
    * 
    * @param fileName
    *           nom du fichier à archiver
    * @param contenu
    *           contenu du fichier à archiver
    * @param metadatas
    *           liste des métadonnées pour l'archive
    * @return réponse de l'archivage unitaire
    * @throws RemoteException
    *            levée par le web service
    */
   public final ArchivageUnitairePJResponseType archivageUnitairePJ(
         String fileName, byte[] contenu, Collection<Metadata> metadatas)
         throws RemoteException {

      ArchivageUnitairePJ request = RequestServiceFactory
            .createArchivagePJUnitaire(fileName, contenu, metadatas);

      return service.archivageUnitairePJ(request)
            .getArchivageUnitairePJResponse();
   }

   /**
    * appel du service d'archivage unitaire du SAE
    * 
    * @param fileName
    *           nom du fichier à archiver
    * @param contenu
    *           contenu du fichier à archiver
    * @param metadatas
    *           liste des métadonnées pour l'archive
    * @return l'UUID du document archivé
    * @throws RemoteException
    *            levée par le web service
    */
   public UUID archivageUnitairePJavecRetourUUID(String fileName,
         byte[] contenu, Collection<Metadata> metadatas) throws RemoteException {

      ArchivageUnitairePJ request = RequestServiceFactory
            .createArchivagePJUnitaire(fileName, contenu, metadatas);

      ArchivageUnitairePJResponseType response = service.archivageUnitairePJ(
            request).getArchivageUnitairePJResponse();

      return UUID.fromString(response.getIdArchive().getUuidType());

   }

}
