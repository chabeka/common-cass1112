/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class CaptureMasseControleSupportValidationTest {

   @Autowired
   private CaptureMasseControleSupport support;

   @Test(expected = IllegalArgumentException.class)
   public void testControleSAEDocumentDocumentObligatoire()
         throws UnknownCodeRndEx, EmptyDocumentEx, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         UnknownHashCodeEx, CaptureMasseSommaireDocumentNotFoundException,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException, 
         InvalidPagmsCombinaisonException {

      support.controleSAEDocument(null, new File(""));
      Assert.fail("sortie aspect attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testControleSAEDocumentEcdeObligatoire()
         throws UnknownCodeRndEx, EmptyDocumentEx, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         UnknownHashCodeEx, CaptureMasseSommaireDocumentNotFoundException,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException,
         InvalidPagmsCombinaisonException {
      support.controleSAEDocument(new UntypedDocument(), null);
      Assert.fail("sortie aspect attendue");

   }

   @Test(expected = IllegalArgumentException.class)
   public void testControleSAEDocumentStockDocumentObligatoire()
         throws RequiredStorageMetadataEx {
      support.controleSAEDocumentStockage(null);
      Assert.fail("sortie aspect attendue");

   }

   @Test
   public void testControleReferenceFileObligatoire() {

      try {
         support.controleFichier(null, null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("fichier de référence"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

   @Test
   public void testControleReferenceFileDirectoryObligatoire() {

      try {
         support.controleFichier(new VirtualReferenceFile(), null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("ecdeDirectory"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

   @Test
   public void testControleSAEMetadataDocumentObligatoire() {

      try {
         support.controleSAEMetadatas(null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("document virtuel"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

   @Test
   public void testControleSAEMetadataFichierReferenceObligatoire() {

      try {
         support.controleSAEMetadatas(new UntypedVirtualDocument());
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("fichier de référence du document"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

   @Test
   public void testControleSAEVirtualDocumentStockageDocumentObligatoire() {

      try {
         support.controleSAEVirtualDocumentStockage(null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("document virtuel"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }
}
