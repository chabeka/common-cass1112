/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;
import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.listener.EcdeConnexionConfiguration;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.controles.SAEControleSupportService;

/**
 * Tasklet pour la vérification du fichier sommaire.xml
 * 
 */
@Component
public class CheckFileSommaireTasklet extends AbstractCaptureMasseTasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CheckFileSommaireTasklet.class);

   @Autowired
   private EcdeSommaireFileSupport fileSupport;

   @Autowired
   private SAEControleSupportService controleSupport;

   @Autowired
   private EcdeConnexionConfiguration configuration;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> parameters = chunkContext.getStepContext()
            .getJobParameters();

      final String urlEcde = (String) parameters.get(Constantes.SOMMAIRE);

      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      context.put(Constantes.SOMMAIRE, urlEcde);

      int index = 0;
      boolean checked = false;
      Exception storedException = null;
      while (index < configuration.getNbreEssaiMax() && !checked) {
         try {
            final URI uriEcde = new URI(urlEcde);

            final File sommaire = fileSupport.convertURLtoFile(uriEcde);

            controleSupport.checkEcdeWrite(sommaire);

            context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

            checked = true;

         } catch (CaptureMasseSommaireEcdeURLException exception) {
            storedException = exception;

         } catch (CaptureMasseSommaireFileNotFoundException exception) {
            storedException = exception;

         } catch (CaptureMasseEcdeWriteFileException exception) {
            storedException = exception;

         } catch (CaptureMasseRuntimeException exception) {
            storedException = exception;
         } finally {

            if (!checked) {
               pause(index);
            }

            index++;

         }
      }

      if (!checked) {
         addException(chunkContext, storedException);
      }

      return RepeatStatus.FINISHED;
   }

   private void addException(ChunkContext chunkContext, Exception paramException) {
      final Exception exception = new Exception(paramException.getMessage());

      getExceptionErreurListe(chunkContext).add(exception);

   }

   private void pause(int index) {

      String trcPrefix = "pause()";

      if (index < configuration.getNbreEssaiMax() - 1) {
         try {
            Thread.sleep(configuration.getDelaiAttenteMs());

         } catch (InterruptedException interruptedException) {
            LOGGER.info("impossible d'endormir le process");
         }

         LOGGER
               .warn(
                     "{} - La tentative {} de connexion à l'ECDE échouée. Nouvel essai dans {} ms.",
                     new Object[] { trcPrefix, index + 1,
                           configuration.getDelaiAttenteMs() });
      } else {
         LOGGER.warn("{} - La tentative {} de connexion à l'ECDE échouée.",
               new Object[] { trcPrefix, index + 1 });
      }
   }
}
