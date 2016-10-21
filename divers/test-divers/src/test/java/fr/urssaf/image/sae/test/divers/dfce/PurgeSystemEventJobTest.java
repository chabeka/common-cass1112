package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.commons.jobs.JobUtils;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.batch.DfceJobExecutionException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.test.divers.cassandra.AllColumnsIterator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
public class PurgeSystemEventJobTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(PurgeSystemEventJobTest.class);
   
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
   
   /**
    * Temps d'attente (2h)
    */
   private final static long DUREE_ATTENTE = 2 * 60 * 60 * 1000;
   
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
   
   private Keyspace getKeyspaceSAEFromKeyspace() {
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
      return HFactory.createKeyspace("SAE", cluster, ccl,
            failoverPolicy, credentials);
   }

   @Test
   @Ignore
   public void purgeToDate() {

      Date endDate = new DateTime(2014,12,31,2,31,0,0).toDate();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      // Lancement du job de purge DFCE
      try {

         DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
         String sEndDay = dateFormat.format(endDate);
         String jobParams = String.format("type=SYSTEM, end.date(date)=%s",
               sEndDay);
         
         LOGGER.debug("Lancement du job avec les parametres : {}", jobParams);

         Long idJob = serviceProvider.getJobAdministrationService().start(
               JobUtils.CLEAR_EVENT_JOB, jobParams);
         
         LOGGER.debug("Id du job lancé : {}", idJob);
         
         Thread.sleep(DUREE_ATTENTE);
         
         String resultat = serviceProvider.getJobAdministrationService().getSummary(idJob);
         LOGGER.debug("Resultat du job : {}", resultat);

      } catch (DfceJobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      } catch (UnexpectedJobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      } catch (InterruptedException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void getStatutJob() {

      Long idJob = 1L;
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      try {

         String resultat = serviceProvider.getJobAdministrationService().getSummary(idJob);
         LOGGER.debug("Resultat du job : {}", resultat);

      } catch (DfceJobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      } catch (UnexpectedJobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      } 
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void countSystemEventBetweenDate() {
         
      Date dateMax = new DateTime().withDate(2014, 12, 31).withTime(0,0,0,0).toDate();
      Date dateDebutIncident = new DateTime().withDate(2014, 12, 30).withTimeAtStartOfDay().toDate();
      
      boolean continu = true;
      ServiceProvider serviceProvider = null;
      
      final int decalageHeure = -1;
      long compteur = 0;
      long compteurOrphelin = 0;
      
      Date dateMin = new DateTime(dateMax.getTime()).plusHours(decalageHeure).toDate();
      //Date dateMin = new DateTime(dateMax.getTime()).plusMinutes(decalageHeure).toDate();
      SimpleDateFormat formatterJJMMAAAA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      serviceProvider = dfceConnectionService.openConnection();
      
      while (continu) {
      
         LOGGER.debug("Récupération des événements système de {} à {}", new String[] { formatterJJMMAAAA.format(dateMin), formatterJJMMAAAA.format(dateMax)});
         final RecordManagerService recordManagerService = serviceProvider
            .getRecordManagerService();
         List<RMSystemEvent> events = recordManagerService.getSystemEventLogsByDates(dateMin, dateMax);
         //LOGGER.debug("{} événements récupérés", events.size());
         compteur += events.size();
         for (RMSystemEvent event : events) {
            if (event.getArchiveUUID() == null) {
               LOGGER.debug("Evenement sans id d'archive : {} : {} a {} -> {}", new String[] { event.getEventUUID().toString(), event.getEventDescription(), formatterJJMMAAAA.format(event.getEventDate()), event.getAttributes().toString()});
               compteurOrphelin++;
            } else if (event.getEventUUID().toString().equals("46b4a962-5655-4776-b957-af9809139d07")) {
               LOGGER.debug("Evenement sans id d'archive : {} : {} a {} -> {}", new String[] { event.getEventUUID().toString(), event.getEventDescription(), formatterJJMMAAAA.format(event.getEventDate()), event.getAttributes().toString()});
               compteurOrphelin++;
            }
         }
         
         dateMax = dateMin;
         dateMin = new DateTime(dateMax.getTime()).plusHours(decalageHeure).toDate();
         //dateMin = new DateTime(dateMax.getTime()).plusMinutes(decalageHeure).toDate();
         continu = (dateMax.after(dateDebutIncident));
      }
      LOGGER.debug("Nb events : {}", compteur);
      LOGGER.debug("Nb events orphelin : {}", compteurOrphelin);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void getSystemEventByIdEventFromCassandra() {
      String uuid = "46b4a962-5655-4776-b957-af9809139d07";
      String dateEvent = "20141230";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, Composite, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, StringSerializer.get(), CompositeSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("SystemEventLogByTimeSerialized");

      AllColumnsIterator<Composite, byte[]> it = new AllColumnsIterator<Composite, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<Composite, byte[]> column = it.next();
         
         // nom de colonne (compositeSerializer) : date + event uuid
         // valeur (objectSerializer) : map (archiveUUID, eventDate, eventDescription, eventUUID, eventStatus, username, attributes)
         Date date = DateSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(0));
         UUID idEvent = UUIDSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(1));
         
         if (idEvent.toString().equals(uuid)) {
            
            Object valeur = ObjectSerializer.get().fromByteBuffer((ByteBuffer) column.getValueBytes());
            
            LOGGER.info("trouvé : {} -> {}", new String[] { formatter.format(date), idEvent.toString()});
            LOGGER.info("valeur : {}", new String[] { valeur.toString() });
            break;
         }
      }
   }
   
   @Test
   public void getDocEventByIdEventFromCassandra() {
      String uuid = "11514ad7-ae49-491a-a0ca-323e3c7bb218";
      String dateEvent = "20140407";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, Composite, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, StringSerializer.get(), CompositeSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("DocEventLogByTimeSerialized");

      AllColumnsIterator<Composite, byte[]> it = new AllColumnsIterator<Composite, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<Composite, byte[]> column = it.next();
         
         // nom de colonne (compositeSerializer) : date + event uuid
         // valeur (objectSerializer) : map (archiveUUID, eventDate, eventDescription, eventUUID, eventStatus, username, attributes)
         Date date = DateSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(0));
         UUID idEvent = UUIDSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(1));
         
         if (idEvent.toString().equals(uuid)) {
            
            Object valeur = ObjectSerializer.get().fromByteBuffer((ByteBuffer) column.getValueBytes());
            
            LOGGER.info("trouvé : {} -> {}", new String[] { formatter.format(date), idEvent.toString()});
            LOGGER.info("valeur : {}", new String[] { valeur.toString() });
            break;
         }
      }
   }
   
   @Test
   public void getSystemEventByIdArchivageFromCassandra() throws IOException, SearchQueryParseException {
      String uuid = "98d98f1c-dea3-400d-a1ef-ee3f6b9fd0ad";
      //String uuid = "09866303-91fc-4605-9c49-8d27130e186a";
      String dateEvent = "20141230";
      //String dateEvent = "20141231";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      long compteur = 0;
      
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:/tmp/system-event.csv")));
      
      String archiveUuid="archiveUUID=[" + uuid + "]";
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, Composite, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, StringSerializer.get(), CompositeSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("SystemEventLogByTimeSerialized");

      AllColumnsIterator<Composite, byte[]> it = new AllColumnsIterator<Composite, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<Composite, byte[]> column = it.next();
         
         // nom de colonne (compositeSerializer) : date + event uuid
         // valeur (objectSerializer) : map (archiveUUID, eventDate, eventDescription, eventUUID, eventStatus, username, attributes)
         Date date = DateSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(0));
         UUID idEvent = UUIDSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(1));
         Object valeur = ObjectSerializer.get().fromByteBuffer((ByteBuffer) column.getValueBytes());
         
         if (valeur.toString().indexOf(archiveUuid) != -1) {
            LOGGER.info("trouvé : {} -> {}", new String[] { formatter.format(date), idEvent.toString()});
            LOGGER.info("valeur : {}", new String[] { valeur.toString() });
            compteur++;
            out.write(idEvent.toString() + ";");
            out.write(formatter.format(date) + ";");
            out.write(valeur.toString().replace('\n', ' ') + ";\n");
         }
      }
      out.close();
      
      LOGGER.info("nb events : {}", compteur);
   }
   
   @Test
   public void getSystemEventFromCassandra() throws Exception {
      //String uuid = "46b4a962-5655-4776-b957-af9809139d07";
      String uuid = "c7f08efe-faac-44c6-9fa4-1f7ad301626c";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<byte[], String, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(uuid.getBytes("UTF-8"));
      query.setColumnFamily("SystemEventLog");

      AllColumnsIterator<String, byte[]> it = new AllColumnsIterator<String, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<String, byte[]> column = it.next();
         
         if (column.getName().equals("eventUUID")
               || column.getName().equals("archiveUUID")) {
            LOGGER.info("{} -> {}", column.getName(), UUIDSerializer.get().fromBytes(column.getValue()).toString());
         } else if (column.getName().equals("eventDate")) {
            LOGGER.info("{} -> {}", column.getName(), formatter.format(DateSerializer.get().fromBytes(column.getValue())));
         } else {
            LOGGER.info("{} -> {}", column.getName(), new String(column.getValue()));
         }
      }
      
      /*Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[], String, byte[]> query = HFactory.createRangeSlicesQuery(
            keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(),
            BytesArraySerializer.get());
      query.setKeys(null, null);
      query.setColumnFamily("SystemEventLog");
      query.setColumnNames("eventUUID", "archiveUUID", "eventDate", "eventDescription");
      query.setRowCount(25);
      
      QueryResult<OrderedRows<byte[], String, byte[]>> result = query.execute();
      if (!result.get().getList().isEmpty()) {
         for (Row<byte[], String, byte[]> row : result.get().getList()) {
            if (!row.getColumnSlice().getColumns().isEmpty()) {
               //LOGGER.info("row : {}", getReadableUTF8String(row.getKey()));
               LOGGER.info("row : {}", new String(row.getKey(), "UTF-8"));
            }
            for (HColumn<String, byte[]> colonne : row.getColumnSlice().getColumns()) {
               if (colonne.getName().equals("eventUUID")
                     || colonne.getName().equals("archiveUUID")) {
                  LOGGER.info("{} -> {}", colonne.getName(), UUIDSerializer.get().fromBytes(colonne.getValue()).toString());
               } else if (colonne.getName().equals("eventDate")) {
                  LOGGER.info("{} -> {}", colonne.getName(), formatter.format(DateSerializer.get().fromBytes(colonne.getValue())));
               } else {
                  LOGGER.info("{} -> {}", colonne.getName(), new String(colonne.getValue()));
               }
            }
         }
      }*/
   }
   
   @Test
   public void testExistanceSystemEvent() throws Exception {
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:/tmp/system-event.csv")));
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      String line = in.readLine();
      while (line != null) {
         
         String uuid = line.split(";")[0];
      
         SliceQuery<byte[], String, byte[]> query = HFactory.createSliceQuery(
               keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(),
               BytesArraySerializer.get());
         query.setKey(uuid.getBytes("UTF-8"));
         query.setColumnFamily("SystemEventLog");
   
         AllColumnsIterator<String, byte[]> it = new AllColumnsIterator<String, byte[]>(
               query);
         while (it.hasNext()) {
            HColumn<String, byte[]> column = it.next();
            
            if (column.getName().equals("eventUUID")
                  || column.getName().equals("archiveUUID")) {
               LOGGER.info("{} -> {}", column.getName(), UUIDSerializer.get().fromBytes(column.getValue()).toString());
            } else if (column.getName().equals("eventDate")) {
               LOGGER.info("{} -> {}", column.getName(), formatter.format(DateSerializer.get().fromBytes(column.getValue())));
            } else {
               LOGGER.info("{} -> {}", column.getName(), new String(column.getValue()));
            }
         }
         
         line = in.readLine();
      }
      
      in.close();
      
      /*Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      RangeSlicesQuery<byte[], String, byte[]> query = HFactory.createRangeSlicesQuery(
            keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(),
            BytesArraySerializer.get());
      query.setKeys(null, null);
      query.setColumnFamily("SystemEventLog");
      query.setColumnNames("eventUUID", "archiveUUID", "eventDate", "eventDescription");
      query.setRowCount(25);
      
      QueryResult<OrderedRows<byte[], String, byte[]>> result = query.execute();
      if (!result.get().getList().isEmpty()) {
         for (Row<byte[], String, byte[]> row : result.get().getList()) {
            if (!row.getColumnSlice().getColumns().isEmpty()) {
               //LOGGER.info("row : {}", getReadableUTF8String(row.getKey()));
               LOGGER.info("row : {}", new String(row.getKey(), "UTF-8"));
            }
            for (HColumn<String, byte[]> colonne : row.getColumnSlice().getColumns()) {
               if (colonne.getName().equals("eventUUID")
                     || colonne.getName().equals("archiveUUID")) {
                  LOGGER.info("{} -> {}", colonne.getName(), UUIDSerializer.get().fromBytes(colonne.getValue()).toString());
               } else if (colonne.getName().equals("eventDate")) {
                  LOGGER.info("{} -> {}", colonne.getName(), formatter.format(DateSerializer.get().fromBytes(colonne.getValue())));
               } else {
                  LOGGER.info("{} -> {}", colonne.getName(), new String(colonne.getValue()));
               }
            }
         }
      }*/
   }
   
   @Test
   @Ignore
   public void removeSystemEventFromCassandra() throws Exception {
      String uuid = "98d98f1c-dea3-400d-a1ef-ee3f6b9fd0ad";
      String dateEvent = "20141230";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      long compteur = 0;
      
      String archiveUuid="archiveUUID=[" + uuid + "]";
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, Composite, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, StringSerializer.get(), CompositeSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("SystemEventLogByTimeSerialized");

      AllColumnsIterator<Composite, byte[]> it = new AllColumnsIterator<Composite, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<Composite, byte[]> column = it.next();
         
         // nom de colonne (compositeSerializer) : date + event uuid
         // valeur (objectSerializer) : map (archiveUUID, eventDate, eventDescription, eventUUID, eventStatus, username, attributes)
         Date date = DateSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(0));
         UUID idEvent = UUIDSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(1));
         Object valeur = ObjectSerializer.get().fromByteBuffer((ByteBuffer) column.getValueBytes());
         
         if (valeur.toString().indexOf(archiveUuid) != -1) {
            LOGGER.info("Suppression de l'evenement {} du {}", idEvent.toString(), formatter.format(date));
            
            Mutator mutator = HFactory.createMutator(keyspaceDocubase, StringSerializer.get());
            mutator.delete(dateEvent, "SystemEventLogByTimeSerialized", column.getName(), CompositeSerializer.get());
            
            compteur++;
         }
      }
      
      LOGGER.info("nb events : {}", compteur);
   }
   
   @Test
   @Ignore
   public void removeDocEventByIdEventFromCassandra() {
      String uuid = "ee1fbb93-1254-4dbd-b0e3-562790c26ac3";
      String dateEvent = "20140407";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, Composite, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, StringSerializer.get(), CompositeSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("DocEventLogByTimeSerialized");

      AllColumnsIterator<Composite, byte[]> it = new AllColumnsIterator<Composite, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<Composite, byte[]> column = it.next();
         
         // nom de colonne (compositeSerializer) : date + event uuid
         // valeur (objectSerializer) : map (archiveUUID, eventDate, eventDescription, eventUUID, eventStatus, username, attributes)
         Date date = DateSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(0));
         UUID idEvent = UUIDSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(1));
         
         //if (idEvent.toString().equals(uuid)) {
            LOGGER.info("Suppression de l'evenement {} du {}", idEvent.toString(), formatter.format(date));
            
            Mutator mutator = HFactory.createMutator(keyspaceDocubase, StringSerializer.get());
            mutator.delete(dateEvent, "DocEventLogByTimeSerialized", column.getName(), CompositeSerializer.get());
            //break;
         //}
      }
   }
   
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   private static String bytesToHex(byte[] bytes) {
      final char[] hexArray = "0123456789ABCDEF".toCharArray();
      char[] hexChars = new char[bytes.length * 2];
      for (int j = 0; j < bytes.length; j++) {
         int v = bytes[j] & 0xFF;
         hexChars[j * 2] = hexArray[v >>> 4];
         hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
   }
   
   private static byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
         data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
               .digit(s.charAt(i + 1), 16));
      }
      return data;
   }
   
   private static String getReadableUTF8String(byte[] bytes) throws Exception {
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
   
   private static String getHexString(byte b) throws Exception {
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
   private static String getHexString(byte[] bytes) throws Exception {
      String result = "";
      for (int i = 0; i < bytes.length; i++) {
         result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
      }
      return result;
   }
   
   /*@Test
   public void createSystemEventArchiveTest() {
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      Date lastRun = serviceProvider.getArchiveService().getLastSucessfulSystemLogsArchiveRunDate();
      Date endDate = new DateTime().withDate(2015, 5, 6).withTime(2, 30, 5, 0).toDate();
      
      try {
         DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
         String sLastRun = dateFormat.format(lastRun);
         String sEndDay = dateFormat.format(endDate);
         String jobParams = String.format("last.run.date(date)=%s, type=SYSTEM, endDate(date)=%s",
               sLastRun, sEndDay);
         
         Long idJob = serviceProvider.getJobAdministrationService().start(
               "systemLogsArchiveJob", jobParams);
         
         //jobAdministrationService.startNextInstance("systemLogsArchiveJob");

      } catch (JobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      } catch (UnexpectedJobExecutionException e) {
         LOGGER.error("Erreur : {}", e.getMessage());
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }*/   
   
   @Test
   public void getDocEventByIdEventFromCassandra2() {
      String uuid = "15bfdb10-6e18-4d1e-9b24-ee648052d5e2";
      String dateEvent = "20160526";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            "hwi31ginsaecas1.cer31.recouv:9160,hwi31ginsaecas2.cer31.recouv:9160,hwi31ginsaecas3.cer31.recouv:9160");
      /*CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            "cnp31gingntcas1.cer31.recouv:9160,cnp31gingntcas2.cer31.recouv:9160,cnp31gingntcas3.cer31.recouv:9160");*/
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      Keyspace keyspaceDocubase = HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
      
      SliceQuery<String, Composite, byte[]> query = HFactory.createSliceQuery(
            keyspaceDocubase, StringSerializer.get(), CompositeSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("DocEventLogByTimeSerialized");

      AllColumnsIterator<Composite, byte[]> it = new AllColumnsIterator<Composite, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<Composite, byte[]> column = it.next();
         
         // nom de colonne (compositeSerializer) : date + event uuid
         // valeur (objectSerializer) : map (archiveUUID, eventDate, eventDescription, eventUUID, eventStatus, username, attributes)
         Date date = DateSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(0));
         UUID idEvent = UUIDSerializer.get().fromByteBuffer((ByteBuffer) column.getName().get(1));
         
         //if (idEvent.toString().equals(uuid)) {
            
            Object valeur = ObjectSerializer.get().fromByteBuffer((ByteBuffer) column.getValueBytes());
            
            Map<String, List> valeurs = (HashMap<String, List>) valeur;
            //valeurs.get("docUUID").
            
            if (valeurs.get("docUUID").get(0).equals(uuid)) {
               
               LOGGER.info("Column Name {}:{} -> {}", new String[] {formatter.format(date), idEvent.toString(), valeurs.get("eventType").get(0).toString() });
            }
         //}
      }
   }
   
   @Test
   //@Ignore
   public void purgeRegTechFromCassandra() {
      String dateEvent = "20151029";
      boolean dryRun = true;
      
      if (dryRun) {
         LOGGER.info("Mode dry run activé");
      } else {
         LOGGER.info("Mode réel activé");
      }
      
      LOGGER.info("Recherche des traces du registre de surveillance technique pour le : {}", dateEvent);
      Keyspace keyspaceSAE = getKeyspaceSAEFromKeyspace();
      SliceQuery<String, UUID, byte[]> query = HFactory.createSliceQuery(
            keyspaceSAE, StringSerializer.get(), UUIDSerializer.get(),
            BytesArraySerializer.get());
      query.setKey(dateEvent);
      query.setColumnFamily("TraceRegTechniqueIndex");

      long compteur=0;
      AllColumnsIterator<UUID, byte[]> it = new AllColumnsIterator<UUID, byte[]>(
            query);
      while (it.hasNext()) {
         HColumn<UUID, byte[]> column = it.next();
         
         if (!dryRun) {
         
            Mutator<UUID> trace = HFactory.createMutator(keyspaceSAE, UUIDSerializer.get());
            trace.addDeletion(column.getName(), "TraceRegTechnique");
            trace.execute();
            
            Mutator<String> traceIndex = HFactory.createMutator(keyspaceSAE, StringSerializer.get());
            traceIndex.addDeletion(dateEvent, "TraceRegTechniqueIndex", column.getName(), UUIDSerializer.get());
            traceIndex.execute();
         }
         
         compteur++;
         if (compteur % 1000 == 0) {
            LOGGER.info("En cours : {} documents", compteur);
            //break;
         }
      }
      LOGGER.info("{} documents traités", compteur);
   }
}
