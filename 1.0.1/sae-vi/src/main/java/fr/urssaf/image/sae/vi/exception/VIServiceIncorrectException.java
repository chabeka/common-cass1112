package fr.urssaf.image.sae.vi.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.text.StrSubstitutor;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Le service visé ne correspond pas au service indiqué dans l'assertion
 * 
 * 
 */
public class VIServiceIncorrectException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /**
    * instancie le message d'exception Le service visé '<i>&lt;serviceVise></i>'
    * ne correspond pas à celui indiqué dans le VI '<i>&lt;audience></i>'
    * 
    * @param serviceVise
    *           service visé
    * @param audience
    *           service dans le VI
    */
   public VIServiceIncorrectException(URI serviceVise, URI audience) {
      super(createMessage(serviceVise, audience));
   }

   private static String createMessage(URI serviceVise, URI audience) {

      Map<String, String> args = new HashMap<String, String>();
      args.put("0", serviceVise.toASCIIString());
      args.put("1", audience.toASCIIString());

      String message = "Le service visé '${0}' ne correspond pas à celui indiqué dans le VI '${1}'";

      return StrSubstitutor.replace(message, args);
   }

   /**
    * 
    * @return "vi:InvalidService"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createVISoapFaultCode("InvalidService");
   }

   /**
    * 
    * @return "Le service visé par le VI n'existe pas ou est invalide"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "Le service visé par le VI n'existe pas ou est invalide";
   }

}
