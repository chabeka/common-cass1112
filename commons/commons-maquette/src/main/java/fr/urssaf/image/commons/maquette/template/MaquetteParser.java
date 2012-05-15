package fr.urssaf.image.commons.maquette.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.maquette.definition.IMenu;
import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingInfoBoxPropertyException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;
import fr.urssaf.image.commons.maquette.template.generator.MenuGenerator;
import fr.urssaf.image.commons.maquette.template.parser.HeadPartParser;
import fr.urssaf.image.commons.maquette.template.parser.internal.ContentAppParser;
import fr.urssaf.image.commons.maquette.template.parser.internal.FooterParser;
import fr.urssaf.image.commons.maquette.template.parser.internal.DivHeaderParser;
import fr.urssaf.image.commons.maquette.template.parser.internal.PageReminderParser;


/**
 * Gère l'instanciation de la class Source (jericho) pour le template 
 * html et le contenu html de l'application métier
 */
public final class MaquetteParser {

   private static final Logger LOGGER = LoggerFactory.getLogger(MaquetteParser.class);

   private final OutputDocument outDoc ;
   private Source htmlSrcFromTmpl ;
   private final Source htmlSrcFromAppl ;
   private final HttpServletRequest request ;
   private final MaquetteConfig maquetteCfg ;


   /**
    * Constructeur
    * 
    * @param html le rendu html généré par l'application métier
    * @param cheminTemplate chemin vers le template html de la maquette (main.html)
    * @param request la request en cours
    * @param maquetteCfg la configuration de la maquette
    * 
    * @throws IOException en cas de problème d'E/S
    */
   public MaquetteParser(
         String html,
         String cheminTemplate,
         HttpServletRequest request,
         MaquetteConfig maquetteCfg)
   throws IOException
   {
    
      // Charge le template HTML dans le champ privé htmlSourceFromApplication,
      // qui est un objet du Parser Jericho
      InputStream inputStreamTmpl = this.getClass().getResourceAsStream(cheminTemplate);
      try {
         
         // Vérifie que le fichier de ressource ait été trouvé
         if (inputStreamTmpl==null) {
            throw new IOException(
                  String.format(
                        "Le template suivant ne semble pas accessible : %s",
                        cheminTemplate)) ;
         }
         
         // Charge le template HTML dans un objet net.htmlparser.jericho.Source
         htmlSrcFromTmpl = new Source(inputStreamTmpl) ;
         
      }
      finally {
         if (inputStreamTmpl!=null) {
            inputStreamTmpl.close();
         }
      }
      
      // Charge le HTML généré par l'application métier dans un objet 
      // du Parser Jericho
      htmlSrcFromAppl = new Source(html);
      
      // Génère le document HTML de sortie dans un objet du Parser Jericho, 
      // en initialisant ce document avec le contenu du template HTML
      outDoc = new OutputDocument( htmlSrcFromTmpl );
      
      // Retire tous les commentaires HTML issus du template de la maquette
      outDoc.remove(
            htmlSrcFromAppl.getAllElements(StartTagType.COMMENT));
      
      // Mémorise certains attributs
      this.maquetteCfg = maquetteCfg ;
      this.request = request ;
      
   }
   
   
   /**
    * Le document HTML modifié par le parser<br>
    * <br>
    * Il faut penser à appeler la méthode {@link #decore()} avant de récupérer
    * cet objet.
    * 
    * @return le document HTML modifié par le parser
    */
   public OutputDocument getOutputDocument()
   {
      return outDoc ;
   }
   

   /**
    * Renvoie la source HTML du template
    * @return la source HTML du template
    */
   protected Source getHtmlSrcFromTmpl() {
      return htmlSrcFromTmpl;
   }


   /**
    * Lance la décoration du HTML généré par l'application cliente avec le contenu de
    * la maquette.
    * 
    * @throws MissingHtmlElementInTemplateParserException si un élément d'une source HTML est
    *         manquant alors qu'il est requis
    * @throws MissingSourceParserException problème technique de source HTML manquante 
    * @throws MissingInfoBoxPropertyException S'il manque une propriété requise à l'InfoBox
    * @throws MenuException si une erreur se produit lors de la génération du menu principal
    */
   public void decore()
   throws
   MissingSourceParserException,
   MissingHtmlElementInTemplateParserException,
   MissingInfoBoxPropertyException,
   MenuException {	

      
      // Traitement de la partie <head>
      buildHead();
      
      // Traitement du bandeau haut (la div identifiée par "header")
      buildDivHeader();
      
      // Traitement de la colonne de gauche
      MaquetteParserLeftColHelper.build(outDoc, maquetteCfg, htmlSrcFromTmpl, request);
      
      // Traitement du body
      buildBody();
      
      // Traitement du pied de page
      buildFooter();
      		
   }

   /**
    * Insère le contenu du &lt;head&gt; de l'application métier après celui
    * du document HTML résultat (qui est le template de la maquette)
    * 
    * @throws MissingSourceHeadPartParserException
    * @throws MissingHtmlElementInTemplateParserException  
    */
   protected void buildHead() 
   throws 
   MissingSourceParserException, 
   MissingHtmlElementInTemplateParserException
   {
      
      // Récupération du head du template
      HeadPartParser hppTpl = new HeadPartParser(htmlSrcFromTmpl) ;
      
      // Récupération du head de l'application métier
      // On met un try pour masquer l'exception qui est levée si l'application
      // métier n'a pas de <head>
      try {
         
         // Récupération du head de l'application métier
         HeadPartParser hppApp = new HeadPartParser(htmlSrcFromAppl);

         // S'il y a un <title> dans le HTML de l'application métier, on retire
         // le title vide du templace de la maquette (c'est le cas normal)
         // Sinon, on met une trace de Warning pour prévenir le développeur/infographique
         // qu'il/elle a oublié son <title>
         if (hppApp.getTitleTag()==null) {
            LOGGER.warn(
                  String.format(
                        "Attention : il manque l'attribut <title> dans le <head> pour l'URI suivante : %s",
                        request.getRequestURI()));
         }
         else {
            outDoc.remove(hppTpl.getTitleTag()) ;
         }
         
         // Insère le contenu du <head> de l'application métier à la suite
         // du <head> du template
         int start = hppTpl.getHeadTag().getEndTag().getBegin() ;
         outDoc.insert(start, hppApp.getHeadTag().getContent().toString());
          
      } catch (MissingHtmlElementInTemplateParserException ex) {		
         
         if (ex.getMessage().equals(HTMLElementName.HEAD)) {
            // Pas de head trouvé dans l'application métier
            // On masque l'exception, et on se met une petite trace
            LOGGER.debug("Pas de head trouvé dans l'application métier");
         }
         else {
            // un autre problème
            throw ex;
         }
         
      }

   }

   /**
    * Traite la balise &lt;div id="header"&gt;<br>
    * <br>
    * Remplace quelques valeurs par celles de la configuration de la maquette
    * 
    */
   protected void buildDivHeader()
   throws 
   MissingSourceParserException, 
   MissingHtmlElementInTemplateParserException, MenuException 
   {
      
      String srcAttribut = "src"; // pour PMD
      
      // Récupération du Parser
      DivHeaderParser parserTemplate = new DivHeaderParser(htmlSrcFromTmpl) ;

      // Balise <h1> "title-app" : écriture du contenu
      MaquetteParserHelper.ecritContenuBalise(
            outDoc,
            parserTemplate.getTitleTag(),
            maquetteCfg.getConfigDuFiltre().getAppTitle(),
            true);

      // Balise <h1> "title-app" : écriture de l'attribut title
      MaquetteParserHelper.ecritAttributBalise(
            outDoc,
            parserTemplate.getTitleTag(),
            "title",
            maquetteCfg.getConfigDuFiltre().getAppTitle(),
            true);

      // Balise <img> "logoimage" : écriture de l'attribut src
      MaquetteParserHelper.ecritAttributBalise(
            outDoc,
            parserTemplate.getMainLogoTag(),
            srcAttribut,
            maquetteCfg.getConfigDuFiltre().getTheme().getMainLogo(),
            false);

      // Balise <img> "logoappli" : écriture de l'attribut src
      MaquetteParserHelper.ecritAttributBalise(
            outDoc,
            parserTemplate.getLogoTag(),
            srcAttribut,
            maquetteCfg.getConfigDuFiltre().getTheme().getAppLogo(),
            false);
      
      
      // Balise <img> "logoappli" : Ajouter la taille de l'image si le navigateur client est sous MSIE6 
      if( maquetteCfg.isInternetExplorer() )
      {
         // texte à rajouter sur les images suivantes du header
         String heightLogo = " style='height:50px' " ;

         // log de l'application
         int start = parserTemplate.getLogoTag().getAttributes().get(srcAttribut).getEnd() ;
         outDoc.insert(start, heightLogo ) ;
         
         // image pour la hauteur minimum
         Attributes attMinHeightImg = parserTemplate.getMinHeightImg().getAttributes();
         start = attMinHeightImg.get(srcAttribut).getEnd();
         outDoc.insert(start, heightLogo ) ;
         
      }

      // Balise <div> "menu" : écriture du menu
      String menu = maquetteCfg.getMenu(request) ;
      MaquetteParserHelper.ecritContenuBalise(
            outDoc,parserTemplate.getMenuTag(),menu,false);
      
   }


   
   
   
   /**
    * Rendu du <code>&lt;body&gt;</code><br>
    * <br>
    * Prend le <code>&lt;body&gt;</code> de l'application métier et le 
    * colle dans la <code>div</code> identifiée par <code>content-application</code>
    * dans le template.<br>
    * <br>
    * Si la balise <code>&lt;noscript&gt;</code> n'est pas présente
    * dans l'application métier, on ajoute au <code>&lt;body&gt;</code>
    * celle du template. 
    * 
    * 
    * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML
    */
   protected void buildBody()
   throws
   MissingSourceParserException,
   MissingHtmlElementInTemplateParserException
   
   {
      
      // Récupération du body du template
      ContentAppParser capTpl = new ContentAppParser(htmlSrcFromTmpl) ;
      PageReminderParser prTpl = new PageReminderParser(htmlSrcFromTmpl) ;

      // Ecriture du fil d'ariane
      StringBuilder sbFilAriane = new StringBuilder();
      sbFilAriane.append(buildBodyGetFilAriane());
      sbFilAriane.append("&nbsp;");
      MaquetteParserHelper.ecritContenuBalise(
            outDoc,prTpl.getPageReminderTag(), sbFilAriane.toString(), false);

      // Récupération du contenu du <body> de l'application métier
      List<Element> elBody = htmlSrcFromAppl.getAllElements("body");
      StringBuilder sbBody = new StringBuilder();
      Attributes bodyAppMetierAttr = null ;
      if (elBody.isEmpty()) {
         // prise en compte du cas où l'on a pas de body dans le html de l'application
         sbBody.append(htmlSrcFromAppl.toString());
      }
      else {
         
         // Récupère l'élément BODY
         Element body = elBody.get(0);
         
         // Récupère le contenu du BODY en chaîne de caractères 
         Segment contenuBody = body.getContent();
         String sContenuBody = contenuBody.toString();
         
         // Ajout le contenu du BODY
         sbBody.append(sContenuBody);
         
         // Mémorise les attributs du BODY pour plus tard
         bodyAppMetierAttr = body.getAttributes();
         
      }

      // Si le HTML de l'application métier n'a pas de balise <noscript>, on ajoute
      // au body celles du template de la maquette
      List<Element> elNoScriptList = htmlSrcFromAppl.getAllElements(HTMLElementName.NOSCRIPT) ;
      if(elNoScriptList.isEmpty())
      {
         StringBuilder sbNoScript = new StringBuilder();
         for( Element el : capTpl.getNoScriptTag() ) {
            sbNoScript.append(el.toString()) ;
         }
         sbBody.append(sbNoScript) ;
      }
      
      // Remplacement du contenu de la div content-application par le body
      // construit plus haut
      MaquetteParserHelper.ecritContenuBalise(
            outDoc,capTpl.getContentAppTag(), sbBody.toString(), false);
      
      // Fusionne les attributs du BODY de la maquette avec ceux de l'application métier
      if (bodyAppMetierAttr!=null) {
         
         Element bodyMaquette = htmlSrcFromTmpl.getAllElements("body").get(0);
         
         MaquetteParserHelper.fusionneAttributs(
               outDoc,
               bodyMaquette,
               bodyAppMetierAttr);
         
      }
      
   }
   
   
   
   protected String buildBodyGetFilAriane() {
      
      // Initialise le résultat
      String filAriane = "";
      
      // Tente d'obtenir le fil d'ariane contextuel 
      IMenu implMenu = maquetteCfg.getImplMenu();
      if (implMenu!=null) {
         filAriane = implMenu.getBreadcrumb(request);
      }
      
      // En dernier recours, on récupère le fil d'ariane à l'aide
      // du menu en cours stocké dans la classe MenuGenerator
      if (StringUtils.isEmpty(filAriane)) {
         filAriane = MenuGenerator.buildBreadcrumb(request); 
      }
      
      // Renvoie du résultat
      return filAriane;
      
   }
   

   /**
    * @throws MissingSourceFooterParserException 
    * @throws MissingHtmlElementInTemplateParserException 
    * @desc modifie le source html
    * 		 s'occupe uniquement du contenu de la balise <body> mais au niveau de la div header (bandeau 
    * 		 supérieur de la maquette)
    * 		 procède en récupérant l'instance de HeaderConfig et injectes ses paramètres dans le template
    */
   protected void buildFooter()
   throws 
   MissingSourceParserException, 
   MissingHtmlElementInTemplateParserException
   {
      int start ;
      int end ;

      // récupération du footer du template
      FooterParser fpTpl = new FooterParser(htmlSrcFromTmpl) ;

      // suppression du lien providedBy dans template généré du footer
      start = fpTpl.getProvidedByTag().getContent().getBegin() ;
      end = fpTpl.getProvidedByTag().getContent().getEnd() ;
      if( !maquetteCfg.getConfigDuFiltre().getAppDispStdNorms() ) {
         outDoc.replace( start, end, "" ) ;
      }

      // suppression du lien providedBy dans template généré du footer
      start = fpTpl.getCopyrightTag().getContent().getBegin() ;
      end = fpTpl.getCopyrightTag().getContent().getEnd() ;
      String appCopyright = maquetteCfg.getConfigDuFiltre().getAppCopyright();
      outDoc.replace( start, end, appCopyright ) ;
      
   }
 
   
   
   
   
   
}