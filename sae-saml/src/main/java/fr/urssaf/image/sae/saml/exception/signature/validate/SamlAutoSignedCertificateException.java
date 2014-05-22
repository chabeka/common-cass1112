package fr.urssaf.image.sae.saml.exception.signature.validate;

/**
 * Les certificats auto-signés ne sont pas autorisés pour la signature d'une assertion SAML 
 */
public class SamlAutoSignedCertificateException extends SamlSignatureValidateException {

   
   private static final long serialVersionUID = -6654407569097464485L;


   /**
    * Constructeur
    */
   public SamlAutoSignedCertificateException() {
      super("");
   }
   
   
   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlAutoSignedCertificateException(String message) {
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
   public SamlAutoSignedCertificateException(String message, Throwable cause) {
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
   public SamlAutoSignedCertificateException(Throwable cause) {
      super(cause);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return
         "Les certificats auto-signés ne sont pas autorisés pour la signature d'une assertion SAML";
   }
   
}
