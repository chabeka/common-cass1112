package fr.urssaf.image.commons.springsecurity.web.authenticate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService implements UserDetailsService{

   private static Map<String, AuthenticateUserDetails> users = new HashMap<String, AuthenticateUserDetails>();

   private static final Logger LOG = Logger
         .getLogger(AuthenticateService.class);

   static {

      put("user", "userpassword", "ROLE_USER");
      put("admin", "adminpassword", "ROLE_ADMIN");
      put("auth", "authpassword", "ROLE_AUTH");

      for (AuthenticateUserDetails user : users.values()) {
         LOG.debug(user.getUsername() + " " + user.getPassword());
      }

   }

   private static void put(String login, String password, String... roles) {

      AuthenticateUserDetails user = new AuthenticateUserDetails(login,
            DigestUtils.md5Hex(password), roles);
      users.put(user.getUsername(), user);
   }

   @Override
   public UserDetails loadUserByUsername(String username)
         throws UsernameNotFoundException, DataAccessException {
      
      AuthenticateUserDetails user = users.get(username);

      if (user == null) {
         throw new UsernameNotFoundException("le compte n'existe pas");
      }

      return user;
   }

}
