package fr.urssaf.image.sae.services.batch.validation;

import java.text.MessageFormat;
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

   private static final String METHOD_1 = "execution(void " + CLASS
         + "ajouterJob(*))" + "&& args(parametres)";

   private static final String METHOD_2 = "execution(void " + CLASS
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
   @Before(METHOD_1)
   public final void ajouterJob(TraitemetMasseParametres parametres) {

      if (parametres == null) {
         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "parametres"));
      }

      if (parametres.getType() == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "type"));
      }

      String type = parametres.getType();

      if (Constantes.TYPES_JOB.capture_masse.name().equals(type)) {
         if (StringUtils.isBlank(parametres.getJobParameters().get(
               Constantes.ECDE_URL))) {
            throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                  "urlEcde"));
         }
      } else if (Constantes.TYPES_JOB.restore_masse.name().equals(type)) {
         if (StringUtils.isBlank(parametres.getJobParameters().get(
               Constantes.UUID_TRAITEMENT))) {
            throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                  "uuid"));
         }
      } else if (Constantes.TYPES_JOB.suppression_masse.name().equals(type)) {
         if (StringUtils.isBlank(parametres.getJobParameters().get(
               Constantes.REQUETE))) {
            throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
                  "requete"));
         }
      }

      if (parametres.getUuid() == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "uuid"));
      }

   }

   /**
    * Validation des arguments d'entrée de la méthode
    * {@link fr.urssaf.image.sae.services.batch.TraitementAsynchroneService#lancerJob(UUID)}
    * 
    * @param idJob
    *           doit être renseigné
    */
   @Before(METHOD_2)
   public final void lancerJob(UUID idJob) {

      if (idJob == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "idJob"));
      }
   }

}
