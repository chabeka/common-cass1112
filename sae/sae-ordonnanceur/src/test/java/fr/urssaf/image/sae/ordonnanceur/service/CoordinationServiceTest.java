package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-service-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class CoordinationServiceTest {

   @Autowired
   private CoordinationService coordinationService;

   @Autowired
   private JobService jobService;

   @Autowired
   private DecisionService decisionService;

   private JobQueue traitement;

   @Before
   public void before() {

      UUID idTraitement = UUID.randomUUID();

      traitement = new JobQueue();

      traitement.setType("traitement");
      traitement.setIdJob(idTraitement);
   }

   @After
   public void after() {
      EasyMock.reset(jobService);
      EasyMock.reset(decisionService);
   }

   @Test
   public void lancerTraitement_success() throws AucunJobALancerException,
         JobInexistantException, JobDejaReserveException {

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      EasyMock.expect(jobService.recupJobsALancer()).andReturn(jobsEnAttente)
            .once();

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();

      EasyMock.expect(jobService.recupJobEnCours()).andReturn(jobsEnCours)
            .once();

      jobService.reserveJob(traitement.getIdJob());

      EasyMock.expect(
            decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours))
            .andReturn(traitement).once();

      EasyMock.replay(jobService, decisionService);

      Assert.assertEquals("identifiant du traitement lancé inattendu",
            traitement.getIdJob(), coordinationService.lancerTraitement());

      EasyMock.verify(jobService, decisionService);

   }

   @Test
   public void lancerTraitement_failure_JobDejaReserveException()
         throws AucunJobALancerException, JobInexistantException,
         JobDejaReserveException {

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      EasyMock.expect(jobService.recupJobsALancer()).andReturn(jobsEnAttente)
            .once();

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();

      EasyMock.expect(jobService.recupJobEnCours()).andReturn(jobsEnCours)
            .once();

      jobService.reserveJob(traitement.getIdJob());

      EasyMock.expectLastCall().andThrow(
            new JobDejaReserveException(traitement.getIdJob(), "serveur"));

      EasyMock.expect(
            decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours))
            .andReturn(traitement).once();

      EasyMock.replay(jobService, decisionService);

      try {
         coordinationService.lancerTraitement();

         EasyMock.verify(jobService, decisionService);

         Assert.fail("Une exception JobRuntimeException doit être levée");

      } catch (JobRuntimeException e) {

         Assert.assertEquals(
               "identifiant du traitement levant l'exception est inattendu",
               traitement.getIdJob(), e.getJob().getIdJob());

         Assert.assertTrue("Le traitement doit être déjà réservé",
               e.getCause() instanceof JobDejaReserveException);

         JobDejaReserveException cause = (JobDejaReserveException) e.getCause();

         Assert.assertEquals(
               "identifiant du traitement déjà réservé est inattendu",
               traitement.getIdJob(), cause.getInstanceId());

         Assert.assertEquals("Le traitement doit être déjà réservé", "serveur",
               cause.getServer());

      }

   }

   @Test
   public void lancerTraitement_failure_JobInexistantException()
         throws AucunJobALancerException, JobInexistantException,
         JobDejaReserveException {

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      EasyMock.expect(jobService.recupJobsALancer()).andReturn(jobsEnAttente)
            .once();

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();

      EasyMock.expect(jobService.recupJobEnCours()).andReturn(jobsEnCours)
            .once();

      jobService.reserveJob(traitement.getIdJob());

      EasyMock.expectLastCall().andThrow(
            new JobInexistantException(traitement.getIdJob()));

      EasyMock.expect(
            decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours))
            .andReturn(traitement).once();

      EasyMock.replay(jobService, decisionService);

      try {
         coordinationService.lancerTraitement();

         EasyMock.verify(jobService, decisionService);

         Assert.fail("Une exception JobRuntimeException doit être levée");

      } catch (JobRuntimeException e) {

         Assert.assertEquals(
               "identifiant du traitement levant l'exception est inattendu",
               traitement.getIdJob(), e.getJob().getIdJob());

         Assert.assertTrue("Le traitement doit être déjà réservé",
               e.getCause() instanceof JobInexistantException);

         JobInexistantException cause = (JobInexistantException) e.getCause();

         Assert.assertEquals(
               "identifiant du traitement inexistant est inattendu", traitement
                     .getIdJob(), cause.getInstanceId());

      }

   }

}
