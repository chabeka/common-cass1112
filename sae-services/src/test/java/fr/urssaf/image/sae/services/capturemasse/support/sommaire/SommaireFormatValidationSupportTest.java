/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class SommaireFormatValidationSupportTest {

   @Autowired
   private SommaireFormatValidationSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeObligatoire()
         throws CaptureMasseSommaireFormatValidationException {

      support.validationSommaire(null);
      Assert.fail("sortie aspect attendue");

   }

}
