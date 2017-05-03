/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionCapturePoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.StatutCaptureUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.verification.VerificationSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;

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
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private InsertionCapturePoolThreadExecutor executor;

   private static final String CATCH = "AvoidCatchingThrowable";

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

      ExitTraitement exitTraitement = captureMasse(sommaireURL, idTraitement, null, null);

      return exitTraitement;

   }

   /**
    * @param jobExecution
    * @param idTraitement
    * @param sommaireURL
    */
   private void checkFinal(JobExecution jobExecution, URI sommaireURL,
         UUID idTraitement, List<Throwable> listeExceptions) {

      Integer nbreDocs = null;
      Integer nbDocsIntegres = null;
      String batchModeTraitement = null;
      boolean logPresent = false;

      if (jobExecution != null) {
         nbreDocs = (Integer) jobExecution.getExecutionContext().get(
               Constantes.DOC_COUNT);

         nbDocsIntegres = (Integer) jobExecution.getExecutionContext().get(
               Constantes.NB_INTEG_DOCS);

         batchModeTraitement = (String) jobExecution.getExecutionContext().get(
               Constantes.BATCH_MODE_NOM);

         logPresent = jobExecution.getExecutionContext().containsKey(
               Constantes.FLAG_BUL003);
      }
      
      verifSupport.checkFinTraitement(sommaireURL, nbreDocs, nbDocsIntegres, batchModeTraitement,
            logPresent, listeExceptions, idTraitement, executor.getIntegratedDocuments());

   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings(CATCH)
   @Override
   public final ExitTraitement captureMasse(URI sommaireURI, UUID idTraitement,
         String hash, String typeHash) {
      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      mapParam.put(Constantes.SOMMAIRE,
            new JobParameter(sommaireURI.toString()));
      mapParam.put(Constantes.ID_TRAITEMENT, new JobParameter(idTraitement
            .toString()));
      mapParam.put(Constantes.HASH, new JobParameter(hash));
      mapParam.put(Constantes.TYPE_HASH, new JobParameter(typeHash));

      JobParameters parameters = new JobParameters(mapParam);
      ExitTraitement exitTraitement = new ExitTraitement();
      JobExecution jobExecution = null;

      try {
         jobExecution = jobLauncher.run(job, parameters);

         boolean traitementOK = StatutCaptureUtils.isCaptureOk(jobExecution);

         if (traitementOK) {
            exitTraitement.setExitMessage("Traitement réalisé avec succès");
            exitTraitement.setSucces(true);
         } else {

            checkFinal(jobExecution, sommaireURI, idTraitement, jobExecution
                  .getAllFailureExceptions());

            exitTraitement.setExitMessage("Traitement en erreur");
            exitTraitement.setSucces(false);
         }

         /* erreurs Spring non gérées */
      } catch (Throwable e) {

         LOGGER.warn(
               "{} - erreur lors de la capture de masse. Exception levée",
               TRC_CAPTURE, e);

         List<Throwable> listThrowables = new ArrayList<Throwable>();
         listThrowables.add(e);
         checkFinal(jobExecution, sommaireURI, idTraitement, listThrowables);

         exitTraitement.setExitMessage(e.getMessage());
         exitTraitement.setSucces(false);
      }

      return exitTraitement;
   }

}