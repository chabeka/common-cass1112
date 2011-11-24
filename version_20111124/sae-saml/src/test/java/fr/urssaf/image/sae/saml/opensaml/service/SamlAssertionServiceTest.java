package fr.urssaf.image.sae.saml.opensaml.service;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;


@SuppressWarnings("PMD.MethodNamingConventions")
public class SamlAssertionServiceTest {

   private static SamlAssertionService service;
   private static SamlCoreService coreService;

   @BeforeClass
   public static void beforeClass() {

      service = new SamlAssertionService();
      coreService = new SamlCoreService();

   }

   @Test(expected = SamlFormatException.class)
   public void validate_failure_schema_validator() throws SamlFormatException {

      Assertion assertion = coreService.createAssertion("test", new DateTime());

      service.validate(assertion);
   }

   @Test(expected = SamlFormatException.class)
   public void validate_failure_spec_validator() throws SamlFormatException {

      Assertion assertion = coreService.createAssertion("test", new DateTime());
      Issuer issuer = coreService.createIssuer("issuer");

      assertion.setIssuer(issuer);

      service.validate(assertion);
   }
}
