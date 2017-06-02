/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.stockage.batch;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.AbstractDocumentWriterListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.support.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item writer de la modification des documents dans DFCE
 * 
 */
@Component
public class ModificationDocumentWriter extends AbstractDocumentWriterListener
      implements ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ModificationDocumentWriter.class);

   /**
    * Pool executor
    */
   @Autowired
   private InsertionPoolThreadExecutor poolExecutor;

   /**
    * Provider pour la modification.
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   /**
    * Gestionnaire des traces.
    */
   @Autowired
   private TracesControlesSupport tracesSupport;

   private static final String TRC_INSERT = "ModificationDocumentWriter()";
   private static final String CATCH = "AvoidCatchingThrowable";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends StorageDocument> items)
         throws Exception {

      Runnable command;
      int index = 0;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {
         boolean isdocumentATraite = isDocumentATraite(index);
         // Si le document n'est pas en erreur ou dans la liste de document déjà
         // traité (Reprise), on traite, sinon on passe au
         // suivant.
         if (isdocumentATraite) {
            command = new InsertionRunnable(getStepExecution().getReadCount()
                  + index, storageDocument, this);

            try {
               poolExecutor.execute(command);
            } catch (Exception e) {
               if (isModePartielBatch()) {
                  getCodesErreurListe().add(Constantes.ERR_BUL002);
                  getIndexErreurListe().add(
                        getStepExecution().getExecutionContext().getInt(
                              Constantes.CTRL_INDEX));
                  final String message = e.getMessage();
                  getExceptionErreurListe().add(new Exception(message));
                  LOGGER.error(message, e);
               }
            }

            LOGGER.debug(
                  "{} - nombre de documents en attente dans le pool : {}",
                  TRC_INSERT, poolExecutor.getQueue().size());

         }
         index++;

      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public UUID launchTraitement(AbstractStorageDocument storageDocument)
         throws Exception {
      StorageDocument document = null;
      try {
         document = insertStorageDocument((StorageDocument) storageDocument);
      } catch (Throwable except) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            final String message = "Erreur DFCE : " + except.getMessage();
            getExceptionErreurListe().add(new Exception(message));
            LOGGER.error(message, except);
         } else {
            throw new UpdateServiceEx(new Exception("Erreur DFCE : "
                  + except.getMessage()));
         }
      }

      return document != null ? document.getUuid() : null;
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
   @SuppressWarnings(CATCH)
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws UpdateServiceEx {

      // Récuperer l'id du traitement en cours
      String idJob = (String) getStepExecution().getJobParameters()
            .getString(Constantes.ID_TRAITEMENT);
      
      UUID uuidJob = null;
      if(StringUtils.isNotEmpty(idJob)){
         // conversion
         uuidJob = UUID.fromString(idJob);
      }
      serviceProvider.getStorageDocumentService().updateStorageDocument(
            uuidJob, storageDocument.getUuid(), storageDocument.getMetadatas(),
            storageDocument.getMetadatasToDelete());

      return storageDocument;
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
