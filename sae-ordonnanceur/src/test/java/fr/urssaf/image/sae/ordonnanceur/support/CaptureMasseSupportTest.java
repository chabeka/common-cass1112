package fr.urssaf.image.sae.ordonnanceur.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class CaptureMasseSupportTest {

   @Autowired
   private CaptureMasseSupport captureMasseSupport;

   private static final String CAPTURE_MASSE_JN = "capture_masse";

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
   public void filtrerCaptureMasseLocal() {

      List<JobQueue> jobs = new ArrayList<JobQueue>();

      // traitement de capture en masse local
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.testunit.recouv/CS/20130609/01/sommaire.xml"));
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.testunit.recouv/CS/20130609/02/sommaire.xml"));

      // traitement de capture en masse non local
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer34.recouv/sommaire.xml"));
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer44.recouv/sommaire.xml"));

      // traitement de capture en masse avec une URL ECDE non configurée
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer78.recouv/sommaire.xml"));

      // autre traitement de masse
      jobs.add(createJob("OTHER_JN", "OTHER_PARAMETERS"));

      List<JobQueue> traitements = captureMasseSupport
            .filtrerCaptureMasseLocal(jobs);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 2,
            traitements.size());

      Assert.assertEquals("le traitement est inattendu", jobs.get(0),
            traitements.get(0));
      Assert.assertEquals("le traitement de capture est inattendu",
            jobs.get(1), traitements.get(1));
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

      List<JobRequest> traitements = captureMasseSupport
            .filtrerCaptureMasse(jobs);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 2,
            traitements.size());

      Assert.assertEquals("le job de capture en masse est inattendu", jobs
            .get(0), traitements.get(0));
      Assert.assertEquals("le job de capture en masse est inattendu", jobs
            .get(1), traitements.get(1));
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

   private JobRequest createJobRequest(String type) {

      UUID idJob = UUID.randomUUID();

      JobRequest job = new JobRequest();

      job.setType(type);
      job.setIdJob(idJob);

      return job;
   }

   @Test
   public void isEcdeUpJobCaptureMasse_success_true() {

      JobQueue jobQueue = createJob(CAPTURE_MASSE_JN, ecdeTestSommaire
            .getUrlEcde().toString());

      Assert.assertTrue(
            "L'ECDE de la capture de masse devrait être disponible",
            captureMasseSupport.isEcdeUpJobCaptureMasse(jobQueue));

   }

   @Test
   public void isEcdeUpJobCaptureMasse_success_false() {

      JobQueue jobQueue = createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.testunit.recouv/CS/20130619/01/sommaire.xml");

      Assert.assertFalse(
            "L'ECDE de la capture de masse ne devrait pas être disponible",
            captureMasseSupport.isEcdeUpJobCaptureMasse(jobQueue));

   }

   @Test(expected = OrdonnanceurRuntimeException.class)
   public void isEcdeUpJobCaptureMasse_failure_pasCaptureMasse() {

      JobQueue jobQueue = createJob("autre_type_de_job", "TOTO");

      captureMasseSupport.isEcdeUpJobCaptureMasse(jobQueue);

   }

}
