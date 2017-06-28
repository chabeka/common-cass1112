package fr.urssaf.image.sae.services.batch.transfert.support.controle.batch;

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
import fr.urssaf.image.sae.services.batch.transfert.support.controle.TransfertMasseControleSupport;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
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

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleDocumentSommaireTransfertProcessor.class);

   /**
    * Class de controle pour le transfert de masse
    */
   @Autowired
   private TransfertMasseControleSupport support;

   /**
    * bean StorageDocument
    */
   private StorageDocument document;

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument process(final UntypedDocument item) throws Exception {

      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);
      document = new StorageDocument();
      document.setUuid(item.getUuid());
      document.setBatchTypeAction(item.getBatchActionType());
      
      String uuidString = item.getUuid().toString();

      if (item.getBatchActionType().equals("SUPPRESSION")) {
         boolean isExiste = support.controleSAEDocumentSuppression(item);
         if (!isExiste && !isRepriseActifBatch()) {
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
         } else if (!isExiste && isRepriseActifBatch()) {
            getIndexRepriseDoneListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
         }
      } else if (item.getBatchActionType().equals("TRANSFERT")) {
         try {
            UUID idTraitementMasse = null;
            if (getStepExecution() != null
                  && getStepExecution().getJobParameters() != null
                  && getStepExecution().getJobParameters().getString(
                        Constantes.ID_TRAITEMENT) != null) {
               idTraitementMasse = UUID.fromString(getStepExecution()
                     .getJobParameters().getString(Constantes.ID_TRAITEMENT));
            }
            if (isRepriseActifBatch()) {
               document = support.controleSAEDocumentRepriseTransfert(item,
                     idTraitementMasse);
            } else {
               document = support.controleSAEDocumentTransfert(item,
                     idTraitementMasse);
            }
         } catch (TraitementRepriseAlreadyDoneException e) {
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
