package fr.urssaf.image.commons.maquette.theme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;

import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;

/**
 * Tests unitaires de la classe {@link MaquetteThemeTools}
 * 
 */
@SuppressWarnings("PMD")
public class MaquetteThemeToolsTest {

   /**
    * Test du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils
            .testConstructeurPriveSansArgument(MaquetteThemeTools.class);
      assertTrue("Le constructeur privé n'a pas été trouvé", result);

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteThemeTools#getTheme(javax.servlet.FilterConfig)}<br>
    * <br>
    * Cas de test : l'objet FilterConfig passé à la méthode est null<br>
    * <br>
    * Résultat attendu : la méthode doit renvoyer le thème par défaut
    * 
    * @throws MaquetteThemeException
    */
   @Test
   public void getThemeAvecFilterConfigNull() throws MaquetteThemeException {

      AbstractMaquetteTheme theme = MaquetteThemeTools.getTheme(null);

      assertNotNull("Le thème ne devrait pas être null", theme);

      assertEquals("Mauvais thème renvoyé", MaquetteThemeParDefaut.class
            .getName(), theme.getClass().getName());

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteThemeTools#getTheme(javax.servlet.FilterConfig)}<br>
    * <br>
    * Cas de test : l'objet FilterConfig passé à la méthode demande un thème
    * <code>null</code><br>
    * <br>
    * Résultat attendu : la méthode doit renvoyer le thème par défaut
    * 
    * @throws MaquetteThemeException
    */
   @Test
   public void getThemeAvecFilterConfigThemeNull()
         throws MaquetteThemeException {

      MockFilterConfig filterConfig = new MockFilterConfig();
      filterConfig.addInitParameter(ConstantesConfigFiltre.THEME, null);

      AbstractMaquetteTheme theme = MaquetteThemeTools.getTheme(null);

      assertNotNull("Le thème ne devrait pas être null", theme);

      assertEquals("Mauvais thème renvoyé", MaquetteThemeParDefaut.class
            .getName(), theme.getClass().getName());

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteThemeTools#getTheme(javax.servlet.FilterConfig)}<br>
    * <br>
    * Cas de test : l'objet FilterConfig passé à la méthode ne contient pas de
    * thème<br>
    * <br>
    * Résultat attendu : la méthode doit renvoyer le thème par défaut
    * 
    * @throws MaquetteThemeException
    */
   @Test
   public void getThemeAvecParDefaut() throws MaquetteThemeException {

      Properties properties = new Properties();

      AbstractMaquetteTheme theme = MaquetteThemeTools.getTheme(properties);

      assertNotNull("Le thème ne devrait pas être null", theme);

      assertEquals("Mauvais thème renvoyé", MaquetteThemeParDefaut.class
            .getName(), theme.getClass().getName());

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteThemeTools#getTheme(javax.servlet.FilterConfig)}<br>
    * <br>
    * Cas de test : l'objet FilterConfig passé à la méthode demande le thème AED<br>
    * <br>
    * Résultat attendu : la méthode doit renvoyer le thème AED
    * 
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test
   public void getThemeAed() throws MaquetteThemeException, IOException {

      String values = ConstantesConfigFiltre.THEME + "="
            + MaquetteConstant.THEME_AED;
      InputStream stream = new ByteArrayInputStream(values.getBytes());
      Properties properties = new Properties();
      properties.load(stream);

      AbstractMaquetteTheme theme = MaquetteThemeTools.getTheme(properties);

      assertNotNull("Le thème ne devrait pas être null", theme);

      assertEquals("Mauvais thème renvoyé", MaquetteThemeAed.class.getName(),
            theme.getClass().getName());

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteThemeTools#getTheme(javax.servlet.FilterConfig)}<br>
    * <br>
    * Cas de test : l'objet FilterConfig passé à la méthode demande le thème GED<br>
    * <br>
    * Résultat attendu : la méthode doit renvoyer le thème GED
    * 
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test
   public void getThemeGed() throws MaquetteThemeException, IOException {

      String values = ConstantesConfigFiltre.THEME + "="
            + MaquetteConstant.THEME_GED;
      InputStream stream = new ByteArrayInputStream(values.getBytes());
      Properties properties = new Properties();
      properties.load(stream);

      AbstractMaquetteTheme theme = MaquetteThemeTools.getTheme(properties);

      assertNotNull("Le thème ne devrait pas être null", theme);

      assertEquals("Mauvais thème renvoyé", MaquetteThemeGed.class.getName(),
            theme.getClass().getName());

   }

   /**
    * Test unitaire de la méthode
    * {@link MaquetteThemeTools#getTheme(javax.servlet.FilterConfig)}<br>
    * <br>
    * Cas de test : l'objet FilterConfig passé à la méthode demande un thème
    * inconnu<br>
    * <br>
    * Résultat attendu : la méthode doit lever une exception
    * 
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test(expected = MaquetteThemeException.class)
   public void getThemeInconnu() throws MaquetteThemeException, IOException {

      String values = ConstantesConfigFiltre.THEME + "=gloubi-boulga";
      InputStream stream = new ByteArrayInputStream(values.getBytes());
      Properties properties = new Properties();
      properties.load(stream);

      MaquetteThemeTools.getTheme(properties);

   }

}
