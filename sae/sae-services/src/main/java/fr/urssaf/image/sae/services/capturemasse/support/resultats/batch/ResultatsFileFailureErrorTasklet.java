/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport;

/**
 * Tasklet décriture du fichier resultats.xml en cas d'erreur bloquante
 * 
 */
@Component
public class ResultatsFileFailureErrorTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileFailureErrorTasklet.class);

   @Autowired
   private ResultatsFileEchecBloquantSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final StepContext stepContext = chunkContext.getStepContext();
      final ExecutionContext context = stepContext.getStepExecution()
            .getJobExecution().getExecutionContext();

      final CaptureMasseSommaireDocumentException erreur = (CaptureMasseSommaireDocumentException) context
            .get(Constantes.DOC_EXCEPTION);

      LOGGER.error(erreur.getMessage(), erreur.getCause());

      final String sommairePath = context.getString(Constantes.SOMMAIRE_FILE);
      final File sommaireFile = new File(sommairePath);
      File ecdeDirectory = sommaireFile.getParentFile();
      CaptureMasseSommaireFormatValidationException erreurEx = (CaptureMasseSommaireFormatValidationException) erreur
            .getCause();
      support.writeResultatsFile(ecdeDirectory, erreurEx);

      return RepeatStatus.FINISHED;
   }
}
