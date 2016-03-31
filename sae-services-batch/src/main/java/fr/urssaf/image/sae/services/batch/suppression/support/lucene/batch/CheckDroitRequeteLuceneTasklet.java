package fr.urssaf.image.sae.services.batch.suppression.support.lucene.batch;

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
import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseRequeteValidationException;
import fr.urssaf.image.sae.services.batch.suppression.support.lucene.RequeteLuceneValidationSupport;
import fr.urssaf.image.sae.services.batch.suppression.tasklet.AbstractSuppressionMasseTasklet;

/**
 * Tasklet de vérification des droits de la requete lucene de suppression
 * 
 */
@Component
public class CheckDroitRequeteLuceneTasklet extends AbstractSuppressionMasseTasklet {
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(CheckDroitRequeteLuceneTasklet.class);
   
   private static final String TRC_EXEC = "execute()";
   
   @Autowired
   private RequeteLuceneValidationSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      
      LOGGER.debug("{} - Début de méthode", TRC_EXEC);
      
      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      
      // on recupere la requete du contexte d'execution du job 
      final String requeteLucene = (String) context.get(Constantes.REQ_LUCENE_SUPPRESSION);

      LOGGER.debug("{} - Début de vérification des droits de la requete lucene de suppression",
            TRC_EXEC);
      
      String requeteFinale = "";
      try {
         requeteFinale = support.verificationDroitRequeteLucene(requeteLucene);
         
         // quand la verification de droit reussie, on va stocker la requete finale
         // dans le contexte d'execution de l'execution du job
         context.put(Constantes.REQ_FINALE_TRT_MASSE, requeteFinale);
      } catch (SuppressionMasseRequeteValidationException e) {
         getExceptionErreurListe(chunkContext).add(e);
         
         // specifie le nombre de docs supprimés
         context.putInt(Constantes.NB_DOCS_SUPPRIMES, 0);
      }
      LOGGER.debug("{} - Fin de vérification des droits de la requete lucene de suppression",
            TRC_EXEC);
      
      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);
      
      return RepeatStatus.FINISHED;
   }

}
