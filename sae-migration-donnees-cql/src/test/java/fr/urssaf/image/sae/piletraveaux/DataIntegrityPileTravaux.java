/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletraveaux;

import static org.junit.Assert.fail;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

  @Autowired
  private JobQueueService jobQueueService;

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

  List<UUID> idsJob = new ArrayList<>();

  @Before
  public void init() throws Exception {

    final Keyspace keyspace = ccf.getKeyspace();

    sysout = new PrintStream(System.out, true, "UTF-8");

    // Pour dumper sur un fichier plutôt que sur la sortie standard
    // sysout = new PrintStream("c:/temp/out.txt");
    dumper = new Dumper(keyspace, sysout);
  }

  private void populateTableThrift() throws Exception {

    for (int i = 0; i < 1005; i++) {
      final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      idsJob.add(idJob);

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

  private void addHistory() {


    final int i = 0;
    for (final UUID idJob : idsJob) {
      if (i < 10) {
        final Date date = new Date();
        final long timestamp = date.getTime();
        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 1),
            "message n°1");
      }
      if (i < 30) {
        final Date date = new Date();
        final long timestamp = date.getTime();
        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 1),
            "message n°1");

        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 2),
            "message n°2");
      }
      if (i < 60) {
        final Date date = new Date();
        final long timestamp = date.getTime();
        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 1),
            "message n°1");

        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 2),
            "message n°2");
        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 3),
            "message n°3");
        jobQueueService.addHistory(idJob,
                                   TimeUUIDUtils
                                   .getTimeUUID(new Date().getTime() + 4),
            "message n°4");
      }

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
    addHistory();

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
