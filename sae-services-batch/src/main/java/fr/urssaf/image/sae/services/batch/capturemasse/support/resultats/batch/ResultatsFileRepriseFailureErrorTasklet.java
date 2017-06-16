/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch;

import java.io.File;

import org.apache.commons.lang.StringUtils;
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
 * Tasklet décriture du fichier resultats.xml en cas d'erreur bloquante
 * 
 */
@Component
public class ResultatsFileRepriseFailureErrorTasklet extends AbstractCaptureMasseTasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileRepriseFailureErrorTasklet.class);

   /**
    * Support fichier resultat bloquant
    */
   @Autowired
   private ResultatsFileEchecBloquantSupport support;

   /**
    * Bean validation XSD
    */
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

      Exception erreur = getExceptionErreurListe(chunkContext).peek();
      if (erreur != null) {
         LOGGER.error("erreur bloquante détectée", erreur);
         stepContext.getStepExecution().getJobExecution().addFailureException(
               erreur);
      }

      Object sommairePathObject = context.get(Constantes.SOMMAIRE_FILE);
      final String ident = (String) stepContext.getStepExecution()
            .getJobParameters().getString(Constantes.ID_TRAITEMENT_A_REPRENDRE);
      if (sommairePathObject instanceof String && erreur != null) {

         String sommairePath = (String) sommairePathObject;
         final File sommaireFile = new File(sommairePath);
         File ecdeDirectory = sommaireFile.getParentFile();
         ErreurTraitement erreurTraitement = new ErreurTraitement();
         erreurTraitement.setCodeErreur(Constantes.ERR_RE_BUL001);
         erreurTraitement.setMessageErreur(StringUtils.EMPTY);
         erreurTraitement.setException(erreur);
         support.writeResultatsFile(ecdeDirectory, erreurTraitement);

         File resultats = new File(ecdeDirectory, "resultats.xml");

         xsdValidationSupport.resultatsValidation(resultats);

      }

      return RepeatStatus.FINISHED;
   }
}
