/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.update.Assignment;
import com.datastax.oss.driver.api.querybuilder.update.Update;

import fr.urssaf.javaDriverTest.cleanjob.JobCleaner;
import fr.urssaf.javaDriverTest.cleanjob.JobCleaner.JobInstanceInfo;
import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.dao.CassandraSessionFactory;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class CleanJobTest {
   private static final Logger LOGGER = LoggerFactory.getLogger(CleanJobTest.class);

   CqlSession session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      final String servers;
      // servers = "cnp69imagedev.gidn.recouv";
      // servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
      // servers = "cnp69saecas4.cer69.recouv, cnp69saecas5.cer69.recouv, cnp69saecas6.cer69.recouv";
      servers = "cnp69gntcas1,cnp69gntcas2";
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

      // final String cassandraLocalDC = "DC6";
      final String cassandraLocalDC = "LYON_SP";
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
   public void findNonExistantJobInstancesTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      cleaner.findNonExistantJobInstances(session);
   }

   @Test
   public void purgeOldJobsTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      cleaner.purgeOldJobs(session, 400);
   }

   @Test
   public void getJobInstanceInfoTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      final long jobInstanceId = 12118;
      final JobInstanceInfo infos = cleaner.getJobInstanceInfo(session, jobInstanceId);
      System.out.println("JobName=" + infos.jobName);
      System.out.println("JobKey=" + infos.jobKey);
      // assertEquals("867a56b59e34aba79b072c5d5941993e", infos.jobKey);
   }

   @Test
   public void deleteOneJobInstanceTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      cleaner.deleteOneJobInstance(session, 12415);
   }

   @Test
   public void setAsNotRunningTest() throws Exception {
      // setAsNotRunning("MANAGE_RANGE_INDEX_JOB|SAE-PROD|cot&cop&nst&");
      // setAsNotRunning("DOCUMENT_EVENTS_PURGE_JOB");
      setAsNotRunning("SYSTEM_EVENTS_PURGE_JOB");
   }

   private void setAsNotRunning(final String jobKey) {
      final Update query = update("dfce", "job")
            .set(Assignment.setColumn("running", literal(false)))
            .whereColumn("job_key")
            .isEqualTo(literal(jobKey))
            .ifColumn("running")
            .isEqualTo(literal(true));
      final ResultSet result = session.execute(query.build());
      final List<String> warnings = result.getExecutionInfo().getWarnings();
      if (!warnings.isEmpty()) {
         LOGGER.warn("Warnings : {}", warnings);
      }
   }

   @Test
   public void findSplitJobInstanceIdTest() throws Exception {
      final JobCleaner cleaner = new JobCleaner();
      final String baseId = BaseDAO.getBaseName(session);
      final String indexName = "SM_MODIFICATION_DATE";
      final String rangeToSplit = "[min_lower_bound TO max_upper_bound]";
      final Long jobInstance = cleaner.findSplitJobInstanceId(session, indexName, rangeToSplit, 1000000, "600000", "EQUI_DISTRIBUTED", baseId);
      System.out.println("jobInstance = " + jobInstance);
   }

   @Test
   public void findJobInstanceByJobExecution() throws Exception {
      final long executionId = 12347;
      final SimpleStatement query = SimpleStatement.newInstance("select * from dfce.job_execution_by_id where id=?",
            executionId);
      final Row row = session.execute(query).one();
      final long jobInstance = row.getLong("job_instance_id");
      System.out.println("jobInstance = " + jobInstance);
   }
}
