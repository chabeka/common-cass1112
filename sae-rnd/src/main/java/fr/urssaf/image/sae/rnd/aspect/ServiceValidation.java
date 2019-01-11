package fr.urssaf.image.sae.rnd.aspect;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;

/**
 * Classe de validation
 * 
 * 
 */
@Aspect
public class ServiceValidation {

   private static final String AJOUTER_CORRES = "execution(void fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport.ajouterCorrespondance(*,*))"
         + "&& args(correspondance, clock)";

   private static final String AJOUTER_RND = "execution(void fr.urssaf.image.sae.rnd.dao.support.RndSupport.ajouterRnd(*, *))"
         + "&& args(typeDoc, clock)";

   private static final String UPDATE_VERSION = "execution(void fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport.updateVersionRnd(*))"
         + "&& args(versionRnd)";

   private static final String RND_SERVICE = "execution(* fr.urssaf.image.sae.rnd.service.RndService.get*(*))"
         + "&& args(codeRnd)";

   /**
    * Vérification des arguments des méthodes get de RndService
    * 
    * @param codeRnd
    *           le code RND
    */
   @Before(RND_SERVICE)
   public final void validationRndService(String codeRnd) {

      if (StringUtils.isBlank(codeRnd)) {
         throw new IllegalArgumentException(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide");
      }

   }

   /**
    * Vérification des arguments des méthodes get de updateVersionRnd
    * 
    * @param versionRnd
    *           la version RND
    */
   @Before(UPDATE_VERSION)
   public final void validationArgUpdateVersionRnd(VersionRnd versionRnd) {
      if (versionRnd == null) {
         throw new IllegalArgumentException(
               "l'argument versionRnd n'est pas valide");
      } else {
         if (versionRnd.getDateMiseAJour() == null) {
            throw new IllegalArgumentException(
                  "l'argument versionRnd n'est pas valide : date de mise à jour null");
         }
         if (StringUtils.isBlank(versionRnd.getVersionEnCours())) {
            throw new IllegalArgumentException(
                  "l'argument versionRnd n'est pas valide : numéro version null ou vide");
         }
      }
   }

   /**
    * Validation des arguments de la méthode d'ajout d'un type de document
    * 
    * @param typeDoc
    *           le type de doc à ajouter
    * @param clock
    *           Horloge de la création
    */
   @Before(AJOUTER_RND)
   public final void validationArgAjouterRnd(TypeDocument typeDoc, long clock) {

      if (clock == 0) {
         throw new IllegalArgumentException("l'argument clock n'est pas valide");
      }

      if (typeDoc == null) {
         throw new IllegalArgumentException(
               "l'argument typeDoc n'est pas valide");
      } else {
         // Le code RND ne peut pas être null
         if (StringUtils.isBlank(typeDoc.getCode())) {
            throw new IllegalArgumentException(
                  "l'argument typeDoc n'est pas valide : code Rnd null ou vide");
         }

         // Le code activité peut être null

         // Le code fonction peut être null uniquement pour les types de
         // document temporaire
         if (!TypeCode.TEMPORAIRE.equals(typeDoc.getType())
               && StringUtils.isBlank(typeDoc.getCodeFonction())) {
            throw new IllegalArgumentException(
                  "l'argument typeDoc n'est pas valide : codeFonction null ou vide alors que le type de document n'est pas TEMPORAIRE");
         }

         // La durée de conservation doit être renseignée
         if (typeDoc.getDureeConservation() <= 0) {
            throw new IllegalArgumentException(
                  "l'argument typeDoc n'est pas valide : dureeConservation négatif ou nul");
         }
      }
   }

   /**
    * Vérification des arguments de la méthode ajouterCorrespondance
    * 
    * @param correspondance
    *           la correspondance
    * @param clock
    *           Horloge de la création
    */
   @Before(AJOUTER_CORRES)
   public final void validationArgAjouterCorrespondance(
         Correspondance correspondance, long clock) {

      if (clock == 0) {
         throw new IllegalArgumentException("l'argument clock n'est pas valide");
      }

      if (correspondance == null) {
         throw new IllegalArgumentException(
               "l'argument correspondance n'est pas valide");
      } else {
         if (StringUtils.isBlank(correspondance.getCodeDefinitif())) {
            throw new IllegalArgumentException(
                  "l'argument correspondance n'est pas valide : code définitif null ou vide");
         }

         if (StringUtils.isBlank(correspondance.getCodeTemporaire())) {
            throw new IllegalArgumentException(
                  "l'argument correspondance n'est pas valide : code temporaire null ou vide");
         }
      }
   }

}
