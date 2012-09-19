package fr.urssaf.image.commons.maquette.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteConfigException;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;
import fr.urssaf.image.commons.maquette.fixture.FixtureMenu;
import fr.urssaf.image.commons.maquette.session.SessionTools;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.tool.MenuItem;

/**
 * Tests unitaires de la classe {@link MaquetteParser}
 * 
 */
@SuppressWarnings("PMD")
public class MaquetteParserTest {

   static private File file;
   static private SimpleNamingContextBuilder builder;
   static private MockHttpServletRequest request;
   static private MockFilterConfig filterConfig;

   @BeforeClass
   public static void init() throws IOException, IllegalStateException,
         NamingException {

      builder = new SimpleNamingContextBuilder();

      file = File.createTempFile("confFile_success", ".properties");
      builder.bind("java:comp/env/confFile", file.getAbsolutePath());
      builder.activate();

      request = new MockHttpServletRequest();
      filterConfig = new MockFilterConfig();
      filterConfig.addInitParameter("fichierProprietes", "confFile");
   }

   @AfterClass
   public static void end() {
      builder.clear();
      builder.deactivate();
      file.delete();
   }

   @After
   public void endExec() throws IOException {
      String path = file.getAbsolutePath();
      file.delete();
      file = new File(path);
      file.createNewFile();
   }

   /**
    * Test du constructeur<br>
    * <br>
    * Cas de test : cas normal
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws URISyntaxException
    * @throws NamingException
    */
   @Test
   public void constructeur_CasNormal() throws IOException,
         MaquetteConfigException, MaquetteThemeException, URISyntaxException,
         NamingException {

      // Le HTML
      String html = "<html></html>";

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = MaquetteConstant.CHEMIN_TMPL_MAIN_HTML;

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(html, cheminTemplate, request,
            maquetteCfg);

      // Vérifications
      assertNotNull("L'objet MaquetteParser n'a pas été instancié", parser);
      assertNotNull("Le document HTML généré par le parser est null", parser
            .getOutputDocument());

   }

   /**
    * Test du constructeur<br>
    * <br>
    * Cas de test : le chemin vers le template html passé au constructeur est
    * incorrect<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    */
   @Test
   public void constructeur_CasTemplateInexistant() throws IOException,
         MaquetteConfigException, MaquetteThemeException {

      // Le HTML
      String html = "<html></html>";

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/titi";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      try {
         new MaquetteParser(html, cheminTemplate, request, maquetteCfg);
      } catch (IOException ex) {

         // Vérifie le message de l'exception
         assertEquals("Le message de l'exception levée est incorrect", String
               .format("Le template suivant ne semble pas accessible : %s",
                     cheminTemplate), ex.getMessage());

         // Si on arrive jusque là, le test est OK
         return;

      }

      // Si on arrive jusque là, c'est que l'exception attendue n'a
      // pas été levée
      fail("L'exception attendue n'a pas été levée");

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildHead()}<br>
    * <br>
    * Cas de test : cas normal<br>
    * <br>
    * Résultats attendus :
    * <ul>
    * <li>les head sont bien mergés</li>
    * <li>le title de l'application cliente est bien intégré</li>
    * </ul>
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    */
   @Test
   public void buildHead_CasNormal() throws IOException,
         MaquetteConfigException, MaquetteThemeException,
         MissingSourceParserException,
         MissingHtmlElementInTemplateParserException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      sbHtmlClient.append("<html>" + "\r\n");
      sbHtmlClient.append("<head>" + "\r\n");
      sbHtmlClient.append("<title>Titre par l'application metier</title>"
            + "\r\n");
      sbHtmlClient
            .append("<link href=\"CssAppMetier.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbHtmlClient
            .append("<script type=\"text/javascript\" src=\"JsAppMetier.js\">Js de l'application metier</script>"
                  + "\r\n");
      sbHtmlClient.append("</head>" + "\r\n");
      sbHtmlClient.append("<body>" + "\r\n");
      sbHtmlClient.append("Contenu du body" + "\r\n");
      sbHtmlClient.append("</body>" + "\r\n");
      sbHtmlClient.append("</html>");
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test01.html";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Décoration de la partie HEAD
      parser.buildHead();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      // De la maquette
      sbDecore
            .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
                  + "\r\n");
      sbDecore.append("<html>" + "\r\n");
      sbDecore.append("<head>" + "\r\n");
      sbDecore.append("" + "\r\n"); // <= Suppression de la balise <title>
      sbDecore
            .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"
                  + "\r\n");
      sbDecore
            .append("<link href=\"CssMaquette.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbDecore
            .append("<script type=\"text/javascript\" src=\"JsMaquette.js\">Js de la maquette</script>"
                  + "\r\n");
      // De l'application cliente
      sbDecore.append("\r\n");
      sbDecore.append("<title>Titre par l'application metier</title>" + "\r\n");
      sbDecore
            .append("<link href=\"CssAppMetier.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbDecore
            .append("<script type=\"text/javascript\" src=\"JsAppMetier.js\">Js de l'application metier</script>"
                  + "\r\n");
      // De la maquette
      sbDecore.append("</head>" + "\r\n");
      sbDecore.append("<body>" + "\r\n");
      sbDecore.append("<p>Contenu du template de la maquette</p>" + "\r\n");
      sbDecore.append("</body>" + "\r\n");
      sbDecore.append("</html>");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement du <head>", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildHead()}<br>
    * <br>
    * Cas de test : pas de &lt;head&gt; dans l'application métier<br>
    * <br>
    * Résultats attendus :
    * <ul>
    * <li>pas d'exception levée</li>
    * <li>le rendu HTML est correct</li>
    * </ul>
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    */
   @Test
   public void buildHead_PasDeHeadDansApplicationMetier() throws IOException,
         MaquetteConfigException, MaquetteThemeException,
         MissingSourceParserException,
         MissingHtmlElementInTemplateParserException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      sbHtmlClient.append("<html>" + "\r\n");
      sbHtmlClient.append("<body>" + "\r\n");
      sbHtmlClient.append("Contenu du body" + "\r\n");
      sbHtmlClient.append("</body>" + "\r\n");
      sbHtmlClient.append("</html>");
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test01.html";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Décoration de la partie HEAD
      parser.buildHead();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore
            .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
                  + "\r\n");
      sbDecore.append("<html>" + "\r\n");
      sbDecore.append("<head>" + "\r\n");
      sbDecore.append("<title></title>" + "\r\n");
      sbDecore
            .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"
                  + "\r\n");
      sbDecore
            .append("<link href=\"CssMaquette.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbDecore
            .append("<script type=\"text/javascript\" src=\"JsMaquette.js\">Js de la maquette</script>"
                  + "\r\n");
      sbDecore.append("</head>" + "\r\n");
      sbDecore.append("<body>" + "\r\n");
      sbDecore.append("<p>Contenu du template de la maquette</p>" + "\r\n");
      sbDecore.append("</body>" + "\r\n");
      sbDecore.append("</html>");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement du <head>", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildHead()}<br>
    * <br>
    * Cas de test : pas de &lt;title&gt; dans le &lt;head&gt; de l'application
    * métier<br>
    * <br>
    * Résultats attendus :
    * <ul>
    * <li>pas d'exception levée</li>
    * <li>le rendu HTML est correct</li>
    * </ul>
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    */
   @Test
   public void buildHead_PasDeTitleDansApplicationMetier() throws IOException,
         MaquetteConfigException, MaquetteThemeException,
         MissingSourceParserException,
         MissingHtmlElementInTemplateParserException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      sbHtmlClient.append("<html>" + "\r\n");
      sbHtmlClient.append("<head>" + "\r\n");
      sbHtmlClient
            .append("<link href=\"CssAppMetier.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbHtmlClient
            .append("<script type=\"text/javascript\" src=\"JsAppMetier.js\">Js de l'application metier</script>"
                  + "\r\n");
      sbHtmlClient.append("</head>" + "\r\n");
      sbHtmlClient.append("<body>" + "\r\n");
      sbHtmlClient.append("Contenu du body" + "\r\n");
      sbHtmlClient.append("</body>" + "\r\n");
      sbHtmlClient.append("</html>");
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test01.html";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Décoration de la partie HEAD
      parser.buildHead();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      // De la maquette
      sbDecore
            .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
                  + "\r\n");
      sbDecore.append("<html>" + "\r\n");
      sbDecore.append("<head>" + "\r\n");
      sbDecore.append("<title></title>" + "\r\n");
      sbDecore
            .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"
                  + "\r\n");
      sbDecore
            .append("<link href=\"CssMaquette.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbDecore
            .append("<script type=\"text/javascript\" src=\"JsMaquette.js\">Js de la maquette</script>"
                  + "\r\n");
      // De l'application cliente
      sbDecore.append("\r\n");
      sbDecore
            .append("<link href=\"CssAppMetier.css\" rel=\"stylesheet\" type=\"text/css\" />"
                  + "\r\n");
      sbDecore
            .append("<script type=\"text/javascript\" src=\"JsAppMetier.js\">Js de l'application metier</script>"
                  + "\r\n");
      // De la maquette
      sbDecore.append("</head>" + "\r\n");
      sbDecore.append("<body>" + "\r\n");
      sbDecore.append("<p>Contenu du template de la maquette</p>" + "\r\n");
      sbDecore.append("</body>" + "\r\n");
      sbDecore.append("</html>");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement du <head>", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildDivHeader()}<br>
    * <br>
    * Cas de test : cas normal, avec un navigateur MSIE 6.0<br>
    * <br>
    * Résultat attendu : pas d'exception, le rendu HTML est correct
    * 
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MenuException
    */
   @Test
   public void buildDivHeader_CasNormal() throws MaquetteConfigException,
         MaquetteThemeException, IOException, MissingSourceParserException,
         MissingHtmlElementInTemplateParserException, MenuException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test02.html";

      // Paramétrage de la request
      request.addHeader("User-Agent", "MSIE 6.0");
      request.addHeader(FixtureMenu.REQUEST_HEADER_POUR_AVOIR_UN_MENU, "1");

      // La configuration de la maquette
      List<String> lines = new ArrayList<String>();
      lines.add(ConstantesConfigFiltre.APPTITLE
            + "=Titre de l'application <\\u00e9>");
      lines.add(ConstantesConfigFiltre.MAINLOGO + "=/mainlogo.png");
      lines.add(ConstantesConfigFiltre.APPLOGO + "=/applogo.png");
      lines.add(ConstantesConfigFiltre.IMPL_MENU
            + "=fr.urssaf.image.commons.maquette.fixture.FixtureMenu");
      FileUtils.writeLines(file, lines);

      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Décoration de la partie HEAD
      parser.buildDivHeader();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore.append("avant" + "\r\n");
      sbDecore.append("<div id=\"header\">" + "\r\n");
      sbDecore
            .append("<h1 id=\"title-app\" title=\"Titre de l'application &lt;&eacute;&gt;\" >Titre de l'application &lt;&eacute;&gt;</h1>"
                  + "\r\n");
      sbDecore
            .append("<img id=\"logoimage\" src=\"/mainlogo.png\" />" + "\r\n");
      sbDecore
            .append("<img id=\"logoappli\" src=\"/applogo.png\" style='height:50px'  />"
                  + "\r\n");
      sbDecore
            .append("<img id=\"minheight\" src=\"/bidon.png\" style='height:50px'  />"
                  + "\r\n");
      sbDecore
            .append("<div id=\"menu\"><ul><li><a href='LeLink' class='firstrow' title='Description' tabindex='0'>Titre</a></li></ul></div>"
                  + "\r\n");
      sbDecore.append("</div>" + "\r\n");
      sbDecore.append("apres");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement de la div header", sExpected,
            sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildBodyGetFilAriane()}<br>
    * <br>
    * Cas de test : l'implémentation du menu est fourni, et un fil d'ariane est
    * renvoyé contextuellement à la request HTTP<br>
    * <br>
    * Résultat attendu : le fil d'ariane est correct
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test
   public void buildBodyGetFilAriane_Contextuel()
         throws MaquetteConfigException, MaquetteThemeException, IOException {

      // Le HTML de test, généré par l'application métier
      // aucun intérêt pour ce test
      StringBuffer sbHtmlClient = new StringBuffer();
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      // aucun intérêt pour ce test
      String cheminTemplate = "/resource/html/main_test01.html";

      // Paramétrage de la request
      request.addHeader(FixtureMenu.REQUEST_HEADER_POUR_AVOIR_UN_FIL_ARIANE,
            "1");

      // Configuration de la maquette
      FileUtils.writeStringToFile(file, ConstantesConfigFiltre.IMPL_MENU
            + "=fr.urssaf.image.commons.maquette.fixture.FixtureMenu");
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Construction de l'objet à tester
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      String filAriane = parser.buildBodyGetFilAriane();

      // Vérification
      String sActual = filAriane;
      String sExpected = FixtureMenu.FIL_ARIANE_TEST;
      assertEquals("Erreur dans la récupération du fil d'ariane", sExpected,
            sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildBodyGetFilAriane()}<br>
    * <br>
    * Cas de test : l'implémentation du menu n'est pas fourni <br>
    * Résultat attendu : le fil d'ariane est vide
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test
   public void buildBodyGetFilAriane_SansImplementationMenu()
         throws MaquetteConfigException, MaquetteThemeException, IOException {

      // Le HTML de test, généré par l'application métier
      // aucun intérêt pour ce test
      StringBuffer sbHtmlClient = new StringBuffer();
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      // aucun intérêt pour ce test
      String cheminTemplate = "/resource/html/main_test01.html";

      // Configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Construction de l'objet à tester
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      String filAriane = parser.buildBodyGetFilAriane();

      // Vérification
      String sActual = filAriane;
      String sExpected = "";
      assertEquals("Erreur dans la récupération du fil d'ariane", sExpected,
            sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildBodyGetFilAriane()}<br>
    * <br>
    * Cas de test : l'implémentation du menu est pas fourni, mais pas de fil
    * d'ariane contextuel. Par contre, il y a un menu en cours <br>
    * Résultat attendu : le fil d'ariane correspond au menu en cours
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test
   public void buildBodyGetFilAriane_MenuSelectionne()
         throws MaquetteConfigException, MaquetteThemeException, IOException {

      // Le HTML de test, généré par l'application métier
      // aucun intérêt pour ce test
      StringBuffer sbHtmlClient = new StringBuffer();
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      // aucun intérêt pour ce test
      String cheminTemplate = "/resource/html/main_test01.html";

      // Configuration de la maquette
      filterConfig.addInitParameter(ConstantesConfigFiltre.IMPL_MENU,
            "fr.urssaf.image.commons.maquette.fixture.FixtureMenu");
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Paramétrage de la request
      request.setRequestURI(FixtureMenu.LINK_MENU_POUR_TEST);
      request.addHeader(FixtureMenu.REQUEST_HEADER_POUR_AVOIR_UN_MENU, "1");

      // Construction de l'objet à tester
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Mémorisation du menu en cours
      MenuItem selectedMenu = new FixtureMenu().getMenu(request).get(0);
      SessionTools.storeSelectedMenu(request, selectedMenu);

      // Appel de la méthode à tester
      String filAriane = parser.buildBodyGetFilAriane();
      
      SessionTools.storeSelectedMenu(request, null);
      
      // Vérification
      String sActual = filAriane;
      String sExpected = FixtureMenu.TITRE_MENU_POUR_TEST;
      assertEquals("Erreur dans la récupération du fil d'ariane", sExpected,
            sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildBody()}<br>
    * <br>
    * Cas de test : cas normal<br>
    * <br>
    * Résultat attendu : le rendu html est correct
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    */
   @Test
   public void buildBody_CasNormal() throws IOException,
         MaquetteConfigException, MaquetteThemeException,
         MissingSourceParserException,
         MissingHtmlElementInTemplateParserException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      sbHtmlClient.append("<html>" + "\r\n");
      sbHtmlClient.append("<body>" + "\r\n");
      sbHtmlClient.append("<p>Contenu du body</p>" + "\r\n");
      sbHtmlClient.append("</body>" + "\r\n");
      sbHtmlClient.append("</html>");
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test04.html";

      // Paramétrage de la request
      request.addHeader(FixtureMenu.REQUEST_HEADER_POUR_AVOIR_UN_FIL_ARIANE,
            "1");

      // La configuration de la maquette
      FileUtils.writeStringToFile(file, ConstantesConfigFiltre.IMPL_MENU +
            "=fr.urssaf.image.commons.maquette.fixture.FixtureMenu");
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      parser.buildBody();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore.append("<html>" + "\r\n");
      sbDecore.append("<head>" + "\r\n");
      sbDecore.append("</head>" + "\r\n");
      sbDecore.append("<body>" + "\r\n");
      sbDecore.append("avant" + "\r\n");
      sbDecore
            .append("<div id=\"pagereminder\">LeFilArianeContextuelALaRequest&nbsp;</div>"
                  + "\r\n");
      sbDecore.append("<div id=\"content-application\">" + "\r\n");
      sbDecore.append("<p>Contenu du body</p>" + "\r\n");
      sbDecore.append("<noscript>" + "\r\n");
      sbDecore.append("balise noscript" + "\r\n");
      sbDecore.append("</noscript></div>" + "\r\n");
      sbDecore.append("apres" + "\r\n");
      sbDecore.append("</body>" + "\r\n");
      sbDecore.append("</html>");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement du body", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildBody()}<br>
    * <br>
    * Cas de test : pas de fil d'ariane<br>
    * <br>
    * Résultat attendu : le rendu html est correct
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    */
   @Test
   public void buildBody_SansFilAriane() throws IOException,
         MaquetteConfigException, MaquetteThemeException,
         MissingSourceParserException,
         MissingHtmlElementInTemplateParserException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      sbHtmlClient.append("<html>" + "\r\n");
      sbHtmlClient.append("<body>" + "\r\n");
      sbHtmlClient.append("<p>Contenu du body</p>" + "\r\n");
      sbHtmlClient.append("</body>" + "\r\n");
      sbHtmlClient.append("</html>");
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test04.html";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      parser.buildBody();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore.append("<html>" + "\r\n");
      sbDecore.append("<head>" + "\r\n");
      sbDecore.append("</head>" + "\r\n");
      sbDecore.append("<body>" + "\r\n");
      sbDecore.append("avant" + "\r\n");
      sbDecore.append("<div id=\"pagereminder\">&nbsp;</div>" + "\r\n");
      sbDecore.append("<div id=\"content-application\">" + "\r\n");
      sbDecore.append("<p>Contenu du body</p>" + "\r\n");
      sbDecore.append("<noscript>" + "\r\n");
      sbDecore.append("balise noscript" + "\r\n");
      sbDecore.append("</noscript></div>" + "\r\n");
      sbDecore.append("apres" + "\r\n");
      sbDecore.append("</body>" + "\r\n");
      sbDecore.append("</html>");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement du body", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParser#buildBody()}<br>
    * <br>
    * Cas de test : balise noscript présente dans l'application métier<br>
    * <br>
    * Résultat attendu : le rendu html est correct
    * 
    * @throws IOException
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    */
   @Test
   public void buildBody_NoScriptDansAppMetier() throws IOException,
         MaquetteConfigException, MaquetteThemeException,
         MissingSourceParserException,
         MissingHtmlElementInTemplateParserException {

      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      sbHtmlClient.append("<html>" + "\r\n");
      sbHtmlClient.append("<body>" + "\r\n");
      sbHtmlClient.append("<p>Contenu du body</p>" + "\r\n");
      sbHtmlClient.append("<noscript>noscript dans app metier</noscript>"
            + "\r\n");
      sbHtmlClient.append("</body>" + "\r\n");
      sbHtmlClient.append("</html>");
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test04.html";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            filterConfig);
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Appel du constructeur
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      parser.buildBody();

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore.append("<html>" + "\r\n");
      sbDecore.append("<head>" + "\r\n");
      sbDecore.append("</head>" + "\r\n");
      sbDecore.append("<body>" + "\r\n");
      sbDecore.append("avant" + "\r\n");
      sbDecore.append("<div id=\"pagereminder\">&nbsp;</div>" + "\r\n");
      sbDecore.append("<div id=\"content-application\">" + "\r\n");
      sbDecore.append("<p>Contenu du body</p>" + "\r\n");
      sbDecore.append("<noscript>noscript dans app metier</noscript>" + "\r\n");
      sbDecore.append("</div>" + "\r\n");
      sbDecore.append("apres" + "\r\n");
      sbDecore.append("</body>" + "\r\n");
      sbDecore.append("</html>");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement du body", sExpected, sActual);

   }

}
