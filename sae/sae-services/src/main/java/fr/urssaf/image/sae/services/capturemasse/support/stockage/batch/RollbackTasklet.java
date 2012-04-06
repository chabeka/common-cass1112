/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;

/**
 * Tasklet pour le rollback
 * 
 */
@Component
public class RollbackTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackTasklet.class);

   private static final String TRC_ROLLBACK = "rollbacktasklet()";

   @Autowired
   private RollbackSupport support;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings( { "unchecked", "PMD.AvoidThrowingRawExceptionTypes" })
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) {

      Map<String, Object> mapJob = chunkContext.getStepContext()
            .getJobExecutionContext();

      /*
       * on va incrémenter le nombre d'enregistrements lus et écrits. ces
       * nombres sont décorrélés du nombre lus et écrits dans le step précédent.
       */

      int countRead = chunkContext.getStepContext().getStepExecution()
            .getReadCount();

      int countWrite = chunkContext.getStepContext().getStepExecution()
            .getWriteCount();

      List<UUID> listIntegDocs = (List<UUID>) mapJob.get(Constantes.INTEG_DOCS);

      RepeatStatus status;

      try {

         if (CollectionUtils.isNotEmpty(listIntegDocs)) {

            /*
             * Afin de savoir si il y a eu un problème de rollback dans l'étape
             * de fin, nous supprimons les enregistrements de la liste au fur et
             * à mesure. Il faut donc toujours récupérer le 1er élément de la
             * liste.
             */

            UUID strDocumentID = listIntegDocs.get(0);

            support.rollback(strDocumentID);

            chunkContext.getStepContext().getStepExecution().setReadCount(
                  ++countRead);

            LOGGER.debug("{} - Rollback du document #{}/{} ({})",
                  new Object[] { TRC_ROLLBACK, countRead, listIntegDocs.size(),
                        strDocumentID });

            chunkContext.getStepContext().getStepExecution().setWriteCount(
                  ++countWrite);

            if (countRead == listIntegDocs.size()) {
               status = RepeatStatus.FINISHED;
            } else {
               status = RepeatStatus.CONTINUABLE;
            }

         } else {

            LOGGER.debug("{} - Aucun document à supprimer",
                  new Object[] { TRC_ROLLBACK });

            status = RepeatStatus.FINISHED;
         }

      } catch (Exception e) {

         String idTraitement = (String) chunkContext.getStepContext()
               .getJobParameters().get(Constantes.ID_TRAITEMENT);

         String errorMessage = MessageFormat.format(
               "{0} - Une exception a été levée lors du rollback : {1}",
               TRC_ROLLBACK, idTraitement);

         LOGGER.error(errorMessage, e);

         LOGGER
               .error(

                     "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                     idTraitement);

         status = RepeatStatus.FINISHED;
      }

      return status;
   }

}
