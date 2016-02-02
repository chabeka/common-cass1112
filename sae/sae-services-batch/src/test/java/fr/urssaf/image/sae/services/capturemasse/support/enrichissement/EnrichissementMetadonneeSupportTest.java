/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml" })
public class EnrichissementMetadonneeSupportTest {

   @Autowired
   private EnrichissementMetadonneeSupport support;

   @Autowired
   private SAEEnrichmentMetadataService enrichmentService;

   @After
   public void reset() {
      EasyMock.reset(enrichmentService);
   }

   @Test(expected = IllegalArgumentException.class)
   public void checkDocumentObligatoire() {

      support.enrichirMetadonnee(null);

      Assert.fail("Sortie aspect attendue");
   }

   @Test(expected = CaptureMasseRuntimeException.class)
   public void testCodeRndErrone() throws SAEEnrichmentEx,
         ReferentialRndException, UnknownCodeRndEx {

      enrichmentService.enrichmentMetadata(EasyMock
            .anyObject(SAEDocument.class));
      EasyMock.expectLastCall().andThrow(
            new UnknownCodeRndEx("code RND incorrect"));
      EasyMock.replay(enrichmentService);

      SAEDocument document = new SAEDocument();

      support.enrichirMetadonnee(document);

   }

   @Test(expected = CaptureMasseRuntimeException.class)
   public void testEnrichissementErreur() throws SAEEnrichmentEx,
         ReferentialRndException, UnknownCodeRndEx {

      enrichmentService.enrichmentMetadata(EasyMock
            .anyObject(SAEDocument.class));
      EasyMock.expectLastCall().andThrow(
            new SAEEnrichmentEx("enrichissement en erreur"));
      EasyMock.replay(enrichmentService);

      SAEDocument document = new SAEDocument();

      support.enrichirMetadonnee(document);

   }

   @Test(expected = CaptureMasseRuntimeException.class)
   public void testReferentialRndException() throws SAEEnrichmentEx,
         ReferentialRndException, UnknownCodeRndEx {

      enrichmentService.enrichmentMetadata(EasyMock
            .anyObject(SAEDocument.class));
      EasyMock.expectLastCall().andThrow(
            new ReferentialRndException("référentiel RND en erreur"));
      EasyMock.replay(enrichmentService);

      SAEDocument document = new SAEDocument();

      support.enrichirMetadonnee(document);

   }

   @Test
   public void testEnrichirMetadonneesVirtuellesObligatoire() {

      try {
         support.enrichirMetadonneesVirtuelles(null);
         Assert.fail("exception IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue("le message doit être correct", exception
               .getMessage().contains("document virtuel"));

      } catch (Exception exception) {
         Assert.fail("exception IllegalArgumentException attendue");
      }

   }

}
