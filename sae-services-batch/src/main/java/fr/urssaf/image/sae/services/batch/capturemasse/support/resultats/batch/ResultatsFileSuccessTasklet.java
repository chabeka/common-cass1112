/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet pour l'écriture du fichier resultats.xml quand le traitement est en
 * succès
 * 
 */
@Component
public class ResultatsFileSuccessTasklet implements Tasklet {

   @Autowired
   private ResultatFileSuccessSupport successSupport;

   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private InsertionPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();

      final String path = (String) map.get(Constantes.SOMMAIRE_FILE);

      final File sommaireFile = new File(path);
      final File ecdeDirectory = sommaireFile.getParentFile();

      final ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> listIntDocs = executor
            .getIntegratedDocuments();

      int initCount = (Integer) map.get(Constantes.DOC_COUNT);

      boolean restitutionUuid = false;
      if (chunkContext.getStepContext().getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.RESTITUTION_UUIDS) != null) {
         restitutionUuid = (Boolean) (chunkContext.getStepContext()
               .getStepExecution().getJobExecution().getExecutionContext()
               .get(Constantes.RESTITUTION_UUIDS));
      }
      successSupport.writeResultatsFile(ecdeDirectory, listIntDocs, initCount,
            restitutionUuid, sommaireFile);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      xsdValidationSupport.resultatsValidation(resultats);

      return RepeatStatus.FINISHED;
   }
}
