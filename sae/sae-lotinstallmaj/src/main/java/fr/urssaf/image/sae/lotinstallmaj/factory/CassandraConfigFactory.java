package fr.urssaf.image.sae.lotinstallmaj.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

/**
 * Factory permettant de créer un objet CassandraConfig. 
 *
 */
@Component
public final class CassandraConfigFactory {

   /**
    * Méthode permettant de créer un objet CassandraConfig.
    * 
    * @param saeGeneralConfig
    *                fichier général de configuration SAE
    * @return cassandraConfig 
    *                contenant la configuration d'accès à cassandra.
    */
   public CassandraConfig createCassandraConfig(Resource saeGeneralConfig) {
           
      Properties prop = new Properties();
      CassandraConfig cassandraConfig = null;
      InputStream inputStream = null;        
      try {
          inputStream = saeGeneralConfig.getInputStream();            
          prop.load(inputStream);
          // Récupération du chemin du fichier DFCE à partir du fichier de configuration générale du SAE
          String cassandraConfFile = prop.getProperty("sae.cassandra.cheminFichierConfig");
          if (! StringUtils.isBlank(cassandraConfFile)) {
             // récupération propriétés du chemin de conf DFCE
             Properties propCassandra = readCassandraConf(cassandraConfFile);
             
             cassandraConfig = createCassandraConfig(propCassandra);
          }   
          inputStream.close();
          return cassandraConfig;
      } catch (IOException exception) {
         
         // Une erreur non prévue s'est produite lors de la création de la
         // configuration d'accès à Cassandra.
         throw new MajLotRuntimeException(exception);
      }
   }
   
   /**
    * Création de l'objet properties pour la lecture du fichier de conf Cassandra
    * @param cassandraConfPath : chemin du fichier de conf de cassandra
    * @throws IOException 
    * @throws FileNotFoundException 
    */
   private Properties readCassandraConf(String cassandraConfPath) throws IOException {
      Properties props = new Properties();
      FileInputStream fileInputStream = new FileInputStream(cassandraConfPath);
      props.load(fileInputStream);
      fileInputStream.close();
      return props;
   }
   
   /**
    * Récupération des données et instanciation de l'objet CassandraConfig a partir d'un objet Properties
    */
   private CassandraConfig createCassandraConfig(Properties props) {
      CassandraConfig config = new CassandraConfig();
      config.setHosts(props.getProperty("cassandra.hosts"));
      config.setLogin(props.getProperty("cassandra.username"));
      config.setPassword(props.getProperty("cassandra.password"));
      config.setKeyspaceName(props.getProperty("cassandra.keyspace"));
      return config;      
   }
   
}
