package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.VIPortailContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VIPortailCreateParams;

/**
 * TU de la classe PortailVIService
 */
@SuppressWarnings("PMD")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class PortailVIServiceTest {

   @Autowired
   private PortailVIService service;

   private static final String FAIL_MESSAGE = "Une exception aurait dû être levée";

   private static final String PAGM_EXPECTED = "One of '{pagm}' is expected";

   private static final String XSD = "vi-portail-a-portail.xsd";

   @Test
   public void createVI_success() throws IOException, VIException {

      VIPortailCreateParams viParams = new VIPortailCreateParams();
      viParams.setIssuer("id_du_contrat_de_service");
      viParams.setLogin("LOGIN");
      viParams.setAudience("http://sae.urssaf.fr");
      viParams.setNameId("NOM PRENOM");
      viParams.getPagmList().add("PAGM1");
      viParams.getPagmList().add("PAGM2");
      viParams.setHabAnais("HAB ANAIS");

      String viXml = service.creerVI(viParams);
      viXml = remplaceCRLF(viXml);

      File file = new File(
            "src/test/resources/vi_portail/vi_portail_pour_verif_generation.xml");
      String viPourVerif = FileUtils.readFileToString(file, "UTF-8");
      viPourVerif = remplaceCRLF(viPourVerif);

      assertEquals("Le VI généré n'est pas correct", viPourVerif, viXml);

   }

   @Test
   public void createVI_failure_aucun_pagm() {

      try {

         VIPortailCreateParams viParams = new VIPortailCreateParams();
         viParams.setIssuer("id_du_contrat_de_service");
         viParams.setAudience("http://sae.urssaf.fr");
         viParams.setNameId("NOM PRENOM");
         viParams.setLogin("LOGIN");
         viParams.setHabAnais("HAB ANAIS");

         service.creerVI(viParams);

         fail(FAIL_MESSAGE);

      } catch (VIException e) {

         assertEquals(XSD, e.getXsd());
         assertEquals(1, e.getErrors().length);
         assertError(PAGM_EXPECTED, e.getErrors()[0]);

      }

   }

   @Test
   public void createVI_failure_empty() {

      try {

         VIPortailCreateParams viParams = new VIPortailCreateParams();

         service.creerVI(viParams);

         fail(FAIL_MESSAGE);

      } catch (VIException e) {

         assertEquals(XSD, e.getXsd());

         assertError(PAGM_EXPECTED, e.getErrors()[1]);

         assertEquals(2, e.getErrors().length);

      }

   }

   @Test
   public void readVI_success() throws VIException {

      String viXml = lectureViTestDepuisRessource("vi_portail_pour_test_lecture_ok.xml");

      VIPortailContenuExtrait viData = service.lireVI(viXml);

      assertNotNull(
            "Le fichier vi_portail_ok.xml aurait dû être lu avec succès",
            viData);

      assertEquals("La lecture de 'audience' est incorrecte",
            "http://sae.urssaf.fr", viData.getAudience());

      assertEquals("La lecture de 'issuer' est incorrecte",
            "id_du_contrat_de_service", viData.getIssuer());

      assertEquals("La lecture de 'nameID' est incorrecte", "NOM PRENOM",
            viData.getNameId());

      assertNotNull("La lecture des PAGM est incorrecte", viData.getPagmList());
      assertEquals("La lecture des PAGM est incorrecte", 2, viData
            .getPagmList().size());
      assertEquals("La lecture des PAGM est incorrecte", "PAGM1", viData
            .getPagmList().get(0));
      assertEquals("La lecture des PAGM est incorrecte", "PAGM2", viData
            .getPagmList().get(1));

   }

   @Test
   public void readVI_failure_aucun_pagm() throws IOException {

      String viXml = lectureViTestDepuisRessource("vi_portail_pour_test_lecture_ko_aucun_pagm.xml");

      try {

         service.lireVI(viXml);

         fail(FAIL_MESSAGE);

      } catch (VIException e) {
         assertEquals(XSD, e.getXsd());
         assertEquals(1, e.getErrors().length);
         assertError(PAGM_EXPECTED, e.getErrors()[0]);
      }
   }

   @Test
   public void readVI_failure_noxml() {

      try {

         service.lireVI("no xml");

         fail(FAIL_MESSAGE);

      } catch (VIException e) {

         assertError("Content is not allowed in prolog", e.getErrors()[0]);
         assertEquals(1, e.getErrors().length);
      }
   }

   @Test
   public void readVI_failure_noctd() throws IOException {

      String viXml = lectureViTestDepuisRessource("vi_portail_pour_test_lecture_ko_pas_vi.xml");

      try {

         service.lireVI(viXml);

         fail(FAIL_MESSAGE);

      } catch (VIException e) {

         assertError(
               "Expected elements are <{http://www.cirtil.fr/sae/viPortailAportail}vi>",
               e.getErrors()[0]);
         assertEquals(1, e.getErrors().length);
      }
   }

   private void assertError(String msg, String error) {

      assertTrue("'" + error + "' doit contenir '" + msg + "'", error
            .contains(msg));
   }

   private String remplaceCRLF(String vi) {
      String result = vi;
      result = StringUtils.replace(result, "\r\n", "\n");
      result = StringUtils.replace(result, "\r", "\n");
      return result;
   }

   private String lectureViTestDepuisRessource(String nomFicVi) {

      File file = new File("src/test/resources/vi_portail/" + nomFicVi);
      try {

         String viXml = FileUtils.readFileToString(file, "UTF-8");

         viXml = remplaceCRLF(viXml);

         return viXml;

      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }

   }

}
