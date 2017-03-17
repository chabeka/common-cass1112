package fr.urssaf.image.sae.webservices.impl.factory;

import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponseType;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponseType;
import fr.cirtil.www.saeservice.ModificationMasseResponse;
import fr.cirtil.www.saeservice.ModificationMasseResponseType;
import fr.cirtil.www.saeservice.RestoreMasseResponse;
import fr.cirtil.www.saeservice.RestoreMasseResponseType;
import fr.cirtil.www.saeservice.SuppressionMasseResponse;
import fr.cirtil.www.saeservice.SuppressionMasseResponseType;

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
   
   /**
    * instanciation de {@link SuppressionMasseResponse}.<br>
    * implémentation de {@link SuppressionMasseResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="suppressionMasseResponseType">
    * ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link SuppressionMasseResponse}
    */
   public static SuppressionMasseResponse createSuppressionMasseResponse(String uuid) {

      SuppressionMasseResponse response = new SuppressionMasseResponse();
      SuppressionMasseResponseType responseType = new SuppressionMasseResponseType();
      responseType.setUuid(uuid);
      response.setSuppressionMasseResponse(responseType);
      return response;
   }
   
   
   /**
    * instanciation de {@link RestoreMasseResponse}.<br>
    * implémentation de {@link RestoreMasseResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="restoreMasseResponseType">
    * ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link RestoreMasseResponse}
    */
   public static RestoreMasseResponse createRestoreMasseResponse(String uuid) {

      RestoreMasseResponse response = new RestoreMasseResponse();
      RestoreMasseResponseType responseType = new RestoreMasseResponseType();
      responseType.setUuid(uuid);
      response.setRestoreMasseResponse(responseType);
      return response;
   }

   /**
    * instanciation de {@link ModificationMasseResponse}.<br>
    * implémentation de {@link ModificationMasseResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="modificationMasseResponseType">
    * ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link ModificationMasseResponse}
    */
   public static ModificationMasseResponse createModificationMasseResponse(
         String uuid) {

      ModificationMasseResponse modification = new ModificationMasseResponse();
      ModificationMasseResponseType modificationType = new ModificationMasseResponseType();
      modificationType.setUuid(uuid);
      modification.setModificationMasseResponse(modificationType);
      return modification;
   }
}
