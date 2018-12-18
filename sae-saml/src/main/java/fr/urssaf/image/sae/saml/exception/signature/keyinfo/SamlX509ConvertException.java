package fr.urssaf.image.sae.saml.exception.signature.keyinfo;

/**
 * Erreur technique de conversion d'un certificat X509 au format OpenSaml vers
 * un certificat X509 au format natif Java
 */
public class SamlX509ConvertException extends SamlKeyInfoException {


   private static final long serialVersionUID = -6836006230772810829L;


   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlX509ConvertException(String message) {
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
   public SamlX509ConvertException(String message, Throwable cause) {
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
   public SamlX509ConvertException(Throwable cause) {
      super(cause);
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return 
         "Erreur technique de conversion d'un certificat X509 au format OpenSaml vers" +
         " un certificat X509 au format natif Java";
   }
   

}
