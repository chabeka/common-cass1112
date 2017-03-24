package fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.controller.springsecurity.exemple.service.AuthenticateService;

@Service
public class SecurityService {

   public final AuthenticateService authService;

   @Autowired
   public SecurityService(AuthenticateService authService) {

      Assert.notNull(authService, "'authService' is required");
      this.authService = authService;
   }

   public SecurityUser findByLoginPasswd(String login, String password) {

      SecurityUser user = authService.find(login);

      if (user == null) {
         throw new UsernameNotFoundException("le compte n'existe pas");
      }

      return user;
   }

}
