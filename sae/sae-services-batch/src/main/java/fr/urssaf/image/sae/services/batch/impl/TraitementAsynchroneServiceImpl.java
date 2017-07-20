package fr.urssaf.image.sae.services.batch.impl;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobRequestAlreadyExistsException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.common.utils.BatchAuthentificationUtils;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.batch.exception.JobTypeInexistantException;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du service {@link TraitementAsynchroneService}
 * 
 * 
 */
@Service
public class TraitementAsynchroneServiceImpl implements
TraitementAsynchroneService {

   private static final Logger LOG = LoggerFactory
         .getLogger(TraitementAsynchroneServiceImpl.class);

   private static final String TRC_LANCER = "lancerJob";

   private final JobLectureService jobLectureService;

   private final JobQueueService jobQueueService;

   @Autowired
   @Qualifier("captureMasseTraitement")
   private TraitementExecutionSupport captureMasse;

   @Autowired
   @Qualifier("suppressionMasseTraitement")
   private TraitementExecutionSupport suppressionMasse;

   @Autowired
   @Qualifier("restoreMasseTraitement")
   private TraitementExecutionSupport restoreMasse;

   @Autowired
   @Qualifier("modificationMasseTraitement")
   private TraitementExecutionSupport modificationMasse;

   @Autowired
   @Qualifier("transfertMasseTraitement")
   private TraitementExecutionSupport transfertMasse;

   @Autowired
   @Qualifier("repriseMasseTraitement")
   private TraitementExecutionSupport repriseMasse;

   /**
    * 
    * @param jobLectureService
    *           service de lecture de la pile des travaux
    * @param jobQueueService
    *           service de la pile des travaux
    */
   @Autowired
   public TraitementAsynchroneServiceImpl(JobLectureService jobLectureService,
         JobQueueService jobQueueService) {

      this.jobLectureService = jobLectureService;
      this.jobQueueService = jobQueueService;

   }

   private void ajouterJob(TraitemetMasseParametres parameters)
         throws JobRequestAlreadyExistsException {

      LOG.debug(
            "{} - ajout d'un traitement de masse de type : {} pour  l'identifiant: {}",
            new Object[] { "ajouterJob()", parameters.getType(),
                  parameters.getUuid() });
      byte[] jobKey = createJobKey(parameters.getType().name(),
            parameters.getJobParameters());
      UUID jobRequestId = jobLectureService.getJobRequestIdByJobKey(jobKey);
      if (jobRequestId != null) {
         throw new JobRequestAlreadyExistsException(jobRequestId);
      }
      JobToCreate job = new JobToCreate();
      job.setIdJob(parameters.getUuid());
      job.setType(parameters.getType().name());
      job.setParameters(parameters.getEcdeURL());
      job.setCreationDate(new Date());
      job.setClientHost(parameters.getClientHost());
      job.setSaeHost(parameters.getSaeHost());
      job.setDocCount(parameters.getNbreDocs());
      job.setDocCountTraite(0);
      job.setVi(parameters.getVi());
      job.setJobParameters(parameters.getJobParameters());
      job.setJobKey(jobKey);
      jobQueueService.addJob(job);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void lancerJob(UUID idJob) throws JobInexistantException,
   JobNonReserveException {

      JobRequest job = jobLectureService.getJobRequest(idJob);

      // Vérification que le traitement existe bien dans la pile des travaux
      if (job == null) {
         throw new JobInexistantException(idJob);
      }

      LOG.debug("{} - récupération du VI", TRC_LANCER);

      AuthenticationToken token = BatchAuthentificationUtils.getToken(job);

      LOG.debug("{} - initialisation du contexte de sécurité", TRC_LANCER);
      AuthenticationContext.setModeHeritage();
      AuthenticationContext.setAuthenticationToken(token);

      // Vérification que le type de traitement existe bien
      if (!Constantes.typeJobExist(job.getType())) {
         throw new JobTypeInexistantException(job);
      }

      // Vérification que le job est bien réservé
      if (!JobState.RESERVED.equals(job.getState())) {
         throw new JobNonReserveException(idJob);
      }

      // Récupération du PID
      String pid = null;
      String processName = ManagementFactory.getRuntimeMXBean().getName();

      if (processName.contains("@")) {
         pid = processName.split("@")[0];
         LOG.debug("PID = " + pid);
         jobQueueService.renseignerPidJob(idJob, Integer.valueOf(pid));
      } else {
         LOG.info("impossible de récupérer le pid");
      }

      // démarrage du job et mise à jour de la pile des travaux
      jobQueueService.startingJob(idJob, new Date());

      // Ajout d'une trace
      UUID timeUuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobQueueService.addHistory(idJob, timeUuid, "LANCEMENT DU JOB.");

      ExitTraitement exitTraitement = new ExitTraitement();
      try {
         /*
          * Appel de l'implémentation de TraitementExecutionSupport pour
          * l'exécution du traitement de masse
          */
         if (job.getType().equals(TYPES_JOB.capture_masse.name())) {
            exitTraitement = captureMasse.execute(job);
         } else if (job.getType().equals(TYPES_JOB.suppression_masse.name())) {
            exitTraitement = suppressionMasse.execute(job);
         } else if (job.getType().equals(TYPES_JOB.restore_masse.name())) {
            exitTraitement = restoreMasse.execute(job);
         } else if (job.getType().equals(TYPES_JOB.modification_masse.name())) {
            exitTraitement = modificationMasse.execute(job);
         } else if (job.getType().equals(TYPES_JOB.transfert_masse.name())) {
            exitTraitement = transfertMasse.execute(job);
         } else if (job.getType().equals(TYPES_JOB.reprise_masse.name())) {
            exitTraitement = lancerReprise(job);
         } else {
            LOG.warn(
                  "Impossible d'executer le traitement ID={0}, de type {1}.",
                  job.getIdJob(), job.getType());
            exitTraitement.setSucces(false);
            String mssg = "Impossible d'executer le type de traitement "
                  + job.getType();
            exitTraitement.setExitMessage(mssg);
         }
      } catch (Exception e) {
         LOG.warn("Erreur grave lors de l'exécution  du traitement.", e);
         exitTraitement.setSucces(false);
         exitTraitement.setExitMessage(e.getMessage());
      }

      LOG.debug(
            "{} - le traitement n°{} est terminé {}. Message de sortie : {}.",
            new Object[] {
                  "lancerJob()",
                  job.getIdJob(),
                  BooleanUtils.toString(exitTraitement.isSucces(),
                        "avec succès", "sur un échec"),
                        exitTraitement.getExitMessage() });

      String codeTraitement = null;
      if (job.getJobParameters() != null) {
         codeTraitement = job.getJobParameters()
               .get(Constantes.CODE_TRAITEMENT);
      }

      // le traitement est terminé
      // on met à jour la pile des travaux
      jobQueueService.endingJob(idJob, exitTraitement.isSucces(), new Date(),
            exitTraitement.getExitMessage(), codeTraitement, exitTraitement.getNbDocumentTraite());
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    */
   @Override
   public void ajouterJobCaptureMasse(TraitemetMasseParametres parametres)
         throws JobRequestAlreadyExistsException {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    */
   @Override
   public void ajouterJobRestoreMasse(TraitemetMasseParametres parametres)
         throws JobRequestAlreadyExistsException {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    */
   @Override
   public void ajouterJobSuppressionMasse(TraitemetMasseParametres parametres)
         throws JobRequestAlreadyExistsException {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    */
   @Override
   public void ajouterJobTransfertMasse(TraitemetMasseParametres parametres)
         throws JobRequestAlreadyExistsException {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    */
   @Override
   public void ajouterJobModificationMasse(TraitemetMasseParametres parametres)
         throws JobRequestAlreadyExistsException {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    */
   @Override
   public void ajouterJobReprise(TraitemetMasseParametres parametres)
         throws JobRequestAlreadyExistsException {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public List<JobRequest> recupererJobs(List<UUID> listeUuid) {

      List<JobRequest> listJobs = new ArrayList<JobRequest>();

      for (UUID idJob : listeUuid) {
         JobRequest job = jobLectureService.getJobRequest(idJob);
         // Si le job n'est pas trouvé, on renvoie juste l'UUID dans le
         // jobrequest
         // Ceci est utilisé ensuite pour renvoyer une état UNKNOWN lors de
         // l'appel du service de récupération des états des jobs (demande de
         // Saturne)
         // Nous préférons de pas ajouter un JobState UNKNOWN pour éviter tout
         // risque de régression

         if (job == null) {
            job = new JobRequest();
            job.setIdJob(idJob);
         }
         listJobs.add(job);
      }

      return listJobs;
   }

   /**
    * Permet de lancer le traitement de reprise de masse passé en paramètre
    * @param jobReprise
    *           Le job de reprise
    *           
    * @return ExitTraitement résultat de l'exécution d'un traitement de masse.
    * @throws JobInexistantException
    */
   @Override
   public ExitTraitement lancerReprise(JobRequest jobReprise)
         throws JobInexistantException {

      ExitTraitement exitTraitement = new ExitTraitement();

      // 1- Vérifier si le param uidJobAReprendre est bien renseigné
      String jobAReprendreParam = jobReprise.getJobParameters().get(
            Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH);

      // 2- Vérifier si le jobAReprendre existe en base
      UUID idJobAReprendre = UUID.fromString(jobAReprendreParam);

      // Récupérer le job à reprendre
      JobRequest jobAReprendre = jobLectureService.getJobRequest(idJobAReprendre);

      if (jobAReprendre != null) {
         LOG.debug("Lancement de la reprise du traitement - {}",
               idJobAReprendre.toString());

         // Lancer la reprise de masse
         exitTraitement = repriseMasse.execute(jobReprise);
      }else {
         throw new JobInexistantException(idJobAReprendre);
      }

      return exitTraitement;
   }

   /**
    * Crée une "clé" permettant de résumer un job et ses paramètres.
    * 
    * @param jobName
    *           Le nom du job
    * @param jobParameters
    *           Les paramètres du job
    * @return la "clé" (correspond à un MD5)
    */
   public static byte[] createJobKey(String jobName,
         Map<String, String> jobParameters) {
      Assert.notNull(jobName, "Job name must not be null.");
      Assert.notNull(jobParameters, "JobParameters must not be null.");
      String keyJobName = "__jobName";
      jobParameters.put(keyJobName, jobName);
      StringBuffer stringBuffer = new StringBuffer();
      List<String> keys = new ArrayList<String>(jobParameters.keySet());
      Collections.sort(keys);
      for (String key : keys) {
         String jobParameter = jobParameters.get(key);
         String value = jobParameter == null ? StringUtils.EMPTY : jobParameter;
         stringBuffer.append(key + "=" + value + ";");
      }
      jobParameters.remove(keyJobName);
      MessageDigest digest;
      try {
         digest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException e) {
         throw new IllegalStateException(
               "MD5 algorithm not available.  Fatal (should be in the JDK).");
      }
      try {
         byte[] bytes = digest
               .digest(stringBuffer.toString().getBytes("UTF-8"));
         return bytes;
      } catch (UnsupportedEncodingException e) {
         throw new IllegalStateException(
               "UTF-8 encoding not available.  Fatal (should be in the JDK).");
      }

   }

}
