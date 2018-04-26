package fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.service.AuthenticateService;

@Service
public class SecurityBasicService implements UserDetailsService {

   public final AuthenticateService authService;

   @Autowired
   public SecurityBasicService(AuthenticateService authService) {

      if (authService == null) {
         throw new IllegalStateException("'authService' is required");
      }
      this.authService = authService;
   }

   @Override
   public UserDetails loadUserByUsername(String username)
         throws UsernameNotFoundException, DataAccessException {

      SecurityUser user = authService.find(username);

      if (user == null) {
         throw new UsernameNotFoundException("le compte n'existe pas");
      }

      return user;
   }

}
