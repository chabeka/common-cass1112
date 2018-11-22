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
   * Class de controle pour le transfert de masse
   */
  @Autowired
  private TransfertMasseControleSupport support;

  @Autowired
  private StorageDocumentFromRecycleWriter serviceRestaureDoc;

  /**
   * bean StorageDocument
   */
  private StorageDocument document;

  @Autowired
  private InterruptionTraitementMasseSupport interruptionTraitementMasseSupport;

  @Autowired
  @Qualifier("interruption_traitement_masse")
  private InterruptionTraitementConfig interruptionConfig;

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageDocument process(final UntypedDocument item) throws Exception {

    final String trcPrefix = "process";
    LOGGER.debug("{} - début", trcPrefix);
    document = new StorageDocument();
    document.setUuid(item.getUuid());
    document.setBatchTypeAction(item.getBatchActionType());

    final String uuidString = item.getUuid().toString();
    UUID idTraitementMasse = null;
    if (getStepExecution() != null
        && getStepExecution().getJobParameters() != null
        && getStepExecution().getJobParameters()
                             .getString(
                                        Constantes.ID_TRAITEMENT) != null) {
      idTraitementMasse = UUID.fromString(getStepExecution()
                                                            .getJobParameters()
                                                            .getString(Constantes.ID_TRAITEMENT));
    }

    List<StorageMetadata> listeMetadataDocument = null;
    // -- On vérifie si le document n'est pas gelé
    if (item.getUuid() != null) {
      listeMetadataDocument = support.getListeStorageMetadatasWithGel(item.getUuid());
    }

    if (isFrozenDocument(listeMetadataDocument)) {
      final String frozenDocMsgException = "Le document {0} est gelé et ne peut pas être traité.";

      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexErreurListe().add(
                                getStepExecution().getExecutionContext()
                                                  .getInt(
                                                          Constantes.CTRL_INDEX));
      getErrorMessageList().add(StringUtils.replace(frozenDocMsgException,
                                                    "{0}",
                                                    uuidString));
      LOGGER.warn(StringUtils.replace(frozenDocMsgException,
                                      "{0}",
                                      uuidString),
                  new TransfertException(StringUtils.replace(frozenDocMsgException,
                                                             "{0}",
                                                             uuidString)));
    } else if (item.getBatchActionType().equals("SUPPRESSION")) {
      final boolean isExiste = support.controleSAEDocumentSuppression(item);
      if (isExiste) {
        try {
          document = support.controleSAEDocumentTransfert(item,
                                                          idTraitementMasse);
        }
        catch (final TraitementRepriseAlreadyDoneException e) {
          getIndexRepriseDoneListe().add(
                                         getStepExecution().getExecutionContext()
                                                           .getInt(
                                                                   Constantes.CTRL_INDEX));
        }
        catch (final Exception e) {
          if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                                      getStepExecution().getExecutionContext()
                                                        .getInt(Constantes.CTRL_INDEX));
            getErrorMessageList().add(e.getMessage());
            LOGGER.warn("Une erreur est survenue lors de contrôle des documents",
                        e);
          } else {
            throw e;
          }
        }
      } else if (!isExiste && !isRepriseActifBatch()) {
        if (isModePartielBatch()) {
          getCodesErreurListe().add(Constantes.ERR_BUL002);
          getIndexErreurListe().add(
                                    getStepExecution().getExecutionContext()
                                                      .getInt(
                                                              Constantes.CTRL_INDEX));
          final String message = "Le document {0} n'existe pas. Suppression impossible.";
          getErrorMessageList().add(StringUtils.replace(message,
                                                        "{0}",
                                                        uuidString));
          LOGGER.warn(StringUtils.replace(message,
                                          "{0}",
                                          uuidString),
                      new ArchiveInexistanteEx(StringUtils.replace(message,
                                                                   "{0}",
                                                                   uuidString)));
        } else {
          final String message = "Le document {0} n'existe pas. Suppression impossible.";
          throw new ArchiveInexistanteEx(StringUtils.replace(message,
                                                             "{0}",
                                                             uuidString));
        }
      } else if (!isExiste && isRepriseActifBatch()) {
        try {
          final List<StorageMetadata> metadatasStorageDoc = serviceRestaureDoc
                                                                              .getMetadatasDocFromRecycleBean(document);

          final String idTransfertMasseInterne = StorageMetadataUtils
                                                                     .valueMetadataFinder(
                                                                                          metadatasStorageDoc,
                                                                                          StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE
                                                                                                                                              .getShortCode());

          // Si document existe en corbeille et a été supprimé par la transfert à reprendre
          if (StringUtils.isNotEmpty(idTransfertMasseInterne)
              && idTransfertMasseInterne.equals(idTraitementMasse
                                                                 .toString())) {
            final String message = "Le document {0} a déjà été supprimé par le traitement de transfert de masse en cours ({1})";
            final String messageFormat = StringUtils.replaceEach(message,
                                                                 new String[] {"{0}", "{1}"},
                                                                 new String[] {
                                                                               item.getUuid().toString(),
                                                                               idTraitementMasse.toString()});
            LOGGER.warn(messageFormat);
            getIndexRepriseDoneListe().add(
                                           getStepExecution().getExecutionContext()
                                                             .getInt(Constantes.CTRL_INDEX));
          }
          // Sinon, Si le document a déjà été supprimé par un transfert différent
          else if (StringUtils.isNotEmpty(idTransfertMasseInterne)) {
            // On retourne une exception ArchiveInexistanteEx
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                                      getStepExecution().getExecutionContext()
                                                        .getInt(
                                                                Constantes.CTRL_INDEX));
            final String message = "Le document {0} n'existe pas. Suppression impossible.";
            getErrorMessageList().add(StringUtils.replace(message,
                                                          "{0}",
                                                          uuidString));
            LOGGER.warn(StringUtils.replace(message,
                                            "{0}",
                                            uuidString),
                        new ArchiveInexistanteEx(StringUtils.replace(message,
                                                                     "{0}",
                                                                     uuidString)));
          } else {
            document.getMetadatas()
                    .add(new StorageMetadata(
                                             StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE
                                                                                                 .getShortCode(),
                                             idTraitementMasse.toString()));
          }
        }
        catch (final ArchiveInexistanteEx e) {
          if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                                      getStepExecution().getExecutionContext()
                                                        .getInt(Constantes.CTRL_INDEX));
            final String message = "Le document {0} n'existe pas. Suppression impossible.";
            getErrorMessageList().add(StringUtils.replace(message,
                                                          "{0}",
                                                          uuidString));
            LOGGER.warn(StringUtils.replace(message,
                                            "{0}",
                                            uuidString),
                        new ArchiveInexistanteEx(StringUtils.replace(message,
                                                                     "{0}",
                                                                     uuidString)));
          } else {
            throw e;
          }
        }
        catch (final Exception e) {
          if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                                      getStepExecution().getExecutionContext()
                                                        .getInt(Constantes.CTRL_INDEX));
            final String message = "Une exception a eu lieu lors de la récupération des métadonnées du document de la corbeille";
            getErrorMessageList().add(message);
            LOGGER.warn(message, e);
          } else {
            throw e;
          }
        }

      }
    } else if (item.getBatchActionType().equals("TRANSFERT")) {
      try {
        if (isRepriseActifBatch()) {
          document = support.controleSAEDocumentRepriseTransfert(
                                                                 item,
                                                                 idTraitementMasse);
        } else {
          document = support.controleSAEDocumentTransfert(item,
                                                          idTraitementMasse);
        }
      }
      catch (final TraitementRepriseAlreadyDoneException e) {
        getIndexRepriseDoneListe().add(
                                       getStepExecution().getExecutionContext()
                                                         .getInt(
                                                                 Constantes.CTRL_INDEX));
      }
      catch (final Exception e) {
        if (isModePartielBatch()) {
          getCodesErreurListe().add(Constantes.ERR_BUL002);
          getIndexErreurListe().add(
                                    getStepExecution().getExecutionContext()
                                                      .getInt(
                                                              Constantes.CTRL_INDEX));
          final String message = e.getMessage();
          getErrorMessageList().add(message);
          LOGGER.warn(message, e);
        } else {
          throw e;
        }
      }
    } else {
      if (isModePartielBatch()) {
        getCodesErreurListe().add(Constantes.ERR_BUL002);
        getIndexErreurListe().add(
                                  getStepExecution().getExecutionContext()
                                                    .getInt(
                                                            Constantes.CTRL_INDEX));
        final String message = "BatchTypeAction inconnu.";
        getErrorMessageList().add(message);
        LOGGER.warn(message, new Exception(message));
        document = null;
      } else {
        final String message = "BatchTypeAction inconnu.";
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
