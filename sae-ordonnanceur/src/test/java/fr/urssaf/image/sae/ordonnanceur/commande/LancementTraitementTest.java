package fr.urssaf.image.sae.ordonnanceur.commande;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.JobFailureService;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-ordonnanceur-commande-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class LancementTraitementTest {

   private ScheduledThreadPoolExecutor poolExecutor;

   @Autowired
   private ApplicationContext context;

   @Autowired
   private CoordinationService coordinationService;

   @Autowired
   private JobFailureService jobFailureService;

   private LancementTraitement traitement;

   @Before
   public void before() {

      poolExecutor = new ScheduledThreadPoolExecutor(1);
      traitement = new LancementTraitement(context);

   }

   @After
   public void after() {
      EasyMock.reset(coordinationService);
      EasyMock.reset(jobFailureService);
   }

   private void assertExecute(UUID expectedIdJob) {

      ScheduledFuture<UUID> scheduledFuture = poolExecutor.schedule(traitement,
            0, TimeUnit.SECONDS);

      UUID actualIdJob;
      try {
         actualIdJob = scheduledFuture.get();
      } catch (InterruptedException e) {
         throw new NestableRuntimeException(e);
      } catch (ExecutionException e) {
         throw new NestableRuntimeException(e);
      }

      Assert.assertEquals("L'identifiant du traitement de masse est inattendu",
            expectedIdJob, actualIdJob);
   }

   @Test
   public void execute_success() throws AucunJobALancerException {

      UUID idJob = UUID.randomUUID();

      EasyMock.expect(coordinationService.lancerTraitement()).andReturn(idJob);

      EasyMock.replay(coordinationService);

      assertExecute(idJob);

      EasyMock.verify(coordinationService);

   }

   @Test
   public void execute_failure_AucunJobALancerException()
         throws AucunJobALancerException {

      EasyMock.expect(coordinationService.lancerTraitement()).andThrow(
            new AucunJobALancerException());

      EasyMock.replay(coordinationService);

      assertExecute(null);

      EasyMock.verify(coordinationService);

   }

   @Test
   public void execute_failure_JobRuntimeException()
         throws AucunJobALancerException {

      Throwable cause = new NestableRuntimeException(
            "le lancement du job a échoué");
      JobQueue job = new JobQueue();
      job.setIdJob(UUID.randomUUID());
      EasyMock.expect(coordinationService.lancerTraitement()).andThrow(
            new JobRuntimeException(job, cause));

      EasyMock.replay(coordinationService);

      assertExecute(null);

      EasyMock.verify(coordinationService);

   }
   
   @Test
   public void execute_failure_RuntimeException()
         throws AucunJobALancerException {

      Throwable cause = new NestableRuntimeException(
            "le lancement du job a échoué");
      JobQueue job = new JobQueue();
      job.setIdJob(UUID.randomUUID());
      EasyMock.expect(coordinationService.lancerTraitement()).andThrow(
            new RuntimeException(cause));

      EasyMock.replay(coordinationService);

      assertExecute(null);

      EasyMock.verify(coordinationService);

   }

}
