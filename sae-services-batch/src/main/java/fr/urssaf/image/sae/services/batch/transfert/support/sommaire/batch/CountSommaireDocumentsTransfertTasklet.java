package fr.urssaf.image.sae.services.batch.transfert.support.sommaire.batch;

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

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet permettant de compter le nombre de document à traiter
 * 
 *
 */
@Component
public class CountSommaireDocumentsTransfertTasklet implements Tasklet {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(CountSommaireDocumentsTransfertTasklet.class);

   private static final String TRC_EXEC = "execute()";

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      LOGGER.debug("{} - Debut de méthode", TRC_EXEC);

      StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      String path = context.getString(Constantes.SOMMAIRE_FILE);

      File file = new File(path);

      LOGGER.debug("{} - Début du dénombrement des documents présents dans "
            + "le fichier sommaire.xml", TRC_EXEC);

      int nbreDocs = XmlReadUtils.compterElements(file, "documentMultiAction");

      int nbreVDocs = XmlReadUtils.compterElements(file, "composant");
      int nbre;
      String redirection;

      LOGGER.debug("{} - Fin du dénombrement", TRC_EXEC);

      if ((nbreDocs + nbreVDocs == 0) || (nbreDocs > 0 && nbreVDocs > 0)) {
         throw new CaptureMasseRuntimeException(
               "le fichier sommaire.xml est erroné");

      } else if (nbreDocs > 0) {
         nbre = nbreDocs;
         redirection = "DOCS";

      } else {
         nbre = nbreVDocs;
         redirection = "VDOCS";

      }

      LOGGER.debug("{} - {} documents présents dans le fichier sommaire.xml",
            TRC_EXEC, nbre);

      context.put(Constantes.DOC_COUNT, nbre);
      stepExecution.getExecutionContext().put(Constantes.COUNT_DIRECTION,
            redirection);

      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);

      return RepeatStatus.FINISHED;
   }
}
