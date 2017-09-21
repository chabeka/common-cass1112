/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.sommaire.batch;

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
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Item processor pour convertir un document sommaire d'un modèle objet XML vers
 * un modèle objet métier pour le service de modification en masse
 * 
 */
@Component
public class ConvertMetaDocumentModificationProcessor implements
      ItemProcessor<JAXBElement<DocumentType>, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertMetaDocumentModificationProcessor.class);

   private static final String PREFIXE_TRC = "ConvertMetaDocumentModificationProcessor.process()";

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

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument process(final JAXBElement<DocumentType> item)
         throws Exception {

      LOGGER.debug("{} - Début", PREFIXE_TRC);
      LOGGER.debug("{} - Début du mapping de l'objet Jaxb représentant "
            + "le document vers un objet métier UntypedDocument", PREFIXE_TRC);

      final UntypedDocument untypedDoc = new UntypedDocument();
      
      try {

         final List<MetadonneeType> metaDataType = item.getValue()
               .getMetadonnees().getMetadonnee();
         final List<UntypedMetadata> listUM = new ArrayList<UntypedMetadata>();

         UntypedMetadata untypedMetadata;
         for (MetadonneeType metadonneeType : metaDataType) {
            untypedMetadata = new UntypedMetadata();
            untypedMetadata.setLongCode(metadonneeType.getCode());
            untypedMetadata.setValue(metadonneeType.getValeur());
            listUM.add(untypedMetadata);
         }
         untypedDoc.setUMetadatas(listUM);

         untypedDoc.setUuid(UUID.fromString(item.getValue().getObjetNumerique()
               .getUUID()));

      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  stepExecution.getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getExceptionErreurListe().add(new Exception(e.getMessage()));
         } else {
            throw e;
         }

      }

      LOGGER
            .debug(
                  "{} - Fin du mapping de l'objet Jaxb représentant le sommaire.xml vers un objet métier Sommaire",
                  PREFIXE_TRC);
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
   protected final ConcurrentLinkedQueue<Exception> getExceptionErreurListe() {
      ExecutionContext jobExecution = stepExecution.getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.DOC_EXCEPTION);
   }
}
