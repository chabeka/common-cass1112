package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RecuperationMetadonnees;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RecuperationMetadonneesResponse;

/**
 * Service client de récupération des métadonnées du SAE
 * 
 * 
 */
@Service
public class RecuperationMetadonneesService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public RecuperationMetadonneesService(@Qualifier("secureStub") SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service de récupération des métadonnées du SAE
    * 
    * @return message contenant la liste des métadonnées du SAE
    * @throws RemoteException
    *            levée par le service web
    */
   public final RecuperationMetadonneesResponse recuperationMetadonnees() throws RemoteException {

      RecuperationMetadonnees request = new RecuperationMetadonnees();

      return service.recuperationMetadonnees(request);
   }
}
