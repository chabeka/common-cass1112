/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class ResultatsFileEchecBloquantSupportTest {

   @Autowired
   ResultatsFileEchecBloquantSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeDirectoryObligatoire() {
      support.writeResultatsFile(null,
            new CaptureMasseSommaireFormatValidationException(""));
   }

   @Test(expected = IllegalArgumentException.class)
   public void erreurObligatoire() {
      support.writeResultatsFile(new File(""), null);
   }
}
