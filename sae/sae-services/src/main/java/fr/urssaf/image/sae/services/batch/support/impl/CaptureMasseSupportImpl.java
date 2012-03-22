package fr.urssaf.image.sae.services.batch.support.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;

/**
 * Implémentation du service {@link TraitementExecutionSupport} pour la capture
 * en masse
 * 
 * 
 */
@Component
@Qualifier("captureMasseTraitement")
public class CaptureMasseSupportImpl implements TraitementExecutionSupport {

   private final JobLauncher jobLauncher;

   private final Job captureMasse;

   /**
    * 
    * @param jobLauncher
    *           exécuteur de traitement de masse
    * @param captureMasse
    *           job du traitement de capture en masse
    */
   @Autowired
   public CaptureMasseSupportImpl(JobLauncher jobLauncher,
         @Qualifier("capture_masse") Job captureMasse) {

      this.jobLauncher = jobLauncher;
      this.captureMasse = captureMasse;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement execute(JobRequest job) {

      Assert.notNull(job, "'job' is required");

      UUID idTraitement = job.getIdJob();

      // le paramètre stocké dans la pile des travaux correspond pour les
      // traitements de capture en masse à l'URL ECDE
      String urlECDE = job.getParameters();

      URI sommaire;

      try {
         sommaire = URI.create(urlECDE);

      } catch (IllegalArgumentException e) {

         // cas où ecdeParameter ne respecte pas RFC 2396
         // (Cf. http://www.ietf.org/rfc/rfc2396.txt)

         throw new JobParameterTypeException(job, e);

      }

      // exécution du job avec spring batch

      JobExecution jobExecution = run(idTraitement, sommaire);

      boolean succes = ExitStatus.COMPLETED
            .equals(jobExecution.getExitStatus()) ? true : false;
      String exitMessage = jobExecution.getExitStatus().getExitDescription();

      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setSucces(succes);
      exitTraitement.setExitMessage(exitMessage);

      return exitTraitement;

   }

   // il s'agit d'un code provisoire
   // l'implémentation du lancement du traitement de capture en masse via spring
   // batch se fera dans le composant services.capturemasse
   protected final JobExecution run(UUID idTraitement, URI sommaire) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("capture.masse.idtraitement", new JobParameter(
            idTraitement.toString()));
      parameters.put("capture.masse.sommaire", new JobParameter(sommaire
            .toASCIIString()));
      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.run(captureMasse, jobParameters);
      } catch (JobExecutionAlreadyRunningException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobRestartException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobInstanceAlreadyCompleteException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobParametersInvalidException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      return jobExecution;
   }
}
