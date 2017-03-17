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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
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
   private static final String SUPPRESSION_MASSE_JN = "suppression_masse";
   private static final String RESTORE_MASSE_JN = "restore_masse";
   private static final String MODIFICATION_MASSE_JN = "modification_masse";

   private static final String CER44_SOMMAIRE = "ecde://ecde.cer44.recouv/CS/20130916/02/sommaire.xml";

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private DecisionService decisionService;

   @Autowired
   private JobFailureService jobFailureService;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ApplicationContext appContext;

   private static final String ECDE_URL = "ecdeUrl";
   private static final String REQUETE_SUPPRESSION = "requeteSuppression";
   private static final String AUTRE_PARAM = "autreParam";
   private static final String UUID_SUPPRESSION = "uuidSuppression";

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
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }

   @Test
   public void decisionService_success() throws AucunJobALancerException {

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
      JobQueue job5 = createJob(MODIFICATION_MASSE_JN, CER44_SOMMAIRE);
      JobQueue job6 = createJob(MODIFICATION_MASSE_JN, ecdeTestSommaire
            .getUrlEcde().toString());
      JobQueue job7 = createJob(MODIFICATION_MASSE_JN, ecdeTestSommaire
            .getUrlEcde().toString());

      // ajout de deux échecs pour le job 2
      jobFailureService.ajouterEchec(job2.getIdJob(), new NestableException(
            "echec n°1"));
      jobFailureService.ajouterEchec(job2.getIdJob(), new NestableException(
            "echec n°2"));

      jobFailureService.ajouterEchec(job7.getIdJob(), new NestableException(
            "echec n°1"));
      jobFailureService.ajouterEchec(job7.getIdJob(), new NestableException(
            "echec n°2"));
      jobFailureService.ajouterEchec(job7.getIdJob(), new NestableException(
            "echec n°3"));
      jobFailureService.ajouterEchec(job7.getIdJob(), new NestableException(
            "echec n°4"));

      jobsEnAttente.add(job1);
      jobsEnAttente.add(job2);
      jobsEnAttente.add(job3);
      jobsEnAttente.add(job4);
      jobsEnAttente.add(job5);
      jobsEnAttente.add(job6);
      jobsEnAttente.add(job7);

      List<JobQueue> jobs = decisionService.trouverListeJobALancer(
            jobsEnAttente, jobsEnCours);

      Assert.assertNotNull(jobs);
      Assert.assertEquals(4, jobs.size());

      Assert.assertTrue("le traitement attendu est le job2",
            jobs.contains(job2));

      Assert.assertTrue("le traitement attendu est le job3",
            jobs.contains(job3));

      Assert.assertTrue("le traitement attendu est le job6",
            jobs.contains(job6));

      Assert.assertTrue("le traitement attendu est le job7",
            jobs.contains(job7));

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

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();

      jobsEnCours.add(createJobRequest("OTHER_JN1"));
      jobsEnCours.add(createJobRequest("OTHER_JN2"));

      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      Map<String, String> param1 = new HashMap<String, String>();
      param1.put(ECDE_URL, CER44_SOMMAIRE);
      JobQueue job1 = createJobWithJobParam(CAPTURE_MASSE_JN, param1);
      jobsEnAttente.add(job1);

      Map<String, String> param2 = new HashMap<String, String>();
      param2.put(ECDE_URL, ecdeTestSommaire.getUrlEcde().toString());
      JobQueue job2 = createJobWithJobParam(CAPTURE_MASSE_JN, param2);
      jobsEnAttente.add(job2);

      Map<String, String> param3 = new HashMap<String, String>();
      param3.put(UUID_SUPPRESSION, "82369970-cbdf-11e5-b669-b8ca3a83061d");
      JobQueue job3 = createJobWithJobParam(SUPPRESSION_MASSE_JN, param3);
      jobsEnAttente.add(job3);
      
      Map<String, String> param4 = new HashMap<String, String>();
      param4.put(REQUETE_SUPPRESSION, "requete");
      JobQueue job4 = createJobWithJobParam(SUPPRESSION_MASSE_JN, param4);
      jobsEnAttente.add(job4);

      Map<String, String> param5 = new HashMap<String, String>();
      param5.put(AUTRE_PARAM, "requete");
      JobQueue job5 = createJobWithJobParam(RESTORE_MASSE_JN, param5);
      jobsEnAttente.add(job5);

      Map<String, String> param6 = new HashMap<String, String>();
      param6.put(AUTRE_PARAM, "code");
      JobQueue job6 = createJobWithJobParam(MODIFICATION_MASSE_JN, param6);
      jobsEnAttente.add(job6);

      Map<String, String> param7 = new HashMap<String, String>();
      param7.put(ECDE_URL, ecdeTestSommaire.getUrlEcde().toString());
      JobQueue job7 = createJobWithJobParam(MODIFICATION_MASSE_JN, param7);
      jobsEnAttente.add(job7);

      List<JobQueue> jobs = decisionService.trouverListeJobALancer(
            jobsEnAttente, jobsEnCours);

      Assert.assertNotNull(jobs);
      Assert.assertEquals(5, jobs.size());

      Assert.assertTrue("le traitement attendu est le job2",
            jobs.contains(job2));
      Assert.assertTrue("le traitement attendu est le job3",
            jobs.contains(job3));
      Assert.assertTrue("le traitement attendu est le job4",
            jobs.contains(job4));
      Assert.assertTrue("le traitement attendu est le job5",
            jobs.contains(job5));
      Assert.assertTrue("le traitement attendu est le job7",
            jobs.contains(job7));

      jobsEnAttente.remove(1);
      jobs = decisionService.trouverListeJobALancer(jobsEnAttente, jobsEnCours);

      Assert.assertNotNull(jobs);
      Assert.assertEquals(4, jobs.size());

      Assert.assertTrue("le traitement attendu est le job3",
            jobs.contains(job3));
      Assert.assertTrue("le traitement attendu est le job4",
            jobs.contains(job4));
      Assert.assertTrue("le traitement attendu est le job5",
            jobs.contains(job5));
      Assert.assertTrue("le traitement attendu est le job7",
            jobs.contains(job7));
      
      jobsEnAttente.remove(1);
      jobs = decisionService.trouverListeJobALancer(jobsEnAttente, jobsEnCours);

      Assert.assertNotNull(jobs);
      Assert.assertEquals(3, jobs.size());

      Assert.assertTrue("le traitement attendu est le job4",
            jobs.contains(job4));
      Assert.assertTrue("le traitement attendu est le job5",
            jobs.contains(job5));
      Assert.assertTrue("le traitement attendu est le job7",
            jobs.contains(job7));
   }

   /**
    * aucun traitement en attente
    * 
    */
   @Test
   public void decisionService_failure_noJobEnAttente_noJob() {

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();

      try {

         decisionService.trouverListeJobALancer(null, jobsEnCours);

         Assert.fail("une exception de type AucunJobALancerException doit être levée");

      } catch (AucunJobALancerException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Il n'y a aucun traitement à lancer", e.getMessage());
      }

   }

   /**
    * aucun traitement en masse en attente
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_nojobTraitementMasse()
         throws AucunJobALancerException {

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();
      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      JobQueue job = createJob("OTHER_JN1");

      jobsEnAttente.add(job);

      decisionService.trouverListeJobALancer(jobsEnAttente, jobsEnCours);

   }

   /**
    * aucun traitement de capture en masse en local, ou de suppression/restore
    * de masse en attente
    * 
    */
   @Test(expected = AucunJobALancerException.class)
   public void decisionService_failure_noJobEnAttente_nojobCaptureMasseLocal()
         throws AucunJobALancerException {

      List<JobRequest> jobsEnCours = new ArrayList<JobRequest>();
      List<JobQueue> jobsEnAttente = new ArrayList<JobQueue>();

      jobsEnAttente.add(createJob(CAPTURE_MASSE_JN, CER44_SOMMAIRE));

      decisionService.trouverListeJobALancer(jobsEnAttente, jobsEnCours);

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

      decisionService.trouverListeJobALancer(jobsEnAttente, jobsEnCours);

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

      decisionService.trouverListeJobALancer(jobsEnAttente, jobsEnCours);

   }

   @Test
   public void decisionService_controleDispoEcdeTraitementMasse_success()
         throws AucunJobALancerException {

      JobQueue job1 = createJob(CAPTURE_MASSE_JN, ecdeTestSommaire.getUrlEcde()
            .toString());

      // URL OK
      decisionService.controleDispoEcdeTraitementMasse(job1);

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
