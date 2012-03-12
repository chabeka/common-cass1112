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

import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class ResultatsFileEchecSupportTest {

   @Autowired
   private ResultatsFileEchecSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeDirectoryObligatoire() {
      support.writeResultatsFile(null, new File(""),
            new CaptureMasseSommaireDocumentException(0, new Exception(),
                  new DocumentType()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSommaireObligatoire() {
      support.writeResultatsFile(new File(""), null,
            new CaptureMasseSommaireDocumentException(0, new Exception(),
                  new DocumentType()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testErreurObligatoire() {
      support.writeResultatsFile(new File(""), new File(""), null);
   }

}
