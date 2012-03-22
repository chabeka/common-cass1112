/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-test.xml" })
public class SommaireFormatValidationSupportTest {

   @Autowired
   private SommaireFormatValidationSupport support;

   @Test(expected = CaptureMasseSommaireFormatValidationException.class)
   public void testFichierSommaireNonValideXsd()
         throws CaptureMasseSommaireFormatValidationException {
      File sommaire = new File(
            "src/test/resources/sommaire/sommaire_format_failure.xml");
      support.validationSommaire(sommaire);

      Assert.fail("exception attendue");
   }

   @Test()
   public void testFichierSommaireValideXsd() {

      File sommaire = new File(
            "src/test/resources/sommaire/sommaire_success.xml");

      try {
         support.validationSommaire(sommaire);

      } catch (CaptureMasseSommaireFormatValidationException e) {
         Assert.fail("exception attendue");
      }
   }
}
