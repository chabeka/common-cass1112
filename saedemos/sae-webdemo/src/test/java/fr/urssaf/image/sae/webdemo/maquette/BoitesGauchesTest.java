package fr.urssaf.image.sae.webdemo.maquette;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import net.htmlparser.jericho.Source;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import fr.urssaf.image.commons.maquette.MaquetteFilter;
import fr.urssaf.image.sae.vi.service.PortailVIService;
import fr.urssaf.image.sae.webdemo.TestController;
import fr.urssaf.image.sae.webdemo.security.SecurityAuthentication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@SuppressWarnings("PMD")
public class BoitesGauchesTest {

   private MaquetteFilter filtre;

   private FilterChain chain;

   private MockHttpServletResponse response;

   private MockHttpServletRequest request;

   @Autowired
   private ApplicationContext context;

   @Autowired
   private TestController servlet;

   private Source source;

   @Before
   public void init() throws ServletException {

      filtre = new MaquetteFilter();
      chain = new MockFilterChain();

      MockFilterConfig filterConfig = new MockFilterConfig();
      filterConfig.addInitParameter("implementationILeftCol",
            BoitesGauches.class.getName());
      filterConfig.addInitParameter("implementationIMenu", Menus.class
            .getName());

      // Paramétrage
      filtre.init(filterConfig);

      request = new MockHttpServletRequest();
      request.setMethod("POST");
      request.setRequestURI("/accueil.html");
      response = new MockHttpServletResponse();
      response.setContentType("text/html");

   }

   @Test
   public void decorateur_noauthenticate() throws IOException, ServletException {

      filter();

      assertLeftCol();

      assertNullElement(source, "user-name");
      assertNullElement(source, "user-rights");
      assertNullElement(source, "logout-user");
   }

   @Test
   public void decorateur_authenticate() throws Exception {

      authenticate();
      filter();

      assertLeftCol();

      assertElement(source, "AGENT-CTD-SAE-90040 Prenom-90040", "user-name");
      assertElement(source, "ADMINISTRATION TECHNIQUE", "user-rights");

      assertEquals("javascript:document.location.href='logout'", source
            .getElementById("logout-user").getAttributeValue("onclick"));

   }

   private void filter() throws IOException, ServletException {

      filtre.doFilter(request, response, chain);

      source = new Source(response.getContentAsString());
   }

   private void authenticate() throws Exception {

      PortailVIService saePortailViService = new PortailVIService();

      File file = new File("src/test/resources/saml/ctd_rights.xml");
      String xml = FileUtils.readFileToString(file, "UTF-8");

      Authentication authentication = new SecurityAuthentication(
            saePortailViService.lireVI(xml));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      AnnotationMethodHandlerAdapter handlerAdapter = context
            .getBean(AnnotationMethodHandlerAdapter.class);
      handlerAdapter.handle(request, response, servlet);

   }

   private void assertLeftCol() {

      assertElement(source, "SAE - Application web de d&eacute;monstration",
            "app-name");
      assertElement(source, "0.1", "app-version");
   }

   private static void assertElement(Source source, String expected, String id) {

      assertEquals(expected, source.getElementById(id).getContent().toString());
   }

   private static void assertNullElement(Source source, String id) {

      assertNull(id + " ne doit pas être présent", source.getElementById(id));
   }

}
