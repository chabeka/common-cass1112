package fr.urssaf.image.sae.ordonnanceur.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementMasseSupport;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

//import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-service-mock-test2.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class DecisionService2Test {

   private static final String CAPTURE_MASSE_JN = "capture_masse";

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private DecisionService decisionService;

   @Autowired
   private TraitementMasseSupport traitementMasseSupport;

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
      EasyMock.reset(traitementMasseSupport);
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }

   @Test(expected = AucunJobALancerException.class)
   public void decisionService_controleDispoEcdeTraitementMasse_failure()
         throws AucunJobALancerException, EcdeBadURLException,
         EcdeBadURLFormatException {

      JobQueue job1 = createJob(CAPTURE_MASSE_JN, ecdeTestSommaire.getUrlEcde()
            .toString());
      
      EasyMock.expect(traitementMasseSupport.isEcdeUpJobTraitementMasse(job1))
            .andReturn(false).once();

      EasyMock.expect(traitementMasseSupport.extractUrlEcde(job1))
            .andReturn(ecdeTestSommaire.getUrlEcde()).once();

      EasyMock.replay(traitementMasseSupport);

      // URL OK
      decisionService.controleDispoEcdeTraitementMasse(job1);

      EasyMock.verify(traitementMasseSupport);

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
