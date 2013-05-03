/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionVirtualRunnable;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * ItemWriter des fichiers dzs documents virtuels dans DFCE
 * 
 */
@Component
public class VirtualStorageDocumentWriter implements
      ItemWriter<VirtualStorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualStorageDocumentWriter.class);

   @Autowired
   private InsertionPoolThreadVirtualExecutor poolExecutor;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   private StepExecution stepExecution;

   /**
    * Initialisation du step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {

      this.stepExecution = stepExecution;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void write(List<? extends VirtualStorageDocument> items)
         throws Exception {
      String trcPrefix = "write";
      LOGGER.debug("{} - début", trcPrefix);

      Runnable command;
      int index = 0;

      for (VirtualStorageDocument storageDocument : Utils
            .nullSafeIterable(items)) {

         command = new InsertionVirtualRunnable(this.stepExecution
               .getReadCount()
               + index, storageDocument, this);

         poolExecutor.execute(command);

         LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
               trcPrefix, poolExecutor.getQueue().size());

         index++;

      }

      LOGGER.debug("{} - fin", trcPrefix);

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
      String trcPrefix = "insertStorageDocument()";
      LOGGER.debug("{} - début", trcPrefix);

      try {
         final UUID uuid = serviceProvider.getStorageDocumentService()
               .insertVirtualStorageDocument(storageDocument);

         VirtualStorageDocument document = new VirtualStorageDocument();
         BeanUtils.copyProperties(document, storageDocument);
         document.setUuid(uuid);

         LOGGER.debug("{} - fin", trcPrefix);

         return document;

      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      } catch (Throwable except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);

      }

   }

}
