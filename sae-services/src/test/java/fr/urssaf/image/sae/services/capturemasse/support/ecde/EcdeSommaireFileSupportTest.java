/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class EcdeSommaireFileSupportTest {

   @Autowired
   private EcdeSommaireFileSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testSommaireObligatoire() {
      try {
         support.convertURLtoFile(null);
      } catch (CaptureMasseSommaireEcdeURLException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      } catch (CaptureMasseSommaireFileNotFoundException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      }

      Assert.fail("On doit sortir avant par vérification aspect");
   }

}
