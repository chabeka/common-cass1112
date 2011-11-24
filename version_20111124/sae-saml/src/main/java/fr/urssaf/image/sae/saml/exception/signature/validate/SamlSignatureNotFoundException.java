package fr.urssaf.image.sae.saml.exception.signature.validate;

/**
 * La signature n'a pas été trouvée dans l'assertion SAML 
 */
public class SamlSignatureNotFoundException extends SamlSignatureValidateException {

   
   private static final long serialVersionUID = 3299073373755647219L;


   /**
    * Constructeur
    */
   public SamlSignatureNotFoundException() {
      super("");
   }
   
   
   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlSignatureNotFoundException(String message) {
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
   public SamlSignatureNotFoundException(String message, Throwable cause) {
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
   public SamlSignatureNotFoundException(Throwable cause) {
      super(cause);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return "La signature n'a pas été trouvée dans l'assertion SAML";
   }
   
}
