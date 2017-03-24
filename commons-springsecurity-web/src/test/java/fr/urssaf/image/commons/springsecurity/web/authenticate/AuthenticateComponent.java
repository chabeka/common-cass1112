package fr.urssaf.image.commons.springsecurity.web.authenticate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public final class AuthenticateComponent {
   
   @Autowired
   private AuthenticateService authenticateService;

   public void authenticate(String username) {

      UserDetails userDetails = authenticateService.loadUserByUsername(username);

      Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails
                  .getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }
}
