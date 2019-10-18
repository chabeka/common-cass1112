package fr.urssaf.image.sae.services.metadata.cql;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.services.AbstractServiceCqlTest;
import fr.urssaf.image.sae.services.metadata.MetadataService;


/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })*/
public class MetadataServiceCqlTest extends AbstractServiceCqlTest {

  @BeforeClass
  public static void beforeClass() throws IOException {
    init = false;
    ModeApiAllUtils.setAllModeAPICql();
  }

  @Before
  public void before() throws Exception {
    initMetadata();
  }
  @Autowired
  private MetadataService metadataService;

  @Test
  public void getClientAvailableMetadata() {
    ModeApiAllUtils.setAllModeAPICql();
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
