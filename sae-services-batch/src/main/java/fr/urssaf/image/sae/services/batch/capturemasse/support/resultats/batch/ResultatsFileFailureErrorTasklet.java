/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ErreurTraitement;

/**
 * Tasklet d'écriture du fichier resultats.xml en cas d'erreur bloquante
 * 
 */
@Component
public class ResultatsFileFailureErrorTasklet extends AbstractCaptureMasseTasklet {

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

      String erreur = getErrorMessageList(chunkContext).peek();
      if (erreur != null) {
         LOGGER.error("erreur bloquante détectée", erreur);
         stepContext.getStepExecution().getJobExecution().addFailureException(
              new Exception(erreur));
      }

      Object sommairePathObject = context.get(Constantes.SOMMAIRE_FILE);

      if (sommairePathObject instanceof String && erreur != null) {

         String sommairePath = (String) sommairePathObject;
         final File sommaireFile = new File(sommairePath);
         File ecdeDirectory = sommaireFile.getParentFile();
         ErreurTraitement erreurTraitement = new ErreurTraitement();
         erreurTraitement.setCodeErreur("SAE-EC-SOM001");
         erreurTraitement
               .setMessageErreur("Le fichier sommaire n'est pas valide. Détails : ");
         erreurTraitement.setException(new Exception(erreur));
         support.writeResultatsFile(ecdeDirectory, erreurTraitement);

         File resultats = new File(ecdeDirectory, "resultats.xml");

         xsdValidationSupport.resultatsValidation(resultats);

      }

      return RepeatStatus.FINISHED;
   }
}
