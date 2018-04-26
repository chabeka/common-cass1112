package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Suppression;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.SuppressionResponse;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;

/**
 * Service client de suppression d'un document du SAE
 * 
 * 
 */
@Service
public class SuppressionService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public SuppressionService(@Qualifier("secureStub") SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service de modification des métadonnées du SAE
    * 
    * @param idArchive
    *           identifiant du document du SAe
    * @return message retour du service de modification
    * @throws RemoteException
    *            levée par le service web
    */
   public final SuppressionResponse suppression(String idArchive)
         throws RemoteException {

      Suppression request = RequestServiceFactory.createSuppression(idArchive);

      return service.suppression(request);
   }
}
