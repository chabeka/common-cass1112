package fr.urssaf.image.sae.webdemo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext/applicationContext-security-test.xml")
@SuppressWarnings("PMD")
public class SecurityFilterTest {

   @Autowired
   private SecurityFilter filtre;

   private FilterChain chain;

   private MockHttpServletResponse response;

   private MockHttpServletRequest request;

   @Before
   public void before() throws ServletException {

      chain = new MockFilterChain();

      request = new MockHttpServletRequest();

      response = new MockHttpServletResponse();
      response.setContentType("text/html");

      MockFilterConfig filterConfig = new MockFilterConfig();
      // Paramétrage
      filtre.init(filterConfig);

   }

   @Test
   public void securitySuccess_1() throws Exception {

      this.securitySuccess(AuthenticationControllerTest.RELAY_VALUE);

   }

   @Test
   public void securitySuccess_2() throws Exception {

      this.securitySuccess("/");

   }

   private void securitySuccess(String relay) throws IOException,
         ServletException {

      String samlValue = FileUtils.readFileToString(new File(
            "src/test/resources/saml/ctd_rights.xml"), "UTF-8");

      String idSession = request.getSession().getId();
      
      authenticate(samlValue, relay);

      assertEquals(relay, response.getRedirectedUrl());

      SecurityAuthentication authenticate = (SecurityAuthentication) SecurityContextHolder
            .getContext().getAuthentication();

      assertEquals("AGENT-CTD-SAE-90040 Prenom-90040", authenticate.getPrincipal());
      
      assertEquals("ADMINISTRATION TECHNIQUE", authenticate.getCredentials());
      
      assertNotSame("le session doit être invalidée",idSession,request.getSession().getId());

   }

   @Test
   public void securityFailure_SAML() throws Exception {

      String samlValue = FileUtils.readFileToString(new File(
            "src/test/resources/saml/ctd_0_right.xml"), "UTF-8");

      authenticate(samlValue, AuthenticationControllerTest.RELAY_VALUE);

      this.securityFailure(403, "error/erreur403_viformatko");

   }

   @Test
   public void securityFailure_Service() throws IOException, ServletException {

      String samlValue = FileUtils.readFileToString(new File(
            "src/test/resources/saml/ctd_rights.xml"), "UTF-8");

      authenticate(samlValue, "\toto.html");

      this.securityFailure(404, "error/erreur404_serviceinexistant");

   }

   @Test
   public void securityFailure_empty_SAML() throws IOException,
         ServletException {

      request.getSession().setAttribute(
            AuthenticationControllerTest.RELAY_FIELD,
            AuthenticationControllerTest.RELAY_VALUE);
      request.setRequestURI("/authenticate_check.html");

      filtre.doFilter(request, response, chain);
      this.securityFailure(403, "error/erreur403_viko");

   }

   @Test
   public void securityFailure_empty_RELAY() throws IOException,
         ServletException {

      String samlValue = FileUtils.readFileToString(new File(
            "src/test/resources/saml/ctd_rights.xml"), "UTF-8");

      request.getSession().setAttribute(
            AuthenticationControllerTest.SAML_FIELD, samlValue);

      request.setRequestURI("/authenticate_check.html");

      filtre.doFilter(request, response, chain);
      this.securityFailure(403, "error/erreur403_viko");

   }

   private void securityFailure(int status, String view) throws IOException,
         ServletException {

      assertEquals("/authenticationFailure.html", response.getRedirectedUrl());

      SecurityException exception = (SecurityException) request.getSession()
            .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      assertEquals(status, exception.getStatus());
      assertEquals(view, exception.getView());

      assertNull(SecurityContextHolder.getContext().getAuthentication());

      assertNull(request.getSession().getAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
   }

   private void authenticate(String samlValue, String relayValue)
         throws IOException, ServletException {

      request.getSession().setAttribute(
            AuthenticationControllerTest.SAML_FIELD, samlValue);
      request.getSession().setAttribute(
            AuthenticationControllerTest.RELAY_FIELD, relayValue);
      request.setRequestURI("/authenticate_check.html");

      filtre.doFilter(request, response, chain);
   }

}
