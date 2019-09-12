package fr.urssaf.javaDriverTest;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;

import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.dao.CassandraSessionFactory;
import fr.urssaf.javaDriverTest.dao.IndexReference;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class DumpTest {

   CqlSession session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "cnp69imagedev.gidn.recouv";
      // servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
      // servers = "cnp69saecas4.cer69.recouv, cnp69saecas5.cer69.recouv, cnp69saecas6.cer69.recouv";
      // servers = "cnp69gntcas1,cnp69gntcas2,cnp69gntcas3";
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cnp69pregntcas1, cnp69pregntcas2";
      // servers = "cnp69givngntcas1, cnp69givngntcas2";
      // servers = "hwi69gincleasaecas1.cer69.recouv,hwi69gincleasaecas2.cer69.recouv";
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
      // servers = "hwi69devsaecas1.cer69.recouv,hwi69devsaecas2.cer69.recouv"; // Intégration interne GNS
      // servers = "hwi69ginsaecas2.cer69.recouv";
      // servers = "cer69-saeint3";
      // servers = "cnp69devgntcas1.gidn.recouv, cnp69devgntcas2.gidn.recouv";
      // servers = "cnp69dev2gntcas1.gidn.recouv, cnp69dev2gntcas2.gidn.recouv";
      // servers = "cnp69miggntcas1.gidn.recouv,cnp69miggntcas2.gidn.recouv"; // Migration cassandra V2
      // servers = "cnp69dev2gntcas1.gidn.recouv";
      // servers = "cnp69devgntcas1.gidn.recouv,cnp69devgntcas2.gidn.recouv";
      // servers = "hwi69intgnscas1.gidn.recouv,hwi69intgnscas2.gidn.recouv";
      // servers = "cnp31devpicgntcas1.gidn.recouv,cnp31devpicgntcas2.gidn.recouv";
      servers = "cnp31devpicgnscas1.gidn.recouv,cnp31devpicgnscas2.gidn.recouv";
      // servers = "cnp69gincleagntcas1.cer69.recouv,cnp69gincleagntcas2.cer69.recouv";
      // servers = "hwi69progednatgnspaj1bocas1,hwi69progednatgnspaj1bocas2";
      // servers = "cnp69intgnsp1cas1,cnp69intgnsp1cas2";
      // servers = "cnp69intgntp1cas1.cer69.recouv";

      final String cassandraLocalDC = "DC1";
      // final String cassandraLocalDC = "LYON_SP";
      session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
      dumper = new Dumper(sysout);
   }

   @After
   public void close() throws Exception {
      session.close();
   }

   @Test
   public void testDump_acl() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.acl limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_action_by_day() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.action_by_day limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_active_session() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.active_session limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_annotations() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.annotations limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_base() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.base limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_batch_counter() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.batch_counter limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_composite_index() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.composite_index limit 500");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_content_repository() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.content_repository limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_dictionary() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.dictionary limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_event_by_date() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_event_by_date limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_event_by_register_date() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_event_by_register_date limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_info() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_info limit 200");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_info_specific() throws Exception {
      final UUID uuid = UUID.fromString("c43ef1c4-bc8a-4c21-92b0-6592d1662c3a");
      final Select query = QueryBuilder.selectFrom("dfce", "doc_info")
                                       .all()
                                       .whereColumn("document_uuid")
                                       .isEqualTo(literal(uuid))
                                       .whereColumn("document_version")
                                       .isEqualTo(literal("0.0.0"));
      final ResultSet rs = session.execute(query.build());
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_statistics() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_statistics limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_time_series() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_time_series limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_document_life_cycle() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.document_life_cycle limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_documents() throws Exception {
      final SimpleStatement query = SimpleStatement.newInstance("select * from dfce.documents limit 3");
      query.setTimeout(Duration.ofMillis(30000));
      final ResultSet rs = session.execute(query);
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_doc_version() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_version limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_dynamic_dictionary_terms() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.dynamic_dictionary_terms limit 500");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_encrypted_key() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.encrypted_key limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_event() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.event limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_external_event() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.external_event limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_group() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.group limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_indexable_entity_event() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.indexable_entity_event limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_index_counter() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.index_counter limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_index_reference() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.index_reference limit 2000");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_index_reference_specific() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "cot&apr&atr&ame&SM_ARCHIVAGE_DATE&";
      // final String index = "SM_LIFE_CYCLE_REFERENCE_DATE";
      final String index = "SM_UUID";
      // final String index = "SM_FINAL_DATE";
      // final String index = "SM_ARCHIVAGE_DATE";
      // final String index = "SM_CREATION_DATE";
      // final String index = "den";
      final Select query = QueryBuilder.selectFrom("dfce", "index_reference")
                                       .all()
                                       .whereColumn("index_name")
                                       .isEqualTo(literal(index))
                                       .whereColumn("base_id")
                                       .isEqualTo(literal(baseId));
      final ResultSet rs = session.execute(query.build());
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_job_execution_by_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_execution_by_id limit 500");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_job_execution_by_instance() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_execution_by_instance limit 2000");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_job() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_job_instance_by_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_instance_by_id limit 2000");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_job_instance_by_name_and_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_instance_by_name_and_id limit 2000");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_job_instance_by_name_and_parameters() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_instance_by_name_and_parameters limit 2000");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_key_reference() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.key_reference limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_life_cycle_rule() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.life_cycle_rule limit 1500");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_life_cycle_step_history() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.life_cycle_step_history limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_lock() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.lock limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_metadata() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.metadata limit 500");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_metadata_translation() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.metadata_translation limit 500");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_note() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.note limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_note_index() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.note_index limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_running_job_executions() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.running_job_executions limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_stat_history() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.stat_history limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_stat_last_execution() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.stat_last_execution limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_stat_series() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.stat_series limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_step_execution_by_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.step_execution_by_id limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_step_execution_by_job_execution() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.step_execution_by_job_execution limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_sys_event_by_date() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.sys_event_by_date limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_sys_event_by_register_date() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.sys_event_by_register_date limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_temporary_token() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.temporary_token limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info limit 10");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_date() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_date limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_datetime() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_datetime limit 10");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_datetime_specific() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      // final String index = "SM_LIFE_CYCLE_REFERENCE_DATE";
      final String index = "SM_CREATION_DATE";
      // final String index = "SM_ARCHIVAGE_DATE";
      final String startDate = "20110907";
      final String endDate = "201109089999999";
      final int rangeId = findRangeId(baseId, index, startDate);
      final String indexCode = "";
      final Select query = QueryBuilder.selectFrom("dfce", "term_info_range_datetime")
                                       // .columns("metadata_value", "document_uuid")
                                       .all()
                                       .whereColumn("index_code")
                                       .isEqualTo(literal(indexCode))
                                       .whereColumn("base_uuid")
                                       .isEqualTo(literal(baseId))
                                       .whereColumn("metadata_name")
                                       .isEqualTo(literal(index))
                                       .whereColumn("range_index_id")
                                       .isEqualTo(literal(rangeId))
                                       .whereColumn("metadata_value")
                                       .isGreaterThan(literal(startDate))
                                       .whereColumn("metadata_value")
                                       .isLessThan(literal(endDate))
                                       // .and(QueryBuilder.gt("metadata_value", "20150520110809498"))
                                       .limit(2000);
      System.out.println(query.build().getQuery());
      final ResultSet rs = session.execute(query.build());
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_datetime_specific_filter() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String index = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      // final String index = "SM_LIFE_CYCLE_REFERENCE_DATE";
      final String index = "SM_CREATION_DATE";
      // final String index = "SM_ARCHIVAGE_DATE";
      final String startDate = "20110907";
      final String endDate = "201109089999999";
      final int rangeId = findRangeId(baseId, index, startDate);
      final String indexCode = "";
      final Select query = QueryBuilder.selectFrom("dfce", "term_info_range_datetime")
                                       // .columns("metadata_value", "document_uuid")
                                       .all()
                                       .whereColumn("index_code")
                                       .isEqualTo(literal(indexCode))
                                       .whereColumn("base_uuid")
                                       .isEqualTo(literal(baseId))
                                       .whereColumn("metadata_name")
                                       .isEqualTo(literal(index))
                                       .whereColumn("range_index_id")
                                       .isEqualTo(literal(rangeId))
                                       .whereColumn("metadata_value")
                                       .isGreaterThan(literal(startDate))
                                       .whereColumn("metadata_value")
                                       .isLessThan(literal(endDate))
                                       .limit(10000);
      System.out.println(query.build().getQuery());
      final ResultSet rs = session.execute(query.build());
      int counter = 0;
      for (final Row row : rs) {
         final Object valueAsObject = row.getObject("serialized_document");
         final ByteBuffer valueAsByteBuffer = (ByteBuffer) valueAsObject;
         final Map<String, List<String>> map = (Map<String, List<String>>) Dumper.getBytesAsObject(valueAsByteBuffer);
         final String domaineCot = getMetaValue(map, "cot");
         final String codeOrga = getMetaValue(map, "cop");
         final String statutWatt = getMetaValue(map, "swa");
         final String codeProduit = getMetaValue(map, "cpr");
         /*
         if ("true".equals(domaineCot) && "UR827".equals(codeOrga) && "PRET".equals(statutWatt)) {
            if ("PC77A".equals(codeProduit) || "PC66A".equals(codeProduit)) {
               final UUID uuid = row.getUuid("document_uuid");
               sysout.println(uuid);
               counter++;
            }
         } else {
            System.out.println("domaineCot=" + domaineCot);
            System.out.println("codeOrga=" + codeOrga);
            System.out.println("statutWatt=" + statutWatt);
            System.out.println("codeProduit=" + codeProduit);
         }
         */
         final String documentArchivage = getMetaValue(map, "dar");
         final String den = getMetaValue(map, "den");
         if ("true".equals(documentArchivage) && "Test 2300-Recherche-Iterateur-OK-Test-Libre".equals(den)) {
            counter++;
         }
      }
      System.out.println("counter=" + counter);
   }

   private String getMetaValue(final Map<String, List<String>> map, final String meta) {
      final List<String> list = map.get(meta);
      if (list == null || list.isEmpty()) {
         return null;
      }
      return list.get(0);
   }

   private int findRangeId(final UUID baseId, final String index, final String metaValue) throws Exception {
      final IndexReference indexReference = new IndexReference();
      indexReference.readIndexReference(session, baseId, index, "NOMINAL");
      return indexReference.metaToRangeId(metaValue);
   }

   @Test
   public void testDump_term_info_range_double() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_double limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_float() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_float limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_integer() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_integer limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_long() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_long limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_string() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_string limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_string_specific() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String indexName = "cot&cop&swa&SM_ARCHIVAGE_DATE&";
      // final String minValue = "true\0ur827\0pret\020190618";
      final String minValue = "true\0" + "727" + "\0pret\0" + "20190523";
      final String maxValue = "true\0" + "727" + "\0pret\0" + "2033";
      final int rangeId = findRangeId(baseId, indexName, minValue);
      final int limit = 500;
      final SimpleStatement query = SimpleStatement.newInstance("select * "
            + "from dfce.term_info_range_string "
            + "where index_code = '' and metadata_name =? and base_uuid=? and range_index_id=? "
            + "and metadata_value > ? and metadata_value < ? order by metadata_value asc limit ?",
                                                                indexName,
                                                                baseId,
                                                                BigInteger.valueOf(rangeId),
                                                                minValue,
                                                                maxValue,
                                                                limit);
      final ResultSet rs = session.execute(query);
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_string_specific2() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      // final String value = "true\0scribe\02019";
      final int rangeId = 43;
      // final String indexName = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      final String indexCode = "RB";
      final String indexName = "srt";
      final SimpleStatement query = SimpleStatement.newInstance("select * from dfce.term_info_range_string  where index_code = ? and metadata_name =? and base_uuid=? and range_index_id=? limit 100",
                                                                indexCode,
                                                                indexName,
                                                                baseId,
                                                                BigInteger.valueOf(rangeId));
      final ResultSet rs = session.execute(query);
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_uuid() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_uuid limit 1000");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_term_info_range_uuid_specific() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final int rangeId = 9;
      final String indexName = "SM_UUID";
      final String value = "d02cb203-5337-4941-be6f-52018965c68a";
      final String indexCode = "RB";
      final SimpleStatement query = SimpleStatement.newInstance("select * from dfce.term_info_range_uuid  where index_code = ? and metadata_name =? and base_uuid=? and range_index_id=? and metadata_value >= ? limit 100",
                                                                indexCode,
                                                                indexName,
                                                                baseId,
                                                                BigInteger.valueOf(rangeId),
                                                                value);
      final ResultSet rs = session.execute(query);
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_thumbnail() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.thumbnail limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_translation_code() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.translation_code limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_user() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.user limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_user_preferences() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.user_preferences limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_users_by_group() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.users_by_group limit 100");
      dumper.dumpRows(rs);
   }

   @Test
   public void testDump_sae_jobrequest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".\"JobRequest\" limit 500");
      dumper.dumpRows(rs);
   }
}
