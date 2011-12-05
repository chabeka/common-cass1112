package fr.urssaf.image.sae.webdemo.maquette;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import net.htmlparser.jericho.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.maquette.MaquetteFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@SuppressWarnings("PMD")
public class MenusTest {

   private MaquetteFilter filtre;

   private FilterChain chain;

   private MockHttpServletResponse response;

   private MockHttpServletRequest request;

   private Source source;

   @Before
   public void init() throws ServletException {

      filtre = new MaquetteFilter();
      chain = new MockFilterChain();

      MockFilterConfig filterConfig = new MockFilterConfig();
      filterConfig.addInitParameter("implementationIMenu", Menus.class
            .getName());

      // Paramétrage
      filtre.init(filterConfig);

      request = new MockHttpServletRequest();

      response = new MockHttpServletResponse();
      response.setContentType("text/html");

   }

   @Test
   public void menu_accueil() throws Exception {

      request.setServletPath("/accueil.html");

      filter();

      assertBreadCrumb("menu.accueil&nbsp;");

   }

   @Test
   public void menu_registre_exploitation() throws Exception {

      request.setServletPath("/registre_exploitation.html");

      filter();

      assertBreadCrumb("menu.trace &gt; menu.regExpl&nbsp;");

   }

   @Test
   public void menu_applicationDemo() throws Exception {

      request.setServletPath("/applicationDemo.html");

      filter();

      assertBreadCrumb("&nbsp;");

   }

   private void filter() throws IOException, ServletException {

      filtre.doFilter(request, response, chain);

      source = new Source(response.getContentAsString());
   }

   private void assertBreadCrumb(String menu) {

      assertElement(source, menu, "pagereminder");

   }

   private static void assertElement(Source source, String expected, String id) {

      assertEquals(expected, source.getElementById(id).getContent().toString());
   }

}
