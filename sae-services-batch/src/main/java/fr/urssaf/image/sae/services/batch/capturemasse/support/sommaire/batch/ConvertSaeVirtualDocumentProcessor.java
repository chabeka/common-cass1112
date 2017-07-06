/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.batch.capturemasse.model.SaeListCaptureMasseReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Classe de transformation d'un objet {@link SAEVirtualDocument} en
 * {@link VirtualStorageDocument}
 * 
 */
@Component
public class ConvertSaeVirtualDocumentProcessor implements
      ItemProcessor<SAEVirtualDocument, VirtualStorageDocument> {

   private static final Logger LOG = LoggerFactory
         .getLogger(ConvertSaeVirtualDocumentProcessor.class);

   @Autowired
   private MappingDocumentService documentService;

   @Autowired
   private SaeListCaptureMasseReferenceFile references;

   /**
    * {@inheritDoc}
    */
   @Override
   public final VirtualStorageDocument process(SAEVirtualDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOG.debug("{} - début", trcPrefix);

      VirtualStorageDocument document = documentService
            .saeVirtualDocumentToVirtualStorageDocument(item);
      document.setReferenceFile(references.get(item.getIndex()).getReference());

      LOG.debug("{} - fin", trcPrefix);

      return document;
   }

}
