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

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.model.OrdonnanceurConfiguration;
import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
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

   private final DecisionService decisionService;

   private final JobService jobService;

   private final TraitementLauncherSupport captureMasseLauncher;

   private final OrdonnanceurConfiguration ordonnanceurConfiguration;

   private static final String FORMAT = "dd/MM/yyyy HH'h'mm ss's' SSS'ms'";

   /**
    * 
    * @param decisionService
    *           service de décision pour les traitements à lancer
    * @param jobService
    *           service de la pile des travaux
    * @param launcher
    *           service de lancement du traitement de la capture en masse
    * @param config
    *           configuration des traitements
    */
   @Autowired
   public CoordinationServiceImpl(DecisionService decisionService,
         JobService jobService,
         @Qualifier("captureMasseLauncher") TraitementLauncherSupport launcher,
         OrdonnanceurConfiguration config) {

      this.decisionService = decisionService;
      this.jobService = jobService;
      this.captureMasseLauncher = launcher;
      this.ordonnanceurConfiguration = config;

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
      this.captureMasseLauncher.lancerTraitement(traitement);

      // renvoie l'identifiant du traitement lancé
      return traitement.getIdJob();

   }

   private JobQueue trouverJobALancer() throws AucunJobALancerException {

      // etape 1 : Récupération de la liste des traitements

      List<JobRequest> jobsEnCours = this.jobService.recupJobEnCours();
      LOG.debug("{} - nombre de traitements en cours ou réservés: {}",
            PREFIX_LOG, CollectionUtils.size(jobsEnCours));

      List<JobQueue> jobsEnAttente = this.jobService.recupJobsALancer();
      LOG.debug("{} - nombre de traitements en attente: {}", PREFIX_LOG,
            CollectionUtils.size(jobsEnAttente));

      // Etape 2 : Contrôle de la pile des travaux
      controlePile(jobsEnCours);

      // Etape 3: Décision du traitement à lancer

      JobQueue traitement = this.decisionService.trouverJobALancer(
            jobsEnAttente, jobsEnCours);
      LOG
            .debug("{} - traitement à lancer {}", PREFIX_LOG,
                  toString(traitement));

      // Etape 4 : Réservation du traitement

      try {

         LOG.debug("{} - réservation du traitement {}", PREFIX_LOG,
               toString(traitement));
         this.jobService.reserveJob(traitement.getIdJob());

      } catch (JobInexistantException e) {

         // le traitement n'existe plus, on chercher un nouveau traitement à
         // lancer
         LOG
               .warn(
                     "{} - échec de la réservation du traitement {} - il n'existe plus",
                     new Object[] { PREFIX_LOG, toString(traitement) });
         // traitement = trouverJobALancer();
         throw new JobRuntimeException(traitement, e);

      } catch (JobDejaReserveException e) {

         // le traitement est déjà réservé, on chercher un nouveau traitement à
         // lancer
         LOG
               .warn(
                     "{} - échec de la réservation du traitement {} - il est déjà réservé par {}",
                     new Object[] { PREFIX_LOG, toString(traitement),
                           e.getServer() });
         // traitement = trouverJobALancer();
         throw new JobRuntimeException(traitement, e);

      } catch (RuntimeException e) {

         // le traitement n'a pas pu être réservé pour une raison inconnue
         LOG
               .warn("{} - échec de la réservation du traitement {} - {}",
                     new Object[] { PREFIX_LOG, toString(traitement),
                           e.getMessage() });

         throw new JobRuntimeException(traitement, e);
      }

      // renvoie le traitement
      return traitement;
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
               && currentDate.after(DateUtils.addMinutes(jobCourant
                     .getReservationDate(), ordonnanceurConfiguration
                     .getTpsMaxReservation()))
               && !Boolean.TRUE.equals(jobCourant.getToCheckFlag());

         boolean isJobLanceBloque = JobState.STARTING.equals(jobCourant
               .getState())
               && currentDate.after(DateUtils.addMinutes(jobCourant
                     .getStartingDate(), ordonnanceurConfiguration
                     .getTpsMaxTraitement()))
               && !Boolean.TRUE.equals(jobCourant.getToCheckFlag());

         if (isJobReserveBloque) {
            LOG
                  .error(
                        "Contrôler le traitement n°{}. Raison : Etat \"réservé\" "
                              + "depuis plus de {} minutes (date de réservation : {}, date de contrôle : {})",
                        new Object[] {
                              jobCourant.getIdJob(),
                              ordonnanceurConfiguration.getTpsMaxReservation(),
                              DateFormatUtils.format(jobCourant
                                    .getReservationDate(), FORMAT),
                              DateFormatUtils.format(currentDate, FORMAT) });
            try {
               jobService.updateToCheckFlag(jobCourant.getIdJob(), true,
                     "Job réservé depuis plus de "
                           + ordonnanceurConfiguration.getTpsMaxReservation()
                           + " minutes (date de réservation : "
                           + DateFormatUtils.format(jobCourant
                                 .getReservationDate(), FORMAT)
                           + ", date de contrôle : "
                           + DateFormatUtils.format(currentDate, FORMAT));

            } catch (JobInexistantException e) {
               LOG.warn("Impossible de modifier le Job, il n'existe pas", e);
            }

         } else if (isJobLanceBloque) {
            LOG
                  .error(
                        "Contrôler le traitement n°{}. Raison : Etat \"en cours\" "
                              + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                              + "date de contrôle : {})", new Object[] {
                              jobCourant.getIdJob(),
                              ordonnanceurConfiguration.getTpsMaxTraitement(),
                              DateFormatUtils.format(jobCourant
                                    .getStartingDate(), FORMAT),
                              DateFormatUtils.format(currentDate, FORMAT) });
            try {
               jobService.updateToCheckFlag(jobCourant.getIdJob(), true,
                     "Job en cours depuis plus de "
                           + ordonnanceurConfiguration.getTpsMaxTraitement()
                           + " minutes (date de démarrage : "
                           + DateFormatUtils.format(jobCourant
                                 .getStartingDate(), FORMAT)
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
