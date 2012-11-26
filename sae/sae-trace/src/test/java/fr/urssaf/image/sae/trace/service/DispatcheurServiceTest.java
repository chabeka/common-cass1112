/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class DispatcheurServiceTest {

   @Autowired
   private DispatcheurService service;

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   @Test
   public void testTraceObligatoire() {

      try {
         service.ajouterTrace(null);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "trace"), exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }
}
