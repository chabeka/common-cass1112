package fr.urssaf.image.sae.services.batch.common.support.resultat;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.transfert.support.resultats.ResultatFileSuccessTransfertSupport;

public abstract class AbstractResultatsFileSuccessTransfertTasklet {
   
   @SuppressWarnings("unchecked")
   public final RepeatStatus executeCommon(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();

      final String path = (String) map.get(Constantes.SOMMAIRE_FILE);
      final String modeBatch = (String) map.get(Constantes.BATCH_MODE_NOM);

      final File sommaireFile = new File(path);
      final File ecdeDirectory = sommaireFile.getParentFile();

      final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> listIntDocs = (ConcurrentLinkedQueue<TraitementMasseIntegratedDocument>) getExecutor()
            .getIntegratedDocuments();

      int initCount = (Integer) map.get(Constantes.DOC_COUNT);

      boolean restitutionUuid = false;
      if (chunkContext.getStepContext().getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.RESTITUTION_UUIDS) != null) {
         restitutionUuid = (Boolean) (chunkContext.getStepContext()
               .getStepExecution().getJobExecution().getExecutionContext()
               .get(Constantes.RESTITUTION_UUIDS));
      }
      getSuccessSupport().writeResultatsFile(ecdeDirectory, listIntDocs,
            initCount, restitutionUuid, sommaireFile, modeBatch);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      getXsdValidationSupport().resultatsValidation(resultats);

      return RepeatStatus.FINISHED;
   }

   /**
    * Getter
    * 
    * @return le support de validation de la XSD.
    */
   protected abstract XsdValidationSupport getXsdValidationSupport();

   /**
    * Getter
    * 
    * @return le support du process de succès.
    */
   protected abstract ResultatFileSuccessTransfertSupport getSuccessSupport();

   /**
    * Getter
    * 
    * @return le pool de thread.
    */
   protected abstract AbstractPoolThreadExecutor<?, ?> getExecutor();

}
