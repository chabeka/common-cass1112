/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Assignment;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.dao.CassandraSessionFactory;
import fr.urssaf.javaDriverTest.dao.IndexReference;
import fr.urssaf.javaDriverTest.dao.IndexReferenceDAO;
import fr.urssaf.javaDriverTest.dao.RangeIndexEntity;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class IndexReferenceUpdater {

   CqlSession session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "cnp69imagedev.gidn.recouv";
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
      // servers = "cnp6gntcvecas1.cve.recouv,cnp6gntcvecas2.cve.recouv"; // Charge GNT
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

      // final String cassandraLocalDC = "DC6";
      final String cassandraLocalDC = "LYON_SP";
      // final String cassandraLocalDC = "DC1";
      session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(sysout);
   }

   @After
   public void close() throws Exception {
      session.close();
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
      final Select query = QueryBuilder.selectFrom("dfce", "term_info_range_string")
            .columns("metadata_value")
            .whereColumn("index_code")
            .isEqualTo(literal(""))
            .whereColumn("base_uuid")
            .isEqualTo(literal(baseId))
            .whereColumn("metadata_name")
            .isEqualTo(literal(index))
            .whereColumn("range_index_id")
            .isEqualTo(literal(rangeId));

      final ResultSet rs = session.execute(query.build());
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
      final String minValue = "true&cm420&1.2.2.4.12&1970".replace('&', (char) 0);
      final String maxValue = "true&cm420&1.2.2.4.12&2999".replace('&', (char) 0);
      final int[] rangeIds = new int[] {110, 111};
      int totalCounter = 0;
      int distinctCounter = 0;
      for (final int rangeId : rangeIds) {
         final Select query = QueryBuilder.selectFrom("dfce", "term_info_range_string")
               .columns("metadata_value")
               .whereColumn("index_code")
               .isEqualTo(literal(""))
               .whereColumn("base_uuid")
               .isEqualTo(literal(baseId))
               .whereColumn("metadata_name")
               .isEqualTo(literal(index))
               .whereColumn("range_index_id")
               .isEqualTo(literal(rangeId))
               .whereColumn("metadata_value")
               .isGreaterThan(literal(minValue))
               .whereColumn("metadata_value")
               .isLessThan(literal(maxValue));

         final ResultSet rs = session.execute(query.build());
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

         final Select query = QueryBuilder.selectFrom("dfce", "term_info_range_datetime")
               .columns("metadata_value")
               .whereColumn("index_code")
               .isEqualTo(literal(""))
               .whereColumn("base_uuid")
               .isEqualTo(literal(baseId))
               .whereColumn("metadata_name")
               .isEqualTo(literal(index))
               .whereColumn("range_index_id")
               .isEqualTo(literal(rangeId));

         final ResultSet rs = session.execute(query.build());
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
      final Select query = QueryBuilder.selectFrom("dfce", "index_reference")
            .all()
            .whereColumn("index_name")
            .isEqualTo(literal(index))
            .whereColumn("base_id")
            .isEqualTo(literal(baseId));
      final Row row = session.execute(query.build()).one();
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
      final Select query = QueryBuilder.selectFrom("dfce", "index_reference")
            .all()
            .whereColumn("index_name")
            .isEqualTo(literal(index))
            .whereColumn("base_id")
            .isEqualTo(literal(baseId));
      final Row row = session.execute(query.build()).one();
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
         throws Exception, IOException {
      final RangeIndexEntity rangeEntity = getRangeIndexEntity(baseId, index, rangeId);
      if (rangeEntity == null) {
         System.out.println("entity non trouvé pour rangeId=" + rangeId);
         return;
      }
      rangeEntity.setCOUNT(countToSet);
      final ObjectMapper jsonMapper = new ObjectMapper();
      final String json = jsonMapper.writeValueAsString(rangeEntity);
      System.out.println("json=" + json);

      final Update query = QueryBuilder.update("dfce", "index_reference")
            .set(Assignment.setMapValue("index_ranges", literal(rangeId), literal(json)))
            .whereColumn("index_name")
            .isEqualTo(literal(index))
            .whereColumn("base_id")
            .isEqualTo(literal(baseId))
            .ifExists();
      final ResultSet result = session.execute(query.build());
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   public void updateDistinctUseCountTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "den";
      final String index = "SM_MODIFICATION_DATE";
      final int distinctUseCount = 178937250;

      final Update query = QueryBuilder.update("dfce", "index_reference")
            .set(Assignment.setColumn("distinct_use_count", literal(distinctUseCount)))
            .whereColumn("index_name")
            .isEqualTo(literal(index))
            .whereColumn("base_id")
            .isEqualTo(literal(baseId))
            .ifExists();
      final ResultSet result = session.execute(query.build());
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   public void updateTotalUseCountTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "den";
      final String index = "SM_MODIFICATION_DATE";
      final int totalUseCount = 178937250;

      final Update query = QueryBuilder.update("dfce", "index_reference")
            .set(Assignment.setColumn("total_use_count", literal(totalUseCount)))
            .whereColumn("index_name")
            .isEqualTo(literal(index))
            .whereColumn("base_id")
            .isEqualTo(literal(baseId))
            .ifExists();
      final ResultSet result = session.execute(query.build());
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   /**
    * Exemple de procédure pour annuler un split qui ne s'est pas terminé
    */
   public void cancelSplitTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);

      final BatchStatementBuilder batchBuilder = BatchStatement.builder(DefaultBatchType.LOGGED);

      final String index = "SM_MODIFICATION_DATE";

      // On passe un range en "NOMINAL" au lieu de SPLITTING
      final int splitingRangeId = 0;
      final RangeIndexEntity rangeIndexEntity = IndexReferenceDAO.getRangeIndexEntity(session, baseId, index, splitingRangeId);
      rangeIndexEntity.setSTATE("NOMINAL");
      final ObjectMapper jsonMapper = new ObjectMapper();
      final String newJson = jsonMapper.writeValueAsString(rangeIndexEntity);
      final Update updateQuery = update("dfce", "index_reference")
            .set(Assignment.setMapValue("index_ranges", literal(splitingRangeId), literal(newJson)))
            .whereColumn("index_name")
            .isEqualTo(literal(index))
            .whereColumn("base_id")
            .isEqualTo(literal(baseId));
      batchBuilder.addStatement(updateQuery.build());

      // On supprime les ranges "BUILDING"
      final int[] rangesToDelete = new int[] {1, 2, 3, 4};
      for (final int rangeIdToDelete : rangesToDelete) {
         batchBuilder.addStatement(SimpleStatement.newInstance("DELETE index_ranges[?] FROM dfce.index_reference WHERE index_name=? AND base_id=?",
               rangeIdToDelete,
               index,
               baseId));
      }

      final BatchStatement batch = batchBuilder.build();
      final ResultSet result = session.execute(batch);
      System.out.println("Errors=" + result.getExecutionInfo().getErrors().toString());
   }

   @Test
   public void jsonTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String index = "cot&apr&atr&ame&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      final int rangeId = 0;
      final RangeIndexEntity rangeIndexEntity = IndexReferenceDAO.getRangeIndexEntity(session, baseId, index, rangeId);
      // rangeIndexEntity.setSTATE("NOMINAL");
      final ObjectMapper jsonMapper = new ObjectMapper();
      final String newJson = jsonMapper.writeValueAsString(rangeIndexEntity);
      System.out.println(newJson);
   }

   @Test
   public void deleteOneRangeTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);

      final BatchStatementBuilder batchBuilder = BatchStatement.builder(DefaultBatchType.LOGGED);

      final String index = "cot&cop&djc&";
      final int rangeToDelete = 4;
      batchBuilder.addStatement(SimpleStatement.newInstance("DELETE index_ranges[?] FROM dfce.index_reference WHERE index_name=? AND base_id=?",
            rangeToDelete,
            index,
            baseId));
      final BatchStatement batch = batchBuilder.build();
      final ResultSet result = session.execute(batch);
      System.out.println("Errors=" + result.getExecutionInfo().getErrors().toString());
   }

   @Test
   public void findRangeIdTest() throws Exception {
      final IndexReference index = new IndexReference();
      final UUID baseId = BaseDAO.getBaseUUID(session);
      index.readIndexReference(session, baseId, "SM_UUID", "NOMINAL");
      final int rangeId = index.metaToRangeId("6224504e");
      System.out.println("RangeId=" + rangeId);
   }

   @Test
   public void deseactivateCompositeIndex() throws Exception {
      final String index = "cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&";

      final Update query = QueryBuilder.update("dfce", "composite_index")
            .set(Assignment.setColumn("computed", literal(false)))
            .whereColumn("id")
            .isEqualTo(literal(index))
            .ifExists();
      final ResultSet result = session.execute(query.build());
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   public void activateCompositeIndex() throws Exception {
      final String index = "cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&";

      final Update query = QueryBuilder.update("dfce", "composite_index")
            .set(Assignment.setColumn("computed", literal(true)))
            .whereColumn("id")
            .isEqualTo(literal(index))
            .ifExists();
      final ResultSet result = session.execute(query.build());
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   public void insertCompositeIndex() throws Exception {

      final List<String> metaList = new ArrayList<>();
      metaList.add("{\"name\":\"cot\",\"categoryType\":\"BOOLEAN\"}");
      metaList.add("{\"name\":\"cop\",\"categoryType\":\"STRING\"}");
      metaList.add("{\"name\":\"swa\",\"categoryType\":\"STRING\"}");
      metaList.add("{\"name\":\"cpr\",\"categoryType\":\"STRING\"}");
      metaList.add("{\"name\":\"ctr\",\"categoryType\":\"STRING\"}");
      metaList.add("{\"name\":\"SM_ARCHIVAGE_DATE\",\"categoryType\":\"DATETIME\"}");
      final Insert query = QueryBuilder.insertInto("dfce", "composite_index")
            .value("id", literal("cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&"))
            .value("computed", literal(true))
            .value("metadata_list", literal(metaList));
      final ResultSet result = session.execute(query.build());
      System.out.println("wasApplied=" + result.wasApplied());
   }

   @Test
   public void deleteCompositeIndex() throws Exception {
      final String id = "cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&";
      final SimpleStatement deleteQuery = SimpleStatement.newInstance("DELETE FROM dfce.composite_index WHERE id=?", id);
      final ResultSet result = session.execute(deleteQuery);
      System.out.println("wasApplied=" + result.wasApplied());
   }

}
