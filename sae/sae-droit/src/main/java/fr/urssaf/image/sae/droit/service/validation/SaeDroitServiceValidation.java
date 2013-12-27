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
import fr.urssaf.image.sae.droit.model.SaePagm;
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

   private static final String LOAD_METHOD_2 = "execution(fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat fr.urssaf.image.sae.droit.service.SaeDroitService.loadSaeDroits(*,*))"
         + "&& args(idClient, pagms)";

   private static final String CREATE_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaeDroitService.createContratService(*,*))"
         + "&& args(contrat, listeSaePagms)";

   private static final String CHECK_METHOD = "execution(boolean fr.urssaf.image.sae.droit.service.SaeDroitService.contratServiceExists(*))"
         + "&& args(idClient)";

   private static final String GET_METHOD = "execution(fr.urssaf.image.sae.droit.dao.model.ServiceContract fr.urssaf.image.sae.droit.service.SaeDroitService.getServiceContract(*))"
         + "&& args(idClient)";

   private static final String ADD_PAGM_METHOD = "execution(void fr.urssaf.image.sae.droit.service.SaeDroitService.addPagmContratService(*,*))"
         + "&& args(idClient, pagm)";;

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
    * Validation de la méthode loadSaeDroits
    * 
    * @param idClient
    *           identifiant du contrat de service
    * @param pagms
    *           liste des pagms
    */
   @Before(LOAD_METHOD_2)
   public final void checkLoad2(final String idClient, List<String> pagms) {

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
    * @param listeSaePagms
    *           liste des pagms
    */
   @Before(CREATE_METHOD)
   public final void checkCreate(ServiceContract contrat,
         List<SaePagm> listeSaePagms) {

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

      if (CollectionUtils.isEmpty(listeSaePagms)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "liste des pagms"));
      }

      if (StringUtils.isEmpty(contrat.getIdPki())
            && CollectionUtils.isEmpty(contrat.getListPki())) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "le nom de la PKI ou la liste des PKI"));
      }

      if (StringUtils.isEmpty(contrat.getIdCertifClient())
            && CollectionUtils.isEmpty(contrat.getListCertifsClient())
            && contrat.isVerifNommage()) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED,
               "le certificat client ou la liste des certificats clients"));
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

   /**
    * Validation de la méthode getServiceContract(idClient)
    * 
    * @param idClient
    *           contrat de service
    */
   @Before(GET_METHOD)
   public final void checkGetServiceContract(String idClient) {

      if (StringUtils.isEmpty(idClient)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "le code client"));
      }
   }

   /**
    * Validation de la méthode loadSaeDroits(String, List)
    * 
    * @param idClient
    *           identifiant client
    * @param pagm
    *           le pagm
    */
   @Before(ADD_PAGM_METHOD)
   public final void checkAddPagm(final String idClient, Pagm pagm) {

      if (StringUtils.isEmpty(idClient)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "identifiant client"));
      }

      if (pagm == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARG_REQUIRED, "pagm"));
      }

   }

}
