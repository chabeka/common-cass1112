package fr.urssaf.image.sae.webservices.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
   private static final Logger LOG = LoggerFactory
         .getLogger(ObjectArchivageUnitaireFactory.class);

   private ObjectArchivageUnitaireFactory() {

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
      String prefixeTrc = "createArchivageUnitaireResponse()";
      LOG.debug("{} - Début", prefixeTrc);
      ArchivageUnitaireResponse response = createArchivageUnitaireResponse();
      ArchivageUnitaireResponseType responseType = response
            .getArchivageUnitaireResponse();

      responseType.setIdArchive(ObjectTypeFactory.createUuidType(idArchive));
      if (response != null) {
         LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, response
               .getArchivageUnitaireResponse().getIdArchive());
      } else {
         LOG.debug("{} - Valeur de retour : null", prefixeTrc);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
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
