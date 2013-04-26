package fr.urssaf.image.sae.commons.factory;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Factory de création de l'objet de type PropertyPlaceholderConfigurer
 * à mettre dans le conteneur IOC avec l'ensemble de toutes les paires
 * clé/valeur des fichiers properties
 */
public class PropertiesFactory {

   
   private static final int RES_LENGTH = 3;



   /**
    * Méthode à utiliser en tant que "factory-method" Spring pour mettre dans
    * le conteneur IOC l'objet PropertyPlaceholderConfigurer contenant
    * l'ensemble des paires clé/valeur des fichiers Properties du SAE.
    * 
    * @param saeConfigResource le fichier de configuration générale
    * @return l'objet PropertyPlaceholderConfigurer à mettre dans le conteneur IOC
    * @throws IOException en cas de problème de lecture du fichier de configuration générale
    */
   public final static PropertyPlaceholderConfigurer load(FileSystemResource saeConfigResource) throws IOException  {
      
      // Deux fichiers properties : 
      //  - Fichier de configuration générale
      //  - Fichier de configuration DFCE
      Resource[] tabResource = new Resource[RES_LENGTH];
      
      // Fichier de configuration générale
      tabResource[0] = saeConfigResource;
      
      // Fichier de configuration DFCE
      String dfceConfigPath = getCheminFichierConfigDfce(saeConfigResource,"sae.dfce.cheminFichierConfig");
      FileSystemResource dfceConfigResource = new FileSystemResource(dfceConfigPath); 
      tabResource[1] = dfceConfigResource;
      
      // Fichier de configuration Cassandra
      String cassandraConfigPath = getCheminFichierConfigDfce(saeConfigResource,"sae.cassandra.cheminFichierConfig");
      FileSystemResource cassandraConfigResource = new FileSystemResource(cassandraConfigPath); 
      tabResource[2] = cassandraConfigResource;
      
      // Création du PropertyPlaceholderConfigurer
      PropertyPlaceholderConfigurer property = new PropertyPlaceholderConfigurer();
      property.setLocations(tabResource);
      return property;
      
   }
   
   
   
   private static String getCheminFichierConfigDfce(FileSystemResource saeConfigResource,String property) throws IOException {
      
      Properties props = new Properties();
      
      props.load(saeConfigResource.getInputStream());
      
      return props.getProperty(property);
      
   }
   
}
