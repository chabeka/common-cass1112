/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class RollbackSupportTest {

   @Autowired
   private RollbackSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testIdentifiantObligatoire()
         throws CaptureMasseSommaireFormatValidationException {

      support.rollback(null);
      Assert.fail("sortie aspect attendue");

   }
}
