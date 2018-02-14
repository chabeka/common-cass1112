package fr.urssaf.image.sae.vi.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Le niveau d'authentification initial n'est pas conforme au contrat
 * d'interopérabilité
 * 
 * 
 */
public class VINivAuthException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /**
    * instancie le message d'exception Le niveau d'authentification
    * '<i>&lt;methodAuth></i>' est incorrect
    * 
    * @param methodAuth
    *           niveau d'authentification
    */
   public VINivAuthException(URI methodAuth) {
      super(createMessage(methodAuth));
   }

   private static String createMessage(URI methodAuth) {

      String result;
      
      if (StringUtils.isBlank(ObjectUtils.toString(methodAuth))) {
         
         result = "Le niveau d'authentification n'est pas renseigné";
         
      } else {
         
         Map<String, String> args = new HashMap<String, String>();
         args.put("0", methodAuth.toASCIIString());

         String message = "Le niveau d'authentification '${0}' est incorrect";
         
         result = StrSubstitutor.replace(message, args);
         
      }
      
      return result;
       
   }

   /**
    * 
    * @return "vi:InvalidAuthLevel"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createVISoapFaultCode("InvalidAuthLevel");
   }

   /**
    * 
    * @return "Le niveau d'authentification initial n'est pas conforme au contrat d'interopérabilité"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "Le niveau d'authentification initial n'est pas conforme au contrat d'interopérabilité";
   }
}
