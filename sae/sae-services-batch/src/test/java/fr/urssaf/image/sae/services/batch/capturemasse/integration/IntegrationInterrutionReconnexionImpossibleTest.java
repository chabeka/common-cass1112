/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.commons.lang.time.DateUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
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
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.utils.LogUtils;
import fr.urssaf.image.sae.utils.SaeLogAppender;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                                   "/applicationContext-sae-services-batch-test.xml",
                                   "/applicationContext-sae-services-capturemasse-test-integration.xml",
                                   "/applicationContext-sae-services-capturemasse-test-mock-dfcemanager.xml"})
public class IntegrationInterrutionReconnexionImpossibleTest {

  /**
   *
   */
  private static final String ERREUR_CONNEXION = "erreur connexion";

  /**
   *
   */
  private static final String ERREUR_ATTENDUE = "La capture de masse en "
      + "mode \"Tout ou rien\" a été interrompue. "
      + "Une procédure d'exploitation a été initialisée pour supprimer les "
      + "données qui auraient pu être stockées.";

  private static final String LOG_ERROR = "Le traitement de masse "
      + "n°{} doit faire l'objet d'une reprise par une procédure d'exploitation";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private SAECaptureMasseService service;

  @Autowired
  private EcdeTestTools ecdeTestTools;

  private static final String FORMAT_DATE = "HH:mm:ss";

  @Autowired
  @Qualifier("storageDocumentService")
  private StorageDocumentService storageDocumentService;

  @Autowired
  private DFCEServices dfceServices;

  @Autowired
  @Qualifier("interruption_traitement_masse")
  private InterruptionTraitementConfig config;

  @Autowired
  @Qualifier("storageServiceProvider")
  private StorageServiceProvider provider;

  @Autowired
  private ParametersService parametersService;

  @Autowired
  private RndSupport rndSupport;

  @Autowired
  private JobClockSupport jobClockSupport;

  private EcdeTestSommaire ecdeTestSommaire;

  private Logger logger;

  private SaeLogAppender logAppender;

  @Before
  public void init() {
    logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    logAppender = new SaeLogAppender(Level.WARN, "fr.urssaf.image.sae");
    logger.addAppender(logAppender);

    ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

    logger.debug("initialisation du répertoire de traitetement :"
        + ecdeTestSommaire.getRepEcde());

    // initialisation du contexte de sécurité
    final VIContenuExtrait viExtrait = new VIContenuExtrait();
    viExtrait.setCodeAppli("TESTS_UNITAIRES");
    viExtrait.setIdUtilisateur("UTILISATEUR TEST");
    viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

    final SaeDroits saeDroits = new SaeDroits();
    final List<SaePrmd> saePrmds = new ArrayList<>();
    final SaePrmd saePrmd = new SaePrmd();
    saePrmd.setValues(new HashMap<String, String>());
    final Prmd prmd = new Prmd();
    prmd.setBean("permitAll");
    prmd.setCode("default");
    saePrmd.setPrmd(prmd);
    final String[] roles = new String[] {"ROLE_archivage_masse"};
    saePrmds.add(saePrmd);

    saeDroits.put("archivage_masse", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(),
                                                                                 viExtrait,
                                                                                 roles);
    AuthenticationContext.setAuthenticationToken(token);

    // Paramétrage du RND
    parametersService.setVersionRndDateMaj(new Date());
    parametersService.setVersionRndNumero("11.2");

    final TypeDocument typeDocCree = new TypeDocument();
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
  public void end() {
    try {
      ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
    }
    catch (final IOException e) {
      // rien a faire
    }

    EasyMock.reset(provider, storageDocumentService, dfceServices);

    logger.detachAppender(logAppender);
  }

  @Test
  @DirtiesContext
  @Ignore("test trop long (temps d'attente entre deux essais de reco = 120 secondes)")
  public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
      InsertionServiceEx, IOException, MetaDataUnauthorizedToSearchEx,
      MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
      UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx,
      JAXBException, SAXException, InsertionIdGedExistantEx {
    initComposants();
    initDatas();

    final UUID uuid = UUID.randomUUID();

    final ExitTraitement exitStatus = service.captureMasse(
                                                           ecdeTestSommaire.getUrlEcde(),
                                                           uuid);

    EasyMock.verify(provider, storageDocumentService, dfceServices);

    Assert.assertFalse("le traitement doit etre en erreur",
                       exitStatus.isSucces());

    checkFiles();

    checkLogs(uuid.toString());

  }

  private void initComposants() throws ConnectionServiceEx, DeletionServiceEx,
      InsertionServiceEx, MetaDataUnauthorizedToSearchEx,
      MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
      UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx,
      InsertionIdGedExistantEx {

    // configuration de l'interruption
    final Date dateNow = new Date();
    final Date dateStart = DateUtils.addMinutes(dateNow, -1);
    final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
    final String value = sdf.format(dateStart);
    config.setStart(value);
    config.setDelay(70);

    dfceServices.reconnect();
    EasyMock.expectLastCall().andThrow(new Exception(ERREUR_CONNEXION));

    // règlage provider
    provider.openConnexion();
    EasyMock.expectLastCall().anyTimes();
    provider.closeConnexion();
    EasyMock.expectLastCall().anyTimes();
    EasyMock.expect(provider.getStorageDocumentService())
            .andReturn(storageDocumentService)
            .anyTimes();

    // règlage storageDocumentService
    final StorageDocument storageDocument = new StorageDocument();
    storageDocument.setUuid(UUID.randomUUID());

    // simulation de la non intégration d'un seul document
    EasyMock
            .expect(
                    storageDocumentService.insertStorageDocument(EasyMock
                                                                         .anyObject(StorageDocument.class)))
            .andReturn(storageDocument)
            .anyTimes();

    EasyMock.replay(provider, storageDocumentService, dfceServices);
  }

  private void initDatas() throws IOException {
    final File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
    final ClassPathResource resSommaire = new ClassPathResource(
                                                                "testhautniveau/rollBack0DocRechercheSucces/sommaire.xml");
    FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

    final File origine = new File(ecdeTestSommaire.getRepEcde(), "documents");
    final String resourceString = "testhautniveau/rollBack0DocRechercheSucces/documents/doc1.PDF";
    final ClassPathResource resource = new ClassPathResource(resourceString);
    final File attestation = new File(origine, "doc1.PDF");
    FileUtils.copyURLToFile(resource.getURL(), attestation);
  }

  private void checkFiles() throws IOException, JAXBException, SAXException {

    final File repTraitement = ecdeTestSommaire.getRepEcde();
    final File debut = new File(repTraitement, "debut_traitement.flag");
    final File fin = new File(repTraitement, "fin_traitement.flag");
    final File resultats = new File(repTraitement, "resultats.xml");

    Assert.assertTrue("le fichier debut_traitement.flag doit exister",
                      debut.exists());
    Assert.assertTrue("le fichier fin_traitement.flag doit exister",
                      fin.exists());
    Assert.assertTrue("le fichier resultats.xml doit exister",
                      resultats.exists());

    final ResultatsType res = getResultats(resultats);

    Assert.assertEquals("10 documents doivent être initialement présents",
                        Integer.valueOf(10),
                        res.getInitialDocumentsCount());
    Assert.assertEquals("10 documents doivent être rejetés",
                        Integer.valueOf(10),
                        res.getNonIntegratedDocumentsCount());
    Assert.assertEquals("0 documents doivent être intégrés",
                        Integer.valueOf(0),
                        res.getIntegratedDocumentsCount());
    Assert.assertEquals(
                        "0 documents virtuels doivent être initialement présents",
                        Integer.valueOf(0),
                        res.getInitialVirtualDocumentsCount());
    Assert.assertEquals("0 documents virtuels doivent être rejetés",
                        Integer.valueOf(0),
                        res.getNonIntegratedVirtualDocumentsCount());
    Assert.assertEquals("0 documents virtuels doivent être intégrés",
                        Integer.valueOf(0),
                        res.getIntegratedVirtualDocumentsCount());

    boolean erreurFound = false;
    int index = 0;
    int indexErreur = 0;
    List<ErreurType> listeErreurs;
    final List<NonIntegratedDocumentType> docs = res.getNonIntegratedDocuments()
                                                    .getNonIntegratedDocument();
    ErreurType erreurType;
    while (!erreurFound && index < docs.size()) {

      if (CollectionUtils.isNotEmpty(docs.get(index)
                                         .getErreurs()
                                         .getErreur())) {

        indexErreur = 0;
        listeErreurs = docs.get(index).getErreurs().getErreur();
        while (!erreurFound && indexErreur < listeErreurs.size()) {
          erreurType = listeErreurs.get(indexErreur);

          if (Constantes.ERR_BUL003.equals(erreurType.getCode())
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

  /**
   * @param resultats
   * @throws JAXBException
   * @throws IOException
   * @throws SAXException
   */
  private ResultatsType getResultats(final File resultats) throws JAXBException,
      IOException, SAXException {
    final JAXBContext context = JAXBContext
                                           .newInstance(new Class[] {ObjectFactory.class});
    final Unmarshaller unmarshaller = context.createUnmarshaller();

    final Resource classPath = applicationContext
                                                 .getResource("classpath:xsd_som_res/resultats.xsd");
    URL xsdSchema;

    xsdSchema = classPath.getURL();

    // Affectation du schéma XSD si spécifié
    if (xsdSchema != null) {
      final SchemaFactory schemaFactory = SchemaFactory
                                                       .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      final Schema schema = schemaFactory.newSchema(xsdSchema);
      unmarshaller.setSchema(schema);
    }

    // Déclenche le unmarshalling
    @SuppressWarnings("unchecked")
    final JAXBElement<ResultatsType> doc = (JAXBElement<ResultatsType>) unmarshaller
                                                                                    .unmarshal(resultats);

    return doc.getValue();

  }

  private void checkLogs(final String uuid) {
    final List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

    Assert.assertNotNull("liste des messages non null", loggingEvents);

    Assert.assertTrue("au moins un log", loggingEvents.size() > 0);

    final List<ILoggingEvent> errors = LogUtils.getLogsByLevel(loggingEvents,
                                                               Level.ERROR);

    Assert.assertEquals("un seul message de niveau ERREUR attendu",
                        1,
                        errors.size());

    final boolean messageFound = LogUtils.logContainsMessage(errors.get(0),
                                                             LOG_ERROR.replace("{}", uuid));

    Assert.assertTrue("message d'erreur correct", messageFound);

  }

}
