package fr.urssaf.image.sae.saml.exception.signature.validate;

/**
 * Un problème est survenue lors de la lecture du certificat de signature dans
 * la partie KeyInfo de la signature de l'assertion SAML. 
 */
public class SamlSignatureKeyInfoException extends SamlSignatureValidateException {

   
   private static final long serialVersionUID = -6458292705585354551L;


   /**
    * Constructeur
    */
   public SamlSignatureKeyInfoException() {
      super("");
   }
   
   
   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlSignatureKeyInfoException(String message) {
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
   public SamlSignatureKeyInfoException(String message, Throwable cause) {
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
   public SamlSignatureKeyInfoException(Throwable cause) {
      super(cause);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return
         "Un problème est survenue lors de la lecture du certificat de signature dans" + 
         " la partie KeyInfo de la signature de l'assertion SAML.";
   }
   
}
