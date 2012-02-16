package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationMTOM;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationMTOMResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;


/**
 * Service client pour la consultation avec optimisation MTOM du SAE
 * 
 * 
 */
@Service
public class ConsultationMTOMService {
   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public ConsultationMTOMService(SaeServiceStub service) {
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
   public final ConsultationMTOMResponseType consultationMTOM(String uuid)
         throws RemoteException {

      ConsultationMTOM request = RequestServiceFactory.createConsultationMTOM(uuid, null);

      return service.consultationMTOM(request).getConsultationMTOMResponse();
   }

   /**
    * appel du service de consultation d'une archive du SAE
    * 
    * @param uuid
    *           identifiant du document du SAe
    * @param listMetaData
    *           liste des metadata attendues
    * @return réponse de la consultation
    * @throws RemoteException
    *            levée par le web service
    */
   public final ConsultationMTOMResponseType consultationMTOM(String uuid,
         List<String> listMetaData) throws RemoteException {

      ConsultationMTOM request = RequestServiceFactory.createConsultationMTOM(uuid, listMetaData);

      return service.consultationMTOM(request).getConsultationMTOMResponse();
   }
}
