package fr.urssaf.image.sae.webdemo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;

import fr.urssaf.image.sae.webdemo.ControllerTestSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext/applicationContext-security-test.xml")
@SuppressWarnings("PMD")
public class AuthenticationFailureControllerTest extends
      ControllerTestSupport<AuthenticationFailureController> {

   @Autowired
   private AuthenticationFailureController servlet;

   @Test
   public void post() throws Exception {

      try {
         this.initPost();
         this.handle(servlet);
         fail("le test doit lever une exception IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         assertEquals(HttpRequestMethodNotSupportedException.class, e
               .getCause().getClass());
      }
   }

   @Test
   public void get_exception() {

      this.initGet();

      ModelAndView view = this.handle(servlet);
      assertEquals("error/erreur403_pasauthentifie", view.getViewName());
      assertEquals(403, this.getStatut());

   }

   @Test
   public void get_authenticateException() {

      int errorStatus = 404;
      String errorView = "error_view";

      SecurityException authException = new SecurityException("error_msg",
            errorView, errorStatus);

      authException.getExtraInformation().put("service", "/toto.html");

      this.setSession(WebAttributes.AUTHENTICATION_EXCEPTION, authException);

      this.initGet();

      ModelAndView view = this.handle(servlet);
      assertEquals(errorView, view.getViewName());
      assertEquals(errorStatus, this.getStatut());

   }

}
