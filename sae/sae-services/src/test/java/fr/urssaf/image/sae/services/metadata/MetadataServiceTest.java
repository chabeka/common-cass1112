package fr.urssaf.image.sae.services.metadata;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class MetadataServiceTest {

   @Autowired
   private MetadataService metadataService;

   @Test
   public void getClientAvailableMetadata() {
      List<MetadataReference> metadatas = metadataService
            .getClientAvailableMetadata();
      Assert
            .assertNotNull(
                  "La liste des métadonnées mise à disposition du client ne doit pas être nulle",
                  metadatas);
      Assert
            .assertFalse(
                  "La liste des métadonnées  mise à disposition du client ne doit pas être vide",
                  metadatas.isEmpty());

      for (MetadataReference metadata : metadatas) {
         Assert.assertTrue(
               "La métadonnée n'est pas mise à disposition du client", metadata
                     .isClientAvailable());
      }
   }
}
