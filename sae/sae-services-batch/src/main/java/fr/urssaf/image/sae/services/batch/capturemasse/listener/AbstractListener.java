/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Méthodes de base pour les listener
 * 
 */
public abstract class AbstractListener {

   private StepExecution stepExecution;
   private String batchMode;
   private boolean repriseActif;

   private static final ExitStatus FAILED_FIN_BLOQUANT = new ExitStatus(
         "FAILED_FIN_BLOQUANT");

   /**
    * initialisation de variables
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void beforeStep(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      batchMode = (String) getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.BATCH_MODE_NOM_REDIRECT);
      repriseActif = getStepExecution().getJobParameters().getString(
            Constantes.TRAITEMENT_REPRISE) != null
            && Boolean.valueOf((String) getStepExecution()
                  .getJobParameters().getString(Constantes.TRAITEMENT_REPRISE));
      specificInitOperations();
   }

   /**
    * Méthode déclenchée à la fin du step
    * 
    * @param stepExecution
    *           le stepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {
      this.stepExecution = stepExecution;

      ExitStatus exitStatus = stepExecution.getExitStatus();
      if (!FAILED_FIN_BLOQUANT.equals(exitStatus)) {
         exitStatus = specificAfterStepOperations();
      }

      return exitStatus;
   }

   /**
    * @return la liste des codes erreurs stockée dans le contexte d'execution du
    *         job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<String> getCodesErreurListe() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
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
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_EXCEPTION);
   }

   /**
    * @return la liste des exceptions des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<String> getErrorMessageList() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.DOC_EXCEPTION);
   }
   
   /**
    * @return la liste des index des références des erreurs stockée dans le
    *         contexte d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Integer> getIndexReferenceErreurListe() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_REF_EXCEPTION);
   }

   /**
    * Getter
    * 
    * @return la liste des index des documents déjà traité par le traitement de
    *         masse initial dans le contexte d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Integer> getIndexRepriseDoneListe() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_DOCUMENT_DONE);
   }

   /**
    * Opérations supplémentaires à réaliser lors de l'initialisation du
    * listener. Initialisation du stepExecution déjà réalisée
    */
   protected abstract void specificInitOperations();

   /**
    * Opérations supplémentaires à réaliser lors de la fin du step.
    * 
    * @return le code status de retour
    */
   protected abstract ExitStatus specificAfterStepOperations();

   /**
    * @return le StepExecution
    */
   protected final StepExecution getStepExecution() {
      return stepExecution;
   }

   /**
    * 
    * Methode permettant de savoir si la reprise est actif.
    * 
    * @return True si reprise est actif, false sinon.
    */
   protected boolean isRepriseActifBatch() {
      return repriseActif;
   }

   /**
    * 
    * Methode permettant de savoir si le mode de traitement du batch est
    * PARTIEL.
    * 
    * @return True si mode PARTIEL, false sinon.
    */
   protected boolean isModePartielBatch() {
      return batchMode != null
            && Constantes.BATCH_MODE.PARTIEL.getModeNomCourt()
                  .equals(batchMode);
   }

   /**
    * Getter pour batchMode
    * 
    * @return the batchMode
    */
   public String getBatchMode() {
      return batchMode;
   }

   /**
    * Contrôle si la liste de métadonnées passée en paramètre contient la
    * métadonnée gel à true (Document gelé).
    * 
    * @param listeStorageMeta
    *           liste de métadonnées
    * @return true si le document est gelé
    * @throws RetrievalServiceEx
    * @{@link RetrievalServiceEx}
    * @throws SearchingServiceEx
    * @{@link SearchingServiceEx}
    */
   public boolean isFrozenDocument(List<StorageMetadata> listeStorageMeta)
         throws RetrievalServiceEx {
      if (listeStorageMeta != null && !listeStorageMeta.isEmpty()) {
         for (StorageMetadata meta : listeStorageMeta) {
            if (meta.getShortCode().equals(
                  StorageTechnicalMetadatas.GEL.getShortCode())) {
               if (meta.getValue() == Boolean.TRUE) {
                  return true;
               }
            }
         }
      }
      return false;
   }
   
}
