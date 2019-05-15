/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.dao.RangeIndexEntity;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class IndexReferenceUpdater {
   Cluster cluster;

   Session session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
      // servers = "cnp69saecas4.cer69.recouv, cnp69saecas5.cer69.recouv, cnp69saecas6.cer69.recouv";
      servers = "cnp69gntcas1,cnp69gntcas2,cnp69gntcas3";
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cnp69pregntcas1, cnp69pregntcas2";
      // servers = "cnp69givngntcas1, cnp69givngntcas2";
      // servers = "hwi69gincleasaecas1.cer69.recouv,hwi69gincleasaecas2.cer69.recouv";
      // servers = "cnp69pprodsaecas1,cnp69pprodsaecas2,cnp69pprodsaecas3"; //Préprod
      // servers = "cnp69pprodsaecas6"; //Préprod
      // servers = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv"; // Vrai préprod
      // servers = "10.213.82.56";
      // servers = "cnp6gnscvecas01.cve.recouv,cnp3gnscvecas01.cve.recouv,cnp7gnscvecas01.cve.recouv"; // Charge
      // servers = "cnp3gntcvecas1.cve.recouv,cnp6gntcvecas1.cve.recouv,cnp7gntcvecas1.cve.recouv"; // Charge GNT
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cer69imageint9.cer69.recouv";
      // servers = "cer69imageint10.cer69.recouv";
      // servers = "10.207.81.29";
      // servers = "hwi69givnsaecas1.cer69.recouv,hwi69givnsaecas2.cer69.recouv";
      // servers = "hwi69devsaecas1.cer69.recouv,hwi69devsaecas2.cer69.recouv";
      // servers = "hwi69ginsaecas2.cer69.recouv";
      // servers = "cer69-saeint3";
      // servers = "cnp69devgntcas1.gidn.recouv, cnp69devgntcas2.gidn.recouv";
      // servers = "cnp69dev2gntcas1.gidn.recouv, cnp69dev2gntcas2.gidn.recouv";
      // servers = "cnp69miggntcas1.gidn.recouv,cnp69miggntcas2.gidn.recouv"; // Migration cassandra V2
      // servers = "cnp69dev2gntcas1.gidn.recouv";
      // servers = "cnp69devgntcas1.gidn.recouv,cnp69devgntcas2.gidn.recouv";
      // servers = "hwi69intgnscas1.gidn.recouv,hwi69intgnscas2.gidn.recouv";

      cluster = Cluster.builder()
                       .withClusterName("myCluster")
                       .addContactPoints(StringUtils.split(servers, ","))
                       .withoutJMXReporting()
                       .withAuthProvider(new PlainTextAuthProvider("root", "regina4932"))
                       .build();
      session = cluster.connect();

      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
      dumper = new Dumper(sysout);
   }

   @After
   public void close() throws Exception {
      cluster.close();
   }

   @Test
   public void countInRange() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      // final String index = "SM_LIFE_CYCLE_REFERENCE_DATE";
      final String index = "drh&cop&npa&SM_CREATION_DATE&";
      // final String index = "den";
      // final int rangeId = 6;
      final int rangeId = 0;
      final BuiltStatement query = QueryBuilder.select("metadata_value")
                                               // .from("dfce", "term_info_range_datetime")
                                               .from("dfce", "term_info_range_string")
                                               .where(QueryBuilder.eq("index_code", ""))
                                               .and(QueryBuilder.eq("base_uuid", baseId))
                                               .and(QueryBuilder.eq("metadata_name", index))
                                               .and(QueryBuilder.eq("range_index_id", rangeId));

      final ResultSet rs = session.execute(query);
      int totalCounter = 0;
      int distinctCounter = 0;
      String currentValue = "";
      for (final Row row : rs) {
         final String value = row.getString(0);
         if (!value.equals(currentValue)) {
            distinctCounter++;
            currentValue = value;
         }
         totalCounter++;
         if (totalCounter % 50000 == 0) {
            System.out.println("totalCounter=" + totalCounter);
            System.out.println("distinctCounter=" + distinctCounter);
         }
      }
      System.out.println("totalCounter=" + totalCounter);
      System.out.println("distinctCounter=" + distinctCounter);
      // Pour mettre à jour
      // UpdateOneRangeCount(baseId, index, rangeId, totalCounter);
   }

   @Test
   public void countPajeDocuments() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String index = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      final String minValue = "true&cm420&1.2.2.4.12&1970".replace('&', (char) (0));
      final String maxValue = "true&cm420&1.2.2.4.12&2999".replace('&', (char) (0));
      final int[] rangeIds = new int[] {110, 111};
      int totalCounter = 0;
      int distinctCounter = 0;
      for (final int rangeId : rangeIds) {
         final BuiltStatement query = QueryBuilder.select("metadata_value")
                                                  .from("dfce", "term_info_range_string")
                                                  .where(QueryBuilder.eq("index_code", ""))
                                                  .and(QueryBuilder.eq("base_uuid", baseId))
                                                  .and(QueryBuilder.eq("metadata_name", index))
                                                  .and(QueryBuilder.eq("range_index_id", rangeId))
                                                  .and(QueryBuilder.gt("metadata_value", minValue))
                                                  .and(QueryBuilder.lt("metadata_value", maxValue));

         final ResultSet rs = session.execute(query);
         String currentValue = "";
         for (final Row row : rs) {
            final String value = row.getString(0);
            if (!value.equals(currentValue)) {
               distinctCounter++;
               currentValue = value;
            }
            totalCounter++;
            if (totalCounter % 50000 == 0) {
               System.out.println("value=" + value);
               // System.out.println("value2=" + ConvertHelper.getReadableUTF8String(value.getBytes()));
               System.out.println("totalCounter=" + totalCounter);
               System.out.println("distinctCounter=" + distinctCounter);
            }
         }
      }
      System.out.println("totalCounter=" + totalCounter);
      System.out.println("distinctCounter=" + distinctCounter);
   }

   @Test
   public void countAndUpdateRanges() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String index = "SM_MODIFICATION_DATE";
      final int rangeIdStart = 131;
      final int rangeIdEnd = 187;

      for (int rangeId = rangeIdStart; rangeId <= rangeIdEnd; rangeId++) {

         final BuiltStatement query = QueryBuilder.select("metadata_value")
                                                  .from("dfce", "term_info_range_datetime")
                                                  // .from("dfce", "term_info_range_string")
                                                  .where(QueryBuilder.eq("index_code", ""))
                                                  .and(QueryBuilder.eq("base_uuid", baseId))
                                                  .and(QueryBuilder.eq("metadata_name", index))
                                                  .and(QueryBuilder.eq("range_index_id", rangeId));

         final ResultSet rs = session.execute(query);
         int totalCounter = 0;
         int distinctCounter = 0;
         String currentValue = "";
         for (final Row row : rs) {
            final String value = row.getString(0);
            if (!value.equals(currentValue)) {
               distinctCounter++;
               currentValue = value;
            }
            totalCounter++;
            if (totalCounter % 50000 == 0) {
               System.out.println("totalCounter=" + totalCounter);
               System.out.println("distinctCounter=" + distinctCounter);
            }
         }
         System.out.println("totalCounter=" + totalCounter);
         System.out.println("distinctCounter=" + distinctCounter);
         if (totalCounter > 0) {
            updateOneRangeCount(baseId, index, rangeId, totalCounter);
         }
      }
   }

   private RangeIndexEntity getRangeIndexEntity(final UUID baseId, final String index, final int rangeId) throws Exception {
      final BuiltStatement query = QueryBuilder.select()
                                               .from("dfce", "index_reference")
                                               .where(QueryBuilder.eq("index_name", index))
                                               .and(QueryBuilder.eq("base_id", baseId));
      final Row row = session.execute(query).one();
      final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
      final String rangeAsJson = ranges.get(rangeId);
      if (rangeAsJson == null) {
         return null;
      }
      final ObjectMapper jsonMapper = new ObjectMapper();
      final RangeIndexEntity rangeEntity = jsonMapper.readValue(rangeAsJson, RangeIndexEntity.class);
      return rangeEntity;
   }

   /**
    * Calcule la somme des COUNT des différents ranges
    * 
    * @param baseId
    * @param index
    * @return
    * @throws Exception
    */
   private int sumCountsInRanges(final UUID baseId, final String index) throws Exception {
      final BuiltStatement query = QueryBuilder.select()
                                               .from("dfce", "index_reference")
                                               .where(QueryBuilder.eq("index_name", index))
                                               .and(QueryBuilder.eq("base_id", baseId));
      final Row row = session.execute(query).one();
      final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
      final ObjectMapper jsonMapper = new ObjectMapper();
      int sum = 0;
      for (final Entry<Integer, String> rangeElement : ranges.entrySet()) {
         final String rangeAsJson = rangeElement.getValue();
         final RangeIndexEntity rangeEntity = jsonMapper.readValue(rangeAsJson, RangeIndexEntity.class);
         sum += rangeEntity.getCOUNT();
      }
      return sum;
   }

   @Test
   public void sumCountsInRangesTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String index = "SM_ARCHIVAGE_DATE";
      final int sum = sumCountsInRanges(baseId, index);
      System.out.println("sum=" + sum);
   }

   @Test
   public void updateOneRangeCountTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "SM_CREATION_DATE";
      // final String index = "SM_MODIFICATION_DATE";
      final String index = "SM_FINAL_DATE";
      final int rangeId = 0;
      final int countToSet = 0;

      updateOneRangeCount(baseId, index, rangeId, countToSet);
   }

   /**
    * @param baseId
    * @param index
    * @param rangeId
    * @param countToSet
    * @throws Exception
    * @throws IOException
    * @throws JsonGenerationException
    * @throws JsonMappingException
    */
   private void updateOneRangeCount(final UUID baseId, final String index, final int rangeId, final int countToSet)
         throws Exception, IOException, JsonGenerationException, JsonMappingException {
      final RangeIndexEntity rangeEntity = getRangeIndexEntity(baseId, index, rangeId);
      if (rangeEntity == null) {
         System.out.println("entity non trouvé pour rangeId=" + rangeId);
         return;
      }
      rangeEntity.setCOUNT(countToSet);
      final ObjectMapper jsonMapper = new ObjectMapper();
      final String json = jsonMapper.writeValueAsString(rangeEntity);
      System.out.println("json=" + json);

      final BuiltStatement query = QueryBuilder.update("dfce", "index_reference")
                                               .with(QueryBuilder.put("index_ranges", rangeId, json))
                                               .where(QueryBuilder.eq("index_name", index))
                                               .and(QueryBuilder.eq("base_id", baseId))
                                               .ifExists();
      final ResultSet result = session.execute(query);
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   // @Ignore
   public void updateDistinctUseCountTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "den";
      final String index = "SM_MODIFICATION_DATE";
      final int distinctUseCount = 178937250;

      final BuiltStatement query = QueryBuilder.update("dfce", "index_reference")
                                               .with(QueryBuilder.set("distinct_use_count", distinctUseCount))
                                               .where(QueryBuilder.eq("index_name", index))
                                               .and(QueryBuilder.eq("base_id", baseId))
                                               .ifExists();
      final ResultSet result = session.execute(query);
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   // @Ignore
   public void updateTotalUseCountTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "den";
      final String index = "SM_MODIFICATION_DATE";
      final int totalUseCount = 178937250;

      final BuiltStatement query = QueryBuilder.update("dfce", "index_reference")
                                               .with(QueryBuilder.set("total_use_count", totalUseCount))
                                               .where(QueryBuilder.eq("index_name", index))
                                               .and(QueryBuilder.eq("base_id", baseId))
                                               .ifExists();
      final ResultSet result = session.execute(query);
      System.out.println("wasApplied=" + result.wasApplied());
   }

}
