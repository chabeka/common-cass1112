/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.support.resultat;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.modification.support.stockage.multithreading.ModificationPoolThreadExecutor;

/**
 * Tasklet pour l'écriture du fichier resultats.xml quand le traitement est en
 * succès
 * 
 */
@Component
public class ResultatsFileSuccessTasklet extends
      AbstractResultatsFileSuccessTasklet implements Tasklet {

   @Autowired
   private ResultatFileSuccessSupport successSupport;

   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private ModificationPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {
      return executeCommon(contribution, chunkContext);
   }

   @Override
   protected XsdValidationSupport getXsdValidationSupport() {
      return xsdValidationSupport;
   }

   @Override
   protected ResultatFileSuccessSupport getSuccessSupport() {
      return successSupport;
   }

   @Override
   protected AbstractPoolThreadExecutor<?, ?> getExecutor() {
      return executor;
   }
}