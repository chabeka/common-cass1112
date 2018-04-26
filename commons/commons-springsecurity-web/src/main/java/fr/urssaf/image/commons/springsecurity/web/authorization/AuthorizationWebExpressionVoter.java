package fr.urssaf.image.commons.springsecurity.web.authorization;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

public class AuthorizationWebExpressionVoter extends WebExpressionVoter {

   private final DefaultWebSecurityExpressionHandler expressionHandler;

   public AuthorizationWebExpressionVoter() {
      super();
      expressionHandler = new DefaultWebSecurityExpressionHandler();
      this.setExpressionHandler(expressionHandler);
   }

   public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
      expressionHandler.setRoleHierarchy(roleHierarchy);
   }

}
