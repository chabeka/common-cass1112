/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class ResultatFileSuccessSupportTest {

   @Autowired
   private ResultatFileSuccessSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeDirectoryObligatoire() {
      
      support.writeResultatsFile(null,
            new ArrayList<CaptureMasseIntegratedDocument>(), 0);
      Assert.fail("Vérification aspect doit se déclencher");
   }
   
   
   @Test(expected = IllegalArgumentException.class)
   public void testDocumentsCountObligatoire() {
      
      support.writeResultatsFile(new File("fichier"),
            new ArrayList<CaptureMasseIntegratedDocument>(), -1);
      Assert.fail("Vérification aspect doit se déclencher");
   }

}
