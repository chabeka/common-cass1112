package fr.urssaf.image.sae.services.batch.transfert.impl;

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

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.StatutCaptureUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.verification.VerificationSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.modification.impl.SAEModificationMasseServiceImpl;
import fr.urssaf.image.sae.services.batch.transfert.SAETransfertMasseService;
import fr.urssaf.image.sae.services.batch.transfert.support.stockage.multithreading.TransfertPoolThreadExecutor;

/**
 * Implémentation du service {@link SAETransfertMasseService}
 */
@Service
public class SAETransfertMasseServiceImpl implements SAETransfertMasseService{

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAEModificationMasseServiceImpl.class);

   /**
    * Trace
    */
   private static final String TRC_TRANSFERT = "transfertMasse()";

   /**
    * Catch PMD suppress
    */
   private static final String CATCH = "AvoidCatchingThrowable";

   /**
    * Executable du traitement de masse.
    */
   @Autowired
   private JobLauncher jobLauncher;

   /**
    * Service de gestion de la pile des travaux.
    */
   @Autowired
   private JobQueueService jobQueueService;

   /**
    * Support pour la verification final du traitement.
    */
   @Autowired
   private VerificationSupport verifSupport;

   /**
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private TransfertPoolThreadExecutor executor;

   /**
    * Job de transfert de documents
    */
   @Autowired
   @Qualifier("transfert_masse")
   private Job job;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings(CATCH)
   @Override
   public ExitTraitement transfertMasse(final URI sommaireURI, final UUID idTraitement, final String hash,
         String typeHash) {

      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      if (sommaireURI != null) {
         mapParam.put(Constantes.SOMMAIRE,
               new JobParameter(sommaireURI.toString()));  
      } else {
         throw new IllegalArgumentException("Le fichier sommaire n'est pas renseigné");
      }
      if (idTraitement != null) {
         mapParam.put(Constantes.ID_TRAITEMENT, new JobParameter(idTraitement
               .toString()));  
      } else {
         throw new IllegalArgumentException("L'identifiant du job n'est pas renseigné");
      }
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

         // met a jour le job pour renseigner le nombre de docs transférés
         int nbDocsTransfert = 0;
         if (jobExecution.getExecutionContext().containsKey(
               Constantes.NB_INTEG_DOCS)) {
            nbDocsTransfert = jobExecution.getExecutionContext().getInt(
                  Constantes.NB_INTEG_DOCS);
         }

         jobQueueService.renseignerDocCountJob(idTraitement, nbDocsTransfert);

         /* erreurs Spring non gérées */
      } catch (Throwable e) {

         LOGGER.warn(
               "{} - erreur lors de la capture de masse. Exception levée",
               TRC_TRANSFERT, e);

         List<Throwable> listThrowables = new ArrayList<Throwable>();
         listThrowables.add(e);
         checkFinal(jobExecution, sommaireURI, idTraitement, listThrowables);

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
   private void checkFinal(final JobExecution jobExecution, final URI sommaireURL,
         final UUID idTraitement, final List<Throwable> listeExceptions) {

      Integer nbreDocs = null;
      Integer nbDocsIntegres = null;
      String batchModeTraitement = null;
      boolean logPresent = false;

      if (jobExecution != null) {
         nbreDocs = (Integer) jobExecution.getExecutionContext().get(
               Constantes.DOC_COUNT);

         nbDocsIntegres = (Integer) jobExecution.getExecutionContext().get(
               Constantes.NB_INTEG_DOCS);
         logPresent = jobExecution.getExecutionContext().containsKey(
               Constantes.FLAG_BUL003);

         batchModeTraitement = (String) jobExecution.getExecutionContext().get(
               Constantes.BATCH_MODE_NOM);
      }

      verifSupport.checkFinTraitement(sommaireURL, nbreDocs, nbDocsIntegres, batchModeTraitement,
            logPresent, listeExceptions, idTraitement, executor.getIntegratedDocuments(), TYPES_JOB.transfert_masse);

   }

}
