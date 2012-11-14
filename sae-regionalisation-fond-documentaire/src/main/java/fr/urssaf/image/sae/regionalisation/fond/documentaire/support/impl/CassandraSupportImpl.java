/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.support.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

import fr.urssaf.image.sae.regionalisation.fond.documentaire.bean.CassandraConfig;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

/**
 * Implémentation du service {@link CassandraSupport}
 * 
 */
@Component
public class CassandraSupportImpl implements CassandraSupport {

   private Keyspace keyspace;
   private AstyanaxContext<Keyspace> context;

   @Autowired
   private CassandraConfig cassandraConfig;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void connect() {
      String servers;
      servers = cassandraConfig.getServers();

      AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            cassandraConfig.getUser(), cassandraConfig.getPassword());

      context = new AstyanaxContext.Builder().forCluster(
            cassandraConfig.getCluster()).forKeyspace(
            cassandraConfig.getKeyspace()).withAstyanaxConfiguration(
            new AstyanaxConfigurationImpl().setDiscoveryType(
                  NodeDiscoveryType.NONE).setDefaultReadConsistencyLevel(
                  ConsistencyLevel.CL_ONE).setDefaultWriteConsistencyLevel(
                  ConsistencyLevel.CL_QUORUM)).withConnectionPoolConfiguration(
            new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(
                  cassandraConfig.getPort()).setMaxConnsPerHost(1).setSeeds(
                  servers).setAuthenticationCredentials(credentials))
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
