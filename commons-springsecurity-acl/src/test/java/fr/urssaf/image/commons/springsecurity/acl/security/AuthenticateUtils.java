package fr.urssaf.image.commons.springsecurity.acl.security;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthenticateUtils {

   private AuthenticateUtils() {

   }

   public static void authenticateReader() {
      authenticate("ROLE_READER");
   }

   public static void authenticateAuthor() {
      authenticate("ROLE_AUTHOR");
   }

   public static void authenticateEditor() {
      authenticate("ROLE_EDITOR");
   }

   public static void authenticate(String role) {

      Authentication authentication = new TestingAuthenticationToken(
            "login_test", "password_test", role);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   public static void authenticate(int identifiant, String role) {

      Authentication authentication = new TestingAuthenticationToken(
            identifiant, "password_test", role);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   public static void logout() {
      SecurityContextHolder.getContext().setAuthentication(null);
   }
}
