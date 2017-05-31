package fr.urssaf.image.sae.services.batch.impl;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
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
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;
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

   private void ajouterJob(TraitemetMasseParametres parameters) {

      LOG.debug(
            "{} - ajout d'un traitement de masse de type : {} pour  l'identifiant: {}",
            new Object[] { "ajouterJob()", parameters.getType(),
                  parameters.getUuid() });

      JobToCreate job = new JobToCreate();
      job.setIdJob(parameters.getUuid());
      job.setType(parameters.getType().name());
      job.setParameters(parameters.getEcdeURL());
      job.setCreationDate(new Date());
      job.setClientHost(parameters.getClientHost());
      job.setSaeHost(parameters.getSaeHost());
      job.setDocCount(parameters.getNbreDocs());
      job.setVi(parameters.getVi());
      job.setJobParameters(parameters.getJobParameters());
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
            exitTraitement.getExitMessage(), codeTraitement);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public void ajouterJobCaptureMasse(TraitemetMasseParametres parametres) {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public void ajouterJobRestoreMasse(TraitemetMasseParametres parametres) {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public void ajouterJobSuppressionMasse(TraitemetMasseParametres parametres) {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public void ajouterJobTransfertMasse(TraitemetMasseParametres parametres) {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public void ajouterJobModificationMasse(TraitemetMasseParametres parametres) {
      ajouterJob(parametres);
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    */
   @Override
   public void ajouterJobReprise(TraitemetMasseParametres parametres) {
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
   public ExitTraitement lancerReprise(JobRequest jobReprise)
         throws JobParameterTypeException, JobInexistantException {

      ExitTraitement exitTraitement = new ExitTraitement();

      // 1- Vérifier si le param uidJobAReprendre est bien renseigné
      String jobAReprendreParam = jobReprise.getJobParameters().get(
            Constantes.UUID_JOB_A_Reprendre);
      
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
         throw new JobTypeInexistantException(jobAReprendre);
      }

      return exitTraitement;
   }

}
