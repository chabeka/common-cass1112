package fr.urssaf.image.sae.webdemo.security;


/**
 * Classe de formulaire pour l'authentification
 * 
 */
public class AuthentificationForm {

   private String relay;

   private String saml;

   /**
    * Retourne le contenu du paramètre <code>RelayState</code><br>
    * 
    * @return la servlet demandé à l'authentification
    */
   public final String getRelayState() {
      return relay;
   }

   /**
    * Initialise le contenu du paramètre <code>RelayState</code>
    * 
    * @param relay
    *           la servlet demandé à l'authentification
    */
   public final void setRelayState(String relay) {
      this.relay = relay;
   }

   /**
    * Retourne le contenu du paramètre <code>SAMLResponse</code><br>
    * 
    * @return le jeton VI de l'authentification
    */
   public final String getSAMLResponse() {
      return saml;
   }

   /**
    * Initialise le contenu du paramètre <code>SAMLResponse</code>
    * 
    * @param saml
    *           le jeton VI de l'authentification
    */
   public final void setSAMLResponse(String saml) {
      this.saml = saml;
   }

}
