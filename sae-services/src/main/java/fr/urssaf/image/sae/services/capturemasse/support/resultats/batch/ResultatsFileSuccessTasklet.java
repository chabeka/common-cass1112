/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * Tasklet pour l'écriture du fichier resultats.xml quand le traitement est en
 * succès
 * 
 */
@Component
public class ResultatsFileSuccessTasklet implements Tasklet {

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      // FIXME FBON - Implémentation ResultatsFileSuccessTasklet
      return null;
   }

}
