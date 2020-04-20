package fr.urssaf.image.sae.vi.exception;

import javax.xml.namespace.QName;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.vi.exception.factory.SoapFaultCodeFactory;

/**
 * Une erreur technique sur le format du VI a été détectée
 * 
 * 
 */
public class VIFormatTechniqueException extends VIVerificationException {

   private static final long serialVersionUID = 1L;

   /**
    * @param exception
    *           assertion SAML non conforme
    */
   public VIFormatTechniqueException(SamlFormatException exception) {
      super(exception);
   }

   /**
    * 
    * @return "wsse:InvalidSecurityToken"
    */
   @Override
   public final QName getSoapFaultCode() {

      return SoapFaultCodeFactory.createWsseSoapFaultCode("InvalidSecurityToken");
   }

   /**
    * 
    * @return "Le jeton de sécurité fourni est invalide"
    */
   @Override
   public final String getSoapFaultMessage() {

      return "Le jeton de sécurité fourni est invalide";
   }

}
