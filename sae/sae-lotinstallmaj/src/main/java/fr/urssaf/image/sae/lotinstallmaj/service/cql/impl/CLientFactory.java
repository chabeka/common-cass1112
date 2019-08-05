package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

public interface CLientFactory {
		
	   final static String SEPARATOR_SPLIT_HOST_PORT = ":";
	   final static String SEPARATOR_SPLIT_HOSTS = ",";
	   final static int CASSANDRA_DEFAULT_PORT = 9142;
	   /**
	  * 
	  * @param config
	  * @throws InterruptedException
	  */
	  public default Cluster connectToCluster() throws InterruptedException {
	     final QueryOptions qo = new QueryOptions().setConsistencyLevel(ConsistencyLevel.QUORUM);
	     final PoolingOptions poolingOptions = new PoolingOptions();


	     final List<InetSocketAddress> adresses = getInetSocketAddressList(getConfig());
	     Cluster cluster = Cluster.builder()
	                    .addContactPointsWithPorts(adresses)
	                    .withCredentials(getConfig().getLogin(), getConfig().getPassword())
	                    .withPoolingOptions(poolingOptions)
	                    .withQueryOptions(qo)
	                    .build();
	     return cluster;
	  }
	   /**
	   * @param cassandraServer
	   *          Paramètres de connection à Cassandra
	   */
	  public default List<InetSocketAddress> getInetSocketAddressList(final CassandraConfig config) {
	    final List<InetSocketAddress> adresses = new ArrayList<>();
	    if (config.getHosts() != null && !config.getHosts().isEmpty()) {
	      if (config.getHosts().contains(SEPARATOR_SPLIT_HOSTS)) {
	        for (final String host : config.getHosts().split(SEPARATOR_SPLIT_HOSTS)) {
	          if (host != null && !host.isEmpty()) {
	            final InetSocketAddress addr = getInetSocketAddress(host);
	            if (addr != null) {
	              adresses.add(addr);
	            }
	          }
	        }
	      } else {
	        final InetSocketAddress addr = getInetSocketAddress(config.getHosts());
	        if (addr != null) {
	          adresses.add(addr);
	        }
	      }
	    }

	    return adresses;
	  }
	  /**
	   * Créer une adresse de type InetSocketAddress à partir d'un hostname ou
	   * d'une IP comprenant ou pas un port.
	   *
	   * @param host
	   *          hostname ou d'une IP comprenant ou pas un port
	   * @param cassandraServer
	   *          Paramètres de connection à Cassandra
	   */
	  public default InetSocketAddress getInetSocketAddress(String host) {
	    InetSocketAddress addr = null;
	    if (host.contains(SEPARATOR_SPLIT_HOST_PORT)) {
	      final String[] inetAddressParam = host.split(SEPARATOR_SPLIT_HOST_PORT);
	      if (inetAddressParam.length == 2) {
	        try {
	          addr = new InetSocketAddress(inetAddressParam[0], Integer.parseInt(inetAddressParam[1]));
	        } catch (final Exception e) {
	          //LOG.error("Le port n'est pas un entier. La connection vers le serveur suivante ne pourra etre realise : " + cassandraServer.getHosts());
	        }
	      } else {
	    	  getLogger().error("Seul le hostname (ou IP) et le port sont autorises. La connection vers le serveur suivante ne pourra etre realise : "
	            + host);
	      }
	    } else {
	      addr = new InetSocketAddress(host, CASSANDRA_DEFAULT_PORT);
	    }

	    return addr;
	}
	
	   
	public Logger getLogger(); 
	
	/** Confi pour l'acces au server cassandra */
	public CassandraConfig getConfig();
	
	/** La session sur le keyspace*/
	public Session getSession();
	
	/** Pour la connection au keyspace */
	public void connectToKeyspace();
}
