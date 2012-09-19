package fr.urssaf.image.commons.maquette.theme;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;

/**
 * Test de la classe {@link MaquetteThemeGed}
 * 
 */
@SuppressWarnings("PMD")
public class MaquetteThemeParDefautTest {

   @Test
   public void testValeurParDefaut() throws MaquetteThemeException {

      MaquetteThemeParDefaut theme = new MaquetteThemeParDefaut(null);

      assertEquals("Erreur dans le thème", MaquetteConstant.GETRESOURCEURI
            + "?name=/resource/img/logo_aed.png", theme.getAppLogo());
      assertEquals("Erreur dans le thème", "#fff", theme
            .getCssContentBackgroundColor());
      assertEquals("Erreur dans le thème", "#000", theme
            .getCssContentFontColor());
      assertEquals("Erreur dans le thème", "#051A7D", theme
            .getCssHeaderBackgroundColor());
      assertEquals("Erreur dans le thème", MaquetteConstant.GETRESOURCEURI
            + "?name=/resource/img/degrade_h_aed.png", theme
            .getCssHeaderBackgroundImg());
      assertEquals("Erreur dans le thème", "#EAEAEF", theme
            .getCssInfoboxBackgroundColor());
      assertEquals("Erreur dans le thème", MaquetteConstant.GETRESOURCEURI
            + "?name=/resource/img/leftcol_aed.png", theme
            .getCssLeftcolBackgroundImg());
      assertEquals("Erreur dans le thème", "#A6A9CA", theme
            .getCssMainBackgroundColor());
      assertEquals("Erreur dans le thème", "#fff", theme.getCssMainFontColor());
      assertEquals("Erreur dans le thème", "#000", theme
            .getCssMenuFirstRowFontColor());
      assertEquals("Erreur dans le thème", "#000", theme
            .getCssMenuLinkFontColor());
      assertEquals("Erreur dans le thème", "#000", theme
            .getCssMenuLinkHoverFontColor());
      assertEquals("Erreur dans le thème", "#05577D", theme
            .getCssSelectedMenuBackgroundColor());
      assertEquals("Erreur dans le thème", MaquetteConstant.GETRESOURCEURI
            + "?name=/resource/img/logo_image_aed.png", theme.getMainLogo());

   }

   @Test
   public void testValeurDansConfigFiltre() throws MaquetteThemeException,
         IOException {

      StringBuffer buffer = new StringBuffer();
      buffer.append(ConstantesConfigFiltre.APPLOGO + "=APPLOGO\n");
      buffer.append(ConstantesConfigFiltre.CSSCONTENTBACKGROUNDCOLOR
            + "=CSSCONTENTBACKGROUNDCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSCONTENTFONTCOLOR
            + "=CSSCONTENTFONTCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSHEADERBACKGROUNDCOLOR
            + "=CSSHEADERBACKGROUNDCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSHEADERBACKGROUNDIMG
            + "=CSSHEADERBACKGROUNDIMG\n");
      buffer.append(ConstantesConfigFiltre.CSSINFOBOXBACKGROUNDCOLOR
            + "=CSSINFOBOXBACKGROUNDCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSLEFTCOLORBACKGROUNDIMG
            + "=CSSLEFTCOLORBACKGROUNDIMG\n");
      buffer.append(ConstantesConfigFiltre.CSSMAINBACKGROUNDCOLOR
            + "=CSSMAINBACKGROUNDCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSMAINFONTCOLOR
            + "=CSSMAINFONTCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSMENUFIRSTROWFONTCOLOR
            + "=CSSMENUFIRSTROWFONTCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSMENULINKFONTCOLOR
            + "=CSSMENULINKFONTCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSMENULINKHOVERFONTCOLOR
            + "=CSSMENULINKHOVERFONTCOLOR\n");
      buffer.append(ConstantesConfigFiltre.CSSSELECTEDMENUBACKGROUNDCOLOR
            + "=CSSSELECTEDMENUBACKGROUNDCOLOR\n");
      buffer.append(ConstantesConfigFiltre.MAINLOGO + "=MAINLOGO\n");

      Properties properties = new Properties();
      InputStream stream = new ByteArrayInputStream(buffer.toString()
            .getBytes());
      properties.load(stream);
      MaquetteThemeParDefaut theme = new MaquetteThemeParDefaut(properties);

      assertEquals("Erreur dans le thème", "APPLOGO", theme.getAppLogo());
      assertEquals("Erreur dans le thème", "CSSCONTENTBACKGROUNDCOLOR", theme
            .getCssContentBackgroundColor());
      assertEquals("Erreur dans le thème", "CSSCONTENTFONTCOLOR", theme
            .getCssContentFontColor());
      assertEquals("Erreur dans le thème", "CSSHEADERBACKGROUNDCOLOR", theme
            .getCssHeaderBackgroundColor());
      assertEquals("Erreur dans le thème", "CSSHEADERBACKGROUNDIMG", theme
            .getCssHeaderBackgroundImg());
      assertEquals("Erreur dans le thème", "CSSINFOBOXBACKGROUNDCOLOR", theme
            .getCssInfoboxBackgroundColor());
      assertEquals("Erreur dans le thème", "CSSLEFTCOLORBACKGROUNDIMG", theme
            .getCssLeftcolBackgroundImg());
      assertEquals("Erreur dans le thème", "CSSMAINBACKGROUNDCOLOR", theme
            .getCssMainBackgroundColor());
      assertEquals("Erreur dans le thème", "CSSMAINFONTCOLOR", theme
            .getCssMainFontColor());
      assertEquals("Erreur dans le thème", "CSSMENUFIRSTROWFONTCOLOR", theme
            .getCssMenuFirstRowFontColor());
      assertEquals("Erreur dans le thème", "CSSMENULINKFONTCOLOR", theme
            .getCssMenuLinkFontColor());
      assertEquals("Erreur dans le thème", "CSSMENULINKHOVERFONTCOLOR", theme
            .getCssMenuLinkHoverFontColor());
      assertEquals("Erreur dans le thème", "CSSSELECTEDMENUBACKGROUNDCOLOR",
            theme.getCssSelectedMenuBackgroundColor());
      assertEquals("Erreur dans le thème", "MAINLOGO", theme.getMainLogo());

   }
}
