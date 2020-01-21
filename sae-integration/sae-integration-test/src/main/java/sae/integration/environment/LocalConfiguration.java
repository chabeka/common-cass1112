package sae.integration.environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;


/**
 * Singleton représentant la configuration locale, lue dans c:\hawai\data\ged
 */
public class LocalConfiguration {

   private static final Logger LOGGER = LoggerFactory.getLogger(LocalConfiguration.class);

   private final String configDir = "c:/hawai/data/ged";

   private final String ecdeName;

   private final String ecdeMountPoint;

   private String cassandraServers;

   private String appliServer;

   // private constructor restricted to this class itself
   private LocalConfiguration() {
      XML xml;
      try {
         xml = new XMLDocument(new File(configDir + "/ecdesources.xml"));
      }
      catch (final FileNotFoundException e) {
         throw new RuntimeException(e);
      }
      ecdeName = xml.xpath("/sources/source/host/text()").get(0);
      ecdeMountPoint = xml.xpath("/sources/source/basePath/text()").get(0);

      try (InputStream input = new FileInputStream(configDir + "/sae-cassandra-config.properties")) {

         final Properties prop = new Properties();
         prop.load(input);
         cassandraServers = prop.getProperty("cassandra.hosts").replace(":9160", "");
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }

      try (InputStream input = new FileInputStream(configDir + "/sae-dfce-config.properties")) {

         final Properties prop = new Properties();
         prop.load(input);
         appliServer = prop.getProperty("db.hostName");
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }

      LOGGER.debug("Configuration locale : ecdeName={}, ecdeMountPoint={}, cassandra={}, appliServer={}",
            ecdeName,
            ecdeMountPoint,
            cassandraServers,
            appliServer);
   }

   private static class SingletonHolder {
      public static final LocalConfiguration singleton = new LocalConfiguration(); // This will be lazily initialized
   }

   /**
    * Récupère l'instance du singleton correspondant à la configuration locale
    * 
    * @return
    */
   public static LocalConfiguration getInstance() {
      return SingletonHolder.singleton;
   }


   /**
    * @return Le nom de l'ECDE, à passer en préfixe de l'url ECDE
    */
   public String getEcdeName() {
      return ecdeName;
   }

   /**
    * @return Le point de montage de l'ECDE sur le serveur d'application
    */
   public String getEcdeMountPoint() {
      return ecdeMountPoint;
   }

   /**
    * @return Les (ou une partie des) serveurs cassandra de la plateforme
    */
   public String getCassandraServers() {
      return cassandraServers;
   }

   /**
    * @return Le nom du serveur d'application principal de la plateforme
    */
   public String getAppliServer() {
      return appliServer;
   }

}
