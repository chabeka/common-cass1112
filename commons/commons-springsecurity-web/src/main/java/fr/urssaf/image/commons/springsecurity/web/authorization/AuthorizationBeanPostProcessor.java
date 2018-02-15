package fr.urssaf.image.commons.springsecurity.web.authorization;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

public class AuthorizationBeanPostProcessor implements BeanPostProcessor {

   private RoleHierarchy roleHierarchy;

   public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
      this.roleHierarchy = roleHierarchy;
   }

   @Override
   public Object postProcessAfterInitialization(Object bean, String beanName)
         throws BeansException {
      if (bean instanceof DefaultWebSecurityExpressionHandler) {
         ((DefaultWebSecurityExpressionHandler) bean)
               .setRoleHierarchy(roleHierarchy);

      }
      return bean;

   }

   public Object postProcessBeforeInitialization(Object bean, String name) {
      return bean;
   }

}
