package fr.urssaf.image.commons.maquette;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import fr.urssaf.image.commons.maquette.template.MaquetteConfig;
import fr.urssaf.image.commons.maquette.template.MaquetteParser;
import fr.urssaf.image.commons.maquette.tool.CharResponseWrapper;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.tool.MaquetteTools;
import fr.urssaf.image.commons.maquette.tool.UrlPatternMatcher;

public class MaquetteFilter implements Filter {
	
	public static final Logger logger = Logger.getLogger( MaquetteFilter.class.getName() );
	
	private FilterConfig filterConfig;

	@Override
	public void destroy() {
		filterConfig = null;
	}
	
	@Override
	public void doFilter(ServletRequest rq, ServletResponse rs,
			FilterChain chain) throws IOException, ServletException {
logger.debug( "---------------------------------------" );
		HttpServletRequest request = (HttpServletRequest) rq;
		HttpServletResponse response = (HttpServletResponse) rs;
		
		// Gestion des param�tres statiques (via le web.xml)
		try {
			MaquetteConfig maquetteCfg = new MaquetteConfig( getFilterConfig(), request ) ;

logger.debug( "Request URL : " + request.getRequestURL() + "?" + request.getQueryString() );

			// 0) test d'application ou non du filtre
			if (!applyFilter(request)) {
logger.debug( "Arr�t du filtre") ;
				chain.doFilter(request, response) ;
				return;
			}
	
			// 1) r�cup�rer le flux : cr�er le writer et appeler le doChain pour intercepter le html et le stocker 
			// 		dans une variable accessible gr�ce au CharResponseWrapper
			CharResponseWrapper wrapper = new CharResponseWrapper(
					(HttpServletResponse) response);
	
			// permet de remplir le wrapper(=response) qui est vide actuellement : on est le premier filtre, la 
			// servlet sera ensuite interpr�t�e et on r�cup�re la main
			PrintWriter pw = response.getWriter();
			chain.doFilter(request, wrapper);
			
			// Cas 1 : on a du text/html ou du text/plain ou on demande � forcer le passage
			if (wrapper.getContentType() != null
					&& ( wrapper.getContentType().equals("text/html") 
							|| wrapper.getContentType().equals("text/plain") )) {
				
				// forcer le type mime (surtout pour le text/plain en entr�e)
				wrapper.setContentType("text/html");
				
				// Cr�ation du parser avec la cha�ne � d�corer et le tremplate d�corateur
				MaquetteParser mp = new MaquetteParser( wrapper.toString(), MaquetteTools.getResourcePath("/html/main.html"), request, maquetteCfg ) ;
				try {
					mp.build();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				String outputDocument = mp.getOutputDocument().toString() ;
				pw.println( outputDocument );
			} 
			// Cas 2 : ce n'est pas une resource � d�corer => on l'affiche juste
			else if(wrapper.getContentType() != null )
			{
				String outputDocument = wrapper.toString() ; 
				pw.println( outputDocument );
			}
			// Cas 3 : la resource demand�e n'est pas trouv�e => 404 not found
			else
				response.setStatus(404);
			
		} catch (Exception e) {
logger.fatal( "Probl�me avec MaquetteConfig, regardez la stackTrace" );
			e.printStackTrace();
			throw new ServletException( "La servlet a rencontr� une erreur : (" + e.getClass() + ") " + e.getMessage() ,
					e.getCause() );
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}
	

	/**
	 * @desc retourne false si on a trouv� un param�tre excludeFile ET que le
	 * contenu du param�tre correspond � l'URI demand� SINON retourne true, et
	 * le filtre pourra �tre appliqu�
	 * @return 
	 */
	public boolean applyFilter(HttpServletRequest rq) {
		// Exclusion par d�faut li� � la MaquetteServlet
		if( checkGetResource(rq) )
			return false ;
		
		// Exclusion explicite
		return !checkFilesParameters( rq, "excludeFiles" ) ;
	}
	
	/**
	 * @desc v�rifie si l'URI de la requete correspond au pattern de notre Servlet getResource.
	 * @param rq
	 * @return
	 * @todo Ne pas utiliser une expression r�guli�re mais une URI et son pattern RFC 2396
	 */
	private boolean checkGetResource( HttpServletRequest rq )
	{			
		Boolean match ;
		String URI = rq.getRequestURI();
		RegularExpression re = new RegularExpression( MaquetteConstant.GETRESOURCEURI );
		match = re.matches(URI) ;
		return match ;
	}
	
	/**
	 * @desc lit le param�tre de configuration includeFiles/excludeFiles et retourne true si la requ�te correspond � un des pattern
	 * 		 inscrit dans la configuration du filtre
	 * @param rq
	 * @return
	 */
	private boolean checkFilesParameters( HttpServletRequest rq, String paramName )
	{
		Boolean match = false ;
		String patternList = getFilterConfig().getInitParameter(
				paramName );

		if (patternList != null) {
			String[] filesToTest = patternList.split(";");
			String URI = rq.getRequestURI();
			match = UrlPatternMatcher.matchOne( filesToTest, URI ) ;
		}

		return match;
	}

}
