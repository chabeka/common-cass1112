/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.modification.support.controle.ModificationMasseControleSupport;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 * 
 */
@Component
public class ControleModificationDocumentProcessor extends AbstractListener
      implements ItemProcessor<UntypedDocument, StorageDocument> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ControleModificationDocumentProcessor.class);
   
   @Autowired
   private ModificationMasseControleSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument process(final UntypedDocument item) throws Exception {
      
      StorageDocument document = null;

      try {
         document = support.controleSAEDocumentModification(item);
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            final String message = e.getMessage();
            getExceptionErreurListe().add(new Exception(message));
            LOGGER.error(message, e);
         } else {
            throw e;
         }
      }

      return document;
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
