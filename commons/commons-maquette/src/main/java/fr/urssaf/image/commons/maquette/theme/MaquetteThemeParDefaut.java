package fr.urssaf.image.commons.maquette.theme;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;

/**
 * Représente le thème par défaut<br>
 * <br>
 * Utilise les valeurs spécifiées dans la configuration du filtre (web.xml)
 * ainsi que les valeurs par défaut
 * 
 */
public final class MaquetteThemeParDefaut extends AbstractMaquetteTheme {

   private static final long serialVersionUID = 5262508270477499371L;

   /**
    * Constructeur
    * 
    * @param filterConfig
    *           la configuration du filtre
    * @throws MaquetteThemeException
    *            un problème est survenu
    */
   public MaquetteThemeParDefaut(Properties filterConfig)
         throws MaquetteThemeException {

      super(filterConfig);

      updateValeurThemesDepuisConfigFiltre();

   }

   @Override
   protected String getFichierRessourceTheme() {
      return "theme_defaut.properties";
   }

   private void updateValeurThemesDepuisConfigFiltre()
         throws MaquetteThemeException {

      // Si des valeurs sont passées dans la configuration du filtre,
      // elles sont prioritaires

      surchargeConfig(ConstantesConfigFiltre.CSSMAINBACKGROUNDCOLOR,
            ConstantesConfigTheme.CSSMAINBACKGROUNDCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSCONTENTBACKGROUNDCOLOR,
            ConstantesConfigTheme.CSSCONTENTBACKGROUNDCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSLEFTCOLORBACKGROUNDIMG,
            ConstantesConfigTheme.CSSLEFTCOLBACKGROUNDIMG);
      surchargeConfig(ConstantesConfigFiltre.CSSINFOBOXBACKGROUNDCOLOR,
            ConstantesConfigTheme.CSSINFOBOXBACKGROUNDCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSHEADERBACKGROUNDCOLOR,
            ConstantesConfigTheme.CSSHEADERBACKGROUNDCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSHEADERBACKGROUNDIMG,
            ConstantesConfigTheme.CSSHEADERBACKGROUNDIMG);
      surchargeConfig(ConstantesConfigFiltre.CSSSELECTEDMENUBACKGROUNDCOLOR,
            ConstantesConfigTheme.CSSSELECTEDMENUBACKGROUNDCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSMAINFONTCOLOR,
            ConstantesConfigTheme.CSSMAINFONTCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSCONTENTFONTCOLOR,
            ConstantesConfigTheme.CSSCONTENTFONTCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSMENUFIRSTROWFONTCOLOR,
            ConstantesConfigTheme.CSSMENUFIRSTROWFONTCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSMENULINKFONTCOLOR,
            ConstantesConfigTheme.CSSMENULINKFONTCOLOR);
      surchargeConfig(ConstantesConfigFiltre.CSSMENULINKHOVERFONTCOLOR,
            ConstantesConfigTheme.CSSMENULINKHOVERFONTCOLOR);
      surchargeConfig(ConstantesConfigFiltre.MAINLOGO,
            ConstantesConfigTheme.MAINLOGO);
      surchargeConfig(ConstantesConfigFiltre.APPLOGO,
            ConstantesConfigTheme.APPLOGO);

   }

   private void surchargeConfig(String paramDansFiltre, String paramDansConfig) {
      String valeurFiltre = getFilterValue(paramDansFiltre);
      if (!StringUtils.isEmpty(valeurFiltre)) {
         getTheme().setProperty(paramDansConfig, valeurFiltre);
      }
   }

}
