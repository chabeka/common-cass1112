/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle.batch;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
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
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
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
      
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);
      
      StorageDocument document = null;
      
      // Récuperer l'id du traitement en cours
      String idJob = (String) getStepExecution().getJobParameters()
            .getString(Constantes.ID_TRAITEMENT);
      
      UUID uuidJob = null;
      if(StringUtils.isNotEmpty(idJob)){
         // conversion
         uuidJob = UUID.fromString(idJob);
      }
      try {
         document = support.controleSAEDocumentModification(uuidJob, item);
         
      } catch (TraitementRepriseAlreadyDoneException e1){
         getIndexRepriseDoneListe().add(
             getStepExecution().getExecutionContext().getInt(
                   Constantes.CTRL_INDEX));
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            final String message = e.getMessage();
            getErrorMessageList().add(message);
            LOGGER.warn(message, e);
         } else {
            throw e;
         }
      }

      // Si le document est en erreur, on le créer avec les informations
      // minimums pour son traitement dans le writer.
      if (document == null) {
         document = new StorageDocument();
         document.setUuid(item.getUuid());
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
