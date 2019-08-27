package fr.urssaf.image.sae.services.batch.transfert.support.controle.batch;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractDfceListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.restore.support.stockage.batch.StorageDocumentFromRecycleWriter;
import fr.urssaf.image.sae.services.batch.transfert.support.controle.TransfertMasseControleSupport;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * Item processor pour le contrôle des documents du fichier sommaire.xml pour le
 * service de transfert de masse
 */
@Component
public class ControleDocumentSommaireTransfertProcessor extends
AbstractDfceListener implements
ItemProcessor<UntypedDocument, StorageDocument> {

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleDocumentSommaireTransfertProcessor.class);

   /**
    * Class de contrôle pour le transfert de masse
    */
   @Autowired
   private TransfertMasseControleSupport support;

   @Autowired
   private StorageDocumentFromRecycleWriter serviceRestaureDoc;

   @Autowired
   private InterruptionTraitementMasseSupport interruptionTraitementMasseSupport;

   @Autowired
   @Qualifier("interruption_traitement_masse")
   private InterruptionTraitementConfig interruptionConfig;

   /**
    * {@inheritDoc}
    * Convertit un UntypedDocument directement issu du fichier sommaire.xml en un StorageDocument.
    * Au passage, on effectue les contrôles métier.
    * En mode "partiel", on renvoie null en cas d'erreur sur le document.
    * En mode "tout ou rien", on envoie une exception en cas d'erreur sur le document.
    * Attention : le mode tout en rien n'est en réalité pas implémenté et pas testé.
    */
   @Override
   public StorageDocument process(final UntypedDocument item) throws Exception {

      final String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      if (item.getUuid() == null) {
         final String message = "L'UUID du document n'a pas pu être trouvé. Ce document ne peut donc pas être traité.";
         return sendException(message, new TransfertException(message));
      }

      final String uuidString = item.getUuid().toString();
      UUID idTraitementMasse = null;
      if (getStepExecution() != null
            && getStepExecution().getJobParameters() != null
            && getStepExecution().getJobParameters().getString(Constantes.ID_TRAITEMENT) != null) {
         idTraitementMasse = UUID.fromString(getStepExecution().getJobParameters().getString(Constantes.ID_TRAITEMENT));
      }

      // Les contrôles dépendent de l'action demandée sur le document : suppression ou transfert
      if (item.getBatchActionType().equals("SUPPRESSION")) {
         return controlsForSuppression(item, uuidString, idTraitementMasse);
      } else if (item.getBatchActionType().equals("TRANSFERT")) {
         return controlsForTransfert(item, idTraitementMasse);
      } else {
         final String message = "BatchTypeAction inconnu : " + item.getBatchActionType();
         return sendException(message, new TransfertException(message));
      }

   }

   /**
    * Effectue les contrôles dans le cas d'une demande de transfert du document
    * 
    * @param item
    * @param idTraitementMasse
    * @throws Exception
    */
   private StorageDocument controlsForTransfert(final UntypedDocument item, final UUID idTraitementMasse) throws Exception {
      try {
         if (isRepriseActifBatch()) {
            return support.controleSAEDocumentRepriseTransfert(item, idTraitementMasse);
         } else {
            return support.controleSAEDocumentTransfert(item, idTraitementMasse);
         }
      }
      catch (final TraitementRepriseAlreadyDoneException e) {
         // En mode reprise : ce document est détecté comme étant déjà transféré. On le met dans la liste adéquate
         getIndexRepriseDoneListe().add(getIndexOfCurrentDocument());
         // Dans ce cas là, on ne renvoie pas nul car il ne s'agit pas d'une erreur.
         // On renvoie le document, mais sans ces métadonnées dont on n'a pas besoin par la suite
         return getEmptyDocumentFromItem(item);
      }
      catch (final Exception e) {
         return sendException(e.getMessage(), e);
      }
   }

   /**
    * Renvoie l'index du document en cours de traitement. Il s'agit de la postion du document
    * dans le fichier sommaire.xml
    * 
    * @return
    */
   private int getIndexOfCurrentDocument() {
      return getStepExecution().getExecutionContext().getInt(Constantes.CTRL_INDEX);
   }

   /**
    * Permet d'instancier un StorageDocument sans métadonnées
    * 
    * @param item
    *           permet d'avoir l'identifidant du StorageDocument à instancier
    * @return
    */
   private StorageDocument getEmptyDocumentFromItem(final UntypedDocument item) {
      final StorageDocument document = new StorageDocument();
      document.setUuid(item.getUuid());
      document.setBatchTypeAction(item.getBatchActionType());
      return document;
   }

   /**
    * Effectue les contrôles dans le cas d'une demande de suppression du document
    * 
    * @param item
    * @param uuidString
    * @param idTraitementMasse
    * @return
    *         Renvoie un StorageDocument, qui contient les métadonnées que l'on veut modifier avant mise à la corbeille du document
    */
   private StorageDocument controlsForSuppression(final UntypedDocument item, final String uuidString, final UUID idTraitementMasse)
         throws Exception {

      final boolean docExists = support.controleSAEDocumentSuppression(item);
      if (!docExists) {
         return handleDeletionOfInexistantDocument(item, uuidString, idTraitementMasse);
      }
      try {
         return support.controleSAEDocumentTransfert(item, idTraitementMasse);
      }
      catch (final TraitementRepriseAlreadyDoneException e) {
         getIndexRepriseDoneListe().add(getIndexOfCurrentDocument());
         return getEmptyDocumentFromItem(item);
      }
      catch (final Exception e) {
         return sendException("Une erreur est survenue lors du contrôle des documents", e);
      }
   }

   private StorageDocument handleDeletionOfInexistantDocument(final UntypedDocument item, final String uuidString, final UUID idTraitementMasse)
         throws Exception {

      if (!isRepriseActifBatch()) {
         final String message = getAlreadyDeletedDocumentErrorMessage(uuidString);
         return sendException(message, new ArchiveInexistanteEx(message));
      }

      try {
         // On regarde si le document se trouve dans la corbeille
         final StorageDocument document = getEmptyDocumentFromItem(item);
         final List<StorageMetadata> metadatasStorageDoc = serviceRestaureDoc.getMetadatasDocFromRecycleBean(document);

         final String idTransfertMasseInterne = StorageMetadataUtils
               .valueMetadataFinder(
                                    metadatasStorageDoc,
                                    StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE
                                    .getShortCode());

         if (StringUtils.isNotEmpty(idTransfertMasseInterne) && idTransfertMasseInterne.equals(idTraitementMasse.toString())) {
            // Le document existe en corbeille et a été supprimé par la transfert à reprendre
            final String message = "Le document {0} a déjà été supprimé par le traitement de transfert de masse en cours ({1})";
            final String messageFormat = StringUtils.replaceEach(message,
                                                                 new String[] {"{0}", "{1}"},
                                                                 new String[] {
                                                                               item.getUuid().toString(),
                                                                               idTraitementMasse.toString()});
            LOGGER.warn(messageFormat);
            getIndexRepriseDoneListe().add(getIndexOfCurrentDocument());
            return document;
         } else if (StringUtils.isNotEmpty(idTransfertMasseInterne)) {
            // le document a déjà été supprimé par un transfert différent
            // On retourne une exception ArchiveInexistanteEx
            final String message = getAlreadyDeletedDocumentErrorMessage(uuidString);
            return sendException(message, new ArchiveInexistanteEx(message));
         } else {
            // Le doc est dans la corbeille mais n'a pas d'id de traitement de transfert. On le rajoute
            document.getMetadatas()
            .add(new StorageMetadata(StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode(),
                                     idTraitementMasse.toString()));
            return document;
         }
      }
      catch (final ArchiveInexistanteEx e) {
         final String message = getAlreadyDeletedDocumentErrorMessage(uuidString);
         return sendException(message, new ArchiveInexistanteEx(message));
      }
      catch (final Exception e) {
         final String message = "Une exception a eu lieu lors de la récupération des métadonnées du document de la corbeille";
         return sendException(message, e);
      }

   }

   private String getAlreadyDeletedDocumentErrorMessage(final String uuidString) {
      return "Le document " + uuidString + " n'existe pas. Suppression impossible.";
   }

   /**
    * En fonction du mode du batch : envoie une exception, ou log l'exception et renvoie null.
    * En l'occurrence, le batch est forcément en mode partiel car le mode "tout ou rien" n'est pas implémenté
    * 
    * @param message
    *           le message d'erreur à logger
    * @param e
    *           l'exception à lancer
    * @throws Exception
    */
   private StorageDocument sendException(final String message, final Exception e) throws Exception {
      if (isModePartielBatch()) {
         getCodesErreurListe().add(Constantes.ERR_BUL002);
         getIndexErreurListe().add(getIndexOfCurrentDocument());
         getErrorMessageList().add(message);
         LOGGER.warn(message, e);
         return null;
      } else {
         throw e;
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // Pas d'initialisation spécifique
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected InterruptionTraitementMasseSupport getInterruptionTraitementSupport() {
      return interruptionTraitementMasseSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected InterruptionTraitementConfig getInterruptionConfig() {
      return interruptionConfig;
   }

}
