package fr.urssaf.image.sae.webservices.service.factory;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.ListeMetadonneeDispoType;
import fr.cirtil.www.saeservice.MetadonneeDispoType;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponseType;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link RecuperationMetadonneesResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectRecuperationMetadonneesFactory {

   private ObjectRecuperationMetadonneesFactory() {

   }

   /**
    * instanciation de {@link RecuperationMetadonneesResponse}.<br>
    * Implementation de {@link RecuperationMetadonneesResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="recuperationMetadonneesResponseType">
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="metadonnees" type="sae:listeMetadonneeDispoType">
    *       ...
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * 
    * @param metadonnees
    *           valeur de <code>listeMetadonneeDispoType</code>
    * @return instance de {@link ConsultationResponse}
    */
   public static RecuperationMetadonneesResponse createRecuperationMetadonneesResponse(
         List<MetadonneeDispoType> metadonnees) {

      RecuperationMetadonneesResponse response = new RecuperationMetadonneesResponse();
      RecuperationMetadonneesResponseType responseType = new RecuperationMetadonneesResponseType();

      ListeMetadonneeDispoType listeMetadonnee = new ListeMetadonneeDispoType();

      if (CollectionUtils.isNotEmpty(metadonnees)) {

         for (MetadonneeDispoType metadonnee : metadonnees) {

            listeMetadonnee.addMetadonnee(metadonnee);
         }

      }

      responseType.setMetadonnees(listeMetadonnee);
      response.setRecuperationMetadonneesResponse(responseType);
      return response;
   }

   /**
    * instanciation de {@link RecuperationMetadonneesResponse} vide<br>
    * 
    * @return instance de {@link RecuperationMetadonneesResponse}
    */
   public static RecuperationMetadonneesResponse createRecuperationMetadonneesResponse() {

      RecuperationMetadonneesResponse response = new RecuperationMetadonneesResponse();
      RecuperationMetadonneesResponseType responseType = new RecuperationMetadonneesResponseType();
      response.setRecuperationMetadonneesResponse(responseType);

      return response;
   }

}
