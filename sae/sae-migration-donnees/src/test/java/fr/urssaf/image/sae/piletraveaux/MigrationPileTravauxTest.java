/**
 *
 */
package fr.urssaf.image.sae.piletraveaux;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;
import fr.urssaf.image.sae.piletravaux.MigrationJobHistory;
import fr.urssaf.image.sae.piletravaux.MigrationJobQueue;
import fr.urssaf.image.sae.piletravaux.MigrationJobRequest;
import fr.urssaf.image.sae.utils.Dumper;
import junit.framework.Assert;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
public class MigrationPileTravauxTest {

   private Dumper dumper;

   private PrintStream sysout;

   // table name
   private static final String JOBSQUEUE_CFNAME = "JobsQueue";

   private static final String JOBHISTORY_CFNAME = "JobHistory";

   public static final String JOBREQUEST_CFNAME = "JobRequest";
   // SERVICE

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;

   @Autowired
   private JobQueueCqlService jobQueueServicecql;

   @Autowired
   private JobLectureCqlService jobLectureServicecql;

   // DAO CQL
   @Autowired
   IJobRequestDaoCql reqdaocql;

   @Autowired
   IJobHistoryDaoCql histdaocql;

   @Autowired
   IJobsQueueDaoCql queuedaocql;

   // SERVER
   @Autowired
   private CassandraServerBean server;

   @Autowired
   private CassandraServerBeanCql servercql;

   @Autowired
   private CassandraClientFactory ccf;

   //

   @Autowired
   MigrationJobHistory migJobH;

   @Autowired
   MigrationJobQueue migJobQ;

   @Autowired
   MigrationJobRequest migJobR;

   @Before
   public void init() throws Exception {

      final Keyspace keyspace = ccf.getKeyspace();

      sysout = new PrintStream(System.out, true, "UTF-8");

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      // sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(keyspace, sysout);
   }

   @After
   public final void after() throws Exception {
      // Après chaque test, on reset les données de cassandra
      server.resetData(true);
      servercql.resetData();
   }

   @Test
   public void migrationFromThriftToCql() throws Exception {
      populateTableThrift();

      // migration de la table jobRequest
      migJobR.migrationFromThriftToCql();
      // verification de la table cql
      final Iterator<JobRequestCql> it = reqdaocql.findAllWithMapper();
      final List<JobRequestCql> nb_Rows = Lists.newArrayList(it);
      Assert.assertEquals(101, nb_Rows.size());

      // verification de la table thrift
      final List<JobRequest> itReq = jobLectureService.getAllJobs(ccf.getKeyspace());
      Assert.assertEquals(101, itReq.size());

      // migration de la table JobQueue
      migJobQ.migrationFromThriftToCql();
      // verification de la table cql
      final Iterator<JobQueueCql> itQ = queuedaocql.findAllWithMapper();
      final List<JobQueueCql> nb_RowsQ = Lists.newArrayList(itQ);
      Assert.assertEquals(101, nb_RowsQ.size());

      // verification de la table thrift
      final Iterator<JobQueue> itReqQueue = jobLectureService.getUnreservedJobRequestIterator();
      final List<JobQueue> nb_RowsQueue = Lists.newArrayList(itReqQueue);
      Assert.assertEquals(101, nb_RowsQueue.size());

      // migration de la table JobHistory
      migJobH.migrationFromThriftToCql();
      // verification de la table cql
      final Iterator<JobHistoryCql> itH = histdaocql.findAllWithMapper();
      final List<JobHistoryCql> nb_RowsH = Lists.newArrayList(itH);
      Assert.assertEquals(101, nb_RowsH.size());

      // verification de la table thrift
      final int nb_keyHistCql = dumper.getKeysCount(JOBHISTORY_CFNAME);
      // Assert.assertEquals(101, nb_keyHistCql);

   }

   @Test
   public void migrationFromCqlTothrift() throws Exception {
      populateTableCql();

      // JOBQueue
      // migration de la table
      migJobQ.migrationFromCqlTothrift();

      final Iterator<JobRequestCql> itQ = reqdaocql.findAllWithMapper();
      final List<JobRequestCql> nb_RowsQ = Lists.newArrayList(itQ);
      Assert.assertEquals(101, nb_RowsQ.size());

      // verification de la table thrift
      final Iterator<JobQueue> itReqQueue = jobLectureService.getUnreservedJobRequestIterator();
      final List<JobQueue> nb_RowsQueue = Lists.newArrayList(itReqQueue);
      Assert.assertEquals(101, nb_RowsQueue.size());

      // JOBREQUEST
      // migration de la table
      migJobR.migrationFromCqlTothrift();

      final Iterator<JobRequestCql> it = reqdaocql.findAllWithMapper();
      final List<JobRequestCql> nb_Rows = Lists.newArrayList(it);
      Assert.assertEquals(101, nb_Rows.size());

      // verification de la table thrift
      final List<JobRequest> itReq = jobLectureService.getAllJobs(ccf.getKeyspace());
      Assert.assertEquals(101, itReq.size());

      // JOBHistory
      // migration de la table
      migJobH.migrationFromCqlTothrift();

      final Iterator<JobRequestCql> itH = reqdaocql.findAllWithMapper();
      final List<JobRequestCql> nb_RowsH = Lists.newArrayList(itH);
      Assert.assertEquals(101, nb_RowsH.size());

      final int nb_keyHistCql = dumper.getKeysCount(JOBHISTORY_CFNAME);
      // Assert.assertEquals(101, nb_keyHistCql);
   }

   //
   public void populateTableCql() {

      for (int i = 0; i < 101; i++) {
         final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

         final Date dateCreation = new Date();
         final Map<String, String> jobParam = new HashMap<String, String>();
         jobParam.put("parameters", "param" + i);

         final JobToCreate job = new JobToCreate();
         job.setIdJob(idJob);
         job.setType("ArchivageMasse" + i);
         job.setJobParameters(jobParam);
         job.setClientHost("clientHost" + i);
         job.setDocCount(100);
         job.setSaeHost("saeHost" + i);
         job.setCreationDate(dateCreation);
         final String jobKey = new String("jobKey" + i);
         job.setJobKey(jobKey.getBytes());
         jobQueueServicecql.addJob(job);
      }

   }

   private void populateTableThrift() throws Exception {

      for (int i = 0; i < 101; i++) {
         final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
         System.out.println(idJob);

         final Date dateCreation = new Date();
         final Map<String, String> jobParam = new HashMap<String, String>();
         jobParam.put("parameters", "param" + i);

         final JobToCreate job = new JobToCreate();
         job.setIdJob(idJob);
         job.setType("ArchivageMasse" + i);
         job.setJobParameters(jobParam);
         job.setClientHost("clientHost" + i);
         job.setDocCount(100);
         job.setSaeHost("saeHost" + i);
         job.setCreationDate(dateCreation);
         final String jobKey = new String("jobKey" + 1);
         job.setJobKey(jobKey.getBytes());
         jobQueueService.addJob(job);
      }

   }
}
