package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureRequest;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse;

/**
 * Service client du ping du SAE
 * 
 * 
 */
@Service
public class PingSecureService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public PingSecureService(@Qualifier("secureStub") SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service ping sécurisé du SAE
    * 
    * @return message du ping sécurisé
    * @throws RemoteException
    *            levée par le service web
    */
   public final PingSecureResponse pingSecure() throws RemoteException {

      PingSecureRequest request = new PingSecureRequest();

      return service.pingSecure(request);
   }
   
}
