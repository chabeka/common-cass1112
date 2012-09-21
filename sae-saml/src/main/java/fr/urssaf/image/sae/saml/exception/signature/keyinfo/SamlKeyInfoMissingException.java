package fr.urssaf.image.sae.saml.exception.signature.keyinfo;

import org.apache.commons.lang.StringUtils;

/**
 * Il manque la partie KeyInfo dans la signature XML
 */
public class SamlKeyInfoMissingException extends SamlKeyInfoException {

   
   private static final long serialVersionUID = -707820833377865708L;

   
   /**
    * Constructeur
    */
   public SamlKeyInfoMissingException() {
      super(StringUtils.EMPTY);
   }
   

   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlKeyInfoMissingException(String message) {
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
   public SamlKeyInfoMissingException(String message, Throwable cause) {
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
   public SamlKeyInfoMissingException(Throwable cause) {
      super(cause);
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return 
         "Erreur lors de la lecture de la clé publique qui a servi a signer l'assertion : " + 
         "La signature est incomplète : il manque la partie <KeyInfo>";
   }
   

}
