package fr.urssaf.image.sae.test.divers.dfce;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
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

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
public class UpdateTypeMetadonneeTest {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(UpdateTypeMetadonneeTest.class);

   /**
    * Injecte le keyspace SAE.
    */
   @Autowired
   private Keyspace keyspace;

   /**
    * Recuperation
    */
   @Autowired
   private CassandraServerBean cassandraServer;

   /**
    * Mise a jour du type de la metadonnee ControleComptable.
    */
   @Test
   //@Ignore
   public void updateTypeMetadonnees() {
      String codeCourtControleComptable = "cco";
      String codeLongControleComptable = "ControleComptable";
      boolean dryRun = true;
      
      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // modification du type de la categorie cco
      LOGGER.debug("Recuperation de la categorie docubase : {}", codeCourtControleComptable);
      SliceQuery<String,String,String> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
      queryDocubase.setColumnFamily("CategoriesReference").setKey(codeCourtControleComptable).setRange(null, null, false, 200);
      QueryResult<ColumnSlice<String,String>> resultDocubase = queryDocubase.execute();
      if (resultDocubase != null && resultDocubase.get() != null) {
         LOGGER.debug("Categorie trouvee : {}", codeCourtControleComptable);
         
         for(HColumn<String, String> test : resultDocubase.get().getColumns()) {
            LOGGER.debug("{} : {}", test.getName(), test.getValue());
         }
         
         // recupere le type
         HColumn<String, String> type = resultDocubase.get().getColumnByName("categoryTypeENUM_ATTRIBUTE_VALUE_SUFFIXE");
         LOGGER.debug("Type de la metadonnee : {}", type.getValue());
         
         if (!dryRun) {
            LOGGER.debug("Modification de la categorie : {}", codeCourtControleComptable);
            Mutator<String> mutator = HFactory.createMutator(keyspaceDocubase,
                  StringSerializer.get());
            mutator.addInsertion(codeCourtControleComptable, "CategoriesReference", 
                    HFactory.createStringColumn(type.getName(),
                                          "BOOLEAN"));
            mutator.execute();
         } else {
            LOGGER.debug("Mode dry run : On devrait modifier le type de la categorie : {}", codeCourtControleComptable);
            for (HColumn<String, String> column : resultDocubase.get().getColumns()) {
               LOGGER.debug("{}: {}", new String[] {column.getName(), column.getValue() });
            }
         }
      }
      
      // modification du type de la metadata ControleComptable
      LOGGER.debug("Recuperation de la metadata SAE : {}", codeLongControleComptable);
      SliceQuery<String,String,String> querySAE = HFactory.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
      querySAE.setColumnFamily("Metadata").setKey(codeLongControleComptable).setRange(null, null, false, 100);
      QueryResult<ColumnSlice<String,String>> resultSAE = querySAE.execute();
      if (resultSAE != null && resultSAE.get() != null) {
         LOGGER.debug("Metadata trouvee : {}", codeLongControleComptable);
         
         // recupere le type
         HColumn<String, String> type = resultSAE.get().getColumnByName("type");
         LOGGER.debug("Type de la metadonnee : {}", type.getValue());
         
         if (!dryRun) {
            LOGGER.debug("Modification de la metadata : {}", codeLongControleComptable);
            Mutator<String> mutator = HFactory.createMutator(keyspace,
                  StringSerializer.get());
            mutator.addInsertion(codeLongControleComptable, "Metadata", 
                  HFactory.createStringColumn(type.getName(),
                     "Boolean"));
            mutator.execute();
         } else {
            LOGGER.debug("Mode dry run : On devrait modifier le type de la metadata : {}", codeLongControleComptable);
            for (HColumn<String, String> column : resultSAE.get().getColumns()) {
               LOGGER.debug("{}: {}", new String[] {column.getName(), column.getValue() });
            }
         }
      }
   }

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            cassandraServer.getHosts());
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-"
            + new Date().getTime(), hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl, failoverPolicy,
            credentials);
   }
}
