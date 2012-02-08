package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.DroitApplicatif;
import fr.urssaf.image.sae.vi.modele.ObjectFactory;

@SuppressWarnings("PMD")
public class VIServiceTest {

   private VIService service;

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private static final String DROITS_EXPECTED = "One of '{Droits}' is expected";

   // private static final String PRENOM_EXPECTED =
   // "One of '{Prenom}' is expected";

   private static final String NOM_EXPECTED = "One of '{Nom}' is expected";

   private static final String XSD = "sae-anais.xsd";

   @Before
   public void before() {

      service = new VIService();
   }

   @Test
   public void readVI_success() throws IOException, VIException {

      File file = new File("src/test/resources/ctd_2_rights.xml");
      String xml = FileUtils.readFileToString(file, "UTF-8");

      assertNotNull("le fichier 'ctd_2_rights.xml' est invalide", service
            .readVI(xml));
   }

   @Test
   public void readVI_failure_droits() throws IOException {

      File file = new File("src/test/resources/ctd_0_right.xml");
      String xml = FileUtils.readFileToString(file, "UTF-8");

      try {
         service.readVI(xml);
         fail(FAIL_MESSAGE);
      } catch (VIException e) {
         assertEquals(XSD, e.getXsd());
         assertEquals(1, e.getErrors().length);
         assertError(DROITS_EXPECTED, e.getErrors()[0]);
      }
   }

   @Test
   public void readVI_failure_empty() throws IOException {

      File file = new File("src/test/resources/ctd_empty.xml");
      String xml = FileUtils.readFileToString(file, "UTF-8");

      try {
         service.readVI(xml);
         fail(FAIL_MESSAGE);
      } catch (VIException e) {

         assertError(NOM_EXPECTED, e.getErrors()[0]);
         // assertError(PRENOM_EXPECTED, e.getErrors()[1]);
         assertError(DROITS_EXPECTED, e.getErrors()[1]);

         assertEquals(2, e.getErrors().length);
      }
   }

   @Test
   public void readVI_failure_noxml() {

      try {
         service.readVI("no xml");
         fail(FAIL_MESSAGE);
      } catch (VIException e) {

         assertError("Content is not allowed in prolog", e.getErrors()[0]);
         assertEquals(1, e.getErrors().length);
      }
   }

   @Test
   public void readVI_failure_noctd() throws IOException {

      File file = new File("src/test/resources/noctd.xml");
      String xml = FileUtils.readFileToString(file, "UTF-8");

      try {
         service.readVI(xml);
         fail(FAIL_MESSAGE);
      } catch (VIException e) {

         assertError("Expected elements are <{}SaeJetonAuthentification>", e
               .getErrors()[0]);
         assertEquals(1, e.getErrors().length);
      }
   }

   @Test
   public void createVI_success() throws IOException, VIException {

      List<DroitApplicatif> droits = new ArrayList<DroitApplicatif>();

      droits.add(this.createDroitApplicatif("Code 1", "Type 1", "Value 1"));
      droits.add(this.createDroitApplicatif("Code 2", "Type 2", "Value 2"));

      String token = service.createVI("Nom", "Prenom", droits);

      File file = new File("src/test/resources/ctd_2_rights.xml");

      assertEquals(FileUtils.readFileToString(file, "UTF-8"), token);
   }

   @Test
   public void createVI_failure_droits() {

      try {
         List<DroitApplicatif> droits = new ArrayList<DroitApplicatif>();
         service.createVI("Nom", "Prenom", droits);
         fail(FAIL_MESSAGE);
      } catch (VIException e) {
         assertEquals(XSD, e.getXsd());
         assertEquals(1, e.getErrors().length);
         assertError(DROITS_EXPECTED, e.getErrors()[0]);
      }

   }

   @Test
   public void createVI_failure_empty() {

      try {
         service.createVI(null, null, new ArrayList<DroitApplicatif>());
         fail(FAIL_MESSAGE);
      } catch (VIException e) {
         assertEquals(XSD, e.getXsd());

         assertError(NOM_EXPECTED, e.getErrors()[0]);
         // assertError(PRENOM_EXPECTED, e.getErrors()[1]);
         assertError(DROITS_EXPECTED, e.getErrors()[1]);

         assertEquals(2, e.getErrors().length);
      }

   }

   private void assertError(String msg, String error) {

      assertTrue("'" + error + "' doit contenir '" + msg + "'", error
            .contains(msg));
   }

   private DroitApplicatif createDroitApplicatif(String code, String type,
         String value) {

      DroitApplicatif droit = ObjectFactory.createDroitAplicatif();
      droit.setCode(code);
      droit.setPerimetreType(type);
      droit.setPerimetreValue(value);

      return droit;

   }
}
