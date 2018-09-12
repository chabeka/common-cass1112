package fr.urssaf.image.sae.services.batch.restore.support.lucene.batch;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.restore.exception.RestoreMasseParamValidationException;
import fr.urssaf.image.sae.services.batch.restore.support.lucene.RestoreParamValidationSupport;
import fr.urssaf.image.sae.services.batch.restore.tasklet.AbstractRestoreMasseTasklet;

/**
 * Tasklet de vérification des droits de restore
 * 
 */
@Component
public class CheckDroitRestoreTasklet extends AbstractRestoreMasseTasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CheckDroitRestoreTasklet.class);
   
   private static final String TRC_EXEC = "execute()";
   
   @Autowired
   private RestoreParamValidationSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      
      LOGGER.debug("{} - Début de méthode", TRC_EXEC);
      
      final Map<String, Object> parameters = chunkContext.getStepContext()
            .getJobParameters();

      final String idTraitementARestorer = (String) parameters.get(Constantes.ID_TRAITEMENT_A_RESTORER);
      
      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      
      LOGGER.debug("{} - Début de vérification des droits de la requete lucene de restore",
            TRC_EXEC);
      
      String requeteFinale = "";
      try {
         requeteFinale = support.verificationDroitRestore(UUID.fromString(idTraitementARestorer));
         
         // quand la verification de droit reussie, on va stocker la requete finale
         // dans le contexte d'execution de l'execution du job
         context.put(Constantes.REQ_FINALE_TRT_MASSE, requeteFinale);
      } catch (RestoreMasseParamValidationException e) {
         getExceptionErreurListe(chunkContext).add(e);
         
         // specifie le nombre de docs restorés
         context.putInt(Constantes.NB_DOCS_RESTORES, 0);
      }
      LOGGER.debug("{} - Fin de vérification des droits de la requete lucene de restore",
            TRC_EXEC);
      
      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);
      
      return RepeatStatus.FINISHED;
   }
}
