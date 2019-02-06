package fr.urssaf.image.commons.maquette.theme;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;

/**
 * Représente un thème graphique de la maquette.<br>
 * <br>
 * Classe abstraite que chaque thème doit dériver.
 * 
 */
public abstract class AbstractMaquetteTheme implements Serializable {

   private static final long serialVersionUID = 3801358133753378008L;

   private final Properties filterConfig;

   /**
    * Les valeurs du thème chargées à partir d'un fichier properties
    */
   private final Properties theme = new Properties();

   /**
    * Renvoie l'objet Properties contenant les valeurs du thème<br>
    * <br>
    * En protected pour permettre aux classes dérivées de modifier le contenu de
    * cet objet
    * 
    * @return l'objet Properties contenant les valeurs du thème
    */
   protected final Properties getTheme() {
      return this.theme;
   }

   /**
    * Le chemin de l'image du logo principal (positionnée en haut à gauche).<br>
    * <br>
    * Typiquement le logo du pôle Image avec les bonnes couleurs de fond et de
    * transparence<br>
    * <br>
    * Correspond au paramètre "logoPrincipal" du web.xml<br>
    * 
    * @return Le chemin de l'image du logo principal
    * 
    */
   public final String getMainLogo() {
      return theme.getProperty(ConstantesConfigTheme.MAINLOGO);
   }

   /**
    * Le chemin de l'image du logo de l'application (positionnée en haut à
    * droite).<br>
    * <br>
    * Correspond au paramètre "appLogo" du web.xml<br>
    * <br>
    * Exemple : "Rhododendron.bmp" où le fichier est dans
    * /WEB_INF/Rhododendron.bmp
    * 
    * @return Le chemin de l'image du logo de l'application (positionnée en haut
    *         à droite).
    * 
    */
   public final String getAppLogo() {
      return theme.getProperty(ConstantesConfigTheme.APPLOGO);
   }

   /**
    * CSS de la couleur de fond de la fenêtre<br>
    * <br>
    * Correspond au paramètre "cssMainBackgroundColor" du web.xml<br>
    * <br>
    * Exemple : "#FF0000"
    * 
    * @return CSS de la couleur de fond de la fenêtre
    * 
    */
   public final String getCssMainBackgroundColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSMAINBACKGROUNDCOLOR);
   }

   /**
    * CSS de la couleur de fond de la zone où s'affiche le contenu applicatif
    * (zone centrale et menu de gauche)<br>
    * <br>
    * Correspond au paramètre "cssContentBackgroundColor" du web.xml<br>
    * Exemple : "lightblue"
    * 
    * @return CSS de la couleur de fond de la zone où s'affiche le contenu
    *         applicatif (zone centrale et menu de gauche)
    * 
    */
   public final String getCssContentBackgroundColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSCONTENTBACKGROUNDCOLOR);
   }

   /**
    * CSS de la couleur de fond de la zone entourant le contenu applicatif<br>
    * <br>
    * Correspond au paramètre "cssHeaderBackgroundColor" du web.xml<br>
    * <br>
    * Exemple : "cyan"
    * 
    * @return CSS de la couleur de fond de la zone entourant le contenu
    *         applicatif
    * 
    */
   public final String getCssHeaderBackgroundColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSHEADERBACKGROUNDCOLOR);
   }

   /**
    * CSS de l'image du dégradé du header (1920*1px)<br>
    * <br>
    * Correspond au paramètre "cssHeaderBackgroundImg" du web.xml<br>
    * <br>
    * Exemple : img/degrade_haut.png
    * 
    * @return CSS de l'image du dégradé du header (1920*1px)
    * 
    */
   public final String getCssHeaderBackgroundImg() {
      return theme.getProperty(ConstantesConfigTheme.CSSHEADERBACKGROUNDIMG);
   }

   /**
    * CSS de l'image de la même couleur que le {@link #cssHeaderBackgroundColor}
    * pour simuler la colonne de gauche (140*1px)<br>
    * <br>
    * Correspond au paramètre "cssLeftcolBackgroundImg" du web.xml<br>
    * <br>
    * Exemple : "img/degrade_gauche.png"
    * 
    * @return CSS de l'image de la même couleur que le
    *         {@link #cssHeaderBackgroundColor} pour simuler la colonne de
    *         gauche (140*1px)
    * 
    */
   public final String getCssLeftcolBackgroundImg() {
      return theme.getProperty(ConstantesConfigTheme.CSSLEFTCOLBACKGROUNDIMG);
   }

   /**
    * CSS de la couleur des polices pour les textes en dehors de la zone de
    * contenu (appTitle, appCopyright, titres des boîtes de gauche)<br>
    * <br>
    * Correspond au paramètre "cssMainFontColor" du web.xml<br>
    * <br>
    * Exemple : "purple"
    * 
    * @return CSS de la couleur des polices pour les textes en dehors de la zone
    *         de contenu (appTitle, appCopyright, titres des boîtes de gauche)
    * 
    */
   public final String getCssMainFontColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSMAINFONTCOLOR);
   }

   /**
    * CSS de la couleur des polices pour les textes de la zone de contenu<br>
    * <br>
    * Correspond au paramètre "cssContentFontColor" du web.xml<br>
    * <br>
    * Exemple : "gray"
    * 
    * @return CSS de la couleur des polices pour les textes de la zone de
    *         contenu
    * 
    */
   public final String getCssContentFontColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSCONTENTFONTCOLOR);
   }

   /**
    * CSS de la couleur de fond des boîtes contextuelles de la colonne de gauche<br>
    * <br>
    * Correspond au paramètre "cssInfoboxBackgroundColor" du web.xml<br>
    * <br>
    * Exemple : "magenta"
    * 
    * @return CSS de la couleur de fond des boîtes contextuelles de la colonne
    *         de gauche
    * 
    */
   public final String getCssInfoboxBackgroundColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSINFOBOXBACKGROUNDCOLOR);
   }

   /**
    * CSS de la couleur de fond du menu sélectionné, fil d'Ariane et titre des
    * boîtes contextuelles<br>
    * <br>
    * Correspond au paramètre "cssSelectedMenuBackgroundColor" du web.xml<br>
    * <br>
    * Exemple : "yellow"
    * 
    * @return CSS de la couleur de fond du menu sélectionné, fil d'Ariane et
    *         titre des boîtes contextuelles
    * 
    */
   public final String getCssSelectedMenuBackgroundColor() {
      return theme
            .getProperty(ConstantesConfigTheme.CSSSELECTEDMENUBACKGROUNDCOLOR);
   }

   /**
    * CSS de la couleur des polices pour les liens de la première ligne du menu<br>
    * <br>
    * Correspond au paramètre "cssMenuFirstRowFontColor" du web.xml<br>
    * <br>
    * Exemple : "black"
    * 
    * @return CSS de la couleur des polices pour les liens de la première ligne
    *         du menu
    * 
    */
   public final String getCssMenuFirstRowFontColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSMENUFIRSTROWFONTCOLOR);
   }

   /**
    * CSS de la couleur des polices pour les liens du menu<br>
    * <br>
    * Correspond au paramètre "cssMenuLinkFontColor" du web.xml<br>
    * <br>
    * Exemple : "green"
    * 
    * @return CSS de la couleur des polices pour les liens du menu
    * 
    */
   public final String getCssMenuLinkFontColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSMENULINKFONTCOLOR);
   }

   /**
    * CSS de la couleur des polices pour les liens survolés du menu<br>
    * <br>
    * Correspond au paramètre "cssMenuLinkHoverFontColor" du web.xml<br>
    * <br>
    * Exemple : "yellow"
    * 
    * @return CSS de la couleur des polices pour les liens survolés du menu
    * 
    */
   public final String getCssMenuLinkHoverFontColor() {
      return theme.getProperty(ConstantesConfigTheme.CSSMENULINKHOVERFONTCOLOR);
   }

   /**
    * Constructeur
    * 
    * @param filterConfig
    *           la configuration du filtre
    * 
    * @throws MaquetteThemeException
    *            un problème est survenu sur le thème de la maquette
    */
   public AbstractMaquetteTheme(Properties filterConfig)
         throws MaquetteThemeException {
      this.filterConfig = filterConfig;
      chargeTheme();
   }

   /**
    * Renvoie une valeur de la configuration du filtre
    * 
    * @param name
    *           le nom de la valeur à renvoyé
    * @return la valeur (chaîne vide ou renseignée, jamais de null)
    */
   @SuppressWarnings("PMD.ConfusingTernary")
   protected final String getFilterValue(String name) {

      String valeur;

      if (name == null) {
         valeur = "";
      } else if (filterConfig != null) {
         valeur = filterConfig.getProperty(name);
      } else {
         valeur = "";
      }

      if (valeur == null) {
         valeur = "";
      } else {
         valeur = valeur.trim();
      }

      return valeur;

   }

   /**
    * Renvoie le nom du fichier properties de <b>src/main/ressources/themes</b>
    * contenant la définition des valeurs du thème.<br>
    * <br>
    * Exemple : "theme_defaut.properties"
    * 
    * @return le nom du fichier properties contenant la définition des valeurs
    *         du thème
    */
   protected abstract String getFichierRessourceTheme();

   /**
    * Charge le fichier properties contenant les valeurs du thème dans un objet
    * Java de type {@link Properties}
    * 
    * @throws MaquetteThemeException
    *            un problème est survenu ...
    */
   private void chargeTheme() throws MaquetteThemeException {

      // Récupère le chemin du fichier properties de ressource contenant les
      // valeurs du thème. La méthode getFichierRessourceTheme() est implémentée
      // dans les classes dérivées de AbstractMaquetteTheme
      String fichierTheme = "themes/" + getFichierRessourceTheme();

      // Charge dans un flux le fichier properties contenant les valeurs du
      // thème
      try {
         InputStream inStream = this.getClass().getClassLoader()
               .getResourceAsStream(fichierTheme);
         try {
            theme.load(inStream);
         } finally {
            inStream.close();
         }
      } catch (IOException ex) {
         throw new MaquetteThemeException(ex);
      }

   }

}
