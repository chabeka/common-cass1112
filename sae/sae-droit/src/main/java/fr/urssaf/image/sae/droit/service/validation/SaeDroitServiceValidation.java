/**
 * 
 */
package fr.urssaf.image.sae.droit.service.validation;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de validation des arguments entrée des implémentations du service
 * SaeDroitServiceValidation
 * 
 */
@Aspect
public class SaeDroitServiceValidation {

   /**
    * 
    */
   private static final String ARG_REQUIRED = "argument.required";

   private static final String LOAD_METHOD = "execution(fr.urssaf.image.sae.droit.model.SaeDroits fr.urssaf.image.sae.droit.service.SaeDroitService.loadSaeDroits(*,*))"
         + "&& args(idClient, pagms)";

   private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaeDroitService.createContratService(*,*))"
         + "&& args(contrat, pagms)";

   private static final String CHECK_METHOD = "execution(boolean fr.urssaf.image.sae.droit.service.SaeDroitService.contratServiceExists(*))"
         + "&& args(idClient)";

   /**
    * Validation de la méthode loadSaeDroits(String, List)
    * 
    * @param idClient
    *           identifiant client
    * @param pagms
    *           liste des pagms
    */
   @Before(LOAD_METHOD)
   public final void checkLoad(final String idClient, List<String> pagms) {

      if (StringUtils.isEmpty(idClient)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "identifiant client"));
      }

      if (CollectionUtils.isEmpty(pagms)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "liste des pagms"));
      }

   }

   /**
    * Validation de la méthode
    * SaeDroitService#createContratService(ServiceContract, List)
    * 
    * @param contrat
    *           contrat de service
    * @param pagms
    *           liste des pagms
    */
   @Before(CREATE_METHOD)
   public final void checkCreate(ServiceContract contrat, List<Pagm> pagms) {

      if (contrat == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "contrat"));
      }

      if (StringUtils.isEmpty(contrat.getCodeClient())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "code client contrat"));
      }

      if (StringUtils.isEmpty(contrat.getDescription())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "description contrat"));
      }

      if (StringUtils.isEmpty(contrat.getLibelle())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "libellé contrat"));
      }

      if (contrat.getViDuree() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "durée contrat"));
      }

      if (CollectionUtils.isEmpty(pagms)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "liste des pagms"));
      }

   }

   /**
    * Validation de la méthode
    * SaeDroitService#createContratService(ServiceContract, List)
    * 
    * @param idClient
    *           contrat de service
    */
   @Before(CHECK_METHOD)
   public final void checkExists(String idClient) {

      if (StringUtils.isEmpty(idClient)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "le code client"));
      }
   }

}
