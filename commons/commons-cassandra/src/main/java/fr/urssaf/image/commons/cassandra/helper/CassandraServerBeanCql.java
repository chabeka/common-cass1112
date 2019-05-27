package fr.urssaf.image.commons.cassandra.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
   * {@inheritDoc}
   */
  @Override
  public void resetData(final String... newDataSets) throws Exception {
    resetData(false, newDataSets);
  }

  /**
   * Réinitialise les données de la base cassandra locale, avec le jeu de
   * données utilisé initialement lors de la création du serveur
   *
   * @param dropAndCreateKeyspace
   *          Si true, suppression et creation d'un nouveau keyspace sinon false.
   * @param newDataSets
   * @throws Exception
   *           Une erreur est survenue
   */
  public void resetData(final boolean dropAndCreateKeyspace, String... newDataSets) throws Exception {
    if (!startLocal) {
      return;
    }

    if (newDataSets == null || newDataSets != null && newDataSets.length == 0) {
      newDataSets = dataSets;
    }
    // demarage du serveur
    cassandraUnitInitilization();

    // On attend que le serveur soit prêt
    waitForServer();

    // On inject les jeux de données
    if (newDataSets != null && newDataSets.length > 0) {
      final CQLDataLoader cqlDataLoader = new CQLDataLoader(testSession);
      cqlDataLoader.load(mergeDataSets(testSession, dropAndCreateKeyspace, newDataSets));
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
   * @param dropAndCreateKeyspace
   * @param dataSets
   *          fichier CQL
   * @return Le dataSet permettant contenant le load CQL
   */
  private FileCQLDataSet mergeDataSets(final Session session, final boolean dropAndCreateKeyspace, final String... dataSets) {
    LOG.debug("Merge des datasets en cours");
    final String logFinMerge = "Fin du merge des datasets";
    // On vérifie que le keyspace existe pour le créer si besoin
    final boolean createKeyspace = !(isExistKeyspace(session) && !dropAndCreateKeyspace);

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
            final InputStream dataSetStream = getClass().getClassLoader().getResourceAsStream(dataSet);
            if (dataSetStream != null) {
              try (BufferedReader br = new BufferedReader(new InputStreamReader(dataSetStream, "UTF-8"))) {
                String text = null;
                while ((text = br.readLine()) != null) {
                  bw.write(text);
                  bw.newLine();
                }
              } finally {
              }
            }
          }
        } catch (final IOException exp) {
          LOG.error("Erreur de merge des datasets : " + exp);
          LOG.warn("Ajout de la dataSet par défaut : " + fileDataSet);
          tmpFileDataSet = Paths.get(fileDataSet);
        }
      } else {
        LOG.warn("Le fichier de merge des datasets n'existe pas. Ajout de la premiere dataSet uniquement.");

        LOG.debug(logFinMerge);

        return new FileCQLDataSet(FileSystems.getDefault()
                                             .getPath(new UrlResource(getClass().getClassLoader().getResource(dataSets[0]).toURI()).getFile().getAbsolutePath())
                                             .toFile()
                                             .getAbsolutePath(),
                                  createKeyspace,
                                  dropAndCreateKeyspace,
                                  AbstractCassandraServer.KEYSPACE_TU);
      }

    } catch (final IOException | URISyntaxException e) {
      LOG.error("Erreur de merge des datasets : " + e);
      LOG.warn("Ajout de la dataSet par défaut : " + fileDataSet);
      tmpFileDataSet = Paths.get(fileDataSet);
    } finally {
      LOG.debug(logFinMerge);
    }

    // Renvoie l'objet Dataset fusionné
    return new FileCQLDataSet(tmpFileDataSet.toFile().getAbsolutePath(), createKeyspace, dropAndCreateKeyspace, AbstractCassandraServer.KEYSPACE_TU);
  }

  /**
   * Vérifie si le keyspace existe ou pas.
   * 
   * @param session
   *          Session de test
   * @return true si le keyspace existe, false sinon.
   */
  private boolean isExistKeyspace(final Session session) {
    boolean existKeyspace = false;

    // Vérification de l'existence du keyspace
    final String selectQuery = "SELECT keyspace_name FROM system.schema_keyspaces where keyspace_name='" + AbstractCassandraServer.KEYSPACE_TU + "'";
    final ResultSet keyspaceQueryResult = session.execute(selectQuery);

    existKeyspace = keyspaceQueryResult != null && keyspaceQueryResult.iterator() != null && keyspaceQueryResult.iterator().hasNext();

    // Si le keyspace existe, on l'utilise dans la session car cela n'est pas fait par défaut dans le librairie cassandra-unit.
    if (existKeyspace) {
      final String useQuery = "USE " + AbstractCassandraServer.KEYSPACE_TU;
      LOG.debug("executing : " + useQuery);
      session.execute(useQuery);
    }

    return existKeyspace;
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
