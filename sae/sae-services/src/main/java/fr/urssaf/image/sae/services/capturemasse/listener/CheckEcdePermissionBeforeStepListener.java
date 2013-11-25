/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.listener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.exception.EcdePermissionException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.controles.SAEControleSupportService;

/**
 * Listener permettant de vérifier que les droits sur l'ECDE sont présents
 * 
 */
@Component
public class CheckEcdePermissionBeforeStepListener {

   @Autowired
   private SAEControleSupportService controleSupport;

   @Autowired
   private EcdeSommaireFileSupport fileSupport;

   @Autowired
   private EcdeConnexionConfiguration config;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CheckEcdePermissionBeforeStepListener.class);

   /**
    * vérification des droits d'accès
    * 
    * @param stepExecution
    *           le stepExecution
    * @throws Exception
    *            l'exception
    */
   @BeforeStep
   public final void beforeStep(StepExecution stepExecution) throws Exception {
      String urlEcde = stepExecution.getJobExecution().getExecutionContext()
            .getString(Constantes.SOMMAIRE);

      boolean finished = false;
      int index = 0;
      Exception exception = null;

      while (index < config.getNbreEssaiMax() && !finished) {
         exception = checkWrite(urlEcde);

         if (exception == null) {
            finished = true;
         } else {

            pause(index);
         }

         index++;
      }

      if (exception != null) {
         throw exception;
      }

   }

   private Exception checkWrite(String urlEcde) throws URISyntaxException {

      Exception exc = null;

      try {
         final URI uriEcde = new URI(urlEcde);

         final File sommaire = fileSupport.convertURLtoFile(uriEcde);

         controleSupport.checkEcdeWrite(sommaire);

      } catch (CaptureMasseSommaireEcdeURLException exception) {
         exc = new EcdePermissionException(exception);

      } catch (CaptureMasseSommaireFileNotFoundException exception) {
         exc = new EcdePermissionException(exception);

      } catch (CaptureMasseEcdeWriteFileException exception) {
         exc = new EcdePermissionException(exception);

      } catch (CaptureMasseRuntimeException exception) {
         exc = new EcdePermissionException(exception);
      }

      return exc;
   }

   private void pause(int index) {

      String trcPrefix = "pause()";

      if (index < config.getNbreEssaiMax() - 1) {
         try {
            Thread.sleep(config.getDelaiAttenteMs());

         } catch (InterruptedException interruptedException) {
            LOGGER.info("impossible d'endormir le process");
         }

         LOGGER
               .info(
                     "{} - La tentative {} de connexion à l'ECDE échouée. Nouvel essai dans {} ms.",
                     new Object[] { trcPrefix, index + 1,
                           config.getDelaiAttenteMs() });
      } else {
         LOGGER.info("{} - La tentative {} de connexion à l'ECDE échouée.",
               new Object[] { trcPrefix, index + 1 });
      }
   }
}
