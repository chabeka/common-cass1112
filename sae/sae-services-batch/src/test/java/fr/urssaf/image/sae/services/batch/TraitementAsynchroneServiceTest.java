package fr.urssaf.image.sae.services.batch;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.batch.exception.JobTypeInexistantException;
import fr.urssaf.image.sae.services.batch.restore.SAERestoreMasseService;
import fr.urssaf.image.sae.services.batch.suppression.SAESuppressionMasseService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-batch-test.xml",
      "/applicationContext-sae-services-batch-test-mock-Services.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class TraitementAsynchroneServiceTest {

   @Autowired
   private TraitementAsynchroneService service;

   @Autowired
   private JobLectureService jobLectureService;

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   @Qualifier("captureMasseService")
   private SAECaptureMasseService captureMasseServiceAvecSpringSecurity;
   
   @Autowired
   @Qualifier("suppressionMasseService")
   private SAESuppressionMasseService suppressionMasseServiceAvecSpringSecurity;
   
   @Autowired
   @Qualifier("restoreMasseService")
   private SAERestoreMasseService restoreMasseServiceAvecSpringSecurity;

   private SAECaptureMasseService captureMasseServiceSansSpringSecurity;
   private SAESuppressionMasseService suppressionMasseServiceSansSpringSecurity;
   private SAERestoreMasseService restoreMasseServiceSansSpringSecurity;

   private UUID idJob;

   private void setJob(UUID idJob) {
      this.idJob = idJob;
   }

   @Before
   public void before() throws Exception {

      // Retire l'aspect de Spring Security pour pouvoir utiliser EasyMock
      // avec l'objet captureMasseServiceSansSpringSecurity
      Advised advisedCapture = (Advised) captureMasseServiceAvecSpringSecurity;
      captureMasseServiceSansSpringSecurity = (SAECaptureMasseService) advisedCapture
            .getTargetSource().getTarget();
      
      Advised advisedSuppress = (Advised) suppressionMasseServiceAvecSpringSecurity;
      suppressionMasseServiceSansSpringSecurity = (SAESuppressionMasseService) advisedSuppress
            .getTargetSource().getTarget();

      Advised advisedRestore = (Advised) restoreMasseServiceAvecSpringSecurity;
      restoreMasseServiceSansSpringSecurity = (SAERestoreMasseService) advisedRestore
            .getTargetSource().getTarget();
      
      setJob(null);
   }

   @After
   public void after() {

      EasyMock.reset(captureMasseServiceSansSpringSecurity);
      EasyMock.reset(captureMasseServiceSansSpringSecurity);
      EasyMock.reset(restoreMasseServiceSansSpringSecurity);

      //-- Suppression du traitement de masse
      if (idJob != null) {
         jobQueueService.deleteJob(idJob);
      }
   }

   @Test
   public void ajouterJobCaptureMasse_success() {

      String[] roles = new String[] { "archivage_masse" };
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            "cle", "valeur", roles);
      AuthenticationContext.setAuthenticationToken(token);

      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, "url_ecde");
      
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, idJob, Constantes.TYPES_JOB.capture_masse.name(), 
            null, null, null, null);

      service.ajouterJobCaptureMasse(parametres);

      JobRequest job = jobLectureService.getJobRequest(idJob);

      Assert.assertNotNull("le traitement " + idJob + " doit être créé", job);

      Assert
            .assertEquals(
                  "L'identifiant unique du traitement est invalide",
                  idJob, job.getIdJob());

      Assert.assertEquals(
            "La paramètre de la capture en masse doit être une URL ECDE",
            "url_ecde", job.getJobParameters().get(Constantes.ECDE_URL));
      
      Assert.assertEquals(
            "Le type de traitement doit correspondre à la capture de masse",
            Constantes.TYPES_JOB.capture_masse.name(), job.getType());
   }
   
   @Test
   public void ajouterJobSuppressionMasse_success() {
   
       String[] roles = new String[] { "suppression_masse" };
       AuthenticationToken token = AuthenticationFactory.createAuthentication(
             "cle", "valeur", roles);
       AuthenticationContext.setAuthenticationToken(token);
   
       idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
       
       Map<String, String> jobParams = new HashMap<String, String>();
       jobParams.put(Constantes.REQ_LUCENE_SUPPRESSION, "req_lucene");
       
       TraitemetMasseParametres parametres = new TraitemetMasseParametres(
             jobParams, idJob, Constantes.TYPES_JOB.suppression_masse.name(), 
             null, null, null, null);
       
       service.ajouterJobSuppressionMasse(parametres);
   
       JobRequest job = jobLectureService.getJobRequest(idJob);
   
       Assert.assertNotNull("le traitement " + idJob + " doit être créé", job);
   
       Assert.assertEquals(
                   "L'identifiant unique du traitement est invalide",
                   idJob, job.getIdJob());
       
       Assert.assertEquals(
             "Le type de traitement est incorrect",
             Constantes.TYPES_JOB.suppression_masse.name(), job.getType());
   }  
   
   @Test
   public void ajouterJobRestoreMasse_success() {
   
       String[] roles = new String[] { "restore_masse" };
       AuthenticationToken token = AuthenticationFactory.createAuthentication(
             "cle", "valeur", roles);
       AuthenticationContext.setAuthenticationToken(token);
   
       idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
       
       Map<String, String> jobParams = new HashMap<String, String>();
       jobParams.put(Constantes.UUID_TRAITEMENT_RESTORE, "id_traitement");
       
       TraitemetMasseParametres parametres = new TraitemetMasseParametres(
             jobParams, idJob, Constantes.TYPES_JOB.restore_masse.name(), 
             null, null, null, null);
       
       service.ajouterJobRestoreMasse(parametres);
   
       JobRequest job = jobLectureService.getJobRequest(idJob);
   
       Assert.assertNotNull("le traitement " + idJob + " doit être créé", job);
   
       Assert.assertEquals(
                   "L'identifiant unique du traitement est invalide",
                   idJob, job.getIdJob());
       
       Assert.assertEquals(
             "Le type de traitement est incorrect",
             Constantes.TYPES_JOB.restore_masse.name(), job.getType());
   }  
 

   /**
    * helper de création d'un VI basic
    * @param droit
    * @param code
    * @return VIContenuExtrait
    */
   private VIContenuExtrait createTestVi(String droit, String code) {
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli(code);
      viExtrait.setIdUtilisateur(code);
        
      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      saeDroits.put(droit, saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      return viExtrait;
   } 

   @Test
   public void lancerJobCaptureMasse_success() throws Exception {

      // création d'un traitement de capture en masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.ECDE_URL, "ecde://ecde.cer69.recouv/sommaire.xml");
      jobParam.put(Constantes.HASH, "hash");
      jobParam.put(Constantes.TYPE_HASH, "typeHash");
      
      String codeVi = "TEST_LANCER_JOB_CAPTURE";
      VIContenuExtrait viExtrait = createTestVi("archivage_masse", codeVi);

      JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(idJob);
      jobToCreate.setType(TYPES_JOB.capture_masse.name());
      jobToCreate.setJobParameters(jobParam);
      jobToCreate.setVi(viExtrait);

      jobQueueService.addJob(jobToCreate);
      jobQueueService.reserveJob(idJob, "hostname", new Date());

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(true);
      exitTraitement.setExitMessage("message de sortie en succès");
      EasyMock.expect(
            captureMasseServiceSansSpringSecurity.captureMasse(URI
                  .create(jobToCreate.getJobParameters().get(
                        Constantes.ECDE_URL)), idJob, jobParam
                  .get(Constantes.HASH), jobParam.get(Constantes.TYPE_HASH)))
            .andReturn(exitTraitement);

      EasyMock.replay(captureMasseServiceSansSpringSecurity);

      service.lancerJob(idJob);

      JobRequest job = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.SUCCESS, job.getState());
      Assert
            .assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  exitTraitement.getExitMessage(), job.getMessage());

      EasyMock.verify(captureMasseServiceSansSpringSecurity);
   }
   
   @Test
   public void lancerJobSuppressionMasse_success() throws Exception {
      
      //-- création d'un traitement de masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.REQ_LUCENE_SUPPRESSION, "req_lucene");
      
      String codeVi = "TEST_LANCER_JOB_SUPPR";
      VIContenuExtrait viExtrait = createTestVi("suppression_masse", codeVi);

      JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(idJob);
      jobToCreate.setType(TYPES_JOB.suppression_masse.name());
      jobToCreate.setJobParameters(jobParam);
      jobToCreate.setVi(viExtrait);

      jobQueueService.addJob(jobToCreate);
      jobQueueService.reserveJob(idJob, "hostname", new Date());

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(true);
      exitTraitement.setExitMessage("message de sortie en succès");
      EasyMock.expect(suppressionMasseServiceSansSpringSecurity.suppressionMasse(
            idJob, jobParam.get(Constantes.REQ_LUCENE_SUPPRESSION))
      ).andReturn(exitTraitement);

      EasyMock.replay(suppressionMasseServiceSansSpringSecurity);

      service.lancerJob(idJob);

      JobRequest job = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.SUCCESS, job.getState());
      
      Assert.assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  exitTraitement.getExitMessage(), job.getMessage());

      EasyMock.verify(suppressionMasseServiceSansSpringSecurity);
   }
   
   @Test
   public void lancerJobRestoreMasse_success() throws Exception {
      
      //-- création d'un traitement de masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      UUID idTraitement = UUID.randomUUID();
      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.UUID_TRAITEMENT_RESTORE, idTraitement.toString());
      
      String codeVi = "TEST_LANCER_JOB_RESTORE";
      VIContenuExtrait viExtrait = createTestVi("restore_masse", codeVi);

      JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(idJob);
      jobToCreate.setType(TYPES_JOB.restore_masse.name());
      jobToCreate.setJobParameters(jobParam);
      jobToCreate.setVi(viExtrait);

      jobQueueService.addJob(jobToCreate);
      jobQueueService.reserveJob(idJob, "hostname", new Date());

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(true);
      exitTraitement.setExitMessage("message de sortie en succès");
      EasyMock.expect(
            restoreMasseServiceSansSpringSecurity.restoreMasse(idJob, idTraitement)
      ).andReturn(exitTraitement);

      EasyMock.replay(restoreMasseServiceSansSpringSecurity);

      service.lancerJob(idJob);

      JobRequest job = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.SUCCESS, job.getState());
      
      Assert.assertEquals(
            "le message de sortie du job dans la pile des travaux est inattendu",
            exitTraitement.getExitMessage(), job.getMessage());

      EasyMock.verify(suppressionMasseServiceSansSpringSecurity);
   } 
   @Test
   public void lancerJob_failure_capturemasse() throws JobInexistantException,
         JobNonReserveException, JobDejaReserveException, LockTimeoutException {

      // création d'un traitement de capture en masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam
            .put(Constantes.ECDE_URL, "ecde://ecde.cer69.recouv/sommaire.xml");
      jobParam.put(Constantes.HASH, "hash");
      jobParam.put(Constantes.TYPE_HASH, "typeHash");

      JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(idJob);
      jobToCreate.setType(TYPES_JOB.capture_masse.name());
      jobToCreate.setJobParameters(jobParam);

      jobQueueService.addJob(jobToCreate);
      jobQueueService.reserveJob(idJob, "hostname", new Date());

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(false);
      exitTraitement.setExitMessage("message de sortie en échec");
      EasyMock.expect(
            captureMasseServiceSansSpringSecurity.captureMasse(URI
                  .create(jobToCreate.getJobParameters().get(
                        Constantes.ECDE_URL)), idJob, jobParam
                  .get(Constantes.HASH), jobParam.get(Constantes.TYPE_HASH)))
            .andReturn(exitTraitement);

      EasyMock.replay(captureMasseServiceSansSpringSecurity);

      service.lancerJob(idJob);

      JobRequest job = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.FAILURE, job.getState());
      Assert
            .assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  exitTraitement.getExitMessage(), job.getMessage());

      EasyMock.verify(captureMasseServiceSansSpringSecurity);
   }

   @Test
   public void lancerJob_failure_JobParameterTypeException()
         throws JobInexistantException, JobNonReserveException,
         JobDejaReserveException, LockTimeoutException {

      // création d'un traitement de capture en masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put(Constantes.ECDE_URL, "ecde://azaz^^/sommaire.xml");

      JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(idJob);
      jobToCreate.setType(TYPES_JOB.capture_masse.name());
      jobToCreate.setJobParameters(jobParam);

      jobQueueService.addJob(jobToCreate);
      jobQueueService.reserveJob(idJob, "hostname", new Date());

      service.lancerJob(idJob);

      JobRequest job = jobLectureService.getJobRequest(idJob);
      Assert.assertEquals(
            "l'état du job dans la pile des travaux est incorrect",
            JobState.FAILURE, job.getState());
      Assert
            .assertEquals(
                  "le message de sortie du job dans la pile des travaux est inattendu",
                  "Le traitement n°" + idJob
                        + " a des paramètres inattendu : '"
                        + job.getJobParameters().get(Constantes.ECDE_URL)
                        + "'.", job.getMessage());

   }

   @Test
   public void lancerJob_failure_JobNonReserveException()
         throws JobInexistantException, JobNonReserveException {

      // création d'un traitement de capture en masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put("PARAM", "");

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType(TYPES_JOB.capture_masse.name());
      job.setJobParameters(jobParam);
      jobQueueService.addJob(job);

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobNonReserveException doit être levée pour le traitement "
                     + idJob);

      } catch (JobNonReserveException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible d'exécuter le traitement n°" + idJob
                     + " car il n'a pas été réservé.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getJobId());

      }

   }

   @Test
   public void lancerJob_failure_JobTypeInexistantException()
         throws JobInexistantException, JobNonReserveException {

      // création d'un traitement de capture en masse
      idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put("PARAM", "");

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("type_inexistant");
      job.setJobParameters(jobParam);

      jobQueueService.addJob(job);

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobTypeInexistantException doit être levée pour le traitement "
                     + idJob);

      } catch (JobTypeInexistantException e) {

         Assert
               .assertEquals(
                     "le message de l'exception est inattendu",
                     "Traitement n° "
                        + idJob
                        + " - Le type de traitement 'type_inexistant' est inconnu.",
                     e.getMessage());

         Assert.assertEquals("Le job est incorrect", idJob, e.getJob()
               .getIdJob());

      }

   }

   @Test
   public void lancerJob_failure_jobInexistantException()
         throws JobInexistantException, JobNonReserveException {

      idJob = UUID.randomUUID();

      JobRequest job = jobLectureService.getJobRequest(idJob);

      // on s'assure que le traitement n'existe pas!
      if (job != null) {

         jobQueueService.deleteJob(job.getIdJob());

      }

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobInexistantException doit être levée pour le traitement "
                     + idJob);

      } catch (JobInexistantException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer ou de réserver le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

      }
   }
}
