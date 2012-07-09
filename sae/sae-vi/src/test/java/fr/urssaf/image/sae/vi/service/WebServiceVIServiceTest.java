package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService;
import fr.urssaf.image.sae.vi.exception.VIFormatTechniqueException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VISignatureException;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.testutils.TuGenererVi;
import fr.urssaf.image.sae.vi.testutils.TuUtils;
import fr.urssaf.image.sae.vi.util.XMLUtils;

@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.TooManyMethods",
      "PMD.ExcessiveImports" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class WebServiceVIServiceTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(WebServiceVIServiceTest.class);

   @Autowired
   private WebServiceVIService service;

   private static SamlAssertionExtractionService extraction;

   @BeforeClass
   public static void beforeClass() {

      extraction = new SamlAssertionExtractionService();

   }

   private KeyStore keystore;

   private String alias;

   @Before
   public void before() throws KeyStoreException, NoSuchAlgorithmException,
         CertificateException, IOException {

      keystore = KeyStoreFactory.createKeystore();
      alias = keystore.aliases().nextElement();
   }

   @Test
   public void creerVIpourServiceWeb_success_idNotEmpty() throws SAXException {

      assertCreerVIpourServiceWeb(TuGenererVi.ID_UTILISATEUR,
            TuGenererVi.ID_UTILISATEUR);
   }

   @Test
   public void creerVIpourServiceWeb_success_idEmpty() throws SAXException {

      assertCreerVIpourServiceWeb("NON_RENSEIGNE", null);

   }

   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   private void assertCreerVIpourServiceWeb(String idExpected, String idActual)
         throws SAXException {

      List<String> pagm = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

      String password = "hiUnk6O3QnRN";

      Element assertion = service.creerVIpourServiceWeb(pagm,
            TuGenererVi.ISSUER, idActual, keystore, alias, password);

      LOG.debug("\n" + XMLUtils.print(assertion));

      SamlAssertionData data = extraction.extraitDonnees(assertion);

      assertNotNull(data.getAssertionParams().getCommonsParams().getId());
      assertNotNull(data.getAssertionParams().getCommonsParams()
            .getIssueInstant());
      assertEquals(TuGenererVi.ISSUER, data.getAssertionParams()
            .getCommonsParams().getIssuer());

      long diff = data.getAssertionParams().getCommonsParams()
            .getNotOnOrAfter().getTime()
            - data.getAssertionParams().getCommonsParams().getNotOnBefore()
                  .getTime();

      assertEquals(Long.valueOf(2 * 3600 * 1000), Long.valueOf(diff));
      assertEquals("http://sae.urssaf.fr", data.getAssertionParams()
            .getCommonsParams().getAudience().toASCIIString());
      assertNotNull(data.getAssertionParams().getCommonsParams()
            .getAuthnInstant());
      assertEquals("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified",
            data.getAssertionParams().getSubjectFormat2().toASCIIString());
      assertEquals(idExpected, data.getAssertionParams().getSubjectId2());
      assertEquals("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified", data
            .getAssertionParams().getMethodAuthn2().toASCIIString());
      assertEquals("urn:URSSAF", data.getAssertionParams().getRecipient()
            .toASCIIString());

   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_success() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_success.xml");

      VIContenuExtrait extrait = service.verifierVIdeServiceWeb(identification,
            TuGenererVi.SERVICE_VISE, TuUtils.buildSignVerifParamsOK());

      assertEquals(TuGenererVi.ID_UTILISATEUR, extrait.getIdUtilisateur());
      // FIXME
      // assertEquals(2,extrait.getPagm().size());
      // assertEquals("DROIT_APPLICATIF_1",extrait.getPagm().get(0).getDroitApplicatif());
      // assertEquals("PERIMETRE_DONNEES_1",extrait.getPagm().get(0).getPerimetreDonnees());
      // assertEquals("DROIT_APPLICATIF_2",extrait.getPagm().get(1).getDroitApplicatif());
      // assertEquals("PERIMETRE_DONNEES_2",extrait.getPagm().get(1).getPerimetreDonnees());

      assertEquals("Portail Image", extrait.getCodeAppli());

   }

   @Test(expected = VIFormatTechniqueException.class)
   public void verifierVIdeServiceWeb_failure_format() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_format.xml");

      service.verifierVIdeServiceWeb(identification, TuGenererVi.SERVICE_VISE,
            new VISignVerifParams());

   }

   @Test(expected = VISignatureException.class)
   public void verifierVIdeServiceWeb_failure_sign() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_sign.xml");

      service.verifierVIdeServiceWeb(identification, TuGenererVi.SERVICE_VISE,
            new VISignVerifParams());

   }

   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_failure_id_1() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_id_1.xml");

      try {

         service.verifierVIdeServiceWeb(identification,
               TuGenererVi.SERVICE_VISE, TuUtils.buildSignVerifParamsOK());

         fail("Une exception de type VIInvalideException était attendue");

      } catch (VIInvalideException ex) {

         assertEquals(
               "Vérification du message de l'exception",
               "L'ID de l'assertion doit être un UUID correct (ce qui n'est pas le cas de 'bad id')",
               ex.getMessage());

      }

   }

   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_failure_id_2() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_id_2.xml");

      try {

         service.verifierVIdeServiceWeb(identification,
               TuGenererVi.SERVICE_VISE, TuUtils.buildSignVerifParamsOK());

         fail("Une exception de type VIInvalideException était attendue");

      } catch (VIInvalideException ex) {

         assertEquals(
               "Vérification du message de l'exception",
               "L'ID de l'assertion doit être un UUID correct (ce qui n'est pas le cas de 'pfx5d541dee-4468-74d2-7cbe-03078ef284e7')",
               ex.getMessage());

      }

   }

}
