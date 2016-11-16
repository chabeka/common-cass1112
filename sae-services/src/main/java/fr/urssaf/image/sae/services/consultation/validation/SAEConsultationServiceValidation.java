package fr.urssaf.image.sae.services.consultation.validation;

import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * fr.urssaf.image.sae.services.consultation.SAEConsultationService.<br>
 * * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class SAEConsultationServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.services.consultation.SAEConsultationService.";

   private static final String METHOD = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument "
         + CLASS + "consultation(*))" + "&& args(idArchive)";

   private static final String MAIN_METHOD = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument "
         + CLASS + "consultation(*))" + "&& args(consultParams)";
   
   private static final String METHOD_CONSULT_AFFICH = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument "
      + CLASS + "consultationAffichable(*))" + "&& args(consultParams)";
   
   private static final String ARGUMENT_REQUIRED = "argument.required";

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.services.consultation.SAEConsultationService#consultation(UUID)}
    * <br>
    * <ul>
    * <li><code>idArchive</code> doit être renseigné</li>
    * </ul>
    * 
    * @param idArchive
    *           identifiant de l'archive à consulter
    */
   @Before(METHOD)
   public final void consultation(UUID idArchive) {

      if (idArchive == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "idArchive"));
      }

   }

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.services.consultation.SAEConsultationService#consultation(UUID)}
    * <br>
    * <ul>
    * <li><code>idArchive</code> doit être renseigné</li>
    * </ul>
    * 
    * @param consultParams
    *           Objet contenant l'ensemble des paramètres
    * 
    */
   @Before(MAIN_METHOD)
   public final void consultation(ConsultParams consultParams) {

      if (consultParams == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "consultParams"));
      }

      if (consultParams.getIdArchive() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "idArchive"));
      }

   }

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.services.consultation.SAEConsultationService#consultationAffichable(ConsultParams)}
    * <br>
    * <ul>
    * <li><code>idArchive</code> doit être renseigné</li>
    * </ul>
    * 
    * @param consultParams
    *           Objet contenant l'ensemble des paramètres
    * 
    */
   @Before(METHOD_CONSULT_AFFICH)
   public final void consultationAffichable(ConsultParams consultParams) {

      if (consultParams == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "consultParams"));
      }

      if (consultParams.getIdArchive() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "idArchive"));
      }

   }
}
