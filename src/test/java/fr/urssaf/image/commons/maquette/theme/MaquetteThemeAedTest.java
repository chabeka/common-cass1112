package fr.urssaf.image.commons.maquette.theme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;


/**
 * Test de la classe {@link MaquetteThemeAed}
 *
 */
@SuppressWarnings("PMD")
public class MaquetteThemeAedTest {

   
   @Test
   public void testTheme() throws MaquetteThemeException {
      
      MaquetteThemeAed theme = new MaquetteThemeAed(null);
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/logo_aed.png",
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
            "#051A7D",
            theme.getCssHeaderBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/degrade_h_aed.png",
            theme.getCssHeaderBackgroundImg());
      assertEquals(
            "Erreur dans le thème",
            "#EAEAEF",
            theme.getCssInfoboxBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "getResourceImageMaquette.do?name=/resource/img/leftcol_aed.png",
            theme.getCssLeftcolBackgroundImg());
      assertEquals(
            "Erreur dans le thème",
            "#A6A9CA",
            theme.getCssMainBackgroundColor());
      assertEquals(
            "Erreur dans le thème",
            "#fff",
            theme.getCssMainFontColor());
      assertEquals(
            "Erreur dans le thème",
            "#fff",
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
            "getResourceImageMaquette.do?name=/resource/img/logo_image_aed.png",
            theme.getMainLogo());
      
   }
   
   
}
