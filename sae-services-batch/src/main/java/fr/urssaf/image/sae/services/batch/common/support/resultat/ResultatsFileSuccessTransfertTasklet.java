package fr.urssaf.image.sae.services.batch.common.support.resultat;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.AbstractPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.common.support.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.transfert.support.resultats.ResultatFileSuccessTransfertSupport;

@Component
public class ResultatsFileSuccessTransfertTasklet extends AbstractResultatsFileSuccessTransfertTasklet implements Tasklet{

   @Autowired
   private ResultatFileSuccessTransfertSupport successSupport;

   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private InsertionPoolThreadExecutor executor;

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
   protected ResultatFileSuccessTransfertSupport getSuccessSupport() {
      return successSupport;
   }

   @Override
   protected AbstractPoolThreadExecutor<?, ?> getExecutor() {
      return executor;
   }

}
