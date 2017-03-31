package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.exception.ParameterRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.model.OrdonnanceurConfiguration;
import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementLauncherSupport;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;

/**
 * implémentation du service {@link CoordinationService}
 * 
 * 
 */
@Service
public class CoordinationServiceImpl implements CoordinationService {

   /**
    * Logger.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(CoordinationServiceImpl.class);

   private final DecisionService decisionService;

   private final JobService jobService;

   @Autowired
   @Qualifier("traitementMasseLauncher")
   private TraitementLauncherSupport traitementMasseLauncher;

   private final OrdonnanceurConfiguration ordonnanceurConfiguration;

   private final DFCESupport dfceSuppport;

   private static final String FORMAT = "dd/MM/yyyy HH'h'mm ss's' SSS'ms'";

   /**
    * 
    * @param decisionService
    *           service de décision pour les traitements à lancer
    * @param jobService
    *           service de la pile des travaux
    * @param launcher
    *           service de lancement du traitement de masse
    * @param config
    *           configuration des traitements
    */
   @Autowired
   public CoordinationServiceImpl(DecisionService decisionService,
         JobService jobService, OrdonnanceurConfiguration config,
         DFCESupport dfceSuppport) {

      this.decisionService = decisionService;
      this.jobService = jobService;
      this.ordonnanceurConfiguration = config;
      this.dfceSuppport = dfceSuppport;

   }

   private static final Logger LOG = LoggerFactory
         .getLogger(CoordinationServiceImpl.class);

   private static final String PREFIX_LOG = "ordonnanceur()";

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final UUID lancerTraitement() throws AucunJobALancerException {

      // Récupération d'un traitement à lancer
      JobQueue traitement = trouverJobALancer();

      // Récupération de l'identifiant du traitement de capture en masse
      LOG.debug("{} - lancement du traitement {}", PREFIX_LOG,
            toString(traitement));
      this.traitementMasseLauncher.lancerTraitement(traitement);

      // renvoie l'identifiant du traitement lancé
      return traitement.getIdJob();

   }

   /**
    * Trouver le job qui doit être lancé.
    * 
    * @return Le Job à lancer.
    * @throws AucunJobALancerException
    *            @{@link AucunJobALancerException}
    */
   private JobQueue trouverJobALancer() throws AucunJobALancerException {

      // Etape 1 : Récupération de la liste des traitements
      List<JobRequest> jobsEnCours = this.jobService.recupJobEnCours();
      LOG.debug("{} - nombre de traitements en cours ou réservés: {}",
            PREFIX_LOG, CollectionUtils.size(jobsEnCours));

      List<JobQueue> jobsEnAttente = this.jobService.recupJobsALancer();
      LOG.debug("{} - nombre de traitements en attente: {}", PREFIX_LOG,
            CollectionUtils.size(jobsEnAttente));

      // Etape 2 : Contrôle de la pile des travaux
      controlePile(jobsEnCours);

      // Etape 3: Décision du traitement à lancer

      List<JobQueue> listeTraitement = this.decisionService
            .trouverListeJobALancer(jobsEnAttente, jobsEnCours);

      // Etape 4 : vérification que le serveur DFCE est Up!
      if (!dfceSuppport.isDfceUp()) {
         LOG.debug("{} - DFCE n'est pas accessible avec la configuration",
               PREFIX_LOG);
         throw new AucunJobALancerException();

      }

      // Etape 5 : Réservation du traitement
      JobQueue traitement = null;
      for (JobQueue jobQueue : listeTraitement) {
         if (isJobSelectionnableALancer(jobQueue)) {
            try {
               traitement = this.jobService
                     .reserverCodeTraitementJobALancer(jobQueue);
               break;
            } catch (ParameterRuntimeException e) {
               // le job est déjà en cours de traitement, on cherche un nouveau
               // job à traiter
               LOG.warn(
                     "{} - échec lors de la confirmation de disponibilité du job à lancer - il est déjà en cours de traitement - Traitement d'un nouveau job en cours...",
                     new Object[] { PREFIX_LOG });
            }
         } else {
            LOGGER.info("{} - Le job " + jobQueue.getIdJob().toString()
                  + " n'est pas sélectionné - Passage au job suivant");
         }
      }

      // Si on ne trouve aucun job de disponible, on mets en attente
      // l'ordonnanceur.
      if (traitement == null) {
         throw new JobRuntimeException(traitement, new Exception(
               "Aucun job n'est disponible pour réservation"));
      }

      // Etape 6 : Vérification que l'URL ECDE est toujours actif
      this.decisionService.controleDispoEcdeTraitementMasse(traitement);

      // Etape 7 : Réservation du Job
      LOG.debug("{} - traitement à lancer {}", PREFIX_LOG, toString(traitement));
      try {

         LOG.debug("{} - réservation du traitement {}", PREFIX_LOG,
               toString(traitement));
         this.jobService.reserveJob(traitement.getIdJob());

      } catch (JobInexistantException e) {

         // le traitement n'existe plus, on chercher un nouveau traitement à
         // lancer
         LOG.warn(
               "{} - échec de la réservation du traitement {} - il n'existe plus",
               new Object[] { PREFIX_LOG, toString(traitement) });
         // traitement = trouverJobALancer();
         throw new JobRuntimeException(traitement, e);

      } catch (JobDejaReserveException e) {

         // le traitement est déjà réservé, on chercher un nouveau traitement à
         // lancer
         LOG.warn(
               "{} - échec de la réservation du traitement {} - il est déjà réservé par {}",
               new Object[] { PREFIX_LOG, toString(traitement), e.getServer() });
         // traitement = trouverJobALancer();
         throw new JobRuntimeException(traitement, e);

      } catch (RuntimeException e) {

         // le traitement n'a pas pu être réservé pour une raison inconnue
         LOG.warn(
               "{} - échec de la réservation du traitement {} - {}",
               new Object[] { PREFIX_LOG, toString(traitement), e.getMessage() });

         throw new JobRuntimeException(traitement, e);
      }

      // renvoie le traitement
      return traitement;
   }

   /**
    * Détermine si le job est selectionnable pour les traitements suivants.
    * 
    * @param jobQueue
    *           job {@link jobQueue}
    * @return True si le job est selectionnable pour les traitements suivants,
    *         false sinon.
    */
   private boolean isJobSelectionnableALancer(JobQueue jobQueue) {
      return !jobService.isJobCodeTraitementEnCoursOuFailure(jobQueue);
   }

   /**
    * Vérifie que les traitements en cours ne sont pas bloqués. Si c'est le cas,
    * ajout d'un historique et d'un LOG en erreur à destination de
    * l'exploitation
    * 
    * @param jobsEnCours
    */
   private void controlePile(List<JobRequest> jobsEnCours) {
      Date currentDate;
      for (JobRequest jobCourant : jobsEnCours) {

         currentDate = new Date();

         boolean isJobReserveBloque = JobState.RESERVED.equals(jobCourant
               .getState())
               && currentDate.after(DateUtils.addMinutes(
                     jobCourant.getReservationDate(),
                     ordonnanceurConfiguration.getTpsMaxReservation()))
               && !Boolean.TRUE.equals(jobCourant.getToCheckFlag());

         boolean isJobLanceBloque = JobState.STARTING.equals(jobCourant
               .getState())
               && currentDate.after(DateUtils.addMinutes(
                     jobCourant.getStartingDate(),
                     ordonnanceurConfiguration.getTpsMaxTraitement()))
               && !Boolean.TRUE.equals(jobCourant.getToCheckFlag());

         if (isJobReserveBloque) {
            LOG.error(
                  "Contrôler le traitement n°{}. Raison : Etat \"réservé\" "
                        + "depuis plus de {} minutes (date de réservation : {}, date de contrôle : {})",
                  new Object[] {
                        jobCourant.getIdJob(),
                        ordonnanceurConfiguration.getTpsMaxReservation(),
                        DateFormatUtils.format(jobCourant.getReservationDate(),
                              FORMAT),
                        DateFormatUtils.format(currentDate, FORMAT) });
            try {
               jobService.updateToCheckFlag(
                     jobCourant.getIdJob(),
                     true,
                     "Job réservé depuis plus de "
                           + ordonnanceurConfiguration.getTpsMaxReservation()
                           + " minutes (date de réservation : "
                           + DateFormatUtils.format(
                                 jobCourant.getReservationDate(), FORMAT)
                           + ", date de contrôle : "
                           + DateFormatUtils.format(currentDate, FORMAT));

            } catch (JobInexistantException e) {
               LOG.warn("Impossible de modifier le Job, il n'existe pas", e);
            }

         } else if (isJobLanceBloque) {
            LOG.error(
                  "Contrôler le traitement n°{}. Raison : Etat \"en cours\" "
                        + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                        + "date de contrôle : {})",
                  new Object[] {
                        jobCourant.getIdJob(),
                        ordonnanceurConfiguration.getTpsMaxTraitement(),
                        DateFormatUtils.format(jobCourant.getStartingDate(),
                              FORMAT),
                        DateFormatUtils.format(currentDate, FORMAT) });
            try {
               jobService.updateToCheckFlag(
                     jobCourant.getIdJob(),
                     true,
                     "Job en cours depuis plus de "
                           + ordonnanceurConfiguration.getTpsMaxTraitement()
                           + " minutes (date de démarrage : "
                           + DateFormatUtils.format(
                                 jobCourant.getStartingDate(), FORMAT)
                           + ", date de contrôle : "
                           + DateFormatUtils.format(currentDate, FORMAT));

            } catch (JobInexistantException e) {
               LOG.warn("Impossible de modifier le Job, il n'existe pas", e);
            }
         }
      }

   }

   private String toString(JobQueue traitement) {
      return "'" + traitement.getType() + "' identifiant:"
            + traitement.getIdJob();
   }

}
