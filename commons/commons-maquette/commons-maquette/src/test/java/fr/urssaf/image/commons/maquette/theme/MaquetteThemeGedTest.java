package fr.urssaf.image.commons.maquette.theme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;


/**
 * Test de la classe {@link MaquetteThemeGed}
 *
 */
@SuppressWarnings("PMD")
public class MaquetteThemeGedTest {

   
   @Test
   public void testTheme() throws MaquetteThemeException {
      
      MaquetteThemeGed theme = new MaquetteThemeGed(null);
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/logo_ged.png",
            theme.getAppLogo());
      assertEquals(
            "Erreur dans le thème",
            "#fff",
            theme.getCssContentBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "#000",
            theme.getCssContentFontColor());
      assertEquals(
            "Erreur dans le thème",
            "#044a6e",
            theme.getCssHeaderBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/degrade_h_ged.png",
            theme.getCssHeaderBackgroundImg());
      assertEquals(
            "Erreur dans le thème",
            "#EAEAEF",
            theme.getCssInfoboxBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/leftcol_ged.png",
            theme.getCssLeftcolBackgroundImg());
      assertEquals(
            "Erreur dans le thème",
            "#AAB9BD",
            theme.getCssMainBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "#fff",
            theme.getCssMainFontColor());
      assertEquals(
            "Erreur dans le thème",
            "#CEEDFF",
            theme.getCssMenuFirstRowFontColor());
      assertEquals(
            "Erreur dans le thème",
            "#fff",
            theme.getCssMenuLinkFontColor());
      assertEquals(
            "Erreur dans le thème",
            "#000",
            theme.getCssMenuLinkHoverFontColor());
      assertEquals(
            "Erreur dans le thème",
            "#05577D",
            theme.getCssSelectedMenuBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/logo_image_ged.png",
            theme.getMainLogo());
      
   }
   
   
}
