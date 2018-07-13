package fr.urssaf.image.commons.maquette.constantes;


/**
 * Les noms des paramètres du filtre Maquette dans le web.xml<br>
 * <br>
 * <b><u>Le paramétrage est décrit dans la documentation Word du composant commons-maquette</u></b><br>
 */
@SuppressWarnings("PMD.LongVariable")
public final class ConstantesConfigFiltre {

   
   private ConstantesConfigFiltre() {
      
   }
   
   /**
    * Le nom du paramètre contenant la liste des pattern d'URI ne pas
    * décorer. Les pattern sont séparés par des points virgules (<code>;</code>)<br>
    * <br>
    * Exemple : <code>download.do;popup.do;contrat.do</code>
    */
   public static final String EXCLUDEFILES = "excludeFiles";
   
   public static final String APPTITLE = "appTitle";
   
   public static final String COPYRIGHT = "appCopyright";
   
   public static final String STANDARD_AND_NORM = "appDisplayStandardsAndNorms";
   
   public static final String IMPL_MENU = "implementationIMenu" ;
   
   public static final String IMPL_LEFTCOL = "implementationILeftCol" ;
   
   public static final String THEME = "theme";
   
   public static final String MAINLOGO = "logoPrincipal";
   
   public static final String APPLOGO = "appLogo";
   
   public static final String CSSMAINBACKGROUNDCOLOR = "cssMainBackgroundColor" ;
   public static final String CSSCONTENTBACKGROUNDCOLOR = "cssContentBackgroundColor" ;
   public static final String CSSHEADERBACKGROUNDCOLOR = "cssHeaderBackgroundColor" ;
   public static final String CSSHEADERBACKGROUNDIMG = "cssHeaderBackgroundImg" ;
   public static final String CSSLEFTCOLORBACKGROUNDIMG = "cssLeftcolBackgroundImg" ;
   public static final String CSSMAINFONTCOLOR = "cssMainFontColor" ;
   public static final String CSSCONTENTFONTCOLOR = "cssContentFontColor" ;
   public static final String CSSINFOBOXBACKGROUNDCOLOR = "cssInfoboxBackgroundColor" ;
   public static final String CSSSELECTEDMENUBACKGROUNDCOLOR = "cssSelectedMenuBackgroundColor" ;
   public static final String CSSMENUFIRSTROWFONTCOLOR = "cssMenuFirstRowFontColor" ;
   public static final String CSSMENULINKFONTCOLOR = "cssMenuLinkFontColor" ;
   public static final String CSSMENULINKHOVERFONTCOLOR = "cssMenuLinkHoverFontColor" ;

   public static final String PROPERTY_FILE = "fichierProprietes";
   public static final String PATH_CONFIG = "sae.ihm.web.exploit.cheminConfigurationMaquette";
   
}
