package fr.urssaf.image.sae.services.batch.validation;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.exception.JobParameterTypeException;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService}.<br>
 * La validation est basée sur la programmation aspect
 */
@Aspect
@Component
public class TraitementAsynchroneServiceValidation {

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(TraitementAsynchroneServiceValidation.class);

  private static final String CLASS = "fr.urssaf.image.sae.services.batch.TraitementAsynchroneService.";

  private static final String METHOD_AJOUT_CAPTURE = "execution(void " + CLASS
      + "ajouterJobCaptureMasse(*))" + "&& args(parametres)";

  private static final String METHOD_TRANSFERT_MASSE = "execution(void " + CLASS
      + "ajouterJobTransfertMasse(*))" + "&& args(parametres)";

  private static final String METHOD_AJOUT_SUPPRESSION = "execution(void "
      + CLASS + "ajouterJobSuppressionMasse(*))" + "&& args(parametres)";

  private static final String METHOD_AJOUT_RESTORE = "execution(void " + CLASS
      + "ajouterJobRestoreMasse(*))" + "&& args(parametres)";

  private static final String METHOD_MODIFICATION_MASSE = "execution(void "
      + CLASS + "ajouterJobModificationMasse(*))" + "&& args(parametres)";

  private static final String METHOD_RECUPERER_JOB = "execution(List<fr.urssaf.image.sae.pile.travaux.model.JobRequest> "
      + CLASS + "recupererJobs(*))" + "&& args(listeUuid)";

  private static final String METHOD_LANCER_JOB = "execution(void " + CLASS
      + "lancerJob(*))" + "&& args(idJob)";

  private static final String METHOD_REPRISE = "execution(void " + CLASS
      + "ajouterJobReprise(*))" + "&& args(parametres)";

  private static final String METHOD_LANCER_REPRISE = "execution(fr.urssaf.image.sae.services.batch.common.model.ExitTraitement " + CLASS
      + "lancerReprise(*))" + "&& args(jobReprise)";

  private static final String ARG_EMPTY = "L''argument ''{0}'' doit être renseigné.";

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobCaptureMasse}
   * 
   * @param parametres
   *          ensemble des paramètres nécessaires à la création de
   *          l'enregistrement de la capture de masse
   */
  @Before(METHOD_AJOUT_CAPTURE)
  public final void ajouterJobCaptureMasse(final TraitemetMasseParametres parametres) {

    if (parametres == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "parametres"));
    }

    if (parametres.getType() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "type"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.ECDE_URL))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "urlEcde"));
    }

    if (parametres.getUuid() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuid"));
    }
  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobSuppressionMasse}
   * 
   * @param parametres
   *          ensemble des paramètres nécessaires à la création de
   *          l'enregistrement de la capture de masse
   */
  @Before(METHOD_AJOUT_SUPPRESSION)
  public final void ajouterJobSuppressionMasse(
                                               final TraitemetMasseParametres parametres) {

    if (parametres == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "parametres"));
    }

    if (parametres.getType() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "type"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.REQ_LUCENE_SUPPRESSION))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "requete"));
    }

    if (parametres.getUuid() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuid"));
    }
  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobRestoreMasse}
   * 
   * @param parametres
   *          ensemble des paramètres nécessaires à la création de
   *          l'enregistrement de la capture de masse
   */
  @Before(METHOD_AJOUT_RESTORE)
  public final void ajouterJobRestoreMasse(final TraitemetMasseParametres parametres) {

    if (parametres == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "parametres"));
    }

    if (parametres.getType() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "type"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.ID_TRAITEMENT_A_RESTORER))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "idTraitement"));
    }

    if (parametres.getUuid() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuid"));
    }

  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#recupererJobs
   * 
   * @param listeUuid
   *          Liste des UUID nécessaires à la récupération des jobs
   */
  @Before(METHOD_RECUPERER_JOB)
  public final void recupererJobs(final List<UUID> listeUuid) {

    if (listeUuid == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "listeUuid"));
    }

  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#lancerJob(UUID)}
   * 
   * @param idJob
   *          doit être renseigné
   */
  @Before(METHOD_LANCER_JOB)
  public final void lancerJob(final UUID idJob) {

    if (idJob == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "idJob"));
    }
  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobTransfertMasse}
   * 
   * @param parametres
   *          paramètres nécessaires à la création de
   *          l'enregistrement de transfert de masse
   */
  @Before(METHOD_TRANSFERT_MASSE)
  public final void ajouterJobTransfertMasse(final TraitemetMasseParametres parametres) {

    if (parametres == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "parametres"));
    }

    if (parametres.getType() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "type"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.ECDE_URL))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "urlEcde"));
    }

    if (parametres.getUuid() == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuid"));
    }
  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobModificationMasse}
   * 
   * @param parametres
   *          paramètres nécessaires à la création de l'enregistrement de
   *          modification de masse
   */
  @Before(METHOD_MODIFICATION_MASSE)
  public final void ajouterJobModificationMasse(
                                                final TraitemetMasseParametres parametres) {

    if (parametres == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "parametres"));
    }

    if (parametres.getType() == null) {

      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "type"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.ECDE_URL))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "urlEcde"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.CODE_TRAITEMENT))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "codeTraitement"));
    }

    if (parametres.getUuid() == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuid"));
    }
  }

  /**
   * Validation des arguments d'entrée de la méthode
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobReprise}
   * 
   * @param parametres
   *          paramètres nécessaires à la création de
   *          l'enregistrement de transfert de masse
   */
  @Before(METHOD_REPRISE)
  public final void ajouterJobReprise(final TraitemetMasseParametres parametres) {

    if (parametres == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "parametres"));
    }

    if (parametres.getType() == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "type"));
    }

    if (StringUtils.isBlank(parametres.getJobParameters()
                                      .get(
                                           Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH))) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuidJobAReprendre"));
    }

    if (parametres.getUuid() == null) {
      throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                                                              "uuid"));
    }
  }

  /**
   * Validation du traitement de masse à reprendre
   * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#lancerReprise}
   * 
   * @param idJob
   *          l'uuid du job de reprise
   */
  @Before(METHOD_LANCER_REPRISE)
  public final void lancerReprise(final JobRequest jobReprise) {
    // Méthode de validation des traitements de reprise
    final String jobAReprendreParam = jobReprise.getJobParameters()
                                                .get(
                                                     Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH);

    if (jobAReprendreParam == null || jobAReprendreParam.isEmpty()) {
      LOG.warn("Impossible d'executer le traitement de reprise de ID={0}: "
          + "Le pramètre uuidJobAReprendre ne peut pas être null ", jobReprise.getIdJob().toString());
      throw new JobParameterTypeException(jobReprise,
                                          new Exception(
                                                        "L'uuidJobAReprendre est obligatoire pour le lancement du job de reprise"));
    }

  }

}
