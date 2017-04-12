package fr.urssaf.image.sae.services.batch.transfert.support.controle.batch;

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
import fr.urssaf.image.sae.services.batch.transfert.support.controle.TransfertMasseControleSupport;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Item processor pour le contrôle des documents du fichier sommaire.xml pour le
 * service de transfert de masse
 * 
 */
@Component
public class ControleDocumentSommaireTransfertProcessor extends
      AbstractListener implements
      ItemProcessor<UntypedDocument, StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleDocumentSommaireTransfertProcessor.class);

   @Autowired
   private TransfertMasseControleSupport support;

   private StorageDocument document;

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument process(final UntypedDocument item) throws Exception {

      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);
      document = new StorageDocument();

      String uuidString = item.getUuid().toString();

      if (item.getBatchActionType().equals("SUPPRESSION")) {
         boolean isExiste = support.controleSAEDocumentSuppression(item);
         document.setUuid(item.getUuid());
         document.setBatchTypeAction(item.getBatchActionType());
         if (!isExiste) {
            if (isModePartielBatch()) {
               getCodesErreurListe().add(Constantes.ERR_BUL002);
               getIndexErreurListe().add(
                     getStepExecution().getExecutionContext().getInt(
                           Constantes.CTRL_INDEX));
               final String message = "Le document {0} n'existe pas. Suppression impossible.";
               getExceptionErreurListe().add(
                     new Exception(StringUtils.replace(message, "{0}",
                           uuidString)));
            } else {
               String message = "Le document {0} n'existe pas. Suppression impossible.";
               throw new ArchiveInexistanteEx(StringUtils.replace(message,
                     "{0}", uuidString));
            }
         }
      } else if (item.getBatchActionType().equals("TRANSFERT")) {
         try {
            document = support.controleSAEDocumentTransfert(item);
         } catch (Exception e) {
            if (isModePartielBatch()) {
               getCodesErreurListe().add(Constantes.ERR_BUL002);
               getIndexErreurListe().add(
                     getStepExecution().getExecutionContext().getInt(
                           Constantes.CTRL_INDEX));
               final String message = e.getMessage();
               getExceptionErreurListe().add(new Exception(message));
            } else {
               throw e;
            }
         }
      } else {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            final String message = "BatchTypeAction inconnu.";
            getExceptionErreurListe().add(new Exception(message));
            document = null;
         } else {
            String message = "BatchTypeAction inconnu.";
            throw new ArchiveInexistanteEx(message);
         }
      }

      return document;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   

}
