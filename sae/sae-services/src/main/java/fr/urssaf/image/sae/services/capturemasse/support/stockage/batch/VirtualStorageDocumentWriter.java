/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
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
    * initialisation du context
    * 
    * @param stepExecution
    *           context de l'étape
    */
   @SuppressWarnings("unchecked")
   @BeforeStep
   public final void init(StepExecution stepExecution) {

      String trcPrefix = "init()";

      this.stepExecution = stepExecution;

      try {
         serviceProvider.openConnexion();

      } catch (Throwable e) {

         LOGGER.warn("{} - erreur de connexion à DFCE", trcPrefix, e);

         ExecutionContext jobExecution = stepExecution.getJobExecution()
               .getExecutionContext();
         ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) jobExecution
               .get(Constantes.CODE_EXCEPTION);
         ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) jobExecution
               .get(Constantes.INDEX_EXCEPTION);
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
               .get(Constantes.DOC_EXCEPTION);

         codes.add(Constantes.ERR_BUL001);
         index.add(0);
         exceptions.add(new Exception(e.getMessage()));

         stepExecution.setExitStatus(new ExitStatus("FAILED_NO_ROLLBACK"));

         throw new CaptureMasseRuntimeException(e);
      }

      LOGGER.debug("{} - ouverture de la connexion DFCE", trcPrefix);
   }

   /**
    * Action executée après le step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return un status de sortie
    */
   @AfterStep
   @SuppressWarnings("unchecked")
   public final ExitStatus end(final StepExecution stepExecution) {

      String trcPrefix = "end()";

      // pour l'instant nous avons fait le choix de propager l'erreur
      // pour ne pas la cacher et attérir dans un état en erreur

      ExitStatus exitStatus = stepExecution.getExitStatus();

      try {
         serviceProvider.closeConnexion();

      } catch (Throwable e) {

         LOGGER.warn("{} - erreur lors de la fermeture de la base de données",
               trcPrefix, e);

         ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) stepExecution
               .getJobExecution().getExecutionContext().get(
                     Constantes.CODE_EXCEPTION);

         ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) stepExecution
               .getJobExecution().getExecutionContext().get(
                     Constantes.INDEX_EXCEPTION);

         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) stepExecution
               .getJobExecution().getExecutionContext().get(
                     Constantes.DOC_EXCEPTION);

         codes.add(Constantes.ERR_BUL001);
         index.add(0);
         exceptions.add(new Exception(e.getMessage()));

         exitStatus = ExitStatus.FAILED;
      }

      return exitStatus;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(List<? extends VirtualStorageDocument> items)
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
