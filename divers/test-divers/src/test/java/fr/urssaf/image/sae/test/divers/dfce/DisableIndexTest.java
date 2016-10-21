package fr.urssaf.image.sae.test.divers.dfce;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
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
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class DisableIndexTest {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DisableIndexTest.class);

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
    * Desactive l'index sur le NumeroMatriculeAgent et le NomPatronymiqueAgent 
    */
   @Test
   //@Ignore("L'index n'est plus créé par le script de création des metas")
   public void disableIndexNmaEtNpa() throws Exception {
      
      boolean dryRun = true;

      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);
      
      String[] metasCodeLong = { "NumeroMatriculeAgent", "NomPatronymiqueAgent" };
      String[] metasCodeCourt = { "nma", "npa" };
      
      // ETAPE 1 : Update coté SAE
      for (String codeLong : metasCodeLong) {
         // modification du flag indexé de la meta
         LOGGER.debug("Recuperation de la metadata SAE : {}", codeLong);
         SliceQuery<String,String,byte[]> querySAE = HFactory.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
         querySAE.setColumnFamily("Metadata").setKey(codeLong).setRange(null, null, false, 100);
         QueryResult<ColumnSlice<String,byte[]>> resultSAE = querySAE.execute();
         if (resultSAE != null && resultSAE.get() != null) {
            LOGGER.debug("Metadata trouvee : {}", codeLong);
            
            // recupere le flag indexe
            HColumn<String, byte[]> indexee = resultSAE.get().getColumnByName("index");
            LOGGER.debug("Meta indexee : {}", getHexString(indexee.getValue()));
            
            if (!dryRun) {
               LOGGER.debug("Modification de la metadata : {}", codeLong);
               Mutator<String> mutator = HFactory.createMutator(keyspace,
                     StringSerializer.get());
               mutator.addInsertion(codeLong, "Metadata", 
                     HFactory.createColumn(indexee.getName(),
                        new byte[] { 0 }, StringSerializer.get(), BytesArraySerializer.get()));
               mutator.execute();
            } else {
               LOGGER.debug("Mode dry run : On devrait modifier le flag indexee de la metadata : {}", codeLong);
            }
         }
      }
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // ETAPE 2 : Update coté référentiel des categories DFCE
      for (String codeCourt : metasCodeCourt) {
         // modification du flag indexé de la meta
         RangeSlicesQuery<byte[],String,byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
         rangeQueryDocubase.setColumnFamily("BaseCategoriesReference").setKeys(null, null);
         rangeQueryDocubase.setColumnNames("indexed");
         QueryResult<OrderedRows<byte[],String, byte[]>> rangeResultDocubase = rangeQueryDocubase.execute();
         if (rangeResultDocubase != null && rangeResultDocubase.get() != null) {
            Iterator<Row<byte[], String, byte[]>> iterateur = rangeResultDocubase.get().iterator();
            while (iterateur.hasNext()) {
               Row<byte[], String, byte[]> row = iterateur.next();
               String rowKey = new String(row.getKey()); 
               if (rowKey.endsWith(codeCourt)) {
                  LOGGER.debug("Lien Base / Categorie trouvee : {}", rowKey);
                  
                  // recupere le flag indexe
                  HColumn<String, byte[]> indexee = row.getColumnSlice().getColumnByName("indexed");
                  LOGGER.debug("Categorie indexee : {}", getHexString(indexee.getValue()));

                  if (!dryRun) {
                     LOGGER.debug("Modification du flag index de la base / categorie : {}", rowKey);
                     Mutator<byte[]> mutator = HFactory.createMutator(keyspaceDocubase,
                           BytesArraySerializer.get());
                     mutator.addInsertion(row.getKey(), "BaseCategoriesReference", 
                           HFactory.createColumn(indexee.getName(),
                              new byte[] { 0 }, StringSerializer.get(), BytesArraySerializer.get()));
                     mutator.execute();
                  } else {
                     LOGGER.debug("Mode dry run : On devrait modifier le flag indexee de la base / categorie : {}", rowKey);
                  }
               }
            }
         }
      }
      
      // ETAPE 3 : Suppression de l'indexation des documents dans DFCE
      for (String codeCourt : metasCodeCourt) {
         
         String cfName = "TermInfoRangeString";

         RangeSlicesQuery<byte[], String, String> query = HFactory
               .createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer
                     .get(), StringSerializer.get(), StringSerializer.get());
         query.setColumnFamily(cfName).setKeys(null, null).setReturnKeysOnly();
         query.setRowCount(5000);
         QueryResult<OrderedRows<byte[], String, String>> result = query.execute();
         for (Row<byte[], String, String> row : result.get().getList()) {
            String nomCle = getReadableUTF8String(row.getKey());
            if (nomCle.startsWith("\\x00\\x00\\x00\\x00\\x03" + codeCourt)) {
               if (!dryRun) {
                  LOGGER.info("Suppression de l'indexation : {}", nomCle);
                  Mutator<byte[]> mutator = HFactory.createMutator(
                        keyspaceDocubase, BytesArraySerializer.get());
                  mutator.addDeletion(row.getKey(), cfName);
                  mutator.execute();
               } else {
                  LOGGER.debug(
                        "Mode dry run : On devrait supprimer l'indexation : {}",
                        nomCle);
               }
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
   
   public static String getReadableUTF8String(byte[] bytes) throws Exception {
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
   
   public static String getHexString(byte b) throws Exception {
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
   public static String getHexString(byte[] bytes) throws Exception {
      String result = "";
      for (int i = 0; i < bytes.length; i++) {
         result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
      }
      return result;
   }
   
   public String toHex(String arg) {
      return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
}
