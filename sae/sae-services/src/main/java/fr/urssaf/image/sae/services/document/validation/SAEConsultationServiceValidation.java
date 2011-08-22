package fr.urssaf.image.sae.services.document.validation;

import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.document.SAEConsultationService}.<br>
 * * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class SAEConsultationServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.services.document.SAEConsultationService.";

   private static final String METHOD = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument "
         + CLASS + "consultation(*))" + "&& args(idArchive)";

   @SuppressWarnings("PMD.LongVariable")
   public static final String IGC_CONFIG_NOTEXIST = "Le fichier de configuration IGC est introuvable (${0})";

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.services.document.SAEConsultationService#consultation(UUID)}
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
               "argument.required", "idArchive"));
      }

   }

}
