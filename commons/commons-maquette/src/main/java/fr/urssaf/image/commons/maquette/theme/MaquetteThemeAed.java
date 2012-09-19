package fr.urssaf.image.commons.maquette.theme;

import java.util.Properties;

import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;


/**
 * Représente le thème AED
 *
 */
public final class MaquetteThemeAed extends AbstractMaquetteTheme {


   private static final long serialVersionUID = -6898249833760590030L;
   
   
   /**
    * Constructeur
    * 
    * @param filterConfig la configuration du filtre
    * @throws MaquetteThemeException un problème est survenu
    */
   public MaquetteThemeAed(Properties filterConfig) throws MaquetteThemeException {
      super(filterConfig);
   }


   @Override
   protected String getFichierRessourceTheme() {
      return "theme_aed.properties";
   }


}
