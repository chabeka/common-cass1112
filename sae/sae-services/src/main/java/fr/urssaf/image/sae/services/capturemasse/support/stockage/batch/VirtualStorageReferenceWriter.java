/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseReferenceFile;
import fr.urssaf.image.sae.services.capturemasse.model.SaeListCaptureMasseReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * ItemWriter pour les fichiers de référence dans DFCE
 * 
 */
@Component
public class VirtualStorageReferenceWriter implements
      ItemWriter<VirtualStorageReference> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualStorageReferenceWriter.class);

   @Autowired
   private StorageServiceProvider storageServiceProvider;

   @Autowired
   private SaeListCaptureMasseReferenceFile saeListCaptureMasseReferenceFile;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(List<? extends VirtualStorageReference> items)
         throws Exception {
      String trcPrefix = "write";
      LOGGER.debug("{} - début", trcPrefix);

      for (VirtualStorageReference virtualStorageReference : items) {
         LOGGER
               .debug("{} - Insertion du fichier de référence {}",
                     new Object[] { trcPrefix,
                           virtualStorageReference.getFilePath() });
         
         StorageReferenceFile storageReference = storageServiceProvider
               .getStorageDocumentService().insertStorageReference(
                     virtualStorageReference);

         LOGGER.debug("{} - Insertion du fichier de référence "
               + "dans la liste des éléments insérés dans DFCE", new Object[] {
               trcPrefix, virtualStorageReference.getFilePath() });
         
         CaptureMasseReferenceFile referenceFile = new CaptureMasseReferenceFile();
         referenceFile.setReference(storageReference);
         referenceFile.setFileName(FilenameUtils
               .getBaseName(virtualStorageReference.getFilePath()));
         saeListCaptureMasseReferenceFile.add(referenceFile);
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

}
