package fr.urssaf.image.sae.vi.component;

import java.net.URI;
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

import fr.urssaf.image.sae.saml.util.ListUtils;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;

/**
 * La classe implémenté en AOP permet de vérifier les arguments des méthodes
 * dans {@link fr.urssaf.image.sae.vi.service.impl.WebServiceVIServiceImpl}<br>
 * 
 * 
 */
@Aspect
public class WebServiceVIServiceValidate {

   private static final String SERVICE_PACKAGE = "fr.urssaf.image.sae.vi.service.WebServiceVIService";

   private static final String CREATE_PACKAGE = "fr.urssaf.image.sae.vi.service.WebServiceVICreateService";

   private static final String WRITE_METHODE = "execution(public final * "
         + CREATE_PACKAGE + ".creerVIpourServiceWeb(..))";

   private static final String CHECK_METHODE = "execution(public final * "
         + SERVICE_PACKAGE + ".verifierVIdeServiceWeb(..))";

   private static final String ARG_EMPTY = "Le paramètre [${0}] n'est pas renseigné alors qu'il est obligatoire";

   private static final int INDEX_3 = 3;

   private static final int INDEX_4 = 4;

   private static final int INDEX_5 = 5;

   /**
    * Vérification des paramètres d'entrée de la méthode
    * {@link fr.urssaf.image.sae.vi.service.WebServiceVICreateService#creerVIpourServiceWeb}
    * <br>
    * <ul>
    * <li>pagm : doit avoir au moins un droit renseigné</li>
    * <li>issuer : doit être renseigné</li>
    * <li>keystore : doit être renseigné</li>
    * <li>alias : doit être renseigné</li>
    * <li>password: doit être renseigné</li>
    * </ul>
    * 
    * @param joinPoint
    *           point de jointure de la méthode
    */
   @Before(WRITE_METHODE)
   public final void creerVIpourServiceWeb(JoinPoint joinPoint) {

      // récupération des paramétres
      @SuppressWarnings("unchecked")
      List<String> pagm = (List<String>) joinPoint.getArgs()[0];
      String issuer = (String) joinPoint.getArgs()[1];
      KeyStore keystore = (KeyStore) joinPoint.getArgs()[INDEX_3];
      String alias = (String) joinPoint.getArgs()[INDEX_4];
      String password = (String) joinPoint.getArgs()[INDEX_5];

      // issuer not null
      notNullValidate(issuer, "issuer");
      // PAGM not null
      // on filtre les pagms
      if (CollectionUtils.isEmpty(ListUtils.filter(pagm))) {

         throw new IllegalArgumentException(
               "Il faut spécifier au moins un PAGM");
      }
      // keystore not null
      notNullValidate(keystore, "keystore");
      // alias
      notNullValidate(alias, "alias");
      // password
      notNullValidate(password, "password");

   }

   /**
    * Vérification des paramètres d'entrée de la méthode
    * {@link fr.urssaf.image.sae.vi.service.impl.WebServiceVIServiceImpl#verifierVIdeServiceWeb}
    * <br>
    * <ul>
    * <li>identification : doit être renseigné</li>
    * <li>serviceVise : doit être renseigné</li>
    * <li>idAppliClient : doit être renseigné</li>
    * <li>keystore : doit être renseigné</li>
    * <li>alias : doit être renseigné</li>
    * <li>password: doit être renseigné</li>
    * <li>crl : doit contenir au moins un CRL</li>
    * </ul>
    * 
    * @param joinPoint
    *           point de jointure de la méthode
    */
   @Before(CHECK_METHODE)
   public final void verifierVIdeServiceWeb(JoinPoint joinPoint) {

      // récupération des paramétres
      Element identification = (Element) joinPoint.getArgs()[0];
      URI serviceVise = (URI) joinPoint.getArgs()[1];
      VISignVerifParams signVerifParams = (VISignVerifParams) joinPoint
            .getArgs()[2];

      // identification not null
      notNullValidate(identification, "identification");

      // serviceVise not null
      notNullValidate(serviceVise, "serviceVise");

      // signVerifParams not null
      notNullValidate(signVerifParams, "signVerifParams");

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

      if (!StringUtils.isNotBlank(obj)) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", name);

         throw new IllegalArgumentException(StrSubstitutor.replace(ARG_EMPTY,
               args));
      }

   }

}
