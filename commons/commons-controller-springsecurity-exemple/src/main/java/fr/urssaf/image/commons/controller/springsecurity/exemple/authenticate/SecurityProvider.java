package fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class SecurityProvider extends AbstractUserDetailsAuthenticationProvider {

   private final SecurityService userService;

   protected SecurityProvider(SecurityService userService) {
      super();
      Assert.notNull(userService, "'userService' is required");

      this.userService = userService;
   }

   @Override
   protected void additionalAuthenticationChecks(UserDetails userDetails,
         UsernamePasswordAuthenticationToken authentication)
         throws AuthenticationException {

      String password = DigestUtils.md5Hex((String) authentication
            .getCredentials());

      if (!StringUtils.equals(userDetails.getPassword(), password)) {
         throw new BadCredentialsException(messages.getMessage(
               "AbstractUserDetailsAuthenticationProvider.badCredentials",
               "Bad credentials"));
      }

   }

   @Override
   protected UserDetails retrieveUser(String username,
         UsernamePasswordAuthenticationToken authentication)
         throws AuthenticationException {

      SecurityUser user = userService.findByLoginPasswd((String) authentication
            .getPrincipal(), (String) authentication.getCredentials());

      return user;
   }

}
