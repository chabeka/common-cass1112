/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item writer de l'écriture des documents dans DFCE
 * 
 */
@Component
public class StorageDocumentWriter implements ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentWriter.class);

   @Autowired
   private InsertionPoolThreadExecutor poolExecutor;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   private StepExecution stepExecution;

   private static final String TRC_INSERT = "StorageDocumentWriter()";

   /**
    * initialisation du context
    * 
    * @param stepExecution
    *           context de l'étape
    */
   @SuppressWarnings("unchecked")
   @BeforeStep
   public final void init(StepExecution stepExecution) {

      this.stepExecution = stepExecution;

      try {
         serviceProvider.openConnexion();

      } catch (Exception e) {
         ExecutionContext jobExecution = stepExecution.getJobExecution()
               .getExecutionContext();
         List<String> codes = (List<String>) jobExecution
               .get(Constantes.CODE_EXCEPTION);
         List<Integer> index = (List<Integer>) jobExecution
               .get(Constantes.INDEX_EXCEPTION);
         List<Exception> exceptions = (List<Exception>) jobExecution
               .get(Constantes.DOC_EXCEPTION);

         codes.add(Constantes.ERR_BUL001);
         index.add(0);
         exceptions.add(new Exception(e.getMessage()));

         throw new CaptureMasseRuntimeException(e);
      }

      LOGGER.debug("{} - ouverture de la connexion DFCE", TRC_INSERT);
   }

   /**
    * Action executée après le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @AfterStep
   public final void end(final StepExecution stepExecution) {
      serviceProvider.closeConnexion();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends StorageDocument> items)
         throws Exception {

      Runnable command;
      int index = 0;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {

         command = new InsertionRunnable(this.stepExecution.getReadCount()
               + index, storageDocument, this);

         poolExecutor.execute(command);

         index++;

      }

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
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {

      try {
         final StorageDocument retour = serviceProvider
               .getStorageDocumentService().insertStorageDocument(
                     storageDocument);

         return retour;
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }

   }
}
