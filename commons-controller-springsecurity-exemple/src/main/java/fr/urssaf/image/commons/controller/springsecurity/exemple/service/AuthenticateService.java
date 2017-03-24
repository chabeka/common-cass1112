package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate.SecurityUser;
import fr.urssaf.image.commons.controller.springsecurity.exemple.modele.Role;

@Service
public class AuthenticateService {

   private static Map<String, SecurityUser> users = new HashMap<String, SecurityUser>();

   private static final Logger LOG = Logger
         .getLogger(AuthenticateService.class);

   static {

      SecurityUser user1 = new SecurityUser("user", DigestUtils
            .md5Hex("userpassword"), Role.role_user.getAuthority());
      users.put(user1.getUsername(), user1);

      SecurityUser user2 = new SecurityUser("admin", DigestUtils
            .md5Hex("adminpassword"), Role.role_admin.getAuthority());
      users.put(user2.getUsername(), user2);

      SecurityUser user3 = new SecurityUser("auth", DigestUtils
            .md5Hex("authpassword"), Role.role_auth.getAuthority());
      users.put(user3.getUsername(), user3);

      SecurityUser user4 = new SecurityUser("users", DigestUtils
            .md5Hex("userpassword"), Role.role_user.getAuthority(),
            Role.role_user2.getAuthority());
      users.put(user4.getUsername(), user4);

      for (SecurityUser user : users.values()) {
         LOG.debug(user.getUsername() + " " + user.getPassword());
      }

   }

   public SecurityUser find(String login) {

      return users.get(login);
   }

}
