package fr.urssaf.image.sae.saml.exception.signature.validate;

/**
 * La signature n'est pas cryptographiquement correcte 
 */
public class SamlSignatureCryptoException extends SamlSignatureValidateException {

   
   private static final long serialVersionUID = -5876969135021245300L;


   /**
    * Constructeur
    */
   public SamlSignatureCryptoException() {
      super("");
   }
   
   
   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlSignatureCryptoException(String message) {
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
   public SamlSignatureCryptoException(String message, Throwable cause) {
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
   public SamlSignatureCryptoException(Throwable cause) {
      super(cause);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return "La signature n'est pas cryptographiquement correcte";
   }
   
}
