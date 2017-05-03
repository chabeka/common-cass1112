package fr.urssaf.image.sae.services.batch.transfertmasse.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.transfert.SAETransfertMasseService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-batch-test.xml",
      "/applicationContext-sae-services-transfertmasse-test-mock.xml" })
public class SAETransfertMasseTest {

   @Autowired
   private ApplicationContext applicationContext;
   
   @Autowired
   private SAETransfertMasseService service;
   @Autowired
   private EcdeTestTools tools;

   @Autowired
   private JobLauncher jobLauncher;

   /**
    * Service de gestion de la pile des travaux.
    */
   @Autowired
   private JobQueueService jobQueueService;

   private EcdeTestSommaire testSommaire;
   
   
   private static final String HASH_TYPE = "SHA-1";
   
   @Before
   public void init() {
      testSommaire = tools.buildEcdeTestSommaire();

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "transfert_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("transfert_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void end() {
      try {
         EasyMock.reset(jobLauncher, jobQueueService);
         tools.cleanEcdeTestSommaire(testSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      AuthenticationContext.setAuthenticationToken(null);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testURLObligatoire() {
      service.transfertMasse(null, UUID.randomUUID(), "hash", HASH_TYPE);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testUUIDObligatoire() {
      service.transfertMasse(testSommaire.getUrlEcde(), null, "hash", HASH_TYPE);
   }
   
   @Test
   public void testLancementService_sucess() {

      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.COMPLETED);

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class)))
               .andReturn(jobEx).once();

         jobQueueService.renseignerDocCountJob(EasyMock.isA(UUID.class),
               EasyMock.isA(Integer.class));
         EasyMock.expectLastCall();

         EasyMock.replay(jobLauncher, jobQueueService);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);
         
         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(testSommaire
               .getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertTrue("l'opération doit etre ok", exitTraitement
               .isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
   
   @Test
   public void testLancementService_exitStatus_failed() {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.FAILED);

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class))).andReturn(jobEx)
               .once();

         jobQueueService.renseignerDocCountJob(EasyMock.isA(UUID.class),
               EasyMock.isA(Integer.class));
         EasyMock.expectLastCall();

         EasyMock.replay(jobLauncher, jobQueueService);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
   
   @Test
   public void testLancementService_fin_bloquant_failed() {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.COMPLETED);
      jobEx.addStepExecutions(Arrays.asList(new StepExecution("finBloquant",
            jobEx)));

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class))).andReturn(jobEx)
               .once();

         jobQueueService.renseignerDocCountJob(EasyMock.isA(UUID.class),
               EasyMock.isA(Integer.class));
         EasyMock.expectLastCall();

         EasyMock.replay(jobLauncher, jobQueueService);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
   
   @Test
   public void testLancementService_exitStatus_fin_erreur_failed() {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.COMPLETED);
      jobEx.addStepExecutions(Arrays.asList(new StepExecution("finErreur",
            jobEx)));

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class))).andReturn(jobEx)
               .once();

         jobQueueService.renseignerDocCountJob(EasyMock.isA(UUID.class),
               EasyMock.isA(Integer.class));
         EasyMock.expectLastCall();

         EasyMock.replay(jobLauncher, jobQueueService);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }

   @Test
   public void testLancementService_exitStatus_fin_erreur_virtuelle_failed() {

      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.COMPLETED);
      jobEx.addStepExecutions(Arrays.asList(new StepExecution(
            "finErreurVirtuel", jobEx)));

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class))).andReturn(jobEx)
               .once();

         jobQueueService.renseignerDocCountJob(EasyMock.isA(UUID.class),
               EasyMock.isA(Integer.class));
         EasyMock.expectLastCall();

         EasyMock.replay(jobLauncher, jobQueueService);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
   
   @Test
   public void testLancementService_exitStatus_autre_success() {

      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.COMPLETED);
      jobEx.addStepExecutions(Arrays.asList(new StepExecution("autre", jobEx)));

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class))).andReturn(jobEx)
               .once();

         jobQueueService.renseignerDocCountJob(EasyMock.isA(UUID.class),
               EasyMock.isA(Integer.class));
         EasyMock.expectLastCall();

         EasyMock.replay(jobLauncher, jobQueueService);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertTrue("l'opération doit etre ok",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }

   @Test
   public void testLancementService_JobExecutionAlreadyRunningException_failed() {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.FAILED);

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class)))
               .andThrow(new JobExecutionAlreadyRunningException("Error"))
               .once();

         EasyMock.replay(jobLauncher);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }
   
   @Test
   public void testLancementService_JobInstanceAlreadyCompleteException_failed() {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.FAILED);

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class)))
               .andThrow(new JobInstanceAlreadyCompleteException("Error"))
               .once();

         EasyMock.replay(jobLauncher);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }

   @Test
   public void testLancementService_JobParametersInvalidException_failed() {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.FAILED);

      try {
         EasyMock
               .expect(
                     jobLauncher.run(EasyMock.isA(FlowJob.class),
                           EasyMock.isA(JobParameters.class)))
               .andThrow(new JobParametersInvalidException("Error"))
               .once();

         EasyMock.replay(jobLauncher);

         File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
         ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
         FileOutputStream fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         String hash = getHashSommaireFile(sommaire);

         ExitTraitement exitTraitement = service.transfertMasse(
               testSommaire.getUrlEcde(), UUID.randomUUID(), hash, HASH_TYPE);

         Assert.assertFalse("l'opération doit etre ko",
               exitTraitement.isSucces());

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }

   @Test
   public void testLancementSommaireInexistant() {
  
      ExitTraitement exitTraitement = service.transfertMasse(testSommaire
            .getUrlEcde(), UUID.randomUUID(), HASH_TYPE, HASH_TYPE);

      Assert.assertFalse("l'opération doit etre en erreur", exitTraitement
            .isSucces());

      File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");

      Assert.assertFalse("le fichier résultats.xml ne doit pas exister",
            resultats.exists());
   }
   
   /**
    * Renvoi le hash d'un fichier sommaire.xml
    * @param sommaire
    * @return
    */
   private String getHashSommaireFile(File sommaire) {
      // récupération du contenu pour le calcul du HASH
      byte[] content;
      try {
         content = FileUtils.readFileToByteArray(sommaire);
      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);
      }
      // calcul du Hash     
      return DigestUtils.shaHex(content);
      
   }
}
