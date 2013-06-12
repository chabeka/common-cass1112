/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePrmdServiceTest {

   /**
    * 
    */
   private static final String TYPE_CORRECTE = "type de l'exception correcte";
   /**
    * 
    */
   private static final String ERREUR_ATTENDUE = "erreur attendue";
   @Autowired
   private SaePrmdService service;

   @Test
   public void testCreatePrmdObligatoire() {
      try {
         service.createPrmd(null);
         Assert.fail(ERREUR_ATTENDUE);
      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECTE, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient prmd", e
               .getMessage().contains("prmd"));
      }

   }

   @Test
   public void testCreateCodePrmdObligatoire() {
      try {

         Prmd prmd = new Prmd();
         prmd.setDescription("description");
         prmd.setLucene("lucène");

         service.createPrmd(prmd);
         Assert.fail(ERREUR_ATTENDUE);
      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECTE, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient code", e
               .getMessage().contains("code"));
      }

   }

   @Test
   public void testCreateDescriptionObligatoire() {
      try {

         Prmd prmd = new Prmd();
         prmd.setCode("code");
         prmd.setLucene("lucène");

         service.createPrmd(prmd);
         Assert.fail(ERREUR_ATTENDUE);
      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECTE, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient description", e
               .getMessage().contains("description"));
      }
   }

  @Test
   public void testCheckCodeObligatoire() {
      try {

         service.prmdExists(null);
         Assert.fail(ERREUR_ATTENDUE);
      } catch (Exception e) {
         Assert.assertEquals(TYPE_CORRECTE, IllegalArgumentException.class, e
               .getClass());
         Assert.assertTrue("message de l'exception contient code", e
               .getMessage().contains("code"));
      }
   }
  
  @Test
  public void getPrmdTest(){
     Prmd prmd = new Prmd();

     prmd.setCode("codePrmd");
     prmd.setDescription("description Prmd");
     prmd.setLucene("lucene Prmd");
     prmd.setBean("bean1");
     Map<String, List<String>> map = new HashMap<String, List<String>>();
     map.put("cle1", Arrays.asList(new String[]{"valeur1"}));
     prmd.setMetadata(map);

     service.createPrmd(prmd);

     Prmd storedPrmd = service.getPrmd("codePrmd");
     Assert.assertEquals("les deux prmds doivent être identiques", storedPrmd,
           prmd);
  }
}
