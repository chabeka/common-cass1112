package fr.urssaf.image.commons.cassandra.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.AbstractResource;

import fr.urssaf.image.commons.cassandra.exception.CassandraConfigurationException;
import fr.urssaf.image.commons.cassandra.model.NoConnectionKeyspace;

/**
 * Factory pour récupérer une connexion à cassandra.
 * Utile pour faire des instantiations via spring
 * 
 */
public final class CassandraClientFactory implements DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraClientFactory.class);
   
   private final static String SAE_CONFIG_CASSANDRA_TRANSFERT_CONFIG = "sae.cassandra.transfert.cheminFichierConfig";
   
   private final static String CASSANDRA_START_LOCAL = "cassandra.startlocal";
   private final static String CASSANDRA_HOSTS = "cassandra.hosts";
   private final static String CASSANDRA_USERNAME = "cassandra.username";
   private final static String CASSANDRA_PASSWORD = "cassandra.password";
   private final static String CASSANDRA_KEYSPACE = "cassandra.keyspace";
   private final static String CASSANDRA_DATASET = "cassandra.dataset";

   // En mode "cassandra local" uniquement. Unité : milli-secondes
   private static final int DELAY_BETWEEN_RETRIES = 1000;

   private Cluster cluster;
   private Keyspace keyspace;

   /**
    * Constructeur utilisé pour le transfert.
    * @param saeConfigResource prend en parametres le fichier de configuration du sae.
    * @throws InterruptedException
    */
   public CassandraClientFactory(AbstractResource saeConfigResource) throws InterruptedException{
      
      Properties saeProperties = new Properties();

      try {
         saeProperties.load(saeConfigResource.getInputStream());
      } catch (IOException e) {
         throw new CassandraConfigurationException(e);
      }
      
      String pathConfCassandraTransfert = saeProperties
            .getProperty(SAE_CONFIG_CASSANDRA_TRANSFERT_CONFIG);
   
      // on teste si la connexion de transfert est configuree ou non
      if (StringUtils.isNotBlank(pathConfCassandraTransfert)) {
         
         // la connexion cassandra de transfert est configuree
         // on initialise les objets
         Properties cassandraProp = new Properties();

         try {
            cassandraProp.load(new FileInputStream(pathConfCassandraTransfert));
         } catch (IOException e) {
            throw new CassandraConfigurationException(e);
         }
         
         String hosts = cassandraProp.getProperty(CASSANDRA_HOSTS);
         String dataset = cassandraProp.getProperty(CASSANDRA_DATASET);
         String userName = cassandraProp.getProperty(CASSANDRA_USERNAME);
         String password = cassandraProp.getProperty(CASSANDRA_PASSWORD);
         String keyspaceName = cassandraProp.getProperty(CASSANDRA_KEYSPACE); 
         Boolean startLocal = Boolean.valueOf(cassandraProp.getProperty(CASSANDRA_START_LOCAL));
         
         CassandraServerBean cassandraServer = new CassandraServerBean();
         cassandraServer.setDataSet(dataset);
         cassandraServer.setHosts(hosts);
         cassandraServer.setStartLocal(startLocal);

         initCassandra(cassandraServer, keyspaceName, userName, password);
      } else {
         // on est dans le cas ou la connexion de transfert n'est pas configuree
         // on utilise un keyspace bidon
         keyspace = new NoConnectionKeyspace();
      }
   }
   
   /**
    * 
    * Instancie un objet CassandraClientFactory, et ouvre une connexion à cassandra.
    * On prépare un objet "Keyspace" correspondant à une connexion à un serveur
    * cassandra. Utile pour simplifier l'instanciation via spring
    * 
    * @param cassandraServer
    *           Correspond au(x) serveur(s) qu'on tente de joindre
    * @param keyspaceName
    *           Nom du keyspace
    * @param userName
    *           Nom d'utilisateur, pour l'authentification
    * @param password
    *           Mot de passe, pour l'authentification
    * @throws InterruptedException     Ou nous a demandé de nous arrêter alors on s'arrête
    */
   public CassandraClientFactory(CassandraServerBean cassandraServer,
         String keyspaceName, String userName, String password) throws InterruptedException {
      initCassandra(cassandraServer, keyspaceName, userName, password);
   }
   
   /**
    * Méthode factorisation : initilisation connection cassandra
    * 
    * @param cassandraServer
    * @param keyspaceName
    * @param userName
    * @param password
    * @throws InterruptedException
    */
   private void initCassandra(CassandraServerBean cassandraServer,
         String keyspaceName, String userName, String password) throws InterruptedException {
      LOG.debug("Creation d'un client cassandra utilisant les serveurs suivants : " + cassandraServer.getHosts());
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", userName);
      credentials.put("password", password);
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            cassandraServer.getHosts());
      if (cassandraServer.getStartLocal()) {
         // Mode test : petit pool de connexion
         hostConfigurator.setMaxActive(1);
      }
      else {
         // Mode réel : on active le DynamicLoadBalancingPolicy
         
         // Désactivation du DynamicLoadBalancingPolicy suite à la détection d'un 
         //  bug Hector rencontré en intégration interne.
         // Cf. Mantis 8022, pièce jointe sae_services_executable-logs.zip 
         // Ce commentaire et la ligne de code sont laissés pour éviter
         //  une réactivation de cette option.
         // hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
         
      }
      cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      if (cassandraServer.getStartLocal()) {
         failoverPolicy = new FailoverPolicy(Integer.MAX_VALUE-1, DELAY_BETWEEN_RETRIES);
      } else {
         failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      }
      keyspace = HFactory.createKeyspace(keyspaceName, cluster, ccl,
            failoverPolicy, credentials);
   }

   @Override
   public void destroy() throws Exception {
      if (cluster != null) {
         HFactory.shutdownCluster(cluster);
         cluster = null;
      }
   }

   /**
    * @return  Le keyspace cassandra
    */
   public Keyspace getKeyspace() {
      return keyspace;
   }
}
