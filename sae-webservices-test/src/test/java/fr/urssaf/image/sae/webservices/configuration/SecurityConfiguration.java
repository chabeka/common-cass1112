package fr.urssaf.image.sae.webservices.configuration;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Classe de configuration des services sécurisés
 * 
 * 
 */
public final class SecurityConfiguration {

   private SecurityConfiguration() {

   }

   /**
    * méthode à appeller dans le after des tests
    */
   public static void cleanSecurityContext() {

      SecurityContextHolder.clearContext();
   }

}
