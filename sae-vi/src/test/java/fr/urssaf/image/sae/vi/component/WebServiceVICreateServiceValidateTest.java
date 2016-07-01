package fr.urssaf.image.sae.vi.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.vi.service.WebServiceVICreateService;

@SuppressWarnings( { "PMD.TooManyMethods", "PMD.MethodNamingConventions" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class WebServiceVICreateServiceValidateTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private static final String ISSUER = "issuer";

   private static final String ID_UTILISATEUR = "id_utilisateur";

   private static final String ALIAS = "alias";

   private static final String PASSWORD = "password";

   @Autowired
   private WebServiceVICreateService service;

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

}
