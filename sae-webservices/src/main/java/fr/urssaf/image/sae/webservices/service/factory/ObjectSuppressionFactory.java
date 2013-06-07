package fr.urssaf.image.sae.webservices.service.factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cirtil.www.saeservice.ModificationResponse;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.cirtil.www.saeservice.SuppressionResponseType;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link ModificationResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectSuppressionFactory {
   private static final Logger LOG = LoggerFactory
         .getLogger(ObjectSuppressionFactory.class);

   private ObjectSuppressionFactory() {

   }

   /**
    * instanciation de {@link SuppressionResponse}.<br>
    * Implementation de {@link SuppressionResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="suppressionResponseType">
    *    ...     
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link SuppressionResponse}
    */
   public static SuppressionResponse createSupressionResponse() {
      String prefixeTrc = "createSuppressionResponse()";
      LOG.debug("{} - Début", prefixeTrc);
      SuppressionResponse response = new SuppressionResponse();
      SuppressionResponseType responseType = new SuppressionResponseType();
      response.setSuppressionResponse(responseType);

      LOG.debug("{} - Valeur de retour : null", prefixeTrc);

      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
   }

}
