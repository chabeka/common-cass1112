package fr.urssaf.image.commons.springsecurity.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
      "/applicationContext-security.xml" })
public class SimpleWebServiceTest {

   @Autowired
   private SimpleWebService service;

   public void authenticate(String... roles) {

      Authentication authentication = new AnonymousAuthenticationToken("login",
            "password", AuthorityUtils.createAuthorityList(roles));

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   @Test
   public void saveSuccess() {

      save("ROLE_ADMIN");

   }

   @Test(expected = AccessDeniedException.class)
   public void saveFailure() {

      save("ROLE_USER");
   }

   private void save(String role) {

      authenticate(role);
      service.save("text", "title");
   }

   @Test
   public void loadSuccess() {

      load("ROLE_USER");

   }

   @Test(expected = AccessDeniedException.class)
   public void loadFailure() {

      load("ROLE_ADMIN");
   }

   private void load(String role) {

      authenticate(role);
      service.load();
   }

}
