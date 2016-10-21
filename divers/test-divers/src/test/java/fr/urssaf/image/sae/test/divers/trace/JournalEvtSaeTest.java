package fr.urssaf.image.sae.test.divers.trace;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.support.clock.impl.JobClockSupportImpl;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDocDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.service.impl.JournalEvtServiceImpl;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.service.support.TraceFileSupport;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;

@RunWith(BlockJUnit4ClassRunner.class)
public class JournalEvtSaeTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(JournalEvtSaeTest.class);
   
   // Developpement 
   //private String hosts = "cer69imageint9.cer69.recouv";
   //private String hosts = "cer69imageint10.cer69.recouv";
   
   // Recette interne GNT
   private String hosts = "cnp69devgntcas1.gidn.recouv:9160,cnp69devgntcas2.gidn.recouv:9160";
   
   // Recette interne GNS
   //private String hosts = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   
   // Integration nationale GNT
   //private String hosts = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160";
   
   // Integration nationale GNS
   //private String hosts = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
   
   // Validation nationale GNT
   //private String hosts = "cnp69givngntcas1.cer69.recouv:9160,cnp69givngntcas2.cer69.recouv:9160,cnp69givngntcas3.cer69.recouv:9160";
   
   // Validation nationale GNS
   //private String hosts = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
   
   // Pre-prod nationale GNT
   //private String hosts = "cnp69pregntcas1.cer69.recouv:9160,cnp69pregntcas2.cer69.recouv:9160,cnp69pregntcas3.cer69.recouv:9160";
   
   // Pre-prod nationale GNS
   //private String hosts = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas2.cer69.recouv,cnp69pregnscas3.cer69.recouv,cnp69pregnscas4.cer69.recouv,cnp69pregnscas5.cer69.recouv,cnp69pregnscas6.cer69.recouv";
   
   // Prod nationale GNT
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   //private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
   
   private Keyspace getKeyspaceSAE() {
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
      return HFactory.createKeyspace("SAE", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private TraceJournalEvtSupport getInstanceTraceJournalEvtSupport(Keyspace keyspace) {
      TraceJournalEvtDao dao = new TraceJournalEvtDao(keyspace);
      TraceJournalEvtIndexDao indexDao = new TraceJournalEvtIndexDao(keyspace);
      TraceJournalEvtIndexDocDao indexDocDao = new TraceJournalEvtIndexDocDao(keyspace);
      TimeUUIDEtTimestampSupport timeUUIDSupport = new TimeUUIDEtTimestampSupport();
      return new TraceJournalEvtSupport(dao, indexDao, indexDocDao, timeUUIDSupport);
   }
   
   private JobClockSupport getInstanceJobClockSupport(Keyspace keyspace) {
      JobClockConfiguration clockConfiguration = new JobClockConfiguration();
      clockConfiguration.setMaxTimeSynchroWarn(2000000);
      clockConfiguration.setMaxTimeSynchroError(10000000);
      return new JobClockSupportImpl(keyspace, clockConfiguration);
   }
   
   private JournalEvtServiceImpl getInstance(Keyspace keyspace) {
      TraceJournalEvtSupport journalEvtSupport = getInstanceTraceJournalEvtSupport(keyspace);
      JobClockSupport jobClockSupport = getInstanceJobClockSupport(keyspace);
      LoggerSupport loggerSupport = new LoggerSupport();
      TraceFileSupport fileSupport = new TraceFileSupport();
      return new JournalEvtServiceImpl(journalEvtSupport, jobClockSupport, loggerSupport, fileSupport);
   }
   
   @Test
   public void getJournalSaeEventsAsJson() {
      
      UUID idArchive = UUID.fromString("C81452FA-0CD8-4939-9791-879CF83A08AE");
      
      LOGGER.debug("Recup des evenements du doc {}", idArchive.toString());
      List<TraceJournalEvtIndexDoc> evtSae = getInstance(getKeyspaceSAE())
            .getTraceJournalEvtByIdDoc(idArchive);
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
      
      for (TraceJournalEvtIndexDoc event : evtSae) {
         LOGGER.debug("evenement : {} à {}", new String[] { event.getCodeEvt(), formatter.format(event.getTimestamp())});
      }
      
      ObjectMapper mapper = new ObjectMapper();
      try {
         String retour = mapper.writeValueAsString(evtSae);
         LOGGER.debug("Couversion json : {}", retour);
      } catch (JsonGenerationException e) {
         LOGGER.error(e.getMessage());
      } catch (JsonMappingException e) {
         LOGGER.error(e.getMessage());
      } catch (IOException e) {
         LOGGER.error(e.getMessage());
      }
   }
   
   @Test
   public void getTracesByDate() {
      DateTime date = new DateTime().withDate(2016, 6, 3).withTimeAtStartOfDay();
      TraceJournalEvtSupport support = getInstanceTraceJournalEvtSupport(getKeyspaceSAE());
      List<TraceJournalEvtIndex> evtSae = support.findByDate(date.toDate());
      LOGGER.debug("nb trace : {}", evtSae.size());
      long nbMissingEvt = 0;
      for (TraceJournalEvtIndex evt : evtSae) {
         TraceJournalEvt trace = support.find(evt.getIdentifiant());
         if (trace == null) {
            LOGGER.debug("evenement manquant : {}", evt.getIdentifiant());
            nbMissingEvt++;
         } else if (trace.getIdentifiant() == null) {
            LOGGER.debug("identifiant manquant : {}", evt.getIdentifiant());
         }
      }
      LOGGER.debug("nb evt manquant : {}", nbMissingEvt);
   }

   /*@Test
   public void getTracesJournalSaeByDate() {
      
      DateTime debut = new DateTime().withDate(2015, 12, 9).withTimeAtStartOfDay();
      DateTime fin = new DateTime().withDate(2015, 12, 9).withTime(23, 59, 59, 999);
      int limite = 200000;
      String cs = "CS_SICOMOR";
      
      Map<UUID, List<TraceJournalEvt>> map = new HashMap<UUID, List<TraceJournalEvt>>();
      
      JournalEvtServiceImpl service = getInstance(getKeyspaceSAE());
      
      LOGGER.debug("Recup des evenements du {}", debut.toString("dd/MM/yyyy"));
      List<TraceJournalEvtIndex> evtSae = service.lecture(debut.toDate(), fin.toDate(), limite, false);
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
      
      for (TraceJournalEvtIndex event : evtSae) {
         if (StringUtils.isEmpty(cs) || event.getContratService().equals(cs)) {
            TraceJournalEvt evenement = service.lecture(event.getIdentifiant());
            UUID idDoc = UUID.fromString((String) evenement.getInfos().get("idDoc"));
            LOGGER.debug("evenement : {} à {} -> {}", new String[] { event.getCodeEvt(), formatter.format(event.getTimestamp()), event.getIdentifiant().toString()});
            if (!map.containsKey(idDoc)) {
               map.put(idDoc, new ArrayList<TraceJournalEvt>());
            }
            map.get(idDoc).add(evenement);
         }
      }
      
      // analyse des traces 
      for (UUID idDoc : map.keySet()) {
         int compteurModif = 0;
         int compteurModifIdent = 0;
         String lastModifMeta = "";
         String lastDelMeta = "";
         for (TraceJournalEvt event : map.get(idDoc)) {
            compteurModif++;
            if (event.getCodeEvt().equals("DFCE_MODIF_DOC|OK")) {
               String modifMeta = (String) event.getInfos().get("modifiedMetadatas");
               String delMeta = (String) event.getInfos().get("deletedMetadatas");
               
               if (lastModifMeta.equals(modifMeta) && lastDelMeta.equals(delMeta)) {
                  compteurModifIdent++;
               } else {
                  lastModifMeta = modifMeta;
                  lastDelMeta = delMeta;
               }
            }
         }
      }
   }*/
}
