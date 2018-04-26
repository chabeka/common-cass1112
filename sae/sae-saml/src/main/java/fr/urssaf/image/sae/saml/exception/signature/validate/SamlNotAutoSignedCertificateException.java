package fr.urssaf.image.sae.saml.exception.signature.validate;

import org.apache.commons.lang.StringUtils;

/**
 * Les certificats des AC racine transmis pour la validation de la signature, doivent
 * être des certificats auto-signés 
 */
public class SamlNotAutoSignedCertificateException extends SamlSignatureValidateException {


   private static final long serialVersionUID = -3176760018248140014L;

   
   private final String subject;
   private final String issuer;
      

   /**
    * Constructeur
    * 
    * @param subject le sujet du certificat qui n'est pas auto-signé
    * @param issuer l'issuer du certificat qui n'est pas auto-signé
    */
   public SamlNotAutoSignedCertificateException(
         String subject,
         String issuer) {
      super(StringUtils.EMPTY);
      this.subject = subject;
      this.issuer = issuer;
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      StringBuilder sbMessage = new StringBuilder();
      sbMessage.append("Les certificats des AC racine tranmis pour la validation de la signature");
      sbMessage.append(" doivent obligatoirement être des certificats auto-signés.");
      sbMessage.append("\r\n" + "Hors le certificat suivant ne l'est pas :");
      sbMessage.append("\r\n" + "Subject : " + this.subject);
      sbMessage.append("\r\n" + "Issuer : " + this.issuer);
      return sbMessage.toString();
   }
   
}
