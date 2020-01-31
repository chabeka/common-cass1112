package fr.urssaf.astyanaxtest;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.RowCallback;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Equality;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.AllRowsQuery;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.TermInfoRangeKey;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeCF;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeColumn;

public class IterateEvents {
   /**
    * Représente le keyspace cassandra sur lequel on travaille
    */
   Keyspace keyspace;
   
   /**
    * Facilite le mapping cassandra<->entité
    */
   MappingUtil mapper;
   
   PrintStream sysout;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";     //GIVN
      //servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160"; // Production
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3:9160";
      servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160";     //Préprod

      AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root", "regina4932");

      AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster("Docubase").forKeyspace("Docubase")
            .withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl().setDiscoveryType(
                        NodeDiscoveryType.NONE).setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_ONE)
                        .setDefaultWriteConsistencyLevel(
                              ConsistencyLevel.CL_QUORUM))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(9160).setMaxConnsPerHost(1).setSeeds(servers)
                        .setAuthenticationCredentials(credentials))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      keyspace = context.getEntity();
      mapper = new MappingUtil(keyspace, new MappingCache());

      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("c:/temp/out.txt");
   }

   
   @Test
   /**
    * Exemple d'itération 
    */
   public void testIterate() throws Exception {
      ColumnFamily<String, String> CF_DocEventLog =
         new ColumnFamily<String, String>(
           "DocEventLog",              // Column Family Name
           StringSerializer.get(),   // Key Serializer
           StringSerializer.get());  // Column Serializer
      
      AllRowsQuery<String, String> query = keyspace
            .prepareQuery(CF_DocEventLog)
            .getAllRows()
            .setRowLimit(100)  // This is the page size
            .setRepeatLastToken(false);
      /*
            .executeWithCallback(new RowCallback<String, String>() {
            @Override
            public boolean failure(ConnectionException e) {
               // TODO Auto-generated method stub
               e.printStackTrace(sysout);
               return false;
            }
            @Override
            public void success(Rows<String, String> rows) {
               int compteur = 0;
               for (Row<String, String> row : rows) {
                  sysout.println("Key : " + row.getKey());
                  ColumnList<String> cols = row.getColumns();
                  for (Column<String> column : cols) {
                     String hexValue = getHexString(column.getByteArrayValue());
                     String sValue = column.getStringValue();
                     String name = column.getName();
                     if (name.equals("digest") || name.equals("eventTypeENUM_ATTRIBUTE_VALUE_SUFFIXE")) {
                        // String
                        sysout.println("Name : " + column.getName() + " - StringValue : " + sValue);
                     }
                     else if (name.equals("docEndDate")|| name.equals("eventDate")|| name.equals("xxx")|| name.equals("xxx")|| name.equals("xxx")) {
                        // Date
                        sysout.println("Name : " + column.getName() + " - Value : " + column.getDateValue());
                     }
                     else if (name.equals("docUUID")|| name.equals("attributes.size")|| name.equals("xxx")|| name.equals("xxx")) {
                        // Hex
                        sysout.println("Name : " + column.getName() + " - Value : " + hexValue);
                     }
                     else if (name.equals("TYPE_ATTRIBUTE_NAME")|| name.equals("digestAlgorithm")|| name.equals("docVersion")|| name.equals("eventTypeENUM_ATTRIBUTE_TYPE_SUFFIXE")|| name.equals("eventUUID")|| name.equals("username")) {
                        // Ignore
                     }
                     else {
                        sysout.println("Name : " + column.getName() + " - StringValue : " + sValue + " - hexValue = " + hexValue);
                     }
                     
                  }
                  sysout.println();
                  compteur ++;
                  if (compteur > 200) break;
               }
               
            }
          });
            */
      Rows<String, String> rows;
      int compteurLigne = 0;
      rows = query.execute().getResult();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      for (Row<String, String> row : rows) {
         sysout.println("Key : " + row.getKey());
         ColumnList<String> cols = row.getColumns();
         for (Column<String> column : cols) {
            String hexValue = getHexString(column.getByteArrayValue());
            String sValue = column.getStringValue();
            String name = column.getName();
            if (name.equals("digest") || name.equals("eventTypeENUM_ATTRIBUTE_VALUE_SUFFIXE")) {
               // String
               sysout.println(name + " : " + sValue);
            }
            else if (name.equals("docEndDate")|| name.equals("eventDate")|| name.equals("xxx")|| name.equals("xxx")|| name.equals("xxx")) {
               // Date
               sysout.println(name + " : " + dateFormat.format(column.getDateValue()));
            }
            else if (name.equals("docUUID")|| name.equals("attributes.size")|| name.equals("archiveUUID")|| name.equals("xxx")) {
               // Hex
               sysout.println(name + " : " + hexValue);
            }
            else if (name.equals("TYPE_ATTRIBUTE_NAME")|| name.equals("digestAlgorithm")|| name.equals("docVersion")|| name.equals("eventTypeENUM_ATTRIBUTE_TYPE_SUFFIXE")|| name.equals("eventUUID")|| name.equals("username")) {
               // Ignore
            }
            else {
               sysout.println("Name : " + column.getName() + " - StringValue : " + sValue + " - hexValue = " + hexValue);
            }
            
         }
         sysout.println();
         compteurLigne ++;
         if (compteurLigne >= 200000000) {
            break;
         }
      }
      
      
   }
   
   /**
    * Renvoie la représentation hexadécimale d'un tableau de bytes
    * @param bytes tableau de bytes
    * @return
    * @throws Exception
    */
   public static String getHexString(byte[] bytes) {
        String result = "";
        for (int i=0; i < bytes.length; i++) {
          result +=
                Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
   }


}
