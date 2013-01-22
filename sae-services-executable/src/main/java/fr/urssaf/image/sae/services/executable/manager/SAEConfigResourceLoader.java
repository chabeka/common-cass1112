package fr.urssaf.image.sae.services.executable.manager;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Classe de chargement des propriétés du fichier de configuration générale du
 * SAE
 * 
 * 
 */
public class SAEConfigResourceLoader implements InitializingBean {

   /**
    * Clé du fichier Properties dans laquelle se trouve le chemin complet
    * du fichier de configuration DFCE
    */
   protected static final String DFCE_CONFIG = "sae.dfce.cheminFichierConfig";

   /**
    * Clé du fichier Properties dans laquelle se trouve le chemin complet
    * du fichier de configuration Cassandra
    */
   protected static final String CASSANDRA_CONFIG = "sae.cassandra.cheminFichierConfig";

   private final Properties saeProperties;

   /**
    * Chargement du fichier de configuration du SAE
    * 
    * @param saeConfigResource
    *           fichier de configuration du SAE
    * @throws IOException
    *            exception levée lors du chargement du fichier de configuration
    */
   public SAEConfigResourceLoader(Resource saeConfigResource)
         throws IOException {

      Assert.notNull(saeConfigResource, "'saeConfigResource' is required");

      saeProperties = new Properties();
      saeProperties.load(saeConfigResource.getInputStream());
   }

   /**
    * Le fichier de configuration doit contenir les propriétés :
    * <ul>
    * <li><code>{@value #DFCE_CONFIG}</code></li>
    * <li><code>{@value #CASSANDRA_CONFIG}</code></li>
    * </ul>
    */
   @Override
   public final void afterPropertiesSet() {

      Assert.isTrue(saeProperties.containsKey(DFCE_CONFIG),
            "le fichier de configuration du SAE doit contenir la propriété "
                  + DFCE_CONFIG);

      Assert.isTrue(saeProperties.containsKey(CASSANDRA_CONFIG),
            "le fichier de configuration du SAE doit contenir la propriété "
                  + CASSANDRA_CONFIG);

   }

   /**
    * Retourne le fichier de configuration de DFCE contenu dans la configuration
    * générale du SAE.<br>
    * <br>
    * Le propriété est {@value #DFCE_CONFIG}
    * 
    * @return fichier de configuration de DFCE
    */
   public final FileSystemResource loadDFCEConfigResource() {

      FileSystemResource dfceResource = new FileSystemResource(saeProperties
            .getProperty(DFCE_CONFIG));

      return dfceResource;
   }

   /**
    * Retourne le fichier de configuration de Cassandra contenu dans la
    * configuration générale du SAE.<br>
    * <br>
    * Le propriété est {@value #CASSANDRA_CONFIG}
    * 
    * @return fichier de configuration de Cassandra
    */
   public final FileSystemResource loadCassandraConfigResource() {

      FileSystemResource cassandraResource = new FileSystemResource(
            saeProperties.getProperty(CASSANDRA_CONFIG));

      return cassandraResource;
   }

}
