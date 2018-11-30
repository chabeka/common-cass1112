package fr.urssaf.image.sae.vi.exception;

import javax.xml.namespace.QName;

/**
 * Une erreur a été détectée lors de la vérification du VI
 * 
 * 
 */
@SuppressWarnings("PMD.AbstractNaming")
public abstract class VIVerificationException extends Exception {

   private static final long serialVersionUID = 1L;

   protected VIVerificationException(Exception exception) {
      super(exception);
   }
   
   protected VIVerificationException(String msg) {
      super(msg);
   }
   
   
   protected VIVerificationException(String msg, Throwable cause) {
      super(msg,cause);
   }

   /**
    * 
    * @return code du SoapFault
    */
   public abstract QName getSoapFaultCode();

   /**
    * 
    * @return message du SoapFault
    */
   public abstract String getSoapFaultMessage();

}
