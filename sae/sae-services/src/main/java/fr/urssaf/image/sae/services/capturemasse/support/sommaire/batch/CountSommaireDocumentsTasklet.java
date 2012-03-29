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

   /**
    * 
    */
   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {

      StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      String path = context.getString(Constantes.SOMMAIRE_FILE);

      File file = new File(path);

      int nbreElements = XmlReadUtils.compterElements(file, "document");

      context.put(Constantes.DOC_COUNT, nbreElements);

      return RepeatStatus.FINISHED;
   }

}
