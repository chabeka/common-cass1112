package fr.urssaf.image.sae.webservices.service.factory;

import java.util.UUID;

import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponseType;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link ArchivageUnitaireResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectArchivageUnitaireFactory {
   
   private ObjectArchivageUnitaireFactory(){
      
   }

   /**
    * instanciation de {@link ArchivageUnitaireResponse}.<br>
    * Implementation de {@link ArchivageUnitaireResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="archivageUnitaireResponseType">
    *    ...     
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="idArchive" type="sae:uuidType">
    *       ...      
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @param idArchive
    *           valeur de <code>uuidType</code>
    * @return instance de {@link ArchivageUnitaireResponse}
    */
   public static ArchivageUnitaireResponse createArchivageUnitaireResponse(
         UUID idArchive) {

      ArchivageUnitaireResponse response = createArchivageUnitaireResponse();
      ArchivageUnitaireResponseType responseType = response
            .getArchivageUnitaireResponse();

      responseType.setIdArchive(ObjectTypeFactory.createUuidType(idArchive));

      return response;
   }
   
   /**
    * instanciation de {@link ArchivageUnitaireResponse} vide.<br>
    * 
    * @return instance de {@link ArchivageUnitaireResponse}
    */
   public static ArchivageUnitaireResponse createArchivageUnitaireResponse() {

      ArchivageUnitaireResponse response = new ArchivageUnitaireResponse();
      ArchivageUnitaireResponseType responseType = new ArchivageUnitaireResponseType();
      response.setArchivageUnitaireResponse(responseType);

      return response;
   }

}
