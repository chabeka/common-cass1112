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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
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
      File srcFile = new File(
            "src/test/resources/doc/attestation_consultation.pdf");

      byte[] content = FileUtils.readFileToByteArray(srcFile);

      String[] parsePatterns = new String[] { "yyyy-MM-dd" };
      Map<String, Object> metadatas = new HashMap<String, Object>();

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

      Date creationDate = DateUtils.parseDate("2012-01-01", parsePatterns);
      Date dateDebutConservation = DateUtils.parseDate("2013-01-01",
            parsePatterns);
      String documentTitle = "attestation_consultation";
      String documentType = "pdf";
      String codeRND = "2.3.1.1.12";
      String title = "Attestation de vigilance";
      return testProvider.captureDocument(content, metadatas, documentTitle,
            documentType, creationDate, dateDebutConservation, codeRND, title, null);
   }

   @Test(expected = AccessDeniedException.class)
   public void consultation_accessDenied() throws ConnectionServiceEx,
         IOException, ParseException, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
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
      String[] roles = new String[] { "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("recherche", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
      uuid = capture();

      ConsultParams consultParams = new ConsultParams(uuid);

      service.consultation(consultParams);
      Assert.fail("exception attendue");
   }

   @Test(expected = AccessDeniedException.class)
   public void consultation_accessMetaDataDenied() throws ConnectionServiceEx,
         IOException, ParseException, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitNothing");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("consultation", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
      AuthenticationContext.getAuthenticationToken();
      uuid = capture();

      ConsultParams consultParams = new ConsultParams(uuid);

      service.consultation(consultParams);
      Assert.fail("exception attendue");
   }

   @Test
   public void consultation_accessSuccess() throws ConnectionServiceEx,
         IOException, ParseException, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
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
      String[] roles = new String[] { "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("consultation", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
      AuthenticationContext.getAuthenticationToken();
      uuid = capture();

      ConsultParams consultParams = new ConsultParams(uuid);

      Map<String, String> values = new HashMap<String, String>();
      values.put("CodeActivite", "3");
      values.put("VersionRND", "11.1");
      values.put("CodeFonction", "2");

      List<String> codes = new ArrayList<String>(values.keySet());
      consultParams.setMetadonnees(codes);
      UntypedDocument document = service.consultation(consultParams);

      Assert.assertNotNull("le document existe", document);
      Assert.assertEquals("il doit y avoir seulement 3 métadonnées", 3,
            document.getUMetadatas().size());
      for (UntypedMetadata metadata : document.getUMetadatas()) {
         Assert.assertTrue("la métadonnée " + metadata.getLongCode()
               + " doit avoir été demandée", values.keySet().contains(
               metadata.getLongCode()));
         Assert.assertEquals("la valeur stockée et retournée de la metadata "
               + metadata.getLongCode() + " doivent etre identiques", values
               .get(metadata.getLongCode()), metadata.getValue());
      }

   }

}
