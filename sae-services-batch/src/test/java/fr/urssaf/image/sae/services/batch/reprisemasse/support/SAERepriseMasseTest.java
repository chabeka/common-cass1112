package fr.urssaf.image.sae.services.batch.reprisemasse.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.reprise.SAERepriseMasseService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-batch-test.xml",
      "/applicationContext-sae-services-reprisemasse-test-mock.xml" })
public class SAERepriseMasseTest {

   @Autowired
   private SAERepriseMasseService service;

   @Autowired
   private JobLauncher jobLauncher;

   @Autowired
   private EcdeTestTools tools;

   /**
    * Service de gestion de la pile des travaux
    */
   @Autowired
   private JobQueueService jobQueueService;

   private JobToCreate traitementAReprendre;

   private JobToCreate traitementReprise;

   @Before
   public void init() throws Exception {

      String[] roles = new String[] { "reprise_masse" };
      // Similation du service de reprise
      String codeVi = "TEST_REPRISE_JOB_MODIFICATION_MASSE";
      VIContenuExtrait viExtrait = createTestVi("reprise_masse", codeVi);

      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Création du job
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      String contexteLog = TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
      MDC.put("log_contexte_uuid", contexteLog);

      String typeHash = "SHA-1";
      String hash = "29ff24a0ec2474463f1c904ddf1e8a3c671198e9";
      String codeTraitement = "UR827";
      String urlEcdeSommaire = "ecde://cnp69devecde.cer69.recouv/doc_5000_AUG/CS_DEV_TOUTES_ACTIONS/20170426/Traitement001_ModificationMasse_passant/sommaire.xml";

      traitementAReprendre = new JobToCreate();
      traitementAReprendre.setIdJob(idJob);
      // Simuler un job de modification de masse
      traitementAReprendre.setType(TYPES_JOB.modification_masse.name());
      Map<String, String> jobParameters = new HashMap<String, String>();
      jobParameters.put(Constantes.CODE_TRAITEMENT, codeTraitement);
      jobParameters.put(Constantes.HASH, hash);
      jobParameters.put(Constantes.ECDE_URL, urlEcdeSommaire);
      jobParameters.put(Constantes.TYPE_HASH, typeHash);
      traitementAReprendre.setJobParameters(jobParameters);
      traitementAReprendre.setVi(viExtrait);
      jobQueueService.addJob(traitementAReprendre);

   }

   @After
   public void after() {

      // suppression des traitements
      if (traitementAReprendre != null) {
         jobQueueService.deleteJob(traitementAReprendre.getIdJob());
      }
      if(traitementReprise !=null){
         jobQueueService.deleteJob(traitementReprise.getIdJob());
      }
      AuthenticationContext.setAuthenticationToken(null);
   }

   /**
    * Teste le batch de reprise de la vérification des paramètres jusqu'au
    * lancement du traitement batch
    */
   @Test
   public void testLancementService_sucess() {

      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.COMPLETED);

      try {

         String codeVi = "TEST_REPRISE_JOB_MODIFICATION_MASSE";
         traitementReprise = new JobToCreate();
         traitementReprise.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
         traitementReprise.setType(TYPES_JOB.reprise_masse.name());
         Map<String, String> paramReprise = new HashMap<String, String>();
         paramReprise.put(Constantes.ID_TRAITEMENT_A_REPRENDRE,
               traitementAReprendre.getIdJob().toString());
         VIContenuExtrait viReprise = createTestVi("reprise_masse", codeVi);
         traitementReprise.setVi(viReprise);
         traitementReprise.setJobParameters(paramReprise);
         jobQueueService.addJob(traitementReprise);

         jobQueueService.reserveJob(traitementReprise.getIdJob(), "hostname",
               new Date());

         ExitTraitement exitTraitement = service.repriseMasse(traitementReprise
               .getIdJob());

         Assert.assertNotNull("l'opération doit etre ok", exitTraitement);

      } catch (Throwable e) {
         Assert.fail("pas d'erreur attendue");
      }

   }

   /**
    * Teste la vérification du contrat de service du traitement de reprise
    * @throws JobDejaReserveException
    * @throws JobInexistantException
    * @throws LockTimeoutException
    */
   @Test(expected = IllegalArgumentException.class)
   public void testRepriseJobAReprendreInexistant_success()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.FAILED);

      String codeVi = "TEST_REPRISE_JOB_MODIFICATION_MASSE";
      traitementReprise = new JobToCreate();
      traitementReprise.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      traitementReprise.setType(TYPES_JOB.reprise_masse.name());
      Map<String, String> paramReprise = new HashMap<String, String>();
      // On charge un traitement à reprendre inexistant
      paramReprise.put(Constantes.ID_TRAITEMENT_A_REPRENDRE, TimeUUIDUtils
            .getUniqueTimeUUIDinMillis().toString());
      VIContenuExtrait viReprise = createTestVi("reprise_masse", codeVi);
      traitementReprise.setVi(viReprise);
      traitementReprise.setJobParameters(paramReprise);
      jobQueueService.addJob(traitementReprise);

      jobQueueService.reserveJob(traitementReprise.getIdJob(), "hostname",
            new Date());
      ExitTraitement exitTraitement = service.repriseMasse(traitementReprise
            .getIdJob());
      Assert.assertFalse("l'opération doit etre ko", exitTraitement.isSucces());

   }

   /**
    * Teste la vérification du contrat de service du traitement de reprise 
    * @throws JobDejaReserveException
    * @throws JobInexistantException
    * @throws LockTimeoutException
    */
   @Test(expected = AccessDeniedException.class)
   public void testRepriseCsAccessFailed()
         throws JobDejaReserveException, JobInexistantException,
         LockTimeoutException {
      JobExecution jobEx = new JobExecution(1L);
      jobEx.setExitStatus(ExitStatus.FAILED);

      String codeVi = "TEST_REPRISE_JOB_MODIFICATION_";
      traitementReprise = new JobToCreate();
      traitementReprise.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      traitementReprise.setType(TYPES_JOB.reprise_masse.name());
      Map<String, String> paramReprise = new HashMap<String, String>();
      paramReprise.put(Constantes.ID_TRAITEMENT_A_REPRENDRE,
            traitementAReprendre.getIdJob().toString());
      VIContenuExtrait viReprise = createTestVi("reprise_masse", codeVi);
      traitementReprise.setVi(viReprise);
      traitementReprise.setJobParameters(paramReprise);
      jobQueueService.addJob(traitementReprise);

      jobQueueService.reserveJob(traitementReprise.getIdJob(), "hostname",
            new Date());
      ExitTraitement exitTraitement = service.repriseMasse(traitementReprise
            .getIdJob());
      Assert.assertFalse("l'opération doit etre ko", exitTraitement.isSucces());

   }

   /**
    * helper de création d'un VI basic
    * 
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

}
