/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaeDroitServiceTest {

   private static final String EXCEPTION_ATTENDUE = "exception attendue";
   private static final String ARG_REQUIRED = "argument.required";
   private static final String TYPE_CORRECT = "type de l'exception correcte";
   private static final String MESSAGE_CORRECT = "le message de l'exception doit etre correct";

   @Autowired
   private SaeDroitService service;

   @Test
   public void testLoadIdObligatoire() {

      try {
         service.loadSaeDroits(null, Arrays.asList(new String[] { "pagm1" }));
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT, getMessage("identifiant client"),
               e.getMessage());
      }

   }

   @Test
   public void testLoadListObligatoire() {

      try {
         service.loadSaeDroits("id", null);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT, getMessage("liste des pagms"), e
               .getMessage());
      }

   }

   @Test
   public void testCreateContratObligatoire() {

      try {

         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());

         service.createContratService(null, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT, getMessage("contrat"), e
               .getMessage());
      }

   }

   @Test
   public void testCreateCodeContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setDescription("description");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));
         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT,
               getMessage("code client contrat"), e.getMessage());
      }

   }

   @Test
   public void testCreateDescriptionContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));
         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());
         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT,
               getMessage("description contrat"), e.getMessage());
      }

   }

   @Test
   public void testCreateLibelleContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setViDuree(Long.valueOf(12));
         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT, getMessage("libellé contrat"), e
               .getMessage());
      }

   }

   @Test
   public void testCreateDureeContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setLibelle("libellé");
         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT, getMessage("durée contrat"), e
               .getMessage());
      }

   }

   @Test
   public void testCreateListContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));

         service.createContratService(contract, null);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT, getMessage("liste des pagms"), e
               .getMessage());
      }

   }

   @Test
   public void testCreatePkiObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));
         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertEquals(MESSAGE_CORRECT,
               getMessage("le nom de la PKI ou la liste des PKI"), e
                     .getMessage());
      }

   }

   @Test
   public void testCreateCertificatClientObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));
         List<SaePagm> list = new ArrayList<SaePagm>();
         list.add(new SaePagm());
         contract.setIdPki("pki");
         contract.setVerifNommage(true);

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert
               .assertEquals(
                     MESSAGE_CORRECT,
                     getMessage("le certificat client ou la liste des certificats clients"),
                     e.getMessage());
      }

   }

   @Test
   public void testExistsContratObligatoire() {

      try {

         service.contratServiceExists(null);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient code client", e
               .getMessage().contains("code client"));
      }

   }

   @Test
   public void testGetServiceContratObligatoire() {

      try {

         service.getServiceContract(null);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient code client", e
               .getMessage().contains("code client"));
      }

   }

   private String getMessage(String value) {
      return ResourceMessagesUtils.loadMessage(ARG_REQUIRED, value);
   }
}
