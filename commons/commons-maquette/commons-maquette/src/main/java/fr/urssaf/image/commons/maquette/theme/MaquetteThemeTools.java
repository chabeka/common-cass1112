package fr.urssaf.image.commons.maquette.theme;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;


/**
 * Méthodes utilitaires pour la gestion des thèmes
 *
 */
public final class MaquetteThemeTools {
   
   
   private MaquetteThemeTools() {
      
   }
   
   
   /**
    * Renvoie un objet représentant le thème qu'il faut appliquer<br>
    * <br>
    * Se base sur la propriété de configuration "<code>theme</code>" de
    * la configuration du filtre pour déterminer le thème à utiliser
    * 
    * @param propertiesConfig la configuration du filtre
    * @return le thème
    * @throws MaquetteThemeException s'il y a un problème sur le thème
    */
   public static AbstractMaquetteTheme getTheme(Properties propertiesConfig) throws MaquetteThemeException {
      
      // Lit le nom du thème dans la configuration du filtre
      String theme;
      if (propertiesConfig==null) {
         theme = "";
      }
      else {
         theme = propertiesConfig.getProperty(ConstantesConfigFiltre.THEME) ;
      }
      if (theme==null) {
         theme = "";
      } else {
         theme = theme.trim();
      }
      
      // Créé la bonne classe selon le thème
      AbstractMaquetteTheme result;
      if (theme.equals(MaquetteConstant.THEME_AED)) {
         result = new MaquetteThemeAed(propertiesConfig);
      } 
      else if (theme.equals(MaquetteConstant.THEME_GED)) {
         result = new MaquetteThemeGed(propertiesConfig);
      }
      else if (StringUtils.isEmpty(theme)) {
         result = new MaquetteThemeParDefaut(propertiesConfig);
      }
      else {
         throw new MaquetteThemeException(
               String.format("Le thème %s n'est pas reconnu", theme));
      }
      
     // Renvoie du résulat
     return result;
      
   }

}
