/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.util.XmlReadUtils;

/**
 * Tasklet de vérification du format de fichier sommaire.xml
 * 
 */
@Component
public class CheckFormatFileSommaireTasklet implements Tasklet {

   @Autowired
   private SommaireFormatValidationSupport validationSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CheckFormatFileSommaireTasklet.class);

   private static final String TRC_EXEC = "execute()";

   @Autowired
   private SAEControleSupportService controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) {

      LOGGER.debug("{} - Début de méthode", TRC_EXEC);

      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      final String sommairePath = context.getString(Constantes.SOMMAIRE_FILE);

      final File sommaireFile = new File(sommairePath);

      final String hash = (String) chunkContext.getStepContext()
            .getJobParameters().get(Constantes.HASH);

      final String typeHash = (String) chunkContext.getStepContext()
            .getJobParameters().get(Constantes.TYPE_HASH);

      try {

         LOGGER.debug("{} - Début de validation du fichier sommaire.xml",
               TRC_EXEC);

         if (hash != null) {
            controleSupport.checkHash(sommaireFile, hash, typeHash);
         }

         validationSupport.validationSommaire(sommaireFile);

         LOGGER.debug("{} - Fin de validation du fichier sommaire.xml",
               TRC_EXEC);
         LOGGER.debug("{} - Début de validation du BATCH_MODE du sommaire.xml",
               TRC_EXEC);

         validationSupport.validerModeBatch(sommaireFile, "TOUT_OU_RIEN");

         LOGGER.debug("{} - Fin de validation du BATCH_MODE du sommaire.xml",
               TRC_EXEC);

         boolean restitutionUuids = false;
         String valeur = XmlReadUtils.getElementValue(sommaireFile,
               "restitutionUuids");
         if (StringUtils.isNotBlank(valeur)) {
            restitutionUuids = BooleanUtils.toBoolean(valeur);
            context.put(Constantes.RESTITUTION_UUIDS, restitutionUuids);
         }

      } catch (CaptureMasseSommaireFormatValidationException e) {
         final Exception exception = new Exception(e.getMessage());
         @SuppressWarnings("unchecked")
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);

      } catch (CaptureMasseRuntimeException e) {
         final Exception exception = new Exception(e.getMessage());
         @SuppressWarnings("unchecked")
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);
      } catch (CaptureMasseSommaireHashException e) {
         final Exception exception = new Exception(e.getMessage());
         @SuppressWarnings("unchecked")
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);
      } catch (CaptureMasseSommaireTypeHashException e) {
         final Exception exception = new Exception(e.getMessage());
         @SuppressWarnings("unchecked")
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);
      }

      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);

      return RepeatStatus.FINISHED;
   }
}
