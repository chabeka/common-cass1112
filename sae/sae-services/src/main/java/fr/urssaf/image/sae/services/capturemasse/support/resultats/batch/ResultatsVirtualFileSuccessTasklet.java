/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Tasklet pour 'écriture du fichier resultats.xml dans le cas d'un succès de
 * traitement pour les documents virtuels
 * 
 */
public class ResultatsVirtualFileSuccessTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsVirtualFileSuccessTasklet.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      String trcPrefix = "execute";
      LOGGER.debug("{} - début", trcPrefix);

      LOGGER.debug("{} - fin", trcPrefix);
      // TODO Auto-generated method stub
      return null;
   }

}
