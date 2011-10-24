package fr.urssaf.image.sae.vi.exception;

import javax.xml.namespace.QName;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Le VI est invalide
 * 
 * 
 */
public class VIInvalideException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param msg
    *           message de l'exception
    */
   public VIInvalideException(String msg) {
      super(msg);
   }

   /**
    * 
    * @return "vi:InvalidVI"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createVISoapFaultCode("InvalidVI");
   }

   /**
    * 
    * @return "Le VI est invalide"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "Le VI est invalide";
   }

}
