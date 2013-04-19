package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

/**
 * Classe permettant la mise à jour du schéma du keyspace SAE dans cassandra
 * 
 */
public class DFCECassandraUpdater {

   private static final String UTF8_TYPE = "UTF8Type";

   /**
    * Nom du keyspace
    */
   private final String ksName;

   private final Cluster cluster;
   private Keyspace keyspace;
   private final Map<String, String> credentials;

   // LOGGER
   private static final Logger LOG = LoggerFactory
         .getLogger(DFCECassandraUpdater.class);

   // GCgrace par fixé à 20 jours.
   // Il est à 10 jours par défaut. Ça nous laisse plus de temps pour réagir en
   // cas de problème avec les repair.
   private static final int DEFAULT_GCGRACE = 1728000;

   /**
    * Constructeur
    * 
    * @param config
    *           : configuration d'accès au cluster cassandra
    */
   public DFCECassandraUpdater(CassandraConfig config) {
      ksName = config.getKeyspaceName();
      credentials = new HashMap<String, String>();
      credentials.put("username", config.getLogin());
      credentials.put("password", config.getPassword());
      CassandraHostConfigurator chc = new CassandraHostConfigurator(config
            .getHosts());
      cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
   }

   /**
    * Mise à jour vers la version 110
    */
   public final void updateToVersion110() {

      LOG.info("Mise à jour du keyspace DFCE en version 1.1.0");

      // Si le KeySpace n'existe pas, on quitte
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(ksName);
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      connectToKeyspace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      // DocEventLogByTimeSerialized

      String comparatorAlias = "("
            + ComparatorType.getByClassName("DateType").getTypeName() + ", "
            + ComparatorType.UUIDTYPE.getTypeName() + ")";

      ColumnFamilyDefinition column0 = HFactory
            .createColumnFamilyDefinition(ksName,
                  "DocEventLogByTimeSerialized", ComparatorType.COMPOSITETYPE);
      column0.setComparatorTypeAlias(comparatorAlias);
      column0.setRowCacheSize(0);
      column0.setKeyCacheSize(0);
      cfDefs.add(column0);

      // SystemEventLogByTimeSerialized

      ColumnFamilyDefinition column1 = HFactory.createColumnFamilyDefinition(
            ksName, "SystemEventLogByTimeSerialized",
            ComparatorType.COMPOSITETYPE);
      column1.setComparatorTypeAlias(comparatorAlias);
      column1.setRowCacheSize(0);
      column1.setKeyCacheSize(0);
      cfDefs.add(column1);

      // JobInstance

      List<ColumnDefinition> columnMetadata2 = new ArrayList<ColumnDefinition>();

      BasicColumnDefinition e20 = new BasicColumnDefinition();
      e20.setName(StringSerializer.get().toByteBuffer("jobInstanceId"));
      e20.setValidationClass("LongType");
      e20.setIndexType(ColumnIndexType.KEYS);
      e20.setIndexName("JobInstance_jobInstanceId_idx");
      columnMetadata2.add(e20);

      BasicColumnDefinition e21 = new BasicColumnDefinition();
      e21.setName(StringSerializer.get().toByteBuffer("jobInstanceName"));
      e21.setValidationClass(UTF8_TYPE);
      e21.setIndexType(ColumnIndexType.KEYS);
      e21.setIndexName("JobInstance_jobInstanceName_idx");
      columnMetadata2.add(e21);

      ColumnFamilyDefinition column2 = HFactory.createColumnFamilyDefinition(
            ksName, "JobInstance", ComparatorType.UTF8TYPE, columnMetadata2);
      cfDefs.add(column2);

      // JobExecution

      List<ColumnDefinition> columnMetadata3 = new ArrayList<ColumnDefinition>();

      BasicColumnDefinition e30 = new BasicColumnDefinition();
      e30.setName(StringSerializer.get().toByteBuffer("jobInstanceId"));
      e30.setValidationClass("LongType");
      e30.setIndexType(ColumnIndexType.KEYS);
      e30.setIndexName("JobExecution_jobInstanceId_idx");
      columnMetadata3.add(e30);

      BasicColumnDefinition e31 = new BasicColumnDefinition();
      e31.setName(StringSerializer.get().toByteBuffer("jobInstanceName"));
      e31.setValidationClass(UTF8_TYPE);
      e31.setIndexType(ColumnIndexType.KEYS);
      e31.setIndexName("JobExecution_jobInstanceName_idx");
      columnMetadata3.add(e31);

      ColumnFamilyDefinition column3 = HFactory.createColumnFamilyDefinition(
            ksName, "JobExecution", ComparatorType.UTF8TYPE, columnMetadata3);
      cfDefs.add(column3);

      // StepExecution

      List<ColumnDefinition> columnMetadata4 = new ArrayList<ColumnDefinition>();

      BasicColumnDefinition e40 = new BasicColumnDefinition();
      e40.setName(StringSerializer.get().toByteBuffer("jobExecutionId"));
      e40.setValidationClass("LongType");
      e40.setIndexType(ColumnIndexType.KEYS);
      e40.setIndexName("StepExecution_jobExecutionId_idx");
      columnMetadata4.add(e40);

      BasicColumnDefinition e41 = new BasicColumnDefinition();
      e41.setName(StringSerializer.get().toByteBuffer("stepName"));
      e41.setValidationClass(UTF8_TYPE);
      e41.setIndexType(ColumnIndexType.KEYS);
      e41.setIndexName("StepExecution_stepName_idx");
      columnMetadata4.add(e41);

      BasicColumnDefinition e42 = new BasicColumnDefinition();
      e42.setName(StringSerializer.get().toByteBuffer("jobInstanceName"));
      e42.setValidationClass(UTF8_TYPE);
      e42.setIndexType(ColumnIndexType.KEYS);
      e42.setIndexName("StepExecution_jobInstanceName_idx");
      columnMetadata4.add(e42);

      ColumnFamilyDefinition column4 = HFactory.createColumnFamilyDefinition(
            ksName, "StepExecution", ComparatorType.UTF8TYPE, columnMetadata4);
      cfDefs.add(column4);

      // BatchCounter

      ColumnFamilyDefinition column5 = HFactory.createColumnFamilyDefinition(
            ksName, "BatchCounter", ComparatorType.UTF8TYPE);
      column5.setDefaultValidationClass("CounterColumnType");
      cfDefs.add(column5);

      // Locker

      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Locker",
            ComparatorType.UTF8TYPE));

      // Ajoute les options les plus courantes à chacune des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         addDefaultCFAttributs(c);
      }

      // Création des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         if (cfExists(keyspaceDef, c.getName())) {
            LOG.info("La famille de colonnes " + c.getName()
                  + " est déjà existante");
         } else {
            LOG.info("Création de la famille de colonnes " + c.getName());
            cluster.addColumnFamily(c, true);
         }
      }

   }

   /**
    * Mise à jour vers la version 120
    */
   public final void updateToVersion120() {

      LOG.info("Mise à jour du keyspace DFCE en version 1.2.0");

      // Si le KeySpace n'existe pas, on quitte
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(ksName);
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      connectToKeyspace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      List<ColumnDefinition> columnMetadata0 = new ArrayList<ColumnDefinition>();

      BasicColumnDefinition e01 = new BasicColumnDefinition();
      e01.setName(StringSerializer.get().toByteBuffer("6e616d65"));
      e01.setValidationClass("BytesType");
      e01.setIndexType(ColumnIndexType.KEYS);
      e01.setIndexName("stat_name_idx");
      columnMetadata0.add(e01);

      ColumnFamilyDefinition column0 = HFactory.createColumnFamilyDefinition(
            ksName, "Statistics", ComparatorType.BYTESTYPE, columnMetadata0);
      cfDefs.add(column0);

      String comparatorAlias = "(a=>" + ComparatorType.ASCIITYPE.getTypeName()
            + ", b=>" + ComparatorType.BYTESTYPE.getTypeName() + ", i=>"
            + ComparatorType.INTEGERTYPE.getTypeName() + ", x=>"
            + ComparatorType.LEXICALUUIDTYPE.getTypeName() + ", l=>"
            + ComparatorType.LONGTYPE.getTypeName() + ", t=>"
            + ComparatorType.TIMEUUIDTYPE.getTypeName() + ", s=>"
            + ComparatorType.UTF8TYPE.getTypeName() + ", u=>"
            + ComparatorType.UUIDTYPE.getTypeName() + ")";
      ColumnFamilyDefinition column1 = HFactory.createColumnFamilyDefinition(
            ksName, "StatisticsDatas", ComparatorType.DYNAMICCOMPOSITETYPE);
      column1.setComparatorTypeAlias(comparatorAlias);
      column1.setRowCacheSize(0);
      column1.setKeyCacheSize(0);
      cfDefs.add(column1);

      // Ajoute les options les plus courantes à chacune des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         addDefaultCFAttributs(c);
      }

      // Création des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         if (cfExists(keyspaceDef, c.getName())) {
            LOG.info("La famille de colonnes " + c.getName()
                  + " est déjà existante");
         } else {
            LOG.info("Création de la famille de colonnes " + c.getName());
            cluster.addColumnFamily(c, true);
         }
      }

   }

   private void connectToKeyspace() {
      if (keyspace != null)
         return;
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      keyspace = HFactory.createKeyspace(ksName, cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
   }

   /**
    * @param keyspaceDef
    *           Keyspace definition
    * @param cfName
    *           Name of the CF to search for
    * @return true if the CF exists in keyspace
    */
   private boolean cfExists(KeyspaceDefinition keyspaceDef, String cfName) {
      if (keyspaceDef == null)
         return false;
      for (ColumnFamilyDefinition cfDef : keyspaceDef.getCfDefs()) {
         if (cfDef.getName().equals(cfName)) {
            return true;
         }
      }
      return false;
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
