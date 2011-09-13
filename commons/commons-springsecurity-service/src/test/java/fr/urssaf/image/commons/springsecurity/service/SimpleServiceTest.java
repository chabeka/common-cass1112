package fr.urssaf.image.commons.springsecurity.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service.xml",
      "/applicationContext-security.xml" })
public class SimpleServiceTest {

   @Autowired
   private SimpleService service;

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void saveSuccess() {

      authenticate("ROLE_ADMIN");
      save();

   }

   @Test(expected = AccessDeniedException.class)
   public void saveFailure() {

      authenticate("ROLE_USER");
      save();
   }

   private void save() {

      Modele modele = new Modele();
      modele.setText("text");
      modele.setTitle("title");
      service.save(modele);
   }

   private void authenticate(String role) {

      Authentication authentication = new TestingAuthenticationToken(
            "login_test", "password_test", role);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   @Test
   public void loadSuccess() {

      authenticate("ROLE_USER");
      Assert.assertEquals("Monstesquieu",service.load());

   }

   @Test(expected = AccessDeniedException.class)
   public void loadFailure() {

      authenticate("ROLE_AUTH");
      service.load();
   }

}
