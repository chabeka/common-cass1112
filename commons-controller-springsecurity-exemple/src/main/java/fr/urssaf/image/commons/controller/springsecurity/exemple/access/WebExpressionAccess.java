package fr.urssaf.image.commons.controller.springsecurity.exemple.access;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

public class WebExpressionAccess extends WebExpressionVoter {

   private final DefaultWebSecurityExpressionHandler expressionHandler;

   public WebExpressionAccess() {
      super();
      expressionHandler = new DefaultWebSecurityExpressionHandler();
      this.setExpressionHandler(expressionHandler);
   }

   public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
      expressionHandler.setRoleHierarchy(roleHierarchy);
   }

}
