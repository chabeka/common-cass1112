/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class FinTraitementFlagSupportTest {

   @Autowired
   private FinTraitementFlagSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeObligatoire() {

      support.writeFinTraitementFlag(null);

   }

}
