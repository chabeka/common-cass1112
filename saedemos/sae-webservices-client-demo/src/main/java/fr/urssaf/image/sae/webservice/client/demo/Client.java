package fr.urssaf.image.sae.webservice.client.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.ArrayUtils;

import fr.urssaf.image.sae.webservice.client.demo.component.PropertiesLoader;
import fr.urssaf.image.sae.webservice.client.demo.service.ModificationMasseService;
import fr.urssaf.image.sae.webservice.client.demo.service.PingSecureService;
import fr.urssaf.image.sae.webservice.client.demo.service.PingService;
import fr.urssaf.image.sae.webservice.client.demo.service.SupervisionJobService;

/**
 * Principale classe executable de client demo<br>
 * 
 * 
 */
public final class Client {

   private static Keyspace keyspace;
   private static Cluster cluster;

   private Client() {

   }

   /**
    * Méthode principale de l'exécutable<br>
    * Redirection vers les autres méthodes exécutables de client demo<br>
    * <br>
    * paramètres ordonnés:<br>
    * <br>
    * arg[0]: action
    * <ul>
    * <li>ping: appel du ping (voir {@link PingService})</li>
    * <li>ping_secure: appel du ping sécurisé (voir {@link PingSecureService})
    * <ul>
    * <li>arg[1]: role</li>
    * </ul>
    * </li>
    * </ul>
    * 
    * @param args
    *           arguments
    */
   public static void main(String[] args) {

      if (ArrayUtils.isEmpty(args)) {
         throw new IllegalArgumentException("action required");
      }

      initialisationContext();

      ConcurrentLinkedQueue<String> listeUUIDJobLaunch = new ConcurrentLinkedQueue<String>();

      Object[] argsSup = new Object[2];
      argsSup[0] = listeUUIDJobLaunch;
      argsSup[1] = keyspace;
      SupervisionJobService.main(argsSup);

      Object[] newArgs = (Object[]) ArrayUtils.subarray(args, 1, args.length);

      if ("ping".equals(args[0])) {

         PingService.main(newArgs);

      } else if ("ping_secure".equals(args[0])) {

         PingSecureService.main(newArgs);

      } else if ("modification_masse".equals(args[0])) {

         ModificationMasseService.main(newArgs, listeUUIDJobLaunch);

      } else {
         throw new IllegalArgumentException("Unknown action defined: "
               + args[0]);
      }

      SupervisionJobService.waitFinish();

   }

   private static void initialisationContext() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      Map<String, String> credentials = new HashMap<String, String>() {
         /**
          * SUID
          */
         private static final long serialVersionUID = -2987951055674011491L;
         {
            put("username", "root");
         }
         {
            put("password", "regina4932");
         }
      };
      String servers = PropertiesLoader.getInstance().getUrlServeurCassandra();

      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            servers);
      hostConfigurator.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy());
      cluster = HFactory.getOrCreateCluster("SAE", hostConfigurator);
      keyspace = HFactory.createKeyspace("SAE", cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

   }

}
