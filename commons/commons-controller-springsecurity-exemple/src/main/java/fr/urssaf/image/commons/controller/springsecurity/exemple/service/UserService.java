package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate.SecurityUser;

@Service
public class UserService {

   private static Map<String, SecurityUser> users = new HashMap<String, SecurityUser>();
   
   private static final Logger LOG = Logger.getLogger(UserEncodedService.class);

   static {

      SecurityUser user1 = new SecurityUser("user", "userpassword");
      users.put(user1.getUsername(), user1);

      SecurityUser user2 = new SecurityUser("admin", "adminpassword");
      users.put(user2.getUsername(), user2);

      for (SecurityUser user : users.values()) {
         LOG.debug(user.getUsername() + " " + user.getPassword());
      }
   }

   public SecurityUser findByLoginPasswd(String login, String password) {

      return users.get(login);
   }

}
