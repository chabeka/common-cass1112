/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class EnrichissementMetadonneeSupportTest {

   @Autowired
   private EnrichissementMetadonneeSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void checkDocumentObligatoire() {

      support.enrichirMetadonnee(null);

      Assert.fail("Sortie aspect attendue");
   }
}
