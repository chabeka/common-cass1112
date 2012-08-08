package fr.urssaf.image.commons.cassandra.helper;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

/**
 * Factory pour récupérer une connexion à cassandra.
 * Utile pour faire des instantiations via spring
 * 
 */
public final class CassandraClientFactory implements DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraClientFactory.class);

   // En mode "cassandra local" uniquement. Unité : milli-secondes
   private static final int DELAY_BETWEEN_RETRIES = 1000;

   private Cluster cluster;
   private Keyspace keyspace;
   
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
