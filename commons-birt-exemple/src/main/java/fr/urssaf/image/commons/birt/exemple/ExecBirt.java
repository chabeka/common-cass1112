package fr.urssaf.image.commons.birt.exemple;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;

import fr.urssaf.image.commons.birt.BirtEngineFactory;
import fr.urssaf.image.commons.birt.BirtEngineFactoryKeys;
import fr.urssaf.image.commons.birt.BirtRender;
import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtException;
import fr.urssaf.image.commons.birt.exception.MissingKeyInHashMapBirtEngineFactoryException;
import fr.urssaf.image.commons.birt.exception.MissingParamBirtRenderException;
import fr.urssaf.image.commons.birt.exception.NoEngineBirtEngineException;
import fr.urssaf.image.commons.birt.exception.NoInstanceBirtEngineException;
import fr.urssaf.image.commons.birt.exception.NullFactoryBirtEngineException;

/**
 * Servlet implementation class ExecBirt
 */
public class ExecBirt extends HttpServlet {
	
   private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger( ExecBirt.class.getName() );
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	   // Préparation des paramètres à envoyer au moteur de rendu pour la génération du rapport
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		
		try {
		   // Démarrage du serveur Birt
		   Map<String, Object> factoryParams = new HashMap<String,Object>();
		   factoryParams.put(BirtEngineFactoryKeys.SERVLET_CONTEXT , this.getServletContext()) ;
		   factoryParams.put(BirtEngineFactoryKeys.LOG_PATH, this.getServletContext().getRealPath("./logs"));
		   BirtEngineFactory.getBirtEngineInstance(
		         BirtEngineFactory.EnumTypeApplication.WEB_APP,
		         factoryParams ) ;
		   
			LOGGER.info("Démarrage du moteur de rendu");
			BirtRender renderer = new BirtRender(
			      this.getServletContext().getRealPath("./outputpath/"), 
			      "output-birt" );
			LOGGER.info("Moteur de rendu démarré");
			
			LOGGER.info("Démarrage traitement de rendu");
			renderer.doRender(
			      this.getServletContext().getRealPath("./reports/monPremierRapport.rptdesign"),
			      BirtRender.EnumFormatRendu.PDF, 
			      paramValues) ;
			LOGGER.info("Le traitement de rendu est terminé, le fichier doit être disponible.\n");
						
			// Affiche le PDF généré
			response.sendRedirect("outputpath/output-birt.pdf");
			
			
		} catch (MissingConstructorParamBirtException e) {
			LOGGER.fatal( e );
			throw new ServletException(e) ;
		} catch (BirtException e) {
		   LOGGER.info("Le serveur Birt n'a pas pu démarrer");
			LOGGER.fatal( e );
			throw new ServletException(e) ;
		} catch (NoInstanceBirtEngineException e) {
		   LOGGER.info("Le moteur de rendu n'a pas pu démarrer car aucune instance du BirtEngine n'existe");
         LOGGER.fatal( e );
         throw new ServletException(e) ;
      } catch (NoEngineBirtEngineException e) {
         LOGGER.info("Le serveur Birt n'est pas démarré");
         LOGGER.fatal( e );
         throw new ServletException(e) ;
      } catch (NullFactoryBirtEngineException e) {
         LOGGER.info("Le serveur Birt n'a pas pu démarrer");
         LOGGER.fatal( e );
         throw new ServletException(e) ;
      } catch (MissingKeyInHashMapBirtEngineFactoryException e) {
         LOGGER.info("Erreur d'utilisation de la factory BirtEngine");
         LOGGER.fatal( e );
         throw new ServletException(e) ;
      } catch (MissingParamBirtRenderException e) {
         LOGGER.info("Paramètre manquant lors de la demande du rendu d'un rapport");
         LOGGER.fatal( e );
      }
		
	}

}
