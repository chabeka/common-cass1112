package fr.urssaf.image.sae.services.documentExistent;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEDocumentExistantServiceTest {

  @Autowired
  private SAEDocumentExistantService documentExistant;

  @Autowired
  @Qualifier("SAEServiceTestProvider")
  private SAEServiceTestProvider testProvider;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private ParametersService parametersService;

  @Autowired
  private RndSupport rndSupport;

  @Autowired
  private JobClockSupport jobClockSupport;

  private UUID uuid;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;


  @Before
  public void before() throws Exception {
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.HECTOR);
    // initialisation de l'uuid de l'archive
    uuid = null;

    // initialisation du contexte de sécurité
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
    final String[] roles = new String[] { "ROLE_documentExistant" };
    saePrmds.add(saePrmd);

    saeDroits.put("documentExistant", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);

    // Paramétrage du RND

    server.resetData(true, MODE_API.HECTOR);
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
  public void after() throws Exception {

    // suppression de l'insertion
    if (uuid != null) {

      testProvider.deleteDocument(uuid);
    }

    // on vide le contexte de sécurité
    AuthenticationContext.setAuthenticationToken(null);

    server.resetData(true, MODE_API.HECTOR);
  }

  private UUID capture() throws IOException, ConnectionServiceEx,
  ParseException {
    final File srcFile = new File(
        "src/test/resources/doc/attestation_consultation.pdf");

    final byte[] content = FileUtils.readFileToByteArray(srcFile);

    final String[] parsePatterns = new String[] { "yyyy-MM-dd" };
    final Map<String, Object> metadatas = new HashMap<>();

    metadatas.put("apr", "ADELAIDE");
    metadatas.put("cop", "CER69");
    metadatas.put("cog", "UR750");
    metadatas.put("vrn", "11.1");
    metadatas.put("dom", "2");
    metadatas.put("act", "3");
    metadatas.put("nbp", "2");
    metadatas.put("ffi", "fmt/354");
    metadatas.put("cse", "ATT_PROD_001");
    metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
    metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));
    metadatas.put("cot", Boolean.TRUE);

    final Date creationDate = DateUtils.parseDate("2012-01-01", parsePatterns);
    final Date dateDebutConservation = DateUtils.parseDate("2013-01-01",
                                                           parsePatterns);
    final String documentTitle = "attestation_consultation";
    final String documentType = "pdf";
    final String codeRND = "7.7.8.8.1";
    final String title = "Attestation de vigilance";
    final String note = "note du document";
    return testProvider.captureDocument(content, metadatas, documentTitle,
                                        documentType, creationDate, dateDebutConservation, codeRND, title,
                                        note);
  }

  @Test
  public void documentExistant__true_success() throws ConnectionServiceEx,
  SearchingServiceEx, IOException, ParseException {
    uuid = capture();
    final boolean res = documentExistant.documentExistant(uuid);
    assertEquals("Le document existe donc renvoie true", true, res);

  }

  @Test
  public void documentExistant_false_success() throws ConnectionServiceEx,
  SearchingServiceEx {
    uuid = UUID.fromString("C675CED1-6ACE-463E-BA58-725A103A320B");
    final boolean res = documentExistant.documentExistant(uuid);
    assertEquals("Le document n'existe pas donc renvoie false", false, res);
    uuid = null;

  }
}
