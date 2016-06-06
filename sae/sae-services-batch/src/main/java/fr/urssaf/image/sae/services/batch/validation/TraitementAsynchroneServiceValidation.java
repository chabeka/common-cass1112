package fr.urssaf.image.sae.services.batch.validation;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService}.<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class TraitementAsynchroneServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.services.batch.TraitementAsynchroneService.";

   private static final String METHOD_AJOUT_CAPTURE = "execution(void " + CLASS
         + "ajouterJobCaptureMasse(*))" + "&& args(parametres)";

   private static final String METHOD_AJOUT_SUPPRESSION = "execution(void "
         + CLASS + "ajouterJobSuppressionMasse(*))" + "&& args(parametres)";

   private static final String METHOD_AJOUT_RESTORE = "execution(void " + CLASS
         + "ajouterJobRestoreMasse(*))" + "&& args(parametres)";

   private static final String METHOD_RECUPERER_JOB = "execution(List<fr.urssaf.image.sae.pile.travaux.model.JobRequest> "
         + CLASS + "recupererJobs(*))" + "&& args(listeUuid)";

   private static final String METHOD_LANCER_JOB = "execution(void " + CLASS
         + "lancerJob(*))" + "&& args(idJob)";

   private static final String ARG_EMPTY = "L''argument ''{0}'' doit être renseigné.";

   /**
    * Validation des arguments d'entrée de la méthode
    * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#ajouterJobCaptureMasse}
    * 
    * @param parametres
    *           ensemble des paramètres nécessaires à la création de
    *           l'enregistrement de la capture de masse
    */
   @Before(METHOD_AJOUT_CAPTURE)
   public final void ajouterJobCaptureMasse(TraitemetMasseParametres parametres) {

      if (parametres == null) {
         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "parametres"));
      }

      if (parametres.getType() == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "type"));
      }

      if (StringUtils.isBlank(parametres.getJobParameters().get(
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
    *           ensemble des paramètres nécessaires à la création de
    *           l'enregistrement de la capture de masse
    */
   @Before(METHOD_AJOUT_SUPPRESSION)
   public final void ajouterJobSuppressionMasse(
         TraitemetMasseParametres parametres) {

      if (parametres == null) {
         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "parametres"));
      }

      if (parametres.getType() == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "type"));
      }

      if (StringUtils.isBlank(parametres.getJobParameters().get(
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
    *           ensemble des paramètres nécessaires à la création de
    *           l'enregistrement de la capture de masse
    */
   @Before(METHOD_AJOUT_RESTORE)
   public final void ajouterJobRestoreMasse(TraitemetMasseParametres parametres) {

      if (parametres == null) {
         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "parametres"));
      }

      if (parametres.getType() == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "type"));
      }

      if (StringUtils.isBlank(parametres.getJobParameters().get(
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
    *           Liste des UUID nécessaires à la récupération des jobs
    */
   @Before(METHOD_RECUPERER_JOB)
   public final void recupererJobs(List<UUID> listeUuid) {

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
    *           doit être renseigné
    */
   @Before(METHOD_LANCER_JOB)
   public final void lancerJob(UUID idJob) {

      if (idJob == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "idJob"));
      }
   }

}
