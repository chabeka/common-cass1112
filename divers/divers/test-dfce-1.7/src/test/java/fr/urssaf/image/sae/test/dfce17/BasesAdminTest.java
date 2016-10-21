package fr.urssaf.image.sae.test.dfce17;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;

@RunWith(BlockJUnit4ClassRunner.class)
public class BasesAdminTest {

   private static final Logger LOGGER = LoggerFactory
   .getLogger(BasesAdminTest.class);
   
   // Developpement 
   //private String urlDfce = "http://cer69-ds4int.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cer69imageint9.cer69.recouv";
   //private String hosts = "cer69imageint10.cer69.recouv";
   
   // Recette interne GNT
   //private String urlDfce = "http://hwi69devgntappli1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69devgntcas1.gidn.recouv:9160,cnp69devgntcas2.gidn.recouv:9160";
   
   // Recette interne GNS
   //private String urlDfce = "http://hwi69devsaeapp1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
   
   // Integration cliente GNT
   //private String urlDfce = "http://hwi69intgntappli1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   
   // Integration cliente GNS
   //private String urlDfce = "http://hwi69intgnsapp1.gidn.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   
   // Integration nationale GNT
   //private String urlDfce = "http://hwi69gingntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160";
   
   // Integration nationale GNS
   //private String urlDfce = "http://hwi69ginsaeappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
   
   // Validation nationale GNT
   //private String urlDfce = "http://hwi69givngntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69givngntcas1.cer69.recouv:9160,cnp69givngntcas2.cer69.recouv:9160,cnp69givngntcas3.cer69.recouv:9160";
   
   // Validation nationale GNS
   //private String urlDfce = "http://hwi69givnsaeappli.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
   
   // Pre-prod nationale GNT
   //private String urlDfce = "http://hwi69pregntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69pregntcas1.cer69.recouv:9160,cnp69pregntcas2.cer69.recouv:9160,cnp69pregntcas3.cer69.recouv:9160";
   
   // Pre-prod nationale GNS
   //private String urlDfce = "http://hwi69pregnsapp.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas2.cer69.recouv,cnp69pregnscas3.cer69.recouv,cnp69pregnscas4.cer69.recouv,cnp69pregnscas5.cer69.recouv,cnp69pregnscas6.cer69.recouv";
   
   // Prod nationale GNT
   private String urlDfce = "http://hwi69gntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   //private String urlDfce = "http://hwi69saeappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   //private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
   
   @Test
   @Ignore
   public void startBaseLog() throws NoSuchDfceJobExecutionException {
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", urlDfce, 3 * 60 * 1000);
      
      Base base = provider.getBaseAdministrationService().getBase("DAILY_LOG_ARCHIVE_BASE");
      
      if (!base.isStarted()) {
         LOGGER.debug("La base n'est pas démarrée. On va la démarrer");
         // on la démarre
         provider.getBaseAdministrationService().startBase(base);
      } else {
         LOGGER.debug("La base est démarré. Rien à faire");
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void isBasesStarted() throws NoSuchDfceJobExecutionException {
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", urlDfce, 3 * 60 * 1000);
      
      List<Base> bases = provider.getBaseAdministrationService().getAllBases();
      
      for (Base base : bases) {
         if (!base.isStarted()) {
            LOGGER.debug("La base {} n'est pas démarrée", base.getBaseId());
         } else {
            LOGGER.debug("La base {} est démarrée", base.getBaseId());
         }
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void getNbCategoriesInBases() {
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      provider.connect("_ADMIN", "DOCUBASE", urlDfce, 3 * 60 * 1000);
      
      List<Base> bases = provider.getBaseAdministrationService().getAllBases();
      
      for (Base base : bases) {
         LOGGER.debug("La base {} comporte {} categories", base.getBaseId(), base.getBaseCategories().size() );
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   private Keyspace getKeyspaceDocubaseFromKeyspace(String hosts) {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            hosts);
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private List<String> getBases(Keyspace keyspaceDocubase) {
      List<String> bases = new ArrayList<String>();
      LOGGER.debug("Recuperation de la liste des bases");
      RangeSlicesQuery<String,String,byte[]> rangeSlicesQuery= HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeSlicesQuery.setColumnFamily("BasesReference");
      rangeSlicesQuery.setReturnKeysOnly();
      rangeSlicesQuery.setRowCount(500);
      QueryResult<OrderedRows<String, String, byte[]>> rangeSliceResult = rangeSlicesQuery.execute();
      if (rangeSliceResult != null && rangeSliceResult.get() != null) {
         OrderedRows<String, String, byte[]> rows = rangeSliceResult.get();
         for(Row<String, String, byte[]> row : rows.getList()) {
            bases.add(row.getKey());
         }
      }
      return bases;
   }
   
   @Test
   public void getNbDocsInBases() throws JSONException {
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hosts);
      
      List<String> bases = getBases(keyspaceDocubase);
      
      for (String nomBase : bases) {
         LOGGER.debug("Recuperation du nombre de docs de la base {}", nomBase);
         
         Composite keyToFind = new Composite();
         keyToFind.addComponent("DAILY", StringSerializer.get());
         keyToFind.addComponent("BASE", StringSerializer.get());
         keyToFind.addComponent(nomBase, StringSerializer.get());
         
         // calcul la date d'hier
         Calendar yesterday = new GregorianCalendar();
         yesterday.add(Calendar.DAY_OF_MONTH, -1);
         yesterday.set(Calendar.HOUR_OF_DAY, 0);
         yesterday.set(Calendar.MINUTE, 0);
         yesterday.set(Calendar.SECOND, 0);
         yesterday.set(Calendar.MILLISECOND, 0);
         SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
         String columnToFind = formatter.format(yesterday.getTime());
         
         SliceQuery<Composite, String, String> query =  HFactory.createSliceQuery(keyspaceDocubase, CompositeSerializer.get(), StringSerializer.get(), StringSerializer.get());
         query.setColumnFamily("DocStatistics");
         query.setKey(keyToFind);
         // permet de limiter la liste a la derniere colonne
         query.setRange(columnToFind, null, false, 100);
         QueryResult<ColumnSlice<String, String>> result = query.execute();
         if (result != null && result.get() != null && result.get().getColumns().size() > 0) {
            HColumn<String, String> colonne = result.get().getColumns().get(result.get().getColumns().size() - 1);
            
            // parse la chaine json
            JSONObject jsonValue = new JSONObject(colonne.getValue());
            LOGGER.debug("    Nb total de docs : {}", jsonValue.get("C"));
            
         } else {
            LOGGER.debug("    La derniere colonne n'a pas ete trouvee ({})", columnToFind);
         }
      }
   }
   
   @Test
   public void getNbDocsByType() throws JSONException {
      
      String codeRNDToFind = "7.7.8.8.1"; // Journal du SAE
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hosts);
      
      LOGGER.debug("Recuperation du nombre de docs pour le code rnd {}", codeRNDToFind);
      
      Composite keyToFind = new Composite();
      keyToFind.addComponent("DAILY", StringSerializer.get());
      keyToFind.addComponent("TYPE", StringSerializer.get());
      keyToFind.addComponent(codeRNDToFind, StringSerializer.get());
      
      // calcul la date d'hier
      Calendar yesterday = new GregorianCalendar();
      yesterday.add(Calendar.DAY_OF_MONTH, -1);
      yesterday.set(Calendar.HOUR_OF_DAY, 0);
      yesterday.set(Calendar.MINUTE, 0);
      yesterday.set(Calendar.SECOND, 0);
      yesterday.set(Calendar.MILLISECOND, 0);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      String columnToFind = formatter.format(yesterday.getTime());
      
      SliceQuery<Composite, String, String> query =  HFactory.createSliceQuery(keyspaceDocubase, CompositeSerializer.get(), StringSerializer.get(), StringSerializer.get());
      query.setColumnFamily("DocStatistics");
      query.setKey(keyToFind);
      // permet de limiter la liste a la derniere colonne
      query.setRange(columnToFind, null, false, 100);
      QueryResult<ColumnSlice<String, String>> result = query.execute();
      if (result != null && result.get() != null && result.get().getColumns().size() > 0) {
         HColumn<String, String> colonne = result.get().getColumns().get(result.get().getColumns().size() - 1);
         
         // parse la chaine json
         JSONObject jsonValue = new JSONObject(colonne.getValue());
         LOGGER.debug("    Nb total de docs : {}", jsonValue.get("C"));
         
      } else {
         LOGGER.debug("    La derniere colonne n'a pas ete trouvee ({})", columnToFind);
      }
   }
}
