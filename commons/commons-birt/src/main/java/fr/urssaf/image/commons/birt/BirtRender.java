package fr.urssaf.image.commons.birt ;

import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtException;
import fr.urssaf.image.commons.birt.exception.MissingParamBirtRenderException;
import fr.urssaf.image.commons.birt.exception.NoEngineBirtEngineException;
import fr.urssaf.image.commons.birt.exception.NoInstanceBirtEngineException;


/**
 * Classe permettant de générer des rapports html ou pdf à partir de rapports BIRT<br>
 * <br>
 * <b><u>Pour l'utilisation des classes de BIRT, se référer à la fiche de développement F025</u></b>
 */
public final class BirtRender
{
   
   /**
    * Le répertoire dans lequel générer le rapport
    */
	private String outputPath ;
	
	
	/**
	 * Le nom du fichier dans lequel générer le rapport (sans l'extension)
	 */
	private String outputFilename ;
	
	
	/**
	 * Le nom de fichier par défaut dans lequel générer le rapport (sans l'extension)
	 */
	private String defaultOutputFilename ; //NOPMD
	
	
	private static final Logger LOGGER = Logger.getLogger(BirtRender.class);
	
	
	/**
    * Le format de rendu du rapport 
    *
    */
   public enum EnumFormatRendu {
      
      /**
       * PDF
       */
      PDF,
      
      /**
       * HTML
       */
      HTML
      
   };
	
   
	/**
	 * Démarre le moteur de rendu
	 * 
	 * @param outputPath le répertoire dans lequel générer le rapport
	 * @param outputFilename le nom du fichier dans lequel générer le rapport (sans l'extension)
	 * @throws MissingConstructorParamBirtException si un problème survient lors de 
	 *         l'instanciation du {@link BirtEngine}
	 * @throws BirtException si le BIRT Report Engine rencontre un problème 
	 * @throws NoEngineBirtEngineException si le serveur BIRT n'est pas démarré
	 * @throws NoInstanceBirtEngineException si l'objet {@link BirtEngine} n'a pas été créé  
    *         au préalable à l'aide de la factory {@link BirtEngineFactory}
    *         
    * @see BirtEngineFactory#getBirtEngineInstance
    *          
	 */
	public BirtRender(
	      String outputPath,
	      String outputFilename )
	throws
	      MissingConstructorParamBirtException,
	      BirtException, 
	      NoEngineBirtEngineException, 
	      NoInstanceBirtEngineException {
			   
	   if( BirtEngine.getInstance() != null 
	      && BirtEngine.getInstance().isStopped() )
	   {
	      throw new NoEngineBirtEngineException( "Le serveur Birt a été arrêté" ) ;
	   }
	   
		setConstructorParams( outputPath, outputFilename );
	}
	
	/**
	 * Lance la génération du rapport avec la possibilité de spécifier un nom de
	 * fichier de rapport différent de celui passé au constructeur de l'objet. 
	 * 
	 * @param reportFilePath emplacement du modèle de rapport (par exemple 
	 * "<code>src/main/resources/reports/monModele.rptdesign</code>")
	 * @param outputFilename le nom du fichier dans lequel générer le rapport
	 * (sans les répertoires ni l'extension). Par exemple : "Rapport_2010". 
	 * Le répertoire de sortie a été défini lors de la construction
	 * de l'objet ({@link BirtRender#BirtRender(String, String)}). L'extension
	 * sera déterminée automatiquement selon le format de rendu.
	 * @param renderMode le format de rendu du rapport (PDF, HTML, ...)
	 * @param paramValues les valeurs des paramètres dynamiques du rapport (dans
	 * un modèle de rapport, il est possible d'indiquer des "variables" dont les
	 * valeurs sont spécifiées lors de la génération du rapport, comme un titre, 
	 * un auteur, ... cf. fiche de développement pour les détails)
	 * @throws MissingParamBirtRenderException Si un paramètre est manquant lors de la 
    *         demande du rendu d'un rapport
    * @throws NoInstanceBirtEngineException si l'objet {@link BirtEngine} n'a pas été  
    *         créé au préalable à l'aide de la factory {@link BirtEngineFactory} 
    * @throws NoEngineBirtEngineException si le serveur BIRT n'est pas démarré
	 * @throws EngineException si BIRT rencontre un problème lors du rendu
	 * 
	 * @see #doRender(String, EnumFormatRendu, Map)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void doRender(
	      String reportFilePath,
	      String outputFilename,
	      EnumFormatRendu renderMode,
	      Map paramValues )
	throws
	MissingParamBirtRenderException, 
	NoInstanceBirtEngineException, 
	NoEngineBirtEngineException,      
	EngineException
	{

	   if ( outputFilename == null )
		{
		   this.outputFilename = defaultOutputFilename ;
		}
		else
		{
		   this.outputFilename = outputFilename ;
		}
	   
	   doRender( reportFilePath, renderMode, paramValues ) ;
	}
	
	/**
	 * Lance la génération du rapport
	 * @param reportFilePath emplacement du modèle de rapport (par exemple 
    * "<code>src/main/resources/reports/monModele.rptdesign</code>")
	 * @param renderMode le format de rendu du rapport (PDF, HTML, ...)
	 * @param paramValues les valeurs des paramètres dynamiques du rapport (dans
    * un modèle de rapport, il est possible d'indiquer des "variables" dont les
    * valeurs sont spécifiées lors de la génération du rapport, comme un titre, 
    * un auteur, ... cf. fiche de développement pour les détails)
	 * @throws MissingParamBirtRenderException Si un paramètre est manquant lors de la 
    *         demande du rendu d'un rapport
    * @throws NoInstanceBirtEngineException si l'objet {@link BirtEngine} n'a pas été  
    *         créé au préalable à l'aide de la factory {@link BirtEngineFactory} 
    * @throws NoEngineBirtEngineException si le serveur BIRT n'est pas démarré
    * @throws EngineException si BIRT rencontre un problème lors du rendu
    * 
    * @see #doRender(String, String, EnumFormatRendu, Map)
    * 
	 */
	@SuppressWarnings("unchecked")
	public void doRender( 
	      String reportFilePath, 
	      EnumFormatRendu renderMode, 
	      Map paramValues ) 
	throws 
	   MissingParamBirtRenderException, 
	   NoInstanceBirtEngineException, 
	   NoEngineBirtEngineException,
	   EngineException{
	   
	   // Trace
	   LOGGER.debug("Demande de génération d'un rapport BIRT");
	   
	   if( reportFilePath == null )
	   {
			throw new MissingParamBirtRenderException("reportFilePath");
	   }

		// Récupération du moteur depuis l'instance BirtEngine
		IReportEngine engine = BirtEngine.getInstance().getEngine() ;
		
		//Open the report design
		IReportRunnable design = engine.openReportDesign( reportFilePath ); 
			
		//Create task to run and render the report,
		IRunAndRenderTask task = engine.createRunAndRenderTask(design); 
		
		//Set parent classloader for engine
		task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtRender.class.getClassLoader()); 
			
		// set and validate parameters
		if ( paramValues != null ) {
			task.setParameterValues( paramValues );
			task.validateParameters();
		}
		
		// set render options
		IRenderOption options = getRenderOptions( renderMode ) ;
		task.setRenderOption(options);
		
		//run and render report
		task.run();
		task.close();
	}
	
	/**
	 * 
	 * Création de l'objet contenant les paramètres de rendu pour le format de rendu (PDF, HTML, ...)
	 * 
	 * @param renderMode le format de rendu du rapport (PDF, HTML, ...)
	 * @return Les options de rendu pour le moteur
	 */
	protected IRenderOption getRenderOptions( EnumFormatRendu renderMode )
	{		
		IRenderOption options ;
		String fileExtension ;
		
		switch ( renderMode ) {
			case HTML:
			   // Trace
		      LOGGER.debug("Format de sortie du rapport : HTML");
			   //Setup rendering to HTML
				options = new HTMLRenderOption();	
				//Setting this to true removes html and body tags
				((HTMLRenderOption) options).setEmbeddable(false);
				options.setOutputFormat("html");
				fileExtension = "html" ;
				break ;
				
			case PDF :
			default :
			   // Trace
            LOGGER.debug("Format de sortie du rapport : PDF");
				//Setup rendering to PDF
				options = new PDFRenderOption();	
				options.setOutputFormat("pdf");
				fileExtension = "pdf" ;
		}
			
		String outputFileName = FilenameUtils.concat(outputPath,outputFilename) + "." + fileExtension;
		
		LOGGER.debug(String.format("Fichier à générer : %s",outputFileName));
		
		options.setOutputFileName(outputFileName);

		return options;		
	}	
	
	
	private void setConstructorParams(
	      String outputPath,
	      String outputFilename )
	throws
	      MissingConstructorParamBirtException {		
	   
		if( outputPath == null )
		{
		   throw new MissingConstructorParamBirtException("outputPath") ;
		}
		else
		{
		   this.outputPath = outputPath ;
		}
		
		if( outputFilename == null )
		{
		   throw new MissingConstructorParamBirtException("outputFilename") ;
		}
		else{
	      this.defaultOutputFilename = outputFilename ;
		   this.outputFilename = outputFilename ;
		}
		
	}

	/**
	 * Renvoie le répertoire dans lequel générer le rapport
	 * @return le répertoire dans lequel générer le rapport
	 */
	public String getOutputPath() {
		return outputPath;
	}

	/**
	 * Renvoie le nom du fichier dans lequel générer le rapport
	 * @return le nom du fichier dans lequel générer le rapport
	 */
	public String getOutputFilename() {
		return outputFilename;
	}
	
	/**
    * Renvoie le nom de fichier par défaut dans lequel générer le rapport
    * @return le nom de fichier par défaut dans lequel générer le rapport
    */
   public String getDefaultOutputFilename() {
      return defaultOutputFilename;
   }

}