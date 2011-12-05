package fr.urssaf.image.sae.webservices.service;

import java.net.URI;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;

/**
 * Service client pour l'archivage de masse du SAE.
 * 
 * 
 */
@Service
public class ArchivageMasseService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ArchivageMasseService(SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service de l'archivage de masse du SAE
    * 
    * @param urlSommaireEcde
    *           URL du sommaire dans l'ECDE
    * @return réponse de l'archivage de masse
    * @throws RemoteException
    *            levée par le web service
    */
   public final ArchivageMasseResponseType archivageMasse(URI urlSommaireEcde)
         throws RemoteException {

      ArchivageMasse request = RequestServiceFactory
            .createArchivageMasse(urlSommaireEcde);

      return service.archivageMasse(request).getArchivageMasseResponse();
   }
}
