package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import fr.urssaf.image.commons.controller.springsecurity.exemple.modele.Role;

@Service
public class AuthorizationService {

   private static Map<String, Role[]> pages = new HashMap<String, Role[]>();

   private static final Logger LOG = Logger
         .getLogger(AuthorizationService.class);

   static {

      pages.put("/page1*", null);
      pages.put("/page2*", new Role[] { Role.role_user });
      pages.put("/page3*", new Role[] { Role.role_admin });

      for (String page : pages.keySet()) {

         LOG.debug(page + " "
               + ArrayUtils.toString(pages.get(page), "sans authorisation"));
      }

   }

   private final PathMatcher urlMatcher;

   public AuthorizationService() {
      urlMatcher = new AntPathMatcher();
   }

   public Boolean isAuthorized(String url, Collection<String> roles) {

      Boolean authorized = null;
      for (String page : pages.keySet()) {

         if (urlMatcher.match(page, url)) {

            Role[] authorizedRoles = pages.get(page);
            authorized = isAuthorized(authorizedRoles, roles);

            break;
         }
      }

      return authorized;
   }

   private Boolean isAuthorized(Role[] authorizedRoles, Collection<String> roles) {

      Boolean authorized = null;

      if (authorizedRoles != null) {

         for (Role authorizedRole : authorizedRoles) {
            authorized = false;
            if (roles.contains(authorizedRole.getAuthority())) {
               authorized = true;
               break;
            }

         }

      }

      return authorized;
   }
}
