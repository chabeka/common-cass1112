package fr.urssaf.image.sae.webservices.service.factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cirtil.www.saeservice.ModificationResponse;
import fr.cirtil.www.saeservice.ModificationResponseType;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link ModificationResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectModificationFactory {
   private static final Logger LOG = LoggerFactory
         .getLogger(ObjectModificationFactory.class);

   private ObjectModificationFactory() {

   }

   /**
    * instanciation de {@link ModificationResponse}.<br>
    * Implementation de {@link ModificationResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="modificationResponseType">
    *    ...     
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link ModificationResponse}
    */
   public static ModificationResponse createModificationResponse() {
      String prefixeTrc = "createModificationResponse()";
      LOG.debug("{} - Début", prefixeTrc);
      ModificationResponse response = new ModificationResponse();
      ModificationResponseType responseType = new ModificationResponseType();
      response.setModificationResponse(responseType);

      LOG.debug("{} - Valeur de retour : null", prefixeTrc);

      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
   }

}
