package fr.urssaf.image.sae.vi.exception;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.text.StrSubstitutor;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * L'identifiant de l'organisme client présent dans le VI est invalide ou
 * inconnu
 * 
 * 
 */
public class VIAppliClientException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /***
    * instancie le message d'exception"L'identifiant de l'organisme client
    * présent dans le VI (<i>&lt;idOrg></i>) est invalide ou inconnu"
    * 
    * @param idOrg
    *           identifiant de l'organisme client
    */
   public VIAppliClientException(String idOrg) {
      super(createMessage(idOrg));
   }

   private static String createMessage(String idOrg) {

      Map<String, String> args = new HashMap<String, String>();
      args.put("0", idOrg);

      String message = "L'identifiant de l'organisme client présent dans le VI (${0}) est invalide ou inconnu";

      return StrSubstitutor.replace(message, args);
   }

   /**
    * 
    * @return "vi:InvalidIssuer"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createVISoapFaultCode("InvalidIssuer");
   }

   /**
    * 
    * @return 
    *         "L'identifiant de l'organisme client présent dans le VI est invalide ou inconnu"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "L'identifiant de l'organisme client présent dans le VI est invalide ou inconnu";
   }

}
