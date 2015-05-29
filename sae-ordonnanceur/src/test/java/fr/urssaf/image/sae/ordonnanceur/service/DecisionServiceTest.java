package fr.urssaf.image.sae.ordonnanceur.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.lang.exception.NestableException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

//import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class DecisionServiceTest {

   private static final String CAPTURE_MASSE_JN = "capture_masse";

   private static final String CER44_SOMMAIRE = "ecde://ecde.cer44.recouv/CS/20130916/02/sommaire.xml";

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private DecisionService decisionService;

   @Autowired
   private DFCESupport dfceSuppport;

   @Autowired
   private JobFailureService jobFailureService;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Before
   public void before() {

      // Création d'un répertoire ECDE permettant d'y déposer un sommaire.xml
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      // Dépôt d'un sommaire.xml vide dans ce répertoire
      // On n'aura besoin que de vérifier sa présence
      File fileSom = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      try {
         fileSom.createNewFile();
      } catch (IOException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

   }

   @After
   public void after() {

      EasyMock.reset(dfceSuppport);

      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }

   }

   @Test
   public void decisionService_success() throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(true);

      EasyMock.replay(dfceSuppport);

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();

      jobsEnCours.add(createJobRequest("OTHER_JN1"));
      jobsEnCours.add(createJobRequest("OTHER_JN2"));

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      JobQueue job1 = createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE);
      JobQueue job2 = createJob(CAPTURE_MASSE_JN, ecdeTestSommaire.getUrlEcde()
            .toString());
      JobQueue job3 = createJob(CAPTURE_MASSE_JN, ecdeTestSommaire.getUrlEcde()
            .toString());
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
    * test permettant de vérifier le traitement des parametres passés sous la
    * forme d'un jobParameter
    * 
    * @throws AucunJobALancerException
    */
   @Test
   public void decisionService_JobParamsuccess()
         throws AucunJobALancerException {

      EasyMock.expect(dfceSuppport.isDfceUp()).andReturn(true);

      EasyMock.replay(dfceSuppport);

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();

      jobsEnCours.add(createJobRequest("OTHER_JN1"));
      jobsEnCours.add(createJobRequest("OTHER_JN2"));

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      Map<String, String> param = new HashMap<String, String>();
      param.put("ecdeUrl", ecdeTestSommaire.getUrlEcde().toString());
      JobQueue job5 = createJobWithJobParam(CAPTURE_MASSE_JN, param);

      jobsEnAttente.add(job5);

      JobQueue job = decisionService.trouverJobALancer(jobsEnAttente,
            jobsEnCours);

      Assert.assertEquals("le traitement attendu est le job2", job5.getIdJob(),
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

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, ecdeTestSommaire
            .getUrlEcde().toString()));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement en attente
    * 
    */
   @Test
   public void decisionService_failure_noJobEnAttente_noJob() {

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();

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

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();
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

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();
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

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();
      jobsEnCours.add(createJobRequest(CAPTURE_MASSE_JN));

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, ecdeTestSommaire
            .getUrlEcde().toString()));

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * traitement en masse a trop d'anomalies
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_maxAnomalie()
         throws AucunJobALancerException {

      JobQueue job = createJob(CAPTURE_MASSE_JN, ecdeTestSommaire.getUrlEcde()
            .toString());

      jobFailureService.ajouterEchec(job.getIdJob(), new NestableException(
            "echec n°1"));
      jobFailureService.ajouterEchec(job.getIdJob(), new NestableException(
            "echec n°2"));
      jobFailureService.ajouterEchec(job.getIdJob(), new NestableException(
            "echec n°3"));

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();
      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(job);

      decisionService.trouverJobALancer(jobsEnAttente, jobsEnCours);

   }

   private JobQueue createJob(String type, String parameters) {

      JobQueue job = createJob(type);
      job.setParameters(parameters);

      return job;
   }

   private JobQueue createJobWithJobParam(String type,
         Map<String, String> parameters) {

      JobQueue job = createJob(type);
      job.setJobParameters(parameters);

      return job;
   }

   private JobRequest createJobRequest(String type) {
      UUID idJob = UUID.randomUUID();

      JobRequest job = new JobRequest();

      job.setType(type);
      job.setIdJob(idJob);

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
