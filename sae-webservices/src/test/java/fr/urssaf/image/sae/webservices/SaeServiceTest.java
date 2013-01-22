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

import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

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

   private static void authenticate(String... roles) {

      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("TU");
      extrait.setIdUtilisateur("login_test");
      SaeDroits droits = new SaeDroits();
      extrait.setSaeDroits(droits);

      Authentication authentication = new TestingAuthenticationToken(extrait,
            "password_test", roles);

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }
}
