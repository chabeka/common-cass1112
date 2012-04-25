package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.lang.exception.NestableException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

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

   @Autowired
   private JobFailureService jobFailureService;

   @After
   public void after() {

      EasyMock.reset(dfceSuppport);
   }

   @Test
   public void decisionService_success() throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(true);

      EasyMock.replay(dfceSuppport);

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();

      jobsEnCours.add(createJob("OTHER_JN1"));
      jobsEnCours.add(createJob("OTHER_JN2"));

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      JobQueue job1 = createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE);
      JobQueue job2 = createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE);
      JobQueue job3 = createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE);
      JobQueue job4 = createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE);

      // ajout de deux échecs pour le job 2
      jobFailureService.ajouterEchec(job2.getIdJob(), new NestableException(
            "echec n°1"));
      jobFailureService.ajouterEchec(job2.getIdJob(), new NestableException(
            "echec n°2"));

      jobsEnAttente.add(job1);
      jobsEnAttente.add(job2);
      jobsEnAttente.add(job3);
      jobsEnAttente.add(job4);

      JobQueue job = decisionService.trouverJobALancer(jobsEnAttente,
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

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement en attente
    * 
    */
   @Test
   public void decisionService_failure_noJobEnAttente_noJob() {

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();

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

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();
      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      JobQueue job = createJob("OTHER_JN1");

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

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();
      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * traitement de capture en masse en cours
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_executionJobCaptureMasse()
         throws AucunJobALancerException {

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();
      jobsEnCours.add(createJob(CAPTURE_MASSE_JN));

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * traitement en masse a trop d'anomalies
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_maxAnomalie()
         throws AucunJobALancerException {

      JobQueue job = createJob(CAPTURE_MASSE_JN, CER69_SOMMAIRE);

      jobFailureService.ajouterEchec(job.getIdJob(), new NestableException(
            "echec n°1"));
      jobFailureService.ajouterEchec(job.getIdJob(), new NestableException(
            "echec n°2"));
      jobFailureService.ajouterEchec(job.getIdJob(), new NestableException(
            "echec n°3"));

      List<JobQueue> jobsEnCours = new ArrayList<JobQueue>();
      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(job);

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   private JobQueue createJob(String type, String parameters) {

      JobQueue job = createJob(type);
      job.setParameters(parameters);

      return job;
   }

   private JobQueue createJob(String type) {

      UUID idJob = UUID.randomUUID();

      JobQueue job = new JobQueue();

      job.setType(type);
      job.setIdJob(idJob);

      return job;
   }

}
