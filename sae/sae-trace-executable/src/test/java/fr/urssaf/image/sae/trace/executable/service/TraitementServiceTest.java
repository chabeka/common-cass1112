/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-executable-test.xml" })
public class TraitementServiceTest {

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   @Autowired
   private TraitementService service;

   @Test
   public void testPurgeObligatoire() {

      try {
         service.purgerRegistre(null);
      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "type de purge"),
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception IllegalArgumentException est attendue");
      }
   }
   
   
   
}
