package fr.urssaf.image.commons.birt ;

import java.util.logging.Level;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtException;
import fr.urssaf.image.commons.birt.exception.NoEngineBirtEngineException;
import fr.urssaf.image.commons.birt.exception.NoInstanceBirtEngineException;
import fr.urssaf.image.commons.birt.exception.NullFactoryBirtEngineException;

/**
 * Classe permettant de démarrer le serveur BIRT<br>
 * <br>
 * <b><u>Pour l'utilisation des classes de BIRT, se référer à la fiche de développement F025</u></b>
 */
public final class BirtEngine
{
   public static final Logger LOGGER = Logger.getLogger(BirtEngine.class) ;
   
   private static BirtEngine instance = null ;
   
   /**
    * le contexte de la servlet qui veut utiliser BIRT
    */
   private ServletContext servletContext ;
   
   
   /**
    * Le chemin complet du Report Engine tel qu'il a été passé
    * à la méthode {@link #getInstance(String, String, ServletContext)}
    */
   private String reportEnginePath ;
   
   
   /**
    * Le répertoire dans lequel BIRT doit générer ses logs, tel
    * qu'il a été passé à la méthode {@link #getInstance(String, String, ServletContext)}
    */
   private String logPath ;
   
   
   /**
    * Le niveau de log
    */
   private Level logLevel = Level.ALL ;
   
   
   private IReportEngine engine = null ;
   private boolean stopped = true ;
   
   
   private BirtEngine(
         String reportEnginePath,
         String logPath,
         ServletContext context )
   throws
         MissingConstructorParamBirtException,
         BirtException, NullFactoryBirtEngineException {
         
      LOGGER.debug(String.format("reportEnginePath = %s",reportEnginePath));
      LOGGER.debug(String.format("logPath = %s",logPath));
      LOGGER.debug(String.format("ServletContext = %s",context));
      
      setConstructorParams( reportEnginePath, logPath, context );
      EngineConfig config = getConfig() ;
      startEngine( config ) ;

   }
   
   /**
    * Retourne l'instance du BirtEngine, qu'il soit démarré ou non
    * 
    * @return l'instance du BirtEngine
    * @throws NoInstanceBirtEngineException Si l'objet {@link BirtEngine} 
    *         n'a pas été créé au préalable à l'aide de la factory
    *         {@link BirtEngineFactory}
    */
   public static BirtEngine getInstance() 
   throws NoInstanceBirtEngineException {
      
      if( BirtEngine.instance == null )
      {
         throw new NoInstanceBirtEngineException() ;
      }
      
      return BirtEngine.instance ;
   }
   
   /**
    * Retourne l'instance existante du BirtEngine, ou en créé une nouvelle
    * 
    * @param reportEnginePath le chemin complet du BIRT Report Engine. Une chaîne vide est
    *        autorisée si le paramètre <code>context</code> est renseigné. La valeur 
    *        <code>null</code> est par contre interdite.
    * @param logPath le répertoire dans lequel BIRT doit générer ses fichiers de logs (option)
    * @param context l'objet {@link ServletContext} de la servlet voulant utiliser BIRT. 
    *        Mettre <code>null</code> si on est dans un contexte non web.
    * @return l'instance du BirtEngine
    * @throws NullFactoryBirtEngineException si le BIRT Report Engine n'arrive pas à créer sa Factory 
    * @throws BirtException si le BIRT Report Engine rencontre un problème
    * @throws MissingConstructorParamBirtException si un problème survient lors de l'instanciation du {@link BirtEngine} 
    */
   public static BirtEngine getInstance(
         String reportEnginePath,
         String logPath,
         ServletContext context ) 
   throws 
      MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
      
      BirtEngine returnedInstance = null ;
      
      try {
         returnedInstance = BirtEngine.getInstance() ;
      } catch ( NoInstanceBirtEngineException e ) {
         BirtEngine.instance = new BirtEngine(reportEnginePath, logPath, context) ;
         returnedInstance = BirtEngine.instance;
      }      
      
      return returnedInstance ;
   }
   
   /**
    * Permet de modifier le niveau de log
    * 
    * @param lvl le niveau de log que l'on veut atteindre
    * @return un flag indiquant si le changement de niveau de log a eu lieu
    */
   public Boolean doChangeLogLevel( Level lvl ) {
      
      Boolean result ;
      
      if ( engine == null ) {
         result = false;
      } else {
         logLevel = lvl ;
         engine.changeLogLevel( logLevel );
         result = true ;
      }
      
      return result ;
   }
   
   /**
    * Arrête le moteur de rendu
    */
   public void stopEngine() {
      
      if ( engine != null ) {
         engine.destroy();
         Platform.shutdown();
         engine = null ; //NOPMD
         stopped = true ;
      }
   }
   
   
   private EngineConfig getConfig() {
      
      final EngineConfig config = new EngineConfig( );
      
      config.setEngineHome( reportEnginePath );
      LOGGER.debug(String.format("EngineConfig : reportEnginePath=%s",reportEnginePath));
      
      config.setLogConfig( logPath, logLevel );
      LOGGER.debug(String.format("EngineConfig : logPath=%s",logPath));
      LOGGER.debug(String.format("EngineConfig : logLevel=%s",logLevel));
      
      // compatibilité JavaApplication et WebApp
      if( getServletContext() == null )
      {
         LOGGER.debug("EngineConfig : Pas de servletContext");
      }
      else
      {
         LOGGER.debug("EngineConfig : Ajout du servletContext");
         config.setPlatformContext( getContext() ) ;
      }
      
      return config ;
   }
   
   
   private IPlatformContext getContext() {
      
      IPlatformContext context = new PlatformServletContext( servletContext );

      return context ;
   }
   
   
   private void startEngine( EngineConfig config ) 
   throws 
      BirtException, 
      NullFactoryBirtEngineException {
      
      
      // Traces
      LOGGER.debug(String.format("EngineConfig.getBIRTHome() = %s",config.getBIRTHome()));
      LOGGER.debug(String.format("EngineConfig.getLogDirectory() = %s",config.getLogDirectory()));
      LOGGER.debug(String.format("EngineConfig.getLogFile() = %s",config.getLogFile()));
      LOGGER.debug(String.format("EngineConfig.getTempDir() = %s",config.getTempDir()));
      
      Platform.startup( config );  //If using RE API in Eclipse/RCP application this is not needed.
      IReportEngineFactory factory = (IReportEngineFactory) Platform
            .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
      
      if( factory == null )
      {
         throw new NullFactoryBirtEngineException() ;
      }
         
      engine = factory.createReportEngine( config );
      
      stopped = false ;
   }
   
   
   
   /**
    * Renvoie l'interface du ReportEngine
    * 
    * @return l'interface du ReportEngine
    * @throws NoEngineBirtEngineException si le serveur BIRT n'est pas démarré
    */
   public IReportEngine getEngine() 
   throws NoEngineBirtEngineException {
      
      if ( engine == null ) {
         throw new NoEngineBirtEngineException("Le serveur Birt n'est pas démarré") ;
      }
      
      return engine ;
   }
   
   
   private void setConstructorParams(
         String reportEnginePath,
         String logPath,
         ServletContext context )
   throws
         MissingConstructorParamBirtException {
      
      // peut être null
      this.logPath = logPath ;
      
      // reportEnginePath peut être vide mais pas null
      if ( reportEnginePath == null )
      {
         throw new MissingConstructorParamBirtException("reportEnginePath") ;
      }
      else
      {
         this.reportEnginePath = reportEnginePath ;
      }
      
      // en mode javaApplication, context peut être null SI reportEnginePath n'est pas vide
      if ((context==null) && (reportEnginePath.isEmpty()))
      {
         throw new MissingConstructorParamBirtException("context") ;
      }
      else
      {
         this.servletContext = context ;
      }
      
   }
   
   /**
    * Supprime l'instance
    */
   protected static void removeInstance() {
      BirtEngine.instance = null ; //NOPMD
   }
   
   /**
    * Renvoie le contexte de la servlet qui veut utiliser BIRT
    * 
    * @return le contexte de la servlet qui veut utiliser BIRT
    */
   public ServletContext getServletContext() {
      return servletContext;
   }

   /**
    * Définit le contexte de la servlet qui veut utiliser BIRT
    * @param servletContext le contexte de la servlet qui veut utiliser BIRT
    */
   public void setServletContext(final ServletContext servletContext) {
      this.servletContext = servletContext;
   }
   
   /**
    * Renvoie un flag indiquant si le Birt Engine est arrêté
    * @return un flag indiquant si le Birt Engine est arrêté
    */
   public Boolean isStopped() {
      return stopped ;
   }
   
   /**
    * Renvoie le niveau de log
    * @return le niveau de log
    */
   public Level getLogLevel() {
      return logLevel ;
   }
   
   /**
    * Renvoie le chemin complet du Report Engine tel qu'il a été passé
    * à la méthode {@link #getInstance(String, String, ServletContext)}
    * @return le chemin complet du Report Engine
    */
   public String getReportEnginePath() {
      return reportEnginePath;
   }

   /**
    * Renvoie le répertoire dans lequel BIRT doit générer ses logs, tel
    * qu'il a été passé à la méthode {@link #getInstance(String, String, ServletContext)}
    * 
    * @return le répertoire des logs
    */
   public String getLogPath() {
      return logPath;
   }
   
   /**
    * Supprime l'instance
    */
   public static void killInstance() {
      BirtEngine.removeInstance() ;
   }
}
