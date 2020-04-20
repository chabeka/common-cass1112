/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.support.controle.sommaire;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.Constantes.BATCH_MODE;

/**
 * Tasklet de vérification des doublons dans le fichier sommaire.xml
 */
@Component
public class CheckDoublonSommaireTasklet extends AbstractCaptureMasseTasklet {

  /**
   * Logger
   */
  protected static final Logger LOGGER = LoggerFactory
                                                      .getLogger(CheckDoublonSommaireTasklet.class);

  /**
   * Nom methode trace
   */
  protected static final String TRC_EXEC = "execute()";

  /**
   * Support pour validation
   */
  private final SommaireFormatValidationSupport validationSupport;

  /**
  * 
  */
  @Autowired
  public CheckDoublonSommaireTasklet(final SommaireFormatValidationSupport validationSupport) {
    this.validationSupport = validationSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final RepeatStatus execute(final StepContribution contribution,
                                    final ChunkContext chunkContext) {

    LOGGER.debug("{} - Début de méthode", TRC_EXEC);
    final StepExecution stepExecution = chunkContext.getStepContext()
                                                    .getStepExecution();
    final ExecutionContext context = stepExecution.getJobExecution()
                                                  .getExecutionContext();

    // Mode PARTIEL uniquement
    if (BATCH_MODE.PARTIEL.getModeNom().equals(context.get(Constantes.BATCH_MODE_NOM))) {
      final String sommairePath = context.getString(Constantes.SOMMAIRE_FILE);
      final File sommaireFile = new File(sommairePath);
      ConcurrentLinkedQueue<String> listTagDoublonsCheckList = null;
      List<Integer> documentIndexDoublons = new ArrayList<>();

      try {
        if (context.containsKey(Constantes.META_DOUBLON_CHECK_LIST)) {
          // Verification des doublons dans les métadonnées entre documents
          listTagDoublonsCheckList = (ConcurrentLinkedQueue<String>) context.get(Constantes.META_DOUBLON_CHECK_LIST);

          LOGGER.debug("{} - Début de validation doublons du fichier sommaire.xml",
                       TRC_EXEC);
          for (final String nomMeta : listTagDoublonsCheckList) {
            if (StringUtils.isNotEmpty(nomMeta)) {
              // Vérification des doublons sur la metadonnée indiquée.
              documentIndexDoublons = validationSupport.validerUniciteMeta(sommaireFile, nomMeta);
              addDocumentException(nomMeta, documentIndexDoublons, context);
            }
          }
        } else if (context.containsKey(Constantes.TAG_DOUBLON_CHECK_LIST)) {
          // Verification des doublons sur la valeur d'un tag donnée entre documents
          listTagDoublonsCheckList = (ConcurrentLinkedQueue<String>) context.get(Constantes.TAG_DOUBLON_CHECK_LIST);

          LOGGER.debug("{} - Début de validation doublons du fichier sommaire.xml",
                       TRC_EXEC);
          for (final String nomMeta : listTagDoublonsCheckList) {
            if (StringUtils.isNotEmpty(nomMeta)) {
              // Vérification des doublons sur la metadonnée indiquée.
              documentIndexDoublons = validationSupport.validerUniciteTag(sommaireFile, nomMeta);
              addDocumentException(nomMeta, documentIndexDoublons, context);
            }
          }
        }
      }
      catch (final CaptureMasseRuntimeException e) {
        getErrorMessageList(chunkContext).add(e.getMessage());
        logFailedValidationSommaire(e);
        LOGGER.warn("{} - Erreur lors du controle des doublons : " + e.getMessage(), TRC_EXEC);
      }
      catch (final IOException e) {
        getErrorMessageList(chunkContext).add(e.getMessage());
        logFailedValidationSommaire(e);
      }
      finally {
        LOGGER.debug("{} - Fin de méthode", TRC_EXEC);
      }
    }

    return RepeatStatus.FINISHED;
  }

  /**
   * Permet d'ajouter une exception sur les documents en erreur.
   * 
   * @param nomTag
   *          Nom du tag en erreur
   * @param documentIndexDoublons
   *          Liste d'index en doublons
   * @param context
   *          Contexte spring batch
   */
  private void addDocumentException(final String nomTag, final List<Integer> documentIndexDoublons, final ExecutionContext context) {
    if (ArrayUtils.isNotEmpty(documentIndexDoublons.toArray())) {
      // Si la métadonnée est en doublon, on ne mets pas en echec le traitement, mais on remonte les indexes des documents en doublons.
      final ConcurrentLinkedQueue<Integer> listIndexException = (ConcurrentLinkedQueue<Integer>) context.get(Constantes.INDEX_EXCEPTION);
      final ConcurrentLinkedQueue<String> listCodeException = (ConcurrentLinkedQueue<String>) context.get(Constantes.CODE_EXCEPTION);
      final ConcurrentLinkedQueue<String> listMessageException = (ConcurrentLinkedQueue<String>) context.get(Constantes.DOC_EXCEPTION);
      for (final Integer indexDocDoublon : documentIndexDoublons) {
        listIndexException.add(indexDocDoublon);
        // Erreur fonctionnelle
        listCodeException.add(Constantes.ERR_BUL002);
        // Message d'erreur
        final String messageError = "La métadonnée " + nomTag
            + " est en doublon dans le sommaire.xml. Une procédure d'exploitation doit être initialisée afin de rejouer le traitement en echec.";
        LOGGER.debug("{} - " + messageError, TRC_EXEC);
        listMessageException.add(messageError);
      }
    }

  }

  /**
   * @param e
   */
  private void logFailedValidationSommaire(final Exception e) {
    LOGGER.warn(TRC_EXEC + " - Erreur lors du controle des doublons : ", e);
  }

}
