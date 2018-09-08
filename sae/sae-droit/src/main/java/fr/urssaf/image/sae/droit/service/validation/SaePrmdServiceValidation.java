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

   /**
    * 
    */
   private static final String ARG_REQUIRED = "argument.required";
   private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaePrmdService.createPrmd(*))"
         + "&& args(prmd)";
   private static final String CHECK_METHOD = "execution(boolean fr.urssaf.image.sae.droit.service.SaePrmdService.prmdExists(*))"
      + "&& args(code)";

   /**
    * Méthode de validation de la méthode SaePrmdService#createPrmd(Prmd)
    * 
    * @param prmd
    *           prmd à créer
    */
   @Before(CREATE_METHOD)
   public final void checkCreate(Prmd prmd) {

      if (prmd == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "prmd"));
      }

      if (StringUtils.isEmpty(prmd.getCode())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "code prmd"));
      }

      if (StringUtils.isEmpty(prmd.getDescription())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "description prmd"));
      }

   }
   
   /**
    * Méthode de validation de la méthode SaePrmdService#prmdExists(idClient)
    * 
    * @param code code PRMD
    */
   @Before(CHECK_METHOD)
   public final void checkCreate(String code) {
      if (StringUtils.isEmpty(code)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "code"));
      }
   }

}
