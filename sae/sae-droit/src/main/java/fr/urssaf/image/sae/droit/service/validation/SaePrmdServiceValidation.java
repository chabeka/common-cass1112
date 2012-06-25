/**
 * 
 */
package fr.urssaf.image.sae.droit.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * SaePrmdServiceValidation
 * 
 */
@Aspect
public class SaePrmdServiceValidation {

   private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaePrmdService.createPrmd(*))"
         + "&& args(prmd)";

   @Before(CREATE_METHOD)
   public final void checkCreate(Prmd prmd) {

      if (prmd == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "prmd"));
      }

      if (StringUtils.isEmpty(prmd.getCode())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "code prmd"));
      }

      if (StringUtils.isEmpty(prmd.getDescription())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "description prmd"));
      }

      if (StringUtils.isEmpty(prmd.getLucene())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "lucène pagmp"));
      }

   }

}
