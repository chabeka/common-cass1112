package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

//import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class DecisionServiceTest {

   private static final String CAPTURE_MASSE_JN = "capture_masse";

   private static final String CER69_SOMMAIRE = "ecde://ecde.cer69.recouv/sommaire.xml";

   private static final String CER44_SOMMAIRE = "ecde://ecde.cer44.recouv/sommaire.xml";

   @Autowired
   private DecisionService decisionService;

   @Autowired
   private DFCESupport dfceSuppport;

   @After
   public void after() {

      EasyMock.reset(dfceSuppport);
   }

   @Test
   public void decisionService_success() throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(true);

      EasyMock.replay(dfceSuppport);

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();

      jobsEnCours.add(createJob("OTHER_JN1"));
      jobsEnCours.add(createJob("OTHER_JN2"));

      List<SimpleJobRequest> jobsEnAttente = new ArrayList<SimpleJobRequest>();

      SimpleJobRequest job1 = createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE);
      SimpleJobRequest job2 = createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE);
      SimpleJobRequest job3 = createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE);
      SimpleJobRequest job4 = createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE);

      jobsEnAttente.add(job1);
      jobsEnAttente.add(job2);
      jobsEnAttente.add(job3);
      jobsEnAttente.add(job4);

      SimpleJobRequest job = decisionService.trouverJobALancer(jobsEnAttente,
            jobsEnCours);

      Assert.assertEquals("le traitement attendu est le job2", job2.getIdJob(),
            job.getIdJob());

      EasyMock.verify(dfceSuppport);

   }

   /**
    * DFCE est down
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_dfceIsDown()
         throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(false);

      EasyMock.replay(dfceSuppport);

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();

      List<SimpleJobRequest> jobsEnAttente = new ArrayList<SimpleJobRequest>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement en attente
    * 
    */
   @Test
   public void decisionService_failure_noJobEnAttente_noJob() {

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();

      try {

         decisionService.trouverJobALancer(null, jobsEnCours);

         Assert
               .fail("une exception de type AucunJobALancerException doit être levée");

      } catch (AucunJobALancerException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Il n'y a aucun traitement à lancer", e.getMessage());
      }

   }

   /**
    * aucun traitement de capture en masse en attente
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_nojobCaptureMasse()
         throws AucunJobALancerException {

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();
      List<SimpleJobRequest> jobsEnAttente = new ArrayList<SimpleJobRequest>();

      SimpleJobRequest job = createJob("OTHER_JN1");

      jobsEnAttente.add(job);

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement de capture en masse en local en attente
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_nojobCaptureMasseLocal()
         throws AucunJobALancerException {

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();
      List<SimpleJobRequest> jobsEnAttente = new ArrayList<SimpleJobRequest>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * traitement de capture en masse en cours
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_executionJobCaptureMasse()
         throws AucunJobALancerException {

      List<SimpleJobRequest> jobsEnCours = new ArrayList<SimpleJobRequest>();
      jobsEnCours.add(createJob(CAPTURE_MASSE_JN));

      List<SimpleJobRequest> jobsEnAttente = new ArrayList<SimpleJobRequest>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   private SimpleJobRequest createJob(String type, String parameters) {

      SimpleJobRequest job = createJob(type);
      job.setParameters(parameters);

      return job;
   }

   private SimpleJobRequest createJob(String type) {

      UUID idJob = UUID.randomUUID();

      SimpleJobRequest job = new SimpleJobRequest();

      job.setType(type);
      job.setIdJob(idJob);

      return job;
   }

}
