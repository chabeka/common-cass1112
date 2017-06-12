package fr.urssaf.image.sae.services.batch.reprise.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.StatutCaptureUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.verification.VerificationSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.common.support.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;
import fr.urssaf.image.sae.services.batch.reprise.SAERepriseMasseService;

/**
 * Implémentation du service {@link SAERepriseMasseService}
 */
@Service
public class SAERepriseMasseServiceImpl implements SAERepriseMasseService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAERepriseMasseServiceImpl.class);

   /**
    * Trace
    */
   private static final String TRC_REPRISE = "repriseMasse()";

   /**
    * Executable du traitement de masse.
    */
   @Autowired
   private JobLauncher jobLauncher;

   @Autowired
   private JobLectureService jobLectureService;
   
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
   private InsertionPoolThreadExecutor executor;
   
   @Autowired
   private JobExplorer jobExplorer;
   @Autowired
   private JobRepository jobRepository;

   /**
    * Job de reprise de masse
    */
   @Autowired
   @Qualifier("reprise_masse")
   private Job job;

   @Override
   public ExitTraitement repriseMasse(UUID idJobReprise) {
      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>();
      JobRequest jobReprise = jobLectureService.getJobRequest(idJobReprise);

      String idJobAReprendreParam = jobReprise.getJobParameters().get(
            Constantes.UUID_JOB_A_Reprendre);
      UUID uidJobAReprendre = UUID.fromString(idJobAReprendreParam);
      JobRequest jobAReprendre = jobLectureService
            .getJobRequest(uidJobAReprendre);
      
      Assert.notNull(jobAReprendre, "Le job à reprendre est requis");

      // Gestion de droits pour la reprise
      List<String> pagmsReprise = jobReprise.getVi().getPagms();
      List<String> pagmsJobAReprendre = jobAReprendre.getVi().getPagms();
      boolean checkAccessReprise = true;
      
      if(!jobReprise.getVi().getCodeAppli().equals(jobAReprendre.getVi().getCodeAppli()) ){
         checkAccessReprise = false;
      }

      if(checkAccessReprise){
         for (String pagmAReprendre : pagmsJobAReprendre) {
            if(!pagmsReprise.contains(pagmAReprendre)){
               checkAccessReprise = false;
            }
         }
      }
      if (!checkAccessReprise) {
         throw new AccessDeniedException(
               "Erreur PAGMS de Reprise: Le job de reprise doit avoir le même contrat de service");
      }

      // Chargement des paramètres de reprise
      mapParam.put(Constantes.ID_TRAITEMENT_REPRISE, new JobParameter(
            idJobReprise.toString()));
      mapParam.put(Constantes.ID_TRAITEMENT_A_REPRENDRE, new JobParameter(
            idJobAReprendreParam));
      mapParam.put(Constantes.TYPE_TRAITEMENT_A_REPRENDRE, new JobParameter(
            jobAReprendre.getType()));
      
      String urlECDE = jobAReprendre.getJobParameters().get(Constantes.ECDE_URL);
      URI sommaireURL;
      try {
         sommaireURL = URI.create(urlECDE);
      } catch (IllegalArgumentException e) {
         throw new JobParameterTypeException(jobAReprendre, e);
      }

      JobParameters parameters = new JobParameters(mapParam);
      ExitTraitement exitTraitement = new ExitTraitement();
      JobExecution jobExecution = null;
      JobExecution lastExecution = null;

      try {
         jobExecution = jobLauncher.run(job, parameters);
         String jobName = jobAReprendre.getType();
         List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, 1);
         JobInstance internalJobInstance = instances.size() > 0 ? instances.get(0) : null;
         Assert.notNull(internalJobInstance, "ERREUR RREPRISE: Le traitement en erreur n'a pas été relancé par la reprise");
         lastExecution = jobRepository.getLastJobExecution(internalJobInstance.getJobName(),internalJobInstance.getJobParameters());
         boolean traitementOK = StatutCaptureUtils.isCaptureOk(lastExecution);
         if(traitementOK){
            // Validation de la reprise
            traitementOK = checkErreursReprise(lastExecution);
         }
         
         // Récupérer le nombre de documents traités par la reprise de masse
         int nbDocsTraites = 0;
         if (lastExecution.getExecutionContext().containsKey(
               Constantes.NB_INTEG_DOCS)) {
            nbDocsTraites = lastExecution.getExecutionContext().getInt(
                  Constantes.NB_INTEG_DOCS);
         } else if(lastExecution.getExecutionContext().containsKey(
               Constantes.NB_DOCS_RESTORES)){
            nbDocsTraites = lastExecution.getExecutionContext().getInt(
                  Constantes.NB_DOCS_RESTORES);
         } else if(lastExecution.getExecutionContext().containsKey(
               Constantes.NB_DOCS_SUPPRIMES)){
            nbDocsTraites = lastExecution.getExecutionContext().getInt(
                  Constantes.NB_DOCS_SUPPRIMES);
         }

         if (traitementOK) {
            exitTraitement.setExitMessage("Traitement réalisé avec succès");
            exitTraitement.setSucces(true);
            // Mise à jour compteur job à reprendre
            jobQueueService.renseignerDocCountJob(uidJobAReprendre,
                  nbDocsTraites);
            // Mise à jour compteur job reprise
            jobQueueService.renseignerDocCountJob(idJobReprise, nbDocsTraites);
         } else {
            checkFinal(lastExecution, sommaireURL, uidJobAReprendre,
                  jobExecution.getAllFailureExceptions(),
                  jobAReprendre.getType());

            exitTraitement.setExitMessage("Traitement en erreur");
            exitTraitement.setSucces(false);
            // Mise à jour compteur job reprise uniquement pour le voir le delta
            // qui a été repris.
            jobQueueService.renseignerDocCountJob(idJobReprise, nbDocsTraites);
         }

         /* erreurs Spring non gérées */
      } catch (Throwable e) {
         LOGGER.warn(
               "{} - erreur lors de la reprise de masse. Exception levée",
               TRC_REPRISE, e);

         List<Throwable> listThrowables = new ArrayList<Throwable>();
         listThrowables.add(e);
         checkFinal(lastExecution, sommaireURL, uidJobAReprendre, listThrowables, jobAReprendre.getType());
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
         final UUID idTraitement, final List<Throwable> listeExceptions, String typeJob) {

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
               Constantes.BATCH_MODE_NOM_REDIRECT);
      }

      verifSupport.checkFinTraitement(sommaireURL, nbreDocs, nbDocsIntegres, batchModeTraitement,
            logPresent, listeExceptions, idTraitement, executor.getIntegratedDocuments(), TYPES_JOB.valueOf(typeJob));

   }
   
   /**
    * Contrôle les erreurs éventuelles lors de la de reprise de traitement de masse
    * @param jobExecution
    * @return true si aucune erreur rencontrée, false sinon
    */
   @SuppressWarnings("unchecked")
   private boolean checkErreursReprise(final JobExecution jobExecution){
      boolean traitementOk = true;
      List<ConcurrentLinkedQueue<String>> listErreursReprise = new ArrayList<ConcurrentLinkedQueue<String>>();      
      listErreursReprise.add((ConcurrentLinkedQueue<String>) jobExecution.getExecutionContext().get(Constantes.CODE_EXCEPTION));
      listErreursReprise.add((ConcurrentLinkedQueue<String>) jobExecution.getExecutionContext().get(Constantes.DOC_EXCEPTION));
      listErreursReprise.add((ConcurrentLinkedQueue<String>) jobExecution.getExecutionContext().get(Constantes.INDEX_EXCEPTION));
      listErreursReprise.add((ConcurrentLinkedQueue<String>) jobExecution.getExecutionContext().get(Constantes.INDEX_REF_EXCEPTION));
      listErreursReprise.add((ConcurrentLinkedQueue<String>) jobExecution.getExecutionContext().get(Constantes.ROLLBACK_EXCEPTION));
      
      for (ConcurrentLinkedQueue<String> concurrentLinkedQueue : listErreursReprise) {
         if (concurrentLinkedQueue != null && !concurrentLinkedQueue.isEmpty()) {
            traitementOk =false;
         }
      }
      return traitementOk;
   }
  
}
