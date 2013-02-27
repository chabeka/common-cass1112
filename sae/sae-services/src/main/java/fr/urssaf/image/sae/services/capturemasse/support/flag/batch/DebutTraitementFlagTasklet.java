/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.batch;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport;
import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;
import fr.urssaf.image.sae.services.util.HostnameUtil;

/**
 * Tasklet d'écriture du fichier fin_traitement.flag
 * 
 */
@Component
public class DebutTraitementFlagTasklet implements Tasklet {

   @Autowired
   private DebutTraitementFlagSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();

      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      final String sommairePath = (String) context
            .get(Constantes.SOMMAIRE_FILE);

      final File sommaireFile = new File(sommairePath);
      final File ecdeDirectory = sommaireFile.getParentFile();

      final String ident = (String) stepExecution.getJobParameters().getString(
            Constantes.ID_TRAITEMENT);
      final UUID idTraitement = UUID.fromString(ident);

      final DebutTraitementFlag flag = new DebutTraitementFlag();
      flag.setHostInfo(HostnameUtil.getLocalHost());
      flag.setIdTraitement(idTraitement);
      flag.setStartDate(new Date());

      support.writeDebutTraitementFlag(flag, ecdeDirectory);

      return RepeatStatus.FINISHED;
   }

}
