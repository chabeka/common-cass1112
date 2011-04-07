package fr.urssaf.image.sae.vi.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.util.ConverterUtils;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.service.CRLFactory;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;

public class WebServiceVIServiceValidateTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private static final String ISSUER = "issuer";

   private static final String ID_UTILISATEUR = "id_utilisateur";

   private static final String ID_APPLI = "id_appli";

   private static Element identification;

   private static final String ALIAS = "alias";

   private static final String PASSWORD = "password";

   private static final URI SERVICE_VISE = ConverterUtils
         .uri("http://sae.urssaf.fr");

   private static List<X509CRL> crl = new ArrayList<X509CRL>();

   private static WebServiceVIService service;

   @BeforeClass
   public static void beforeClass() throws ParserConfigurationException {

      service = EasyMock.createMock(WebServiceVIService.class);

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      identification = document.createElement("test");

      try {
         crl.add(CRLFactory
               .createCRL("src/test/resources/CRL/Pseudo_ACOSS.crl"));
      } catch (CRLException e) {
         throw new IllegalStateException(e);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   private KeyStore keystore;

   private List<String> pagm;

   @Before
   public void before() throws KeyStoreException {

      keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      pagm = Arrays.asList("PAGM_1", "", "   ", "PAGM_2", null);
   }

   @Test
   public void creerVIpourServiceWebFailure_pagm() {

      assertCreerVIpourServiceWebFailure_pagm(null);
      assertCreerVIpourServiceWebFailure_pagm(Arrays.asList("", " ", null));

   }

   private void assertCreerVIpourServiceWebFailure_pagm(List<String> pagm) {

      try {
         service.creerVIpourServiceWeb(pagm, ISSUER, ID_UTILISATEUR, keystore,
               ALIAS, PASSWORD);
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals("Il faut spécifier au moins un PAGM", e.getMessage());
      }

   }

   @Test
   public void creerVIpourServiceWebFailure_issuer() {

      assertCreerVIpourServiceWebFailure("issuer", pagm, null, ID_UTILISATEUR,
            keystore, ALIAS, PASSWORD);

   }

   @Test
   public void creerVIpourServiceWebFailure_keystore() {

      assertCreerVIpourServiceWebFailure("keystore", pagm, ISSUER,
            ID_UTILISATEUR, null, ALIAS, PASSWORD);

   }

   @Test
   public void creerVIpourServiceWebFailure_alias() {

      assertCreerVIpourServiceWebFailure("alias", pagm, ISSUER, ID_UTILISATEUR,
            keystore, null, PASSWORD);

   }

   @Test
   public void creerVIpourServiceWebFailure_password() {

      assertCreerVIpourServiceWebFailure("password", pagm, ISSUER,
            ID_UTILISATEUR, keystore, ALIAS, null);

   }

   private void assertCreerVIpourServiceWebFailure(String param,
         List<String> pagm, String issuer, String idUtilisateur,
         KeyStore keystore, String alias, String password) {

      try {
         service.creerVIpourServiceWeb(pagm, issuer, idUtilisateur, keystore,
               alias, password);
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals("Le paramètre [" + param
               + "] n'est pas renseigné alors qu'il est obligatoire", e
               .getMessage());
      }

   }

   @Test
   public void verifierVIdeServiceWebFailure_identification()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("identification", null, SERVICE_VISE,
            ID_APPLI, keystore, crl);

   }

   @Test
   public void verifierVIdeServiceWebFailure_serviceVise()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("serviceVise", identification, null,
            ID_APPLI, keystore, crl);

   }

   @Test
   public void verifierVIdeServiceWebFailure_application()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("idAppliClient", identification,
            SERVICE_VISE, null, keystore, crl);

   }

   @Test
   public void verifierVIdeServiceWebFailure_keystore()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("keystore", identification, SERVICE_VISE,
            ID_APPLI, null, crl);

   }

   private void assertVerifierVIdeServiceWeb(
         String param,
         Element identification, 
         URI serviceVise, 
         String idAppliClient,
         KeyStore keystore, 
         List<X509CRL> crl)
      throws 
         VIVerificationException {

      try {
         service.verifierVIdeServiceWeb(identification, serviceVise,
               idAppliClient, keystore, crl);
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals("Le paramètre [" + param
               + "] n'est pas renseigné alors qu'il est obligatoire", e
               .getMessage());
      }

   }

   @Test
   public void verifierVIdeServiceWebFailure_crl()
         throws VIVerificationException {

      assertVerifierVIdeServiceWebFailure_crl(null);
      List<X509CRL> crl = new ArrayList<X509CRL>();
      crl.add(null);
      crl.add(null);
      assertVerifierVIdeServiceWebFailure_crl(crl);
      assertVerifierVIdeServiceWebFailure_crl(new ArrayList<X509CRL>());

   }

   private void assertVerifierVIdeServiceWebFailure_crl(List<X509CRL> crl)
         throws VIVerificationException {

      try {
         service.verifierVIdeServiceWeb(identification, SERVICE_VISE, ID_APPLI,
               keystore, crl);
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals("Il faut spécifier au moins un CRL", e.getMessage());
      }

   }

}
