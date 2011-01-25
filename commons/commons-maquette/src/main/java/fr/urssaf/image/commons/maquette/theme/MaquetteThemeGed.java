package fr.urssaf.image.commons.maquette.theme;

import javax.servlet.FilterConfig;

import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;


/**
 * Représente le thème GED
 *
 */
public final class MaquetteThemeGed extends AbstractMaquetteTheme {

   
   private static final long serialVersionUID = -870262983313666444L;
   

   /**
    * Constructeur
    * 
    * @param filterConfig la configuration du filtre
    * @throws MaquetteThemeException un problème est survenu
    */
   public MaquetteThemeGed(FilterConfig filterConfig) throws MaquetteThemeException {
      super(filterConfig);
   }

   
   @Override
   protected String getFichierRessourceTheme() {
      return "theme_ged.properties";
   }
   

}
