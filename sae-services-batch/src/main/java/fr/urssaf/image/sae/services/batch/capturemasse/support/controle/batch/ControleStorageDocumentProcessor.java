/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractDfceListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 */
@Component
public class ControleStorageDocumentProcessor extends AbstractDfceListener
                                              implements
                                              ItemProcessor<SAEDocument, SAEDocument> {

  @Autowired
  private CaptureMasseControleSupport support;

  @Autowired
  private InterruptionTraitementMasseSupport interruptionTraitementMasseSupport;

  @Autowired
  @Qualifier("interruption_traitement_masse")
  private InterruptionTraitementConfig interruptionConfig;

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(ControleStorageDocumentProcessor.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public final SAEDocument process(final SAEDocument item) throws Exception {

    final String trcPrefix = "process";
    LOGGER.debug("{} - début", trcPrefix);

    try {
      // Si il y a déjà eu une erreur sur un document en mode partiel, on ne
      // cherche pas à continuer sur ce document
      if (!(isModePartielBatch()
          && getIndexErreurListe().contains(getStepExecution().getExecutionContext()
                                                              .getInt(
                                                                      Constantes.CTRL_INDEX)))) {
        support.controleSAEDocumentStockage(item);
      }

      final String pathSommaire = getStepExecution().getJobExecution()
                                                    .getExecutionContext()
                                                    .getString(Constantes.SOMMAIRE_FILE);

      final File sommaireFile = new File(pathSommaire);
      final File ecdeDirectory = sommaireFile.getParentFile();

      final String path = ecdeDirectory.getAbsolutePath() + File.separator
          + "documents" + File.separator + item.getFilePath();
      item.setFilePath(path);
    }
    catch (final Exception e) {
      if (isModePartielBatch()) {
        getCodesErreurListe().add(Constantes.ERR_BUL002);
        getIndexErreurListe().add(
                                  getStepExecution().getExecutionContext()
                                                    .getInt(
                                                            Constantes.CTRL_INDEX));
        getErrorMessageList().add(e.getMessage());
        LOGGER.warn("Une erreur est survenue lors de contrôle des documents",
                    e);
      } else {
        throw e;
      }
    }
    return item;
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
