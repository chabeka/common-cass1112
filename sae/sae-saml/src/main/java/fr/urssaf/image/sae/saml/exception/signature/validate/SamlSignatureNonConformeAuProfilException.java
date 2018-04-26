package fr.urssaf.image.sae.saml.exception.signature.validate;

/**
 * La signature n'est pas conforme au profil d'une signature SAML (le XML est incorrect) 
 */
public class SamlSignatureNonConformeAuProfilException extends SamlSignatureValidateException {

   
   private static final long serialVersionUID = 8367049501194691456L;


   /**
    * Constructeur
    */
   public SamlSignatureNonConformeAuProfilException() {
      super("");
   }
   
   
   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlSignatureNonConformeAuProfilException(String message) {
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
   public SamlSignatureNonConformeAuProfilException(String message, Throwable cause) {
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
   public SamlSignatureNonConformeAuProfilException(Throwable cause) {
      super(cause);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return "La signature n'est pas conforme au profil d'une signature SAML (le XML est incorrect)";
   }
   
}
