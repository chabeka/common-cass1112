package fr.urssaf.image.commons.birt;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtException;
import fr.urssaf.image.commons.birt.exception.MissingKeyInHashMapBirtEngineFactoryException;
import fr.urssaf.image.commons.birt.exception.NullFactoryBirtEngineException;


/**
 * Factory pour créer l'objet {@link BirtEngine} qui va piloter le BIRT Report Engine<br>
 * <br>
 * <b><u>Pour l'utilisation des classes de BIRT, se référer à la fiche de développement F025</u></b> 
 *
 */
public final class BirtEngineFactory {
   
   
   private static final Logger LOGGER = Logger.getLogger(BirtEngineFactory.class);
   
   
   /**
    * Type d'application qui veut utiliser BIRT
    *
    */
   public enum EnumTypeApplication {
      
      /**
       * Application Web
       */
      WEB_APP,
      
      /**
       * Application qui n'est pas Web
       */
      JAVA_APP
      
   };
   
       
   private BirtEngineFactory()
   {}
   
   
   /**
    * 
    * Création d'une instance de la classe {@link BirtEngine} qui va piloter le BIRT Report Engine
    * 
    * @param appType type d'application requérant l'utilisation de BIRT (Web / non web)
    * @param param les paramètres d'initialisation de BIRT (couples de clé/valeur) :<br>
    * <br>
    * <ul>
    *    <li>
    *       Pour une application Web, les paramètres sont :
    *       <table border=1>
    *          <tr>
    *             <td><i>Clé</i></td>
    *             <td><i>Valeur</i></td>
    *             <td><i>Type de la valeur</i></td>
    *             <td><i>Obligatoire</i></td>
    *          </tr>
    *          <tr>
    *             <td>{@link BirtEngineFactoryKeys#SERVLET_CONTEXT}</td>
    *             <td>l'objet ServletContext associé à la servlet depuis laquelle BIRT est utilisé</td>
    *             <td>{@link ServletContext}</td>
    *             <td>Oui</td>
    *          </tr>
    *          <tr>
    *             <td>{@link BirtEngineFactoryKeys#LOG_PATH}</td>
    *             <td>le chemin dans lequel écrire les fichiers de log BIRT</td>
    *             <td>{@link String}</td>
    *             <td>Non</td>
    *          </tr>
    *       </table>
    *    </li>
    *    <br>
    *    <li>Pour une application <u>non</u> Web :</li>
    *    <table border=1>
    *          <tr>
    *             <td><i>Clé</i></td>
    *             <td><i>Valeur</i></td>
    *             <td><i>Type de la valeur</i></td>
    *             <td><i>Obligatoire</i></td>
    *          </tr>
    *          <tr>
    *             <td>{@link BirtEngineFactoryKeys#REPORT_ENGINE_HOME}</td>
    *             <td>le chemin complet du BIRT Report Engine</td>
    *             <td>{@link String}</td>
    *             <td>Oui</td>
    *          </tr>
    *          <tr>
    *             <td>{@link BirtEngineFactoryKeys#LOG_PATH}</td>
    *             <td>le chemin dans lequel écrire les fichiers de log BIRT</td>
    *             <td>{@link String}</td>
    *             <td>Non</td>
    *          </tr>
    *       </table>
    * </ul>
    * @return l'instance de l'objet {@link BirtEngine} 
    * @throws MissingKeyInHashMapBirtEngineFactoryException si un paramètre obligatoire est manquant
    * @throws MissingConstructorParamBirtException si un problème survient lors de l'instanciation du {@link BirtEngine}
    * @throws BirtException si le BIRT Report Engine rencontre un problème
    * @throws NullFactoryBirtEngineException si le BIRT Report Engine n'arrive pas à créer sa Factory
    */
   public static BirtEngine getBirtEngineInstance( EnumTypeApplication appType, Map<String,Object> param ) 
   throws MissingKeyInHashMapBirtEngineFactoryException, 
   MissingConstructorParamBirtException, 
   BirtException, 
   NullFactoryBirtEngineException {
      
      BirtEngine engine = null ;
      ServletContext servletContext = null ;
      String reportEngineHome = null ;
      String logPath = null ;
      
      LOGGER.debug("Démarrage du serveur Birt");
      
      switch ( appType ) {
         case WEB_APP :
            LOGGER.debug("Mode Web");
            if ( param.containsKey( BirtEngineFactoryKeys.SERVLET_CONTEXT ) )
            {
               servletContext = (ServletContext) param.get( BirtEngineFactoryKeys.SERVLET_CONTEXT ) ;
            }
            else
            {
               throw new MissingKeyInHashMapBirtEngineFactoryException( BirtEngineFactoryKeys.SERVLET_CONTEXT ) ;
            }
               
            if ( param.containsKey( BirtEngineFactoryKeys.LOG_PATH ) )
            {
               logPath = (String) param.get( BirtEngineFactoryKeys.LOG_PATH ) ;
            }

            LOGGER.debug(String.format("logPath = %s",logPath));
            engine = getBirtEngineInstanceForWebApp( servletContext, logPath );
            break ;
         case JAVA_APP :
         default :
            LOGGER.debug("Mode Java App");
            if ( param.containsKey( BirtEngineFactoryKeys.REPORT_ENGINE_HOME ) )
            {
               reportEngineHome = (String) param.get( BirtEngineFactoryKeys.REPORT_ENGINE_HOME ) ;
            }
            else
            {
               throw new MissingKeyInHashMapBirtEngineFactoryException( BirtEngineFactoryKeys.REPORT_ENGINE_HOME ) ;
            }
               
            if ( param.containsKey( BirtEngineFactoryKeys.LOG_PATH ) )
            {
               logPath = (String) param.get( BirtEngineFactoryKeys.LOG_PATH ) ;
            }

            engine = getBirtEngineInstanceForJavaApp( reportEngineHome, logPath );
            break ;
      }
      
      
      LOGGER.debug("Serveur Birt démarré");
      
      return engine ;
      
   }
   
   
   private static BirtEngine getBirtEngineInstanceForWebApp( ServletContext servletContext, String logPath ) 
   throws MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
        
      LOGGER.debug(String.format("logPath = %s",logPath));
      
      BirtEngine engine = BirtEngine.getInstance(
            "", 
            logPath, 
            servletContext ) ;
      
      return engine ;
   }
   
   private static BirtEngine getBirtEngineInstanceForJavaApp( String reportEngineHome, String logPath ) 
   throws MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
            
      BirtEngine engine = null ;
      
      try {
         engine = BirtEngine.getInstance(
               reportEngineHome, 
               logPath, 
               null );
      } catch (MissingConstructorParamBirtException e) {
         // hcangement du message s'il n'est pas cohérent avec l'utilisation de la factory
         if ( e.getMessage().contains(": context") )
         {
            throw new MissingConstructorParamBirtException("reportEnginePath",e);
         }
         LOGGER.error(e.getMessage());
         LOGGER.fatal(e);
      }
      
      return engine ;
   }

}
