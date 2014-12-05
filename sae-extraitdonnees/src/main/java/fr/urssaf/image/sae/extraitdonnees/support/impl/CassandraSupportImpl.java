package fr.urssaf.image.sae.extraitdonnees.support.impl;

import org.springframework.stereotype.Component;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.image.sae.extraitdonnees.bean.CassandraConfig;
import fr.urssaf.image.sae.extraitdonnees.support.CassandraSupport;

/**
 * Implémentation du support {@link CassandraSupport}
 */
@Component
public class CassandraSupportImpl implements CassandraSupport {

   private Keyspace keyspace;
   private AstyanaxContext<Keyspace> context;

   private static final int TIME_OUT = 10000; // 10 secondes

   /**
    * {@inheritDoc}
    */
   @Override
   public final void connect(CassandraConfig cassandraConfig) {
      String servers;
      servers = cassandraConfig.getServers();

      AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            cassandraConfig.getUser(), cassandraConfig.getPassword());

      context = new AstyanaxContext.Builder().forCluster("MyCluster")
            .forKeyspace("Docubase").withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl().setDiscoveryType(
                        NodeDiscoveryType.NONE).setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_QUORUM)
                        .setDefaultWriteConsistencyLevel(
                              ConsistencyLevel.CL_QUORUM))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(cassandraConfig.getPort()).setMaxConnsPerHost(
                              1).setSeeds(servers)
                        .setAuthenticationCredentials(credentials)
                        .setConnectTimeout(TIME_OUT))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();

      keyspace = context.getEntity();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void disconnect() {
      context.shutdown();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Keyspace getKeySpace() {
      return keyspace;
   }

}
