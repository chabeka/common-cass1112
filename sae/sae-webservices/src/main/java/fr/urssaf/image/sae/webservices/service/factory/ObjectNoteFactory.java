package fr.urssaf.image.sae.webservices.service.factory;

import java.util.List;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cirtil.www.saeservice.AjoutNoteResponse;
import fr.cirtil.www.saeservice.AjoutNoteResponseType;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.MetadonneeType;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link ConsultationResponse}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectNoteFactory {

   private static final Logger LOG = LoggerFactory
         .getLogger(ObjectSuppressionFactory.class);

   private ObjectNoteFactory() {

   }

   /**
    * instanciation de {@link AjoutNoteResponse}.<br>
    * Implementation de {@link AjoutNoteResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="AjoutNoteResponseType">
    *    ...     
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link AjoutNoteResponse}
    */
   public static AjoutNoteResponse createAjoutNoteResponse() {

      String prefixeTrc = "createAjoutNoteResponse()";
      LOG.debug("{} - Début", prefixeTrc);

      AjoutNoteResponse response = new AjoutNoteResponse();
      AjoutNoteResponseType responseType = new AjoutNoteResponseType();

      response.setAjoutNoteResponse(responseType);

      LOG.debug("{} - Valeur de retour : null", prefixeTrc);

      LOG.debug("{} - Sortie", prefixeTrc);

      return response;
   }

}
