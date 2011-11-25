package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;

/**
 * Service client pour la consultation du SAE
 * 
 * 
 */
@Service
public class ConsultationService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ConsultationService(SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service de consultation d'une archive du SAE
    * 
    * @param uuid
    *           identifiant du document du SAe
    * @return réponse de la consultation
    * @throws RemoteException
    *            levée par le web service
    */
   public final ConsultationResponseType consultation(String uuid)
         throws RemoteException {

      Consultation request = RequestServiceFactory.createConsultation(uuid);

      return service.consultation(request).getConsultationResponse();
   }
}
