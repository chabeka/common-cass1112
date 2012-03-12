/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Implémentation du support {@link CaptureMasseControleSupport}
 * 
 */
@Component
public class CaptureMasseControleSupportImpl implements
      CaptureMasseControleSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void controleSAEDocument(UntypedDocument document, File ecdeDirectory)
         throws CaptureMasseSommaireFileNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx, UnknownCodeRndEx {
      
      // FIXME FBON - Implémentation controleSAEDocument

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void controleSAEDocumentStockage(SAEDocument document)
         throws RequiredStorageMetadataEx {
      // FIXME FBON - Implémentation controleSAEDocumentStockage

   }

}
