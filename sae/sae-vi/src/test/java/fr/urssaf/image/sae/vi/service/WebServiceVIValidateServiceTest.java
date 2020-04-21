package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService;
import fr.urssaf.image.sae.vi.exception.VIAppliClientException;
import fr.urssaf.image.sae.vi.exception.VIFormatTechniqueException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VINivAuthException;
import fr.urssaf.image.sae.vi.exception.VIPagmIncorrectException;
import fr.urssaf.image.sae.vi.exception.VIServiceIncorrectException;
import fr.urssaf.image.sae.vi.exception.VISignatureException;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.testutils.TuGenererVi;
import fr.urssaf.image.sae.vi.testutils.TuUtils;
import fr.urssaf.image.sae.vi.util.XMLUtils;

@SuppressWarnings( { "PMD.TooManyMethods", "PMD.MethodNamingConventions",
   "PMD.VariableNamingConventions", "PMD.AvoidDuplicateLiterals" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class WebServiceVIValidateServiceTest {

   @Autowired
   private WebServiceVIValidateService service;

   private static SamlAssertionExtractionService extraction;

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private static Date system_date;

   @BeforeClass
   public static void beforeClass() {

      extraction = new SamlAssertionExtractionService();

      try {
         system_date = DateUtils.parseDate("12/12/1999 01:00:00",
               new String[] { "dd/MM/yyyy HH:mm:ss" });
      } catch (final ParseException e) {
         throw new IllegalStateException(e);
      }
   }

   private void assertVIVerificationException_wsse(final String faultCode,
         final String faultMessage, final VIVerificationException exception) {

      assertVIVerificationException(
            "wsse",
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
            faultCode, faultMessage, exception);
   }

   private void assertVIVerificationException_vi(final String faultCode,
         final String faultMessage, final VIVerificationException exception) {

      assertVIVerificationException("vi", "urn:iops:vi:faultcodes", faultCode,
            faultMessage, exception);

   }

   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   private void assertVIVerificationException(final String prefix, final String namespace,
         final String faultCode, final String faultMessage,
         final VIVerificationException exception) {

      assertEquals(prefix, exception.getSoapFaultCode().getPrefix());
      assertEquals(namespace, exception.getSoapFaultCode().getNamespaceURI());
      assertEquals(faultCode, exception.getSoapFaultCode().getLocalPart());
      assertEquals(faultMessage, exception.getSoapFaultMessage());
   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void verifierVIdeServiceWeb_failure_datebefore()
         throws VIAppliClientException, VINivAuthException,
         VIPagmIncorrectException, IOException, VIServiceIncorrectException,
         SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_datebefore.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);
      } catch (final VIInvalideException exception) {
         assertEquals(
               "L'assertion n'est pas encore valable: elle ne sera active qu'à partir de 31/12/1999 02:00:00 alors que nous sommes le 12/12/1999 01:00:00",
               exception.getMessage());

         assertVIVerificationException_vi("InvalidVI", "Le VI est invalide",
               exception);
      }

   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void verifierVIdeServiceWeb_failure_dateafter()
         throws VIAppliClientException, VINivAuthException,
         VIPagmIncorrectException, IOException, VIServiceIncorrectException,
         SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_dateafter.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);

      } catch (final VIInvalideException exception) {
         assertEquals(
               "L'assertion a expirée : elle n'était valable que jusqu’au 01/12/1999 02:00:00, hors nous sommes le 12/12/1999 01:00:00",
               exception.getMessage());

         assertVIVerificationException_vi("InvalidVI", "Le VI est invalide",
               exception);
      }

   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void verifierVIdeServiceWeb_failure_serviceVise()
         throws VIAppliClientException, VINivAuthException,
         VIPagmIncorrectException, IOException, VIInvalideException,
         SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_servicevise.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);

      } catch (final VIServiceIncorrectException exception) {
         assertEquals(
               "Le service visé '"
                     + TuGenererVi.SERVICE_VISE
                     + "' ne correspond pas à celui indiqué dans le VI 'http://service_test.fr'",
                     exception.getMessage());

         assertVIVerificationException_vi("InvalidService",
               "Le service visé par le VI n'existe pas ou est invalide",
               exception);
      }

   }

   @Test
   @Ignore
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void verifierVIdeServiceWeb_failure_idapplication()
         throws VINivAuthException, VIPagmIncorrectException, IOException,
         VIInvalideException, VIServiceIncorrectException, SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_idapplication.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);

      } catch (final VIAppliClientException exception) {
         assertEquals(
               "L'identifiant de l'organisme client présent dans le VI (service_failure) est invalide ou inconnu",
               exception.getMessage());

         assertVIVerificationException_vi(
               "InvalidIssuer",
               "L'identifiant de l'organisme client présent dans le VI est invalide ou inconnu",
               exception);
      }

   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void verifierVIdeServiceWeb_failure_methodauth()
         throws VIPagmIncorrectException, IOException, VIInvalideException,
         VIAppliClientException, VIServiceIncorrectException, SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_methodauthn.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);

      } catch (final VINivAuthException exception) {
         assertEquals(
               "Le niveau d'authentification 'method_failure' est incorrect",
               exception.getMessage());

         assertVIVerificationException_vi(
               "InvalidAuthLevel",
               "Le niveau d'authentification initial n'est pas conforme au contrat d'interopérabilité",
               exception);
      }

   }

   @Test
   public void verifierVIdeServiceWeb_failure_methodauth_vide()
         throws VIPagmIncorrectException, IOException, VIInvalideException,
         VIAppliClientException, VIServiceIncorrectException, SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_methodauthn_vide.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);

      } catch (final VINivAuthException exception) {
         assertEquals("Vérification du message de l'exception",
               "Le niveau d'authentification n'est pas renseigné", exception
               .getMessage());

         assertVIVerificationException_vi(
               "InvalidAuthLevel",
               "Le niveau d'authentification initial n'est pas conforme au contrat d'interopérabilité",
               exception);
      }

   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void verifierVIdeServiceWeb_failure_pagm() throws IOException,
   VIInvalideException, VIAppliClientException, VINivAuthException,
   VIServiceIncorrectException, SAXException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_pagm.xml");
      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, system_date);

         fail(FAIL_MESSAGE);

      } catch (final VIPagmIncorrectException exception) {
         assertVIVerificationException_vi("InvalidPagm",
               "Le ou les PAGM présents dans le VI sont invalides", exception);
      }

   }

   @Test
   @Ignore("a réactiver si un certificat = 1 pagm")
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void verifierVIdeServiceWeb_applicationInexistante()
         throws IOException, SAXException, VIFormatTechniqueException,
         VISignatureException, VIInvalideException, VINivAuthException,
         VIServiceIncorrectException, VIPagmIncorrectException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_success.xml");

      final SamlAssertionData data = extraction.extraitDonnees(identification);

      try {

         service.validate(data, TuGenererVi.SERVICE_VISE, new Date());

         fail(FAIL_MESSAGE);

      } catch (final VIAppliClientException exception) {
         assertVIVerificationException_vi(
               "InvalidIssuer",
               "L'identifiant de l'organisme client présent dans le VI est invalide ou inconnu",
               exception);
      }

   }

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_success() throws IOException,
   SAXException, VIFormatTechniqueException, VISignatureException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_success.xml");

      final boolean shouldValidateCertificates = true;
      service.validate(identification, TuUtils.buildSignVerifParamsOK(), shouldValidateCertificates);

      // Résultat attendu : aucune exception levée

   }

   @Test
   public void verifierVIdeServiceWeb_failure_format() throws IOException,
   SAXException, VISignatureException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_format.xml");

      try {
         final boolean shouldValidateCertificates = true;
         service.validate(identification, new VISignVerifParams(), shouldValidateCertificates);
         fail(FAIL_MESSAGE);
      } catch (final VIFormatTechniqueException e) {

         assertVIVerificationException_wsse("InvalidSecurityToken",
               "Le jeton de sécurité fourni est invalide", e);
      }

   }

   @Test
   public void verifierVIdeServiceWeb_failure_sign() throws IOException,
   SAXException, VIFormatTechniqueException {

      final Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_sign.xml");

      try {
         final boolean shouldValidateCertificates = true;
         service.validate(identification, new VISignVerifParams(), shouldValidateCertificates);
         fail(FAIL_MESSAGE);
      } catch (final VISignatureException exception) {

         assertVIVerificationException_wsse("FailedCheck",
               "La signature ou le chiffrement n'est pas valide", exception);
      }

   }

}
