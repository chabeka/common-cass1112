package fr.urssaf.image.sae.services.transfert.validation;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe SAETransfertServiceValidation
 * 
 * Classe de validation des arguments en entrée des implementations du service
 * fr.urssaf.image.sae.services.transfert.SAETransfertService
 * 
 */
@Aspect
public class SAETransfertServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae." +
   		"services.transfert.SAETransfertService.";
   		
   private static final String PARAM = "execution(void "
         + CLASS + "transfertDoc(*))" + "&& args(uuid)";

   /**
    * Vérifie les paramètres d'entrée de la méthode transfert
    * 
    * @param uuid
    *           identifiant unique du document à transferer
    */
   @Before(PARAM)
   public final void transfert(UUID uuid) {
      Validate.notNull(uuid, ResourceMessagesUtils.loadMessage(
            "argument.required", "'identifiant de l'archive'"));
   }
}
