/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Implémentation du support {@link EnrichissementMetadonneeSupport}
 * 
 */
@Component
public class EnrichissementMetadonneeSupportImpl implements
      EnrichissementMetadonneeSupport {

   @Autowired
   private SAEEnrichmentMetadataService service;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void enrichirMetadonnee(final SAEDocument document) {

      try {
         service.enrichmentMetadata(document);
      } catch (SAEEnrichmentEx e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (ReferentialRndException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (UnknownCodeRndEx e) {
         throw new CaptureMasseRuntimeException(e);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void enrichirMetadonneesVirtuelles(
         SAEVirtualDocument saeVirtualDocument) {

      try {
         service.enrichmentVirtualMetadata(saeVirtualDocument);

      } catch (SAEEnrichmentEx e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (ReferentialRndException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (UnknownCodeRndEx e) {
         throw new CaptureMasseRuntimeException(e);
      }

   }

}
