package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate.SecurityUser;
import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;

public abstract class AbstractTestFormServiceImpl {

   private FormService service;

   @Autowired
   public void setFormService(@Qualifier("formService") FormService service) {
      this.service = service;
   }

   @Autowired
   private AuthenticateService authenticateService;

   @Test
   public void saveSuccess() {

      save("user");
   }

   @Test(expected = AccessDeniedException.class)
   public void saveFailure() {

      save("auth");
   }

   private void save(String login) {

      SecurityUser securityUser = authenticateService.find(login);

      Authentication authentication = new UsernamePasswordAuthenticationToken(
            securityUser, securityUser.getPassword(), securityUser
                  .getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

      FormFormulaire form = new FormFormulaire();
      form.setText("text");
      form.setTitle("title");

      service.save(form);
   }

   @Test
   public void display() {

      FormFormulaire form = new FormFormulaire();
      form.setText("text");
      form.setTitle("title");

      service.display(form);

   }

   @Test
   public void populate() {

      FormFormulaire form = new FormFormulaire();

      service.populate(form);
   }

   @After
   public void after() {

      SecurityContextHolder.clearContext();
   }
}
