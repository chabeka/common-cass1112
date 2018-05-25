package fr.urssaf.image.sae.services.batch.transfert.support.sommaire.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.DocumentTypeMultiAction;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Processor permettant la conversion du sommaire en un Bean utilisable
 * 
 *
 */
@Component
public class ConvertSommaireDocumentTransfertProcessor implements
      ItemProcessor<JAXBElement<DocumentTypeMultiAction>, UntypedDocument> {

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertSommaireDocumentTransfertProcessor.class);

   private static final String PREFIXE_TRC = "convertSommaireDocumentTransfertProcessor.process()";

   private StepExecution stepExecution;
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
      this.batchMode = (String) stepExecution.getJobExecution()
            .getExecutionContext().get(Constantes.BATCH_MODE_NOM_REDIRECT);

   }

   @Override
   public UntypedDocument process(
         final JAXBElement<DocumentTypeMultiAction> itemTransfert)
         throws Exception {

      LOGGER.debug("{} - Début", PREFIXE_TRC);
      LOGGER.debug("{} - Début du mapping de l'objet Jaxb représentant "
            + "le document vers un objet métier UntypedDocument", PREFIXE_TRC);

      final UntypedDocument untypedDoc = new UntypedDocument();

      try {

         untypedDoc.setUuid(UUID.fromString(itemTransfert.getValue()
               .getObjetNumerique().getUUID()));

         if (itemTransfert.getValue().getMetadonnees() != null) {
            if (itemTransfert.getValue().getMetadonnees().getMetadonnee() != null
                  || !itemTransfert.getValue().getMetadonnees().getMetadonnee()
                        .isEmpty()) {
               final List<MetadonneeType> metaDataType = itemTransfert
                     .getValue().getMetadonnees().getMetadonnee();
               final List<UntypedMetadata> listUM = new ArrayList<UntypedMetadata>();

               UntypedMetadata untypedMetadata;
               for (MetadonneeType metadonneeType : metaDataType) {
                  untypedMetadata = new UntypedMetadata();
                  untypedMetadata.setLongCode(metadonneeType.getCode());
                  untypedMetadata.setValue(metadonneeType.getValeur());
                  listUM.add(untypedMetadata);
               }
               untypedDoc.setUMetadatas(listUM);
            }
         } else {
            untypedDoc.setUMetadatas(new ArrayList<UntypedMetadata>());
         }

         untypedDoc.setBatchActionType(itemTransfert.getValue().getTypeAction()
               .value());
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  stepExecution.getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getErrorMessageList().add(e.getMessage());
            LOGGER.warn("Erreur du mapping de l'objet Jaxb représentant "
                  + "le document vers un objet métier UntypedDocument", e);
         } else {
            throw e;
         }
      }
      
      LOGGER.debug(
            "{} - Fin du mapping", PREFIXE_TRC);
      return untypedDoc;
   }

   /**
    * Methode permettant de savoir si l'on est en mode partiel.
    * 
    * @return True si mode partiel, false sinon
    */
   protected boolean isModePartielBatch() {
      return batchMode != null
            && Constantes.BATCH_MODE.PARTIEL.getModeNomCourt()
                  .equals(batchMode);
   }

   /**
    * Getter code erreur liste
    * 
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
    * Getter code erreur liste
    * 
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
    * Getter code erreur liste
    * 
    * @return la liste des exceptions des erreurs stockée dans le contexte
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
