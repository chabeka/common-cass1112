package fr.urssaf.image.sae.vi.exception;

import javax.xml.namespace.QName;

import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Problème sur les PAGM
 */
public class VIPagmIncorrectException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message Le message de l'exception
    */
   public VIPagmIncorrectException(String message) {
      super(message);
   }

   /**
    * 
    * @return "vi:InvalidPagm"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createVISoapFaultCode("InvalidPagm");
   }

   /**
    * 
    * @return "Le ou les PAGM présents dans le VI sont invalides"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "Le ou les PAGM présents dans le VI sont invalides";
   }

}
