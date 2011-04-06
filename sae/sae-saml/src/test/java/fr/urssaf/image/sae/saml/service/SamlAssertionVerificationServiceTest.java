package fr.urssaf.image.sae.saml.service;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.exception.SamlSignatureException;
import fr.urssaf.image.sae.saml.util.XMLUtils;

@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.JUnitAssertionsShouldIncludeMessage" })
public class SamlAssertionVerificationServiceTest {

   private static SamlAssertionVerificationService service;

   private KeyStore keystore;

   private String alias;

   @BeforeClass
   public static void beforeClass() {

      service = new SamlAssertionVerificationService();

   }

   @Before
   public void before() throws KeyStoreException, NoSuchAlgorithmException,
         CertificateException, IOException {

      keystore = KeyStoreFactory.createKeystore();
      alias = keystore.aliases().nextElement();
   }

   @Test
   public void verifierAssertion_success() throws SamlFormatException,
         IOException, SamlSignatureException, SAXException {

      Element assertionSaml = parse("src/test/resources/saml/saml_sign_success.xml");

      List<X509CRL> crl = new ArrayList<X509CRL>();

      service.verifierAssertion(assertionSaml, keystore, alias, crl);

   }

   @Test(expected = SamlFormatException.class)
   public void verifierAssertion_failure_format_exception()
         throws SamlFormatException, SamlSignatureException, SAXException,
         IOException {

      Element assertionSaml = parse("src/test/resources/saml/saml_extraction.xml");

      service.verifierAssertion(assertionSaml, keystore, alias, null);

   }

   @Test(expected = SamlSignatureException.class)
   public void verifierAssertion_failure_signature_exception()
         throws SamlFormatException, SamlSignatureException, IOException,
         SAXException {

      Element assertionSaml = parse("src/test/resources/saml/saml_sign_failure.xml");

      service.verifierAssertion(assertionSaml, keystore, alias, null);

   }

   private Element parse(String xml) throws SAXException, IOException {

      File file = new File(xml);
      return XMLUtils.parse(FileUtils.readFileToString(file, "UTF-8"));

   }
}
