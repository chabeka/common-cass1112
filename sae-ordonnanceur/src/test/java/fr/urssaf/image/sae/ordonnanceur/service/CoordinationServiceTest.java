package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobDejaReserveException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobInexistantException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-service-mock-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
public class CoordinationServiceTest {

   @Autowired
   private CoordinationService coordinationService;

   @Autowired
   private JobService jobService;

   @Autowired
   private DecisionService decisionService;

   @After
   public void after() {
      EasyMock.reset(jobService);
      EasyMock.reset(decisionService);
   }

   @Test
   public void lancerTraitement_success() throws AucunJobALancerException,
         JobInexistantException, JobDejaReserveException {

      List<JobInstance> jobInstances = new ArrayList<JobInstance>();
      EasyMock.expect(jobService.recupJobsALancer()).andReturn(jobInstances)
            .times(3);

      Collection<JobExecution> jobExecutions = new ArrayList<JobExecution>();
      EasyMock.expect(jobService.recupJobEnCours()).andReturn(jobExecutions)
            .times(3);

      long idTraitement = 1;
      jobService.reserveJob(idTraitement);

      EasyMock.expectLastCall().andThrow(
            new JobDejaReserveException(idTraitement, "serveur")).andThrow(
            new JobInexistantException(idTraitement)).once().once();

      JobInstance traitement = new JobInstance(idTraitement,
            new JobParameters(), "traitement");

      EasyMock.expect(
            decisionService.trouverJobALancer(jobInstances, jobExecutions))
            .andReturn(traitement).times(3);

      EasyMock.replay(jobService, decisionService);

      Assert.assertEquals("identifiant du traitement lancé inattendu", 1,
            coordinationService.lancerTraitement());

      EasyMock.verify(jobService, decisionService);

   }

}
