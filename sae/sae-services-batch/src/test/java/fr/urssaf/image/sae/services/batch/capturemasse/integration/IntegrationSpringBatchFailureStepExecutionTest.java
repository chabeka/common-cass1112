/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.utils.LogUtils;
import fr.urssaf.image.sae.utils.SaeLogAppender;
import fr.urssaf.image.sae.utils.SaeStepExecutionDao;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-batch-test.xml",
"/applicationContext-sae-services-capturemasse-test-dao-spring.xml" })
public class IntegrationSpringBatchFailureStepExecutionTest {

   /**
    * 
    */
   private static final String ERREUR_ROLLBACK = "Erreur rollback";

   private static final String MESSAGE_ERREUR = "erreur insertion";

   private static final String ERREUR_ATTENDUE = "Une erreur interne à l'application est "
         + "survenue lors du traitement du " + "document doc1.PDF. Détails : ";

   private static final String ERREUR_ATTENDUE_RES_003 = "La capture de masse en mode \"Tout ou rien\" a été interrompue. "
         + "Une procédure d'exploitation a été initialisée pour supprimer les données qui auraient pu être stockées.";

   private static final String ERREUR_ATTENDUE_003 = "Le traitement de masse "
         + "n°{} doit être rollbacké par une procédure d'exploitation";

   private static final String ERREUR_ATTENDUE_FIN_TRAITEMENT_003 = "Le traitement de masse n°{} doit éventuellement être rollbacké "
         + "par une procédure d'exploitation (il faut faire une analyse au préalable).";

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private SAECaptureMasseService service;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider provider;

   @Autowired
   private StepExecutionDao stepExecutionDao;

   private EcdeTestSommaire ecdeTestSommaire;

   private Logger logger;

   private SaeLogAppender logAppenderSae;

   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   @Before
   public void init() {
      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppenderSae = new SaeLogAppender(Level.WARN,
            "fr.urssaf.image.sae.services");
      logger.addAppender(logAppenderSae);

      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      logger.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());

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
      String[] roles = new String[] { "ROLE_archivage_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
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

   @After
   public void end() throws Exception {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      EasyMock.reset(provider, storageDocumentService);

      AuthenticationContext.setAuthenticationToken(null);

      logger.detachAppender(logAppenderSae);

      server.resetData(true);
   }

   @Test
   @DirtiesContext
   public void testLancementErreurAvantStockage() throws ConnectionServiceEx,
   DeletionServiceEx, InsertionServiceEx, IOException, JAXBException,
   SAXException {

      initComposantsSansRollback("compteElements");
      initDatas();

      ExitTraitement exitStatus = service.captureMasse(
            ecdeTestSommaire.getUrlEcde(), UUID.randomUUID());

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur",
            exitStatus.isSucces());

      checkFilesAvantStockage();

      checkLogsAvantStockage();

   }

   @Test
   @DirtiesContext
   public void testLancementErreurAvantDebutTraitement()
         throws ConnectionServiceEx, DeletionServiceEx, InsertionServiceEx,
         IOException, JAXBException, SAXException {

      initComposantsSansRollback("debutTraitement");
      initDatas();

      ExitTraitement exitStatus = service.captureMasse(
            ecdeTestSommaire.getUrlEcde(), UUID.randomUUID());

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur",
            exitStatus.isSucces());

      checkFilesAvantStockage();

      checkLogsAvantDebutTraitement();

   }

   @Test
   @DirtiesContext
   public void testLancementErreurApresPersistance()
         throws ConnectionServiceEx, DeletionServiceEx, InsertionServiceEx,
         IOException, JAXBException, SAXException, InsertionIdGedExistantEx {
      initSaveSansRollback();
      initComposantsSansRollback("finSuccesCapture");
      initDatas();

      UUID uuid = UUID.randomUUID();

      ExitTraitement exitStatus = service.captureMasse(
            ecdeTestSommaire.getUrlEcde(), uuid);

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur",
            exitStatus.isSucces());

      checkFilesApresStockage();

      checkLogsApresPersistance(uuid.toString());

   }

   @Test
   @DirtiesContext
   public void testLancementErreurApresRollbackErreur()
         throws ConnectionServiceEx, DeletionServiceEx, InsertionServiceEx,
         IOException, JAXBException, SAXException, InsertionIdGedExistantEx {

      initComposantsAvecRollback("finErreurCapture");
      initDatas();

      UUID uuid = UUID.randomUUID();

      ExitTraitement exitStatus = service.captureMasse(
            ecdeTestSommaire.getUrlEcde(), uuid);

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur",
            exitStatus.isSucces());

      checkFilesApresStockage();

      checkLogsApresRollBackErreur(uuid.toString());

   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initComposantsSansRollback(String stepToFail)
         throws ConnectionServiceEx, DeletionServiceEx, InsertionServiceEx {
      SaeStepExecutionDao dao = (SaeStepExecutionDao) stepExecutionDao;
      dao.setStepToFail(stepToFail);

      // // règlage provider
      provider.openConnexion();
      EasyMock.expectLastCall().anyTimes();
      provider.closeConnexion();
      EasyMock.expectLastCall().anyTimes();

      EasyMock.expect(provider.getStorageDocumentService())
      .andReturn(storageDocumentService).anyTimes();

      EasyMock.replay(provider, storageDocumentService);
   }

   private void initSaveSansRollback() throws InsertionServiceEx,
   InsertionIdGedExistantEx {
      UUID idTraitement = UUID.randomUUID();
      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(idTraitement);
      EasyMock
      .expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
                  .andReturn(storageDocument).times(10);
   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initComposantsAvecRollback(String stepToFail)
         throws ConnectionServiceEx, DeletionServiceEx, InsertionServiceEx,
         InsertionIdGedExistantEx {
      SaeStepExecutionDao dao = (SaeStepExecutionDao) stepExecutionDao;
      dao.setStepToFail(stepToFail);

      // // règlage provider
      provider.openConnexion();
      EasyMock.expectLastCall().anyTimes();
      provider.closeConnexion();
      EasyMock.expectLastCall().anyTimes();

      EasyMock.expect(provider.getStorageDocumentService())
      .andReturn(storageDocumentService).anyTimes();

      // règlage storageDocumentService
      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));
      EasyMock.expectLastCall().andThrow(new DeletionServiceEx(ERREUR_ROLLBACK)).once();

      UUID idTraitement = UUID.randomUUID();
      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(idTraitement);

      EasyMock
      .expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
                  .andReturn(storageDocument).times(9);

      EasyMock
      .expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
                  .andThrow(new InsertionServiceEx(MESSAGE_ERREUR)).once();

      JobExecution jobExecution = new JobExecution(1L);
      StepExecution execution = new StepExecution("controleSommaireStep",
            jobExecution);
      ExecutionContext executionContext = new ExecutionContext();
      executionContext.put(Constantes.DOC_COUNT, 10);
      executionContext.put(Constantes.CTRL_INDEX, 10);
      execution.setExecutionContext(executionContext);

      EasyMock.replay(provider, storageDocumentService);
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/springbatchFailureStepExecution/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/springbatchFailureStepExecution/documents/doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFilesAvantStockage() throws IOException, JAXBException,
   SAXException {

      File repTraitement = ecdeTestSommaire.getRepEcde();
      File debut = new File(repTraitement, "debut_traitement.flag");
      File fin = new File(repTraitement, "fin_traitement.flag");
      File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister",
            debut.exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister",
            fin.exists());
      Assert.assertTrue("le fichier resultats.xml doit exister",
            resultats.exists());

      ResultatsType res = getResultats(resultats);

      Assert.assertEquals("10 documents doivent être initialement présents",
            Integer.valueOf(10), res.getInitialDocumentsCount());
      Assert.assertEquals("10 documents doivent être rejetés",
            Integer.valueOf(10), res.getNonIntegratedDocumentsCount());
      Assert.assertEquals("0 documents doivent être intégrés",
            Integer.valueOf(0), res.getIntegratedDocumentsCount());
      Assert.assertEquals(
            "0 documents virtuels doivent être initialement présents",
            Integer.valueOf(0), res.getInitialVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être rejetés",
            Integer.valueOf(0), res.getNonIntegratedVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être intégrés",
            Integer.valueOf(0), res.getIntegratedVirtualDocumentsCount());

      boolean erreurFound = false;
      int index = 0;
      int indexErreur = 0;
      List<ErreurType> listeErreurs;
      List<NonIntegratedDocumentType> docs = res.getNonIntegratedDocuments()
            .getNonIntegratedDocument();
      ErreurType erreurType;
      while (!erreurFound && index < docs.size()) {

         if (CollectionUtils.isNotEmpty(docs.get(index).getErreurs()
               .getErreur())) {

            indexErreur = 0;
            listeErreurs = docs.get(index).getErreurs().getErreur();
            while (!erreurFound && indexErreur < listeErreurs.size()) {
               erreurType = listeErreurs.get(indexErreur);

               if (Constantes.ERR_BUL001.equals(erreurType.getCode())
                     && erreurType.getLibelle().contains(ERREUR_ATTENDUE)) {
                  erreurFound = true;
               }
               indexErreur++;
            }

         }

         index++;

      }

      Assert.assertTrue("le message d'erreur doit être trouvé", erreurFound);
   }

   private void checkFilesApresStockage() throws IOException, JAXBException,
   SAXException {

      File repTraitement = ecdeTestSommaire.getRepEcde();
      File debut = new File(repTraitement, "debut_traitement.flag");
      File fin = new File(repTraitement, "fin_traitement.flag");
      File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister",
            debut.exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister",
            fin.exists());
      Assert.assertTrue("le fichier resultats.xml doit exister",
            resultats.exists());

      ResultatsType res = getResultats(resultats);

      Assert.assertEquals("10 documents doivent être initialement présents",
            Integer.valueOf(10), res.getInitialDocumentsCount());
      Assert.assertEquals("10 documents doivent être rejetés",
            Integer.valueOf(10), res.getNonIntegratedDocumentsCount());
      Assert.assertEquals("0 documents doivent être intégrés",
            Integer.valueOf(0), res.getIntegratedDocumentsCount());
      Assert.assertEquals(
            "0 documents virtuels doivent être initialement présents",
            Integer.valueOf(0), res.getInitialVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être rejetés",
            Integer.valueOf(0), res.getNonIntegratedVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être intégrés",
            Integer.valueOf(0), res.getIntegratedVirtualDocumentsCount());

      boolean erreurFound = false;
      int index = 0;
      int indexErreur = 0;
      List<ErreurType> listeErreurs;
      List<NonIntegratedDocumentType> docs = res.getNonIntegratedDocuments()
            .getNonIntegratedDocument();
      ErreurType erreurType;
      while (!erreurFound && index < docs.size()) {

         if (CollectionUtils.isNotEmpty(docs.get(index).getErreurs()
               .getErreur())) {

            indexErreur = 0;
            listeErreurs = docs.get(index).getErreurs().getErreur();
            while (!erreurFound && indexErreur < listeErreurs.size()) {
               erreurType = listeErreurs.get(indexErreur);

               if (Constantes.ERR_BUL003.equals(erreurType.getCode())
                     && erreurType.getLibelle().contains(
                           ERREUR_ATTENDUE_RES_003)) {
                  erreurFound = true;
               }
               indexErreur++;
            }

         }

         index++;

      }

      Assert.assertTrue("le message d'erreur doit être trouvé", erreurFound);
   }

   /**
    * @param resultats
    * @throws JAXBException
    * @throws IOException
    * @throws SAXException
    */
   private ResultatsType getResultats(File resultats) throws JAXBException,
   IOException, SAXException {
      JAXBContext context = JAXBContext
            .newInstance(new Class[] { ObjectFactory.class });
      Unmarshaller unmarshaller = context.createUnmarshaller();

      final Resource classPath = applicationContext
            .getResource("classpath:xsd_som_res/resultats.xsd");
      URL xsdSchema;

      xsdSchema = classPath.getURL();

      // Affectation du schéma XSD si spécifié
      if (xsdSchema != null) {
         SchemaFactory schemaFactory = SchemaFactory
               .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
         Schema schema = schemaFactory.newSchema(xsdSchema);
         unmarshaller.setSchema(schema);
      }

      // Déclenche le unmarshalling
      @SuppressWarnings("unchecked")
      JAXBElement<ResultatsType> doc = (JAXBElement<ResultatsType>) unmarshaller
      .unmarshal(resultats);

      return doc.getValue();

   }

   private void checkLogsAvantStockage() {
      List<ILoggingEvent> loggingEvents = logAppenderSae.getLoggingEvents();

      Assert.assertNotNull("liste des messages non null", loggingEvents);

      Assert.assertTrue("au moins deux une trace SAE", loggingEvents.size() > 1);

      List<ILoggingEvent> list = LogUtils.getLogsByLevel(loggingEvents,
            Level.ERROR);

      Assert.assertEquals("deux messages de niveau ERROR", 2, list.size());

      // message resultats.xml
      boolean messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du fichier resultats.xml car il n'a pas été généré par le job de capture_masse. Détails :");

      Assert.assertTrue("présence du message de resultats.xml", messageOk);

      // message fin_traitement.flag
      messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du "
                  + "fichier fin_traitement.flag car il n'a "
                  + "pas été généré par le job de capture_masse");

      Assert.assertTrue("présence du message de fin_traitement.flag", messageOk);
   }

   private void checkLogsApresPersistance(String uuid) {
      List<ILoggingEvent> loggingEvents = logAppenderSae.getLoggingEvents();

      Assert.assertNotNull("liste des messages non null", loggingEvents);

      Assert.assertTrue("au moins quatre traces SAE", loggingEvents.size() > 3);

      List<ILoggingEvent> list = LogUtils.getLogsByLevel(loggingEvents,
            Level.ERROR);

      Assert.assertEquals("4 messages de niveau ERROR", 4, list.size());

      // messages de rollback
      boolean messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du log ERROR "
                  + "de rollback par procédure d'exploitation "
                  + "car il n'a pas été généré par le job de capture_masse");

      Assert.assertTrue("présence du message indiquant que "
            + "le rollback a été effectué en dehors du job", messageOk);

      messageOk = LogUtils.logExists(list, Level.ERROR,
            ERREUR_ATTENDUE_FIN_TRAITEMENT_003.replace("{}", uuid));

      Assert.assertTrue("présence du message indiquant que "
            + "le rollback doit être manuel", messageOk);

      // message resultats.xml
      messageOk = LogUtils.logExists(list, Level.ERROR, "BUL003");

      Assert.assertTrue("présence du message erreur avertissant "
            + "de l'arrêt impromptu du traitement", messageOk);

      // message fin_traitement.flag
      messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du "
                  + "fichier fin_traitement.flag car il n'a "
                  + "pas été généré par le job de capture_masse");

      Assert.assertTrue("présence du message de fin_traitement.flag", messageOk);

   }

   /**
    * 
    */
   private void checkLogsAvantDebutTraitement() {
      List<ILoggingEvent> loggingEvents = logAppenderSae.getLoggingEvents();

      Assert.assertNotNull("liste des messages non null", loggingEvents);

      Assert.assertTrue("au moins trois traces SAE", loggingEvents.size() > 2);

      List<ILoggingEvent> list = LogUtils.getLogsByLevel(loggingEvents,
            Level.ERROR);

      Assert.assertEquals("trois messages de niveau ERROR", 3, list.size());

      boolean messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du "
                  + "fichier debut_traitement.flag car il n'a "
                  + "pas été généré par le job de capture_masse");

      Assert.assertTrue(
            "présence du message pour le fichier debut_traitement.flag",
            messageOk);

      // message resultats.xml
      messageOk = LogUtils.logExists(list, Level.ERROR, "BUL001");

      Assert.assertTrue("présence du message pour le fichier resultats.xml",
            messageOk);

      // message fin_traitement.flag
      messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du "
                  + "fichier fin_traitement.flag car il n'a "
                  + "pas été généré par le job de capture_masse");

      Assert.assertTrue("présence du message de fin_traitement.flag", messageOk);

   }

   /**
    * @param string
    */
   private void checkLogsApresRollBackErreur(String uuid) {
      List<ILoggingEvent> loggingEvents = logAppenderSae.getLoggingEvents();

      Assert.assertNotNull("liste des messages non null", loggingEvents);

      Assert.assertTrue("au moins 3 traces SAE", loggingEvents.size() > 2);

      List<ILoggingEvent> list = LogUtils.getLogsByLevel(loggingEvents,
            Level.ERROR);

      Assert.assertEquals("3 messages de niveau ERROR", 3, list.size());

      // messages de rollback
      boolean messageOk = LogUtils.logExists(list, Level.ERROR,
            ERREUR_ATTENDUE_003.replace("{}", uuid));

      Assert.assertTrue("présence du message indiquant que "
            + "le rollback doit être manuel", messageOk);

      // message resultats.xml
      messageOk = LogUtils.logExists(list, Level.ERROR, "BUL003");

      Assert.assertTrue("présence du message erreur avertissant "
            + "de l'arrêt impromptu du traitement", messageOk);

      // message fin_traitement.flag
      messageOk = LogUtils.logExists(list, Level.ERROR,
            "Génération de secours du "
                  + "fichier fin_traitement.flag car il n'a "
                  + "pas été généré par le job de capture_masse");

      Assert.assertTrue("présence du message de fin_traitement.flag", messageOk);

   }

}
