package fr.urssaf.image.sae.services.enrichment.cql;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.rnd.dao.support.cql.RndCqlSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.AbstractServiceCqlTest;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.utils.MockFactoryBean;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })*/
public class SAEEnrichmentMetadataServiceImplCqlTest extends AbstractServiceCqlTest {

  @Autowired
  @Qualifier("saeEnrichmentMetadataService")
  private SAEEnrichmentMetadataService saeEnrichmentMetadataService;

  @Autowired
  @Qualifier("saeControlesCaptureService")
  private SAEControlesCaptureService controlesCaptureService;

  @Autowired
  private MappingDocumentService mappingService;

  @Autowired
  private RndCqlSupport rndCqlSupport;


  @Autowired
  private ParametersService parametersService;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;


  /**
   * @return Le service de mappingService
   */
  public final MappingDocumentService getMappingService() {
    return mappingService;
  }

  /**
   * @param mappingService
   *           : Le service de mappingService.
   */
  public final void setMappingService(final MappingDocumentService mappingService) {
    this.mappingService = mappingService;
  }

  /**
   * @return Le service d'enrichment des metadonnées.
   */
  public final SAEEnrichmentMetadataService getSaeEnrichmentMetadataService() {
    return saeEnrichmentMetadataService;
  }

  /**
   * @param saeEnrichmentMetadataService
   *           the saeEnrichmentMetadataService to set
   */
  public final void setSaeEnrichmentMetadataService(
                                                    final SAEEnrichmentMetadataService saeEnrichmentMetadataService) {
    this.saeEnrichmentMetadataService = saeEnrichmentMetadataService;
  }

  @BeforeClass
  public static void beforeClass() throws IOException {
    init = false;

  }

  /**
   * Préparation données pour le RND
   * 
   * @throws Exception
   * @throws InterruptedException
   */
  @Before
  public final void preparationDonnees() throws InterruptedException, Exception {
    initMetadata();
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
    final TypeDocument typeDocCree = new TypeDocument();
    typeDocCree.setCloture(false);
    typeDocCree.setCode("2.3.1.1.12");
    typeDocCree.setCodeActivite("3");
    typeDocCree.setCodeFonction("2");
    typeDocCree.setDureeConservation(1825);
    typeDocCree.setLibelle("Libellé 2.3.1.1.12");
    typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

    rndCqlSupport.ajouterRnd(typeDocCree);

    parametersService.setVersionRndDateMaj(new Date());
    parametersService.setVersionRndNumero("11.4");
  }

  /**
   * Test de la méthode
   * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
   * .
   */
  @Test
  public final void enrichmentMetadata() throws SAECaptureServiceEx,
  IOException, ParseException, SAEEnrichmentEx, InvalidSAETypeException,
  MappingFromReferentialException, ReferentialRndException,
  UnknownCodeRndEx, RequiredStorageMetadataEx {

    initDroits();

    final SAEDocument saeDocument = mappingService
        .untypedDocumentToSaeDocument(MockFactoryBean
                                      .getUntypedDocumentMockData());
    saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
    Assert.assertNotNull(saeDocument);
    controlesCaptureService.checkSaeMetadataForStorage(saeDocument);
  }

  /**
   * Test de la méthode
   * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
   * .
   */
  @Test(expected = UnknownCodeRndEx.class)
  public final void enrichmentMetadataFailed() throws SAECaptureServiceEx,
  IOException, ParseException, SAEEnrichmentEx, ReferentialRndException,
  UnknownCodeRndEx {
    final SAEDocument saeDocument = MockFactoryBean.getSAEDocumentMockData();
    for (final SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
      if (saeMetadata.getLongCode().equals(
                                           SAEArchivalMetadatas.CODE_RND.getLongCode())) {
        saeMetadata.setValue("121212");
        break;
      }
    }
    saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
  }

  @After
  public void end() throws Exception {
    AuthenticationContext.setAuthenticationToken(null);
    server.clearTables();
  }

  private void initDroits() {
    final VIContenuExtrait viExtrait = new VIContenuExtrait();
    viExtrait.setCodeAppli("TESTS_UNITAIRES");
    viExtrait.setIdUtilisateur("UTILISATEUR TEST");

    final SaeDroits saeDroits = new SaeDroits();
    final List<SaePrmd> saePrmds = new ArrayList<>();
    final SaePrmd saePrmd = new SaePrmd();
    saePrmd.setValues(new HashMap<String, String>());
    final Prmd prmd = new Prmd();
    prmd.setBean("permitAll");
    prmd.setCode("default");
    saePrmd.setPrmd(prmd);
    final String[] roles = new String[] { "ROLE_capture_masse" };
    saePrmds.add(saePrmd);

    saeDroits.put("capture_masse", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);
  }
}
