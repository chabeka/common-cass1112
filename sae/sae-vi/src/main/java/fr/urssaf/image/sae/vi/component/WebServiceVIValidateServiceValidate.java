package fr.urssaf.image.sae.vi.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;

/**
 * La classe implémenté en AOP permet de vérifier les arguments des méthodes
 * dans {@link fr.urssaf.image.sae.vi.service.impl.WebServiceVIServiceImpl}<br>
 * 
 * 
 */
@Aspect
public class WebServiceVIValidateServiceValidate {

   private static final String VALIDATE_PCK = "fr.urssaf.image.sae.vi.service.WebServiceVIValidateService";

   private static final String CERTIFICATS = "execution(public final void "
         + VALIDATE_PCK
         + ".validateCertificates(*,*)) && args(contract, result)";

   private static final String ARG_EMPTY = "Le paramètre [${0}] n'est pas renseigné alors qu'il est obligatoire";

   /**
    * Vérification des paramètres d'entrée de la méthode
    * {@link fr.urssaf.image.sae.vi.service.WebServiceVIValidateService#validateCertificates}
    * <br>
    * <ul>
    * <li>pagm : doit avoir au moins un droit renseigné</li>
    * <li>issuer : doit être renseigné</li>
    * <li>keystore : doit être renseigné</li>
    * <li>alias : doit être renseigné</li>
    * <li>password: doit être renseigné</li>
    * </ul>
    * 
    * @param contract
    *           le contrat de service
    * @param result
    *           certificat client utilisé pour signer le VI
    */
   @Before(CERTIFICATS)
   public final void validateCertificates(ServiceContract contract,
         SignatureVerificationResult result) {

      notNullValidate(contract, "contrat");

      if (StringUtils.isBlank(contract.getIdPki())
            && listIsEmpty(contract.getListPki())) {
         notNullValidate(contract.getIdPki(), "identifiant de la PKI");
      }

      if (contract.isVerifNommage()
            && StringUtils.isEmpty(contract.getIdCertifClient())
            && listIsEmpty(contract.getListCertifsClient())) {
         notNullValidate(contract.getIdCertifClient(),
               "identifiant du certificat client");
      }

      notNullValidate(result, "certificats entrés en jeu");
      notNullValidate(result.getPki(), "AC racine");
      notNullValidate(result.getCertificat(), "certificat client");

   }

   private void notNullValidate(Object obj, String name) {

      if (obj == null) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", name);

         throw new IllegalArgumentException(StrSubstitutor.replace(ARG_EMPTY,
               args));
      }

   }

   private void notNullValidate(String obj, String name) {

      if (StringUtils.isBlank(obj)) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", name);

         throw new IllegalArgumentException(StrSubstitutor.replace(ARG_EMPTY,
               args));
      }

   }

   private boolean listIsEmpty(List<String> liste) {

      return (CollectionUtils.isEmpty(liste) || (StringUtils.isBlank(liste
            .get(0))));

   }

}
