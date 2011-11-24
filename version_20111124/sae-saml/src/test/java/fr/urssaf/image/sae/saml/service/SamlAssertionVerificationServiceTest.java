package fr.urssaf.image.sae.saml.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.exception.signature.SamlSignatureException;
import fr.urssaf.image.sae.saml.testutils.TuUtils;

@SuppressWarnings({
   "PMD.MethodNamingConventions",
   "PMD.JUnitAssertionsShouldIncludeMessage"
   })
public class SamlAssertionVerificationServiceTest {

   private static SamlAssertionVerificationService service;

   @BeforeClass
   public static void beforeClass() {

      service = new SamlAssertionVerificationService();

   }


   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierAssertion_success() throws SamlFormatException,
         IOException, SamlSignatureException, SAXException {

      Element assertionSaml = TuUtils.loadResourceFileToElement(
            "src/test/resources/saml/saml_sign_success.xml");

      service.verifierAssertion(assertionSaml, TuUtils.buildSignVerifParamsStd());
      
      // Résultat attendu : aucune exception levée

   }

   @Test(expected = SamlFormatException.class)
   public void verifierAssertion_failure_format_exception()
         throws SamlFormatException, SamlSignatureException, SAXException,
         IOException {

      Element assertionSaml = TuUtils.loadResourceFileToElement(
            "src/test/resources/saml/saml_extraction.xml");

      service.verifierAssertion(assertionSaml, TuUtils.buildSignVerifParamsStd());

   }

   @Test
   public void verifierAssertion_failure_signature_exception()
         throws SamlFormatException, SamlSignatureException, IOException,
         SAXException {

      Element assertionSaml = TuUtils.loadResourceFileToElement(
            "src/test/resources/saml/saml_sign_failure.xml");
      
      try {
         service.verifierAssertion(assertionSaml, TuUtils.buildSignVerifParamsStd());
      } catch (SamlSignatureException e) {
         assertEquals(
               "L'exception levée n'est pas celle attendue : les messages ne correspondent pas",
               "La signature n'est pas cryptographiquement correcte",
               e.getMessage());
         return ;
      }

      fail("L'exception attendue (SamlSignatureException) n'a pas été levée");

   }

}
