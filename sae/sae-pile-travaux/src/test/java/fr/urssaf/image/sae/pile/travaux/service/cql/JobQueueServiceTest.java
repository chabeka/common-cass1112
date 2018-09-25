package fr.urssaf.image.sae.pile.travaux.service.cql;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-pile-travaux-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobQueueServiceTest {

  @Autowired
  private JobQueueCqlService jobQueueService;

  @Autowired
  private JobLectureCqlService jobLectureService;

  @Autowired
  private CassandraServerBean cassandraServer;

  @After
  public final void init() throws Exception {
    // Après chaque test, on reset les données de cassandra
    cassandraServer.resetData();
  }

  @Test
  public void concurrentReservation() throws Exception {
    // On crée 5 jobs
    final UUID[] uuids = new UUID[5];
    for (int i = 0; i < 5; i++) {
      uuids[i] = addJobForTest(i);
    }
    // On crée 10 threads
    final Map<UUID, UUID> resevationMap = new ConcurrentHashMap<UUID, UUID>();
    final SimpleThread[] threads = new SimpleThread[10];
    for (int i = 0; i < 10; i++) {
      threads[i] = new SimpleThread(uuids, resevationMap);
      threads[i].start();
    }
    // On attend la fin d'exécution de toutes les threads
    for (int i = 0; i < 10; i++) {
      threads[i].join();
    }
    // On vérifie qu'il y a eu 5 jobs de réservé, ni plus ni moins
    Assert.assertEquals(5, resevationMap.size());
  }

  private class SimpleThread extends Thread {

    Map<UUID, UUID> map;

    UUID[] uuids;

    public SimpleThread(final UUID[] uuids, final Map<UUID, UUID> reservationMap) {
      super();
      this.map = reservationMap;
      this.uuids = uuids;
    }

    @Override
    public void run() {
      for (int i = 0; i < 5; i++) {
        try {
          jobQueueService.reserveJob(uuids[i],
                                     "hostname" + this.getId(),
                                     new Date());
        }
        catch (final JobDejaReserveException e) {
          // rien
        }
        catch (final JobInexistantException e) {
          e.printStackTrace();
          // On génère une erreur
          map.put(UUID.randomUUID(), UUID.randomUUID());
        }
        catch (final LockTimeoutException e) {
          e.printStackTrace();
          // On génère une erreur
          map.put(UUID.randomUUID(), UUID.randomUUID());
        }
        map.put(uuids[i], uuids[i]);
      }
    }
  }

  @Test
  public void delete_success_en_attente() {

    final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    // création d'un job en attente
    createJob(idJob);

    // suppression du job
    jobQueueService.deleteJob(idJob);

    // vérification

    assertJobDelete(idJob);

    // vérification de JobsQueues

    final Iterator<JobQueueCql> jobQueuesEnAttente = jobLectureService.getUnreservedJobRequestIterator();

    JobQueueCql jobQueueEnAttente = null;
    while (jobQueuesEnAttente.hasNext() && jobQueueEnAttente == null) {
      final JobQueueCql jobQueueElement = jobQueuesEnAttente.next();
      if (jobQueueElement.getIdJob().equals(idJob)) {
        jobQueueEnAttente = jobQueueElement;
      }
    }

    Assert.assertNull("le job ne doit plus exister dans la file d'attente",
                      jobQueueEnAttente);

  }

  @Test
  public void delete_success_en_reservation() throws JobDejaReserveException,
      JobInexistantException, LockTimeoutException {

    final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    // création + réservation d'un job
    createJob(idJob);
    final String reservedBy = "hostname";
    jobQueueService.reserveJob(idJob, reservedBy, new Date());

    // suppression du job
    jobQueueService.deleteJob(idJob);

    // vérification

    assertJobDelete(idJob);

    // vérification de JobsQueues

    final Iterator<JobQueueCql> jobQueues = jobLectureService
                                                             .getNonTerminatedSimpleJobs(reservedBy).iterator();

    JobQueueCql jobQueue = null;
    while (jobQueues.hasNext() && jobQueue == null) {
      final JobQueueCql jobQueueElement = jobQueues.next();
      if (jobQueueElement.getIdJob().equals(idJob)) {
        jobQueue = jobQueueElement;
      }
    }

    Assert.assertNull(
                      "le job ne doit plus exister dans la file d'execution de "
                          + reservedBy,
                      jobQueue);
  }

  private void assertJobDelete(final UUID idJob) {

    // vérification de JobRequest

    final JobRequest job = jobLectureService.getJobRequest(idJob);
    Assert.assertNull("le job ne doit plus exister", job);

    // vérification de JobHistory
    final List<JobHistoryCql> jobHistory = jobLectureService.getJobHistory(idJob);

    Assert.assertTrue("le job ne doit plus avoir d'historique", jobHistory
                                                                          .isEmpty());

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

  /**
   * Crée un job
   *
   * @param index
   *          Un n° permettant de différencier les différents jobs
   * @return L'id du job créé
   */
  private UUID addJobForTest(final int index) {
    final UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

    final String parameters = "sommaire=ecde:/toto.toto.com/sommaire.xml&idTraitement="
        + index;
    final Map<String, String> jobParam = new HashMap<String, String>();
    jobParam.put("parameters", parameters);

    final JobToCreate job = new JobToCreate();
    job.setIdJob(idJob);
    job.setType("ArchivageMasse");
    job.setJobParameters(jobParam);
    job.setCreationDate(new Date());
    final String jobKey = new String("jobKey");
    job.setJobKey(jobKey.getBytes());

    jobQueueService.addJob(job);
    return idJob;
  }

}
