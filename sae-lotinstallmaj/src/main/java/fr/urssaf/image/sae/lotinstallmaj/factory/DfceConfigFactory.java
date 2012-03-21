package fr.urssaf.image.sae.lotinstallmaj.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.DfceConfig;

/**
 * Factory permettant de créer un objet DfceConfig. 
 *
 */
@Component
public final class DfceConfigFactory {

   /**
    * Methode permettant de créer un objet DfceConfig.
    * 
    * @param saeGeneralConfig
    *                fichier général de configuration SAE
    * @return dfceConfig 
    *                contenant la configuration d'accès à DFCE.
    */
   public DfceConfig createDfceConfig(Resource saeGeneralConfig) {
           
      Properties prop = new Properties();
      DfceConfig dfceConfig = null;
      InputStream inputStream = null;        
      try {
          inputStream = saeGeneralConfig.getInputStream();            
          prop.load(inputStream);
          // recuperation du chemin du fichier DFCE à partir du fichier de configuration générale du SAE
          String filePathDfceConf = prop.getProperty("sae.dfce.cheminFichierConfig");
          if (! StringUtils.isBlank(filePathDfceConf)) {
             // récupération propriétés du chemin de conf DFCE
             Properties propDfce = readDfceConf(filePathDfceConf);
             
             dfceConfig = createDfceConfig(propDfce);
          }   
          inputStream.close();
          return dfceConfig;
      } catch (IOException exception) {
         
         // Une erreur non prévue s'est produite lors de la création de la
         // configuration d'accès à DFCE.
         throw new MajLotRuntimeException(exception);
      }
   }
   
   /**
    * Création de l'objet properties pour la lecture du fichier de conf DFCE
    * @throws IOException 
    * @throws FileNotFoundException 
    */
   private Properties readDfceConf(String filePathDfceConf) throws IOException {
      Properties props = new Properties();
      FileInputStream fileInputStream = new FileInputStream(filePathDfceConf);
      props.load(fileInputStream);
      fileInputStream.close();
      return props;
   }
   
   /**
    * Récupération des données et instanciation de l'objet DfceConfig a partir d'un objet Properties
    */
   private DfceConfig createDfceConfig(Properties props) {
      
      DfceConfig dfceConfig = new DfceConfig();

      //login
      String login = props.getProperty("db.login");
      dfceConfig.setLogin(login);
      
      //password
      String password = props.getProperty("db.password");
      dfceConfig.setPassword(password);
      
      //basename
      String baseName = props.getProperty("db.baseName");
      dfceConfig.setBasename(baseName);
      
      // secure
      String secure = "http";
      String propSecure = props.getProperty("db.secure"); 
      boolean https = Boolean.valueOf(propSecure);
      if (https) {
         secure = "https";
      }
      // serveur
      String serveur = props.getProperty("db.hostName"); 
      // port
      String port = props.getProperty("db.hostPort");
      // contextRoot
      String contextRoot = props.getProperty("db.contextRoot");
      // URL
      String urlToolkit = secure.concat("://").concat(serveur).concat(":").concat(port).concat(contextRoot);
      
      dfceConfig.setUrlToolkit(urlToolkit);
      
      return dfceConfig;      
      
   }
   
}
