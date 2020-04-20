package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-Cassandra-local-test.xml" })
public class JobQueueServiceProcessExistingJobsTest {

  @Autowired
  private CassandraServerBean cassandraServer;

  @Autowired
  private JobQueueCqlService jobQueueServiceCql;

  @Autowired
  private JobLectureCqlService jobLectureServiceCql;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  private UUID idJobWithJobParam;

  @Before
  public void init() throws Exception, IOException,
  InterruptedException, ConfigurationException {
    // on s'assure que le job initialisé dans le fichier dataSet-avec-job-ase-pile-travaux.xml est bien chargé
    // On démarre un serveur cassandra local
    EmbeddedCassandraServerHelper.startEmbeddedCassandra();

    // Add old job
    ajouterJobDansJobRequest();
  }

  @Before
  public void setup() throws Exception {
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);
  }

  @After
  public void end() throws Exception {
    cassandraServer.resetData(true, MODE_API.DATASTAX);
  }

  /**
   * Test permettant de charger par le biais du fichier
   * dataSet-avec-job-sae-pile-travaux.xml un job avec un parametre à la place
   * du jobParameter. Après le chargement on ajout un nouveau job avec un job
   * parameter et on traite les deux jobs. les deux jobs doivent être traités
   *
   * @throws JobDejaReserveException
   * @throws JobInexistantException
   * @throws LockTimeoutException
   */
  @Test
  public void processExistingJobWithOldParam() throws JobDejaReserveException, JobInexistantException, LockTimeoutException {

    final UUID idJobExistant = UUID.fromString("3897da00-3893-11e2-9ff4-005056c00008");
    JobRequestCql jobRequest = jobLectureServiceCql.getJobRequest(idJobExistant);
    Assert.assertNotNull(jobRequest);
    // creation d'un nouveau job avec des job parameters
    idJobWithJobParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    final Date dateCreation = new Date();

    final Map<String, String> jobParam = new HashMap<>();
    jobParam.put("parameters", "param");

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJobWithJobParam);
    job.setType("ArchivageMasse");
    job.setJobParameters(jobParam);
    job.setClientHost("clientHost");
    job.setDocCount(100);
    job.setSaeHost("saeHost");
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueServiceCql.addJob(job);

    // traitement des deux jobs

    jobQueueServiceCql.reserveJob(idJobExistant, "hostname", new Date());

    jobQueueServiceCql.reserveJob(idJobWithJobParam, "hostname", new Date());

    final Date dateDebutTraitement = new Date();
    jobQueueServiceCql.startingJob(idJobExistant, dateDebutTraitement);

    jobQueueServiceCql.startingJob(idJobWithJobParam, dateDebutTraitement);

    // réactualisation du job après la reservation et le démarrage
    jobRequest = jobLectureServiceCql.getJobRequest(idJobExistant);

    // vérification de JobRequest avec le parameters
    Assert.assertEquals("l'état est inattendu", JobState.STARTING.name(), jobRequest.getState());
    Assert.assertEquals("la date de démarrage est inattendue", dateDebutTraitement, jobRequest.getStartingDate());

    // vérification de JobRequest avec les jobParam
    final JobRequestCql jobRequestJobParam = jobLectureServiceCql.getJobRequest(idJobWithJobParam);
    Assert.assertEquals("l'état est inattendu", JobState.STARTING.name(), jobRequestJobParam.getState());
    Assert.assertEquals("la date de démarrage est inattendue", dateDebutTraitement, jobRequestJobParam.getStartingDate());

    // vérification de JobsQueues

    // rien à vérifier

    // vérification de JobHistory
    final List<JobHistoryCql> histories = jobLectureServiceCql.getJobHistory(idJobExistant);

    // Assert.assertEquals("le nombre de message est inattendu", 2, histories.size());

    Assert.assertNotNull(histories.get(0));
    Assert.assertEquals("le nombre de message est inattendu", 3, histories.get(0).getTrace().size());

    boolean isJobCreated = false;

    final Map<UUID, String> map = histories.get(0).getTrace();
    for (final Map.Entry<UUID, String> entry : map.entrySet()) {
      if ("CREATION DU JOB".equals(entry.getValue())) {
        isJobCreated = true;
      }
    }
    Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isJobCreated);

    // vérification de JobHistory
    final List<JobHistoryCql> historiesJobParam = jobLectureServiceCql.getJobHistory(idJobWithJobParam);

    Assert.assertEquals("le nombre de message est inattendu", 3, historiesJobParam.get(0).getTrace().size());

    boolean isJobCreatedOldJob = false;

    final Map<UUID, String> map2 = histories.get(0).getTrace();
    for (final Map.Entry<UUID, String> entry : map2.entrySet()) {
      if ("DEMARRAGE DU JOB".equals(entry.getValue())) {
        isJobCreatedOldJob = true;
      }
    }
    Assert.assertTrue("le message de l'ajout d'un traitement est inattendu", isJobCreatedOldJob);

  }

  /**
   * @param jobToCreate
   * @param clock
   */
  public final void ajouterJobDansJobRequest() {

    final JobToCreate job = new JobToCreate();

    job.setIdJob(UUID.fromString("3897da00-3893-11e2-9ff4-005056c00008"));
    job.setType("capture_masse");
    job.setParameters("ecde://CER69-TEC24251/SAE_INTEGRATION/20110822/CaptureMasse-212-CaptureMasse-Pile-OK-ECDE-local-1/sommaire.xml");
    job.setSaeHost("toto");
    job.setClientHost("127.0.0.1");
    job.setDocCount(100);
    job.setDocCountTraite(100);

    final VIContenuExtrait vi = new VIContenuExtrait();
    vi.setCodeAppli("CS_ANCIEN_SYSTEME");
    vi.setIdUtilisateur("NON_RENSEIGNE");

    final SaeDroits saeDroits = new SaeDroits();

    final SaePrmd prdm1 = new SaePrmd();

    Prmd prmd = new Prmd();
    prmd.setDescription("acces total");
    prmd.setCode("ACCES_FULL_PRMD");
    prmd.setLucene("Lucene");
    prmd.setBean("permitAll");
    prdm1.setPrmd(prmd);
    final List<SaePrmd> list1 = new ArrayList<>();
    list1.add(prdm1);
    saeDroits.put("recherche", list1);

    final List<SaePrmd> list2 = new ArrayList<>();
    prmd = new Prmd();
    prmd.setDescription("acces total");
    prmd.setCode("ACCES_FULL_PRMD");
    prmd.setLucene("Lucene");
    prmd.setBean("permitAll");
    final SaePrmd prdm2 = new SaePrmd();
    prdm2.setPrmd(prmd);
    list2.add(prdm2);
    saeDroits.put("consultation", list2);

    final List<SaePrmd> list3 = new ArrayList<>();
    prmd = new Prmd();
    prmd.setDescription("acces total");
    prmd.setCode("ACCES_FULL_PRMD");
    prmd.setLucene("Lucene");
    prmd.setBean("permitAll");
    final SaePrmd prdm3 = new SaePrmd();
    prdm3.setPrmd(prmd);
    list3.add(prdm2);
    saeDroits.put("archivage_unitaire", list3);

    final List<SaePrmd> list4 = new ArrayList<>();
    prmd = new Prmd();
    prmd.setDescription("acces total");
    prmd.setCode("ACCES_FULL_PRMD");
    prmd.setLucene("Lucene");
    prmd.setBean("permitAll");
    final SaePrmd prdm4 = new SaePrmd();
    prdm4.setPrmd(prmd);
    list4.add(prdm4);
    saeDroits.put("archivage_masse", list4);

    vi.setSaeDroits(saeDroits);

    job.setVi(vi);
    jobQueueServiceCql.addJob(job);

  }
}
