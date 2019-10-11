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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.datastax.driver.core.Session;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;

/**
 *
 *
 */
public class CassandraServerBean implements InitializingBean, DisposableBean {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraServerBean.class);

   public static final String KEYSPACE_TU = "keyspace_tu";

   private String[] dataSets;

   private String[] dataSetsCql;

   private boolean startLocal = false;

   private boolean isCassandraStarted = false;

   private String hosts = null;

   private Session cqlSession;

   @Override
   public void afterPropertiesSet() throws Exception {
      LOG.debug("CassandraServerBean : startLocal={} - dataSet={}", startLocal, dataSets);
      // On effectue l'initialisation on charge les datasets Cql et Thrift
      init();
   }

   @Override
   public void destroy() throws Exception {
      // Pas besoin d'arrêter le serveur
   }

   /**
    * Réinitialise les données de la base cassandra locale, avec le jeu de
    * données utilisé initialement lors de la création du serveur
    *
    * @throws Exception
    *           Une erreur est survenue
    */
   public void init() throws Exception {
      if (!startLocal) {
         return;
      }
      // demarrage du serveur
      cassandraUnitInitilization();

      clearTables();
      loadInitialData();
   }

   private void loadInitialData() throws InterruptedException {
      boolean created = false;
      if(dataSets != null && dataSets.length > 0) {
         loadDatasets(true, MODE_API.HECTOR, dataSets);
         created = true;
      }
      if (dataSetsCql != null) {
         // La deuxième fois, on ne recrée le keyspace que s'il n'est pas déja créé
         loadDatasets(!created, MODE_API.DATASTAX, dataSetsCql);
      }
   }

   /**
    * Réinitialise les données de la base cassandra locale, avec le jeu de
    * données utilisé initialement lors de la création du serveur
    *
    * @throws Exception
    *           Une erreur est survenue
    */
   public void resetData() throws Exception {
      if (!startLocal) {
         return;
      }
      if (dataSets != null && dataSets.length > 0) {
         resetData(true, MODE_API.HECTOR, dataSets);
      }
      if (dataSetsCql != null && dataSetsCql.length > 0) {
         resetData(false, MODE_API.DATASTAX, dataSetsCql);
      }
   }

   /**
    * Réinitialise les données de la base cassandra locale en effectuant un truncate à l'aide du clearTables
    * on a laissé les paramètres qui ne sont pas utilisés pour eviter un refactoring de tous les projets utilisant cette méthode
    * 
    * @param dropAndCreateKeyspace
    *          Si true, suppression et creation d'un nouveau keyspace sinon false.
    * @param newDataSets
    *          Jeu(x) de données à utiliser
    * @throws Exception
    *           Une erreur est survenue
    */


   public void resetData(final boolean dropAndCreateKeyspace, final String mode, final String... newDataSets) throws Exception {
      if (!startLocal) {
         return;
      }
      clearTables();
      loadDatasets(dropAndCreateKeyspace, mode, newDataSets);
   }

   /**
    * Clears all the tables in the keyspace, without actually dropping the tables.
    * 
    * @throws InterruptedException
    */
   protected void clearTables() throws InterruptedException {
      if (!startLocal) {
         return;
      }
      final ClusterCQLConnecter cqlconnecter = new ClusterCQLConnecter(getCqlHosts());
      cqlconnecter.clearTables();
   }

   /**
    * @param dropAndCreateKeyspace
    * @param mode
    * @param newDataSets
    * @throws InterruptedException
    */
   private void loadDatasets(final boolean dropAndCreateKeyspace, final String mode, String... newDataSets) throws InterruptedException {
      if (ModeGestionAPI.MODE_API.HECTOR.equals(mode)) {
         if (newDataSets == null || newDataSets != null && newDataSets.length == 0) {
            newDataSets = dataSets;
         }
         // creer le connecteur au cluster thrift et charger les datasets
         final ClusterThriftConnecter connecter = new ClusterThriftConnecter(getThriftHosts());
         connecter.loadDataSetToServer(dropAndCreateKeyspace, newDataSets);


      } else if (ModeGestionAPI.MODE_API.DATASTAX.equals(mode)) {
         if (newDataSets == null || newDataSets != null && newDataSets.length == 0) {
            newDataSets = dataSetsCql;
         }
         // creer le connecteur au cluster cql et charger les datasets
         final ClusterCQLConnecter cqlconnecter = new ClusterCQLConnecter(getCqlHosts());
         cqlSession = cqlconnecter.getTestSession();
         // On inject les jeux de données via le connecteur cql
         cqlconnecter.loadDataSetToServer(dropAndCreateKeyspace, newDataSets);
      }
   }

   /**
    * @throws IOException
    */
   protected void cassandraUnitInitilization() throws Exception {

      if (isCassandraStarted) {
         return;
      }
      LOG.debug("CassandraServerBean : reseting data...");

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
      if(getCQLSession() != null) {
         isCassandraStarted = true;
      }
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
    * Recupere la session de connection au cluster via l'api datastax
    * @return
    * @throws InterruptedException 
    * @throws Exception
    */
   public Session getCQLSession() throws InterruptedException  {
      if(cqlSession == null) {
         final ClusterCQLConnecter cqlconnecter = new ClusterCQLConnecter(getCqlHosts());
         cqlSession =cqlconnecter.getTestSession();
      }
      return cqlSession;
   }

   /**
    * Dans le cas d'un cassandra zookeeper non local, il s'agit de la chaîne de
    * connexion
    *
    * @param hosts
    *          Chaîne de connexion (ex :
    *          "toto.toto.com:9160,titi.titi.com:9160")
    */
   public void setHosts(final String hosts) {
      this.hosts = hosts;
   }

   /**
    * get cql port
    * Renvoie la chaîne de connexion au serveur cassandra
    * @return
    */
   public final String getCqlHosts() {
      if (startLocal) {
         // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
         // tenter 3 fois
         // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
         // serveur cassandra local
         // ne se lance pas assez rapidement)
         return "localhost:9042,localhost:9042,localhost:9042";
      } else {
         return hosts;
      }
   }

   /**
    * Thrift port
    * Renvoie la chaîne de connexion au serveur cassandra
    *
    * @return chaîne de connexion
    */
   public final String getThriftHosts() {
      if (startLocal) {
         // Petite bidouille : on met le serveur localhost 3 fois : ça permet de
         // tenter 3 fois
         // l'opération si elle échoue la 1ere fois (ça arrive lorsque le
         // serveur cassandra local
         // ne se lance pas assez rapidement)
         return "localhost:9171,localhost:9171,localhost:9171";
      } else {
         return hosts;
      }
   }

   /**
    * @return the keyspaceTu
    */

   public String getKeyspaceTu() {
      return KEYSPACE_TU;
   }

   /**
    * Indique quel jeu de données cassandraUnit doit être utilisé lors de
    * l'initialisation du serveur cassandra
    *
    * @param dataSet
    *          Jeu de données
    */
   public void setDataSet(final String dataSet) {

      // Il peut y avoir plusieurs dataSets séparés par des ;
      dataSets = StringUtils.split(dataSet, ';');

   }

   /**
    * Indique quel jeu de données cassandraUnit doit être utilisé lors de
    * l'initialisation du serveur cassandra
    *
    * @param dataSet
    *          Jeu de données
    */
   public void setDataSetCql(final String dataSetCql) {

      // Il peut y avoir plusieurs dataSets séparés par des ;
      dataSetsCql = StringUtils.split(dataSetCql, ';');

   }

   /**
    * Indique s'il faut lancer un serveur cassandra local
    *
    * @param startLocal
    *          vrai s'il faut lancer un serveur local
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

   public boolean isCassandraStarted() {
      return isCassandraStarted;
   }

}
