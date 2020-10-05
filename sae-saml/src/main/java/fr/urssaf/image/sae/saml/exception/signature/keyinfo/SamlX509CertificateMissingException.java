package fr.urssaf.image.sae.saml.exception.signature.keyinfo;

import org.apache.commons.lang.StringUtils;

/**
 * Il n'y a aucun X509Certificate dans la partie X509Data de la signature
 */
public class SamlX509CertificateMissingException extends SamlKeyInfoException {

   
   private static final long serialVersionUID = 1191889468081837663L;

   
   /**
    * Constructeur
    */
   public SamlX509CertificateMissingException() {
      super(StringUtils.EMPTY);
   }
   

   /**
    * Constructeur
    * 
    * @param message
    *           the detail message. The detail message is saved for later
    *           retrieval by the {@link #getMessage()} method.
    */
   public SamlX509CertificateMissingException(String message) {
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
   public SamlX509CertificateMissingException(String message, Throwable cause) {
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
   public SamlX509CertificateMissingException(Throwable cause) {
      super(cause);
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return 
         "Erreur lors de la lecture de la clé publique qui a servi a signer l'assertion : " +
         "Aucun certificat X509 n'est présent dans la partie <X509Data> de la signature";
   }
   

}
