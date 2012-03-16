package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

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

   @After
   public void after() {
      EasyMock.reset(jobService);
      EasyMock.reset(decisionService);
   }

   @Test
   public void lancerTraitement_success() throws AucunJobALancerException,
         JobInexistantException, JobDejaReserveException {

      List<SimpleJobRequest> jobsEnAttente = new ArrayList<SimpleJobRequest>();

      EasyMock.expect(jobService.recupJobsALancer()).andReturn(jobsEnAttente)
            .times(3);

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();

      EasyMock.expect(jobService.recupJobEnCours()).andReturn(jobsEnCours)
            .times(3);

      UUID idTraitement = UUID.randomUUID();
      jobService.reserveJob(idTraitement);

      EasyMock.expectLastCall().andThrow(
            new JobDejaReserveException(idTraitement, "serveur")).andThrow(
            new JobInexistantException(idTraitement)).once().once();

      SimpleJobRequest traitement = new SimpleJobRequest();

      traitement.setType("traitement");
      traitement.setIdJob(idTraitement);

      EasyMock.expect(
            decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours))
            .andReturn(traitement).times(3);

      EasyMock.replay(jobService, decisionService);

      Assert.assertEquals("identifiant du traitement lancé inattendu",
            idTraitement, coordinationService.lancerTraitement());

      EasyMock.verify(jobService, decisionService);

   }

}
