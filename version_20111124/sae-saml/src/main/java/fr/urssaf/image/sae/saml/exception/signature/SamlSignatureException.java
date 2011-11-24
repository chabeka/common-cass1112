package fr.urssaf.image.sae.saml.exception.signature;

import fr.urssaf.image.sae.saml.exception.SamlVerificationException;

/**
 * La signature de l'assertion n'est pas valide
 * 
 * 
 */
public class SamlSignatureException extends SamlVerificationException {

   
   private static final long serialVersionUID = 6754056719908182756L;

   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlSignatureException(String message) {
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
   public SamlSignatureException(String message, Throwable cause) {
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
   public SamlSignatureException(Throwable cause) {
      super(cause);
   }

}
