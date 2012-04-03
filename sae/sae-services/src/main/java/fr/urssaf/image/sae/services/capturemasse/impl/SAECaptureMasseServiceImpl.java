/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

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
   @Qualifier("capture_masse")
   private Job job;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ExitTraitement captureMasse(final URI sommaireURL,
         final UUID idTraitement) {

      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      mapParam.put(Constantes.SOMMAIRE,
            new JobParameter(sommaireURL.toString()));
      mapParam.put(Constantes.ID_TRAITEMENT, new JobParameter(idTraitement
            .toString()));

      JobParameters parameters = new JobParameters(mapParam);
      ExitTraitement exitTraitement = new ExitTraitement();
      JobExecution jobExecution = null;

      try {
         jobExecution = jobLauncher.run(job, parameters);

         List<StepExecution> list = new ArrayList<StepExecution>(jobExecution
               .getStepExecutions());
         boolean traitementOK = true;
         int index = 0;
         while (traitementOK && index < list.size()) {
            if ("finBloquant".equalsIgnoreCase(list.get(index).getStepName())
                  || "finErreur".equalsIgnoreCase(list.get(index).getStepName())) {
               traitementOK = false;
            }
            index++;
         }

         if (traitementOK) {
            exitTraitement.setExitMessage("Traitement réalisé avec succès");
            exitTraitement.setSucces(true);
         } else {
            exitTraitement.setExitMessage("Traitement en erreur");
            exitTraitement.setSucces(false);
         }

      } catch (Exception e) {
         exitTraitement.setExitMessage(e.getMessage());
         exitTraitement.setSucces(false);
      }

      return exitTraitement;

   }
}
