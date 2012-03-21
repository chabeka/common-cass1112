package fr.urssaf.image.commons.cassandra.helper;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

/**
 * Factory pour récupérer une connexion à cassandra
 * 
 */
public final class CassandraClientFactory {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraClientFactory.class);

   // En mode "cassandra local" uniquement. Unité : milli-secondes
   private static final int DELAY_BETWEEN_RETRIES = 1000;

   /**
    * Constructeur privé
    */
   private CassandraClientFactory() {
   }

   /**
    * Renvoie un objet "Keyspace" correspondant à une connexion à un serveur
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
    * @return Un "keyspace" prêt à l'emploi
    * @throws InterruptedException     Ou nous a demandé de nous arrêter alors on s'arrête
    */
   public static Keyspace getClient(CassandraServerBean cassandraServer,
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
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      if (cassandraServer.getStartLocal()) {
         failoverPolicy = new FailoverPolicy(Integer.MAX_VALUE-1, DELAY_BETWEEN_RETRIES);
      } else {
         failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      }
      return HFactory.createKeyspace(keyspaceName, cluster, ccl,
            failoverPolicy, credentials);
   }
}
