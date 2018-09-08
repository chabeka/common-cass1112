/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.reprise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeDocumentsType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ListeMetadonneeType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.sommaire.ObjectFactory;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.sommaire.SommaireType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionCapturePoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.common.utils.JAXBTestUtils;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
 "/applicationContext-sae-services-batch-test.xml" })
public class RepriseCaptureMasseInitTaskletTest {

   /**
    * date format
    */
   private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
         "yyyy-MM-dd");

   @Autowired
   @Qualifier("launcherReprise")
   private JobLauncherTestUtils launcherReprise;

   @Autowired
   EcdeServices services;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private SAECaptureMasseService captureMasseService;

   @Autowired
   @Qualifier("saeSearchService")
   private SAESearchService saeSearchService;

   @Autowired
   private StorageDocumentService storageDocumentService;

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private InsertionCapturePoolThreadExecutor poolExecutor;

   @Autowired
   DefaultListableBeanFactory beanFactory;

   private ExecutionContext context;

   private EcdeTestSommaire ecdeTestSommaire;

   private UUID idJob;

   private UUID idJobReprendre;

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      if (idJobReprendre != null) {
         jobQueueService.deleteJob(idJobReprendre);
      }

      try {
         if (!poolExecutor.isShutdown()) {
            poolExecutor.destroy();
         }
         beanFactory.destroySingleton("insertionCapturePoolThreadExecutor");
         beanFactory.destroySingleton("repriseCaptureMasseInitTasklet");

      } catch (Exception e) {
         Assert.fail("Shutdown pool thread KO");
      }

      AuthenticationContext.setAuthenticationToken(null);
   }

   @Before
   public void init() {

      idJob = UUID.randomUUID();
      context = new ExecutionContext();
      context.put(Constantes.ID_TRAITEMENT, idJob.toString());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      idJobReprendre = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

      Map<String, String> jobParam = new HashMap<String, String>();
      jobParam.put("ecdeUrl", ecdeTestSommaire.getUrlEcde().toString());

      createJob(idJobReprendre, jobParam);

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_masse", "recherche",
            "recherche_iterateur" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      saeDroits.put("recherche", saePrmds);
      saeDroits.put("recherche_iterateur", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("ATTESTATION DE VIGILANCE");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testRollbackAllSuccess() {

      File sommaire = null;
      // Ajout de 10 documents
      int nbDocumentIntegrated = 10;
      try {
         sommaire = this.addDocumentCapture(nbDocumentIntegrated);
      } catch (Exception e) {
         Assert.fail("Probleme de capture des documents : " + e.getMessage());
      }

      if (sommaire == null) {
         Assert.fail("Probleme de création du sommaire.xml ");
      }

      verifyIntegrationDocument(nbDocumentIntegrated);

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<String>());

      mapParameter.put(Constantes.ID_TRAITEMENT,
            new JobParameter(idJob.toString()));

      mapParameter.put(Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH, new JobParameter(
            idJobReprendre.toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcherReprise.launchStep(
            "repriseCaptureMasseInitialisation", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());

      verifySuppressionDocument(nbDocumentIntegrated);

   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testRollbackPartielSuccess() {
      // Ajout de documents (sommaire par défaut = 4 docs)
      int nbDocumentIntegrated = 4;
      try {
         this.addDocumentCapture(0);
      } catch (Exception e) {
         Assert.fail("Erreur de capture des documents : " + e.getMessage());
      }

      verifyIntegrationDocument(nbDocumentIntegrated);

      // Création d'un sommaire avec 10 docs pour simuler un rollback qui s'est
      // mal déroulé.
      File sommaire = null;
      try {
         sommaire = initDatas(10);
      } catch (Exception e) {
         Assert.fail("Erreur de création du sommaire.xml : " + e.getMessage());
      }

      if (sommaire == null) {
         Assert.fail("Probleme de création du sommaire.xml ");
      }

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<String>());

      mapParameter.put(Constantes.ID_TRAITEMENT,
            new JobParameter(idJob.toString()));

      mapParameter.put(Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH, new JobParameter(
            idJobReprendre.toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcherReprise.launchStep(
            "repriseCaptureMasseInitialisation", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());

      verifySuppressionDocument(nbDocumentIntegrated);

   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testBatchModePartielManquant() {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_reprise_error_batch_mode_manquant.xml");
      FileOutputStream fos;
      try {
         fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);
      } catch (Exception e) {
         Assert.fail("Erreur de copie du sommaire.xml : " + e.getMessage());
      }

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.ID_TRAITEMENT,
            new JobParameter(idJob.toString()));

      mapParameter.put(Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH, new JobParameter(
            idJobReprendre.toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcherReprise.launchStep(
            "repriseCaptureMasseInitialisation", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      ExitStatus exitStatus = ExitStatus.FAILED;

      Assert.assertEquals("status FAILED attendu", exitStatus.getExitCode(),
            step.getExitStatus().getExitCode());

      ExecutionContext executionContext = execution.getExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> errorMessageList = (ConcurrentLinkedQueue<String>) executionContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (errorMessageList.size()));

      Assert.assertEquals(
            "Une erreur interne à l'application est survenue lors de la reprise du traitement de masse "
                  + idJobReprendre
                  + ". Détails : Le mode du batch n'est pas reconnu",
                  errorMessageList.element());
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testBatchModeIncorrect() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_reprise_error_batch_mode_incorrect.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.ID_TRAITEMENT,
            new JobParameter(idJob.toString()));

      mapParameter.put(Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH, new JobParameter(
            idJobReprendre.toString()));


      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcherReprise.launchStep(
            "repriseCaptureMasseInitialisation", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu",
            ExitStatus.FAILED.getExitCode(), step.getExitStatus().getExitCode());

      ExecutionContext executionContext = execution.getExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> errorMessageList = (ConcurrentLinkedQueue<String>) executionContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (errorMessageList.size()));

      Assert.assertEquals(
            "Une erreur interne à l'application est survenue lors de la reprise du traitement de masse "
                  + idJobReprendre
                  + ". Détails : Le mode du batch n'est pas reconnu",
            errorMessageList.element());
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testFichierSommaireInexistant() throws Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.ID_TRAITEMENT,
            new JobParameter(idJob.toString()));

      mapParameter.put(Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH, new JobParameter(
            idJobReprendre.toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      sommaire.deleteOnExit();

      JobExecution execution = launcherReprise.launchStep(
            "repriseCaptureMasseInitialisation", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu",
            ExitStatus.FAILED.getExitCode(), step.getExitStatus().getExitCode());

      ExecutionContext executionContext = execution.getExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> errorMessageList = (ConcurrentLinkedQueue<String>) executionContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (errorMessageList.size()));

      Assert.assertEquals(
            "Le fichier sommaire " + ecdeTestSommaire.getUrlEcde()
                  + " est introuvable",
            errorMessageList.element());
   }

   /**
    * Lancer un test avec une URI dont le nom de domaine n'est pas connu
    * 
    * @throws Exception
    */
   @Test
   public void testIdTraitementManquant() throws Exception {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_format_failure.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();

      mapParameter.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcherReprise.launchStep(
            "repriseCaptureMasseInitialisation", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu",
            ExitStatus.FAILED.getExitCode(), step.getExitStatus().getExitCode());

      ExecutionContext executionContext = execution.getExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> errorMessageList = (ConcurrentLinkedQueue<String>) executionContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (errorMessageList.size()));

      Assert.assertEquals("L'identifiant du job à reprendre est requis",
            errorMessageList.element());
   }

   /**
    * Vérification de la bonne suppression des documents lors du test.
    * 
    * @param nbDocumentIntegrated
    *           Nombre de document ayant été intégré.
    */
   private void verifySuppressionDocument(int nbDocumentIntegrated) {
      List<UntypedDocument> documents = getDocumentsIntegrated(nbDocumentIntegrated);
      
      Assert.assertFalse(documents == null);

      Assert.assertEquals(0, documents.size());
      
   }

   /**
    * Vérification de la bonne integration des documents lors de la capture de
    * masse d'initialisation du test.
    * 
    * @param nbDocumentIntegrated
    *           Nombre de document ayant été intégré.
    */
   private void verifyIntegrationDocument(int nbDocumentIntegrated) {
      
      List<UntypedDocument> documents = getDocumentsIntegrated(nbDocumentIntegrated);
     
      Assert.assertFalse(documents == null);

      Assert.assertEquals(nbDocumentIntegrated, documents.size());

   }

   private List<UntypedDocument> getDocumentsIntegrated(int nbDocumentIntegrated) {
      final String lucene = String.format("%s:%s", "IdTraitementMasseInterne",
            idJobReprendre.toString());
      List<UntypedDocument> documents = null;
      try {
         documents = saeSearchService.search(lucene, new ArrayList<String>(),
               nbDocumentIntegrated);
      } catch (Exception e) {
         Assert.fail("Erreur de vérification des documents intégrés : "
               + e.getMessage());
      }

      return documents;

   }

   /**
    * Création d'un job dans la pile des travaux.
    * 
    * @param idJob
    *           Identifiant du job
    */
   private void createJob(UUID idJob, Map<String, String> jobParam) {
      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setCreationDate(new Date());
      String jobKey = new String("jobKey");
      job.setJobKey(jobKey.getBytes());

      jobQueueService.addJob(job);

   }

   /**
    * Permet d'alimenter la base de données avec le nombre de documents passé en
    * parametre.
    * 
    * @param nombreDocsCaptures
    *           Nombre de document à intéger.
    * @throws IOException
    * @throws SAECaptureServiceEx
    * @throws ReferentialRndException
    * @throws UnknownCodeRndEx
    * @throws RequiredStorageMetadataEx
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @throws UnknownMetadataEx
    * @throws DuplicatedMetadataEx
    * @throws NotSpecifiableMetadataEx
    * @throws EmptyDocumentEx
    * @throws RequiredArchivableMetadataEx
    * @throws NotArchivableMetadataEx
    * @throws UnknownHashCodeEx
    * @throws CaptureBadEcdeUrlEx
    * @throws CaptureEcdeUrlFileNotFoundEx
    * @throws MetadataValueNotInDictionaryEx
    * @throws ValidationExceptionInvalidFile
    * @throws UnknownFormatException
    * @throws UnexpectedDomainException
    * @throws InvalidPagmsCombinaisonException
    * @throws CaptureExistingUuuidException
    */
   private File addDocumentCapture(int nombreDocsCaptures) throws IOException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException {

      File sommaire = initDatas(nombreDocsCaptures);

      String hash = getHashFile(sommaire);

      ExitTraitement exit = captureMasseService.captureMasse(
            ecdeTestSommaire.getUrlEcde(), idJobReprendre, hash, "SHA-1");

      Assert.assertTrue(exit.isSucces());

      return sommaire;

   }

   /**
    * Initialisation des données pour la capture.
    * 
    * @param nombreDocsCaptures
    *           nombre de document à archiver
    * @throws IOException
    *            @{@link IOException}
    */
   private File initDatas(int nombreDocsCaptures) throws IOException {
      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/metadatacomplet/documents/doc1.PDF");
      File document = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), document);

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");

      if (nombreDocsCaptures == 0) {
         ClassPathResource resSommaire = new ClassPathResource(
               "testhautniveau/reprise_capture/sommaire.xml");
         FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);
      } else {
         creerNouveauSommaire(nombreDocsCaptures, document, sommaire);
      }

      return sommaire;

   }

   /**
    * Création d'un nouveau sommaire.
    * 
    * @param nombreDocsCaptures
    *           nombre de documents à archiver
    * @param document
    *           Document de base
    * @param sommaireFile
    */
   private void creerNouveauSommaire(int nombreDocsCaptures, File document,
         File sommaireFile) {
      final ObjectFactory factory = new ObjectFactory();
      SommaireType sommaireType = new SommaireType();
      sommaireType.setBatchMode(BatchModeType.TOUT_OU_RIEN);
      ListeDocumentsType listDocs = new ListeDocumentsType();
      sommaireType.setDocuments(listDocs);
      ListeDocumentsVirtuelsType listeDocsVirtuels = new ListeDocumentsVirtuelsType();
      listeDocsVirtuels.getDocumentVirtuel();
      sommaireType.setDocumentsVirtuels(listeDocsVirtuels);
      sommaireType.setRestitutionUuids(factory
            .createSommaireTypeRestitutionUuids(true));
      for (int i = 0; i < nombreDocsCaptures; i++) {
         DocumentType doc = new DocumentType();
         FichierType ficType = new FichierType();
         ficType.setCheminEtNomDuFichier(document.getName());
         doc.setObjetNumerique(ficType);
         doc.setMetadonnees(creerListeMetadonnees());
         listDocs.getDocument().add(doc);  
      }

      final JAXBElement<SommaireType> sommaire = factory
            .createSommaire(sommaireType);

      ecrireSommaire(sommaire, sommaireFile);

   }

   /**
    * Création de la Liste des metadonnées
    * 
    * @return la Liste des metadonnées
    */
   private ListeMetadonneeType creerListeMetadonnees() {
      ListeMetadonneeType listeMeta = new ListeMetadonneeType();
      Map<String, String> metadonnees = getDefaultMetadatas();
      for (String code : metadonnees.keySet()) {
         MetadonneeType metadonnee = new MetadonneeType();
         metadonnee.setCode(code);
         metadonnee.setValeur(metadonnees.get(code));
         listeMeta.getMetadonnee().add(metadonnee);
      }
      return listeMeta;
   }

   /**
    * Metadonnées par défaut.
    * 
    * @return Map de Métadonnée par défaut.
    */
   private Map<String, String> getDefaultMetadatas() {
      Map<String, String> datas = new HashMap<String, String>();
      Date startDate = new Date();
      datas.put("ApplicationProductrice", "ADELAIDE");
      datas.put("CodeOrganismeGestionnaire", "CER69");
      datas.put("CodeOrganismeProprietaire", "UR750");
      datas.put("CodeRND", "2.3.1.1.12");
      datas.put("DateCreation", "2011-09-08");
      datas.put("DateDebutConservation", FORMAT.format(startDate));
      datas.put("FormatFichier", "fmt/354");
      datas.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      datas.put("NbPages", "2");
      datas.put("Titre", "Attestation de vigilance");
      datas.put("TypeHash", "SHA-1");

      return datas;
   }

   /**
    * Ecriture du fichier de resultat
    * 
    * @param resultat
    *           objet représentant le résultat
    * @param sommaire
    *           sommaire.xml
    */
   private void ecrireSommaire(final JAXBElement<SommaireType> sommaireType,
         final File sommaire) {

      FileOutputStream output = null;
      try {
         output = new FileOutputStream(sommaire);

         final Resource classPath = applicationContext
               .getResource("classpath:xsd_som_res/sommaire.xsd");
         URL xsdSchema;

         xsdSchema = classPath.getURL();
         JAXBTestUtils.marshal(sommaireType, output, xsdSchema, true);

      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (JAXBException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (SAXException e) {
         throw new CaptureMasseRuntimeException(e);
      } finally {
         try {
            if (output != null) {
               output.close();
            }
         } catch (IOException e) {
            Assert.fail("Erreur de création du nouveau sommaire.xml");
         }
      }

   }

   /**
    * Renvoi le hash d'un fichier
    * 
    * @param file
    *           fichier pour calcul du has
    * @return le hash du fichier file
    */
   private String getHashFile(File file) {
      // récupération du contenu pour le calcul du HASH
      byte[] content;
      try {
         content = FileUtils.readFileToByteArray(file);
      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);
      }
      // calcul du Hash
      return DigestUtils.shaHex(content);

   }
}
