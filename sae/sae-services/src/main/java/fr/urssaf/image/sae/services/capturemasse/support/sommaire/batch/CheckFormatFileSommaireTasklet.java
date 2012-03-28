/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;

/**
 * Tasklet de vérification du format de fichier sommaire.xml
 * 
 */
@Component
public class CheckFormatFileSommaireTasklet implements Tasklet {

   @Autowired
   private SommaireFormatValidationSupport validationSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) {

      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      final String sommairePath = context.getString(Constantes.SOMMAIRE_FILE);
      final File sommaireFile = new File(sommairePath);

      try {
         validationSupport.validationSommaire(sommaireFile);
      } catch (CaptureMasseSommaireFormatValidationException e) {
         CaptureMasseSommaireDocumentException exception = new CaptureMasseSommaireDocumentException(
               0, e);
         context.put(Constantes.DOC_EXCEPTION, exception);
      }

      try {
         validationSupport.validerModeBatch(sommaireFile, "TOUT_OU_RIEN");

      } catch (CaptureMasseSommaireFormatValidationException e) {
         final CaptureMasseSommaireDocumentException exception = new CaptureMasseSommaireDocumentException(
               0, e);
         context.put(Constantes.DOC_EXCEPTION, exception);

      }

      return RepeatStatus.FINISHED;
   }
}
