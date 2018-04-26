/**
 * 
 */
package fr.urssaf.image.sae.droit.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * SaePagmpServiceValidation
 * 
 */
@Aspect
public class SaePagmpServiceValidation {

   /**
    * 
    */
   private static final String ARG_REQUIRED = "argument.required";
   private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaePagmpService.createPagmp(*))"
         + "&& args(pagmp)";

   /**
    * Méthode de validation de la méthode SaePagmpService#createPagmp(Pagmp)
    * 
    * @param pagmp
    *           pagmp à créer
    */
   @Before(CREATE_METHOD)
   public final void checkCreate(Pagmp pagmp) {

      if (pagmp == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "pagmp"));
      }

      if (StringUtils.isEmpty(pagmp.getCode())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "code pagmp"));
      }

      if (StringUtils.isEmpty(pagmp.getDescription())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "description pagmp"));
      }

      if (StringUtils.isEmpty(pagmp.getPrmd())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "prmd pagmp"));
      }

   }

}
