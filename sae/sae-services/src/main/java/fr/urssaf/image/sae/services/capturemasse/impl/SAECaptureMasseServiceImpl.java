/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

/**
 * Implémentation du service de capture de masse du SAE
 * 
 */
@Service
public class SAECaptureMasseServiceImpl implements SAECaptureMasseService {

   /**
    * Executable du traitement de capture de masse
    */
   @Autowired
   private JobLauncher jobLauncher;

   /**
    * Job de la capture de masse
    */
   @Autowired
   @Qualifier("traitement_masse")
   private Job job;

   /**
    * {@inheritDoc}
    */
   @Override
   public void captureMasse(final URI sommaireURL, final UUID idTraitement)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException,
         CaptureMasseEcdeWriteFileException {

      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      mapParam.put(Constantes.SOMMAIRE,
            new JobParameter(sommaireURL.toString()));
      mapParam.put(Constantes.ID_TRAITEMENT, new JobParameter(idTraitement
            .toString()));

      JobParameters parameters = new JobParameters(mapParam);

      try {
         jobLauncher.run(job, parameters);

      } catch (JobExecutionAlreadyRunningException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobRestartException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobInstanceAlreadyCompleteException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobParametersInvalidException e) {
         throw new CaptureMasseRuntimeException(e);
      }

   }

}
