package fr.urssaf.image.sae.services.batch;

import java.net.URI;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.dao.JobQueueDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.services.batch.exception.JobInattenduException;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.batch.model.CaptureMasseParametres;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-traitement-masse-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class TraitementAsynchroneServiceTest {

   private static final String TRAITEMENT_TYPE = "capture_masse";

   @Autowired
   private TraitementAsynchroneService service;

   @Autowired
   private JobQueueDao jobQueueDao;

   @Autowired
   private SAECaptureMasseService captureMasseService;

   private JobRequest job;

   private void setJob(JobRequest job) {
      this.job = job;
   }

   @Before
   public void before() {

      setJob(null);
   }

   @After
   public void after() {

      EasyMock.reset(captureMasseService);

      // suppression du traitement dde masse
      if (job != null) {

         jobQueueDao.deleteJobRequest(job);

      }
   }

   @Test
   public void ajouterJobCaptureMasse_success() {

      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      CaptureMasseParametres parametres = new CaptureMasseParametres(
            "url_ecde", idJob, null, null, null);

      service.ajouterJobCaptureMasse(parametres);

      job = jobQueueDao.getJobRequest(idJob);

      Assert.assertNotNull("le traitement " + idJob + " doit être créé", job);

      Assert
            .assertEquals(
                  "L'identifiant unique du traitement doit correspondre à l'identifiant du traitement de capture en masse",
                  idJob, job.getIdJob());

      Assert.assertEquals(
            "La paramètre de la capture en masse doit être une URL ECDE",
            "url_ecde", job.getParameters());

   }

   @Test
   public void lancerJob_success() throws JobInexistantException,
         JobNonReserveException {

      // création d'un traitement de capture en masse
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobRequest();
      job.setIdJob(idJob);
      job.setType(TRAITEMENT_TYPE);
      job.setParameters("ecde://ecde.cer69.recouv/sommaire.xml");
      job.setState(JobState.RESERVED);
      jobQueueDao.saveJobRequest(job);

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(true);
      exitTraitement.setExitMessage("message de sortie en succès");
      EasyMock.expect(
            captureMasseService.captureMasse(URI.create(job.getParameters()),
                  idJob)).andReturn(exitTraitement);

      EasyMock.replay(captureMasseService);

      service.lancerJob(idJob);

      job = jobQueueDao.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.SUCCESS, job.getState());
      Assert
            .assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  exitTraitement.getExitMessage(), job.getMessage());

      EasyMock.verify(captureMasseService);

   }

   @Test
   public void lancerJob_failure_capturemasse() throws JobInexistantException,
         JobNonReserveException {

      // création d'un traitement de capture en masse
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobRequest();
      job.setIdJob(idJob);
      job.setType(TRAITEMENT_TYPE);
      job.setParameters("ecde://ecde.cer69.recouv/sommaire.xml");
      job.setState(JobState.RESERVED);
      jobQueueDao.saveJobRequest(job);

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(false);
      exitTraitement.setExitMessage("message de sortie en échec");
      EasyMock.expect(
            captureMasseService.captureMasse(URI.create(job.getParameters()),
                  idJob)).andReturn(exitTraitement);

      EasyMock.replay(captureMasseService);

      service.lancerJob(idJob);

      job = jobQueueDao.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.FAILURE, job.getState());
      Assert
            .assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  exitTraitement.getExitMessage(), job.getMessage());

      EasyMock.verify(captureMasseService);

   }

   @Test
   public void lancerJob_failure_JobParameterTypeException()
         throws JobInexistantException, JobNonReserveException {

      // création d'un traitement de capture en masse
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobRequest();
      job.setIdJob(idJob);
      job.setType(TRAITEMENT_TYPE);
      job.setParameters("ecde://azaz^^/sommaire.xml");
      job.setState(JobState.RESERVED);
      jobQueueDao.saveJobRequest(job);

      service.lancerJob(idJob);

      job = jobQueueDao.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.FAILURE, job.getState());
      Assert
            .assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  "Le traitement n°" + idJob
                        + " a des paramètres inattendu : '"
                        + job.getParameters() + "'.", job.getMessage());

   }

   @Test
   public void lancerJob_failure_JobNonReserveException()
         throws JobInexistantException, JobNonReserveException {

      // création d'un traitement de capture en masse
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobRequest();
      job.setIdJob(idJob);
      job.setType(TRAITEMENT_TYPE);
      job.setParameters("");
      job.setState(JobState.CREATED);
      jobQueueDao.saveJobRequest(job);

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobNonReserveException doit être levée pour le traitement "
                     + idJob);

      } catch (JobNonReserveException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible d'exécuter le traitement n°" + idJob
                     + " car il n'a pas été réservé.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getJobId());

      }

   }

   @Test
   public void lancerJob_failure_JobInattenduException()
         throws JobInexistantException, JobNonReserveException {

      // création d'un traitement de capture en masse
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      job = new JobRequest();
      job.setIdJob(idJob);
      job.setType("other_masse");
      job.setParameters("");
      job.setState(JobState.CREATED);
      jobQueueDao.saveJobRequest(job);

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobInattenduException doit être levée pour le traitement "
                     + idJob);

      } catch (JobInattenduException e) {

         Assert
               .assertEquals(
                     "le message de l'exception est inattendu",
                     "Le traitement n°"
                           + idJob
                           + " est inattendu. On attend un traitement de type 'capture_masse' et le type est 'other_masse'.",
                     e.getMessage());

         Assert.assertEquals("Le job est incorrect", idJob, e.getJob()
               .getIdJob());

         Assert.assertEquals("le type attendu du job est incorrect",
               "capture_masse", e.getExpectedType());

      }

   }

   @Test
   public void lancerJob_failure_jobInexistantException()
         throws JobInexistantException, JobNonReserveException {

      UUID idJob = UUID.randomUUID();

      job = jobQueueDao.getJobRequest(idJob);

      // on s'assure que le traitement n'existe pas!
      if (job != null) {

         jobQueueDao.deleteJobRequest(job);

      }

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobInexistantException doit être levée pour le traitement "
                     + idJob);

      } catch (JobInexistantException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer ou de réserver le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

      }
   }
}
