package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;


/**
 * Classe permettant la mise à jour du schéma du keyspace SAE dans cassandra
 *
 */
public class SAECassandraUpdater {

   /**
    * Nom du keyspace
    */
   private final String ksName;
   
   private final Cluster cluster; 
   private Keyspace keyspace;
   private final Map<String, String> credentials;

   // LOGGER
   private static final Logger LOG = LoggerFactory
                                       .getLogger(SAECassandraUpdater.class);
   
   // GCgrace par fixé à 20 jours.
   // Il est à 10 jours par défaut. Ça nous laisse plus de temps pour réagir en cas de problème avec les repair.
   private static final int DEFAULT_GCGRACE = 1728000;
   
   /**
    * Constructeur
    * @param config  : configuration d'accès au cluster cassandra
    */
   public SAECassandraUpdater(CassandraConfig config) {
      ksName = config.getKeyspaceName();
      credentials = new HashMap<String, String>();
      credentials.put("username", config.getLogin());
      credentials.put("password", config.getPassword());
      CassandraHostConfigurator chc = new CassandraHostConfigurator(config.getHosts());
      cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
   }
   
   /**
    * Version 1 : création du keyspace SAE
    */
   public final void updateToVersion1() {
      
      long version = getDatabaseVersion();
      if (version >= 1) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }
      
      LOG.info("Création du keyspace SAE en version 1");
      
      // On crée le keyspace "SAE" n'existe pas déjà.
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(ksName);
      if (keyspaceDef == null) {
         // Create the keyspace definition
         // Le facteur de réplication utilisé est le même que celui utilisé pour le keyspace "Docubase", soit
         // 3 pour l'environnement de production, et de 1 à 3 pour les autres.
         int replicationFactor = getDocubaseReplicationFactor(cluster);
         
         KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(ksName,          
               ThriftKsDef.DEF_STRATEGY_CLASS,
               replicationFactor, new ArrayList<ColumnFamilyDefinition>());
         // Add the schema to the cluster.
         // "true" as the second param means that Hector will block until all nodes see the change.
         cluster.addKeyspace(newKeyspace, true);
      }
      
      // On se connecte au keyspace maintenant qu'il existe
      connectToKeyspace();
      
      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();
      
      // JobInstance
      // Cette CF a une colonne indexée : jobKey
      BasicColumnDefinition jobKeyCol = new BasicColumnDefinition();
      jobKeyCol.setName(StringSerializer.get().toByteBuffer("jobKey"));
      jobKeyCol.setIndexName("jobKey_idx");
      jobKeyCol.setValidationClass("BytesType");
      jobKeyCol.setIndexType(ColumnIndexType.KEYS);
      List<ColumnDefinition> colDefs = new ArrayList<ColumnDefinition>();
      colDefs.add(jobKeyCol);
      ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(ksName, 
            "JobInstance", ComparatorType.BYTESTYPE, colDefs);
      cfDefs.add(cfDef);
      
      // JobInstancesByName
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobInstancesByName", ComparatorType.LONGTYPE));
      
      // JobInstanceToJobExecution
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobInstanceToJobExecution", ComparatorType.LONGTYPE));
      
      // JobExecution
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobExecution", ComparatorType.BYTESTYPE));
      
      // JobExecutions
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobExecutions", ComparatorType.LONGTYPE));
      
      // JobExecutionsRunning
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobExecutionsRunning", ComparatorType.LONGTYPE));
      
      // JobExecutionToJobStep
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobExecutionToJobStep", ComparatorType.LONGTYPE));
      
      // JobStep
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobStep", ComparatorType.BYTESTYPE));
      
      // JobSteps
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobSteps", ComparatorType.LONGTYPE));
      
      // Sequences
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "Sequences", ComparatorType.BYTESTYPE));
      
      // JobRequest
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobRequest", ComparatorType.BYTESTYPE));
      
      // JobsQueue
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "JobsQueue", ComparatorType.TIMEUUIDTYPE));

      // Parameters
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, 
            "Parameters", ComparatorType.BYTESTYPE));

      // Ajoute les options les plus courantes à chacune des CF
      for(ColumnFamilyDefinition c : cfDefs) {
         addDefaultCFAttributs(c);
      }

      // Création des CF
      for(ColumnFamilyDefinition c : cfDefs) {
         if (cfExists(keyspaceDef, c.getName())) {
            LOG.info("La famille de colonnes " + c.getName() + " est déjà existante");
         }
         else {
            LOG.info("Création de la famille de colonnes " + c.getName());
            cluster.addColumnFamily(c, true);
         }
      }

      // On positionne la version à 1
      setDatabaseVersion(1L);

   }

   private void connectToKeyspace() {
      if (keyspace != null) return;
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      keyspace = HFactory.createKeyspace(ksName, cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
   }
   
   /**
    * Enregistre le n° de version de la base de données dans cassandra
    * @param version : n° de la version
    */
   private void setDatabaseVersion(long version) {
      ColumnFamilyTemplate<String, String> template = getParametersTemplate();
      String key = "parameters";
      ColumnFamilyUpdater<String, String> updater = template.createUpdater(key);
      updater.setLong("versionBDD", version);
      template.update(updater);
   }

   /**
    * Renvoie le n° de version de la base de données qui est stockée dans cassandra
    * @return n° de version
    */
   public final long getDatabaseVersion() {
      // On regarde si le keyspace "SAE" existe.
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(ksName);
      if (keyspaceDef == null) return 0;
      
      // On regarde si la CF Parameters existe
      if (!cfExists(keyspaceDef, "Parameters")) return 0;
      
      // On lit la version dans la base de données
      connectToKeyspace();
      ColumnFamilyTemplate<String, String> template = getParametersTemplate();
      String key = "parameters";
      return template.querySingleColumn(key, "versionBDD", LongSerializer.get()).getValue();
   }
   
   /**
    * @param keyspaceDef   Keyspace definition
    * @param cfName        Name of the CF to search for
    * @return true if the CF exists in keyspace
    */
   private boolean cfExists (KeyspaceDefinition keyspaceDef, String cfName) {
      if (keyspaceDef == null) return false;
      for (ColumnFamilyDefinition cfDef : keyspaceDef.getCfDefs()) {
         if (cfDef.getName().equals(cfName)) {
            return true;
         }
      }
      return false;      
   }
   
   private ColumnFamilyTemplate<String, String> getParametersTemplate() {
      return
         new ThriftColumnFamilyTemplate<String, String>(keyspace,
                                                        "Parameters", 
                                                        StringSerializer.get(),        
                                                        StringSerializer.get());
   }
   
   
   /**
    * @param cluster : le cluster cassandra
    * @return le facteur de réplication du keyspace Docubase
    */
   private int getDocubaseReplicationFactor(Cluster cluster) {
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace("Docubase");
      if (keyspaceDef != null) {
         return keyspaceDef.getReplicationFactor();
      }
      // On est sûrement en test. On renvoie 1.
      return 1;
   }

   /**
    * Ajoute les options les plus fréquemment utilisées.
    * @param cfDef : définition de la CF
    */
   private void addDefaultCFAttributs(ColumnFamilyDefinition cfDef) {
      // GCgrace fixé à 20 jours.
      cfDef.setGcGraceSeconds(DEFAULT_GCGRACE);

      // Snappy compression
      Map<String, String> compressOptions = new HashMap<String, String>();
      compressOptions.put("sstable_compression", "SnappyCompressor");
      cfDef.setCompressionOptions(compressOptions);
      
      // Leveled compaction.
      cfDef.setCompactionStrategy("LeveledCompactionStrategy");
   }

}
