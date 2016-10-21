package fr.urssaf.image.sae.test.divers.dfce;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
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
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caucho.hessian.client.HessianConnectionException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.commons.dfce.service.impl.DFCEConnectionServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
public class RecordManagerTest {

private static final Logger LOGGER = LoggerFactory.getLogger(RecordManagerTest.class);
   
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
   public void countOpenSession() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

      Date dateMin = new GregorianCalendar(2014, 6, 25, 14, 00, 0).getTime();
      Date dateMax = new GregorianCalendar(2014, 6, 25, 15, 00, 0).getTime();
      
      LOGGER.debug("Récupération des événements système de {} à {}", new String[] { formatterJJMMAAAA.format(dateMin), formatterJJMMAAAA.format(dateMax)});
      final RecordManagerService recordManagerService = serviceProvider
            .getRecordManagerService();
      
      List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(dateMin, dateMax);
      
      int counter = 0;
      for (RMSystemEvent event : events) {
         if ("openSession".equals(event.getEventDescription())) {
            counter++;
         }
      }
      LOGGER.debug("Nombre d'ouverture de session : {}", counter);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   @Ignore
   public void countOpenSessionByHour() {
      Map<Integer, Integer> countByHour = new TreeMap<Integer, Integer>();
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

      Date dateMin = new GregorianCalendar(2014, 6, 25, 8, 00, 0).getTime();
      Date dateMax = new GregorianCalendar(2014, 6, 25, 18, 00, 0).getTime();
      
      LOGGER.debug("Récupération des événements système de {} à {}", new String[] { formatterJJMMAAAA.format(dateMin), formatterJJMMAAAA.format(dateMax)});
      final RecordManagerService recordManagerService = serviceProvider
            .getRecordManagerService();
      
      List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(dateMin, dateMax);
      
      for (RMSystemEvent event : events) {
         if ("openSession".equals(event.getEventDescription())) {
            int hour = event.getEventDate().getHours();
            int counter = 0;
            if (countByHour.get(Integer.valueOf(hour)) != null) {
               counter = countByHour.get(Integer.valueOf(hour)).intValue();
            } 
            counter++;
            countByHour.put(Integer.valueOf(hour), Integer.valueOf(counter));
         }
      }
      Iterator<Entry<Integer, Integer>> iterateur = countByHour.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<Integer, Integer> entry = iterateur.next();
         int hour = entry.getKey().intValue();
         LOGGER.debug("De {}h à {}h : {}", new Integer[] { Integer.valueOf(hour), Integer.valueOf(hour + 1), entry.getValue()});
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   //@Ignore
   public void rechercheSystemEventOrphelin() throws MalformedURLException {
      String[] serveursAppli = { "hwi69saeappli1.cer69.recouv", "hwi69saeappli2.cer69.recouv", "hwi69saeappli3.cer69.recouv" };
      String hostCassandra = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
      String nomBase = "SAE-PROD";
      Date dateMax = null;
      Date dateDebutIncident = null;
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      // Etape 1 - recupere la date de dernier success de la purge des documents
      // -----------------------------------------------------------------------
      // Cette date permet de savoir jusqu'a ou rechercher les documents orphelins
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hostCassandra);
      SliceQuery<String,String,Date> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), DateSerializer.get());
      queryDocubase.setColumnFamily("Jobs").setKey("SYSTEM_EVENTS_PURGE_JOB");
      queryDocubase.setColumnNames("lastSuccessfullRunDate", "launchDate");
      QueryResult<ColumnSlice<String, Date>> resultDocubase = queryDocubase.execute();
      if (resultDocubase != null && resultDocubase.get() != null) {
         for (HColumn<String, Date> colonne : resultDocubase.get().getColumns()) {
            LOGGER.debug("{} : {}", new String[] {colonne.getName(), formatter.format(colonne.getValue())});
            if (colonne.getName().equals("launchDate")) {
               dateMax = new DateTime(colonne.getValue().getTime()).withTimeAtStartOfDay().toDate();
            } else if (colonne.getName().equals("lastSuccessfullRunDate")) {
               dateDebutIncident = new DateTime(colonne.getValue().getTime()).withTimeAtStartOfDay().toDate();
            }
         }
      }
      
      /*if (dateMax == null) {
         dateMax = new DateTime().withTimeAtStartOfDay().plusDays(-1).toDate();
      }*/
      dateMax = new DateTime().withDate(2014, 6, 30).withTimeAtStartOfDay().toDate();
      
      if (dateDebutIncident == null) {
         dateDebutIncident = new DateTime().withDate(2014, 1, 1).withTimeAtStartOfDay().toDate();
      }
      
      boolean continu = true;
      ServiceProvider serviceProvider = null;
      
      final int decalageHeure = -3;
      int compteurReconnexion = 0;
      
      DFCEConnection connexion = new DFCEConnection();
      connexion.setLogin("_ADMIN");
      connexion.setPassword("DOCUBASE");
      connexion.setHostPort(8080);
      connexion.setContextRoot("/dfce-webapp/toolkit/");
      connexion.setSecure(false);
      connexion.setTimeout(180000);
      connexion.setCheckHash(true);
      connexion.setDigestAlgo("SHA-1");
      connexion.setBaseName(nomBase);
      String serveur = serveursAppli[compteurReconnexion % serveursAppli.length];
      connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
      connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
      connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
      
      DFCEConnectionService dfceConnectService = new DFCEConnectionServiceImpl(connexion);
      
      Date dateMin = new DateTime(dateMax.getTime()).plusHours(decalageHeure).toDate();
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      try {
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
         
         while (continu) {
         
            LOGGER.debug("Récupération des événements système de {} à {}", new String[] { formatterJJMMAAAA.format(dateMin), formatterJJMMAAAA.format(dateMax)});
            final RecordManagerService recordManagerService = serviceProvider
               .getRecordManagerService();
            List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(dateMin, dateMax);
            LOGGER.debug("{} événements récupérés", events.size());
            
            for (RMSystemEvent event : events) {
               if (event.getArchiveUUID() == null) {
                  LOGGER.debug("Evenement sans id d'archive : {} : {} a {} -> {}", new String[] { event.getEventUUID().toString(), event.getEventDescription(), formatterJJMMAAAA.format(event.getEventDate()), event.getAttributes().toString()});
               } 
            }
            dateMax = dateMin;
            dateMin = new DateTime(dateMax.getTime()).plusHours(decalageHeure).toDate();
            continu = (dateMax.after(dateDebutIncident));
         }
         
         LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
         serviceProvider.disconnect();
      
      } catch (HessianConnectionException ex) {
         LOGGER.error("SocketTimeOut : ", ex.getMessage());
         compteurReconnexion++;
         if (serviceProvider != null) {
            LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
            serviceProvider.disconnect();
         }
         serveur = serveursAppli[compteurReconnexion % serveursAppli.length];
         connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
         connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
         connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
         dfceConnectService = new DFCEConnectionServiceImpl(connexion);
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
      }
   }
   
   @Test
   @Ignore
   public void rechercheDocEventOrphelin() throws MalformedURLException {
      String[] serveursAppli = { "hwi69givnsaeappli.cer69.recouv" };
      String hostCassandra = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      String nomBase = "SAE-GIVN";
      Date dateMax = null;
      Date dateDebutIncident = null;
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      // Etape 1 - recupere la date de dernier success de la purge des documents
      // -----------------------------------------------------------------------
      // Cette date permet de savoir jusqu'a ou rechercher les documents orphelins
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hostCassandra);
      SliceQuery<String,String,Date> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), DateSerializer.get());
      queryDocubase.setColumnFamily("Jobs").setKey("DOCUMENT_EVENTS_PURGE_JOB");
      queryDocubase.setColumnNames("lastSuccessfullRunDate", "launchDate");
      QueryResult<ColumnSlice<String, Date>> resultDocubase = queryDocubase.execute();
      if (resultDocubase != null && resultDocubase.get() != null) {
         for (HColumn<String, Date> colonne : resultDocubase.get().getColumns()) {
            LOGGER.debug("{} : {}", new String[] {colonne.getName(), formatter.format(colonne.getValue())});
            if (colonne.getName().equals("launchDate")) {
               dateMax = new DateTime(colonne.getValue().getTime()).withTimeAtStartOfDay().toDate();
            } else if (colonne.getName().equals("lastSuccessfullRunDate")) {
               dateDebutIncident = new DateTime(colonne.getValue().getTime()).withTimeAtStartOfDay().toDate();
            }
         }
      }
      LOGGER.debug("Début de l'incident : {}", formatter.format(dateDebutIncident));
      
      if (dateMax == null) {
         dateMax = new DateTime().withTimeAtStartOfDay().plusDays(-1).toDate();
      }
      
      if (dateDebutIncident == null) {
         dateDebutIncident = new DateTime().withDate(2014, 1, 1).withTimeAtStartOfDay().toDate();
      }
      
      boolean continu = true;
      ServiceProvider serviceProvider = null;
      
      final int decalageHeure = -24;
      int compteurReconnexion = 0;
      
      DFCEConnection connexion = new DFCEConnection();
      connexion.setLogin("_ADMIN");
      connexion.setPassword("DOCUBASE");
      connexion.setHostPort(8080);
      connexion.setContextRoot("/dfce-webapp/toolkit/");
      connexion.setSecure(false);
      connexion.setTimeout(180000);
      connexion.setCheckHash(true);
      connexion.setDigestAlgo("SHA-1");
      connexion.setBaseName(nomBase);
      String serveur = serveursAppli[compteurReconnexion % serveursAppli.length];
      connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
      connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
      connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
      
      DFCEConnectionService dfceConnectService = new DFCEConnectionServiceImpl(connexion);
      
      Date dateMin = new DateTime(dateMax.getTime()).plusHours(decalageHeure).toDate();
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      try {
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
         
         while (continu) {
         
            LOGGER.debug("Récupération des événements sur les documents de {} à {}", new String[] { formatterJJMMAAAA.format(dateMin), formatterJJMMAAAA.format(dateMax)});
            final RecordManagerService recordManagerService = serviceProvider
               .getRecordManagerService();
            List<RMDocEvent> events = recordManagerService.getDocumentEventLogsByDates(dateMin, dateMax);
            LOGGER.debug("{} événements récupérés", events.size());
            
            for (RMDocEvent event : events) {
               
               if (event.getArchiveUUID() == null) {
                  LOGGER.debug("Evenement sans id d'archive : {} : {} a {} -> {}", new String[] { event.getEventUUID().toString(), event.getEventType().toString(), formatterJJMMAAAA.format(event.getEventDate()), event.getAttributes().toString()});
               }
               if (event.getEventUUID().toString().equals("ba8dd6d5-fb9c-4b1d-ad3b-2a3bad45f5f6") 
                     || event.getEventUUID().toString().equals("9065fc65-1e14-48d0-a97e-f93ec08e6bb1")) {
                  LOGGER.debug("Evenement trouvé : {} : {} a {} -> {} idArchive -> {}", new String[] { event.getEventUUID().toString(), event.getEventType().toString(), formatterJJMMAAAA.format(event.getEventDate()), event.getAttributes().toString(), (event.getArchiveUUID() == null ? "null" : event.getArchiveUUID().toString()) });
               }
            }
            dateMax = dateMin;
            dateMin = new DateTime(dateMax.getTime()).plusHours(decalageHeure).toDate();
            continu = (dateMax.after(dateDebutIncident));
         }
         
         LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
         serviceProvider.disconnect();
      
      } catch (HessianConnectionException ex) {
         LOGGER.error("SocketTimeOut : ", ex.getMessage());
         compteurReconnexion++;
         if (serviceProvider != null) {
            LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
            serviceProvider.disconnect();
         }
         serveur = serveursAppli[compteurReconnexion % serveursAppli.length];
         connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
         connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
         connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
         dfceConnectService = new DFCEConnectionServiceImpl(connexion);
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
      }
   }
   
   @Test
   @Ignore
   public void traceDateDebutAno() {
      // lastSuccessfull est la date de dernier success dans la CF Jobs pour la row SYSTEM_EVENTS_PURGE_JOB
      DateTime lastSuccessfull = new DateTime(2014, 6, 14, 2, 30, 0, 0);
      // on ne garde que 30 jours de retention de traces "live"
      DateTime dateDebutAno = lastSuccessfull.plusDays(30);
      
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      // date a laquelle on trouve la premiere anomalie de purge
      LOGGER.debug("date de debut d'ano : {}", formatterJJMMAAAA.format(dateDebutAno.toDate()));
   }
   
   @Test
   @Ignore
   public void traceSommaireAno() throws MalformedURLException {
      ServiceProvider serviceProvider = null;
      long idJobExecution = 1842; // identifiant du job de purge en erreur le 05/08/2014
      
      DFCEConnection connexion = new DFCEConnection();
      connexion.setLogin("_ADMIN");
      connexion.setPassword("DOCUBASE");
      connexion.setHostPort(8080);
      connexion.setContextRoot("/dfce-webapp/toolkit/");
      connexion.setSecure(false);
      connexion.setTimeout(180000);
      connexion.setCheckHash(true);
      connexion.setDigestAlgo("SHA-1");
      connexion.setBaseName("SAE-PROD");
      String serveur = "hwi69saeappli1.cer69.recouv";
      connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
      connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
      connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
      
      DFCEConnectionService dfceConnectService = new DFCEConnectionServiceImpl(connexion);
      
      try {
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
         
         LOGGER.debug("{}", serviceProvider.getJobAdministrationService().getSummary(idJobExecution));
         
         LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
         serviceProvider.disconnect();
         
      } catch (HessianConnectionException ex) {
         LOGGER.error("SocketTimeOut : ", ex.getMessage());
         if (serviceProvider != null) {
            LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
            serviceProvider.disconnect();
         }
      } catch (NoSuchDfceJobExecutionException ex) {
         LOGGER.error("Job iconnu : ", ex.getMessage());
      }
   }
   
   @Test
   //@Ignore
   public void getArchiveEventsByIdDoc() {
      ServiceProvider serviceProvider = null;
      UUID idDoc = UUID.fromString("c2b101b9-a549-4161-89f6-4203d5fb21a0");
      try {
         LOGGER.debug("Ouverture de la connexion à DFCE");
         serviceProvider = dfceConnectionService.openConnection();
         
         LOGGER.debug("Recuperation des events sur les docs");
         List<RMDocEvent> events = serviceProvider.getRecordManagerService().getDocumentEventLogsByUUID(idDoc);
         String eventsInJson = JSONArray.toJSONString(events);
         LOGGER.debug("evenements : {}", eventsInJson);
         
         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
         
         for (RMDocEvent event : events) {
            LOGGER.debug("evenement : {} à {}", new String[] { event.getEventType().toString(), formatter.format(event.getEventDate())});
         }
         
         LOGGER.debug("Fermeture de la connexion à DFCE");
         serviceProvider.disconnect();
         
      } catch (HessianConnectionException ex) {
         LOGGER.error("SocketTimeOut : ", ex.getMessage());
         if (serviceProvider != null) {
            LOGGER.debug("Fermeture de la connexion à DFCE");
            serviceProvider.disconnect();
         }
      } 
   }
   
   @Test
   //@Ignore
   public void statSystemEventOrphelin() throws MalformedURLException {
      boolean continu = true;
      ServiceProvider serviceProvider = null;
      String[] serveursAppli = { "hwi69pprodsaeapp.cer69.recouv" };
      final int decalageHeure = 3;
      int compteurReconnexion = 0;
      long compteurEvents = 0;
      long compteurOrphanEvents = 0;
      
      DFCEConnection connexion = new DFCEConnection();
      connexion.setLogin("_ADMIN");
      connexion.setPassword("DOCUBASE");
      connexion.setHostPort(8080);
      connexion.setContextRoot("/dfce-webapp/toolkit/");
      connexion.setSecure(false);
      connexion.setTimeout(180000);
      connexion.setCheckHash(true);
      connexion.setDigestAlgo("SHA-1");
      connexion.setBaseName("SAE-PROD");
      String serveur = serveursAppli[compteurReconnexion % serveursAppli.length];
      connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
      connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
      connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
      
      DFCEConnectionService dfceConnectService = new DFCEConnectionServiceImpl(connexion);
      
      Date dateMin = new GregorianCalendar(2014, 5, 13, 23, 40, 0).getTime();
      Date dateMax = new DateTime(dateMin.getTime()).plusHours(decalageHeure).toDate();
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      try {
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
         
         while (continu) {
         
            LOGGER.debug("Récupération des événements système de {} à {}", new String[] { formatterJJMMAAAA.format(dateMin), formatterJJMMAAAA.format(dateMax)});
            final RecordManagerService recordManagerService = serviceProvider
               .getRecordManagerService();
            List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(dateMin, dateMax);
            LOGGER.debug("{} événements récupérés", events.size());
            
            for (RMSystemEvent event : events) {
               if (event.getArchiveUUID() == null) {
                  LOGGER.debug("Evenement sans id d'archive : {} : {} a {} -> {}", new String[] { event.getEventUUID().toString(), event.getEventDescription(), formatterJJMMAAAA.format(event.getEventDate()), event.getAttributes().toString()});
                  compteurOrphanEvents++;
               } 
            }
            dateMin = dateMax;
            dateMax = new DateTime(dateMin.getTime()).plusHours(decalageHeure).toDate();
            compteurEvents += events.size();
            continu = (events.size() > 0);
         }
         
         
         LOGGER.debug("{} événements orphelins / {} événements système", new Long[] { compteurOrphanEvents, compteurEvents });
         
         LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
         serviceProvider.disconnect();
      
      } catch (HessianConnectionException ex) {
         LOGGER.error("SocketTimeOut : ", ex.getMessage());
         compteurReconnexion++;
         if (serviceProvider != null) {
            LOGGER.debug("Fermeture de la connexion à DFCE {}", serveur);
            serviceProvider.disconnect();
         }
         serveur = serveursAppli[compteurReconnexion % serveursAppli.length];
         connexion.setHostName(serveur.substring(0, serveur.indexOf(".")));
         connexion.setUrlToolkit("http://" + serveur + ":8080/dfce-webapp/toolkit/");
         connexion.setServerUrl(new URL(connexion.getUrlToolkit()));
         dfceConnectService = new DFCEConnectionServiceImpl(connexion);
         LOGGER.debug("Ouverture de la connexion à DFCE : {}", serveur);
         serviceProvider = dfceConnectService.openConnection();
      }
   }
   
   private Keyspace getKeyspaceDocubaseFromKeyspace(String hosts) {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator;
      if (StringUtils.isEmpty(hosts)) {
         hostConfigurator = new CassandraHostConfigurator(
               cassandraServer.getHosts());
      } else {
         hostConfigurator = new CassandraHostConfigurator(
               hosts);
      }
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
   
   private String getReadableUTF8String(byte[] bytes) throws Exception {
      String result = "";
      // Cf http://fr.wikipedia.org/wiki/UTF-8

      int i = 0;
      while (i < bytes.length) {
         byte b = bytes[i];
         // 128 = 10000000
         if ((b & 128) == 0) {
            // On est dans le cas d'un caractère à 7 bits : 0xxxxxxx
            if (b < 32) {
               // Caractère non imprimable
               result += "\\x" + getHexString(b);
            } else {
               result += (char) b;
            }
            i++;
            continue;
         } else {
            // Est-on dans cette forme là ? 110xxxxx 10xxxxxx
            // 192 = 11000000
            // 32 = 00100000
            if (((b & 192) == 192) && ((b & 32) == 0)) {
               if (i < bytes.length - 1) {
                  byte b2 = bytes[i + 1];
                  // 64 = 01000000
                  if (((b2 & 128) == 128) && ((b2 & 64) == 0)) {
                     byte[] myBytes = new byte[2];
                     myBytes[0] = b;
                     myBytes[1] = b2;
                     result += new String(myBytes, "UTF-8");
                     i += 2;
                     continue;
                  }
               }
            }
         }
         // Ce n'est pas un caractère UTF8
         result += "\\x" + getHexString(b);
         i++;
      }
      return result;
   }
   
   private String getHexString(byte b) throws Exception {
      byte[] bytes = new byte[1];
      bytes[0] = b;
      return getHexString(bytes);
   }
   
   /**
    * Renvoie la représentation hexadécimale d'un tableau de bytes
    * 
    * @param bytes
    *           tableau de bytes
    * @return
    * @throws Exception
    */
   private String getHexString(byte[] bytes) throws Exception {
      String result = "";
      for (int i = 0; i < bytes.length; i++) {
         result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
      }
      return result;
   }
   
   @Test
   public void getSystemEventsOrphelinByCassandra() throws Exception {
      String hostCassandra = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
      String[] columns = {"eventUUID", "eventDate", "archiveUUID" };
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hostCassandra);

      String cfName = "SystemEventLog";

      RangeSlicesQuery<byte[], String, byte[]> query = HFactory
            .createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer
                  .get(), StringSerializer.get(), BytesArraySerializer.get());
      query.setColumnFamily(cfName).setKeys(null, null).setColumnNames(columns);
      query.setRowCount(5000);
      QueryResult<OrderedRows<byte[], String, byte[]>> result = query
            .execute();
      for (Row<byte[], String, byte[]> row : result.get().getList()) {
         if (!row.getColumnSlice().getColumns().isEmpty() && row.getColumnSlice().getColumnByName("archiveUUID") == null) {
            //HColumn<String, byte[]> colonneDate = row.getColumnSlice().getColumnByName("eventDate");
            
            //Long timestamp = convertByteToLong(colonneDate.getValue());
            //DateTime date = new DateTime(timestamp);
            //LOGGER.debug("oups: {}", date);
            
            HColumn<String, byte[]> colonneUuid = row.getColumnSlice().getColumnByName("eventUUID");
            LOGGER.debug("oups: {}", getHexString(colonneUuid.getValue()));
            
         }
      }
   }
   
   /*@Test
   public void getSystemEventsOrphelinByDateWithCassandra() throws Exception {
      String hostCassandra = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
      
      String date = "20140830";
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace(hostCassandra);

      String cfName = "SystemEventLogByTimeSerialized";

      SliceQuery<String, byte[], byte[]> query = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(),
            BytesArraySerializer.get(), BytesArraySerializer.get());
      query.setKey(date);
      query.setColumnFamily(cfName);

      AllColumnsIterator<byte[], byte[]> it = new AllColumnsIterator<byte[], byte[]>(query);
      long compteur = 0;
      while (it.hasNext()) {
         HColumn<byte[], byte[]> column = it.next();
         compteur++;
         if (compteur % 10000 == 0) {
            LOGGER.debug("En cours : {}", new String[] { Long.toString(compteur) });
         }
      }
      LOGGER.debug("Nombre d'events pour le {} : {}", new String[] {date, Long.toString(compteur) });
   }*/
}
