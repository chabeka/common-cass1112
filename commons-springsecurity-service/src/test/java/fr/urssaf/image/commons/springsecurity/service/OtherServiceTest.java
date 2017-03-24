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
      "/applicationContext-authorization.xml" })
public class OtherServiceTest {

   @Autowired
   private OtherService service;

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void saveSuccess() {

      authenticate("ROLE_AUTH");
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
      modele.setTitle("Montesquieu");
      service.save(modele);
   }

   private void authenticate(String role) {

      Authentication authentication = new TestingAuthenticationToken(
            "login_test", "password_test", role);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   @Test
   public void loadMontesquieuSuccess() {

      authenticate("ROLE_AUTH");
      Assert.assertEquals("Montesquieu", service.load(0).getTitle());

   }

   @Test(expected = AccessDeniedException.class)
   public void loadMontesquieuFailure() {

      authenticate("ROLE_USER");
      service.load(0);

   }

   @Test
   public void loadConradSuccess() {

      authenticate("ROLE_USER");
      Assert.assertEquals("Conrad", service.load(1).getTitle());
   }

   @Test(expected = AccessDeniedException.class)
   public void loadConradFailure() {

      authenticate("ROLE_AUTH");
      service.load(1);
   }

}
