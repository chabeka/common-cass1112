/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletraveaux;

import static org.junit.Assert.fail;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.piletravaux.MigrationJobHistory;
import fr.urssaf.image.sae.piletravaux.MigrationJobQueue;
import fr.urssaf.image.sae.piletravaux.MigrationJobRequest;
import fr.urssaf.image.sae.utils.Dumper;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
public class DataIntegrityPileTravaux {

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

  // DAO CQL
  @Autowired
  IJobRequestDaoCql reqdaocql;

  @Autowired
  IJobHistoryDaoCql histdaocql;

  @Autowired
  IJobsQueueDaoCql queuedaocql;

  @Autowired
  private CassandraClientFactory ccf;

  @Autowired
  MigrationJobHistory migJobH;

  @Autowired
  MigrationJobQueue migJobQ;

  @Autowired
  MigrationJobRequest migJobR;

  private final static int NB_JOBS = 100;

  @Before
  public void init() throws Exception {

    final Keyspace keyspace = ccf.getKeyspace();

    sysout = new PrintStream(System.out, true, "UTF-8");

    // Pour dumper sur un fichier plutôt que sur la sortie standard
    // sysout = new PrintStream("c:/temp/out.txt");
    dumper = new Dumper(keyspace, sysout);
  }

  private void populateTableThrift() throws Exception {

    for (int i = 0; i < NB_JOBS; i++) {
      final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      System.out.println(idJob);

      final Date dateCreation = new Date();
      final Map<String, String> jobParam = new HashMap<>();
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

  @Test
  public void sliceQueryTest() throws Exception {

    populateTableThrift();

    migJobR.migrationFromThriftToCql();

    try {
      final boolean isEqBase = migJobR.compareJobRequestCql();
      Assert.assertTrue("Les données dans la base thrift et cql doivent être égales", isEqBase);
    }
    catch (final Exception e) {
      fail("Les données dans la base thrift et cql doivent être égales");
    }

  }

  @Test
  public void sliceQueryJobHistoryTest() throws Exception {

    populateTableThrift();

    migJobH.migrationFromThriftToCql();

    try {
      final boolean isEqBase = migJobH.compareJobHistoryCql();
      Assert.assertTrue("Les données dans la base thrift et cql de la table JobHistory et JobHistoryCql doivent être égales", isEqBase);
    }
    catch (final Exception e) {
      fail("Les données dans la base thrift et cql de la table JobHistory et JobHistoryCql  doivent être égales");
    }

  }

  @Test
  public void sliceQueryJobQueueTest() throws Exception {

    populateTableThrift();

    migJobQ.migrationFromThriftToCql();

    try {
      final boolean isEqBase = migJobQ.compareJobQueueCql();
      Assert.assertTrue("Les données dans la base thrift et cql de la table JobHistory et JobHistoryCql doivent être égales", isEqBase);
    }
    catch (final Exception e) {
      fail("Les données dans la base thrift et cql de la table JobHistory et JobHistoryCql  doivent être égales");
    }

  }
}
