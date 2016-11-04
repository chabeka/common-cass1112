package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.DocumentExistant;
import fr.cirtil.www.saeservice.DocumentExistantResponse;
import fr.cirtil.www.saeservice.DocumentExistantResponseType;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.webservices.exception.DocumentExistantAxisFault;
import fr.urssaf.image.sae.webservices.service.WSDocumentExistantService;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Service
public class WSDocumentExistantServiceImpl implements WSDocumentExistantService{
   
   private static final Logger LOG = LoggerFactory
         .getLogger(WSDocumentExistantServiceImpl.class);
   
   @Autowired
   private SAEDocumentExistantService saeService;

   @SuppressWarnings("unused")
   @Override
   public DocumentExistantResponse documentExistant(DocumentExistant request) throws DocumentExistantAxisFault, SearchingServiceEx, ConnectionServiceEx {
      // TODO Auto-generated method stub
      
      String prefixeTrc = "copie()";
      UUID uuid = UUID.fromString(request.getDocumentExistant().getIdGed().getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", "copie()",
            uuid);
      
      boolean res;
         res = saeService.documentExistant(uuid);
         LOG.debug("{} - UUID : \"{}\"", res);
         DocumentExistantResponse response = createDocumentExistantResponse();
         DocumentExistantResponseType responseType = response.getDocumentExistantResponse();
         responseType.setIsDocExist(res);
         if (response == null) {
            LOG.debug("{} - Valeur de retour : null", prefixeTrc);
         } else {
            LOG.debug("{} - Valeur de retour isDocExist: \"{}\"", prefixeTrc,
                  response.getDocumentExistantResponse().getIsDocExist());
         }
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
         return response;
   }
   
   private static DocumentExistantResponse createDocumentExistantResponse() {

      DocumentExistantResponse response = new DocumentExistantResponse();
      DocumentExistantResponseType responseType = new DocumentExistantResponseType();
      response.setDocumentExistantResponse(responseType);

      return response;
   }

}
