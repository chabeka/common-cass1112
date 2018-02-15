package fr.urssaf.image.commons.springsecurity.webservice.authenticate;

import java.util.List;

import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public final class AuthenticationFactory {

   private AuthenticationFactory() {

   }

   public static Authentication createAuthentication(
         WSUsernameTokenPrincipal userPrincipal,
         List<GrantedAuthority> authorities) {

      return new AnonymousAuthenticationToken(userPrincipal.getName(),
            userPrincipal.getPassword(), authorities);
   }
}
