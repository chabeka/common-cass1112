package fr.urssaf.image.commons.maquette.config;

import java.io.Serializable;

import javax.servlet.FilterConfig;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.theme.AbstractMaquetteTheme;
import fr.urssaf.image.commons.maquette.theme.MaquetteThemeTools;


/**
 * La configuration du filtre de la maquette.<br>
 * <br>
 * Correpond au contenu du web.xml pour la configuration du filtre<br>
 * <br>
 * La classe est sérialisable pour qu'elle puisse être mise en session,
 * afin d'être passé du filtre à la servlet de téléchargement des ressources
 * (MaquetteServlet)
 *
 */
public final class MaquetteFilterConfig implements Serializable
{

   private static final long serialVersionUID = 2208915695648084505L;


   /**
    * La liste des mappings d'URL à ne pas décorer<br>
    * <br>
    * Correspond au paramètre " excludeFiles" du web.xml<br>
    * <br>
    * Exemple : download.do;popup.do;contrat.do
    */
   private final String excludeFiles ;
   
   
   /**
    * Le titre de l'application, répété sur toutes les pages dans le bandeau haut
    * <br>
    * Correspond au paramètre "appTitle" du web.xml<br>
    * <br>
    * Exemple : "Archivage Electronique de Documents"
    */
   private final String appTitle ;
   
   
   /**
    * Le texte de Copyright affiché en bas à droite sur toutes les pages<br>
    * <br>
    * Correspond au paramètre "appCopyright" du web.xml<br>
    * <br>
    * Exemple : "Copyright CIRTIL 2006-2010"
    */
   private final String appCopyright ;
   
   
   /**
    * Le flag indiquant s'il faut afficher ou non un lien vers la page
    * des standards et normes.<br>
    * <br>
    * Correspond au paramètre "appDisplayStandardsAndNorms" du web.xml<br>
    * <br>
    * Dans le fichier, on met "1" ou "0"
    * <br>
    * <b>N'est pas implémenté pour l'instant</b>
    */
   private final Boolean appDispStdNorms ;
   
   
   /**
    * L'implémentation pour générer le menu principal<br>
    * <br>
    * Correspond au paramètre "implementationIMenu" du web.xml
    */
   private final String implMenu ;
   
   
   /**
    * L'implémentation pour générer la colonne de gauche<br>
    * <br>
    * Correspond au paramètre "implementationILeftCol" du web.xml
    */
   private final String implLeftCol ;
   
   
   /**
    * Le thème à utiliser
    */
   private final AbstractMaquetteTheme theme;
      
   
   /**
    * @return La liste des mappings d'URL à ne pas décorer
    */
   public String getExcludeFiles() {
      return excludeFiles;
   }


   /**
    * @return Le titre de l'application, répété sur toutes les pages dans le bandeau haut
    */
   public String getAppTitle() {
      return appTitle;
   }


   /**
    * @return Le texte de Copyright affiché en bas à droite sur toutes les pages
    */
   public String getAppCopyright() {
      return appCopyright;
   }


   /**
    * @return Le flag indiquant s'il faut afficher ou non un lien vers la page
    * des standards et normes.
    */
   public Boolean getAppDispStdNorms() {
      return appDispStdNorms;
   }


   /**
    * @return L'implémentation pour générer le menu principal
    */
   public String getImplMenu() {
      return implMenu;
   }


   /**
    * @return L'implémentation pour générer la colonne de gauche
    */
   public String getImplLeftCol() {
      return implLeftCol;
   }
   
   
   /**
    * @return le thème à utiliser
    */
   public AbstractMaquetteTheme getTheme() {
      return theme;
   }

   
   /**
    * Constructeur
    * 
    * @param filterConfig la configuration du filtre maquette
    * @throws MaquetteThemeException s'il y a un problème sur le thème
    */
   public MaquetteFilterConfig(FilterConfig filterConfig)
   throws
   MaquetteThemeException {
      
      // Les paramètres qui n'ont rien à voir avec le thème
      excludeFiles = filterConfig.getInitParameter(ConstantesConfigFiltre.EXCLUDEFILES);
      appTitle = filterConfig.getInitParameter(ConstantesConfigFiltre.APPTITLE);
      appCopyright = filterConfig.getInitParameter(ConstantesConfigFiltre.COPYRIGHT);
      appDispStdNorms = stringToBool(filterConfig.getInitParameter(ConstantesConfigFiltre.STANDARD_AND_NORM));
      implMenu = filterConfig.getInitParameter(ConstantesConfigFiltre.IMPL_MENU);
      implLeftCol = filterConfig.getInitParameter(ConstantesConfigFiltre.IMPL_LEFTCOL);
      
      // Le thème
      theme = MaquetteThemeTools.getTheme(filterConfig);
      
   }
    
   
   private Boolean stringToBool(String valeur) {
      Boolean result;
      
      if (StringUtils.isEmpty(valeur)) {
         result = false;
      }
      else if (valeur.trim().equals("1")) {
         result = true;
      }
      else {
         result =false;
      }
      return result;
   }
   
}
