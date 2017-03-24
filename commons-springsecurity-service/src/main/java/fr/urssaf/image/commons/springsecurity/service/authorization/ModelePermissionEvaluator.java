package fr.urssaf.image.commons.springsecurity.service.authorization;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

@Component
public class ModelePermissionEvaluator implements PermissionEvaluator {

   private static final Logger LOG = Logger
         .getLogger(ModelePermissionEvaluator.class);

   @Override
   public boolean hasPermission(Authentication authentication,
         Object domain, Object permission) {

      LOG.debug("Authentication:" + authentication);
      LOG.debug("domain:" + domain.getClass());
      LOG.debug("permission:" + permission);

      boolean hasPermission = false;

      if (AuthorityUtils.authorityListToSet(authentication.getAuthorities())
            .contains(permission)

            && "Montesquieu".equals(((Modele) domain).getTitle())) {

         hasPermission = true;

      } else if (AuthorityUtils.authorityListToSet(
            authentication.getAuthorities()).contains("ROLE_USER")
            && "Conrad".equals(((Modele) domain).getTitle())) {

         hasPermission = true;

      }

      return hasPermission;
   }

   @Override
   public boolean hasPermission(Authentication authentication,
         Serializable targetId, String targetType, Object permission) {
      // TODO Auto-generated method stub
      return false;
   }

}
