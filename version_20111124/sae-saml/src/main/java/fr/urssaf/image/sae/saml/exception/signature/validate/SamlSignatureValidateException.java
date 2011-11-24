package fr.urssaf.image.sae.saml.exception.signature.validate;

import fr.urssaf.image.sae.saml.exception.signature.SamlSignatureException;

/**
 * La vérification de l'assertion SAML a échoué.
 */
public class SamlSignatureValidateException extends SamlSignatureException {

   
   private static final long serialVersionUID = 8608662785365293868L;


   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlSignatureValidateException(String message) {
      super(message);
   }
   
   
   /**
    * Constructeur
    *
    * @param  message the detail message (which is saved for later retrieval
    *         by the {@link #getMessage()} method).
    * @param  cause the cause (which is saved for later retrieval by the
    *         {@link #getCause()} method).  (A <tt>null</tt> value is
    *         permitted, and indicates that the cause is nonexistent or
    *         unknown.)
    */
   public SamlSignatureValidateException(String message, Throwable cause) {
       super(message, cause);
   }
   
   
   /**
    * 
    * Constructeur
    * 
    * @param cause
    *           the cause (which is saved for later retrieval by the
    *           {@link #getCause()} method). (A <tt>null</tt> value is
    *           permitted, and indicates that the cause is nonexistent or
    *           unknown.)
    */
   public SamlSignatureValidateException(Throwable cause) {
      super(cause);
   }
   
   

}
