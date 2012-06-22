package fr.urssaf.image.sae.webservices.impl.factory;

import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponseType;

/**
 * Classe d'instanciation des réponses pour l'implémentation
 * {@link fr.urssaf.image.sae.webservices.service.impl.WSCaptureMasseServiceImpl}
 * 
 * 
 */
public final class ObjectStorageResponseFactory {

   private ObjectStorageResponseFactory() {

   }
   
   /**
    * instanciation de {@link ArchivageMasseResponse}.<br>
    * implémentation de {@link ArchivageMasseResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="archivageMasseResponseType">
    * ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link ArchivageMasseResponse}
    */
   public static ArchivageMasseResponse createArchivageMasseResponse() {

      ArchivageMasseResponse response = new ArchivageMasseResponse();
      ArchivageMasseResponseType responseType = new ArchivageMasseResponseType();
      response.setArchivageMasseResponse(responseType);
      return response;
   }

}
