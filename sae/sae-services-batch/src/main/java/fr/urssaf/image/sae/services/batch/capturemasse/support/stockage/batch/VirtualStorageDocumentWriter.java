/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionVirtualRunnable;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * ItemWriter des fichiers dzs documents virtuels dans DFCE
 * 
 */
@Component
public class VirtualStorageDocumentWriter extends
AbstractDocumentWriterListener implements
ItemWriter<VirtualStorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualStorageDocumentWriter.class);

   @Autowired
   private InsertionPoolThreadVirtualExecutor poolExecutor;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends VirtualStorageDocument> items)
         throws Exception {
      final String trcPrefix = "write";
      LOGGER.debug("{} - début", trcPrefix);

      Runnable command;
      int index = 0;

      for (final VirtualStorageDocument storageDocument : Utils
            .nullSafeIterable(items)) {

         command = new InsertionVirtualRunnable(getStepExecution()
               .getReadCount()
               + index, storageDocument, this);

         poolExecutor.execute(command);

         LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
               trcPrefix, poolExecutor.getQueue().size());

         index++;

      }

      LOGGER.debug("{} - fin", trcPrefix);

   }

   @Override
   public UUID launchTraitement(final AbstractStorageDocument storageDocument, final int docIndex)
         throws Exception {
      // Non utilisé pour les documents virtuels. A mettre en place si besoin pour rendre le code plus générique.
      // VirtualStorageDocument document = insertStorageDocument((VirtualStorageDocument) storageDocument);
      // UUID uuid = document != null ? document.getUuid() : null;

      return null;
   }

   /**
    * Persistance du document
    * 
    * @param storageDocument
    *           document à sauvegarder
    * @return le document avec l'uuid renseigné
    * @throws InsertionServiceEx
    *            Exception levée lors de la persistance
    */
   public final VirtualStorageDocument insertStorageDocument(
         final VirtualStorageDocument storageDocument)
               throws InsertionServiceEx {
      final String trcPrefix = "insertStorageDocument()";
      LOGGER.debug("{} - début", trcPrefix);

      try {
         final UUID uuid = serviceProvider.getStorageDocumentService()
               .insertVirtualStorageDocument(storageDocument);

         final VirtualStorageDocument document = new VirtualStorageDocument();
         BeanUtils.copyProperties(document, storageDocument);
         document.setUuid(uuid);

         LOGGER.debug("{} - fin", trcPrefix);

         return document;

      } catch (final Exception except) {

         throw new InsertionServiceEx("SAE-ST-INS001", except.getMessage(),
               except);

      } 

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final StorageServiceProvider getServiceProvider() {
      return serviceProvider;
   }

}
