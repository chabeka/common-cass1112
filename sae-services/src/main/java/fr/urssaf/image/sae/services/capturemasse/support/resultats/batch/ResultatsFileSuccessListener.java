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
 * Ecouteur pour l'écriture du fichier resultats.xml quand le traitement est en
 * succes
 * 
 */
@Component
public class ResultatsFileSuccessListener implements Tasklet {

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      // TODO Auto-generated method stub
      return null;
   }

}
