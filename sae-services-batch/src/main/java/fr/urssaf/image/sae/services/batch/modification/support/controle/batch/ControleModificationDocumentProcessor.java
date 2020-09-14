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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractDfceListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.modification.support.controle.ModificationMasseControleSupport;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 */
@Component
public class ControleModificationDocumentProcessor extends AbstractDfceListener
                                                   implements
                                                   ItemProcessor<UntypedDocument, StorageDocument> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControleModificationDocumentProcessor.class);

  @Autowired
  private ModificationMasseControleSupport support;

  @Autowired
  private InterruptionTraitementMasseSupport interruptionTraitementMasseSupport;

  @Autowired
  @Qualifier("interruption_traitement_masse")
  private InterruptionTraitementConfig interruptionConfig;

  /**
   * {@inheritDoc}
   */
  @Override
  public final StorageDocument process(final UntypedDocument item) throws Exception {

    final String trcPrefix = "process";
    LOGGER.debug("{} - début", trcPrefix);

    StorageDocument document = null;

    if (item.getUuid() == null) {
      final String UUIDNullException = "L'UUID du document n'a pas pu être trouvé. Ce document ne peut donc pas être traité.";

      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexErreurListe().add(
                                getStepExecution().getExecutionContext()
                                                  .getInt(
                                                          Constantes.CTRL_INDEX));
      getErrorMessageList().add(UUIDNullException);
      LOGGER.warn(UUIDNullException,
                  new TransfertException(UUIDNullException));

      // Si le document est en erreur, on le créer avec les informations
      // minimums pour son traitement dans le writer.
      if (isModePartielBatch()) {
        document = new StorageDocument();
      }
    } else {
      // Récuperer l'id du traitement en cours
      final String idJob = getStepExecution().getJobParameters()
                                             .getString(Constantes.ID_TRAITEMENT);

      UUID uuidJob = null;
      if (StringUtils.isNotEmpty(idJob)) {
        // conversion
        uuidJob = UUID.fromString(idJob);
      }
      try {
        document = support.controleSAEDocumentModification(uuidJob, item);
      }
      catch (final TraitementRepriseAlreadyDoneException e1) {
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

      // Si le document est en erreur, on le créer avec les informations
      // minimums pour son traitement dans le writer.
      if (document == null) {
        document = new StorageDocument();
        document.setUuid(item.getUuid());
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
