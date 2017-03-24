package fr.urssaf.image.commons.controller.springsecurity.exemple.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate.SecurityUser;
import fr.urssaf.image.commons.controller.springsecurity.exemple.service.AuthenticateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class AuthorizeTest {

   @Autowired
   private AuthenticateService authenticateService;

   private MockHttpServletResponse response;

   private MockHttpServletRequest request;

   private DelegatingFilterProxy filtre;

   private static final String PAGE_1 = "/page1.do";

   private static final String PAGE_2 = "/page2.do";

   private static final String PAGE_3 = "/page3.do";

   @Before
   public void before() throws ServletException {

      request = new MockHttpServletRequest();
      request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());
      response = new MockHttpServletResponse();

      ContextLoaderListener listener = new ContextLoaderListener();

      MockServletContext servletContext = new MockServletContext();
      servletContext
            .addInitParameter(
                  ContextLoaderListener.CONFIG_LOCATION_PARAM,
                  "/applicationContext.xml /security/applicationContext-security-aspect.xml /applicationContext-security-basic.xml /spring-servlet-test.xml");

      listener.initWebApplicationContext(servletContext);

      MockFilterConfig filterConfig = new MockFilterConfig(servletContext);

      filtre = new DelegatingFilterProxy();

      filtre.setTargetBeanName("springSecurityFilterChain");
      filtre.init(filterConfig);

   }

   @After
   public void after() {

      SecurityContextHolder.clearContext();
   }

   @Test
   public void page1_authorized() throws Exception {

      this.authorized(PAGE_1, "auth");

   }

   @Test
   public void page2_authorized() throws Exception {

      this.authorized(PAGE_2, "user");

   }

   @Test
   public void page2_denied() throws Exception {

      this.denied(PAGE_2, "auth");

   }

   @Test
   public void page3_authorized() throws Exception {

      this.authorized(PAGE_3, "admin");
   }

   @Test
   public void page3_denied() throws Exception {

      this.denied(PAGE_3, "user");

   }

   private void authorized(String url, String login) throws Exception {

      servlet(url, login);
      assertNull(
            "la redirection doit être vide " + response.getRedirectedUrl(),
            response.getRedirectedUrl());

   }

   private void denied(String url, String login) throws Exception {

      servlet(url, login);
      assertEquals("/accessDenied.do", response.getForwardedUrl());

   }

   private void servlet(String url, String login) throws Exception {

      request.setMethod("GET");
      request.setServletPath(url);

      FilterChain chain = new MockFilterChain();

      SecurityUser securityUser = authenticateService.find(login);

      Authentication authentication = new UsernamePasswordAuthenticationToken(
            securityUser, securityUser.getPassword(), securityUser
                  .getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

      filtre.doFilter(request, response, chain);

   }

}
