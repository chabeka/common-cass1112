package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
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
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

/**
 * Classe permettant la mise à jour des donnees du keyspace Docubase dans
 * cassandra
 * 
 */
public class DFCEUpdater {

   /**
    * Nom du keyspace
    */
   private static final String DFCE_KEYSPACE_NAME = "Docubase";

   private final Cluster cluster;
   private Keyspace keyspace;
   private final Map<String, String> credentials;

   private static final Logger LOG = LoggerFactory.getLogger(DFCEUpdater.class);

   /**
    * Constructeur
    * 
    * @param config
    *           : configuration d'accès au cluster cassandra
    */
   public DFCEUpdater(CassandraConfig config) {
      credentials = new HashMap<String, String>();
      credentials.put("username", config.getLogin());
      credentials.put("password", config.getPassword());
      CassandraHostConfigurator chc = new CassandraHostConfigurator(
            config.getHosts());
      cluster = HFactory.getOrCreateCluster("SAECluster", chc, credentials);
   }

   private void connectToKeyspace() {
      if (keyspace != null)
         return;
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      keyspace = HFactory.createKeyspace(DFCE_KEYSPACE_NAME, cluster, ccl,
            FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
   }

   /**
    * Mise à jour vers la version 129-P5
    * 
    * @param indexes
    *           Liste des index composites à supprimer
    */
   public final void disableCompositeIndex(Map<String[], String> indexes) {

      // On se connecte au keyspace
      connectToKeyspace();

      // creation du mutator pour lancer l'ensemble des update
      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      for (Entry<String[], String> entry : indexes.entrySet()) {
         String[] index = entry.getKey();

         // calcul le nom de la cle
         StringBuffer nomCle = new StringBuffer();
         for (String meta : index) {
            nomCle.append(meta);
            nomCle.append('&');
         }

         // Traitement de la CF TermInfoRangeString

         String cfName = "TermInfoRangeString";

         RangeSlicesQuery<String, String, String> query = HFactory
               .createRangeSlicesQuery(keyspace, StringSerializer.get(),
                     StringSerializer.get(), StringSerializer.get());
         query.setColumnFamily(cfName).setKeys(null, null).setReturnKeysOnly();
         query.setRowCount(5000);
         QueryResult<OrderedRows<String, String, String>> result = query
               .execute();
         for (Row<String, String, String> row : result.get().getList()) {
            if (row.getKey().startsWith(getStartKey(nomCle.toString()))) {
               LOG.info(
                     "Suppression de l'indexation de l'index composite : {} -> {}",
                     nomCle.toString(), row.getKey());
               mutator.addDeletion(nomCle.toString(), cfName);
            }
         }

         // Traitement de la CF CompositeIndexesReference

         cfName = "CompositeIndexesReference";

         SliceQuery<String, String, String> queryCompositeIndex = HFactory
               .createSliceQuery(keyspace, StringSerializer.get(),
                     StringSerializer.get(), StringSerializer.get());
         queryCompositeIndex.setColumnFamily(cfName).setKey(nomCle.toString())
               .setRange(null, null, false, 1);
         QueryResult<ColumnSlice<String, String>> resultCompositeIndex = queryCompositeIndex
               .execute();
         if (resultCompositeIndex != null && resultCompositeIndex.get() != null
               && !resultCompositeIndex.get().getColumns().isEmpty()) {
            LOG.info("Suppression de l'index composite : {}", nomCle.toString());
            mutator.addDeletion(nomCle.toString(), cfName);
         } else {
            LOG.info("L'index composite {} n'existe pas", nomCle.toString());
         }
      }

      // execute l'ensemble des mises a jour
      mutator.execute();
   }

   /**
    * Indexe à vide les index composite si nécessaire (met les colonnes indexed
    * et computed à true dans la colonne de famille CompositeIndexesReference
    * 
    * @param indexes
    *           Liste des index composites à créer
    */
   public final void indexeAVideCompositeIndex(String indexName) {

      // On se connecte au keyspace
      connectToKeyspace();

      // On indexe l'index composite si ce n'est pas déjà le cas dans DFCE
      if (!isCompositeIndexComputed(indexName)) {

         updateColumn("CompositeIndexesReference", indexName, "computed", true);

      }
   }

   
   /**
    * Indexe à vide un index simple si nécessaire (met les colonnes indexed
    * et computed à true dans la colonne de famille BaseCategoriesReference
    * 
    * @param indexes
    *           Liste des index composites à créer
    */
   public final void indexeAVideIndexSimple(String indexName, String baseName) {

      // On se connecte au keyspace
      connectToKeyspace();
      
      // creation de la rowKey
      StringBuffer buffer = new StringBuffer();
      buffer.append(baseName);
      buffer.append((char) 65535);
      buffer.append(indexName);
      String rowKey = buffer.toString();

      // On indexe l'index composite si ce n'est pas déjà le cas dans DFCE
      if (!isMetaIndexedAndComputed(rowKey)) {
         updateColumn("BaseCategoriesReference", rowKey, "indexed", true);
         updateColumn("BaseCategoriesReference", rowKey, "computed", true);
      }
   }
   
   /**
    * Vérifie si l'index est déjà indexée dans DFCE (computed à true)
    * 
    * @param indexName
    *           le nom de l'index
    * @return vrai si l'index est indexée
    */
   private boolean isCompositeIndexComputed(String indexName) {

      boolean isIndexee = false;

      SliceQuery<String, String, byte[]> queryDocubase = HFactory
            .createSliceQuery(keyspace, StringSerializer.get(),
                  StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("CompositeIndexesReference");
      queryDocubase.setKey(indexName);
      queryDocubase.setColumnNames("computed");

      QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase
            .execute();
      if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
         HColumn<String, byte[]> isComputed = resultat.get().getColumnByName(
               "computed");
         if (isComputed != null) {
            isIndexee = BooleanSerializer.get()
                  .fromBytes(isComputed.getValue());
         }
      }
      return isIndexee;
   }

   /**
    * Methode permettant de verifier qu'une métadonnée est indexée
    * et que l'index est 'actif'.
    * @param rowKey nom de la métadonnées
    * @return boolean indiquant s'il y a quelquechose a faire
    */
   public boolean isMetaIndexedAndComputed(String rowKey) {

      SliceQuery<byte[], String, byte[]> queryDocubase = HFactory
            .createSliceQuery(keyspace, BytesArraySerializer.get(),
                  StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BaseCategoriesReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(rowKey));
      queryDocubase.setColumnNames("indexed", "computed");

      boolean valeurRetour = false;
      
      QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase
            .execute();
      if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
         HColumn<String, byte[]> isIndexed = resultat.get().getColumnByName(
               "indexed");
         HColumn<String, byte[]> isComputed = resultat.get().getColumnByName(
               "computed");

         
         if (isIndexed != null && isComputed != null) {
            boolean valeurIndexed = BooleanSerializer.get().fromBytes(
                  isIndexed.getValue());
            boolean valeurComputed = BooleanSerializer.get().fromBytes(
                  isComputed.getValue());

            if (valeurIndexed && valeurComputed) {
               valeurRetour = true;
            }
         }
      }
      return valeurRetour;
   }

   private void updateColumn(String CFName, String rowName, String columnName,
         Object value) {

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      Collection<String> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (columnNames.contains(columnName)) {

         if (value != null && value instanceof String) {
            HColumn<String, String> column = HFactory.createColumn(columnName,
                  (String) value, StringSerializer.get(),
                  StringSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Long) {
            HColumn<String, Long> column = HFactory.createColumn(columnName,
                  (Long) value, StringSerializer.get(), LongSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Date) {
            HColumn<String, Date> column = HFactory.createColumn(columnName,
                  (Date) value, StringSerializer.get(), DateSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Boolean) {
            HColumn<String, Boolean> column = HFactory.createColumn(columnName,
                  (Boolean) value, StringSerializer.get(),
                  BooleanSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null) {
            LOG.info("Type de valeur non prise en charge : "
                  + value.getClass().getName());
         }

      } else {
         LOG.info("Column " + columnName + " inexistante pour la key "
               + rowName + " dans la CF " + CFName);
      }
   }

   private String getStartKey(String nomCle) {
      StringBuffer startKey = new StringBuffer();
      startKey.append((char) 0);
      startKey.append((char) 0);
      startKey.append((char) 0);
      startKey.append((char) 0);
      startKey.append((char) nomCle.length());
      startKey.append(nomCle);
      startKey.append((char) 0);
      startKey.append((char) 0);
      startKey.append((char) 16);
      return startKey.toString();
   }
}
