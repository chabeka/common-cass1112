package fr.urssaf.image.commons.cassandra.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.FileCQLDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;

/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local par un
 * bean spring.
 */
public class CassandraServerBeanCql extends AbstractCassandraServer {

  private static final Logger LOG = LoggerFactory.getLogger(CassandraServerBeanCql.class);

  private Cluster testCluster = null;

  private Session testSession = null;

  /**
   * Réinitialise les données de la base cassandra locale
   *
   * @param newDataSets
   *          Jeu(x) de données à utiliser
   * @throws Exception
   *           Une erreur est survenue
   */
  @Override
  public void resetData(final String... newDataSets) throws Exception {
    if (!startLocal) {
      return;
    }
    // demarage du serveur
    cassandraUnitInitilization();

    // On attend que le serveur soit prêt
    waitForServer();

    // On inject les jeux de données
    if (newDataSets != null && newDataSets.length > 0) {
      final CQLDataLoader cqlDataLoader = new CQLDataLoader(testSession);
      cqlDataLoader.load(mergeDataSets(testSession, newDataSets));
    }
  }

  /**
   * Il arrive que le serveur cassandra local mette du temps avant d'être
   * opérationnel. Cette méthode fait en sorte d'attendre jusqu'à ce qu'il soit
   * opérationnel
   *
   * @throws InterruptedException
   *           : on a été interrompu
   */
  @Override
  @SuppressWarnings("resource")
  protected void waitForServer() throws InterruptedException {
    Cluster cluster = getTestCluster();
    for (int i = 0; i < WAIT_MAX_TRY; i++) {
      try {
        cluster.getClusterName();
        break;
      } catch (final Exception e) {
        LOG.debug("CassandraServerBean : waiting for server (" + i + ")...");
        Thread.sleep(WAIT_MS);
        LOG.debug("CassandraServerBean : reseting cluster (" + i + ")...");
        try {
          testCluster.close();
        } catch (final Exception ex) {
          LOG.debug("CassandraServerBean : error while shutdowning cluster", ex);
        }
        cluster = getTestCluster();
      }
    }
  }

  private Cluster getTestCluster() {
    if (testCluster == null) {
      final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(getHosts());
      hostConfigurator.setMaxActive(1);

      final Builder testBuilder = Cluster.builder().addContactPoints("localhost").withClusterName(TEST_CLUSTER_NAME).withPort(9142);
      testCluster = Cluster.buildFrom(testBuilder);
      final Session session = testCluster.connect();
      testSession = session;
    }
    testCluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(20000000);
    testCluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(20000000);
    return testCluster;
  }

  /**
   * Renvoie la chaîne de connexion au serveur cassandra
   *
   * @return chaîne de connexion
   */
  @Override
  public final String getHosts() {
    if (startLocal) {
      // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
      // tenter 3 fois
      // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
      // serveur cassandra local
      // ne se lance pas assez rapidement)
      return "localhost:9142,localhost:9142,localhost:9142";
    } else {
      return hosts;
    }
  }

  /**
   * Methode permettant de merger les data sets
   * 
   * @param testSession2
   * @param dataSets
   *          fichier CQL
   * @return Le dataSet permettant contenant le load CQL
   */
  private FileCQLDataSet mergeDataSets(final Session session, final String... dataSets) {
    // On vérifie que le keyspace existe pour le créer si besoin
    final boolean createKeyspace = isCreateKeyspace(session);

    // Vérification des paramètres d'entrée
    Assert.notEmpty(dataSets, "La liste des Dataset est vide");
    final String fileDataSet = "cassandra-local-datasets/migration-cqltable-common.cql";
    final String fileDataSetTmp = "target/tmp/migration-cqltable-common.cql";
    Path tmpFileDataSet = Paths.get(fileDataSetTmp);

    try {

      if (!Files.exists(tmpFileDataSet.getParent())) {
        Files.createDirectories(tmpFileDataSet.getParent());
      }

      if (tmpFileDataSet != null && tmpFileDataSet.toFile() != null) {
        try (BufferedWriter bw = Files.newBufferedWriter(tmpFileDataSet, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
          for (final String dataSet : dataSets) {
            final URI uriDts = getClass().getClassLoader().getResource(dataSet).toURI();
            final Path path = FileSystems.getDefault().getPath(new UrlResource(uriDts).getFile().getAbsolutePath());
            if (path != null && path.getFileName().toString().endsWith(".cql")) {
              try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
                String text = null;
                while ((text = br.readLine()) != null) {
                  bw.write(text);
                  bw.newLine();
                }
              } finally {
              }
            }
          }
        } catch (final IOException | URISyntaxException exp) {
          LOG.error("Erreur de merge des datasets : " + exp);
          LOG.warn("Ajout de la dataSet par défaut : " + fileDataSet);
          tmpFileDataSet = Paths.get(fileDataSet);
        }
      } else {
        LOG.warn("Le fichier de merge des datasets n'existe pas. Ajout de la premiere dataSet uniquement.");

        return new FileCQLDataSet(FileSystems.getDefault()
                                             .getPath(new UrlResource(getClass().getClassLoader().getResource(dataSets[0]).toURI()).getFile().getAbsolutePath())
                                             .toFile()
                                             .getAbsolutePath(),
                                  createKeyspace,
                                  false,
                                  AbstractCassandraServer.KEYSPACE_TU);
      }

    } catch (final IOException | URISyntaxException e) {
      LOG.error("Erreur de merge des datasets : " + e);
      LOG.warn("Ajout de la dataSet par défaut : " + fileDataSet);
      tmpFileDataSet = Paths.get(fileDataSet);
    }

    // Renvoie l'objet Dataset fusionné
    return new FileCQLDataSet(tmpFileDataSet.toFile().getAbsolutePath(), createKeyspace, false, AbstractCassandraServer.KEYSPACE_TU);
  }

  /**
   * Vérifie s'il faut créer le keyspace ou pas.
   * 
   * @param session
   *          Session de test
   * @return true s'il faut crer le keyspace, false sinon.
   */
  private boolean isCreateKeyspace(final Session session) {
    boolean createKeyspace = false;

    // Vérification de l'existence du keyspace
    final String selectQuery = "SELECT keyspace_name FROM system.schema_keyspaces where keyspace_name='" + AbstractCassandraServer.KEYSPACE_TU + "'";
    final ResultSet keyspaceQueryResult = session.execute(selectQuery);

    createKeyspace = !(keyspaceQueryResult != null && keyspaceQueryResult.iterator() != null && keyspaceQueryResult.iterator().hasNext());

    // Si le keyspace existe, on l'utilise dans la session car cela n'est pas fait par défaut dans le librairie cassandra-unit.
    if (!createKeyspace) {
      final String useQuery = "USE " + AbstractCassandraServer.KEYSPACE_TU;
      LOG.debug("executing : " + useQuery);
      session.execute(useQuery);
    }

    return createKeyspace;
  }

  /**
   * Arrête le cluster (partie cliente) de test
   */
  @Override
  public final void shutdownTestCluster() {
    if (testCluster != null) {
      testCluster.close();
    }
  }

  /**
   * @return the testSession
   */
  public Session getTestSession() {
    return testSession;
  }

  /**
   * @return the keyspaceTu
   */
  @Override
  public String getKeyspaceTu() {
    return KEYSPACE_TU;
  }

  @Override
  public Logger getLogger() {
    return LOG;
  }
  //
}
