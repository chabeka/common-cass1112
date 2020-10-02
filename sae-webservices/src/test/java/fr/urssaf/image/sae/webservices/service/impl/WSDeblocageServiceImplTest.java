package fr.urssaf.image.sae.webservices.service.impl;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageRequestType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.utils.TraceDestinataireCqlUtils;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.service.WSDeblocageService;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

/**
 * Classe de test du ws de déblocage de traitement de masse en erreur.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings({ "PMD.MethodNamingConventions",
  "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
public class WSDeblocageServiceImplTest {

  @Autowired
  private WSDeblocageService wsDeblocage;

  @Autowired
  private JobQueueService jobQueueService;

  @Autowired
  private JobLectureService jobLectureService;

  private static final String IP_VALUE = "127.0.0.1";

  private JobToCreate job;

  @Autowired
  TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  @Before
  public void before() {
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);
    createAllTraceDestinataire();
    // Création du job
    final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("jobTest");
    final Map<String, String> jobParameters = new HashMap<>();
    jobParameters.put(Constantes.CODE_TRAITEMENT, Constantes.CODE_TRAITEMENT);
    job.setJobParameters(jobParameters);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());
    jobQueueService.addJob(job);
  }

  @After
  public void after() {
    // suppression du traitement
    if (job != null) {
      jobQueueService.deleteJob(job.getIdJob());
    }
  }

  /**
   * Teste si le job passe bien à l'état Failure après son déblocage
   * 
   * @throws DeblocageAxisFault
   * @throws JobInexistantException
   * @throws JobDejaReserveException
   * @throws LockTimeoutException
   */
  @Test
  public void testDeblocageModificationMasseReserved_success()
      throws DeblocageAxisFault, JobInexistantException,
      JobDejaReserveException, LockTimeoutException {
    jobQueueService.reserveJob(job.getIdJob(), "hostname", new Date());
    mockDeblocageJob(job.getIdJob().toString());
    final JobRequest jobRequest = jobLectureService.getJobRequest(job.getIdJob());
    Assert.assertEquals(
                        "l'état du job dans la pile des travaux est incorrect",
                        JobState.FAILURE, jobRequest.getState());
  }

  /**
   * Teste si le job passe bien à l'état ABORT après son déblocage
   * 
   * @throws DeblocageAxisFault
   * @throws JobInexistantException
   * @throws JobDejaReserveException
   * @throws LockTimeoutException
   */
  @Test
  public void testDeblocageModificationMasseFailure_success()
      throws DeblocageAxisFault, JobInexistantException,
      JobDejaReserveException, LockTimeoutException {
    jobQueueService.reserveJob(job.getIdJob(), "hostname", new Date());
    final Date failureDate = new Date();
    // Passer le job à l'état Failure
    jobQueueService.changerEtatJobRequest(job.getIdJob(),
                                          JobState.FAILURE.name(), failureDate, null);
    // Similation du déblocage
    mockDeblocageJob(job.getIdJob().toString());
    final JobRequest jobRequest = jobLectureService.getJobRequest(job.getIdJob());
    Assert.assertEquals(
                        "l'état du job dans la pile des travaux est incorrect",
                        JobState.ABORT, jobRequest.getState());
  }

  /**
   * Lance le déblocage du job associé à l'id passé en paramètre
   * 
   * @param uuid
   * @throws DeblocageAxisFault
   * @throws JobInexistantException
   */
  private void mockDeblocageJob(final String uuid) throws DeblocageAxisFault,
  JobInexistantException {
    final Deblocage request = new Deblocage();
    request.setDeblocage(new DeblocageRequestType());
    final UuidType uuidType = new UuidType();
    uuidType.setUuidType(uuid);
    request.getDeblocage().setUuid(uuidType);
    wsDeblocage.deblocage(request, IP_VALUE);
  }

  /**
   * Création des données TraceDestinataire pour effectuer les tests des services en Cql
   */
  private void createAllTraceDestinataire() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-traces.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "TraceDestinataire");

    final List<TraceDestinataire> listTraceDestinataire = TraceDestinataireCqlUtils.convertRowsToTraceDestinataires(list);
    for (final TraceDestinataire traceDestinataire : listTraceDestinataire) {

      traceDestinataireCqlSupport.create(traceDestinataire, new Date().getTime());
    }
  }
}
