package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.exception.ParameterRuntimeException;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.model.OrdonnanceurConfiguration;
import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementLauncherSupport;
import fr.urssaf.image.sae.ordonnanceur.util.ProcessChecker;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;

/**
 * implémentation du service {@link CoordinationService}
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

  private static final String FORMAT = "dd/MM/yyyy HH'h'mm ss's' SSS'ms'";

  /**
   * @param decisionService
   *          service de décision pour les traitements à lancer
   * @param jobService
   *          service de la pile des travaux
   * @param launcher
   *          service de lancement du traitement de masse
   * @param config
   *          configuration des traitements
   */
  @Autowired
  public CoordinationServiceImpl(final DecisionService decisionService,
                                 final JobService jobService, final OrdonnanceurConfiguration config) {

    this.decisionService = decisionService;
    this.jobService = jobService;
    ordonnanceurConfiguration = config;
  }

  private static final Logger LOG = LoggerFactory
      .getLogger(CoordinationServiceImpl.class);

  private static final String PREFIX_LOG = "ordonnanceur()";

  /**
   * {@inheritDoc}
   */
  @Override
  public final UUID lancerTraitement() throws AucunJobALancerException {

    // Récupération d'un traitement à lancer
    final JobQueue traitement = trouverJobALancer();

    // Récupération de l'identifiant du traitement de capture en masse
    LOG.debug("{} - lancement du traitement {}",
              PREFIX_LOG,
              toString(traitement));
    traitementMasseLauncher.lancerTraitement(traitement);

    // renvoie l'identifiant du traitement lancé
    return traitement.getIdJob();

  }

  /**
   * Récuperer et réserver le job qui doit être lancé.
   * 
   * @return Le Job à lancer.
   * @throws AucunJobALancerException
   * @{@link AucunJobALancerException}
   */
  private JobQueue trouverJobALancer() throws AucunJobALancerException {

    // Etape 1 : Récupération de la liste des traitements
    final List<JobRequest> jobsEnCours = jobService.recupJobEnCours();
    LOG.debug("{} - nombre de traitements en cours ou réservés: {}",
              PREFIX_LOG,
              CollectionUtils.isNotEmpty(jobsEnCours) ? CollectionUtils.size(jobsEnCours) : 0);

    final List<JobQueue> jobsEnAttente = jobService.recupJobsALancer();
    LOG.debug("{} - nombre de traitements en attente: {}",
              PREFIX_LOG,
              CollectionUtils.isNotEmpty(jobsEnAttente) ? CollectionUtils.size(jobsEnAttente) : 0);

    // Etape 2 : Contrôle de la pile des travaux
    controlePile(jobsEnCours);

    // Etape 3: Décision du traitement à lancer

    final List<JobQueue> listeTraitement = decisionService
        .trouverListeJobALancer(jobsEnAttente, jobsEnCours);

    // Etape 4 : Réservation du traitement
    JobQueue traitement = null;
    for (final JobQueue jobQueue : listeTraitement) {
      if (isJobSelectionnableALancer(jobQueue)) {
        try {
          // Etape 5 : Vérification que l'URL ECDE est toujours actif
          if (!Constantes.REPRISE_MASSE_JN.equals(jobQueue.getType())) {
            decisionService
            .controleDispoEcdeTraitementMasse(jobQueue);
          }
          // Etape 6 : Positionne le sémaphore pour le traitement
          // sélectionné
          traitement = jobService
              .reserverCodeTraitementJobALancer(jobQueue);
          break;
        }
        catch (final ParameterRuntimeException e) {
          // le job est déjà en cours de traitement, on cherche un nouveau
          // job à traiter
          LOG.warn(
                   "{} - échec lors de la confirmation de disponibilité du job à lancer - il est déjà en cours de traitement - Recherche d'un nouveau job en cours...",
                   new Object[] {PREFIX_LOG});
        }
        catch (final AucunJobALancerException e) {
          // L'URL ECDE du job est éronné. Le job n'est donc pas
          // séléectionné.
          LOG.warn(
                   "{} - échec lors de la confirmation de disponibilité du job à lancer - l'URL ECDE du job est erroné - Recherche d'un nouveau job en cours...",
                   new Object[] {PREFIX_LOG});
        }
      } else {
        LOGGER.info("{} - Le job " + jobQueue.getIdJob().toString()
                    + " n'est pas sélectionné - Passage au job suivant");
      }
    }

    // Si on ne trouve aucun job de disponible, on mets en attente
    // l'ordonnanceur.
    if (traitement == null) {
      throw new AucunJobALancerException();
    }

    // Etape 7 : Réservation du Job
    LOG.debug("{} - traitement à lancer {}", PREFIX_LOG, toString(traitement));
    try {

      LOG.debug("{} - réservation du traitement {}",
                PREFIX_LOG,
                toString(traitement));
      jobService.reserveJob(traitement.getIdJob());

    }
    catch (final JobInexistantException e) {

      // le traitement n'existe plus, on chercher un nouveau traitement à
      // lancer
      LOG.warn(
               "{} - échec de la réservation du traitement {} - il n'existe plus",
               new Object[] {PREFIX_LOG, toString(traitement)});
      // traitement = trouverJobALancer();
      throw new JobRuntimeException(traitement, e);

    }
    catch (final JobDejaReserveException e) {

      // le traitement est déjà réservé, on chercher un nouveau traitement à
      // lancer
      LOG.warn(
               "{} - échec de la réservation du traitement {} - il est déjà réservé par {}",
               new Object[] {PREFIX_LOG, toString(traitement), e.getServer()});
      // traitement = trouverJobALancer();
      throw new JobRuntimeException(traitement, e);

    }
    catch (final RuntimeException e) {

      // le traitement n'a pas pu être réservé pour une raison inconnue
      LOG.warn(
               "{} - échec de la réservation du traitement {} - {}",
               new Object[] {PREFIX_LOG, toString(traitement), e.getMessage()});

      throw new JobRuntimeException(traitement, e);
    }

    // renvoie le traitement
    return traitement;
  }

  /**
   * Détermine si le job est selectionnable pour les traitements suivants.
   * 
   * @param jobQueue
   *          job {@link jobQueue}
   * @return True si le job est selectionnable pour les traitements suivants,
   *         false sinon.
   */
  private boolean isJobSelectionnableALancer(final JobQueue jobQueue) {
    return !jobService.isJobCodeTraitementEnCoursOuFailure(jobQueue);
  }

  /**
   * Vérifie que les traitements en cours ne sont pas bloqués. Si c'est le cas,
   * ajout d'un historique et d'un LOG en erreur à destination de
   * l'exploitation
   * 
   * @param jobsEnCours
   */
  private void controlePile(final List<JobRequest> jobsEnCours) {
    Date currentDate;
    for (final JobRequest jobCourant : jobsEnCours) {
      currentDate = new Date();

      final boolean isJobReserveBloque = JobState.RESERVED.equals(jobCourant
                                                                  .getState())
          && currentDate.after(DateUtils.addMinutes(
                                                    jobCourant.getReservationDate(),
                                                    ordonnanceurConfiguration.getTpsMaxReservation()))
          && !Boolean.TRUE.equals(jobCourant.getToCheckFlag());

      final boolean isJobLanceBloque = JobState.STARTING.equals(jobCourant
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
                                    DateFormatUtils.format(currentDate, FORMAT)});
        try {
          jobService.updateToCheckFlag(
                                       jobCourant.getIdJob(),
                                       true,
                                       "Job réservé depuis plus de "
                                           + ordonnanceurConfiguration.getTpsMaxReservation()
                                           + " minutes (date de réservation : "
                                           + DateFormatUtils.format(
                                                                    jobCourant.getReservationDate(),
                                                                    FORMAT)
                                           + ", date de contrôle : "
                                           + DateFormatUtils.format(currentDate, FORMAT));

        }
        catch (final JobInexistantException e) {
          LOG.warn("Impossible de modifier le Job, il n'existe pas", e);
        }

      } else if (isJobLanceBloque) {

        // Le job semnble bloqué à l'état STARTING, on vérifie si le process
        // tourne toujours
        LOG.debug(
                  "Le traitement n°{} semble bloqué à l'état \"en cours\" "
                      + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                      + "date de contrôle : {}) - Vérification de l'existence du process",
                      new Object[] {
                                    jobCourant.getIdJob(),
                                    ordonnanceurConfiguration.getTpsMaxTraitement(),
                                    DateFormatUtils.format(jobCourant.getStartingDate(),
                                                           FORMAT),
                                    DateFormatUtils.format(currentDate, FORMAT)});
        boolean isProcessRunning = true;
        try {
          isProcessRunning = verificationProcessRunning(jobCourant);
        }
        catch (final Exception e) {
          LOG.warn(
                   "Le traitement n°{} semble bloqué à l'état \"en cours\" "
                       + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                       + "date de contrôle : {}) - Echec vérification existence process - {}",
                       new Object[] {
                                     jobCourant.getIdJob(),
                                     ordonnanceurConfiguration.getTpsMaxTraitement(),
                                     DateFormatUtils.format(jobCourant.getStartingDate(),
                                                            FORMAT),
                                     DateFormatUtils.format(currentDate, FORMAT),
                                     e.getMessage()});

          // On met un message sur le job mais sans changer le flag car on
          // ne sait pas si le process existe
          try {
            jobService.updateToCheckFlag(
                                         jobCourant.getIdJob(),
                                         false,
                                         "Job en cours depuis plus de "
                                             + ordonnanceurConfiguration.getTpsMaxTraitement()
                                             + " minutes (date de démarrage : "
                                             + DateFormatUtils.format(
                                                                      jobCourant.getStartingDate(),
                                                                      FORMAT)
                                             + ", date de contrôle : "
                                             + DateFormatUtils.format(currentDate, FORMAT)
                                             + " - ECHEC vérification existence process !!");
          }
          catch (final JobInexistantException ex) {
            LOG.warn("Impossible de modifier le Job, il n'existe pas", ex);
          }
        }

        // Le process ne tourne plus, on peut indiquer que le job est bloqué
        if (!isProcessRunning) {

          LOG.debug(
                    "Le traitement n°{} semble bloqué à l'état \"en cours\" "
                        + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                        + "date de contrôle : {}) - Le process n'a pas été trouvé",
                        new Object[] {
                                      jobCourant.getIdJob(),
                                      ordonnanceurConfiguration.getTpsMaxTraitement(),
                                      DateFormatUtils.format(jobCourant.getStartingDate(),
                                                             FORMAT),
                                      DateFormatUtils.format(currentDate, FORMAT)});

          LOG.error(
                    "Contrôler le traitement n°{}. Raison : Etat \"en cours\" "
                        + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                        + "date de contrôle : {})",
                        new Object[] {
                                      jobCourant.getIdJob(),
                                      ordonnanceurConfiguration.getTpsMaxTraitement(),
                                      DateFormatUtils.format(jobCourant.getStartingDate(),
                                                             FORMAT),
                                      DateFormatUtils.format(currentDate, FORMAT)});
          try {
            jobService.updateToCheckFlag(
                                         jobCourant.getIdJob(),
                                         true,
                                         "Job en cours depuis plus de "
                                             + ordonnanceurConfiguration.getTpsMaxTraitement()
                                             + " minutes (date de démarrage : "
                                             + DateFormatUtils.format(
                                                                      jobCourant.getStartingDate(),
                                                                      FORMAT)
                                             + ", date de contrôle : "
                                             + DateFormatUtils.format(currentDate, FORMAT));

          }
          catch (final JobInexistantException e) {
            LOG.warn("Impossible de modifier le Job, il n'existe pas", e);
          }
        } else {
          LOG.debug(
                    "Le traitement n°{} semble bloqué à l'état \"en cours\" "
                        + "depuis plus de {} minutes (date de démarrage du traitement : {}, "
                        + "date de contrôle : {}) - Le process tourne toujours",
                        new Object[] {
                                      jobCourant.getIdJob(),
                                      ordonnanceurConfiguration.getTpsMaxTraitement(),
                                      DateFormatUtils.format(jobCourant.getStartingDate(),
                                                             FORMAT),
                                      DateFormatUtils.format(currentDate, FORMAT)});
        }
      }
    }
  }

  public boolean verificationProcessRunning(final JobRequest jobCourant)
      throws IOException, InterruptedException, ExecutionException {
    final int delay = 1000;
    ProcessBuilder pb = null;
    if (SystemUtils.IS_OS_WINDOWS) {
      // pb = new ProcessBuilder("cmd.exe", "/C", "tasklist /fi \"PID eq "
      // + jobCourant.getPid() + "\" 2>&1");
      pb = new ProcessBuilder("cmd.exe",
                              "/C",
                              "tasklist /fi \"PID eq "
                                  + jobCourant.getPid() + "\"");

    } else if (SystemUtils.IS_OS_LINUX) {
      // pb = new ProcessBuilder("/bin/sh", "-c",
      // "ps aux | awk '{print $2 }' | grep " + jobCourant.getPid() +
      // " 2>&1");
      pb = new ProcessBuilder("/bin/sh",
                              "-c",
                              "ps aux | awk '{print $2 }' | grep " + jobCourant.getPid());
    }
    if (pb == null) {
      throw new RuntimeException("ProcessBuilder null");
    }
    // Lancement de la commande de vérification du process
    final Process p = pb.start();

    // Initialisation du scheduler de thread
    final ScheduledExecutorService scheduler = Executors
        .newSingleThreadScheduledExecutor();

    // Initialisation du Thread de contrôle du résultat de la vérification du
    // process
    final ProcessChecker processUtils = new ProcessChecker(p.getInputStream(),
                                                           p.getErrorStream(),
                                                           jobCourant.getPid());

    final Future<Boolean> result = scheduler.schedule(processUtils,
                                                      delay,
                                                      TimeUnit.MILLISECONDS);

    // On vérifie que la commande de vérification du process soit bien
    // terminée
    p.waitFor();

    // On attend que le thread contenu dans le pool de thread soit exécuté
    while (!result.isDone() && !result.isCancelled()) {
      LOG.debug(
                "Le traitement n°{} est en cours de vérification (date de démarrage du traitement : {}, "
                    + "date de contrôle : {})",
                    new Object[] {
                                  jobCourant.getIdJob(),
                                  DateFormatUtils.format(jobCourant.getStartingDate(),
                                                         FORMAT),
                                  DateFormatUtils.format(new Date(), FORMAT)});
      // Attend le temps du delay que le thread de vérification se termine.
      // Evite de logger trop souvent le message ci-dessus
      Thread.sleep(delay);
    }

    // Récupération du resultat de la vérification
    return result.get();
  }

  private String toString(final JobQueue traitement) {
    return "'" + traitement.getType() + "' identifiant:"
        + traitement.getIdJob();
  }

}
