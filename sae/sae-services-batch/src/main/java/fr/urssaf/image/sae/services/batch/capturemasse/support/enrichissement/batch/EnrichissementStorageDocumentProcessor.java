/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * ItemProcessor renseignant la métadonnée IdTraitementMasseInterne pour le
 * StorageDocument
 */
@Component
public class EnrichissementStorageDocumentProcessor implements
      ItemProcessor<StorageDocument, StorageDocument> {

   @Autowired
   private EnrichissementStorageDocumentSupport support;
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(EnrichissementStorageDocumentProcessor.class);

   private StepExecution stepExecution;
   private String uuid;
   private String batchMode;

   /**
    * initialisation avant le début du Step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      this.uuid = stepExecution.getJobParameters().getString(
            Constantes.ID_TRAITEMENT);
      this.batchMode = (String) stepExecution.getJobExecution()
            .getExecutionContext().get(Constantes.BATCH_MODE_NOM_REDIRECT);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument process(StorageDocument item) throws Exception {
      
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);
      
      LOGGER.debug("{} - ItemProcessor renseignant la métadonnée IdTraitementMasseInterne pour le document",
            trcPrefix);
      
      try {
         return support.enrichirDocument(item, uuid);
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  stepExecution.getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getErrorMessageList().add(e.getMessage());
            LOGGER.warn("Une erreur est survenue à l'ajout de donnée au storageDocument avant archivage",
                  e);
            return item;
         } else {
            throw e;
         }
      }

   }

   protected boolean isModePartielBatch() {
      return batchMode != null
            && Constantes.BATCH_MODE.PARTIEL.getModeNomCourt()
                  .equals(batchMode);
   }

   /**
    * @return la liste des codes erreurs stockée dans le contexte d'execution du
    *         job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<String> getCodesErreurListe() {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.CODE_EXCEPTION);
   }
   
   /**
    * @return la liste des index des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Integer> getIndexErreurListe() {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_EXCEPTION);
   }
   
   /**
    * @return la liste des messages des exceptions stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<String> getErrorMessageList() {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.DOC_EXCEPTION);
   }
}
