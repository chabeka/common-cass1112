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
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
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
import fr.urssaf.image.sae.services.capturemasse.verification.VerificationSupport;

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

   @Autowired
   private VerificationSupport verifSupport;

   /**
    * Job de la capture de masse
    */
   @Autowired
   @Qualifier("capture_masse")
   private Job job;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAECaptureMasseServiceImpl.class);

   private static final String TRC_CAPTURE = "captureMasse()";

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
         boolean traitementOK = ExitStatus.COMPLETED.equals(jobExecution
               .getExitStatus());

         int index = 0;
         while (traitementOK && index < list.size()) {
            if ("finBloquant".equalsIgnoreCase(list.get(index).getStepName())
                  || "finErreur"
                        .equalsIgnoreCase(list.get(index).getStepName())) {
               traitementOK = false;
            }
            index++;
         }

         if (traitementOK) {
            exitTraitement.setExitMessage("Traitement réalisé avec succès");
            exitTraitement.setSucces(true);
         } else {

            checkFinal(jobExecution, sommaireURL, idTraitement, jobExecution
                  .getAllFailureExceptions());

            exitTraitement.setExitMessage("Traitement en erreur");
            exitTraitement.setSucces(false);
         }

      } catch (Throwable e) {

         LOGGER.warn(
               "{} - erreur lors de la capture de masse. Exception levée",
               TRC_CAPTURE, e);

         List<Throwable> listThrowables = new ArrayList<Throwable>();
         listThrowables.add(e);
         checkFinal(jobExecution, sommaireURL, idTraitement, listThrowables);

         exitTraitement.setExitMessage(e.getMessage());
         exitTraitement.setSucces(false);
      }

      return exitTraitement;

   }

   /**
    * @param jobExecution
    * @param idTraitement
    * @param sommaireURL
    */
   @SuppressWarnings("unchecked")
   private void checkFinal(JobExecution jobExecution, URI sommaireURL,
         UUID idTraitement, List<Throwable> listeExceptions) {

      Integer nbreDocs = null;
      ConcurrentLinkedQueue<UUID> listUuid = null;
      boolean logPresent = false;

      if (jobExecution != null) {
         nbreDocs = (Integer) jobExecution.getExecutionContext().get(
               Constantes.DOC_COUNT);

         listUuid = (ConcurrentLinkedQueue<UUID>) jobExecution
               .getExecutionContext().get(Constantes.INTEG_DOCS);
         logPresent = jobExecution.getExecutionContext().containsKey(
               Constantes.FLAG_BUL003);
      }
      if (listUuid == null) {
         listUuid = new ConcurrentLinkedQueue<UUID>();
      }

      verifSupport.checkFinTraitement(sommaireURL, nbreDocs, listUuid.size(),
            logPresent, listeExceptions, idTraitement);

   }
}
