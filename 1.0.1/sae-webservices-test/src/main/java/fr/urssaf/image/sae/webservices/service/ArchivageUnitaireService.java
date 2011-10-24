package fr.urssaf.image.sae.webservices.service;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaire;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;

/**
 * Service client d'archivage unitaire du SAE
 * 
 * 
 */
@Service
public class ArchivageUnitaireService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ArchivageUnitaireService(SaeServiceStub service) {
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
   public final ArchivageUnitaireResponseType archivageUnitaire(URI urlEcde,
         Collection<Metadata> metadatas) throws RemoteException {

      ArchivageUnitaire request = RequestServiceFactory
            .createArchivageUnitaire(urlEcde, metadatas);

      return service.archivageUnitaire(request).getArchivageUnitaireResponse();
   }
}
