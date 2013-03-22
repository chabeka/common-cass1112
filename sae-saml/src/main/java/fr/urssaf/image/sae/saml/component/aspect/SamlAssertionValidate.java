package fr.urssaf.image.sae.saml.component.aspect;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlSignatureVerifParams;
import fr.urssaf.image.sae.saml.util.ListUtils;

/**
 * La classe implémenté en AOP permet de vérifier les arguments de la méthode
 * <ul>
 * <li>
 * {@link fr.urssaf.image.sae.saml.service.SamlAssertionCreationService#genererAssertion}
 * </li>
 * <li>
 * {@link fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService#extraitDonnees}
 * </li>
 * <li>
 * {@link fr.urssaf.image.sae.saml.service.SamlAssertionVerificationService#verifierAssertion}
 * </li>
 * <ul>
 * <br>
 * Les règles sont les suivantes :
 * <ul>
 * <li>Les paramètres sont non renseignés quand ils sont à null</li>
 * <li>Les attributs sont non renseignées quand :
 * <ul>
 * <li>ils sont à null</li>
 * <li>chaine vide pour les caractères</li>
 * <li>liste vide pour les collections</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * 
 */
@Aspect
public class SamlAssertionValidate {

   private static final String SERVICE_PACKAGE = "fr.urssaf.image.sae.saml.service";

   private static final String WRITE_METHODE = "execution(public * "
         + SERVICE_PACKAGE
         + ".SamlAssertionCreationService.genererAssertion(..))";

   private static final String READ_METHODE = "execution(public * "
         + SERVICE_PACKAGE
         + ".SamlAssertionExtractionService.extraitDonnees(..))";

   private static final String CHECK_METHODE = "execution(public * "
         + SERVICE_PACKAGE
         + ".SamlAssertionVerificationService.verifierAssertion(..))";

   private static final String ARG_EMPTY = "Le paramètre [${0}] n'est pas renseigné alors qu'il est obligatoire";

   private static final String PARAM_EMPTY = "Il faut renseigner [${0}]";

   private static final int INDEX_3 = 3;

   /**
    * méthode appelée avant l'appel de 'genererAssertion'<br>
    * <br>
    * paramètres analysés :
    * <ul>
    * <li>
    * {@link SamlAssertionParams}</li>
    * <li>
    * {@link KeyStore}</li>
    * </ul>
    * si l'un des paramètres n'est pas vérifié alors une exception de type
    * {@link IllegalArgumentException}<br>
    * Les messages de l'exception levée sont les suivants:
    * <ul>
    * <li>paramètre non renseigné :"Le paramètre [<i>&lt;nom du paramètre></i>] n'est pas renseigné alors qu'il est obligatoire"
    * </li>
    * <li>attribut non renseigné :
    * "Il faut renseigner [<i>&lt;nom de l'attribut></i>]"</li>
    * <li>PAGM non renseigné : "Il faut spécifier au moins un PAGM"</li>
    * </ul>
    * <br>
    * 
    * @param joinPoint
    *           jointure de la méthode 'genererAssertion'
    * 
    */
   @Before(WRITE_METHODE)
   public final void assertionCreate(JoinPoint joinPoint) {

      // récupération des paramétres de la méthode genererAssertion
      SamlAssertionParams assertionParams = (SamlAssertionParams) joinPoint
            .getArgs()[0];
      KeyStore keyStore = (KeyStore) joinPoint.getArgs()[1];
      String alias = (String) joinPoint.getArgs()[2];
      String password = (String) joinPoint.getArgs()[INDEX_3];

      // assertionParams not null
      notNullValidate(assertionParams, "assertionParams", ARG_EMPTY);
      // keystore not null
      notNullValidate(keyStore, "keyStore", ARG_EMPTY);
      // alias not null
      notNullValidate(alias, "alias", ARG_EMPTY);
      // password not null
      notNullValidate(password, "password", ARG_EMPTY);

      // commonsParams not null
      notNullValidate(assertionParams.getCommonsParams(),
            "assertionParams.commonsParams", ARG_EMPTY);

      // issuer not null
      notNullValidate(assertionParams.getCommonsParams().getIssuer(),
            "assertionParams.commonsParams.issuer", PARAM_EMPTY);

      // notOnOrAfter not null
      notNullValidate(assertionParams.getCommonsParams().getNotOnOrAfter(),
            "assertionParams.commonsParams.notOnOrAfter", PARAM_EMPTY);

      // notOnBefore not null
      notNullValidate(assertionParams.getCommonsParams().getNotOnBefore(),
            "assertionParams.commonsParams.notOnBefore", PARAM_EMPTY);

      // audience not null
      notNullValidate(assertionParams.getCommonsParams().getAudience(),
            "assertionParams.commonsParams.audience", PARAM_EMPTY);

      // PAGM not null
      // on filtre les pagms
      List<String> pagm = ListUtils.filter(assertionParams.getCommonsParams()
            .getPagm());

      if (CollectionUtils.isEmpty(pagm)) {

         throw new IllegalArgumentException(
               "Il faut spécifier au moins un PAGM");
      }

      // subjectFormat2 not null
      notNullValidate(assertionParams.getSubjectFormat2(),
            "assertionParams.subjectFormat2", PARAM_EMPTY);

      // subjectId2 not null
      notNullValidate(assertionParams.getSubjectId2(),
            "assertionParams.subjectId2", PARAM_EMPTY);

      // methodAuthn2 not null
      notNullValidate(assertionParams.getMethodAuthn2(),
            "assertionParams.methodAuthn2", PARAM_EMPTY);

      // recipient not null
      notNullValidate(assertionParams.getRecipient(),
            "assertionParams.recipient", PARAM_EMPTY);

   }

   /**
    * méthode appelée avant l'appel de 'extraitDonnees'<br>
    * <br>
    * l'unique paramètre 'assertionSaml' est vérifié. la méthode lève
    * {@link IllegalArgumentException} si le paramètre n'est pas renseigné<br>
    * message de l'exception non renseigné :"Le paramètre [<i>&lt;nom du paramètre></i>] n'est pas renseigné alors qu'il est obligatoire"
    * 
    * 
    * @param joinPoint
    *           jointure de la méthode 'extraitDonnees'
    * 
    */
   @Before(READ_METHODE)
   public final void assertionExtraction(JoinPoint joinPoint) {

      // récupération des paramétres de la méthode extraitDonnnees
      Element assertionSaml = (Element) joinPoint.getArgs()[0];

      // assertionSaml not null
      notNullValidate(assertionSaml, "assertionSaml", ARG_EMPTY);
   }

   /**
    * méthode appelée avant l'appel de 'verifierAssertion'<br>
    * <br>
    * les différents paramètres doivent être renseignés. la méthode lève
    * {@link IllegalArgumentException} si le paramètre n'est pas renseigné<br>
    * message de l'exception non renseigné :"Le paramètre [<i>&lt;nom du paramètre></i>] n'est pas renseigné alors qu'il est obligatoire"
    * 
    * 
    * @param joinPoint
    *           jointure de la méthode 'verifierAssertion'
    */
   @Before(CHECK_METHODE)
   public final void assertionVerification(JoinPoint joinPoint) {

      // récupération des paramétres de la méthode verifierAssertion
      Element assertionSaml = (Element) joinPoint.getArgs()[0];
      SamlSignatureVerifParams signVerifParams = (SamlSignatureVerifParams) joinPoint.getArgs()[1];

      // assertionSaml not null
      notNullValidate(assertionSaml, "assertionSaml", ARG_EMPTY);
      // keystore not null
      notNullValidate(signVerifParams, "signVerifParams", ARG_EMPTY);

   }

   private void notNullValidate(Object obj, String name, String message) {

      if (obj == null) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", name);

         throw new IllegalArgumentException(StrSubstitutor.replace(message,
               args));
      }

   }

   private void notNullValidate(String obj, String name, String message) {

      if (!StringUtils.isNotBlank(obj)) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", name);

         throw new IllegalArgumentException(StrSubstitutor.replace(message,
               args));
      }

   }

}