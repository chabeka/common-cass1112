package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.base.Stopwatch;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.NotFoundException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.TimeUUIDUtils;

import fr.urssaf.astyanaxtest.dao.sae.TraceRegTechniqueCF;
import fr.urssaf.astyanaxtest.dao.sae.TraceRegTechniqueIndexCF;

public class IterateTraces {

   /**
    * Représente le keyspace cassandra sur lequel on travaille
    */
   Keyspace keyspace;

   /**
    * Facilite le mapping cassandra<->entité
    */
   MappingUtil mapper;

   /**
    * La où on veut dumper
    */
   PrintStream sysout;

   @Before
   public void init() throws Exception {
      String servers;
      // servers =
      // "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // //GIVN
      //servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      servers = "cnp69gntcas1.cer69.recouv:9160, cnp69gntcas2.cer69.recouv:9160, cnp69progednatgntcot1bocas3.cer69.recouv:9160";
      // // Production
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers =
      // "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      //servers = "cer69-saeint3:9160";
      //servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
      // servers = "cnp6gnscvecas01.cve.recouv:9160,cnp3gnscvecas01.cve.recouv:9160,cnp7gnscvecas01.cve.recouv:9160"; // Charge
      // servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160"; // GNT Intégration client

      final AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root", "regina4932");

      final AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster("SAE")
            .forKeyspace("SAE")
            .withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl()
                  .setDiscoveryType(NodeDiscoveryType.NONE)
                  .setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_ONE)
                  .setDefaultWriteConsistencyLevel(
                        ConsistencyLevel.CL_QUORUM))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                  .setPort(9160)
                  .setMaxConnsPerHost(2)
                  .setSeeds(servers)
                  .setAuthenticationCredentials(credentials))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      keyspace = context.getEntity();
      mapper = new MappingUtil(keyspace, new MappingCache());

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("c:/temp/out.txt");

   }

   @Test
   /**
    * Exemple d'itération sur les traces
    */
   public void testIterateOverTraces() throws Exception {
      final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      final Date start = sdf.parse("2020-04-21 13:50:41");
      final Date end = sdf.parse("2035-09-27 12:59:59");
      final String key = "20200423";
      //
      //20180624
      //20180511
      //20180422

      final int blocSize = 100; // Nombre de colonnes qu'on ramène à la fois de
      // cassandra
      final int hardLimitForTest = 100; // On arrête la boucle une fois ce nombre de

      final RowQuery<String, UUID> query = keyspace
            .prepareQuery(TraceRegTechniqueIndexCF.cf)
            .getKey(key)
            .autoPaginate(true)
            .withColumnRange(TimeUUIDUtils.getTimeUUID(start.getTime() * 100), TimeUUIDUtils.getTimeUUID(end.getTime() * 100), false, blocSize);

      ColumnList<UUID> columns;
      int lineCounter = 0;
      int timeoutCounter = 0;
      boolean shouldStop = false;
      final int[] timeoutsByHour = new int[24];

      final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      final XPath xPath = XPathFactory.newInstance().newXPath();
      final String expression = "/map/entry";
      final XPathExpression xPathExpression = xPath.compile(expression);

      final Stopwatch chrono = new Stopwatch();
      chrono.start();
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (final Column<UUID> c : columns) {
            final UUID traceId = c.getName();
            // sysout.println(traceId);

            try {
               final Column<String> result = keyspace.prepareQuery(TraceRegTechniqueCF.cf)
                     .getKey(traceId)
                     .getColumn("stacktrace")
                     .execute()
                     .getResult();
               final String stackTrace = result.getStringValue();
               // sysout.println(stackTrace);
               if (stackTrace.contains("Cassandra timeout") || stackTrace.contains("ReadTimeout")
                     || stackTrace.contains("WriteTimeout")) {
                  final long timestamp = c.getTimestamp();
                  final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp / 1000),
                        TimeZone.getDefault().toZoneId());
                  final int hour = date.getHour();
                  // Récupération d'infos supplémentaires
                  final ColumnList<String> result2 = keyspace.prepareQuery(TraceRegTechniqueCF.cf)
                        .getKey(traceId)
                        .withColumnSlice("contexte", "cs", "infos")
                        .execute()
                        .getResult();
                  final String context = result2.getStringValue("contexte", "");
                  final String cs = result2.getStringValue("cs", "");
                  final String infos = result2.getStringValue("infos", "");
                  final String server = extractServer(infos, dBuilder, xPathExpression);
                  sysout.println("traceId=" + traceId);
                  sysout.println("date=" + date);
                  sysout.println("hour=" + hour);
                  sysout.println("context=" + context);
                  sysout.println("cs=" + cs);
                  sysout.println("server=" + server);
                  timeoutCounter++;
                  timeoutsByHour[hour]++;
               }
            }
            catch (final NotFoundException e) {
            }
            lineCounter++;
            if (lineCounter % 100 == 0) {
               final long timestamp = c.getTimestamp();
               final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp / 1000),
                     TimeZone.getDefault().toZoneId());
               System.out.println(lineCounter + " - " + timeoutCounter + " - " + date);
            }
            if (lineCounter >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());
      System.out.println(lineCounter + " traces parcourues");
      System.out.println("Nombre de timeouts : " + timeoutCounter);
      System.out.println("Timeouts par tranche horaire :");
      for (int hour = 0; hour < 24; hour++) {
         System.out.println(hour + "h -> " + (hour + 1) + "h : " + timeoutsByHour[hour]);
      }
   }

   private String extractServer(final String xmlString, final DocumentBuilder dBuilder, final XPathExpression xPathExpression) {
      try {
         final Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
         final NodeList nodeList = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
         for (int i = 0; i < nodeList.getLength(); i++) {
            final Node entryNode = nodeList.item(i);
            final NodeList childNodes = entryNode.getChildNodes();
            final String name = childNodes.item(0).getTextContent();
            final String value = childNodes.item(1).getTextContent();
            if ("saeServeurHostname".equals(name)) {
               return value;
            }
         }
         return "";
      }
      catch (final Exception e) {
         throw new RuntimeException(e);
      }
   }
}
