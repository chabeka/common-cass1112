package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService;
import fr.urssaf.image.sae.vi.exception.VIFormatTechniqueException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VIPagmIncorrectException;
import fr.urssaf.image.sae.vi.exception.VISignatureException;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIPagm;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.testutils.TuGenererVi;
import fr.urssaf.image.sae.vi.testutils.TuUtils;
import fr.urssaf.image.sae.vi.util.XMLUtils;

@SuppressWarnings({
   "PMD.MethodNamingConventions",
   "PMD.TooManyMethods",
   "PMD.ExcessiveImports"
   })
public class WebServiceVIServiceTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(WebServiceVIServiceTest.class);

   private static WebServiceVIService service;

   private static SamlAssertionExtractionService extraction;

   @BeforeClass
   public static void beforeClass() {

      service = new WebServiceVIService();
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

      assertCreerVIpourServiceWeb(TuGenererVi.ID_UTILISATEUR, TuGenererVi.ID_UTILISATEUR);
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

      Element assertion = service.creerVIpourServiceWeb(pagm, TuGenererVi.ISSUER, idActual,
            keystore, alias, password);

      LOG.debug("\n" + XMLUtils.print(assertion));

      SamlAssertionData data = extraction.extraitDonnees(assertion);

      assertNotNull(data.getAssertionParams().getCommonsParams().getId());
      assertNotNull(data.getAssertionParams().getCommonsParams()
            .getIssueInstant());
      assertEquals(TuGenererVi.ISSUER, data.getAssertionParams().getCommonsParams()
            .getIssuer());

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

      VIContenuExtrait extrait = service.verifierVIdeServiceWeb(
            identification,
            TuGenererVi.SERVICE_VISE, 
            TuGenererVi.ISSUER, 
            TuUtils.buildSignVerifParamsOK());

      assertEquals(TuGenererVi.ID_UTILISATEUR, extrait.getIdUtilisateur());
      
      assertEquals(2,extrait.getPagm().size());
      assertEquals("DROIT_APPLICATIF_1",extrait.getPagm().get(0).getDroitApplicatif());
      assertEquals("PERIMETRE_DONNEES_1",extrait.getPagm().get(0).getPerimetreDonnees());
      assertEquals("DROIT_APPLICATIF_2",extrait.getPagm().get(1).getDroitApplicatif());
      assertEquals("PERIMETRE_DONNEES_2",extrait.getPagm().get(1).getPerimetreDonnees());
      
      assertEquals("Portail Image", extrait.getCodeAppli());

   }

   @Test(expected = VIFormatTechniqueException.class)
   public void verifierVIdeServiceWeb_failure_format() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_format.xml");

      service.verifierVIdeServiceWeb(
            identification, 
            TuGenererVi.SERVICE_VISE, 
            TuGenererVi.ISSUER,
            new VISignVerifParams());

   }

   @Test(expected = VISignatureException.class)
   public void verifierVIdeServiceWeb_failure_sign() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_sign.xml");

      service.verifierVIdeServiceWeb(
            identification, 
            TuGenererVi.SERVICE_VISE,
            TuGenererVi.ISSUER,
            new VISignVerifParams());

   }
   
   
   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_failure_id_1() throws IOException, SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_id_1.xml");

      try {
      
         service.verifierVIdeServiceWeb(
               identification, 
               TuGenererVi.SERVICE_VISE,
               TuGenererVi.ISSUER,
               TuUtils.buildSignVerifParamsOK());
         
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
   public void verifierVIdeServiceWeb_failure_id_2() throws IOException, SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_id_2.xml");

      try {
      
         service.verifierVIdeServiceWeb(
               identification, 
               TuGenererVi.SERVICE_VISE,
               TuGenererVi.ISSUER,
               TuUtils.buildSignVerifParamsOK());
         
         fail("Une exception de type VIInvalideException était attendue");
         
      } catch (VIInvalideException ex) {
         
         assertEquals(
               "Vérification du message de l'exception",
               "L'ID de l'assertion doit être un UUID correct (ce qui n'est pas le cas de 'pfx5d541dee-4468-74d2-7cbe-03078ef284e7')",
               ex.getMessage());
         
      }

   }
   
   
   /**
    * Tests unitaires de la méthode {@link WebServiceVIService#extraitPagm(List)}<br>
    * <br>
    * Cas de test : Un seul PAGM bien formé<br>
    * <br>
    * Résultat attendu : le PAGM est correctement parsé, et pas de levée d'exception
    * 
    * @throws VIPagmIncorrectException 
    */
   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void extraitPagm_TestOk() throws VIPagmIncorrectException {
      List<String> pagmStr = new ArrayList<String>(); 
      pagmStr.add("DROIT_APPLICATIF1;PERIMETRE_DE_DONNEES_1");
      List<VIPagm> pagm = service.extraitPagm(pagmStr);
      assertNotNull(pagm);
      assertEquals(1,pagm.size());
      assertEquals("DROIT_APPLICATIF1",pagm.get(0).getDroitApplicatif());
      assertEquals("PERIMETRE_DE_DONNEES_1",pagm.get(0).getPerimetreDonnees());
   }
   
   
   /**
    * Tests unitaires de la méthode {@link WebServiceVIService#extraitPagm(List)}<br>
    * <br>
    * Cas de test : Un seul PAGM vide<br>
    * <br>
    * Résultat attendu : levée d'exception
    */
   @Test
   public void extraitPagm_TestKo_Vide() {
      
      List<String> pagmStr = new ArrayList<String>(); 
      pagmStr.add("   ");
      
      // NB : On n'utilise pas @Test(expected) pour pouvoir visualiser le message de l'exception
      try {
         service.extraitPagm(pagmStr);
      } catch (VIPagmIncorrectException ex) {
         LOG.debug(StringUtils.EMPTY,ex);
         return;
      }
      
      fail("Une exception du type VIPagmIncorrectException aurait dû être levée");
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link WebServiceVIService#extraitPagm(List)}<br>
    * <br>
    * Cas de test : Un seul PAGM, pas de périmètre de données<br>
    * <br>
    * Résultat attendu : levée d'exception
    */
   @Test
   public void extraitPagm_TestKo_SansPerimetre() {
      
      List<String> pagmStr = new ArrayList<String>(); 
      pagmStr.add("DROIT_APP");
      
      // NB : On n'utilise pas @Test(expected) pour pouvoir visualiser le message de l'exception
      try {
         service.extraitPagm(pagmStr);
      } catch (VIPagmIncorrectException ex) {
         LOG.debug(StringUtils.EMPTY, ex);
         return;
      }
      
      fail("Une exception du type VIPagmIncorrectException aurait dû être levée");
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link WebServiceVIService#extraitPagm(List)}<br>
    * <br>
    * Cas de test : Un seul PAGM, mal formé<br>
    * <br>
    * Résultat attendu : levée d'exception
    */
   @Test
   public void extraitPagm_TestKo_MalForme() {
      
      List<String> pagmStr = new ArrayList<String>(); 
      pagmStr.add("DROIT_APP;PERIMETRE_DONNEES;AUTRE_INFO");
      
      // NB : On n'utilise pas @Test(expected) pour pouvoir visualiser le message de l'exception
      try {
         service.extraitPagm(pagmStr);
      } catch (VIPagmIncorrectException ex) {
         LOG.debug(StringUtils.EMPTY, ex);
         return;
      }
      
      fail("Une exception du type VIPagmIncorrectException aurait dû être levée");
      
   }

}
