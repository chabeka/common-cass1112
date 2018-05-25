/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 * 
 */
@Component
public class ProcessorConvertMetier extends AbstractListener implements
      ItemProcessor<SAEDocument, StorageDocument> {

   @Autowired
   private MappingDocumentService mappingService;
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessorConvertMetier.class);  

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument process(final SAEDocument item)
         throws Exception {
      
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      try {
         // Si il y a déjà eu une erreur sur un document en mode partiel, on ne
         // cherche pas à continuer sur ce document
         if (!(isModePartielBatch() && getIndexErreurListe().contains(
               getStepExecution().getExecutionContext().getInt(
                     Constantes.CTRL_INDEX)))) {

            return mappingService.saeDocumentToStorageDocument(item);

         } else {
            StorageDocument storageDoc = new StorageDocument();
            storageDoc.setUuid(item.getUuid());
            storageDoc.setFilePath(item.getFilePath());
            return storageDoc;
         }
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            String message = "Une erreur est survenue lors de contrôle du document {0}";
            getErrorMessageList().add(e.getMessage());
            LOGGER.warn(StringUtils.replace(message,"{0}", item.getUuid().toString()),
                  e);
            StorageDocument storageDoc = new StorageDocument();
            storageDoc.setUuid(item.getUuid());
            return storageDoc;
         } else {
            throw e;
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
