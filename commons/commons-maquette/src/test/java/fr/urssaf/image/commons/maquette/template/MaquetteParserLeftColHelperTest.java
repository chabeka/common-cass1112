package fr.urssaf.image.commons.maquette.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteConfigException;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingInfoBoxPropertyException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;
import fr.urssaf.image.commons.maquette.fixture.FixtureBoitesDeGauche;
import fr.urssaf.image.commons.maquette.util.JndiSupport;
import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;

/**
 * Tests unitaires de la classe {@link MaquetteParserLeftColHelper}
 * 
 */
@SuppressWarnings("PMD")
public class MaquetteParserLeftColHelperTest {

   @After
   public void endExec() throws IOException {
      String path = JndiSupport.getFile().getAbsolutePath();
      JndiSupport.getFile().delete();
      File file = new File(path);
      file.createNewFile();
      JndiSupport.setFile(file);
   }

   /**
    * Test du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils
            .testConstructeurPriveSansArgument(MaquetteParserLeftColHelper.class);
      assertTrue("Le constructeur privé n'a pas été trouvé", result);
   }

   /**
    * Test unitaire de la méthode {@link MaquetteParserLeftColHelper#build}<br>
    * <br>
    * Cas de test : l'implémentation des boîtes de gauche n'est pas fourni<br>
    * <br>
    * Résultat attendu : pas d'exception, le rendu HTML de la partie gauche est
    * vide
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingInfoBoxPropertyException
    */
   @Test
   public void buildLeftCol_CasSansImplementation()
         throws MaquetteConfigException, MaquetteThemeException, IOException,
         MissingSourceParserException, MissingInfoBoxPropertyException,
         MissingHtmlElementInTemplateParserException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test03.html";

      // La configuration de la maquette
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Création d'un objet MaquetteParser pour récupérer
      // les objets Jericho
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      MaquetteParserLeftColHelper.build(parser.getOutputDocument(),
            maquetteCfg, parser.getHtmlSrcFromTmpl(), request);

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore.append("avant" + "\r\n");
      sbDecore.append("<div id=\"leftcol\"></div>" + "\r\n");
      sbDecore.append("apres");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement des boites de gauche", sExpected,
            sActual);

   }

   /**
    * Test unitaire de la méthode {@link MaquetteParserLeftColHelper#build}<br>
    * <br>
    * Cas de test : Cas normal<br>
    * <br>
    * Résultat attendu : pas d'exception, le rendu HTML est correct
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    * @throws MissingSourceParserException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingInfoBoxPropertyException
    */
   @Test
   public void buildLeftCol_CasNormal() throws MaquetteConfigException,
         MaquetteThemeException, IOException, MissingSourceParserException,
         MissingInfoBoxPropertyException,
         MissingHtmlElementInTemplateParserException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      // Le HTML de test, généré par l'application métier
      StringBuffer sbHtmlClient = new StringBuffer();
      String htmlClient = sbHtmlClient.toString();

      // Le chemin dans le JAR vers le template HTML
      String cheminTemplate = "/resource/html/main_test03.html";

      // Paramétrage de la request pour obtenir des boîtes de gauche
      request.addHeader(
            FixtureBoitesDeGauche.REQUEST_HEADER_POUR_AVOIR_DES_BOITES, "1");

      // La configuration de la maquette
      FileUtils
            .writeStringToFile(
                  JndiSupport.getFile(),
                  ConstantesConfigFiltre.IMPL_LEFTCOL
                        + "=fr.urssaf.image.commons.maquette.fixture.FixtureBoitesDeGauche");
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());
      MaquetteConfig maquetteCfg = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Création d'un objet MaquetteParser pour récupérer
      // les objets Jericho
      MaquetteParser parser = new MaquetteParser(htmlClient, cheminTemplate,
            request, maquetteCfg);

      // Appel de la méthode à tester
      MaquetteParserLeftColHelper.build(parser.getOutputDocument(),
            maquetteCfg, parser.getHtmlSrcFromTmpl(), request);

      // ------------------------------------------------------------------------
      // Vérifie le résultat
      // ------------------------------------------------------------------------

      // System.out.println(parser.getOutputDocument().toString());

      // Construction du HTML attendu
      StringBuilder sbDecore = new StringBuilder();
      sbDecore.append("avant" + "\r\n");
      sbDecore
            .append("<div id=\"leftcol\"><h3 class=\"boxTitle\" id=\"app-title\">Application</h3>"
                  + "\r\n");
      sbDecore
            .append("<p class=\"boxContent\" id=\"app\" title=\"Informations relatives &Atilde;&nbsp; l'application courante\">"
                  + "\r\n");
      sbDecore
            .append("<span id=\"app-name\" title=\"Nom de l'application\">nom appli &lt;&Atilde;&copy;&gt;</span><br />"
                  + "\r\n");
      sbDecore
            .append("<span id=\"app-version\" title=\"Version de l'application\">0.1a &lt;&Atilde;&nbsp;&gt;</span><br />"
                  + "\r\n");
      sbDecore.append("</p>" + "\r\n");
      sbDecore
            .append("<h3 class=\"boxTitle\" id=\"user-title\">Utilisateur</h3>"
                  + "\r\n");
      sbDecore
            .append("<p class=\"boxContent\" id=\"user\" title=\"Informations relatives &Atilde;&nbsp; l'utilisateur identifi&Atilde;&copy;\">"
                  + "\r\n");
      sbDecore
            .append("<span id=\"user-name\" title=\"Pr&Atilde;&copy;nom Nom de l'utilisateur\">Utilisateur 1 &lt;&Atilde;&copy;&gt;</span><br />"
                  + "\r\n");
      sbDecore
            .append("<span id=\"user-rights\" title=\"Droits affect&Atilde;&copy;s &Atilde;&nbsp; l'utilisateur\">Consultation &lt;&Atilde;&nbsp;&gt;</span><br />"
                  + "\r\n");
      sbDecore.append("</p>" + "\r\n");
      sbDecore
            .append("<h3 class=\"boxTitle\" id=\"logout-title\">D&Atilde;&copy;connexion</h3>"
                  + "\r\n");
      sbDecore
            .append("<p class=\"boxContent\" id=\"logout\" title=\"Bo&Atilde;&reg;te de d&Atilde;&copy;connexion\">"
                  + "\r\n");
      sbDecore
            .append("<input id=\"logout-user\" class=\"logout-user\" type=\"button\" value=\"D&eacute;connexion\" onclick=\"deconnexion.do\" tabindex=\"0\" /></p>"
                  + "\r\n");
      sbDecore
            .append("<h3 class=\"boxTitle\" id=\"boiteGauchePerso1-title\">Title Bo&Atilde;&reg;te 1</h3>"
                  + "\r\n");
      sbDecore
            .append("<p class=\"boxContent\" id=\"boiteGauchePerso1\" title=\"Desc Bo&Atilde;&reg;te 1\">"
                  + "\r\n");
      sbDecore.append("contenu 1</p>" + "\r\n");
      sbDecore
            .append("<h3 class=\"boxTitle\" id=\"boiteGauchePerso2-title\">Title Bo&Atilde;&reg;te 2</h3>"
                  + "\r\n");
      sbDecore
            .append("<p class=\"boxContent\" id=\"boiteGauchePerso2\" title=\"Desc Bo&Atilde;&reg;te 2\">"
                  + "\r\n");
      sbDecore.append("contenu 2</p>" + "\r\n");
      sbDecore.append("</div>" + "\r\n");
      sbDecore.append("apres");

      // Vérification
      String sExpected = sbDecore.toString();
      String sActual = parser.getOutputDocument().toString();
      assertEquals("Erreur lors du traitement des boites de gauche", sExpected,
            sActual);

   }

}
