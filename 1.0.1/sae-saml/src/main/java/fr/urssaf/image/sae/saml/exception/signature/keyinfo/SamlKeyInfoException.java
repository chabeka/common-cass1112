package fr.urssaf.image.sae.saml.exception.signature.keyinfo;

/**
 * Une erreur a été détectée sur la partie KeyInfo de la signature XML
 */
public class SamlKeyInfoException extends Exception {

   
   private static final long serialVersionUID = -7364620476253913304L;


   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlKeyInfoException(String message) {
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
   public SamlKeyInfoException(String message, Throwable cause) {
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
   public SamlKeyInfoException(Throwable cause) {
      super(cause);
   }
   
   

}
