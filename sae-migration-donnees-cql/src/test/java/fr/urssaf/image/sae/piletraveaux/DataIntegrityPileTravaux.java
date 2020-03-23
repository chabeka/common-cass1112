/**
 *   (AC75095028)  Classe de test de migration des PileTravaux
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

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.core.metamodel.clazz.EntityDefinitionBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
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

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private ModeApiCqlSupport modeApiCqlSupport;

  List<UUID> idsJob = new ArrayList<>();

  final static int NB_ROWS = 10;

  Javers javers;


  @Before
  public void init() throws Exception {

    // cassandraServer.resetData(false, MODE_API.DATASTAX);
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
    final Keyspace keyspace = ccf.getKeyspace();

    sysout = new PrintStream(System.out, true, "UTF-8");

    // Pour dumper sur un fichier plutôt que sur la sortie standard
    // sysout = new PrintStream("c:/temp/out.txt");
    dumper = new Dumper(keyspace, sysout);

    // Build Javers instance avec algorithme simple
    javers = JaversBuilder
        .javers()
        .registerEntity(EntityDefinitionBuilder.entityDefinition(JobHistoryCql.class)
                        .withIdPropertyName("idjob")
                        .withTypeName("JobHistoryCql")
                        .build())
        .registerEntity(EntityDefinitionBuilder.entityDefinition(JobQueueCql.class)
                        .withIdPropertyName("idJob")
                        .withTypeName("JobQueueCql")
                        .build())
        .registerEntity(EntityDefinitionBuilder.entityDefinition(JobRequestCql.class)
                        .withIdPropertyName("idJob")
                        .withTypeName("JobRequestCql")

                        .build())

        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();

  }

  private void populateTableThrift() throws Exception {

    for (int i = 0; i < NB_ROWS; i++) {
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
      final String jobKey = new String("jobKey" + i);
      job.setJobKey(jobKey.getBytes());
      jobQueueService.addJob(job);
    }

  }

  @Test
  public void sliceQueryJobRequestTest() throws Exception {

    populateTableThrift();

    migJobR.migrationFromThriftToCql();

    try {
      final Javers javers = JaversBuilder
          .javers()
          .registerEntity(EntityDefinitionBuilder.entityDefinition(JobRequestCql.class)
                          .withIdPropertyName("idJob")
                          .withTypeName("JobRequestCql")
                          .build())
          .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
          .build();
      final Diff diff = migJobR.compareJobRequestCql(javers);
      // final boolean isEqBase = migJobR.compareJobRequestCql();
      Assert.assertTrue("Les données dans la base thrift et cql doivent être égales", !diff.hasChanges());
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
      // final boolean isEqBase = migJobH.compareJobHistoryCql();
      final Diff diff = migJobH.compareJobHistoryCql(javers);
      System.out.println(diff.prettyPrint());
      Assert.assertTrue("Les données dans la base thrift et cql de la table JobHistory et JobHistoryCql doivent être égales", !diff.hasChanges());
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
