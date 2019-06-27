package fr.urssaf.image.sae.services.batch.capturemasse.support.compression;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.exception.CompressionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class CaptureMasseCompressionSupportValidationTest {

   @Autowired
   private CaptureMasseCompressionSupport captureMasseCompressionSupport;
   
   @Test(expected = IllegalArgumentException.class)
   public void testIsDocumentToBeCompressDocumentAndEcdeDirectoryObligatoire() {
      
      captureMasseCompressionSupport.isDocumentToBeCompress(null, null);
      Assert.fail("sortie aspect attendue");
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testIsDocumentToBeCompressDocumentObligatoire() {
      
      captureMasseCompressionSupport.isDocumentToBeCompress(null, new File(""));
      Assert.fail("sortie aspect attendue");
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testIsDocumentToBeCompressEcdeDirectoryObligatoire() {
      
      captureMasseCompressionSupport.isDocumentToBeCompress(new UntypedDocument(), null);
      Assert.fail("sortie aspect attendue");
   }
   
   
   @Test(expected = IllegalArgumentException.class)
   public void testCompresserDocumentDocumentAndEcdeDirectoryObligatoire() throws CompressionException {
      
      captureMasseCompressionSupport.compresserDocument(null, null);
      Assert.fail("sortie aspect attendue");
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testCompresserDocumentDocumentObligatoire() throws CompressionException {
      
      captureMasseCompressionSupport.compresserDocument(null, new File(""));
      Assert.fail("sortie aspect attendue");
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testCompresserDocumentEcdeDirectoryObligatoire() throws CompressionException {
      
      captureMasseCompressionSupport.compresserDocument(new UntypedDocument(), null);
      Assert.fail("sortie aspect attendue");
   }
}
