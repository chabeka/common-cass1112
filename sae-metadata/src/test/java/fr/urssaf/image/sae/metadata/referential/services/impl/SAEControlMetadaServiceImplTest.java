/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.SAEControlMetadataService;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata.xml" })
public class SAEControlMetadaServiceImplTest {

   @Autowired
   private SAEControlMetadataService service;

   @Test
   public void metaDataExists() {

      try {
         service.controlLongCodeExist(Arrays.asList(new String[] { "Siret" }));
      } catch (ReferentialException e) {
         Assert.fail("pas d'exception à lever");
      } catch (LongCodeNotFoundException e) {
         Assert.fail("pas d'exception à lever");
      }

   }

   @Test
   public void metaDataNotExists() {

      try {
         service.controlLongCodeExist(Arrays
               .asList(new String[] { "codeInexistantEnBase" }));
         Assert.fail("une exception doit être levée");
      } catch (ReferentialException e) {
         Assert.fail("pas d'exception à lever");
      } catch (LongCodeNotFoundException e) {
         Assert.assertNotNull(e.getListCode());
         Assert.assertTrue("un élément dans la liste des éléments non trouvés",
               e.getListCode().size() == 1);
         Assert.assertEquals("L'élément doit être codeInexistantEnBase",
               "codeInexistantEnBase", e.getListCode().get(0));
      }

   }

   @Test
   public void testMetaDataConsultable() {

      try {
         service.controlLongCodeIsAFConsultation(Arrays
               .asList(new String[] { "Siret" }));
      } catch (ReferentialException e) {
         Assert.fail("pas d'exception à lever");
      } catch (LongCodeNotFoundException e) {
         Assert.fail("pas d'exception à lever");
      }
   }

   @Test
   public void testMetaDataNonConsultable() {
      try {
         service.controlLongCodeIsAFConsultation(Arrays
               .asList(new String[] { "StartPage" }));
         Assert.fail("une exception doit être levée");
      } catch (ReferentialException e) {
         Assert.fail("pas d'exception à lever");
      } catch (LongCodeNotFoundException e) {
         Assert.assertNotNull(e.getListCode());
         Assert.assertTrue("un élément dans la liste des éléments non trouvés",
               e.getListCode().size() == 1);
         Assert.assertEquals("L'élément doit être StartPage", "StartPage", e
               .getListCode().get(0));
      }
   }

}
