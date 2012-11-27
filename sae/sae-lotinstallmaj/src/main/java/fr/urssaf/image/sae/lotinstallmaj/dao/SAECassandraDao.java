package fr.urssaf.image.sae.lotinstallmaj.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

/**
 * Classe d'accès à CASSANDRA - DAO
 * 
 * 
 */
@Component
public class SAECassandraDao {

   private String keySpaceName;

   private Keyspace keyspace;

   private Cluster cluster;

   private final Map<String, String> credentials;

   private CassandraConfig config;

   /**
    * GCgrace par fixé à 20 jours. Il est à 10 jours par défaut. Ça nous laisse
    * plus de temps pour réagir en cas de problème avec les repair.
    **/
   private static final int DEFAULT_GCGRACE = 1728000;

   /**
    * Constructeir
    * 
    * @param config
    *           paramètres de connexion CASSANDRA
    */
   @Autowired
   public SAECassandraDao(CassandraConfig config) {
      this.config = config;
      credentials = new HashMap<String, String>();
      credentials.put("username", config.getLogin());
      credentials.put("password", config.getPassword());
      CassandraHostConfigurator chc = new CassandraHostConfigurator(config
            .getHosts());
      this.cluster = HFactory
            .getOrCreateCluster("SAECluster", chc, credentials);
      this.keySpaceName = config.getKeyspaceName();
   }

   /**
    * @return le cluster
    */
   public final Cluster getCluster() {
      return cluster;
   }

   /**
    * @return le keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }

   /**
    * @return l'objet de définition du Keyspace
    */
   public final KeyspaceDefinition describeKeyspace() {

      return cluster.describeKeyspace(keySpaceName);

   }

   /**
    * 
    * @return le nom du keyspace
    */
   public final String getKeySpaceName() {
      return keySpaceName;
   }

   /**
    * connection au KeySpace avec les identifiants et les parametres de
    * consistence si on est pas connecté
    */
   public final void connectToKeySpace() {

      if (keyspace != null)
         return;
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      keyspace = HFactory.createKeyspace(keySpaceName, cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);

   }

   /**
    * Permet d'ajouter les options de la CF qui sont fréquement utilisées et de
    * créer la CF
    * 
    * @param c
    *           la définition d'une column family
    * @param blockUntilComplete
    *           boolean permettant de dire si on attend la réponse du/des
    *           serveur/s
    */
   public final void createColumnFamily(ColumnFamilyDefinition c,
         boolean blockUntilComplete) {
      // ajout des attributs par défauts
      addDefaultCFAttributs(c);
      // creattion de la column familly
      cluster.addColumnFamily(c, blockUntilComplete);
   }

   /**
    * Enregistre le n° de version de la base de données dans cassandra
    * 
    * @param version
    *           : n° de la version
    */
   public final void setDatabaseVersion(long version) {
      ColumnFamilyTemplate<String, String> template = getParametersTemplate();
      String key = "parameters";
      ColumnFamilyUpdater<String, String> updater = template.createUpdater(key);
      updater.setLong("versionBDD", version);
      template.update(updater);
   }

   /**
    * Renvoie le n° de version de la base de données qui est stockée dans
    * cassandra
    * 
    * @return n° de version
    */
   public final long getDatabaseVersion() {

      List<ColumnFamilyDefinition> listCFD = getColumnFamilyDefintion();
      if (!listCFD.contains("Parameters"))
         return 0;

      // On lit la version dans la base de données
      connectToKeySpace();
      ColumnFamilyTemplate<String, String> template = getParametersTemplate();
      String key = "parameters";
      return template
            .querySingleColumn(key, "versionBDD", LongSerializer.get())
            .getValue();
   }

   private ColumnFamilyTemplate<String, String> getParametersTemplate() {
      return new ThriftColumnFamilyTemplate<String, String>(keyspace,
            "Parameters", StringSerializer.get(), StringSerializer.get());
   }

   /**
    * @return la définition de la famille de colonnes
    */
   public final List<ColumnFamilyDefinition> getColumnFamilyDefintion() {
      KeyspaceDefinition ksDef = this.cluster.describeKeyspace(keySpaceName);
      List<ColumnFamilyDefinition> returnList = new ArrayList<ColumnFamilyDefinition>();
      if (ksDef != null) {
         returnList = ksDef.getCfDefs();
      }
      return returnList;
   }

   /**
    * @param cluster
    *           : le cluster cassandra
    * @return le facteur de réplication du keyspace Docubase
    */
   public final int getDocubaseReplicationFactor(Cluster cluster) {
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace("Docubase");
      if (keyspaceDef != null) {
         return keyspaceDef.getReplicationFactor();
      }
      // On est sûrement en test. On renvoie 1.
      return 1;
   }

   /**
    * Création d'un nouveau keyspace
    * 
    * @param ksDef
    *           définition du keyspace
    * @param blockUntilComplete
    *           boolean indiquant si le reste est bloqué en attendant la fin de
    *           ce traitement
    */
   public final void createNewKeySpace(KeyspaceDefinition ksDef,
         boolean blockUntilComplete) {
      cluster.addKeyspace(ksDef, blockUntilComplete);
   }

   /**
    * @return la configuration CASSANDRA
    */
   public final CassandraConfig getConfig() {
      return config;
   }

   /**
    * 
    * @param config
    *           la configuration CASSANDRA
    */
   public final void setConfig(CassandraConfig config) {
      this.config = config;
   }

   /**
    * Ajoute les options les plus fréquemment utilisées.
    * 
    * @param cfDef
    *           : définition de la CF
    */
   private void addDefaultCFAttributs(ColumnFamilyDefinition cfDef) {
      // GCgrace fixé à 20 jours.
      cfDef.setGcGraceSeconds(DEFAULT_GCGRACE);

      // Snappy compression
      Map<String, String> compressOptions = new HashMap<String, String>();
      compressOptions.put("sstable_compression", "SnappyCompressor");
      cfDef.setCompressionOptions(compressOptions);
      // FIXME FBON - A Vérifier
      Map<String, String> compactionOptions = new HashMap<String, String>();
      compactionOptions.put("sstable_size_in_mb", "200");

      // Leveled compaction.
      cfDef.setCompactionStrategy("LeveledCompactionStrategy");
      cfDef.setCompactionStrategyOptions(compactionOptions);
   }

}
