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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
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
import fr.urssaf.image.sae.utils.LogUtils;
import fr.urssaf.image.sae.utils.SaeLogAppender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-test.xml",
      "/applicationContext-sae-services-integration-test.xml" })
public class IntegrationRollbackSuccessTest {

   private static final String ERREUR_ATTENDUE = "Une erreur interne à l'application est "
         + "survenue lors de la capture du "
         + "document doc1.PDF. Détails : erreur mémoire";

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

   private EcdeTestSommaire ecdeTestSommaire;

   private Logger logger;

   private SaeLogAppender logAppender;

   @Before
   public void init() {
      logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      logAppender = new SaeLogAppender(Level.WARN, "fr.urssaf.image.sae");
      logger.addAppender(logAppender);

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

      EasyMock.reset(provider, storageDocumentService);

      logger.detachAppender(logAppender);
   }

   @Test
   @DirtiesContext
   public void testLancementThrowable() throws ConnectionServiceEx,
         DeletionServiceEx, InsertionServiceEx, IOException, JAXBException,
         SAXException {
      initThrowable();
      initGeneral();
      initDatas();

      UUID uuid = UUID.randomUUID();

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), uuid);

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur", exitStatus
            .isSucces());

      checkFiles();

      checkLogs(uuid.toString());

   }

   @Test
   @DirtiesContext
   public void testLancementRuntime() throws ConnectionServiceEx,
         DeletionServiceEx, InsertionServiceEx, IOException, JAXBException,
         SAXException {
      initRuntime();
      initGeneral();
      initDatas();

      UUID uuid = UUID.randomUUID();

      ExitTraitement exitStatus = service.captureMasse(ecdeTestSommaire
            .getUrlEcde(), uuid);

      EasyMock.verify(provider, storageDocumentService);

      Assert.assertFalse("le traitement doit etre en erreur", exitStatus
            .isSucces());

      checkFiles();

      checkLogs(uuid.toString());

   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initGeneral() throws ConnectionServiceEx, DeletionServiceEx {
      // règlage provider
      provider.openConnexion();
      EasyMock.expectLastCall().anyTimes();
      provider.closeConnexion();
      EasyMock.expectLastCall().anyTimes();
      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            storageDocumentService).anyTimes();

      // règlage storageDocumentService
      storageDocumentService.deleteStorageDocument(EasyMock
            .anyObject(UUID.class));
      EasyMock.expectLastCall().anyTimes();

      EasyMock.replay(provider, storageDocumentService);

   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initThrowable() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx {
      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());
      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument).times(2);

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andThrow(
            new Error("erreur mémoire")).once();

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument).anyTimes();
   }

   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   private void initRuntime() throws ConnectionServiceEx, DeletionServiceEx,
         InsertionServiceEx {
      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());
      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument).times(2);

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andThrow(
            new RuntimeException("erreur mémoire")).once();

      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument).anyTimes();
   }

   private void initDatas() throws IOException {
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "testhautniveau/rollbackSuccess/sommaire.xml");
      FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

      File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
      ClassPathResource resAttestation1 = new ClassPathResource(
            "testhautniveau/rollbackSuccess/documents/doc1.PDF");
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

   private void checkLogs(String uuid) {
      List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

      Assert.assertNotNull("liste des messages non null", loggingEvents);

      Assert
            .assertTrue("Au moins un message présent", loggingEvents.size() > 0);

      int nbreErreur = LogUtils.countLogsWithLevel(loggingEvents, Level.ERROR);

      Assert.assertEquals("aucun message de niveau error", 0, nbreErreur);

       boolean messageFound = LogUtils.logExists(loggingEvents, Level.WARN, "erreur mémoire");
      
      Assert.assertTrue("le message d'erreur doit être trouvé", messageFound);

   }

}
