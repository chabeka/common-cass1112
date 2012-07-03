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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaeDroitServiceTest {

   /**
    * 
    */
   private static final String EXCEPTION_ATTENDUE = "exception attendue";
   /**
    * 
    */
   private static final String TYPE_CORRECT = "type de l'exception correcte";
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
         Assert.assertTrue("message de l'exception contient identifiant", e
               .getMessage().contains("identifiant"));
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
         Assert.assertTrue("message de l'exception contient liste des pagms", e
               .getMessage().contains("liste des pagms"));
      }

   }

   @Test
   public void testCreateContratObligatoire() {

      try {

         List<Pagm> list = new ArrayList<Pagm>();
         list.add(new Pagm());

         service.createContratService(null, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient contrat", e
               .getMessage().contains("contrat"));
      }

   }

   @Test
   public void testCreateCodeContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setDescription("description");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));
         List<Pagm> list = new ArrayList<Pagm>();
         list.add(new Pagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient code client", e
               .getMessage().contains("code client"));
      }

   }

   @Test
   public void testCreateDescriptionContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setLibelle("libellé");
         contract.setViDuree(Long.valueOf(12));
         List<Pagm> list = new ArrayList<Pagm>();
         list.add(new Pagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient description", e
               .getMessage().contains("description"));
      }

   }

   @Test
   public void testCreateLibelleContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setViDuree(Long.valueOf(12));
         List<Pagm> list = new ArrayList<Pagm>();
         list.add(new Pagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient libellé", e
               .getMessage().contains("libellé"));
      }

   }

   @Test
   public void testCreateDureeContratObligatoire() {

      try {
         ServiceContract contract = new ServiceContract();
         contract.setCodeClient("codeClient");
         contract.setDescription("description");
         contract.setLibelle("libellé");
         List<Pagm> list = new ArrayList<Pagm>();
         list.add(new Pagm());

         service.createContratService(contract, list);
         Assert.fail(EXCEPTION_ATTENDUE);

      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient durée", e
               .getMessage().contains("durée"));
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
         Assert.assertTrue("message de l'exception contient pagms", e
               .getMessage().contains("pagms"));
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
}
