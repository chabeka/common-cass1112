package fr.urssaf.image.sae.webservices.impl.factory;

import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponseType;
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

   /**
    * instanciation de {@link ArchivageMasseAvecHashResponse}.<br>
    * implémentation de {@link ArchivageMasseAvecHashResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="archivageMasseAvecHashResponseType">
    * ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link ArchivageMasseAvecHashResponse}
    */
   public static ArchivageMasseAvecHashResponse createArchivageMasseAvecHashResponse(String uuid) {

      ArchivageMasseAvecHashResponse response = new ArchivageMasseAvecHashResponse();
      ArchivageMasseAvecHashResponseType responseType = new ArchivageMasseAvecHashResponseType();
      responseType.setUuid(uuid);
      response.setArchivageMasseAvecHashResponse(responseType);
      return response;
   }
}
