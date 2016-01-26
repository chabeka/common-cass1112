package fr.urssaf.image.sae.webservices.service.factory;

import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationMTOMResponseType;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponse;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponseType;
import fr.cirtil.www.saeservice.ListeMetadonneeDispoType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeDispoType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponseType;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.webservices.modele.ConsultationDataSource;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link GetDocFormatOrigineResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectGetDocFormatOrigineFactory {

   private ObjectGetDocFormatOrigineFactory() {

   }

   /**
    * Instanciation de {@link GetDocFormatOrigineResponse}.<br>
    * 
    * @param documentAttache
    *           Le document attaché
    * @return instance de {@link GetDocFormatOrigineResponse}
    */
   public static GetDocFormatOrigineResponse createGetDocFormatOrigineResponse(
         DataHandler content) {
      Assert.notNull(content, "content is required");
      
      GetDocFormatOrigineResponse response = new GetDocFormatOrigineResponse();
      GetDocFormatOrigineResponseType responseType = new GetDocFormatOrigineResponseType();

      
      DataSource dataSource;
      try {
         // Type mimie par défaut, car on ne maîtrise pas le format des documents attachés
         dataSource = new ConsultationDataSource(content.getInputStream(),
               "application/octet-stream");

      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      DataHandler dataHandler = new DataHandler(dataSource);

     
      responseType.setContenu(dataHandler);
      
      response.setGetDocFormatOrigineResponse(responseType);
      
      return response;
   }

 
}
