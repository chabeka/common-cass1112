/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class CaptureMasseControleSupportTest {

   @Autowired
   private CaptureMasseControleSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testControleSAEDocumentDocumentObligatoire()
         throws UnknownCodeRndEx, CaptureMasseSommaireFileNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx {

      support.controleSAEDocument(null, new File(""));
      Assert.fail("sortie aspect attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testControleSAEDocumentEcdeObligatoire()
         throws UnknownCodeRndEx, CaptureMasseSommaireFileNotFoundException,
         EmptyDocumentEx, UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx {
      support.controleSAEDocument(new UntypedDocument(), null);
      Assert.fail("sortie aspect attendue");

   }

   @Test(expected = IllegalArgumentException.class)
   public void testControleSAEDocumentStockDocumentObligatoire()
         throws RequiredStorageMetadataEx {
      support.controleSAEDocumentStockage(null);
      Assert.fail("sortie aspect attendue");

   }
}
