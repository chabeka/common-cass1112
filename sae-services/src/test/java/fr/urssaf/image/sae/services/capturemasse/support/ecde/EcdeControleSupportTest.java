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

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class EcdeControleSupportTest {

   @Autowired
   private EcdeControleSupport support;

   /**
    * Test Argument sommaireFile obligatoire
    */
   @Test(expected = IllegalArgumentException.class)
   public void testSommaireFileObligatoire() {

      try {
         support.checkEcdeWrite(null);
      } catch (CaptureMasseEcdeWriteFileException e) {
         Assert.fail("On doit sortir avant par vérification aspect");
      }

      Assert.fail("On doit sortir avant par vérification aspect");
   }
}
