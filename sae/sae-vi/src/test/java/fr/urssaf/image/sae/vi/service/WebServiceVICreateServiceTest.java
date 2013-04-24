package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
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
import fr.urssaf.image.sae.vi.testutils.TuGenererVi;
import fr.urssaf.image.sae.vi.util.XMLUtils;

@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.TooManyMethods",
      "PMD.ExcessiveImports" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class WebServiceVICreateServiceTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(WebServiceVICreateServiceTest.class);

   @Autowired
   private WebServiceVICreateService service;

   private static SamlAssertionExtractionService extraction;

   @BeforeClass
   public static void beforeClass() {

      extraction = new SamlAssertionExtractionService();

   }

   private KeyStore keystore;

   private String alias;

   @Before
   public void before() throws KeyStoreException, NoSuchAlgorithmException,
         CertificateException, IOException, NoSuchProviderException {

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
}
