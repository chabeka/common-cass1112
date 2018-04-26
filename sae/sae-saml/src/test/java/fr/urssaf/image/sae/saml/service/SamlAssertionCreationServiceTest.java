package fr.urssaf.image.sae.saml.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.exception.signature.SamlSignatureException;
import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.testutils.KeyStoreFactory;
import fr.urssaf.image.sae.saml.testutils.TuUtils;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SamlAssertionCreationServiceTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(SamlAssertionCreationServiceTest.class);

   private static SamlAssertionCreationService service;

   private static SamlAssertionVerificationService validationService;

   private KeyStore keystore;

   private String alias;

   private static final String PASSWORD = "hiUnk6O3QnRN";

   @BeforeClass
   public static void beforeClass() {

      service = new SamlAssertionCreationService();
      validationService = new SamlAssertionVerificationService();

   }

   private SamlAssertionParams params;

   private Element assertion;

   @Before
   public void before() throws URISyntaxException, KeyStoreException,
         NoSuchAlgorithmException, CertificateException, IOException,
         NoSuchProviderException {

      params = new SamlAssertionParams();

      URI subjectFormat2 = new URI(
            "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
      params.setSubjectFormat2(subjectFormat2);
      params.setSubjectId2("subjectId2");
      URI methodAuthn2 = new URI(
            "urn:oasis:names:tc:SAML:2.0:ac:classes:Password");
      params.setMethodAuthn2(methodAuthn2);
      URI recipient = new URI("urn:interops:sp:73282932000074:test");
      params.setRecipient(recipient);

      SamlCommonsParams commonsParams = new SamlCommonsParams();
      URI audience = new URI("http://rniam.cnav.fr");
      commonsParams.setAudience(audience);

      commonsParams.setIssuer("urn:interops:73282932000074:idp:test:version");
      Date notOnBefore = new Date();
      commonsParams.setNotOnBefore(notOnBefore);
      Date notOnOrAfter = new Date();
      commonsParams.setNotOnOrAfter(notOnOrAfter);
      commonsParams.setPagm(Arrays.asList("ROLE_USER", "", " ", "ROLE_ADMIN",
            null));
      params.setCommonsParams(commonsParams);

      keystore = KeyStoreFactory.createKeystore();
      alias = keystore.aliases().nextElement();
   }

   @After
   public void after() throws SAXException, SamlFormatException,
         SamlSignatureException {

      validationService.verifierAssertion(assertion, TuUtils
            .buildSignVerifParamsStd());

      TuUtils.debugAssertion(LOG, assertion);

   }

   /**
    * cas où id,issueInstant & authnInstant ne sont pas renseignés
    * 
    * @throws SamlSignatureException
    * @throws SamlFormatException
    * 
    */
   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void genererAssertion() throws SamlFormatException,
         SamlSignatureException {

      SamlCommonsParams commonsParams = params.getCommonsParams();
      Date authnInstant = new Date();
      commonsParams.setAuthnInstant(authnInstant);
      UUID uuid = UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
      commonsParams.setId(uuid);
      Date issueInstant = new Date();
      commonsParams.setIssueInstant(issueInstant);

      assertion = service.genererAssertion(params, keystore, alias, PASSWORD);

   }

   /**
    * cas où id,issueInstant & authnInstant ne sont pas renseignés
    * 
    */
   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void genererAssertion_empty() {

      assertion = service.genererAssertion(params, keystore, alias, PASSWORD);

   }

}
