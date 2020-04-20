package fr.urssaf.image.sae.saml.exception.signature.validate;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * L'Issuer du certificat contenant la clé publique de signature de l'assertion SAML
 * ne répond pas aux patterns spécifiés pour la vérification.
 */
public class SamlIssuerPatternException extends SamlSignatureValidateException {

   
   private static final long serialVersionUID = -2355149236341201155L;

   private final String issuer;
   private final List<String> patterns;
   
   /**
    * Constructeur
    * 
    * @param issuer l'issuer
    * @param patterns les patterns de vérification de l'issuer
    */
   public SamlIssuerPatternException(
         String issuer,
         List<String> patterns) {
      
      super(StringUtils.EMPTY);

      this.issuer = issuer;
      this.patterns = patterns;
      
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      StringBuilder sbMessage = new StringBuilder();
      sbMessage.append("L'Issuer du certificat contenant la clé publique de signature de l'assertion SAML");
      sbMessage.append(" ne répond pas aux patterns spécifiés pour la vérification.");
      sbMessage.append("\r\n" + "Issuer: " + this.issuer);
      sbMessage.append("\r\n" + "Liste des patterns :");
      if ((patterns==null) || (patterns.isEmpty())) {
         sbMessage.append("\r\n" + "aucun pattern :");
      }
      else {
         for(String pattern: patterns) {
            sbMessage.append("\r\n   " + pattern);
         }
      }
      return sbMessage.toString();
   }
   
   
}
