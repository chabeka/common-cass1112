package fr.urssaf.image.sae.ecde.util.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Classe Test de cette factory bean. 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
public class EcdeTestToolsTest {

   @Autowired
   public EcdeTestTools ecdeTestTools;
   
   
   @Test
   public void getUrlEcdeSuccess() throws IOException, URISyntaxException {
      
      EcdeTestSommaire ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
      
      assertEquals(
            "Erreur lors de la mise en place des TU pour l'ECDE", 
            true, 
            ecdeTestSommaire.getRepEcde().exists());
      
   }
   
   @Test
   public void getUrlEcdeDocumentSuccess() throws IOException, URISyntaxException {
      
      String nomDuFichierDoc = "attestation.pdf";
      EcdeTestDocument ecdeTestDocument = ecdeTestTools.buildEcdeTestDocument(nomDuFichierDoc);
      
      assertEquals(
            "Erreur lors de la mise en place des TU pour l'ECDE", 
            true, 
            ecdeTestDocument.getRepEcdeDocuments().exists());
      
   }
   
}
