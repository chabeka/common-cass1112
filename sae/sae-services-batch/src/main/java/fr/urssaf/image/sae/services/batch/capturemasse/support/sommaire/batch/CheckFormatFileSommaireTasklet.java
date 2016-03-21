/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import java.io.File;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet de vérification du format de fichier sommaire.xml
 * 
 */
@Component
public class CheckFormatFileSommaireTasklet extends AbstractCaptureMasseTasklet {

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

         LOGGER.debug("{} - Début de validation unicité IdGed des documents",
               TRC_EXEC);
         
         validationSupport.validerUniciteUuid(sommaireFile);
         
         LOGGER.debug("{} - Fin de validation unicité IdGed des documents",
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
         getExceptionErreurListe(chunkContext).add(exception);

      } catch (CaptureMasseRuntimeException e) {
         final Exception exception = new Exception(e.getMessage());
         getExceptionErreurListe(chunkContext).add(exception);
      
      } catch (CaptureMasseSommaireHashException e) {
         final Exception exception = new Exception(e.getMessage());
         getExceptionErreurListe(chunkContext).add(exception);
      
      } catch (CaptureMasseSommaireTypeHashException e) {
         final Exception exception = new Exception(e.getMessage());
         getExceptionErreurListe(chunkContext).add(exception);
      }

      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);

      return RepeatStatus.FINISHED;
   }
}
