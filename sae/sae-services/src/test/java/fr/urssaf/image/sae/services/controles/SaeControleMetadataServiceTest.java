/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;

/**
 * Classe permettant de tester le service de contrôle.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SaeControleMetadataServiceTest {

  @Autowired
  private SaeControleMetadataService service;

  @BeforeClass
  public static void beforeClass() throws IOException {
    ModeApiAllUtils.setAllModeAPIThrift();
  }

  @Test
  public void testStorageMetaObligatoire() {
    try {
      service.checkMetadataForStorage(null);
      Assert.fail(IllegalArgumentException.class.getName() + " attendue");

    } catch (final IllegalArgumentException exception) {
      Assert.assertTrue("le message doit etre correct", exception
                        .getMessage().contains("liste des métadonnées"));

    } catch (final Exception ex) {
      Assert
      .fail("On attendait une IllegalArgumentException alors qu'on a obtenu: "
          + ex.toString());
    }
  }

  @Test
  public void testCaptureMetaObligatoire() {
    try {
      service.checkMetadataForStorage(null);
      Assert.fail(IllegalArgumentException.class.getName() + " attendue");

    } catch (final IllegalArgumentException exception) {
      Assert.assertTrue("le message doit etre correct", exception
                        .getMessage().contains("liste des métadonnées"));

    } catch (final Exception exception) {
      Assert.fail(IllegalArgumentException.class.getName() + " attendue");
    }
  }

  @Test
  public void testUntypedMetaObligatoire() {
    try {
      service.checkUntypedMetadatas(null);
      Assert.fail(IllegalArgumentException.class.getName() + " attendue");

    } catch (final IllegalArgumentException exception) {
      Assert.assertTrue("le message doit etre correct", exception
                        .getMessage().contains("liste des métadonnées"));

    } catch (final Exception exception) {
      Assert.fail(IllegalArgumentException.class.getName() + " attendue");
    }
  }

}
