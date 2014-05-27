package fr.urssaf.image.sae.webservices.service.factory;

import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationMTOMResponseType;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.ConsultationResponseType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.ObjetNumeriqueConsultationType;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.modele.ConsultationDataSource;

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
         DataHandler content, List<MetadonneeType> metadonnees) {

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

   /**
    * instanciation de {@link ConsultationMTOMResponse}.<br>
    * Implementation de {@link ConsultationMTOMResponseType}
    * 
    * @param content
    *           valeur de <code>objetNumeriqueType</code> non vide
    * @param metadonnees
    *           valeur de <code>listeMetadonneeType</code>
    * @param typeMime
    *           le type MIME à associer à la pièce jointe
    * @return instance de {@link ConsultationMTOMResponse}
    */
   public static ConsultationMTOMResponse createConsultationMTOMResponse(
         DataHandler content, List<MetadonneeType> metadonnees, String typeMime) {

      Assert.notNull(content, "content is required");

      ConsultationMTOMResponse responseMTOM = new ConsultationMTOMResponse();
      ConsultationMTOMResponseType responseMTOMType = new ConsultationMTOMResponseType();

      DataSource dataSource;
      try {
         dataSource = new ConsultationDataSource(content.getInputStream(),
               typeMime);

      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      DataHandler dataHandler = new DataHandler(dataSource);

      responseMTOMType.setContenu(dataHandler);

      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();

      if (CollectionUtils.isNotEmpty(metadonnees)) {

         for (MetadonneeType metadonnee : metadonnees) {

            listeMetadonnee.addMetadonnee(metadonnee);
         }

      }

      responseMTOMType.setMetadonnees(listeMetadonnee);
      responseMTOM.setConsultationMTOMResponse(responseMTOMType);
      return responseMTOM;
   }

}
