package fr.urssaf.image.sae.pile.travaux.service.cql;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-pile-travaux-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceHostsTest {

  @Autowired
  private JobQueueCqlService jobQueueService;

  @Autowired
  private JobLectureCqlService jobLectureService;

  private UUID idJob;

  private void setJob(final UUID idJob) {
    this.idJob = idJob;
  }

  @Before
  public void before() {

    setJob(null);
  }

  @After
  public void after() {

    // suppression du traitement de masse
    if (idJob != null) {

      jobQueueService.deleteJob(idJob);

    }
  }

  @Test
  public void getHosts() throws JobInexistantException,
      JobDejaReserveException, LockTimeoutException {

    // verifie quy'aucun host n'a traite de jobs
    List<String> hosts = jobQueueService.getHosts();
    Assert.assertNotNull("La liste des hosts ne doit pas être null", hosts);

    idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    createJob(idJob);

    jobQueueService.reserveJob(idJob, "hostname", new Date());

    final Date dateDebutTraitement = new Date();
    jobQueueService.startingJob(idJob, dateDebutTraitement);

    final Date dateFinTraitement = new Date();
    jobQueueService.endingJob(idJob, true, dateFinTraitement);

    hosts = jobQueueService.getHosts();
    Assert.assertNotNull("La liste des hosts ne doit pas être null", hosts);
    Assert.assertTrue("La liste des hosts ne doit pas être vide", hosts.isEmpty());

    boolean hostPresent = false;
    for (final String hostname : hosts) {
      if (hostname.equals("hostname")) {
        hostPresent = true;
        break;
      }
    }
    Assert.assertFalse("Le nom du hosts n'est pas present dans la liste", hostPresent);

  }

  private void createJob(final UUID idJob) {

    final Date dateCreation = new Date();

    final Map<String, String> jobParam = new HashMap<String, String>();
    jobParam.put("parameters", "param");

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    job.setJobParameters(jobParam);
    job.setClientHost("clientHost");
    job.setDocCount(100);
    job.setSaeHost("saeHost");
    job.setCreationDate(dateCreation);
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueService.addJob(job);
  }

}
