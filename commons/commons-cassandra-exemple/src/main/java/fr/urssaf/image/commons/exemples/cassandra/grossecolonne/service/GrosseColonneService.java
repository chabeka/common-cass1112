package fr.urssaf.image.commons.exemples.cassandra.grossecolonne.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.commons.exemples.cassandra.config.CassandraEtZookeeperConfig;
import fr.urssaf.image.commons.exemples.cassandra.grossecolonne.dao.JobExecutionDao;

public class GrosseColonneService {

   private static final Logger LOGGER = LoggerFactory.getLogger(GrosseColonneService.class);
   
   public static final String KEYSPACE_NAME = "TESTS_VOLUME";
   
   
   // GCgrace par fixé à 20 jours.
   // Il est à 10 jours par défaut. Ça nous laisse plus de temps pour réagir en
   // cas de problème avec les repair.
   private static final int DEFAULT_GCGRACE = 1728000;
   
   
   private JobExecutionDao dao = new JobExecutionDao();
   
   
   /**
    * Création du Keyspace "TESTS_VOLUME", et sa column family "JobExecution"
    * 
    * @param config Configuration Cassandra
    */
   public void createKeyspaceEtColumnFamily(
          CassandraEtZookeeperConfig config) {
      
      // Préparation des credentials
      Map<String,String> credentials = new HashMap<String, String>();
      credentials.put("username", config.getCassandraUserName());
      credentials.put("password", config.getCassandraPassword());
      
      // Création du cluster pour pouvoir manipuler Cassandra
      CassandraHostConfigurator chc = new CassandraHostConfigurator(
            config.getCassandraHosts());
      Cluster cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
      
      // Création d'un nouveau Keyspace "TESTS_VOLUME"
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(KEYSPACE_NAME);
      if (keyspaceDef!=null) {
         throw new RuntimeException("Le Keyspace " + KEYSPACE_NAME + " existe déjà");
      }
      int replicationFactor = getDocubaseReplicationFactor(cluster);
      KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(
            KEYSPACE_NAME, 
            ThriftKsDef.DEF_STRATEGY_CLASS, 
            replicationFactor,
            new ArrayList<ColumnFamilyDefinition>());
      cluster.addKeyspace(newKeyspace, true);
      
      // Connexion au Keyspace
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HFactory.createKeyspace(
            KEYSPACE_NAME, 
            cluster, 
            ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, 
            credentials); 
      
      // Liste des column family à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();
      
      // Ajout de la colonne family JobExecution
      cfDefs.add(HFactory.createColumnFamilyDefinition(
            KEYSPACE_NAME, 
            "JobExecution",
            ComparatorType.BYTESTYPE));
      
      // Ajoute les options les plus courantes à chacune des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         addDefaultCFAttributs(c);
      }
      
      // Création des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         if (cfExists(keyspaceDef, c.getName())) {
            LOGGER.info("La famille de colonnes " + c.getName()
                  + " est déjà existante");
         } else {
            LOGGER.info("Création de la famille de colonnes " + c.getName());
            cluster.addColumnFamily(c, true);
         }
      }
      
   }
   
   
   
   private int getDocubaseReplicationFactor(Cluster cluster) {
      KeyspaceDefinition keyspaceDef = cluster.describeKeyspace("Docubase");
      if (keyspaceDef != null) {
         return keyspaceDef.getReplicationFactor();
      }
      // On est sûrement en test. On renvoie 1.
      return 1;
   }
   
   
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
    * Ecriture d'une ligne dans la CF JobExecution, contenant un id (long) et
    * un objet ExecutionContext sérialisé, contenant lui-même une liste d'UUID
    * 
    * @param keyspace le Keyspace
    * @param idJob l'id du job (clé de la ligne dans JobExecution)
    * @param nbUUID le nombre d'UUID à mettre dans l'objet ExecutionContext
    */
   public void ecrireExecutionContext(
         Keyspace keyspace,
         long idJob,
         long nbUUID) {
      
      ColumnFamilyTemplate<Long, String> template = dao.createCFTemplate(keyspace);
      
      ColumnFamilyUpdater<Long, String> updater = 
         template.createUpdater(idJob);
      
      ExecutionContext executionContext = buildExecutionContext(idJob,nbUUID); 

      dao.ecritExecutionContext(updater, executionContext);
      
      template.update(updater);
            
   }
   
   
   private ExecutionContext buildExecutionContext(long idJob, long nbUUID) {
      
      // Construction de l'objet ExecutionContext
      ExecutionContext executionContext = new ExecutionContext();
      
      // L'id du job dans ID_JOB (pour mieux le visualiser dans les GUI pour Cassandra)
      executionContext.put("ID_JOB", idJob);
      
      // La liste des UUID dans INTEGRATED_DOCUMENTS
      ConcurrentLinkedQueue<UUID> list = buildListeUUID(nbUUID);
      executionContext.put("INTEGRATED_DOCUMENTS", list);
      
      // Renvoie de l'objet ExecutionContext
      return executionContext;
      
   }
   
   
   private ConcurrentLinkedQueue<UUID> buildListeUUID(long nbUUID) {
      
      ConcurrentLinkedQueue<UUID> listUuid = new ConcurrentLinkedQueue<UUID>();
      
      for (int i=0;i<nbUUID;i++) {
         listUuid.add(UUID.randomUUID());
      }
      
      return listUuid;
      
   }
   
   
   @SuppressWarnings("unchecked")
   public ConcurrentLinkedQueue<UUID> lireExecutionContexte(
         Keyspace keyspace,
         long idJob) {
      
      ColumnFamilyTemplate<Long, String> template = dao.createCFTemplate(keyspace);
      
      ColumnFamilyResult<Long, String> result = template.queryColumns(idJob);
      
      ExecutionContext execCtx = dao.createExecutionContextFromResult(result);
      
      if (execCtx==null) {
         return null;
      }
      
      ConcurrentLinkedQueue<UUID> list = (ConcurrentLinkedQueue<UUID>)execCtx.get("INTEGRATED_DOCUMENTS");
      
      return list;
      
   }
   
}
