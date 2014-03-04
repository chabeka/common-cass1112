package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Modification;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ModificationResponse;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;

/**
 * Service client de modification des métadonnées du SAE
 * 
 * 
 */
@Service
public class ModificationService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ModificationService(@Qualifier("secureStub") SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service de modification des métadonnées du SAE
    * 
    * @param idArchive
    *           identifiant du document du SAe
    * @param metadatas
    *           liste des métadonnées pour l'archive
    * @return message retour du service de modification
    * @throws RemoteException
    *            levée par le service web
    */
   public final ModificationResponse modification(String idArchive,
         Collection<Metadata> metadatas) throws RemoteException {

      Modification request = RequestServiceFactory.createModification(
            idArchive, metadatas);

      return service.modification(request);
   }
}
