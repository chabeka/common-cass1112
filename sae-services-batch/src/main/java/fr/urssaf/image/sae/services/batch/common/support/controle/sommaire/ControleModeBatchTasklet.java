/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.support.controle.sommaire;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet permettant de contrôler le mode de traitement du batch.
 * 
 */
@Component
public class ControleModeBatchTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleModeBatchTasklet.class);

   private static final String TRC_EXEC = "execute()";

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {

      LOGGER.debug("{} - Début de méthode", TRC_EXEC);

      StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      String path = context.getString(Constantes.SOMMAIRE_FILE);

      File file = new File(path);

      LOGGER.debug(
            "{} - Début du controle du mode de traitement du batch présents dans le fichier sommaire.xml",
            TRC_EXEC);

      String batchMode = XmlReadUtils.getElementValue(file,
            Constantes.BATCH_MODE_ELEMENT_NAME);
      String batchModeActif = (String) context.get(Constantes.BATCH_MODE_NOM);

      if (batchModeActif == null || (batchModeActif!= null && !batchMode.equals(batchModeActif))) {
         ExecutionContext jobExecution = chunkContext.getStepContext()
               .getStepExecution().getJobExecution().getExecutionContext();
         final Exception exception = new Exception("Le mode de traitement du batch à changer. Traitement impossible.");

         ((ConcurrentLinkedQueue<Exception>) jobExecution.get(Constantes.DOC_EXCEPTION)).add(exception);
      }

      LOGGER.debug(
            "{} - Fin du controle du mode de traitement du batch présents dans le fichier sommaire.xml",
            TRC_EXEC);

      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);

      return RepeatStatus.FINISHED;
   }

}
