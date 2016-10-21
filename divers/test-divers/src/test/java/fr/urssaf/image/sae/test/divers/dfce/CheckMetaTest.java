package fr.urssaf.image.sae.test.divers.dfce;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class CheckMetaTest {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CheckMetaTest.class);

   /**
    * Injecte le keyspace SAE.
    */
   @Autowired
   private Keyspace keyspace;

   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Test
   public void checkMeta() throws Exception {
      
      Map<String, String> metadatas = new HashMap<String, String>();
      String nomBase = "SAE-INT";
      
      // recupere les metadata
      RangeSlicesQuery<byte[],String,byte[]> rangeQuery = HFactory.createRangeSlicesQuery(keyspace, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      rangeQuery.setColumnFamily("Metadata").setKeys(null, null);
      rangeQuery.setColumnNames("sCode");
      rangeQuery.setRowCount(500);
      QueryResult<OrderedRows<byte[],String,byte[]>> result = rangeQuery.execute();
      
      if (result != null && result.get() != null) {
         Iterator<Row<byte[], String, byte[]>> iterateur = result.get().iterator();
         while (iterateur.hasNext()) {
            Row<byte[], String, byte[]> row = iterateur.next();
            HColumn<String, byte[]> colCodeCourt = row.getColumnSlice().getColumnByName("sCode");
            if (colCodeCourt != null) {
               //LOGGER.info("{} : {}", getReadableUTF8String(row.getKey()), getReadableUTF8String(colCodeCourt.getValue()));
               metadatas.put(getReadableUTF8String(row.getKey()), getReadableUTF8String(colCodeCourt.getValue())); 
            }
         } 
      }
      
      LOGGER.info("Lecture de la cf Metadata : {} metadatas trouvees", metadatas.size());
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      LOGGER.debug("Recuperation de la base : {}", nomBase);
      Base base = serviceProvider.getBaseAdministrationService().getBase(nomBase);
      
      long nbMetaOublie = 0;
      Iterator<Entry<String, String>> iterateur = metadatas.entrySet().iterator();
      while(iterateur.hasNext()) {
         Entry<String, String> entry = iterateur.next();
         boolean ignore = false;
         if (entry.getValue().startsWith("SM_")) {
            // ignore les meta systemes
            ignore = true;
         } else if (entry.getValue().equals("gel") || entry.getValue().equals("dco") || entry.getValue().equals("nfi") || entry.getValue().equals("toa")) {
            // ignore les meta interne aux SAE qui font appel a des api DFCE
            ignore = true;
         }
         if (!ignore && base.getBaseCategory(entry.getValue()) == null) {
            LOGGER.info("Metadata {} non trouve dans Docubase (code court : {})", entry.getKey(), entry.getValue());
            nbMetaOublie++;
         } 
      }
      
      if (nbMetaOublie == 0) {
         LOGGER.info("Toutes les meta sont bien presente cote DFCE");
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
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
}
