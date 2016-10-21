package fr.urssaf.image.sae.test.divers.dfce;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

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
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
public class SuppressionBaseGNTTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionBaseGNTTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;

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
    * Suppression de la base GNT-PROD sur le SAE suite à un problème d'installation de la GNT
    */
   @Test
   @Ignore
   public void deleteBase() {
      String[] uuidsBaseJournaux = { "98d98f1c-dea3-400d-a1ef-ee3f6b9fd0ad", 
            "09866303-91fc-4605-9c49-8d27130e186a" 
      };
      String[] uuidsBaseGNT = { "E176155C-47C8-48EA-A7AC-8974F03F3A4C",
            "817B7F03-D910-4F2A-844F-B832ABBFB4ED",
            "8675D090-83F8-4A0B-8A18-04B0E3DA586A" };
      String nomBase = "GNT-PROD";
      String codeCourtDateArchivageGNT = "dag";
      String codeLongDateArchivageGNT = "DateArchivageGNT";
      boolean dryRun = true;
      
      LOGGER.debug("Lancement en mode dry run (aucun delete) : {}", dryRun);
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider
         .getSearchService();
      Base base = serviceProvider.getBaseAdministrationService()
         .getBase("DAILY_LOG_ARCHIVE_BASE");
      
      // suppression des documents de la base des journaux
      for (String idDoc : uuidsBaseJournaux) {
         LOGGER.debug("Recuperation du journal : {}", idDoc);
         final Document doc = searchService.getDocumentByUUID(base, UUID.fromString(idDoc));
         if (doc != null) {
            if (!dryRun) {
               LOGGER.debug("Suppression du journal : {}", idDoc);
               try {
                  serviceProvider.getStoreService().deleteDocument(UUID.fromString(idDoc));
               } catch (FrozenDocumentException e) {
                  LOGGER.error("Le journal {} est gelé. Impossible de le supprimer : {}", new String[] { idDoc, e.getMessage() });
               }
            } else {
               LOGGER.debug("Mode dry run : On devrait supprimer le journal {}", idDoc);
            }
         } else {
            LOGGER.error("Le journal {} n'existe pas. Impossible de le supprimer", idDoc);
         }
      }
      
      base = serviceProvider.getBaseAdministrationService()
         .getBase(nomBase);
      // suppression des documents de la base GNT-PROD
      for (String idDoc : uuidsBaseGNT) {
         LOGGER.debug("Recuperation du document : {}", idDoc);
         final Document doc = searchService.getDocumentByUUID(base, UUID.fromString(idDoc));
         if (doc != null) {
            if (!dryRun) {
               LOGGER.debug("Suppression du document : {}", idDoc);
               try {
                  serviceProvider.getStoreService().deleteDocument(UUID.fromString(idDoc));
               } catch (FrozenDocumentException e) {
                  LOGGER.error("Le document {} est gelé. Impossible de le supprimer : {}", new String[] { idDoc, e.getMessage() });
               }
            } else {
               LOGGER.debug("Mode dry run : On devrait supprimer le document {}", idDoc);
            }
         } else {
            LOGGER.error("Le document {} n'existe pas. Impossible de le supprimer", idDoc);
         }
      }
      
      // suppression de la base GNT-PROD
      LOGGER.debug("Recuperation de la base : {}", nomBase);
      final Base baseGNT = serviceProvider.getBaseAdministrationService()
         .getBase(nomBase);
      if (baseGNT != null) {
         if (!dryRun) {
            LOGGER.debug("Suppression de la base : {}", nomBase);
            serviceProvider.getBaseAdministrationService().deleteBase(baseGNT);
         } else {
            LOGGER.debug("Mode dry run : On devrait supprimer la base {}", nomBase);
         }
      } else {
         LOGGER.error("La base {} n'existe pas. Impossible de la supprimer", nomBase);
      }
      
      // suppression du lien entre les categories et la base
      LOGGER.debug("Recuperation des categories docubase associee a la base : {}", nomBase);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<String,String,String> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
      rangeQueryDocubase.setColumnFamily("BaseCategoriesReference").setKeys(null, null);
      rangeQueryDocubase.setColumnNames("categoryReference");
      QueryResult<OrderedRows<String,String, String>> rangeResultDocubase = rangeQueryDocubase.execute();
      if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
         Iterator<Row<String, String, String>> iterateur = rangeResultDocubase.get().iterator();
         while (iterateur.hasNext()) {
            Row<String, String, String> row = iterateur.next();
            if (row.getKey().startsWith(nomBase)) {
               LOGGER.debug("Lien Base / Categorie trouvee : {}", row.getKey());
               if (!dryRun) {
                  LOGGER.debug("Suppression du lien base / categorie categorie : {}", row.getKey());
                  Mutator<String> mutator = HFactory.createMutator(keyspaceDocubase,
                        StringSerializer.get());
                  mutator.delete(row.getKey(), "BaseCategoriesReference", null, StringSerializer.get());
               } else {
                  LOGGER.debug("Mode dry run : On devrait supprimer le lien base / categorie : {}", row.getKey());
               }
            }
         }
      }
      
      // suppression de la categorie dag
      LOGGER.debug("Recuperation de la categorie docubase : {}", codeCourtDateArchivageGNT);
      SliceQuery<String,String,String> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
      queryDocubase.setColumnFamily("CategoriesReference").setKey(codeCourtDateArchivageGNT).setRange(null, null, false, 100);
      QueryResult<ColumnSlice<String,String>> resultDocubase = queryDocubase.execute();
      if (resultDocubase != null && resultDocubase.get() != null) {
         LOGGER.debug("Categorie trouvee : {}", codeCourtDateArchivageGNT);
         if (!dryRun) {
            LOGGER.debug("Suppression de la categorie : {}", codeCourtDateArchivageGNT);
            Mutator<String> mutator = HFactory.createMutator(keyspaceDocubase,
                  StringSerializer.get());
            mutator.delete(codeCourtDateArchivageGNT, "CategoriesReference", null, StringSerializer.get());
         } else {
            LOGGER.debug("Mode dry run : On devrait supprimer la categorie : {}", codeCourtDateArchivageGNT);
            for (HColumn<String, String> column : resultDocubase.get().getColumns()) {
               LOGGER.debug("{}: {}", new String[] {column.getName(), column.getValue() });
            }
         }
      }
      
      // suppression de la metadata DateArchivageGNT
      LOGGER.debug("Recuperation de la metadata SAE : {}", codeLongDateArchivageGNT);
      SliceQuery<String,String,String> querySAE = HFactory.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
      querySAE.setColumnFamily("Metadata").setKey(codeLongDateArchivageGNT).setRange(null, null, false, 100);
      QueryResult<ColumnSlice<String,String>> resultSAE = querySAE.execute();
      if (resultSAE != null && resultSAE.get() != null) {
         LOGGER.debug("Metadata trouvee : {}", codeLongDateArchivageGNT);
         if (!dryRun) {
            LOGGER.debug("Suppression de la metadata : {}", codeLongDateArchivageGNT);
            Mutator<String> mutator = HFactory.createMutator(keyspace,
                  StringSerializer.get());
            mutator.delete(codeLongDateArchivageGNT, "Metadata", null, StringSerializer.get());
         } else {
            LOGGER.debug("Mode dry run : On devrait supprimer la metadata : {}", codeLongDateArchivageGNT);
            for (HColumn<String, String> column : resultSAE.get().getColumns()) {
               LOGGER.debug("{}: {}", new String[] {column.getName(), column.getValue() });
            }
         }
      }
      
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
}
