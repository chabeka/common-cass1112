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

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Item Processor pour l'enrichissement des métadonnées
 * 
 */
@Component
public class EnrichissementMetadonneeProcessor implements
      ItemProcessor<UntypedDocument, SAEDocument> {

   @Autowired
   private EnrichissementMetadonneeSupport support;

   @Autowired
   private MappingDocumentService documentService;

   private StepExecution stepExecution;
   private String batchMode;
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(EnrichissementMetadonneeProcessor.class);

   /**
    * initialisation avant le début du Step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      this.batchMode = (String) stepExecution.getJobExecution()
            .getExecutionContext().get(Constantes.BATCH_MODE_NOM_REDIRECT);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEDocument process(final UntypedDocument item)
         throws Exception {

      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);
      
      try {
         // Si il y a déjà eu une erreur sur un document en mode partiel, on ne
         // cherche pas à continuer sur ce document
         if (!(isModePartielBatch() && getIndexErreurListe().contains(
               stepExecution.getExecutionContext().getInt(
                     Constantes.CTRL_INDEX)))) {
            final SAEDocument saeDocument = documentService
                  .untypedDocumentToSaeDocument(item);
            support.enrichirMetadonnee(saeDocument);
            return saeDocument;
         } else {
            SAEDocument saeDocument = new SAEDocument();
            saeDocument.setUuid(item.getUuid());
            saeDocument.setFilePath(item.getFilePath());
            return saeDocument;
         }
      } catch (Exception e) {
         // Si il y a une exception sur le traitement du document en mode partiel, on ne stoppe pas la suite
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  stepExecution.getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getErrorMessageList().add(e.toString());
            LOGGER.warn("erreur lors de l'enrichissement de metadonnée", e);
            SAEDocument saeDocument = new SAEDocument();
            saeDocument.setUuid(item.getUuid());
            return saeDocument;
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
