package fr.urssaf.image.sae.services.consultation;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.SAEServiceTestProvider;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEConsultationServiceDroitsTest {

  @Autowired
  @Qualifier("saeConsultationService")
  private SAEConsultationService service;

  @Autowired
  @Qualifier("SAEServiceTestProvider")
  private SAEServiceTestProvider testProvider;

  private UUID uuid;

  @BeforeClass
  public static void beforeClass() throws IOException {
    ModeApiAllUtils.setAllModeAPIThrift();
  }

  @Before
  public void before() {

    // initialisation de l'uuid de l'archive
    uuid = null;

    // initialisation du contexte de sécurité
  }

  @After
  public void after() throws ConnectionServiceEx {

    // suppression de l'insertion
    if (uuid != null) {

      testProvider.deleteDocument(uuid);
    }

    // on vide le contexte de sécurité

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
    metadatas.put("ffi", "fmt/1354");
    metadatas.put("cse", "ATT_PROD_001");
    metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
    metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));
    metadatas.put("cot", Boolean.TRUE);

    final Date creationDate = DateUtils.parseDate("2012-01-01", parsePatterns);
    final Date dateDebutConservation = DateUtils.parseDate("2013-01-01",
                                                           parsePatterns);
    final String documentTitle = "attestation_consultation";
    final String documentType = "pdf";
    final String codeRND = "2.3.1.1.12";
    final String title = "Attestation de vigilance";
    return testProvider.captureDocument(content, metadatas, documentTitle,
                                        documentType, creationDate, dateDebutConservation, codeRND, title, null);
  }

  @Test(expected = AccessDeniedException.class)
  public void consultation_accessDenied() throws ConnectionServiceEx,
  IOException, ParseException, SAEConsultationServiceException,
  UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
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
    final String[] roles = new String[] { "ROLE_recherche" };
    saePrmds.add(saePrmd);

    saeDroits.put("recherche", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);
    uuid = capture();

    final ConsultParams consultParams = new ConsultParams(uuid);

    service.consultation(consultParams);
    Assert.fail("exception attendue");
  }

  @Test(expected = AccessDeniedException.class)
  public void consultation_accessMetaDataDenied() throws ConnectionServiceEx,
  IOException, ParseException, SAEConsultationServiceException,
  UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
    final VIContenuExtrait viExtrait = new VIContenuExtrait();
    viExtrait.setCodeAppli("TESTS_UNITAIRES");
    viExtrait.setIdUtilisateur("UTILISATEUR TEST");

    final SaeDroits saeDroits = new SaeDroits();
    final List<SaePrmd> saePrmds = new ArrayList<>();
    final SaePrmd saePrmd = new SaePrmd();
    saePrmd.setValues(new HashMap<String, String>());
    final Prmd prmd = new Prmd();
    prmd.setBean("permitNothing");
    prmd.setCode("default");
    saePrmd.setPrmd(prmd);
    final String[] roles = new String[] { "ROLE_consultation" };
    saePrmds.add(saePrmd);

    saeDroits.put("consultation", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);
    AuthenticationContext.getAuthenticationToken();
    uuid = capture();

    final ConsultParams consultParams = new ConsultParams(uuid);

    service.consultation(consultParams);
    Assert.fail("exception attendue");
  }

  @Test
  public void consultation_accessSuccess() throws ConnectionServiceEx,
  IOException, ParseException, SAEConsultationServiceException,
  UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
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
    final String[] roles = new String[] { "ROLE_consultation" };
    saePrmds.add(saePrmd);

    saeDroits.put("consultation", saePrmds);
    viExtrait.setSaeDroits(saeDroits);
    final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                 viExtrait.getIdUtilisateur(), viExtrait, roles);
    AuthenticationContext.setAuthenticationToken(token);
    AuthenticationContext.getAuthenticationToken();
    uuid = capture();

    final ConsultParams consultParams = new ConsultParams(uuid);

    final Map<String, String> values = new HashMap<>();
    values.put("CodeActivite", "3");
    values.put("VersionRND", "11.1");
    values.put("CodeFonction", "2");

    final List<String> codes = new ArrayList<>(values.keySet());
    consultParams.setMetadonnees(codes);
    final UntypedDocument document = service.consultation(consultParams);

    Assert.assertNotNull("le document existe", document);
    Assert.assertEquals("il doit y avoir seulement 3 métadonnées", 3,
                        document.getUMetadatas().size());
    for (final UntypedMetadata metadata : document.getUMetadatas()) {
      Assert.assertTrue("la métadonnée " + metadata.getLongCode()
      + " doit avoir été demandée", values.keySet().contains(
                                                             metadata.getLongCode()));
      Assert.assertEquals("la valeur stockée et retournée de la metadata "
          + metadata.getLongCode() + " doivent etre identiques", values
          .get(metadata.getLongCode()), metadata.getValue());
    }

  }

}
