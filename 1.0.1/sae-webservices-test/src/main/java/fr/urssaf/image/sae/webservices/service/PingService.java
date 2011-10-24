package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingRequest;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingResponse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureRequest;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse;

/**
 * Service client du ping du SAE
 * 
 * 
 */
@Service
public class PingService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public PingService(SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service ping du SAE
    * 
    * @return message du ping
    * @throws RemoteException
    *            levée par le service web
    */
   public final PingResponse ping() throws RemoteException {

      PingRequest request = new PingRequest();

      return service.ping(request);
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
