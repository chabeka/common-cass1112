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

import com.docubase.dfce.exception.runtime.DFCERuntimeException;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.AbstractDocumentWriterListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.modification.support.stockage.multithreading.ModificationPoolThreadExecutor;
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
public class ModificationDocumentWriter extends AbstractDocumentWriterListener implements ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ModificationDocumentWriter.class);

   /**
    * Pool executor
    */
   @Autowired
   private ModificationPoolThreadExecutor poolExecutor;

   /**
    * Provider pour la modification.
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   private int docIndexInWriter = 0;

   private static final String TRC_INSERT = "ModificationDocumentWriter()";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends StorageDocument> items) throws Exception {

      Runnable command;

      for (final StorageDocument storageDocument : Utils.nullSafeIterable(items)) {
         final boolean isdocumentATraite = isDocumentATraite(docIndexInWriter);
         // Si le document n'est pas en erreur ou dans la liste de document déjà
         // traité (Reprise), on traite, sinon on passe au
         // suivant.
         if (isdocumentATraite) {
            command = new InsertionRunnable(docIndexInWriter, storageDocument, this);

            try {
               poolExecutor.execute(command);
            }
            catch (final Exception e) {
               if (isModePartielBatch()) {
                  sendExceptionInPartielMode(e, docIndexInWriter);
                  LOGGER.warn("Ereur lors de la modification du document", e);
               }
               else {
                  throw e;
               }
            }

            LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
                  TRC_INSERT,
                  "Queue : " + poolExecutor.getQueue().size() + " - Total : " + poolExecutor.getTaskCount()
                  + " - Actifs : " + poolExecutor.getActiveCount());

         } else if (isDocumentDejaTraite(docIndexInWriter)) {
            poolExecutor.getIntegratedDocuments()
            .add(new TraitementMasseIntegratedDocument(
                  storageDocument.getUuid(),
                  null,
                  docIndexInWriter));
         }
         docIndexInWriter++;
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public UUID launchTraitement(final AbstractStorageDocument storageDocument, final int docIndex) throws Exception {
      StorageDocument document = null;
      
      // Récupère l'id du traitement en cours
      final String idJob = getStepExecution().getJobParameters().getString(Constantes.ID_TRAITEMENT);
      
      try {
         document = updateDocument((StorageDocument) storageDocument);
      }
      catch (Exception except) {
         final String message = "Erreur lors du traitement du document " + storageDocument.getUuid() + " : "
               + except.getMessage();
         if (isModePartielBatch()) {
        	 if(except.getMessage().isEmpty() && except instanceof DFCERuntimeException) {
        		 except = new Exception("Erreur DFCE - identifiant archivage " + idJob + " :");
        	 }
            sendExceptionInPartielMode(except, docIndex);
            LOGGER.warn(message, except);
         } else {
            throw new UpdateServiceEx(new Exception(message));
         }
      }
      return document != null ? document.getUuid() : null;
   }

   private void sendExceptionInPartielMode(final Exception e, final int docIndex) {
      synchronized (this) {
         getCodesErreurListe().add(Constantes.ERR_BUL002);
         getIndexErreurListe().add(docIndex);
         getErrorMessageList().add(e.getMessage());
      }
   }

   /**
    * Lance la modification du document
    * 
    * @param storageDocument
    *           document à modifier
    * @return le document avec l'uuid renseigné
    * @throws InsertionServiceEx
    *            Exception levée lors de la persistance
    */
   public final StorageDocument updateDocument(final StorageDocument storageDocument) throws UpdateServiceEx {

      // Récupère l'id du traitement en cours
      final String idJob = getStepExecution().getJobParameters().getString(Constantes.ID_TRAITEMENT);

      UUID uuidJob = null;
      if(StringUtils.isNotEmpty(idJob)){
         // conversion
         uuidJob = UUID.fromString(idJob);
      }
      serviceProvider.getStorageDocumentService()
      .updateStorageDocument(uuidJob,
            storageDocument.getUuid(),
            storageDocument.getMetadatas(),
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
