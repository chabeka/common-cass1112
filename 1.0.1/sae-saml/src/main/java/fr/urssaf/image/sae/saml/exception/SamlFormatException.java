package fr.urssaf.image.sae.saml.exception;

/**
 * L'assertion SAML n'est pas conforme aux schémas XSD
 * 
 * 
 */
public class SamlFormatException extends SamlVerificationException {

   

   private static final long serialVersionUID = 761219107158800805L;

   /**
    * 
    * @param exception
    *           exception levée
    */
   public SamlFormatException(Exception exception) {
      super(exception);
   }

}
