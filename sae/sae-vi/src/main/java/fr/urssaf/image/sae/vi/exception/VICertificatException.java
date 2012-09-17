package fr.urssaf.image.sae.vi.exception;

import javax.xml.namespace.QName;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * La signature électronique du VI est incorrecte
 * 
 * 
 */
public class VICertificatException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param message
    *           message d'erreur
    */
   public VICertificatException(String message) {
      super(message);
   }

   /**
    * 
    * @return "wsse:FailedCheck"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createWsseSoapFaultCode("FailedCheck");
   }

   /**
    * 
    * @return "La signature ou le chiffrement n'est pas valide"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "La signature ou le chiffrement n'est pas valide (" + getMessage()
            + ")";
   }

}
