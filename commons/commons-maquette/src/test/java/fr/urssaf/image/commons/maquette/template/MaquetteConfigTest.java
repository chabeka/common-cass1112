package fr.urssaf.image.commons.maquette.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteConfigException;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.fixture.FixtureMenu;
import fr.urssaf.image.commons.maquette.util.JndiSupport;

/**
 * Tests unitaires de la classe {@link MaquetteConfig}
 * 
 */
@SuppressWarnings("PMD")
public class MaquetteConfigTest {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(MaquetteConfigTest.class);
   
   @After
   public void endExec() throws IOException {
      String path = JndiSupport.getFile().getAbsolutePath();
      JndiSupport.getFile().delete();
      File file = new File(path);
      file.createNewFile();
      JndiSupport.setFile(file);
   }

   /**
    * Test du constructeur et des getters, cas normal
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    */
   @Test
   public void constructeurEtGetters() throws MaquetteConfigException,
         MaquetteThemeException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      MaquetteConfig maquetteConfig = new MaquetteConfig(request,
            maquetteFilterConfig);

      // Vérifie la configuration initiale

      assertFalse(
            "Erreur dans la configuration initiale pour internetExplorer",
            maquetteConfig.isInternetExplorer());

      assertNotNull(
            "Erreur dans la configuration initiale pour configDuFiltre",
            maquetteConfig.getConfigDuFiltre());

   }

   /**
    * Vérifie la détection d'Internet Explorer
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    */
   @Test
   public void testInternetExplorer6() throws MaquetteConfigException,
         MaquetteThemeException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.addHeader("User-Agent", "MSIE 6.0");
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      MaquetteConfig maquetteConfig = new MaquetteConfig(request,
            maquetteFilterConfig);

      assertTrue("Erreur dans la configuration la détection d'IE6",
            maquetteConfig.isInternetExplorer());

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteConfig#getMenu(javax.servlet.http.HttpServletRequest)}<br>
    * <br>
    * Cas de test : l'implémentation du menu n'est pas fourni<br>
    * <br>
    * Résultat attendu : le rendu HTML du menu est vide
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MenuException
    */
   @Test
   public void getMenuSansImplementationMenu() throws MaquetteConfigException,
         MaquetteThemeException, MenuException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      MaquetteConfig maquetteConfig = new MaquetteConfig(request,
            maquetteFilterConfig);

      String html = maquetteConfig.getMenu(request);

      assertEquals("Le rendu HTML du menu est incorrect", "", html);

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteConfig#getMenu(javax.servlet.http.HttpServletRequest)}<br>
    * <br>
    * Cas de test : cas normal<br>
    * <br>
    * Résultat attendu : le rendu HTML du menu n'est pas vide
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MenuException
    * @throws IOException
    */
   @Test
   public void getMenuCasNormal() throws MaquetteConfigException,
         MaquetteThemeException, MenuException, IOException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      FileUtils.writeStringToFile(JndiSupport.getFile(), ConstantesConfigFiltre.IMPL_MENU
            + "=fr.urssaf.image.commons.maquette.fixture.FixtureMenu");

      request.addHeader(FixtureMenu.REQUEST_HEADER_POUR_AVOIR_UN_MENU, "1");

      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      MaquetteConfig maquetteConfig = new MaquetteConfig(request,
            maquetteFilterConfig);

      String html = maquetteConfig.getMenu(request);

      LOGGER.debug(String.format("Menu généré : %s", html));

      assertFalse("Le rendu HTML du menu est vide", StringUtils.isEmpty(html));

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteConfig#getMenu(javax.servlet.http.HttpServletRequest)}<br>
    * <br>
    * Cas de test : l'implémentation du menu est fourni, mais elle retourne un
    * menu vide<br>
    * <br>
    * Résultat attendu : le rendu HTML du menu est vide
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws MenuException
    * @throws IOException
    */
   @Test
   public void getMenuCasMenuVide() throws MaquetteConfigException,
         MaquetteThemeException, MenuException, IOException {

      FileUtils.writeStringToFile(JndiSupport.getFile(), ConstantesConfigFiltre.IMPL_MENU
            + "=fr.urssaf.image.commons.maquette.fixture.FixtureMenu");

      MockHttpServletRequest request = new MockHttpServletRequest();

      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      MaquetteConfig maquetteConfig = new MaquetteConfig(request,
            maquetteFilterConfig);

      String html = maquetteConfig.getMenu(request);

      assertEquals("Le rendu HTML du menu est incorrect", "", html);

   }

   /**
    * Cas où l'implémentation du menu n'est pas instantiable par la maquette<br>
    * <br>
    * Résultat attendu : une exception doit être levée
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test(expected = MaquetteConfigException.class)
   public void testCasAvecImplementationMenuIncorrecte()
         throws MaquetteConfigException, MaquetteThemeException, IOException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      // Objects Mock
      FileUtils.writeStringToFile(JndiSupport.getFile(), ConstantesConfigFiltre.IMPL_MENU
            + "=ImplementationInexistante");

      // Création de l'objet MaquetteFilterConfig
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      // Instantiation de l'objet à tester
      new MaquetteConfig(request, maquetteFilterConfig);

   }

   /**
    * Cas où l'implémentation des boîtes de gauche n'est pas instantiable par la
    * maquette<br>
    * <br>
    * Résultat attendu : une exception doit être levée
    * 
    * @throws MaquetteConfigException
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test(expected = MaquetteConfigException.class)
   public void testCasAvecImplementationBoiteGaucheIncorrecte()
         throws MaquetteConfigException, MaquetteThemeException, IOException {

      MockHttpServletRequest request = new MockHttpServletRequest();
      // Objects Mock
      FileUtils.writeStringToFile(JndiSupport.getFile(), ConstantesConfigFiltre.IMPL_MENU
            + "=fr.urssaf.image.commons.maquette.fixture.FixtureMenu");
      FileUtils.writeStringToFile(JndiSupport.getFile(), ConstantesConfigFiltre.IMPL_LEFTCOL
            + "=ImplementationInexistante");

      // Création de l'objet MaquetteFilterConfig
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(
            JndiSupport.getFilterConfig());

      // Instantiation de l'objet à tester
      new MaquetteConfig(request, maquetteFilterConfig);

   }

}