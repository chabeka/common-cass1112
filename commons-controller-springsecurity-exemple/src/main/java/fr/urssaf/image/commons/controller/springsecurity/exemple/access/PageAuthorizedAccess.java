package fr.urssaf.image.commons.controller.springsecurity.exemple.access;

import java.util.Collection;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.controller.springsecurity.exemple.service.AuthorizationService;

public class PageAuthorizedAccess implements AccessDecisionVoter {

   public final AuthorizationService authService;

   public PageAuthorizedAccess(AuthorizationService authService) {

      Assert.notNull(authService, "'authService' is required");
      this.authService = authService;
   }

   @Override
   public boolean supports(ConfigAttribute attribute) {

      return true;
   }

   @Override
   public boolean supports(Class<?> clazz) {

      return FilterInvocation.class.isAssignableFrom(clazz);
   }

   @Override
   public int vote(Authentication authentication, Object object,
         Collection<ConfigAttribute> attributes) {

      Collection<GrantedAuthority> authorities = extractAuthorities(authentication);

      FilterInvocation filter = (FilterInvocation) object;
      String url = filter.getRequestUrl();

      int access = ACCESS_ABSTAIN;

      Boolean authorized = authService.isAuthorized(url, AuthorityUtils
            .authorityListToSet(authorities));
      if (BooleanUtils.isTrue(authorized)) {

         access = ACCESS_GRANTED;

      }

      else if (BooleanUtils.isFalse(authorized)) {

         access = ACCESS_DENIED;
      }

      return access;
   }

   private RoleHierarchy roleHierarchy;

   public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
      this.roleHierarchy = roleHierarchy;
   }

   private Collection<GrantedAuthority> extractAuthorities(Authentication authentication) {

      Collection<GrantedAuthority> authorities;

      if (roleHierarchy == null) {

         authorities = authentication.getAuthorities();

      }

      else {

         authorities = roleHierarchy
               .getReachableGrantedAuthorities(authentication.getAuthorities());
      }

      return authorities;
   }

}
