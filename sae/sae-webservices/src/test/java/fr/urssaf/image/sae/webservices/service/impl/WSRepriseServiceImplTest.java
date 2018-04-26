package fr.urssaf.image.sae.webservices.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.cxf.common.util.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Reprise;
import fr.cirtil.www.saeservice.RepriseRequestType;
import fr.cirtil.www.saeservice.RepriseResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.exception.RepriseAxisFault;
import fr.urssaf.image.sae.webservices.service.WSRepriseService;

/**
 * Classe de test du ws de reprise de traitement de masse en erreur.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-sae-vi-test.xml"})
@SuppressWarnings({ "PMD.MethodNamingConventions",
      "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
public class WSRepriseServiceImplTest {

   @Autowired
   private WSRepriseService wsReprise;

   @Autowired
   private JobQueueService jobQueueService;

   private JobToCreate job;
   
   private static final String IP_VALUE = "127.0.0.1";
   
   @Before
   public void before() {
      // Création du job
      UUID idJob = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      String contexteLog = TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
      MDC.put("log_contexte_uuid", contexteLog);
      
      job = new JobToCreate();
      job.setIdJob(idJob);
      // Simuler un job de modification de masse
      job.setType("modification_masse");
      
      // Similation du service de reprise
      String codeVi = "TEST_REPRISE_JOB_MODIFICATION_MASSE";
      VIContenuExtrait viExtrait = createTestVi("reprise_masse", codeVi);
      
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));
      
      Map<String, String> jobParameters = new HashMap<String, String>();
      jobParameters.put(Constantes.CODE_TRAITEMENT, Constantes.CODE_TRAITEMENT);
      job.setVi(viExtrait);
      job.setJobParameters(jobParameters);
      String jobKey = new String("jobKey");
      job.setJobKey(jobKey.getBytes());

      jobQueueService.addJob(job);
   }

   @After
   public void after() {
      // suppression du traitement
      if (job != null) {
         jobQueueService.deleteJob(job.getIdJob());
      }
      AuthenticationContext.setAuthenticationToken(null);
   }
   
   /**
    * Teste la création du traitement de reprise pour un traitement inexistant
    * 
    * @throws RepriseAxisFault
    * @throws JobInexistantException
    * @throws JobDejaReserveException
    * @throws LockTimeoutException
    */
   @Test(expected = RepriseAxisFault.class)
   public void testRepriseTraitementMasseInexistant_success()
         throws RepriseAxisFault, JobInexistantException,
         JobDejaReserveException, LockTimeoutException {
      jobQueueService.reserveJob(job.getIdJob(), "hostname", new Date());
      // Simulation du job de reprise     
      mockRepriseJob(UUID.randomUUID().toString());
   }
   
   /**
    * Teste la création du traitement de reprise pour un traitement de masse
    * à l'état Reserved
    * 
    * @throws RepriseAxisFault
    * @throws JobInexistantException
    * @throws JobDejaReserveException
    * @throws LockTimeoutException
    */
   @Test(expected=RepriseAxisFault.class)
   public void testRepriseTraitementMasseReserved_success()
         throws RepriseAxisFault, JobInexistantException,
         JobDejaReserveException, LockTimeoutException {
      jobQueueService.reserveJob(job.getIdJob(), "hostname", new Date());
      // Simulation du job de reprise     
      mockRepriseJob(job.getIdJob().toString());
   }

   /**
    * Teste le service de reprise pour un traitement de masse
    * à l'état Failure
    * 
    * @throws RepriseAxisFault
    * @throws JobInexistantException
    * @throws JobDejaReserveException
    * @throws LockTimeoutException
    */
   @Test
   public void testRepriseTraitmentMasseFailure_success()
         throws RepriseAxisFault, JobInexistantException,
         JobDejaReserveException, LockTimeoutException {
      
      RepriseResponse response = new RepriseResponse();
      jobQueueService.reserveJob(job.getIdJob(), IP_VALUE, new Date());
      Date failureDate = new Date();
      // Passer le job à l'état Failure
      jobQueueService.changerEtatJobRequest(job.getIdJob(),
            JobState.FAILURE.name(), failureDate, null);
      
      String[] roles = new String[] { "reprise_masse" };
      // Similation du service de reprise
      String codeVi = "TEST_REPRISE_JOB_MODIFICATION_MASSE";
      VIContenuExtrait viExtrait = createTestVi("reprise_masse", codeVi);

      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
      
      response = mockRepriseJob(job.getIdJob().toString());
      UUID uidReprise =  null;
      if(!StringUtils.isEmpty(response.getRepriseResponse().getUuid())){
         uidReprise = UUID.fromString(response.getRepriseResponse().getUuid());
      }      
      Assert.assertNotNull(uidReprise);

   }
   
   /**
    * Lance la reprise du job associé à l'id passé en paramètre
    * 
    * @param uuid
    * @throws DeblocageAxisFault
    * @throws JobInexistantException
    */
   private RepriseResponse mockRepriseJob(String uuid) throws RepriseAxisFault,
         JobInexistantException {
      RepriseResponse response = new RepriseResponse();
      Reprise request = new Reprise();      
      request.setReprise(new RepriseRequestType());
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);
      request.getReprise().setUuid(uuidType);      
      
      response = wsReprise.reprise(request, IP_VALUE);
      return response;
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
