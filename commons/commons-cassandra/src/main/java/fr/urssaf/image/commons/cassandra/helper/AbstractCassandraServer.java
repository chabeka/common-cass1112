/**
 *
 */
package fr.urssaf.image.commons.cassandra.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cassandra.io.util.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 *
 */
public abstract class AbstractCassandraServer implements InitializingBean, DisposableBean {

   protected static final int WAIT_MAX_TRY = 12;

   protected static final long WAIT_MS = 1000;

   protected static final String TEST_CLUSTER_NAME = "TestCluster";

   public static final String KEYSPACE_TU = "KEYSPACETU";

   protected String[] dataSets;

   protected boolean startLocal = false;

   protected String hosts = null;

   /**
    * Indique quel jeu de données cassandraUnit doit être utilisé lors de
    * l'initialisation du serveur cassandra
    *
    * @param dataSet
    *           Jeu de données
    */
   public void setDataSet(final String dataSet) {

      // Il peut y avoir plusieurs dataSets séparés par des ;
      dataSets = StringUtils.split(dataSet, ';');

   }

   /**
    * Indique s'il faut lancer un serveur cassandra local
    *
    * @param startLocal
    *           vrai s'il faut lancer un serveur local
    */
   public void setStartLocal(final boolean startLocal) {
      this.startLocal = startLocal;
   }

   /**
    * @return vrai si le serveur cassandra est lancé localement
    */
   public boolean getStartLocal() {
      return startLocal;
   }

   @Override
   public void destroy() throws Exception {
      // Pas besoin d'arrêter le serveur
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      getLogger().debug("CassandraServerBean : startLocal={} - dataSet={}",
                        startLocal,
                        dataSets);
      resetData(dataSets);
   }

   /**
    * Réinitialise les données de la base cassandra locale, avec le jeu de
    * données utilisé initialement lors de la création du serveur
    *
    * @throws Exception
    *            Une erreur est survenue
    */
   public void resetData() throws Exception {
      resetData(dataSets);
   }

   /**
    * @throws IOException
    */
   protected void cassandraUnitInitilization() throws Exception {

      getLogger().debug("CassandraServerBean : reseting data...");

      // System.setProperty("cassandra.unsafesystem", "true");

      // ERREUR: https://github.com/jsevellec/cassandra-unit/issues/186
      System.setProperty("cassandra.storagedir", "/tmp/cassandra" + System.nanoTime());

      /*
       * Pour cause d'apparution de cette erreur (adresse ci-dessous) dû à un bug dans la version de cassandra unit utilisée,
       * https://github.com/jsevellec/cassandra-unit/issues/221
       * Lors de la création des différents repertoires de cassandra, le fichier de conf n'est pas initialisé
       * ce qui cré un NullPointerException à:
       * DatabaseDescriptor.createAllDirectories():
       * if (conf.data_file_directories.length == 0) ==> NullPointerException
       * car conf == null
       * Pour contourner le problème il y a 2 solutions:
       * solution 1:
       * On charge d'abord le context (fichier de conf) avant de lancer EmbeddedCassandraServerHelper
       */
      final String tmpDir = EmbeddedCassandraServerHelper.DEFAULT_TMP_DIR;
      final String yamlFile = "/" + EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE;
      final File file = new File(tmpDir + yamlFile);
      System.setProperty("cassandra.config", "file:" + file.getAbsolutePath());
      // On verifie que le fichier existe sinon on le cré
      if (!file.exists()) {
         copyYamlFile(yamlFile, tmpDir);
      }

      EmbeddedCassandraServerHelper.startEmbeddedCassandra(200000L);

   }

   /**
    * Creation du fichier cassandra.yaml
    *
    * @param resource
    * @param directory
    * @throws IOException
    */
   private static void copyYamlFile(final String resource, final String directory) throws IOException {
      FileUtils.createDirectory(directory);
      final String fileName = resource.substring(resource.lastIndexOf("/") + 1);
      final File file = new File(directory + System.getProperty("file.separator") + fileName);
      try (
            InputStream is = EmbeddedCassandraServerHelper.class.getResourceAsStream(resource);
            OutputStream out = new FileOutputStream(file)) {
         final byte buf[] = new byte[1024];
         int len;
         while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
         }
         out.close();
      }
   }

   /**
    * Dans le cas d'un cassandra zookeeper non local, il s'agit de la chaîne de
    * connexion
    *
    * @param hosts
    *           Chaîne de connexion (ex :
    *           "toto.toto.com:9160,titi.titi.com:9160")
    */
   public void setHosts(final String hosts) {
      this.hosts = hosts;
   }

   /**
    * @return the keyspaceTu
    */
   public String getKeyspaceTu() {
      return KEYSPACE_TU;
   }

   // Methodes abstraites

   protected abstract void waitForServer() throws InterruptedException;

   public abstract void shutdownTestCluster();

   public abstract Logger getLogger();

   public abstract String getHosts();

   public abstract void resetData(final String... newDataSets) throws Exception;
}
