package fr.urssaf.image.sae.webservices;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SaeServiceTest {

   @Autowired
   private SaeService service;

   @Test
   public void ping() {

      assertEquals("Test du ping", "Les services SAE sont en ligne", service
            .ping());
   }

   @Test
   public void pingSecure_success() {

      authenticate("ROLE_TOUS");

      assertEquals("Test du ping sécurisé",
            "Les services du SAE sécurisés par authentification sont en ligne",
            service.pingSecure());
   }

   @Test(expected = AccessDeniedException.class)
   public void pingSecure_failure() {

      authenticate("ROLE_OTHER");

      service.pingSecure();
   }

   private static void authenticate(String... roles) {

      Authentication authentication = new TestingAuthenticationToken(
            "login_test", "password_test", roles);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }
}
