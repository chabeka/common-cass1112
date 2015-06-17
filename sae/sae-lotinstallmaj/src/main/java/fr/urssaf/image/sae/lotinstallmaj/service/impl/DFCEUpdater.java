package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
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
 * Classe permettant la mise à jour des donnees du keyspace Docubase dans cassandra
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
      CassandraHostConfigurator chc = new CassandraHostConfigurator(config
            .getHosts());
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
    */
   public final void disableCompositeIndex(List<String[]> indexes) {

      // On se connecte au keyspace
      connectToKeyspace();

      // creation du mutator pour lancer l'ensemble des update
      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      for (String[] index : indexes) {

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
               LOG
                     .info(
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
            LOG
                  .info("Suppression de l'index composite : {}", nomCle
                        .toString());
            mutator.addDeletion(nomCle.toString(), cfName);
         } else {
            LOG.info("L'index composite {} n'existe pas", nomCle.toString());
         }
      }

      // execute l'ensemble des mises a jour
      mutator.execute();
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
