package fr.urssaf.javaDriverTest;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

/**
 * TODO (ac75007394) Description du type
 *
 */
public class DumpTest {

   Cluster cluster;
   Session session;
   PrintStream sysout;
   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      //servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      //servers = "cnp69saecas4.cer69.recouv:9160, cnp69saecas5.cer69.recouv:9160, cnp69saecas6.cer69.recouv:9160";
      //servers = "cnp69gntcas1:9160, cnp69gntcas2:9160, cnp69gntcas3:9160";
      //servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
      //servers = "cnp69pregntcas1:9160, cnp69pregntcas2:9160";
      //servers = "cnp69givngntcas1:9160, cnp69givngntcas2:9160";
      //servers = "hwi69gincleasaecas1.cer69.recouv:9160,hwi69gincleasaecas2.cer69.recouv:9160";
      //servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160";   //Préprod
      //servers = "cnp69pprodsaecas6:9160";   //Préprod
      //servers = "cnp69pregnscas1.cer69.recouv:9160,cnp69pregnscas1.cer69.recouv:9160,cnp69pregnscas1.cer69.recouv:9160"; // Vrai préprod
      //servers = "10.213.82.56:9160";
      //servers = "cnp6gnscvecas01.cve.recouv,cnp3gnscvecas01.cve.recouv,cnp7gnscvecas01.cve.recouv";   // Charge
      //servers = "cnp3gntcvecas1.cve.recouv:9160,cnp6gntcvecas1.cve.recouv:9160,cnp7gntcvecas1.cve.recouv:9160"; // Charge GNT
      //servers = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
      //servers = "cer69imageint9.cer69.recouv:9160";
      //servers = "cer69imageint10.cer69.recouv:9160";
      //servers = "10.207.81.29:9160";
      //servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      //servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      //servers = "hwi69ginsaecas2.cer69.recouv:9160";
      //servers = "cer69-saeint3:9160";
      //servers = "cnp69devgntcas1.gidn.recouv:9160, cnp69devgntcas2.gidn.recouv:9160";
      //servers = "cnp69dev2gntcas1.gidn.recouv:9160, cnp69dev2gntcas2.gidn.recouv:9160";
      //servers = "cnp69miggntcas1.gidn.recouv:9160,cnp69miggntcas2.gidn.recouv:9160";    // Migration cassandra V2
      //servers = "cnp69dev2gntcas1.gidn.recouv:9160";
      servers = "cnp69devgntcas1.gidn.recouv:9160,cnp69devgntcas2.gidn.recouv:9160";

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
      final ResultSet rs = session.execute("select * from dfce.composite_index limit 100");
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
      final ResultSet rs = session.execute("select * from dfce.doc_info limit 100");
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
      final ResultSet rs = session.execute(
                                           new SimpleStatement("select * from dfce.documents limit 5")
                                           .setReadTimeoutMillis(30000));
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_doc_version() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.doc_version limit 100");
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
      final ResultSet rs = session.execute("select * from dfce.index_reference limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_job_execution_by_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_execution_by_id limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_job_execution_by_instance() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_execution_by_instance limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_job() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_job_instance_by_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_instance_by_id limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_job_instance_by_name_and_id() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_instance_by_name_and_id limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_job_instance_by_name_and_parameters() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.job_instance_by_name_and_parameters limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_key_reference() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.key_reference limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_life_cycle_rule() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.life_cycle_rule limit 100");
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
      final ResultSet rs = session.execute("select * from dfce.metadata limit 100");
      dumper.dumpRows(rs);
   }
   @Test
   public void testDump_metadata_translation() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.metadata_translation limit 100");
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
   public void testDump_term_info_range_uuid() throws Exception {
      final ResultSet rs = session.execute("select * from dfce.term_info_range_uuid limit 100");
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

}
