package fr.urssaf.image.commons.maquette;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.fixture.UriPatternTestItem;
import fr.urssaf.image.commons.maquette.fixture.UriPatternTestListe;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.util.JndiSupport;

/**
 * Tests unitaires de la classe {@link MaquetteFilter}
 * 
 */
@SuppressWarnings("PMD")
public class MaquetteFilterTest {

   private final String resultatHtml = "CeciEstUnePageHtml";
   private final String resultatImage = "CeciEstUneImageGif";
   private final String resultatGetRessource = "CeciEstLeGetResource";
   private final String resultatErreur404 = "CeciEstUneErreur404";

   @After
   public void endExec() throws IOException {
      String path = JndiSupport.getFile().getAbsolutePath();
      JndiSupport.getFile().delete();
      File file = new File(path);
      file.createNewFile();
      JndiSupport.setFile(file);
   }

   /**
    * Classe utilitaire pour les tests de la méthode
    * {@link MaquetteFilter#doFilter}
    * 
    */
   private class FilterChainHtml implements FilterChain {

      @Override
      public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
         PrintWriter printWriter = response.getWriter();
         try {
            printWriter.write(resultatHtml);
         } finally {
            printWriter.close();
         }
         response.setContentType("text/html");
      }

   }

   /**
    * Classe utilitaire pour les tests de la méthode
    * {@link MaquetteFilter#doFilter}
    * 
    */
   private class FilterChainImage implements FilterChain {

      @Override
      public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
         PrintWriter printWriter = response.getWriter();
         try {
            printWriter.write(resultatImage);
         } finally {
            printWriter.close();
         }
         response.setContentType("image/gif");
      }

   }

   /**
    * Classe utilitaire pour les tests de la méthode
    * {@link MaquetteFilter#doFilter}
    * 
    */
   private class FilterChainGetRessource implements FilterChain {

      @Override
      public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
         PrintWriter printWriter = response.getWriter();
         try {
            printWriter.write(resultatGetRessource);
         } finally {
            printWriter.close();
         }
         response.setContentType("application/something");
      }

   }

   /**
    * Classe utilitaire pour les tests de la méthode
    * {@link MaquetteFilter#doFilter}
    * 
    */
   private class FilterChainErreur404 implements FilterChain {

      @Override
      public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
         PrintWriter printWriter = response.getWriter();
         try {
            printWriter.write(resultatErreur404);
         } finally {
            printWriter.close();
         }
         response.setContentType("text/html");
         ((HttpServletResponse) response)
               .setStatus(HttpServletResponse.SC_NOT_FOUND);
      }

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteFilter#init(javax.servlet.FilterConfig)}
    * 
    * @throws ServletException
    */
   @Test
   public void initTest() throws ServletException {
      MaquetteFilter filtre = new MaquetteFilter();
      filtre.init(null);
   }

   /**
    * Tests unitaires de {@link MaquetteFilter#getFilterConfig()}
    * 
    * @throws ServletException
    */
   @Test
   public void getFilterConfig() throws ServletException {

      MaquetteFilter filtre = new MaquetteFilter();

      filtre.init(JndiSupport.getFilterConfig());

      FilterConfig filterConfig2 = filtre.getFilterConfig();

      assertEquals("Le getter getFilterConfig() est incorrect", JndiSupport
            .getFilterConfig(), filterConfig2);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#destroy()}<br>
    * <br>
    * On vérifie simplement qu'il n'y ait pas de levée d'exception
    */
   @Test
   public void destroy() {
      MaquetteFilter filtre = new MaquetteFilter();
      filtre.destroy();
   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#isUriDeGetResource}<br>
    * <br>
    * Cas de test : l'URI appelé est <code>/getResourceImageMaquette.do</code><br>
    * <br>
    * Résultat attendu : le filtre doit être appliqué
    */
   @Test
   public void isUriDeGetResource_Test1() {

      MaquetteFilter filtre = new MaquetteFilter();

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI("/" + MaquetteConstant.GETRESOURCEURI);

      Boolean actual = filtre.isUriDeGetResource(request);

      Boolean expected = true;

      assertEquals("Le filtre devrait être appliqué", expected, actual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#isUriDeGetResource}<br>
    * <br>
    * Cas de test : l'URI appelé est <code>/servlet.do</code><br>
    * <br>
    * Résultat attendu : le filtre ne doit pas être appliqué
    */
   @Test
   public void isUriDeGetResource_Test2() {

      MaquetteFilter filtre = new MaquetteFilter();

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI("/servlet.do");

      Boolean actual = filtre.isUriDeGetResource(request);

      Boolean expected = false;

      assertEquals("Le filtre ne devrait être être appliqué", expected, actual);

   }

   /**
    * Tests unitaires de la méthode
    * {@link MaquetteFilter#isUriDansListeExclusions}<br>
    * 
    * @throws ServletException
    */
   @Test
   public void isUriDansListeExclusions() throws ServletException {

      MaquetteFilter filtre = new MaquetteFilter();

      MockHttpServletRequest request = new MockHttpServletRequest();

      // -----------------------------------------------------------------
      // Premiers tests : - un seul pattern dans la liste d'exclusions
      // - une liste de pattern de tests
      // -----------------------------------------------------------------

      // Boucle sur une liste de tests de pattern d'URI
      Boolean bActual;
      String message;
      MockFilterConfig filterConfig;
      List<UriPatternTestItem> listeTests = UriPatternTestListe.getListeTests();
      for (UriPatternTestItem item : listeTests) {

         // Définit la liste d'exclusions
         filterConfig = new MockFilterConfig();
         filterConfig.addInitParameter(ConstantesConfigFiltre.EXCLUDEFILES,
               item.getPattern());
         filtre.init(filterConfig);

         // Définit la request
         request.setRequestURI(item.getUri());

         // Appel de la méthode à tester
         bActual = filtre.isUriDansListeExclusions(request);

         // Vérification du résultat
         if (item.getResultatAttendu()) {
            message = "L'URI devrait être exclue";
         } else {
            message = "L'URI ne devrait pas être exclue";
         }
         message += " (URI=\"%s\", Pattern=\"%s\"";
         message = String.format(message, item.getUri(), item.getPattern());
         assertEquals(message, item.getResultatAttendu(), bActual);

      }

      // -----------------------------------------------------------------
      // 2ème test : plusieurs pattern dans la liste d'exclusions
      // -----------------------------------------------------------------

      String pattern = listeTests.get(3).getPattern() + ";"
            + listeTests.get(7).getPattern() + ";"
            + listeTests.get(8).getPattern() + ";"
            + listeTests.get(9).getPattern();
      String uri = listeTests.get(9).getUri();
      filterConfig = new MockFilterConfig();
      filterConfig.addInitParameter(ConstantesConfigFiltre.EXCLUDEFILES,
            pattern);
      filtre.init(filterConfig);
      request.setRequestURI(uri);
      bActual = filtre.isUriDansListeExclusions(request);
      Boolean bExpected = true;
      message = String.format(
            "L'URI devrait être exclue (URI=\"%s\", Pattern=\"%s\"", uri,
            pattern);
      assertEquals(message, bExpected, bActual);

      // -----------------------------------------------------------------
      // 3ème test : pas de liste d'exclusion
      // -----------------------------------------------------------------

      filterConfig = new MockFilterConfig();
      filtre.init(filterConfig);
      request.setRequestURI("/toto.do");
      bActual = filtre.isUriDansListeExclusions(request);
      bExpected = false;
      assertEquals("L'URI ne devrait pas être exclue (URI=\"/toto.do\")",
            bExpected, bActual);

   }

   /**
    * Tests unitaires de la méthode {@link MaquetteFilter#doitAppliquerFiltre}
    * 
    * @throws ServletException
    */
   @Test
   public void doitAppliquerFiltre() throws ServletException {

      MaquetteFilter filtre = new MaquetteFilter();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockFilterConfig filterConfig;

      Boolean bActual;
      Boolean bExpected;

      // Test 1 : cas standard
      // => le filtre doit être appliqué
      filterConfig = new MockFilterConfig();
      filtre.init(filterConfig);
      request.setRequestURI("/page1.do");
      bActual = filtre.doitAppliquerFiltre(request);
      bExpected = true;
      assertEquals("Le filtre aurait dû être appliqué", bExpected, bActual);

      // Test 2 : L'URI de la requête HTTP est celle de getRessource
      // => le filtre ne doit pas être appliqué
      filterConfig = new MockFilterConfig();
      filtre.init(filterConfig);
      request.setRequestURI("/" + MaquetteConstant.GETRESOURCEURI);
      bActual = filtre.doitAppliquerFiltre(request);
      bExpected = false;
      assertEquals("Le filtre n'aurait pas dû être appliqué", bExpected,
            bActual);

      // Test 3 : L'URI de la requête HTTP fait partie de la liste d'exclusions
      // => le filtre ne doit pas être appliqué
      List<UriPatternTestItem> listeTests = UriPatternTestListe.getListeTests();
      filterConfig = new MockFilterConfig();
      filterConfig.addInitParameter(ConstantesConfigFiltre.EXCLUDEFILES,
            listeTests.get(0).getPattern());
      filtre.init(filterConfig);
      request.setRequestURI(listeTests.get(0).getUri());
      bActual = filtre.doitAppliquerFiltre(request);
      bExpected = false;
      assertEquals("Le filtre n'aurait pas dû être appliqué", bExpected,
            bActual);

      // Test 4 : requête HTTP en Ajax
      // => le filtre ne doit pas être appliqué
      filterConfig = new MockFilterConfig();
      filtre.init(filterConfig);
      request.setRequestURI("/page1.do");
      request.addHeader("X-Requested-With", "XMLHttpRequest");
      bActual = filtre.doitAppliquerFiltre(request);
      bExpected = false;
      assertEquals("Le filtre n'aurait pas dû être appliqué", bExpected,
            bActual);

   }

   /**
    * Tests unitaires de la méthode {@link MaquetteFilter#isReponseDecorable}
    */
   @Test
   public void isReponseDecorable() {

      MaquetteFilter filtre = new MaquetteFilter();

      MockHttpServletResponse response = new MockHttpServletResponse();

      Boolean bExpected;
      Boolean bActual;

      // Test 1 : type MIME non renseigné
      // => non décorable
      response.setContentType(null);
      bActual = filtre.isReponseDecorable(response);
      bExpected = false;
      assertEquals(
            "La réponse HTTP ne devrait pas être décorable (le type MIME est positionné à null)",
            bExpected, bActual);

      // Test 2 : type MIME renseigné, type image
      // => non décorable
      response.setContentType("image/gif");
      bActual = filtre.isReponseDecorable(response);
      bExpected = false;
      assertEquals(
            "La réponse HTTP ne devrait pas être décorable (le type MIME est positionné à image/gif)",
            bExpected, bActual);

      // Test 3 : type MIME renseigné, text/html
      // => décorable
      response.setContentType("text/html");
      bActual = filtre.isReponseDecorable(response);
      bExpected = true;
      assertEquals(
            "La réponse HTTP ne devrait pas être décorable (le type MIME est positionné à text/html)",
            bExpected, bActual);

      // Test 3 : type MIME renseigné, text/plain
      // => décorable
      response.setContentType("text/plain");
      bActual = filtre.isReponseDecorable(response);
      bExpected = true;
      assertEquals(
            "La réponse HTTP ne devrait pas être décorable (le type MIME est positionné à text/plain)",
            bExpected, bActual);

      // Test 4 : type MIME renseigné, text/plain, + charset
      // => décorable
      response.setContentType("text/plain;charset=UTF-8");
      bActual = filtre.isReponseDecorable(response);
      bExpected = true;
      assertEquals(
            "La réponse HTTP ne devrait pas être décorable (le type MIME est positionné à text/plain;charset=UTF-8)",
            bExpected, bActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#doFilter}<br>
    * <br>
    * Cas de test : cas standard, il faut décorer
    * 
    * @throws ServletException
    * @throws IOException
    */
   @Test
   public void doFilter_Test1_Standard() throws ServletException, IOException {

      // Création des objets
      MaquetteFilter filtre = new MaquetteFilter();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain chain = new FilterChainHtml();

      // Paramétrage
      filtre.init(JndiSupport.getFilterConfig());
      request.setRequestURI("/page1.do");

      // Appel de la méthode à tester
      filtre.doFilter(request, response, chain);

      // Vérifie le résultat
      String sNotExpected = resultatHtml;
      String sActual = response.getContentAsString();
      assertFalse("La page aurait dû être décorée", sActual
            .equals(sNotExpected));

   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#doFilter}<br>
    * <br>
    * Cas de test : l'URI demandée correspond à une image, il ne faut pas
    * décorer
    * 
    * @throws ServletException
    * @throws IOException
    */
   @Test
   public void doFilter_Test2_TypeMimeNePasDecorer() throws ServletException,
         IOException {

      // Création des objets
      MaquetteFilter filtre = new MaquetteFilter();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain chain = new FilterChainImage();

      // Paramétrage
      filtre.init(JndiSupport.getFilterConfig());
      request.setRequestURI("/monImage.gif");

      // Appel de la méthode à tester
      filtre.doFilter(request, response, chain);

      // Vérifie le résultat
      String sExpected = resultatImage;
      String sActual = response.getContentAsString();
      assertEquals("L'image n'aurait pas dû être décorée", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#doFilter}<br>
    * <br>
    * Cas de test : l'URI demandée est l'URI du getRessource de la maquette <br>
    * Résultat attendu : pas de décoration
    * 
    * @throws ServletException
    * @throws IOException
    */
   @Test
   public void doFilter_Test3_GetRessourceNePasDecorer()
         throws ServletException, IOException {

      // Création des objets
      MaquetteFilter filtre = new MaquetteFilter();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain chain = new FilterChainGetRessource();

      // Paramétrage
      filtre.init(JndiSupport.getFilterConfig());
      request.setRequestURI("/" + MaquetteConstant.GETRESOURCEURI);

      // Appel de la méthode à tester
      filtre.doFilter(request, response, chain);

      // Vérifie le résultat
      String sExpected = resultatGetRessource;
      String sActual = response.getContentAsString();
      assertEquals("La réponse n'aurait pas dû être décorée", sExpected,
            sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteFilter#doFilter}<br>
    * <br>
    * Cas de test : l'URI demandée provoque une erreur 404 <br>
    * Résultat attendu : décoration
    * 
    * @throws ServletException
    * @throws IOException
    */
   @Test
   public void doFilter_Test4_Erreur404Decorer() throws ServletException,
         IOException {

      // Création des objets
      MaquetteFilter filtre = new MaquetteFilter();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain chain = new FilterChainErreur404();

      // Paramétrage
      filtre.init(JndiSupport.getFilterConfig());
      request.setRequestURI("/unePage.html");

      // Appel de la méthode à tester
      filtre.doFilter(request, response, chain);

      // Vérifie le résultat
      String sNotExpected = resultatHtml;
      String sActual = response.getContentAsString();
      assertFalse("La page aurait dû être décorée", sActual
            .equals(sNotExpected));

   }

}
