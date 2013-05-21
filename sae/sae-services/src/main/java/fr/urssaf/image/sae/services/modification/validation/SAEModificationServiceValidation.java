package fr.urssaf.image.sae.services.modification.validation;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe SAESearchServiceValidation
 * 
 * Classe de validation des arguments en entrée des implementations du service
 * SAESearchService
 * 
 */
@Aspect
public class SAEModificationServiceValidation {

   private static final String SAE_DELETE_CLASS = "fr.urssaf.image.sae.services.modification.SAEModificationService.";
   private static final String PARAM_DELETE = "execution(void "
         + SAE_DELETE_CLASS + "modification(*, *))" + "&& args(uuid, metas)";

   /**
    * Vérifie les paramètres d'entrée de la méthode suppression
    * 
    * @param uuid
    *           identifiant unique du document à supprimer
    */
   @Before(PARAM_DELETE)
   public final void delete(UUID uuid, List<UntypedMetadata> metas) {
      Validate.notNull(uuid, ResourceMessagesUtils.loadMessage(
            "argument.required", "'identifiant de l'archive'"));
      Validate.notEmpty(metas, ResourceMessagesUtils.loadMessage(
            "argument.required", "'les métadonnées'"));
   }
}
