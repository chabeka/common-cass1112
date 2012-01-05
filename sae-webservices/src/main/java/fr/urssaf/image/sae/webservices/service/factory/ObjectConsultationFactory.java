package fr.urssaf.image.sae.webservices.service.factory;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.ConsultationResponseType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.ObjetNumeriqueConsultationType;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link ConsultationResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectConsultationFactory {

   private ObjectConsultationFactory() {

   }

   /**
    * instanciation de {@link ConsultationResponse}.<br>
    * Implementation de {@link ConsultationResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="consultationResponseType">
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="objetNumerique" type="sae:objetNumeriqueType">
    *        ...
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="metadonnees" type="sae:listeMetadonneeType">
    *       ...
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * 
    * @param content
    *           valeur de <code>objetNumeriqueType</code> non vide
    * @param metadonnees
    *           valeur de <code>listeMetadonneeType</code>
    * @return instance de {@link ConsultationResponse}
    */
   public static ConsultationResponse createConsultationResponse(
         byte[] content, List<MetadonneeType> metadonnees) {

      Assert.notNull(content, "content is required");

      ConsultationResponse response = new ConsultationResponse();
      ConsultationResponseType responseType = new ConsultationResponseType();
      ObjetNumeriqueConsultationType objetNumerique = ObjectTypeFactory
            .createObjetNumeriqueConsultationType(content);

      responseType.setObjetNumerique(objetNumerique);

      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();

      if (CollectionUtils.isNotEmpty(metadonnees)) {

         for (MetadonneeType metadonnee : metadonnees) {

            listeMetadonnee.addMetadonnee(metadonnee);
         }

      }
      
      responseType.setMetadonnees(listeMetadonnee);
      response.setConsultationResponse(responseType);
      return response;
   }

   /**
    * instanciation de {@link ConsultationResponse} vide<br>
    * 
    * @return instance de {@link ConsultationResponse}
    */
   public static ConsultationResponse createConsultationResponse() {

      ConsultationResponse response = new ConsultationResponse();
      ConsultationResponseType responseType = new ConsultationResponseType();
      response.setConsultationResponse(responseType);

      return response;
   }
}
