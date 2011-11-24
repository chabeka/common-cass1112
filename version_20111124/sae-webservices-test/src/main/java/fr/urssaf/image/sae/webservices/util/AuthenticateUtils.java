package fr.urssaf.image.sae.webservices.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Classe d'authentification pour Spring Security<br>
 * 
 * 
 */
public final class AuthenticateUtils {

   private AuthenticateUtils() {

   }

   /**
    * 
    * @param roles
    *           autorisations attribuées à l'identification
    */
   public static void authenticate(String... roles) {

      Authentication authentication = new TestingAuthenticationToken(
            "login_test", "password_test", roles);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   /**
    * 
    * @return liste des rôles du contexte de sécurité
    */
   @SuppressWarnings("unchecked")
   public static List<String> getRoles() {

      return SecurityContextHolder.getContext().getAuthentication() == null ? null
            : new ArrayList(AuthorityUtils
                  .authorityListToSet(SecurityContextHolder.getContext()
                        .getAuthentication().getAuthorities()));

   }
}
