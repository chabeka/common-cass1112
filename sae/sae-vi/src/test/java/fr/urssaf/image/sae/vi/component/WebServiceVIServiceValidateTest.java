package fr.urssaf.image.sae.vi.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.vi.testutils.TuGenererVi;

@SuppressWarnings( { "PMD.TooManyMethods", "PMD.MethodNamingConventions" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class WebServiceVIServiceValidateTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private static final String ISSUER = "issuer";

   private static final String ID_UTILISATEUR = "id_utilisateur";

   private static final String ID_APPLI = "id_appli";

   private static Element identification;

   private static final String ALIAS = "alias";

   private static final String PASSWORD = "password";

   @Autowired
   private WebServiceVIService service;

   @BeforeClass
   public static void beforeClass() throws ParserConfigurationException {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      identification = document.createElement("test");

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

         assertEquals(
               "Vérification de la levée d'exception si aucun PAGM n'est spécifié",
               "Il faut spécifier au moins un PAGM", e.getMessage());
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

         assertEquals(
               "Vérification de la levée d'une exception IllegalArgumentException avec le bon message",
               "Le paramètre [" + param
                     + "] n'est pas renseigné alors qu'il est obligatoire", e
                     .getMessage());
      }

   }

   @Test
   public void verifierVIdeServiceWebFailure_identification()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("identification", null,
            TuGenererVi.SERVICE_VISE, ID_APPLI, new VISignVerifParams());

   }

   @Test
   public void verifierVIdeServiceWebFailure_serviceVise()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("serviceVise", identification, null,
            ID_APPLI, new VISignVerifParams());

   }

   @Test
   public void verifierVIdeServiceWebFailure_signVerifParams()
         throws VIVerificationException {

      assertVerifierVIdeServiceWeb("signVerifParams", identification,
            TuGenererVi.SERVICE_VISE, ID_APPLI, null);

   }

   private void assertVerifierVIdeServiceWeb(String param,
         Element identification, URI serviceVise, String idAppliClient,
         VISignVerifParams signVerifParams) throws VIVerificationException {

      try {

         service.verifierVIdeServiceWeb(identification, serviceVise,
               signVerifParams);

         fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         assertEquals(
               "Vérification de la levée d'une exception IllegalArgumentException avec le bon message",
               "Le paramètre [" + param
                     + "] n'est pas renseigné alors qu'il est obligatoire", e
                     .getMessage());
      }

   }

}
