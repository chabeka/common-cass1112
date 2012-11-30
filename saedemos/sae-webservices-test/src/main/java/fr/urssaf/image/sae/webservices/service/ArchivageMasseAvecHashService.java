package fr.urssaf.image.sae.webservices.service;

import java.net.URI;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseAvecHash;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseAvecHashResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;

/**
 * Service client pour l'archivage de masse du SAE.
 * 
 * 
 */
@Service
public class ArchivageMasseAvecHashService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ArchivageMasseAvecHashService(@Qualifier("secureStub") SaeServiceStub service) {
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
   public final ArchivageMasseAvecHashResponseType archivageMasse(URI urlSommaireEcde, String hash, String typeHash)
         throws RemoteException {

      ArchivageMasseAvecHash request = RequestServiceFactory.createArchivageMasseAvecHash(urlSommaireEcde, hash, typeHash);

      return service.archivageMasseAvecHash(request).getArchivageMasseAvecHashResponse();
   }
}
