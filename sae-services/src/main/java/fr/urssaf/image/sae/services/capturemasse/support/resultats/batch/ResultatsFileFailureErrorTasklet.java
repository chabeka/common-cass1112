/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport;
import fr.urssaf.image.sae.services.capturemasse.support.xsd.XsdValidationSupport;

/**
 * Tasklet décriture du fichier resultats.xml en cas d'erreur bloquante
 * 
 */
@Component
public class ResultatsFileFailureErrorTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileFailureErrorTasklet.class);

   @Autowired
   private ResultatsFileEchecBloquantSupport support;

   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final StepContext stepContext = chunkContext.getStepContext();
      final ExecutionContext context = stepContext.getStepExecution()
            .getJobExecution().getExecutionContext();

      @SuppressWarnings("unchecked")
      final ConcurrentLinkedQueue<Exception> erreurs = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Exception erreur = erreurs.peek();
      if (erreur != null) {
         LOGGER.error("erreur bloquante détectée", erreur);
         stepContext.getStepExecution().getJobExecution().addFailureException(
               erreur);
      }

      Object sommairePathObject = context.get(Constantes.SOMMAIRE_FILE);

      if (sommairePathObject instanceof String && erreur != null) {

         String sommairePath = (String) sommairePathObject;
         final File sommaireFile = new File(sommairePath);
         File ecdeDirectory = sommaireFile.getParentFile();
         support.writeResultatsFile(ecdeDirectory, erreur);

         File resultats = new File(ecdeDirectory, "resultats.xml");

         xsdValidationSupport.resultatsValidation(resultats);

      }

      return RepeatStatus.FINISHED;
   }
}
