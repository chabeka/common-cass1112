/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.flag.batch;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.flag.FinTraitementFlagSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet d'écriture du fichier fin_traitement.flag
 * 
 */
@Component
public class FinTraitementFlagTasklet implements Tasklet {

   @Autowired
   private FinTraitementFlagSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {
      
      ExecutionContext context = chunkContext.getStepContext()
            .getStepExecution().getJobExecution().getExecutionContext();

      Object sommairePathObject = context.get(Constantes.SOMMAIRE_FILE);

      if (sommairePathObject instanceof String) {
         String sommairePath = (String) sommairePathObject;

         final File sommaireFile = new File(sommairePath);
         final File ecdeDirectory = sommaireFile.getParentFile();

         support.writeFinTraitementFlag(ecdeDirectory);
      }

      return RepeatStatus.FINISHED;
   }

}
