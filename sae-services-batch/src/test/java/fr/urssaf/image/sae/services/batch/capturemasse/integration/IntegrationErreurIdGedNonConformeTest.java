/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.integration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.util.Nodes;
import org.xmlunit.util.Predicate;

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
import fr.urssaf.image.sae.services.batch.capturemasse.utils.TraceAssertUtils;
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
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                                   "/applicationContext-sae-services-batch-test.xml",
                                   "/applicationContext-sae-services-capturemasse-test-integration.xml"})
public class IntegrationErreurIdGedNonConformeTest {

  private static final String LOG_WARN = "Une erreur est survenue lors de contrôle des documents";

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

  @Autowired
  private TraceAssertUtils traceAssertUtils;

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
    final String[] roles = new String[] {"archivage_masse"};
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
  public void end() throws Exception {
    try {
      ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
    }
    catch (final IOException e) {
      // rien a faire
    }

    EasyMock.reset(provider, storageDocumentService);

    AuthenticationContext.setAuthenticationToken(null);

    logger.detachAppender(logAppender);

    server.resetData();
  }

  @Test
  @DirtiesContext
  public void testLancement() throws ConnectionServiceEx, DeletionServiceEx,
      InsertionServiceEx, IOException, InsertionIdGedExistantEx {
    initComposants();
    initDatas();

    final UUID idTdm = UUID.randomUUID();
    final URI urlSommaire = ecdeTestSommaire.getUrlEcde();

    final ExitTraitement exitStatus = service.captureMasse(urlSommaire, idTdm);

    EasyMock.verify(provider, storageDocumentService);

    Assert.assertFalse("le traitement doit etre en erreur",
                       exitStatus
                                 .isSucces());

    checkFiles();

    checkLogs();

    checkTracabilite(idTdm, urlSommaire);

  }

  private void initComposants() throws ConnectionServiceEx, DeletionServiceEx,
      InsertionServiceEx, InsertionIdGedExistantEx {

    // règlage provider
    provider.openConnexion();
    EasyMock.expectLastCall().anyTimes();
    provider.closeConnexion();
    EasyMock.expectLastCall().anyTimes();
    EasyMock.expect(provider.getStorageDocumentService())
            .andReturn(
                       storageDocumentService)
            .anyTimes();

    final StorageDocument storageDocument = new StorageDocument();
    storageDocument.setUuid(UUID.randomUUID());

    EasyMock.expect(
                    storageDocumentService.insertStorageDocument(EasyMock
                                                                         .anyObject(StorageDocument.class)))
            .andReturn(storageDocument)
            .anyTimes();

    EasyMock.replay(provider, storageDocumentService);
  }

  private void initDatas() throws IOException {
    final File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
    final ClassPathResource resSommaire = new ClassPathResource(
                                                                "testhautniveau/erreurIdGedNonConforme/sommaire.xml");
    FileUtils.copyURLToFile(resSommaire.getURL(), sommaire);

    final File repEcde = new File(ecdeTestSommaire.getRepEcde(), "documents");
    final ClassPathResource resAttestation1 = new ClassPathResource(
                                                                    "testhautniveau/erreurIdGedNonConforme/documents/doc1.PDF");
    final File fileAttestation1 = new File(repEcde, "doc1.PDF");
    FileUtils.copyURLToFile(resAttestation1.getURL(), fileAttestation1);

  }

  private void checkFiles() throws IOException {

    final File repTraitement = ecdeTestSommaire.getRepEcde();
    final File debut = new File(repTraitement, "debut_traitement.flag");
    final File fin = new File(repTraitement, "fin_traitement.flag");
    final File resultats = new File(repTraitement, "resultats.xml");
    final ClassPathResource resultatAttendu = new ClassPathResource(
                                                                    "testhautniveau/erreurIdGedNonConforme/resultats-attendu.xml");

    Assert.assertTrue("le fichier debut_traitement.flag doit exister",
                      debut
                           .exists());
    Assert.assertTrue("le fichier fin_traitement.flag doit exister",
                      fin
                         .exists());
    Assert.assertTrue("le fichier resultats.xml doit exister",
                      resultats
                               .exists());

    final Diff diff = DiffBuilder.compare(Input.from(resultats))
                                 .withTest(Input.from(resultatAttendu.getFile()))
                                 .checkForSimilar()
                                 .withNodeFilter(new Predicate<Node>() {
                                   @Override
                                   public boolean test(final Node n) {
                                     return !(n instanceof Element &&
                                         "uuid".equals(Nodes.getQName(n).getLocalPart()));
                                   }
                                 })
                                 .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.or(ElementSelectors.byNameAndText,
                                                                                             ElementSelectors.byName)))
                                 .ignoreWhitespace()
                                 .ignoreComments()
                                 .build();

    Assert.assertFalse(diff.hasDifferences());

  }

  private void checkLogs() {
    final List<ILoggingEvent> loggingEvents = logAppender.getLoggingEvents();

    Assert.assertNotNull("liste des messages non null", loggingEvents);

    Assert.assertEquals("un message attendu", 7, loggingEvents.size());

    final ILoggingEvent event = loggingEvents.get(0);

    Assert.assertEquals("le log doit être de niveau WARN",
                        Level.WARN,
                        event.getLevel());

    final boolean messageFound = LogUtils.logContainsMessage(event, LOG_WARN);
    Assert.assertTrue("le message d'erreur attendu doit être correct",
                      messageFound);
  }

  private void checkTracabilite(final UUID idTdm, final URI urlSommaire) {
    final String traceValue = "Erreur de parsing de l'UUID du document car la syntax ne respecte pas la nomenclature standard : ";
    final List<String> traceValueList = Arrays.asList(traceValue + "'0c3a2f90-4064-41d6-8da5-1e9a5d011d5bnva'",
                                                      traceValue + "'6983a3f3-fcea-45c0-b5e4-e3ba77d6ccf1nva'",
                                                      traceValue + "'68f20eb9-b998-416c-9951-970af74ffae1nva'",
                                                      traceValue + "'74a8ee88-f01f-4499-8042-18740996aaddcmo'",
                                                      traceValue + "'fe6a8895-74db-44b2-a2f7-c410fb21e290nva'");
    traceAssertUtils
                    .verifieTraceCaptureMasseDansRegTechnique(
                                                              idTdm,
                                                              urlSommaire,
                                                              traceValueList);

  }

}
