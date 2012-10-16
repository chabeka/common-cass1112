package fr.urssaf.image.commons.maquette.template;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.definition.ILeftCol;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingInfoBoxPropertyException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;
import fr.urssaf.image.commons.maquette.template.generator.InfoBoxGenerator;
import fr.urssaf.image.commons.maquette.template.parser.internal.LeftColParser;
import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;


/**
 * Sous-partie de la classe {@link MaquetteParser} pour le
 * traitement des boîtes de gauche
 *
 */
public final class MaquetteParserLeftColHelper {

   
   private MaquetteParserLeftColHelper() {
      
   }
   
   
   /**
    * Rendu des boîtes de gauche dans la &lt;div&gt; "leftcol"
    * 
    */
   protected static void build(
         OutputDocument document,
         MaquetteConfig maquetteCfg,
         Source htmlSrcFromTmpl,
         HttpServletRequest request)
   throws 
   MissingSourceParserException,
   MissingInfoBoxPropertyException, 
   MissingHtmlElementInTemplateParserException
   {

      // Vérifie que l'implémentation des boîtes de gauche est fournie
      ILeftCol implLeftCol = maquetteCfg.getImplLeftCol();
      if( implLeftCol != null )
      {
         
         // Initialise le rendu HTML
         StringBuilder sbHtml = new StringBuilder();
         
         // Ajoute de la boîte de gauche standard : information application
         sbHtml.append(boiteStandardInfoAppli(implLeftCol,request));

         // Ajoute la boîte de gauche standard : information utilisateur
         sbHtml.append(boiteStandardInfoUser(implLeftCol,request));
         
         // Ajoute la boîte de gauche standard : lien déconnexio
         sbHtml.append(boiteStandardLienDeconnexion(implLeftCol,request));

         // Ajout des boîtes spécifiques
         sbHtml.append(boitesSpecifiques(implLeftCol,request));

         // Insertion du html
         LeftColParser lcpTpl = new LeftColParser(htmlSrcFromTmpl) ;
         MaquetteParserHelper.ecritContenuBalise(
               document,lcpTpl.getLeftColTag(), sbHtml.toString(), false);
         
      }
   }

   
   private static String boiteStandardInfoAppli(
         ILeftCol implLeftCol,
         HttpServletRequest request)
   throws MissingInfoBoxPropertyException
   {
      
      // Initialise le résultat de la méthode
      String html = "";
      
      // Vérifie que l'implémentation des boîtes de gauche est fournie
      if (implLeftCol!=null) {
      
         // Récupère les informations nécessaires pour construire la boîte
         String nomAppli = implLeftCol.getNomApplication(request);
         String versionAppli = implLeftCol.getVersionApplication(request);
         
         // Vérifie que les informations requises sont présentes 
         if (StringUtils.isNotEmpty(nomAppli) && StringUtils.isNotEmpty(versionAppli)) {         

            // Construit la boîte de gauche
            
            InfoBoxItem appInfo = new InfoBoxItem(
                  "app",
                  "Application",
                  "Informations relatives à l'application courante") ;

            appInfo.addSpan(
                  "name",
                  "Nom de l'application",
                  StringEscapeUtils.escapeHtml(nomAppli)) ;

            appInfo.addSpan(
                  "version",
                  "Version de l'application",
                  StringEscapeUtils.escapeHtml(versionAppli)) ;

            html = InfoBoxGenerator.build(appInfo) ;

         }

      }
      
      // Renvoie le rendu HTML
      return html;
      
   }
   
   
   private static String boiteStandardInfoUser(
         ILeftCol implLeftCol,
         HttpServletRequest request)
   throws MissingInfoBoxPropertyException {
      
      // Initialise le résultat de la méthode
      String html = "";
      
      // Vérifie que l'implémentation des boîtes de gauche est fournie
      if (implLeftCol!=null) {
         
         // Récupère les informations nécessaires pour construire la boîte
         String nomUser = implLeftCol.getNomUtilisateur(request);
         String roleUser = implLeftCol.getRoleUtilisateur(request);
         
         // Vérifie que les informations requises sont présentes
         if (!StringUtils.isEmpty(nomUser)) {
            
            // Construit la boîte de gauche
            
            InfoBoxItem usrInfo = new InfoBoxItem(
                  "user",
                  "Utilisateur",
                  "Informations relatives à l'utilisateur identifié" ) ;
            
            usrInfo.addSpan(
                  "name",
                  StringEscapeUtils.escapeHtml("Prénom Nom de l'utilisateur"),
                  StringEscapeUtils.escapeHtml(nomUser)) ;
            
            usrInfo.addSpan(
                  "rights",
                  StringEscapeUtils.escapeHtml("Droits affectés à l'utilisateur"),
                  StringEscapeUtils.escapeHtml(roleUser)) ;
            
            html = InfoBoxGenerator.build(usrInfo) ;
            
         }
               
      }
      
      // Renvoie le rendu HTML
      return html;
      
   }
   
   
   private static String boiteStandardLienDeconnexion(
         ILeftCol implLeftCol,
         HttpServletRequest request)
   throws MissingInfoBoxPropertyException {
      
      // Initialise le résultat de la méthode
      String html = "";
      
      // Vérifie que l'implémentation des boîtes de gauche est fournie
      if (implLeftCol!=null) {
      
         // Récupère les informations nécessaires pour construire la boîte
         String lienDeco = implLeftCol.getLienDeconnexion(request);
         
         // Vérifie que les informations requises sont présentes 
         if (StringUtils.isNotEmpty(lienDeco)) {         

            // Construit la boîte de gauche
            
            InfoBoxItem logoutInfo = new InfoBoxItem(
                  "logout",
                  "Déconnexion",
                  "Boîte de déconnexion") ;
            
            logoutInfo.addBtn(
                  "user",
                  "D&eacute;connexion",
                  lienDeco) ;
            
            html = InfoBoxGenerator.build(logoutInfo) ;

         }

      }
      
      // Renvoie le rendu HTML
      return html;
      
   }
   
   
   private static String boitesSpecifiques(
         ILeftCol implLeftCol,
         HttpServletRequest request)
   throws MissingInfoBoxPropertyException {
      
      // Initialise le résultat de la méthode
      StringBuilder sbHtml = new StringBuilder() ;
      
      // Vérifie que l'implémentation des boîtes de gauche est fournie
      if (implLeftCol!=null) {
      
         List<InfoBoxItem> autresBoites = implLeftCol.getInfoBox(request) ;
         if (autresBoites != null) {
            for(InfoBoxItem sibc : autresBoites) {
               sbHtml.append(InfoBoxGenerator.build(sibc)) ;
            }
         }

      }
      
      // Renvoie le rendu HTML
      return sbHtml.toString();
      
   }
   
}
