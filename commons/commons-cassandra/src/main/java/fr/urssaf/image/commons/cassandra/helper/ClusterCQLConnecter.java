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
import java.util.Collection;

import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.FileCQLDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;

public class ClusterCQLConnecter {

  private static final Logger LOG = LoggerFactory.getLogger(ClusterCQLConnecter.class);

  private static final long WAIT_MS = 1000;
  private static final int WAIT_MAX_TRY = 12;
  private static final String TEST_CLUSTER_NAME = "TestCluster";

  private Cluster testCluster = null;
  private Session testSession = null;

  private Builder testBuilder = null;
  protected String[] dataSets;

  private final String hosts;


  public ClusterCQLConnecter(final String hosts) throws InterruptedException {
    this.hosts = hosts;
    waitForServer();
  }

  /**
   * Il arrive que le serveur cassandra local mette du temps avant d'�tre
   * op�rationnel. Cette m�thode fait en sorte d'attendre jusqu'� ce qu'il soit
   * op�rationnel
   *
   * @throws InterruptedException
   *           : on a �t� interrompu
   */
  @SuppressWarnings("resource")
  private void waitForServer() throws InterruptedException {
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
      try {
        final CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(hosts);
        hostConfigurator.setMaxActive(1);
        if (testBuilder == null) {
          testBuilder = Cluster.builder().addContactPoints("localhost").withClusterName(TEST_CLUSTER_NAME).withPort(9142).withoutJMXReporting();
        }
        testCluster = Cluster.buildFrom(testBuilder);
        final Session session = testCluster.connect();
        testSession = session;
      } catch (final Throwable e) {
        LOG.error("Erreur technique : " + e.getMessage());
      }
      testCluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(20000000);
      testCluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(20000000);
    }

    return testCluster;
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
  private FileCQLDataSet mergeCqlDataSets(final boolean dropAndCreateKeyspace, final String... dataSets) {
    LOG.debug("Merge des datasets en cours");
    final String logFinMerge = "Fin du merge des datasets";
    // On v�rifie que le keyspace existe pour le cr�er si besoin
    final boolean createKeyspace = !(isExistKeyspace(testSession) && !dropAndCreateKeyspace);

    // V�rification des param�tres d'entr�e
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
          LOG.warn("Ajout de la dataSet par d�faut : " + fileDataSet);
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
                                  CassandraServerBean.KEYSPACE_TU);
      }

    } catch (final IOException | URISyntaxException e) {
      LOG.error("Erreur de merge des datasets : " + e);
      LOG.warn("Ajout de la dataSet par d�faut : " + fileDataSet);
      tmpFileDataSet = Paths.get(fileDataSet);
    } finally {
      LOG.debug(logFinMerge);
    }

    // Renvoie l'objet Dataset fusionn�
    return new FileCQLDataSet(tmpFileDataSet.toFile().getAbsolutePath(), createKeyspace, dropAndCreateKeyspace, CassandraServerBean.KEYSPACE_TU);
  }

  /**
   * V�rifie si le keyspace existe ou pas.
   * 
   * @param session
   *          Session de test
   * @return true si le keyspace existe, false sinon.
   */
  private boolean isExistKeyspace(final Session session) {
    boolean existKeyspace = false;

    // V�rification de l'existence du keyspace
    final String selectQuery = "SELECT keyspace_name FROM system.schema_keyspaces where keyspace_name='" + CassandraServerBean.KEYSPACE_TU + "'";
    final ResultSet keyspaceQueryResult = session.execute(selectQuery);

    existKeyspace = keyspaceQueryResult != null && keyspaceQueryResult.iterator() != null && keyspaceQueryResult.iterator().hasNext();

    // Si le keyspace existe, on l'utilise dans la session car cela n'est pas fait par d�faut dans le librairie cassandra-unit.
    if (existKeyspace) {
      final String useQuery = "USE " + CassandraServerBean.KEYSPACE_TU;
      LOG.debug("executing : " + useQuery);
      session.execute(useQuery);
    }

    return existKeyspace;
  }

  public void loadDataSetToServer(final boolean dropAndCreateKeyspace, final String... newDataSets) {
    // On inject les jeux de donn�es
    if (newDataSets != null && newDataSets.length > 0) {
      final CQLDataLoader cqlDataLoader = new CQLDataLoader(testSession);
      cqlDataLoader.load(mergeCqlDataSets(dropAndCreateKeyspace, newDataSets));
    }
  }
  /**
   * Arr�te le cluster (partie cliente) de test
   */
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
   * M�thode qui efface le contenu de toutes les tables gr�ce � l'ex�cution d'un truncate
   */
  public final void clearTables() {
    final KeyspaceMetadata keyspace = testCluster.getMetadata().getKeyspace(CassandraServerBean.KEYSPACE_TU);
    if (keyspace != null) {
      final Collection<TableMetadata> tables = keyspace.getTables();
      tables.forEach(table -> testSession.execute(QueryBuilder.truncate(table)));
    }
  }
}
