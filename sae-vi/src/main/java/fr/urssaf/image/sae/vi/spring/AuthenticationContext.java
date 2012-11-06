package fr.urssaf.image.sae.vi.spring;

import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Classe du Contexte de sécurité de Spring<br>
 * <br>
 * Concrètement la classe encapsule {@link SecurityContextHolder}
 * 
 * 
 */
public final class AuthenticationContext {

   private AuthenticationContext() {

   }

   /**
    * La méthode appelle {@link SecurityContextHolder#getContext()
    * #getAuthenticationToken()}
    * 
    * @return le jeton d'authentication courant
    */
   public static AuthenticationToken getAuthenticationToken() {
      return (AuthenticationToken) SecurityContextHolder.getContext()
            .getAuthentication();
   }

   /**
    * initialise le contexte de sécurité courant<br>
    * La méthode appelle {@link SecurityContextHolder#getContext()
    * #setAuthenticationToken(AuthenticationToken)}
    * 
    * @param authentication
    *           jeton d'authentification du contexte de sécurité
    */
   public static void setAuthenticationToken(AuthenticationToken authentication) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
   }
}
