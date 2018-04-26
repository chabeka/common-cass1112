package fr.urssaf.image.sae.webdemo.security;

/**
 * Classe pour les variables globales pour l'authentification
 * <ul>
 * <li>TOKEN_FIELD</li>
 * <li>SERVICE_FIELD</li>
 * <li>SECURITY_URL</li>
 * </ul>
 * 
 * 
 */
public final class AuthenticationConfiguration {

   private AuthenticationConfiguration() {

   }

   /**
    * nom du paramètre de la requête HTTP contenant le VI
    */
   public static final String TOKEN_FIELD = "SAMLResponse";

   /**
    * nom du paramètre de la requête HTTP contenant la valeur du service de
    * redirection
    */
   public static final String SERVICE_FIELD = "RelayState";

   /**
    * URL d'identification de spring security
    */
   public static final String SECURITY_URL = "authenticate_check.html";
}
