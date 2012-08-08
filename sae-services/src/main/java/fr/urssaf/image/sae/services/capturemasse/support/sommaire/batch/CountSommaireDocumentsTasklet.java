/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.util.XmlReadUtils;

/**
 * Tasklet permettant de récupérer le nombre de documents présents dans le
 * sommaire et renseigne le context
 * 
 */
@Component
public class CountSommaireDocumentsTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CountSommaireDocumentsTasklet.class);

   private static final String TRC_EXEC = "execute()";

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {

      LOGGER.debug("{} - Debut de méthode", TRC_EXEC);

      StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      String path = context.getString(Constantes.SOMMAIRE_FILE);

      File file = new File(path);

      LOGGER.debug("{} - Début du dénombrement des documents présents dans "
            + "le fichier sommaire.xml", TRC_EXEC);

      int nbreElements = XmlReadUtils.compterElements(file, "document");
      
      LOGGER.debug("{} - Fin du dénombrement", TRC_EXEC);
      
      LOGGER.debug("{} - {} documents présents dans le fichier sommaire.xml", TRC_EXEC, nbreElements);

      context.put(Constantes.DOC_COUNT, nbreElements);

      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);

      return RepeatStatus.FINISHED;
   }

}
