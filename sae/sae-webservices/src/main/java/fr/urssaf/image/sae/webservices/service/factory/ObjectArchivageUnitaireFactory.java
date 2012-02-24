package fr.urssaf.image.sae.webservices.service.factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponseType;
import fr.cirtil.www.saeservice.ArchivageUnitaireRequestType;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponseType;
import fr.cirtil.www.saeservice.EcdeUrlType;
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
   
   
   
   /**
    * instanciation de {@link ArchivageUnitairePJResponse}.<br>
    * Implementation de {@link ArchivageUnitairePJResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="archivageUnitairePJResponseType">
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
   public static ArchivageUnitairePJResponse createArchivageUnitairePJResponse(
         UUID idArchive) {
      
      String prefixeTrc = "createArchivageUnitairePJResponse()";
      LOG.debug("{} - Début", prefixeTrc);
      
      ArchivageUnitairePJResponse response = createArchivageUnitairePJResponse();
      ArchivageUnitairePJResponseType responseType = response
            .getArchivageUnitairePJResponse();

      responseType.setIdArchive(ObjectTypeFactory.createUuidType(idArchive));
      if (response != null) {
         LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, response
               .getArchivageUnitairePJResponse().getIdArchive());
      } else {
         LOG.debug("{} - Valeur de retour : null", prefixeTrc);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
      return response;
   }
   
   /**
    * instanciation de {@link ArchivageUnitairePJResponse} vide.<br>
    * 
    * @return instance de {@link ArchivageUnitairePJResponse}
    */
   public static ArchivageUnitairePJResponse createArchivageUnitairePJResponse() {

      ArchivageUnitairePJResponse response = new ArchivageUnitairePJResponse();
      ArchivageUnitairePJResponseType responseType = new ArchivageUnitairePJResponseType();
      response.setArchivageUnitairePJResponse(responseType);

      return response;
   }
   
   
   /**
    * Conversion d'un objet archivageUnitairePJ en archivageUnitaire
    * 
    * @param archivageUnitairePJ objet a convertir
    * @return ArchivageUnitaire archivageUnitaire
    */
   public static ArchivageUnitaire convertToArchivageUnitaire(ArchivageUnitairePJ archivageUnitairePJ) {
      
      EcdeUrlType ecdeUrlType = archivageUnitairePJ.getArchivageUnitairePJ().getArchivageUnitairePJRequestTypeChoice_type0().getEcdeUrl();
      
      ArchivageUnitaire archivageUnitaire = new ArchivageUnitaire();
      
      ArchivageUnitaireRequestType archivageUType = new ArchivageUnitaireRequestType();
      archivageUType.setEcdeUrl(ecdeUrlType);
      archivageUType.setMetadonnees(archivageUnitairePJ.getArchivageUnitairePJ().getMetadonnees());
      
      archivageUnitaire.setArchivageUnitaire(archivageUType);
      
      return archivageUnitaire;
   }
   
   
   /**
    * Conversion d'un objet ArchivageUnitaireResponse en ArchivageUnitairePJResponse
    * 
    * @param archivageUnitaireResponse objet a convertir
    * @return ArchivageUnitairePJResponse archivageUnitairePJResponse
    */
   public static ArchivageUnitairePJResponse convertToArchivageUnitairePJResponse(ArchivageUnitaireResponse archivageUnitaireResponse) {

      ArchivageUnitairePJResponse archivageUnitairePJResponse = new ArchivageUnitairePJResponse();
      ArchivageUnitairePJResponseType archivageUnitairePJResponseType = new ArchivageUnitairePJResponseType();
      archivageUnitairePJResponseType.setIdArchive(archivageUnitaireResponse.getArchivageUnitaireResponse().getIdArchive());
      
      archivageUnitairePJResponse.setArchivageUnitairePJResponse(archivageUnitairePJResponseType);
      
      return archivageUnitairePJResponse;
   }
   

}
