package fr.urssaf.image.sae.test.divers.dfce;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.JobAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.batch.DfceJobParametersInvalidException;
import com.docubase.dfce.exception.batch.launch.DfceJobInstanceAlreadyExistsException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
public class IndexCategoryTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(IndexCategoryTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   /**
    * Recuperation
    */
   @Autowired
   private CassandraServerBean cassandraServer;
   
   @Test
   @Ignore
   public void reindexCategory() throws NoSuchDfceJobException, DfceJobInstanceAlreadyExistsException, DfceJobParametersInvalidException, InterruptedException, NoSuchDfceJobExecutionException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
      
      // recuperation de la base
      LOGGER.debug("Recuperation de la base");
      Base base = serviceProvider.getBaseAdministrationService().getBase("GNT-INT");
      
      // recupere la categorie
      LOGGER.debug("Recuperation de la categorie");
      String nomCategory = "sac";
      Category category = storageAdminService.getCategory(nomCategory);
      if (category != null) {
         LOGGER.debug("Category {} récupérée", category.getName());
         
         // recupere l'association base/categorie
         LOGGER.debug("Recuperation de l'association de la categorie avec la base");
         BaseCategory baseCategory = base.getBaseCategory(category.getName());
         if (baseCategory != null) {
            LOGGER.debug("BaseCategory {} récupérée", category.getName());
            // modif la base si necessaire
            if (!baseCategory.isIndexed()) {
               LOGGER.debug("La categorie n'est pas indexee");
               LOGGER.debug("Mise a jour de la base pour la rendre indexee");
               baseCategory.setIndexed(true);
               serviceProvider.getBaseAdministrationService().updateBase(base);
            }
            
            // lance le job d'indexation
            LOGGER.debug("Lancement du job d'indexation");
            Long idJob = jobAdminService.start("indexCategoriesJob","category.names=" + category.getName() + ", base.ids=" + base.getBaseId());
            Thread.sleep(10000);
            String resultatJob = jobAdminService.getSummary(idJob);
            LOGGER.debug("resultat du job d'indexation : {}", resultatJob);
            Map<Long, String> steps = jobAdminService.getStepExecutionSummaries(idJob);
            LOGGER.debug("etapes du job d'indexation : {}", steps);
            
         } else {
            LOGGER.debug("La category {} n'est pas associe a la base {}", new String[] {category.getName(), base.getBaseId()});
         }
      } else {
         LOGGER.debug("Impossible de récupérer la Category pour le {}", nomCategory);
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void getInfoCategory() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      
      // recuperation de la base
      LOGGER.debug("Recuperation de la base");
      Base base = serviceProvider.getBaseAdministrationService().getBase("GNT-INT");
      
      // recupere la categorie
      LOGGER.debug("Recuperation de la categorie");
      String nomCategory = "dli";
      
      Category category = storageAdminService.getCategory(nomCategory);
      if (category != null) {
         LOGGER.debug("Category {} récupérée", category.getName());
         LOGGER.debug("{}", category);
         
         // recupere l'association base/categorie
         LOGGER.debug("Recuperation de l'association de la categorie avec la base");
         BaseCategory baseCategory = base.getBaseCategory(category.getName());
         if (baseCategory != null) {
            LOGGER.debug("BaseCategory {} récupérée", category.getName());
            LOGGER.debug("{}", baseCategory);
         } else {
            LOGGER.debug("La category {} n'est pas associe a la base {}", new String[] {category.getName(), base.getBaseId()});
         }
      } else {
         LOGGER.debug("Impossible de récupérer la Category pour le {}", nomCategory);
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void indexCounterJob() throws Exception {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      JobAdministrationService jobAdminService = serviceProvider.getJobAdministrationService();
      
      // lancement de l'indexation
      LOGGER.debug("Lancement du job d'indexation");
      Long idJob = jobAdminService.startNextInstance("indexCounterJob");
      Thread.sleep(10000);
      String resultatJob = jobAdminService.getSummary(idJob);
      LOGGER.debug("resultat du job d'indexation : {}", resultatJob);
      Map<Long, String> steps = jobAdminService.getStepExecutionSummaries(idJob);
      LOGGER.debug("etapes du job d'indexation : {}", steps);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void updateBaseDfce() {
      
      String[] categories = { "nis",
            "nfs",
            "aex",
            "nti",
            "nfo",
            "nco",
            "dcd",
            "nen",
            "nff",
            "dfa",
            "cba",
            "nor",
            "ncc",
            "obj",
            "nid",
            "mre",
            "dpa",
            "npi",
            "sfa",
            "dsf",
            "lsf",
            "nlo",
            "nds",
            "ndf",
            "ddf",
            "mde",
            "nbl",
            "dli",
            "oec",
            "nbc",
            "dad",
            "bap",
            "dbp",
            "lbp",
            "cco",
            "dcc",
            "lcc",
            "cot",
            "cpt",
            "drh",
            "dar"
      };
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      
      // recuperation de la base
      LOGGER.debug("Recuperation de la base");
      Base base = serviceProvider.getBaseAdministrationService().getBase("GNT-INT");
      
      // recupere la categorie
      LOGGER.debug("Recuperation de la categorie");
      for (String nomCategory : categories) {
         Category category = storageAdminService.getCategory(nomCategory);
         if (category != null) {
            LOGGER.debug("Category {} récupérée", category.getName());
            // recupere l'association base/categorie
            LOGGER.debug("Recuperation de l'association de la categorie avec la base");
            BaseCategory baseCategory = base.getBaseCategory(category.getName());
            if (baseCategory != null) {
               LOGGER.debug("BaseCategory {} récupérée", category.getName());
            } else {
               LOGGER.debug("La category {} n'est pas associe a la base {}", new String[] {category.getName(), base.getBaseId()});
            }
            baseCategory.setMaximumValues(1);
         } else {
            LOGGER.debug("Impossible de récupérer la Category pour le {}", nomCategory);
         }
      }
      LOGGER.debug("Mise a jour de la base DFCE");
      serviceProvider.getBaseAdministrationService().updateBase(base);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
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
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   private byte[] convertLongToByte(Long value) {
      ByteBuffer buffer = ByteBuffer.allocate(8);
      buffer.putLong(value);
      return buffer.array();
   }
   
   @Test
   @Ignore
   public void updateBaseWithHector() {
      String[] categories = { "apr",
            "cop",
            "cog",
            "vrn",
            "dom",
            "dfc",
            "nbp",
            "ffi",
            "cse"
      };
      String nomBase = "SAE-TEST";
      boolean dryRun = true;

      LOGGER.debug("Recuperation de la liste des categories de la base {}", nomBase);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily("BaseCategoriesReference").setKeys(null, null);
      rangeQueryDocubase.setColumnNames("minValues", "categoryReference", "baseId");
      rangeQueryDocubase.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> columnBase = row.getColumnSlice().getColumnByName("baseId");
            if (columnBase != null && new String(columnBase.getValue()).equals(nomBase)) {
               HColumn<String, byte[]> columnCategory = row.getColumnSlice().getColumnByName("categoryReference");
               if (columnCategory != null && Arrays.asList(categories).contains(new String(columnCategory.getValue()))) {
                  HColumn<String, byte[]> columnMin = row.getColumnSlice().getColumnByName("minValues");
                  Long minValue = convertByteToLong(columnMin.getValue());
                  LOGGER.debug("category {} trouvée : {}", new String[] { new String(columnCategory.getValue()), minValue.toString() });
                  if (minValue.longValue() == 0) {
                     byte[] newValue = convertLongToByte(1L);
                     if (dryRun) {
                        LOGGER.debug("Mode dry run : Modification de la valeur min avec la valeur : {}", newValue);
                     } else {
                        // update a faire
                        Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase,
                              BytesArraySerializer.get());
                        columnMin.setValue(newValue);
                        mutator.insert(row.getKey(), "BaseCategoriesReference", columnMin);
                     }
                  }
               }
            }
         }
      }
   }
}
