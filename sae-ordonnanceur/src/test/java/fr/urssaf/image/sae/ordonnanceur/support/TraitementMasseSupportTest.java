package fr.urssaf.image.sae.ordonnanceur.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementMasseSupportTest {



   @Autowired
   private TraitementMasseSupport traitementMasseSupport;

   private static final String CAPTURE_MASSE_JN = "capture_masse";
   private static final String SUPPRESSION_MASSE_JN = "suppression_masse";
   private static final String RESTORE_MASSE_JN = "restore_masse";
   
   private static final String ECDE_URL = "ecdeUrl";
   private static final String REQUETE_SUPPRESSION = "requeteSuppression";
   private static final String AUTRE_PARAM = "autreParam";
   private static final String UUID_SUPPRESSION = "uuidSuppression";

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

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
   public void filtrerTraitementMasse() {

      List<JobQueue> jobs = new ArrayList<JobQueue>();

      // traitement de capture en masse local
      Map<String, String> jobParametersCapture1 = new HashMap<String, String>();
      jobParametersCapture1.put(ECDE_URL, "ecde://ecde.testunit.recouv/CS/20130609/01/sommaire.xml");
      jobs.add(createJob(CAPTURE_MASSE_JN,jobParametersCapture1));
      
      Map<String, String> jobParametersCapture2 = new HashMap<String, String>();
      jobParametersCapture2.put(ECDE_URL, "ecde://ecde.testunit.recouv/CS/20130609/02/sommaire.xml");
      jobs.add(createJob(CAPTURE_MASSE_JN, jobParametersCapture2));

      // traitement de capture en masse non local
      Map<String, String> jobParametersCapture3 = new HashMap<String, String>();
      jobParametersCapture3.put(ECDE_URL, "ecde://ecde.cer34.recouv/sommaire.xml");
      jobs.add(createJob(CAPTURE_MASSE_JN, jobParametersCapture3));
      Map<String, String> jobParametersCapture4 = new HashMap<String, String>();
      jobParametersCapture4.put(ECDE_URL, "ecde://ecde.cer34.recouv/sommaire.xml");
      jobs.add(createJob(CAPTURE_MASSE_JN, jobParametersCapture4));

      // traitement de capture en masse avec une URL ECDE non configurée
      Map<String, String> jobParametersCapture5 = new HashMap<String, String>();
      jobParametersCapture5.put(ECDE_URL, "ecde://ecde.cer78.recouv/sommaire.xml");
      jobs.add(createJob(CAPTURE_MASSE_JN, jobParametersCapture5));
      
      // traitement de restore de masse
      Map<String, String> jobParametersRestore = new HashMap<String, String>();
      jobParametersRestore.put(UUID_SUPPRESSION, "82369970-cbdf-11e5-b669-b8ca3a83061d");
      jobs.add(createJob(RESTORE_MASSE_JN, jobParametersRestore));

      // traitement de suppression de masse
      Map<String, String> jobParametersSuppression = new HashMap<String, String>();
      jobParametersSuppression.put(REQUETE_SUPPRESSION, "requete lucene");
      jobs.add(createJob(SUPPRESSION_MASSE_JN, jobParametersSuppression));
      
      // autre traitement de masse
      Map<String, String> jobParametersAutre = new HashMap<String, String>();
      jobParametersAutre.put(AUTRE_PARAM, "autre");
      jobs.add(createJob("OTHER_JN", jobParametersAutre));

      List<JobQueue> traitements = traitementMasseSupport.filtrerTraitementMasse(jobs);
           

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 4,
            traitements.size());

      Assert.assertEquals("le traitement de capture est inattendu", jobs.get(0),
            traitements.get(0));
      Assert.assertEquals("le traitement de capture est inattendu",
            jobs.get(1), traitements.get(1));
      Assert.assertEquals("le traitement de restore est inattendu",
            jobs.get(5), traitements.get(2));
      Assert.assertEquals("le traitement de suppression est inattendu",
            jobs.get(6), traitements.get(3));
      
   }

   @Test
   public void filtrerJobExecutionLocal() {

      List<JobRequest> jobs = new ArrayList<JobRequest>();

      // traitement de capture en masse
      jobs.add(createJobRequest(CAPTURE_MASSE_JN));
      jobs.add(createJobRequest(CAPTURE_MASSE_JN));

      // autre traitement de masse
      jobs.add(createJobRequest("OTHER_JN1"));
      jobs.add(createJobRequest("OTHER_JN2"));
      
      // traitement de restore de masse
      jobs.add(createJobRequest(RESTORE_MASSE_JN));
      
      // traitement de suppression de masse
      jobs.add(createJobRequest(SUPPRESSION_MASSE_JN));

      List<JobRequest> traitements = traitementMasseSupport.filtrerTraitementMasse(jobs);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 4,
            traitements.size());

      Assert.assertEquals("le job de capture en masse est inattendu", jobs
            .get(0), traitements.get(0));
      Assert.assertEquals("le job de capture en masse est inattendu", jobs
            .get(1), traitements.get(1));
      Assert.assertEquals("le job de restore en masse est inattendu", jobs
            .get(4), traitements.get(2));
      Assert.assertEquals("le job de suppression en masse est inattendu", jobs
            .get(5), traitements.get(3));
   }

   private JobQueue createJob(String type, Map<String,String> jobParameters) {

      JobQueue job = createJob(type);
      job.setJobParameters(jobParameters);

      return job;
   }

   private JobQueue createJob(String type) {

      UUID idJob = UUID.randomUUID();

      JobQueue job = new JobQueue();

      job.setType(type);
      job.setIdJob(idJob);

      return job;
   }

   private JobRequest createJobRequest(String type) {

      UUID idJob = UUID.randomUUID();

      JobRequest job = new JobRequest();

      job.setType(type);
      job.setIdJob(idJob);

      return job;
   }

   @Test
   public void isEcdeUpJobCaptureMasse_success_true() {

      Map<String, String> jobParametersCapture1 = new HashMap<String, String>();
      jobParametersCapture1.put(ECDE_URL, ecdeTestSommaire.getUrlEcde().toString());
      JobQueue jobQueue = createJob(CAPTURE_MASSE_JN, jobParametersCapture1);

      Assert.assertTrue(
            "L'ECDE de la capture de masse devrait être disponible",
            traitementMasseSupport.isEcdeUpJobCaptureMasse(jobQueue));

   }
   
   @Test(expected=OrdonnanceurRuntimeException.class)
   public void isEcdeUpJobCaptureMasse_error_true() {

      Map<String, String> jobParametersSuppression = new HashMap<String, String>();
      jobParametersSuppression.put(REQUETE_SUPPRESSION, "requête lucène");
      JobQueue jobQueue = createJob(SUPPRESSION_MASSE_JN, jobParametersSuppression);

      traitementMasseSupport.isEcdeUpJobCaptureMasse(jobQueue);
      
      Assert.fail("Une exception est attendue");
   }

   @Test
   public void isEcdeUpJobCaptureMasse_success_false() {

      Map<String, String> jobParametersCapture1 = new HashMap<String, String>();
      jobParametersCapture1.put(ECDE_URL, "ecde://ecde.testunit.recouv/CS/20130619/01/sommaire.xml");
      JobQueue jobQueue = createJob(CAPTURE_MASSE_JN,jobParametersCapture1);

      Assert.assertFalse(
            "L'ECDE de la capture de masse ne devrait pas être disponible",
            traitementMasseSupport.isEcdeUpJobCaptureMasse(jobQueue));

   }


}
