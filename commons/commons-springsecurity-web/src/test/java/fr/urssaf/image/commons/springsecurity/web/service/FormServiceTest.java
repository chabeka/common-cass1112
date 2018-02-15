package fr.urssaf.image.commons.springsecurity.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.springsecurity.web.authenticate.AuthenticateComponent;
import fr.urssaf.image.commons.springsecurity.web.form.FormFormulaire;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
      "/applicationContext-security.xml" })
public class FormServiceTest {

   @Autowired
   private FormService service;
   
   @Autowired
   private AuthenticateComponent auth;

   @Test
   public void saveSuccess() {

      auth.authenticate("admin");
      save();

   }

   @Test(expected = AccessDeniedException.class)
   public void saveFailure() {

      auth.authenticate("user");
      save();
   }

   private void save() {

      FormFormulaire form = new FormFormulaire();
      form.setText("text");
      form.setTitle("title");
      
      service.save(form);
   }

   @Test
   public void populateSuccess() {

      auth.authenticate("user");
      populate();

   }

   @Test(expected = AccessDeniedException.class)
   public void populateFailure() {

      auth.authenticate("auth");
      populate();
   }
   
   private void populate() {

      FormFormulaire form = new FormFormulaire();
      service.populate(form);
   }

}
