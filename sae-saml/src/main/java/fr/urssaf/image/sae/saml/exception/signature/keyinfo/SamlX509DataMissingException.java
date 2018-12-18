package fr.urssaf.image.sae.saml.exception.signature.keyinfo;

import org.apache.commons.lang.StringUtils;

/**
 * Il manque la partie X509Data dans la partie KeyInfo de la signature XML
 */
public class SamlX509DataMissingException extends SamlKeyInfoException {

   
   private static final long serialVersionUID = 7549834022780185224L;


   /**
    * Constructeur
    */
   public SamlX509DataMissingException() {
      super(StringUtils.EMPTY);
   }
   
   
   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlX509DataMissingException(String message) {
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
   public SamlX509DataMissingException(String message, Throwable cause) {
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
   public SamlX509DataMissingException(Throwable cause) {
      super(cause);
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return 
         "Erreur lors de la lecture de la clé publique qui a servi a signer l'assertion : " +
         "La signature est incomplète : il manque la partie <X509Data> dans la partie <KeyInfo>";
   }
   

}
