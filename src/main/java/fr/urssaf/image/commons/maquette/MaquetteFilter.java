package fr.urssaf.image.commons.maquette;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.constantes.ConstantesConfigFiltre;
import fr.urssaf.image.commons.maquette.session.SessionTools;
import fr.urssaf.image.commons.maquette.template.MaquetteConfig;
import fr.urssaf.image.commons.maquette.template.MaquetteParser;
import fr.urssaf.image.commons.maquette.tool.CharResponseWrapper;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.tool.UrlPatternMatcher;


/**
 * Filtre de décoration des vues avec la charge graphique du pôle Image (pattern Décorateur)<br>
 * <br>
 * <b><u>Le paramétrage est décrit dans la documentation Word du composant commons-maquette</u></b><br>
 * <br>
 * Ce filtre est sollicité sur toutes les URL à décorer (ceci est définit dans le web.xml, cf. doc)<br>
 * <br>
 * Le filtre doit ensuite déterminer si oui ou non il doit effectivement décorer la vue :<br>
 * <ul>
 *    <li>si l'URI demandée est celle qui est spécifiquement nécessaire au composant pour 
 *    télécharger ses ressources image et Javascript, alors il ne faut pas décorer</li>
 *    <li>si l'URI demandée fait partie des URI à exclure de la décoration, qui sont
 *    définies dans le web.xml, alors il ne faut pas décorer</li>
 *    <li>si l'URI est en mode AJAX alors il ne faut pas décorer. Dans ce cas l'entête de la requête HTTP possède l'entrée 
 *    <code>X-Requested-With</code> avec la valeur <code>XMLHttpRequest</code> </li>
 *    <li>sinon, il faut décorer</li>
 * </ul>
 *
 */
public final class MaquetteFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MaquetteFilter.class);
	
	private FilterConfig filterConfig;

	
	/**
    * {@inheritDoc}
    */
	@Override
	public void destroy() {
		// rien de particulier à libérer
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("PMD.ExcessiveMethodLength")
	public void doFilter(
	      ServletRequest servletRequest,
	      ServletResponse servletResponse,
	      FilterChain chain) throws IOException, ServletException {
	   
	   // Trace
	   LOGGER.debug( "---------------------------------------" );

	   // Transtype les objets request et response
	   HttpServletRequest request = (HttpServletRequest) servletRequest;
	   HttpServletResponse response = (HttpServletResponse) servletResponse;

	   // Récupération de la configuration du filtre
	   MaquetteFilterConfig maqFilterConfig;
	   try {
	      maqFilterConfig = new MaquetteFilterConfig(getFilterConfig());
	   } catch (Exception e) {
         throw new ServletException(e);
      }
	   
	   // Stockage en session de la configuration du filtre, à destination
	   // de la servlet MaquetteServlet
      SessionTools.storeFilterConfig(request, maqFilterConfig);
      
      // Création de l'objet de configuration de la maquette
	   MaquetteConfig maquetteCfg;
	   try {
	      maquetteCfg = new MaquetteConfig(request,maqFilterConfig);
	   } catch (Exception e) {
	      throw new ServletException(e);
	   }

	   // Détermine si le filtre doit être appliqué ou non
	   if (doitAppliquerFiltre(request)) {

	      // => Le filtre doit être appliqué

	      //
	      // La décoration est la dernière étape du traitement de la ressource demandée
	      // 
	      // On va donc appeler le filtre suivant, qui lui-même va appeler le filtre suivant, ...
	      // 
	      // Une fois tous les filtres passés, on reprend la main pour décorer si besoin
	      // le résultat final
	      //
	      // Pour décorer une fois tous les filtres passés, il faut conserver la réponse
	      // dans une structure adéquate, qui est notre CharResponseWrapper
	      //
	      
	      // Création de la structure permettant de travailler la réponse
	      CharResponseWrapper wrapper = new CharResponseWrapper(response);
	      
	      // Force l'encodage en UTF-8 : sinon le contentType était toujours à ISO-xxx
	      // Toutefois, cela pourrait avoir des conséquences si on ne passe pas parla maquette
	      // On stocke donc l'ancien encodage pour le restaurer ensuite
	      String originalCharSet = wrapper.getCharacterEncoding();
	      wrapper.setCharacterEncoding("UTF-8");

	      // On invoque les filtres suivants, tout en se réservant le Writer sur la réponse
	      PrintWriter printWriter = response.getWriter();
	      chain.doFilter(request, wrapper);
         
         // Traces
         LOGGER.debug("wrapper status : " + wrapper.getStatus()) ;
         LOGGER.debug("wrapper content type : " + wrapper.getContentType()) ;
	          
	         // Selon si la réponse est décorable ou non (par son type MIME)
	         if (isReponseDecorable(wrapper)) {

	            // => La réponse est décorable
	            LOGGER.debug(
                     String.format(
                           "La réponse est décorable, le type MIME est : %s",
                           wrapper.getContentType())
                           ) ;
	            
	            // On écrit la réponse en la décorant
	            doFilterEcritReponseAvecDecoration(wrapper,request,maquetteCfg,printWriter);

	         } 
	         else if(wrapper.getContentType() != null )
	         {
	            
	            // => La réponse n'est pas décorable
	            LOGGER.debug(
	                  String.format(
	                        "La réponse n'est pas décorable, le type MIME est : %s",
	                        wrapper.getContentType())
	                        ) ;
	            
	            // On renvoie la réponse telle quelle
	            doFilterEcritReponseSansDecoration(wrapper,originalCharSet,printWriter);
	            
	         }

	   }
	   else {

	      // => Le filtre ne doit pas être appliqué

	      // Trace
	      LOGGER.debug("Le filtre ne doit pas être appliqué") ;

	      // On passe aux filtres suivants
	      chain.doFilter(request, response) ;

	   }

	}

	
	
	
	/**
    * {@inheritDoc}
    */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
	}

	
	/**
	 * Renvoie l'objet FilterConfig
	 * @return le FilterConfig
	 */
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}
	

	/**
	 * Détermine si le filtre doit être appliqué ou non, c'est à dire si la réponse HTTP
	 * doit être décorée ou non.<br>
	 * <br>
	 * Pour les règles de gestion, voir la documentation de la classe directement
	 * 
	 * @return le flag indiquant s'il faut appliquer le filtre ou non
	 * 
	 * @see MaquetteFilter
	 */
	protected boolean doitAppliquerFiltre(HttpServletRequest request) {
		
	   boolean result;
	   
	   // les requêtes AJAX sont exclues de du filtre de décoration
      if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {

         result = false;
      }

      // Exclusion par défaut lié à la MaquetteServlet
      else if( isUriDeGetResource(request) )
		{
			result = false ;
		}
		else
		{
		   // Exclusion explicite
		   result = !isUriDansListeExclusions(request) ; // NOPMD
		}
		
		// Log
		if (result)
		{
		   LOGGER.debug("Le filtre Maquette sera appliqué pour l'URI " + request.getRequestURI());
		}
		else
		{
		   LOGGER.debug("Le filtre Maquette ne sera pas appliqué pour l'URI " + request.getRequestURI());
		}
		
		return result;
	}
	
	/**
	 * Regarde si l'URI de la requête HTTP correspond au pattern de la Servlet getResource.<br>
	 * <br>
	 * Si c'est le cas, il ne faut pas appliquer le filtre.
	 * 
	 * @param request la requête HTTP
	 * @return true si l'URI de la requête correspond à {@link MaquetteConstant#GETRESOURCEURI},
	 *         false dans le cas contraire
	 */
	protected boolean isUriDeGetResource( HttpServletRequest request )
	{			
	   String uri = request.getRequestURI();
     
      Pattern regex = Pattern.compile(MaquetteConstant.GETRESOURCEURI);
      Boolean match = regex.matcher(uri).find();

      LOGGER.debug("checkGetResource : " + match.toString());
      return match;
	}
	
	/**
	 * Lit le paramètre de configuration excludeFiles et retourne true  
	 * si l'URI de la requête HTTP correspond à un des pattern inscrit 
	 * dans excludeFiles 
	 * 
	 * @param request la requête HTTP
	 * @return true si un des pattern correspond, false dans le cas contraire
	 */
	protected boolean isUriDansListeExclusions(HttpServletRequest request)
	{
	   
	   // Résultat par défaut
	   Boolean match = false ;
	   
	   // Récupère la liste des pattern d'URI à exclure de la décoration
	   String patternList = getFilterConfig().getInitParameter(
	         ConstantesConfigFiltre.EXCLUDEFILES);
	   LOGGER.debug( "Pattern d'exclusion : " + patternList );
	   
	   // Détermine si l'URI de la requête HTTP fait partie des exclusions
	   if (patternList != null) {
	      String[] filesToTest = patternList.split(";");
	      String uri = request.getRequestURI();
	      LOGGER.debug( "URI pour test d'exclusion : " + uri );
	      match = UrlPatternMatcher.matchOne( filesToTest, uri ) ;
	   }
	   
	   // Renvoie le résultat
	   return match;
	   
	}
	
	/**
	 * Détermine si la réponse HTTP est une candidate à la décoration ou non, selon
	 * son type MIME
	 * @param response la réponse HTTP
	 * @return flag indiquant si la réponse HTTP est décorable ou non
	 */
	protected boolean isReponseDecorable(HttpServletResponse response) {
	   return (
	         (response.getContentType() != null) && 
	         (
	               (response.getContentType().contains("text/html")) ||
	               (response.getContentType().contains("text/plain"))
	         )
	   );
	}
	
	
	/**
	 * Ecrit la réponse du CharResponseWrapper sur le PrintWriter, sans décoration
	 * 
	 * @param wrapper le CharResponseWrapper
	 * @param originalCharSet le charset d'origine de la réponse
	 * @param printWriter le PrinterWriter réservé sur la HttpServletResponse
	 */
	private void doFilterEcritReponseSansDecoration(
	      CharResponseWrapper wrapper,
	      String originalCharSet,
	      PrintWriter printWriter) {
	   
	   // replace l'ancien charset modifié après la création du wrapper
      if( originalCharSet != null ) {
         wrapper.setCharacterEncoding( originalCharSet );
      }

      String outputDocument = wrapper.toString() ; 
      printWriter.print(outputDocument);
	   
	}
	
	
	/**
	 * Ecrit la réponse du CharResponseWrapper sur le PrintWriter, avec décoration
	 * 
	 * @param wrapper le CharResponseWrapper
	 * @param request l'objet HttpServletRequest
	 * @param maquetteConfig la configuration de la maquette
	 * @param printWriter le PrinterWriter réservé sur la HttpServletResponse
	 * 
	 * @throws IOException
	 */
	private void doFilterEcritReponseAvecDecoration(
	      CharResponseWrapper wrapper,
	      HttpServletRequest request,
	      MaquetteConfig maquetteConfig,
	      PrintWriter printWriter) throws IOException {
	   
	   // Force le type mime (surtout pour le text/plain en entrée)
      wrapper.setContentType("text/html");
      LOGGER.debug( "Encodage des caractères : " + wrapper.getCharacterEncoding() ) ;

      // Création du parser
      MaquetteParser maquetteParser = new MaquetteParser( 
            wrapper.toString(), 
            MaquetteConstant.CHEMIN_TMPL_MAIN_HTML, 
            request, 
            maquetteConfig ) ;
      
      // Lance la décoration
      try {
         maquetteParser.decore();
      } catch (Exception ex) {
         throw new IOException(ex);
      }

      // Récupère le document décoré
      String outputDocument = maquetteParser.getOutputDocument().toString() ;
      
      // Ecrit le document décoré dans la réponse
      printWriter.println( outputDocument );
	   
	}
	

}
