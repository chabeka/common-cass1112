package fr.urssaf.image.commons.cassandra.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.AbstractResource;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;

/**
 * Factory pour récupérer une connexion à cassandra.
 * Utile pour faire des instantiations via spring
 */
public final class CassandraCQLClientFactory implements DisposableBean {

  private static final Logger LOG = LoggerFactory.getLogger(CassandraCQLClientFactory.class);

  private final static String SAE_CONFIG_CASSANDRA_TRANSFERT_CONFIG = "sae.cassandra.transfert.cheminFichierConfig";

  private final static String CASSANDRA_START_LOCAL = "cassandra.startlocal";

  private final static String CASSANDRA_HOSTS = "cassandra.hosts";

  private final static String CASSANDRA_USERNAME = "cassandra.username";

  private final static String CASSANDRA_PASSWORD = "cassandra.password";

  private final static String CASSANDRA_KEYSPACE = "cassandra.keyspace";

  private final static String CASSANDRA_DATASET = "cassandra.dataset";

  private final static String SEPARATOR_SPLIT_HOSTS = ",";

  private final static String SEPARATOR_SPLIT_HOST_PORT = ":";

  private final static int CASSANDRA_DEFAULT_PORT = 9142;

  // En mode "cassandra local" uniquement. Unité : milli-secondes
  // private static final int DELAY_BETWEEN_RETRIES = 1000;

  private Cluster cluster;

  private Session session;

  private String keyspace;

  CassandraServerBeanCql server;

  /**
   * Constructeur utilisé pour le transfert.
   *
   * @param saeConfigResource
   *          prend en parametres le fichier de configuration du sae.
   * @throws InterruptedException
   */
  public CassandraCQLClientFactory(final AbstractResource saeConfigResource) throws InterruptedException {

    final Properties saeProperties = new Properties();

    try {
      saeProperties.load(saeConfigResource.getInputStream());
    }
    catch (final IOException e) {
      // throw new CassandraConfigurationException(e);
    }

    final String pathConfCassandraTransfert = saeProperties
                                                           .getProperty(SAE_CONFIG_CASSANDRA_TRANSFERT_CONFIG);

    // on teste si la connexion de transfert est configuree ou non
    if (StringUtils.isNotBlank(pathConfCassandraTransfert)) {

      // la connexion cassandra de transfert est configuree
      // on initialise les objets
      final Properties cassandraProp = new Properties();

      try {
        cassandraProp.load(new FileInputStream(pathConfCassandraTransfert));
      }
      catch (final IOException e) {
        // throw new CassandraConfigurationException(e);
      }

      final String hosts = cassandraProp.getProperty(CASSANDRA_HOSTS);
      final String dataset = cassandraProp.getProperty(CASSANDRA_DATASET);
      final String userName = cassandraProp.getProperty(CASSANDRA_USERNAME);
      final String password = cassandraProp.getProperty(CASSANDRA_PASSWORD);
      final String keyspaceName = cassandraProp.getProperty(CASSANDRA_KEYSPACE);
      final Boolean startLocal = Boolean.valueOf(cassandraProp.getProperty(CASSANDRA_START_LOCAL));

      final CassandraServerBeanCql cassandraServer = new CassandraServerBeanCql();
      cassandraServer.setDataSet(dataset);
      cassandraServer.setHosts(hosts);
      cassandraServer.setStartLocal(startLocal);

      initCassandra(cassandraServer, keyspaceName, userName, password);
    } else {
      // on est dans le cas ou la connexion de transfert n'est pas configuree
      // on utilise un keyspace bidon
      this.session = null;
      this.keyspace = null;
      this.server = null;
    }
  }

  /**
   * Instancie un objet CassandraClientFactory, et ouvre une connexion à cassandra.
   * On prépare un objet "Keyspace" correspondant à une connexion à un serveur
   * cassandra. Utile pour simplifier l'instanciation via spring
   *
   * @param cassandraServer
   *          Correspond au(x) serveur(s) qu'on tente de joindre
   * @param keyspaceName
   *          Nom du keyspace
   * @param userName
   *          Nom d'utilisateur, pour l'authentification
   * @param password
   *          Mot de passe, pour l'authentification
   * @throws InterruptedException
   *           Ou nous a demandé de nous arrêter alors on s'arrête
   */
  public CassandraCQLClientFactory(final CassandraServerBeanCql cassandraServer,
                                   final String keyspaceName, final String userName, final String password)
      throws InterruptedException {
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
  private void initCassandra(final CassandraServerBeanCql cassandraServer,
                             final String keyspaceName, final String userName, final String password)
      throws InterruptedException {
    // LOG.debug("Creation d'un client cassandra utilisant les serveurs suivants : " + cassandraServer.getHosts());
    final QueryOptions qo = new QueryOptions().setConsistencyLevel(ConsistencyLevel.QUORUM);
    final PoolingOptions poolingOptions = new PoolingOptions();

    if (cassandraServer.getStartLocal()) {
      poolingOptions
                    .setConnectionsPerHost(HostDistance.LOCAL, 1, 1)
                    .setConnectionsPerHost(HostDistance.REMOTE, 1, 1);
      this.session = cassandraServer.getTestSession();
      this.cluster = cassandraServer.getTestSession().getCluster();

    } else {
      final List<InetSocketAddress> adresses = getInetSocketAddressList(cassandraServer);
      this.cluster = Cluster.builder()
                            .addContactPointsWithPorts(adresses)
                            .withCredentials(userName, password)
                            .withPoolingOptions(poolingOptions)
                            .withQueryOptions(qo)
                            .build();
      this.session = this.cluster.connect(keyspaceName);
    }
    this.keyspace = keyspaceName;
    this.server = cassandraServer;
    // final CodecRegistry myCodecRegistry = this.cluster.getConfiguration().getCodecRegistry();

  }

  /**
   * Liste les differentes adresses renseignées dans le fichier de configuration de Cassandra (sae-config-cassandra.properties).
   *
   * @param cassandraServer
   *          Paramètres de connection à Cassandra
   */
  private List<InetSocketAddress> getInetSocketAddressList(final CassandraServerBeanCql cassandraServer) {
    final List<InetSocketAddress> adresses = new ArrayList<InetSocketAddress>();
    if (cassandraServer.getHosts() != null && !cassandraServer.getHosts().isEmpty()) {
      if (cassandraServer.getHosts().contains(SEPARATOR_SPLIT_HOSTS)) {
        for (final String host : cassandraServer.getHosts().split(SEPARATOR_SPLIT_HOSTS)) {
          if (host != null && !host.isEmpty()) {
            final InetSocketAddress addr = getInetSocketAddress(host, cassandraServer);
            if (addr != null) {
              adresses.add(addr);
            }
          }
        }
      } else {
        final InetSocketAddress addr = getInetSocketAddress(cassandraServer.getHosts(), cassandraServer);
        if (addr != null) {
          adresses.add(addr);
        }
      }
    }

    return adresses;
  }

  /**
   * Créer une adresse de type InetSocketAddress à partir d'un hostname ou d'une IP comprenant ou pas un port.
   *
   * @param host
   *          hostname ou d'une IP comprenant ou pas un port
   * @param cassandraServer
   *          Paramètres de connection à Cassandra
   */
  private InetSocketAddress getInetSocketAddress(final String host, final CassandraServerBeanCql cassandraServer) {
    InetSocketAddress addr = null;
    if (host.contains(SEPARATOR_SPLIT_HOST_PORT)) {
      final String[] inetAddressParam = host.split(SEPARATOR_SPLIT_HOST_PORT);
      if (inetAddressParam.length == 2) {
        try {
          addr = new InetSocketAddress(inetAddressParam[0], Integer.parseInt(inetAddressParam[1]));
        }
        catch (final Exception e) {
          LOG.error("Le port n'est pas un entier. La connection vers le serveur suivante ne pourra etre realise : " + cassandraServer.getHosts());
        }
      } else {
        LOG.error("Seul le hostname (ou IP) et le port sont autorises. La connection vers le serveur suivante ne pourra etre realise : "
            + cassandraServer.getHosts());
      }
    } else {
      addr = new InetSocketAddress(host, CASSANDRA_DEFAULT_PORT);
    }

    return addr;
  }

  /**
   *
   */
  /*
   * private void launchSessionMonitor() {
   * final LoadBalancingPolicy loadBalancingPolicy = cluster.getConfiguration().getPolicies().getLoadBalancingPolicy();
   * final PoolingOptions poolingOptions = cluster.getConfiguration().getPoolingOptions();
   * final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
   * scheduled.scheduleAtFixedRate(() -> {
   * final Session.State state = session.getState();
   * for (final Host host : state.getConnectedHosts()) {
   * final HostDistance distance = loadBalancingPolicy.distance(host);
   * final int connections = state.getOpenConnections(host);
   * final int inFlightQueries = state.getInFlightQueries(host);
   * LOG.debug(String.format("%s connections=%d, current load=%d, maxoad=%d%n",
   * host,
   * connections,
   * inFlightQueries,
   * connections * poolingOptions.getMaxRequestsPerConnection(distance)));
   * }
   * }, 5, 5, TimeUnit.SECONDS);
   * }
   */

  @Override
  public void destroy() throws Exception {
    if (this.session != null) {
      this.session.close();
      this.session = null;
    }

    if (this.cluster != null) {
      this.cluster.close();
      this.cluster = null;
    }
  }

  /**
   * Getter
   *
   * @return the session
   */
  public Session getSession() {
    return session;
  }

  /**
   * @return the keyspace
   */
  public String getKeyspace() {
    return keyspace;
  }

  /**
   * @return the cluster
   */
  public Cluster getCluster() {
    return cluster;
  }

  /**
   * @return the server
   */
  public CassandraServerBeanCql getServer() {
    return server;
  }

}
