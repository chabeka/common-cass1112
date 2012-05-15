/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
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
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.utils.SaeLogAppender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-test.xml",
      "/applicationContext-sae-traitement-masse-dao-spring.xml" })
public class IntegrationSpringBatchFailureTest {

   private static final String MESSAGE_ERREUR = "erreur insertion";

   private static final String ERREUR_ATTENDUE = "Une erreur interne à l'application est "
         + "survenue lors de la capture du "
         + "document doc1.PDF. Détails : "
         + MESSAGE_ERREUR;

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

   @Before
   public void init() {
      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppenderSae = new SaeLogAppender(Level.WARN,
            "fr.urssaf.image.sae.services");
      logger.addAppender(logAppenderSae);

      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      logger.debug("initialisation du répertoire de traitetement :"
            + ecdeTestSommaire.getRepEcde());
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      EasyMock.reset(provider, storageDocumentService, stepExecutionDao);

      logger.detachAppender(logAppenderSae);
   }

   @Test
   @DirtiesContext
   @Ignore
   public void testLancementErreurAvantStockage() throws ConnectionServiceEx,
         DeletionServiceEx, InsertionServiceEx, IOException, JAXBException,
         SAXException {

      initComposantsAvantStockage();
      initDatas();

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), UUID.randomUUID());

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur", exitStatus
            .isSucces());

      checkFiles();

      checkLogsAvantStockage();

   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initComposantsAvantStockage() throws ConnectionServiceEx,
         DeletionServiceEx, InsertionServiceEx {

      // // règlage provider
      // provider.openConnexion();
      // EasyMock.expectLastCall().once();
      // provider.closeConnexion();
      // EasyMock.expectLastCall().once();

      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            storageDocumentService).anyTimes();

      // règlage storageDocumentService
      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));
      EasyMock.expectLastCall().anyTimes();

      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andThrow(
            new Error(MESSAGE_ERREUR)).anyTimes();

      // réglage sauvegarde spring
      stepExecutionDao
            .addStepExecutions(EasyMock.anyObject(JobExecution.class));
      EasyMock.expectLastCall().once();
      EasyMock.expectLastCall().andThrow(new Error("erreur stepExecution"));

      EasyMock.replay(provider, storageDocumentService, stepExecutionDao);
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/springbatchFailure/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/springbatchFailure/documents/doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

   }

   private void checkFiles() throws IOException, JAXBException, SAXException {

      File repTraitement = ecdeTestSommaire.getRepEcde();
      File debut = new File(repTraitement, "debut_traitement.flag");
      File fin = new File(repTraitement, "fin_traitement.flag");
      File resultats = new File(repTraitement, "resultats.xml");

      Assert.assertTrue("le fichier debut_traitement.flag doit exister", debut
            .exists());
      Assert.assertTrue("le fichier fin_traitement.flag doit exister", fin
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());

      ResultatsType res = getResultats(resultats);

      Assert.assertEquals("10 documents doivent être initialement présents",
            Integer.valueOf(10), res.getInitialDocumentsCount());
      Assert.assertEquals("10 documents doivent être rejetés", Integer
            .valueOf(10), res.getNonIntegratedDocumentsCount());
      Assert.assertEquals("0 documents doivent être intégrés", Integer
            .valueOf(0), res.getIntegratedDocumentsCount());
      Assert.assertEquals(
            "0 documents virtuels doivent être initialement présents", Integer
                  .valueOf(0), res.getInitialVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être rejetés", Integer
            .valueOf(0), res.getNonIntegratedVirtualDocumentsCount());
      Assert.assertEquals("0 documents virtuels doivent être intégrés", Integer
            .valueOf(0), res.getIntegratedVirtualDocumentsCount());

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
                     && ERREUR_ATTENDUE.equalsIgnoreCase(erreurType
                           .getLibelle())) {
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

      Assert.assertEquals("une seule trace SAE", 1, loggingEvents.size());

      Assert.assertEquals("l'erreur doit etre de niveau WARN", Level.WARN,
            loggingEvents.get(0).getLevel());

      Assert.assertEquals("le message d'exception doit etre valide",
            MESSAGE_ERREUR, loggingEvents.get(0).getThrowableProxy()
                  .getMessage());
   }

}
