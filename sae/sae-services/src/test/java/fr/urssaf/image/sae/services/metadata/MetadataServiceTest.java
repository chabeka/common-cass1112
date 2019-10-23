package fr.urssaf.image.sae.services.metadata;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class MetadataServiceTest {

  @Autowired
  private MetadataService metadataService;

  @BeforeClass
  public static void beforeClass() throws IOException {
    ModeApiAllUtils.setAllModeAPIThrift();
  }

  @Test
  public void getClientAvailableMetadata() {
    final List<MetadataReference> metadatas = metadataService
        .getClientAvailableMetadata();
    Assert
    .assertNotNull(
                   "La liste des métadonnées mise à disposition du client ne doit pas être nulle",
                   metadatas);
    Assert
    .assertFalse(
                 "La liste des métadonnées  mise à disposition du client ne doit pas être vide",
                 metadatas.isEmpty());

    for (final MetadataReference metadata : metadatas) {
      Assert.assertTrue(
                        "La métadonnée n'est pas mise à disposition du client", metadata
                        .isClientAvailable());
    }
  }
}
