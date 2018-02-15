package fr.urssaf.image.commons.springsecurity.web.authenticate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class AuthenticateServiceTest {

   @Autowired
   private AuthenticateService service;

   @Test
   public void loadUserByUsernameSuccess() {

      UserDetails user = service.loadUserByUsername("admin");
      assertEquals("admin", user.getUsername());
      assertTrue(user.isAccountNonExpired());
      assertTrue(user.isAccountNonLocked());
      assertTrue(user.isCredentialsNonExpired());
      assertTrue(user.isEnabled());

   }
   
   
   @Test(expected=UsernameNotFoundException.class)
   public void loadUserByUsernameFailure() {

      service.loadUserByUsername("inconnu");
      

   }
}
