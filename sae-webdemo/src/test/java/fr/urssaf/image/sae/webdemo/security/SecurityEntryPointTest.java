package fr.urssaf.image.sae.webdemo.security;

import static org.junit.Assert.assertEquals;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

@SuppressWarnings("PMD")
public class SecurityEntryPointTest {

   private MockHttpServletResponse response;

   private MockHttpServletRequest request;

   private MockServletContext servletContext;

   private ContextLoaderListener listener;

   @Before
   public void before() throws ServletException {

      listener = new ContextLoaderListener();
      servletContext = new MockServletContext();

      request = new MockHttpServletRequest();

      response = new MockHttpServletResponse();
      response.setContentType("text/html");

   }

   @Test(expected = BeanCreationException.class)
   public void entry_failureURL() {

      this
            .beanException("/applicationContext/applicationContext-security-failureURL-test.xml");
   }

   @Test(expected = BeanCreationException.class)
   public void entry_authenticateURL() {

      this
            .beanException("/applicationContext/applicationContext-security-authenticateURL-test.xml");
   }

   private void beanException(String contextFile) {

      servletContext.addInitParameter(
            ContextLoaderListener.CONFIG_LOCATION_PARAM, contextFile);

      listener.initWebApplicationContext(servletContext);
   }

   @Test
   public void entryPOST() throws Exception {

      this.entry("POST", "/applicationDemo.html");
      assertEquals("/authentication.html", response.getForwardedUrl());

   }

   @Test
   public void entryPOST_failure() throws Exception {

      this.entry("POST", "/accueil.html");
      assertEquals("/authenticationFailure.html", response.getRedirectedUrl());

   }

   @Test
   public void entryGET_failure() throws Exception {

      this.entry("GET", "/applicationDemo.html");
      assertEquals("/authenticationFailure.html", response.getRedirectedUrl());

   }

   private void entry(String method, String url) throws Exception {

      ContextLoaderListener listener = new ContextLoaderListener();

      MockServletContext servletContext = new MockServletContext();
      servletContext.addInitParameter(
            ContextLoaderListener.CONFIG_LOCATION_PARAM,
            "/applicationContext/applicationContext-security-test.xml");

      listener.initWebApplicationContext(servletContext);

      MockFilterConfig filterConfig = new MockFilterConfig(servletContext);

      DelegatingFilterProxy filtre = new DelegatingFilterProxy();

      filtre.setTargetBeanName("springSecurityFilterChain");
      filtre.init(filterConfig);

      FilterChain chain = new MockFilterChain();

      request.setMethod(method);
      request.setParameter("SAMLReponse", "aa");
      request.setParameter("RelayState", "aa");
      request.setServletPath(url);

      filtre.doFilter(request, response, chain);

   }

}
