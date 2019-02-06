package fr.urssaf.image.commons.maquette;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.exception.RessourceNonSpecifieeException;
import fr.urssaf.image.commons.maquette.exception.RessourceNonTrouveeException;
import fr.urssaf.image.commons.maquette.session.SessionTools;
import fr.urssaf.image.commons.maquette.theme.AbstractMaquetteTheme;


/**
 *
 * Servlet permettant le téléchargement des ressources images, Javascript et CSS de la maquette<br>
 * <br>
 * <b><u>Le paramétrage est décrit dans la documentation Word du composant commons-maquette</u></b><br>
 * <br>
 * Cette servlet est nécessaire pour le fonctionnement de la maquette, et elle doit être
 * paramétrée dans l'application cliente comme l'indique la documentation.<br>
 * <br>
 * Dans le template HTML de la maquette, le téléchargement des ressources image et js se fait
 * par l'appel de l'URL suivante :<br>
 * &nbsp;&nbsp;&nbsp;<code>getResourceImageMaquette.do?name=<i>NomDeLaRessource</i></code><br>
 * <br>
 * Par exemple :<br>
 * <ul>
 *    <li>
 *       <code>
 *       &lt;img src="getResourceImageMaquette.do?name=/resource/img/pixel_aed.png" alt="" /&gt;
 *       </code>
 *    </li>
 *    <li>
 *       <code>
 *       &lt;script type="text/javascript" src="getResourceImageMaquette.do?name=/resource/js/classes/Menu.js"&gt;Menu class&lt;/script&gt;
 *       </code>
 *    </li>
 * </ul>
 * <b><u>Pour les CSS, il faut distinguer deux cas :</u></b><br>
 * <br>
 * <ul>
 *    <li>il s'agit d'une CSS qu'il faut modifier pour y inclure les valeurs
 *    du thème en cours</li>
 *    <li>il s'agit d'une ressource comme une autre</li>
 * </ul>
 * Pour différencier les deux cas, dans le template html de la maquette, 
 * un paramètre <code>&overload=1</code> est ajouté au getResourceImageMaquette. Exemple :<br>
 * <br>
 * <code>
 * &lt;link href="getResourceImageMaquette.do?name=/resource/css/main-font.css&overload=1" rel="stylesheet" type="text/css" /&gt;
 * </code>
 * 
 *
 */
public final class MaquetteServlet extends HttpServlet {
	
	
   private static final long serialVersionUID = 1469002127996013302L;
   
   
   /**
    * Taille d'un buffer pour la lecture d'un fichier (en octets)
    */
   private static final int BUFFER_READ_SIZE = 1024;
   
	 
   /**
    * {@inheritDoc}
    * 
    * <br><br>
    * <b>Lève également les exceptions suivantes, dérivées de IOException :</b><br>
    * <ul>
    * <li>{@link RessourceNonSpecifieeException} si le nom de la ressource n'est pas spécifiée
    * (paramètre <code>name</code> manquant dans l'objet <code>HttpServletRequest</code>)</li>
    * <li>{@link RessourceNonTrouveeException} si le nom de la ressource demandée ne correspond
    * pas à une ressource existante (paramètre <code>name</code> de l'objet <code>HttpServletRequest</code>)</li>
    * </ul>
    *
    */
	public void doGet(
	      HttpServletRequest request, 
	      HttpServletResponse response)
	throws IOException {
		
	   
	   // Récupère le nom de la ressource demandée
      String requestedFile = request.getParameter("name");
      if( requestedFile == null )
      {
         throw new RessourceNonSpecifieeException();
      }
	   
      // Détecte si la ressource demandée est un CSS dont il faut modifier
      // le contenu à partir du thème en cours
      String modifierCss = request.getParameter("overload");
		
      // Récupère la ressource sous la forme d'un InputStream
		ServletContext servletContext = getServletContext();
    	InputStream inputStream = getClass().getResourceAsStream(requestedFile);
		try {
		
		   // Vérifie que la ressource a été trouvée
         if (inputStream==null)
         {
            throw new RessourceNonTrouveeException(null, requestedFile);
         }
		   
		   // cas normal on ne fait que lire le fichier de resource et l'afficher
		   if( modifierCss == null )
		   {		
		      printFileContentWithResponse(
		            response,
		            servletContext,
		            inputStream,
		            requestedFile ) ;
		   }
		   // cas de modification des CSS à la volée
		   else
		   {			
		      
		      // Récupère la configuration de la maquette qui a été stockée 
		      // en session lors du passage dans le filtre (MaquetteFilter.doFilter)
		      MaquetteFilterConfig maqFilterConfig = SessionTools.getFilterConfig(request);

		      // On écrit les CSS modifiés dans la réponse HTTP
		      printCssAvecModifTheme(
		            response,
		            inputStream,
		            maqFilterConfig.getTheme()) ;
		      
		   }
		
		}
      finally {
         if (inputStream!=null) {
            inputStream.close();
         }
      }
	}
	
	
	private void printFileContentWithResponse(
         HttpServletResponse response,
         ServletContext servletContext,
         InputStream inputStream,
         String requestedFile)
   throws IOException
   {
       if( inputStream != null )
       {
          // Récupère le type MIME
          String mimeType = servletContext.getMimeType(requestedFile);
          
          // Copie le contenu de la ressource dans la réponse HTTP
          int length = 0 ;
          OutputStream out = response.getOutputStream();
          try {
             byte[] buf = new byte[BUFFER_READ_SIZE];
             int count = inputStream.read(buf);
             while (count>0) {
                out.write(buf, 0, count);
                length += count ;
                count = inputStream.read(buf);
             }
          } finally {
             out.close();
          }
          
          // Définit le type MIME de la réponse
          response.setContentType(mimeType);
          
          // Définit la taille du contenu de la réponse
          response.setContentLength(length);
       }
   }
	
	
	
	protected void printCssAvecModifTheme( 
	      HttpServletResponse response, 
	      InputStream inputStreamCss, 
	      AbstractMaquetteTheme theme )
	throws IOException
	{

	   // Ceinture et bretelle : on vérifie que le fichier CSS
	   // a bien été trouvé, bien que la vérification soit déjà faite
	   // en amont
	   if (inputStreamCss!=null)
	   {       

	      // -----------------------------------------------------------------------
	      // Récupère le fichier CSS sous la forme d'une chaîne de caractères
	      // -----------------------------------------------------------------------

	      String cssContent = getCssContent(inputStreamCss);


	      // -----------------------------------------------------------------------
	      // Met à jour le CSS avec les valeurs du thème en cours
	      // -----------------------------------------------------------------------

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSMAINBACKGROUNDCOLOR, 
	            theme.getCssMainBackgroundColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSCONTENTBACKGROUNDCOLOR,
	            theme.getCssContentBackgroundColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSLEFTCOLORBACKGROUNDIMG, 
	            theme.getCssLeftcolBackgroundImg()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSINFOBOXBACKGROUNDCOLOR,
	            theme.getCssInfoboxBackgroundColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSHEADERBACKGROUNDCOLOR, 
	            theme.getCssHeaderBackgroundColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSHEADERBACKGROUNDIMG, 
	            theme.getCssHeaderBackgroundImg()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSSELECTEDMENUBACKGROUNDCOLOR, 
	            theme.getCssSelectedMenuBackgroundColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSMAINFONTCOLOR, 
	            theme.getCssMainFontColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSCONTENTFONTCOLOR,
	            theme.getCssContentFontColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSMENUFIRSTROWFONTCOLOR, 
	            theme.getCssMenuFirstRowFontColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSMENULINKFONTCOLOR, 
	            theme.getCssMenuLinkFontColor()) ;

	      cssContent = cssContent.replace(
	            ConstantesConfigFiltre.CSSMENULINKHOVERFONTCOLOR, 
	            theme.getCssMenuLinkHoverFontColor()) ;


	      // -----------------------------------------------------------------------
	      // Ecrit le CSS dans la réponse HTTP
	      // -----------------------------------------------------------------------

	      // Le type MIME
	      response.setContentType("text/css"); 

	      // La longueur
	      response.setContentLength( cssContent.length() );

	      // Le CSS en lui-même
	      ServletOutputStream out = response.getOutputStream();
	      out.print( cssContent );

	   }
	}
	
	
	
	private static String getCssContent(InputStream inputStreamCss) throws IOException {
	   
	   String cssContent = "" ;
	   StringBuffer buffer = new StringBuffer();
	   InputStreamReader isr = new InputStreamReader(inputStreamCss, "UTF-8");
	   try {

	      Reader reader = new BufferedReader(isr);
	      
	      int charPos = reader.read();
	      while (charPos>-1) {
	         buffer.append((char)charPos);
	         charPos = reader.read();
	      }
	      
	      cssContent = buffer.toString();
	   }
	   finally  {
	      isr.close();
	   }
      
	   return cssContent;
	   
	}

}
