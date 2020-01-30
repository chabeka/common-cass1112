package fr.urssaf.image.commons.maquette;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteConfigException;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.exception.RessourceNonSpecifieeException;
import fr.urssaf.image.commons.maquette.exception.RessourceNonTrouveeException;
import fr.urssaf.image.commons.maquette.session.SessionTools;
import fr.urssaf.image.commons.maquette.theme.AbstractMaquetteTheme;
import fr.urssaf.image.commons.maquette.theme.MaquetteThemeTools;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.util.JndiSupport;
import fr.urssaf.image.commons.maquette.util.ResourceUtil;

/**
 * Tests unitaires de la classe {@link MaquetteServlet}
 */
@SuppressWarnings("PMD")
public class MaquetteServletTest {

  @After
  public void endExec() throws IOException {
    final String path = JndiSupport.getFile().getAbsolutePath();
    JndiSupport.getFile().delete();
    final File file = new File(path);
    file.createNewFile();
    JndiSupport.setFile(file);
  }

  /**
   * Test de la méthode {@link MaquetteServlet#doGet}<br>
   * <br>
   * Cas de test : le paramètre "name" n'est pas envoyé à la requête<br>
   * <br>
   * Résultat attendu : levée d'une exception
   * {@link RessourceNonSpecifieeException}
   * 
   * @throws IOException
   *           en cas d'erreur d'E/S
   */
  @Test(expected = RessourceNonSpecifieeException.class)
  public void doGet_RessourceNonSpecifiee1() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    // Création des objets Mock
    request = new MockHttpServletRequest();
    final MockHttpServletResponse response = new MockHttpServletResponse();

    // Création de l'objet Servlet
    final MaquetteServlet servlet = new MaquetteServlet();

    // Appel de la servlet
    servlet.doGet(request, response);

    // Si l'exception n'est pas levée, le test sera en échec

  }

  /**
   * Test de la méthode {@link MaquetteServlet#doGet}<br>
   * <br>
   * Cas de test : le paramètre "name" est envoyée à la requête, avec la valeur
   * <code>null</code><br>
   * <br>
   * Résultat attendu : levée d'une exception
   * {@link RessourceNonSpecifieeException}
   * 
   * @throws IOException
   *           en cas d'erreur d'E/S
   */
  @Test(expected = RessourceNonSpecifieeException.class)
  public void doGet_RessourceNonSpecifiee2() throws IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    // Création des objets Mock
    request = new MockHttpServletRequest();
    final MockHttpServletResponse response = new MockHttpServletResponse();

    // Création de l'objet Servlet
    final MaquetteServlet servlet = new MaquetteServlet();

    // Ajoute à la Request d'un nom de ressource inexistant
    final String nomRessource = null;
    request.addParameter("name", nomRessource);

    // Appel de la servlet
    servlet.doGet(request, response);

    // Si l'exception attendue n'est pas levée, le test passera en échec

  }

  /**
   * Test de la méthode {@link MaquetteServlet#doGet}<br>
   * <br>
   * Cas de test : le paramètre "name" contient un nom de ressource inexistante<br>
   * <br>
   * Résultat attendu : levée d'une exception
   * {@link RessourceNonTrouveeException} avec le nom de la ressource demandée
   * 
   * @throws ServletException
   *           exception levée par
   *           {@link javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)}
   * @throws IOException
   *           en cas d'erreur d'E/S
   */
  @Test
  public void doGet_RessourceNonTrouveeException() throws ServletException,
      IOException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    // Création des objets Mock
    final MockServletConfig config = new MockServletConfig();
    request = new MockHttpServletRequest();
    final MockHttpServletResponse response = new MockHttpServletResponse();

    // Création de l'objet Servlet
    final MaquetteServlet servlet = new MaquetteServlet();
    servlet.init(config);

    // Ajoute à la Request d'un nom de ressource inexistant
    final String nomRessource = "toto";
    request.addParameter("name", nomRessource);

    // Appel de la servlet. On attend une RessourceNonTrouveeException
    try {
      servlet.doGet(request, response);
    }
    catch (final RessourceNonTrouveeException ex) {

      // Vérifie l'exception
      final String expected = nomRessource;
      final String actual = ex.getNomRessource();
      assertEquals("L'exception levée n'est pas correcte", expected, actual);

      // Vérifie que la réponse soit vide
      // On vérifie que rien n'a été écrit dans la réponse
      // D'abord la taille
      final int iExpected = 0;
      final int iActual = response.getContentLength();
      assertEquals("L'objet HttpServletResponse n'est pas vide !",
                   iExpected,
                   iActual);
      // Puis le contenu
      final String sExpected = "";
      final String sActual = response.getContentAsString();
      assertEquals("L'objet HttpServletResponse n'est pas vide !",
                   sExpected,
                   sActual);

      // Si on arrive jusque là, le test est OK
      return;

    }

    // Si on arrive jusque là, c'est que l'exception n'a pas été levée
    // Le test est donc en échec
    fail("L'exception attendue RessourceNonTrouveeException n'a pas été levée");

  }

  /**
   * Test de la méthode {@link ExtJsServlet#doGet}<br>
   * <br>
   * Cas de test :<br>
   * - le paramètre "name" contient le bon nom de ressource<br>
   * - la ressource n'est pas une CSS dont il faut remplacer les valeurs<br>
   * <br>
   * Résultat attendu : la ressource est écrite dans la
   * <code>HttpServletResponse</code>
   * 
   * @throws ServletException
   *           exception levée par
   *           {@link javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)}
   * @throws IOException
   *           en cas d'erreur d'E/S
   * @throws URISyntaxException
   *           exception levée par {@link #lectureFichierRessource}
   */
  @Test
  public void doGet_CasStandard() throws ServletException, IOException,
      URISyntaxException {

    // Création des objets Mock
    final MockServletConfig config = new MockServletConfig();
    final MockHttpServletRequest request = new MockHttpServletRequest();
    final MockHttpServletResponse response = new MockHttpServletResponse();

    // Création de l'objet Servlet
    final MaquetteServlet servlet = new MaquetteServlet();
    servlet.init(config);

    // Ajoute à la Request du nom de la ressource à récupérer
    final String nomRessource = "/resource/img/pixel_aed.png";
    request.addParameter("name", nomRessource);

    // Appel de la servlet
    servlet.doGet(request, response);

    // Vérifie le type MIME de la réponse
    String sExpected = "application/octet-stream";
    String sActual = response.getContentType();
    assertEquals("Le type MIME est incorrecte", sExpected, sActual);

    // Charge le fichier image
    final byte[] contenuImage = lectureFichierRessource(nomRessource);

    // Vérifie la taille de la réponse
    final int iExpected = contenuImage.length;
    final int iActual = response.getContentLength();
    assertEquals(
                 "La ressource n'a pas été correctement écrite dans l'objet HttpServletResponse",
                 iExpected,
                 iActual);

    // Vérifie le contenu de la réponse
    sExpected = new String(contenuImage, CharEncoding.ISO_8859_1);
    sActual = response.getContentAsString();
    assertEquals(
                 "La ressource n'a pas été correctement écrite dans l'objet HttpServletResponse",
                 sExpected,
                 sActual);

  }

  /**
   * Test de la méthode {@link ExtJsServlet#doGet}<br>
   * <br>
   * Cas de test :<br>
   * - le paramètre "name" contient le bon nom de ressource<br>
   * - la ressource est une CSS dont il faut remplacer les valeurs<br>
   * <br>
   * Résultat attendu : la ressource est écrite dans la
   * <code>HttpServletResponse</code> et les CSS ont bien été remplacés.<br>
   * En fait, on ne vérifie pas les CSS, cela est fait dans un autre test
   * 
   * @throws ServletException
   * @throws IOException
   * @throws URISyntaxException
   * @throws MaquetteThemeException
   * @throws MaquetteConfigException
   */
  @Test
  public void doGet_RessourceCssAvecRemplacement() throws ServletException,
      IOException, URISyntaxException, MaquetteConfigException,
      MaquetteThemeException {

    // Création des objets Mock
    final MockServletConfig config = new MockServletConfig();
    final MockHttpServletRequest request = new MockHttpServletRequest();
    final MockHttpServletResponse response = new MockHttpServletResponse();

    // Création de l'objet Servlet
    final MaquetteServlet servlet = new MaquetteServlet();
    servlet.init(config);

    // Ajoute à la Request du nom de la ressource à récupérer
    final String nomRessource = "/resource/css/main-color.css";
    request.addParameter("name", nomRessource);

    // Ajoute à la request le paramètre indiquant qu'il faut remplacer
    // le contenu de la CSS par les valeurs du thème
    request.addParameter("overload", "1");

    // Met en session la configuration de la maquette
    final MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
                                                                               JndiSupport.getFilterConfig());
    SessionTools.storeFilterConfig(request, maquetteFilterConfig);

    // Appel de la servlet
    servlet.doGet(request, response);

    // Vérifie le type MIME de la réponse
    final String sExpected = "text/css";
    final String sActual = response.getContentType();
    assertEquals("Le type MIME est incorrecte", sExpected, sActual);

    // NB : les remplacements dans le CSS sont vérifiés dans un autre test

  }

  /**
   * Charge la ressource dans un tableau d'octets
   * 
   * @param nomRessource
   *          le nom de la resource
   * @return le contenu de la ressource dans un tableau d'octets
   * @throws URISyntaxException
   *           exception levée par
   *           {@link ResourceUtil#getResourceFullPath(Object, String)}
   * @throws IOException
   *           exception levée par {@link FileInputStream}
   */
  private byte[] lectureFichierRessource(final String nomRessource)
      throws URISyntaxException, IOException {

    // Récupère le chemin complet de la ressource
    final String cheminComplet = ResourceUtil.getResourceFullPath(this,
                                                                  nomRessource);

    // Lit la ressource dans un tableau d'octets
    final File file = new File(cheminComplet);
    final FileInputStream fileInputStream = new FileInputStream(file);
    try {
      return IOUtils.toByteArray(fileInputStream);
    }
    finally {
      fileInputStream.close();
    }

  }

  /**
   * Test unitaire de la méthode {@link MaquetteServlet#printCssAvecModifTheme}<br>
   * <br>
   * On créé un CSS de test en y mettant tous les mots-clés qui sont censés
   * être remplacés par les valeurs du thème en cours. Puis on appelle la
   * méthode à tester, et on vérifie que les remplacements ont bien été
   * effectués. *
   * 
   * @throws ServletException
   * @throws IOException
   * @throws MaquetteThemeException
   */
  @Test
  public void printCssAvecModifTheme() throws ServletException, IOException,
      MaquetteThemeException {

    // Création des objets Mock
    final MockServletConfig config = new MockServletConfig();
    final MockHttpServletResponse response = new MockHttpServletResponse();

    // Création de l'objet Servlet
    final MaquetteServlet servlet = new MaquetteServlet();
    servlet.init(config);

    // Récupération du thème par défaut
    final AbstractMaquetteTheme theme = MaquetteThemeTools.getTheme(null);

    // Préparation du CSS de test

    final StringBuffer sbCssContent = new StringBuffer();

    sbCssContent.append(String.format("balise01 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSMAINBACKGROUNDCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise02 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSCONTENTBACKGROUNDCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise03 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSHEADERBACKGROUNDCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise04 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSHEADERBACKGROUNDIMG));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise05 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSLEFTCOLORBACKGROUNDIMG));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise06 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSMAINFONTCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise07 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSCONTENTFONTCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise08 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSINFOBOXBACKGROUNDCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise09 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSSELECTEDMENUBACKGROUNDCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise10 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSMENUFIRSTROWFONTCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise11 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSMENULINKFONTCOLOR));
    sbCssContent.append("\r\n");

    sbCssContent.append(String.format("balise12 {attribut: %s;}",
                                      ConstantesConfigFiltre.CSSMENULINKHOVERFONTCOLOR));
    sbCssContent.append("\r\n");

    // Chargement du CSS dans un inputStream
    final ByteArrayInputStream inputStreamCss = new ByteArrayInputStream(
                                                                         sbCssContent.toString().getBytes(CharEncoding.UTF_8));
    try {

      // Appel de la méthode à tester
      servlet.printCssAvecModifTheme(response, inputStreamCss, theme);

      // Vérification le CSS résultat

      final StringBuffer sbResultatAttendu = new StringBuffer();

      sbResultatAttendu.append(String.format("balise01 {attribut: %s;}",
                                             "#A6A9CA"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise02 {attribut: %s;}",
                                             "#fff"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise03 {attribut: %s;}",
                                             "#051A7D"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise04 {attribut: %s;}",
                                             MaquetteConstant.GETRESOURCEURI
                                                 + "?name=/resource/img/degrade_h_aed.png"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise05 {attribut: %s;}",
                                             MaquetteConstant.GETRESOURCEURI
                                                 + "?name=/resource/img/leftcol_aed.png"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise06 {attribut: %s;}",
                                             "#fff"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise07 {attribut: %s;}",
                                             "#000"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise08 {attribut: %s;}",
                                             "#EAEAEF"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise09 {attribut: %s;}",
                                             "#05577D"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise10 {attribut: %s;}",
                                             "#000"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise11 {attribut: %s;}",
                                             "#000"));
      sbResultatAttendu.append("\r\n");

      sbResultatAttendu.append(String.format("balise12 {attribut: %s;}",
                                             "#000"));
      sbResultatAttendu.append("\r\n");

      final String sExpected = sbResultatAttendu.toString();
      final String sActual = response.getContentAsString();

      assertEquals(
                   "Erreur lors du remplacement des CSS par les valeurs du thème",
                   sExpected,
                   sActual);

      // Vérifie le type MIME
      assertEquals("Type MIME incorrect",
                   "text/css",
                   response
                           .getContentType());

    }
    finally {
      inputStreamCss.close();
    }

  }

}
