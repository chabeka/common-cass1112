package fr.urssaf.image.sae.vi.service;

import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService;
import fr.urssaf.image.sae.saml.util.ConverterUtils;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;

/**
 * Classe de lecture et d'écriture du VI pour les web services<br>
 * <br>
 * Le VI est un jeton SAML 2.0 conforme aux <a
 * href="http://saml.xml.org/saml-specifications#samlv20"/>spécifications de
 * OASIS</a><br>
 * <br>
 * L'implémentation s'appuie sur les classes
 * <ul>
 * <li>{@link SamlAssertionCreationService}</li>
 * <li>{@link SamlAssertionExtractionService}</li>
 * </ul>
 * <br>
 * <br>
 * Les paramètres d'entrées de chaque méthode sont vérifiés par AOP par la
 * classe {@link fr.urssaf.image.sae.vi.component.WebServiceVIServiceValidate}<br>
 * 
 */
public class WebServiceVIService {

   private final SamlAssertionCreationService createService;
   private final SamlAssertionExtractionService extractService;

   private final WebServiceVIValidateService validateService;

   /**
    * instanciation de {@link SamlAssertionCreationService}<br>
    * instanciation de {@link SamlAssertionExtractionService}<br>
    * instanciation de {@link WebServiceVIValidateService}<br>
    * 
    */
   public WebServiceVIService() {
      createService = new SamlAssertionCreationService();
      extractService = new SamlAssertionExtractionService();

      validateService = new WebServiceVIValidateService();

   }

   /**
    * Génération d'un Vecteur d'Identification (VI) pour s'authentifier auprès
    * d'un service web du SAE. <br>
    * Le VI prend la forme d'une assertion SAML 2.0 signée électroniquement. <br>
    * La récupération des droits applicatifs doit être faite en amont.<br>
    * <br>
    * Paramètres obligatoires
    * <ul>
    * <li>pagm (au moins un droit)</li>
    * <li>issuer</li>
    * <li>keystore</li>
    * <li>alias</li>
    * <li>password</li>
    * </ul>
    * 
    * @param pagm
    *           Liste des droits applicatifs
    * @param issuer
    *           L'identifiant de l'application cliente
    * @param idUtilisateur
    *           L'identifiant de l'utilisateur
    * @param keystore
    *           Le certificat applicatif de l'application cliente, sa clé
    *           privée, et la chaîne de certification associée, pour la
    *           signature électronique du VI
    * @param alias
    *           L'alias de la clé privée du KeyStore
    * @param password
    *           mot du de la clé privée
    * @return Le Vecteur d'Identification
    */
   public final Element creerVIpourServiceWeb(List<String> pagm, String issuer,
         String idUtilisateur, KeyStore keystore, String alias, String password) {

      Date systemDate = new Date();

      SamlAssertionParams assertionParams = new SamlAssertionParams();
      SamlCommonsParams commonsParams = new SamlCommonsParams();

      assertionParams.setCommonsParams(commonsParams);

      commonsParams.setIssuer(issuer);
      commonsParams.setNotOnOrAfter(DateUtils.addHours(systemDate, 1));
      commonsParams.setNotOnBefore(DateUtils.addHours(systemDate, -1));
      commonsParams.setAudience(ConverterUtils.uri("http://sae.urssaf.fr"));
      commonsParams.setPagm(pagm);

      String subjectId2 = StringUtils.isNotBlank(idUtilisateur) ? idUtilisateur
            : "NON_RENSEIGNE";
      assertionParams.setSubjectId2(subjectId2);
      assertionParams.setSubjectFormat2(ConverterUtils
            .uri("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"));
      assertionParams.setMethodAuthn2(VIConfiguration.METHOD_AUTH2);
      assertionParams.setRecipient(ConverterUtils.uri("urn:URSSAF"));

      return createService.genererAssertion(assertionParams, keystore, alias,
            password);

   }

   /**
    * Vérification d'un Vecteur d'Identification (VI) généré pour s'authentifier
    * auprès d'un service web du SAE<br>
    * <br>
    * Les vérification sont faites dans
    * <ul>
    * <li>
    * {@link WebServiceVIValidateService#validate(SamlAssertionData, URI, String, Date)}
    * </li>
    * <li>
    * {@link WebServiceVIValidateService#validate(Element, KeyStore, String, String, List)}
    * </li>
    * </ul>
    * 
    * @param identification
    *           Le Vecteur d'Identification à vérifier
    * @param serviceVise
    *           URI décrivant le service visé
    * @param idAppliClient
    *           Identifiant de l'application consommatrice du service, récupéré
    *           à partir du certificat client SSL (ignoré pour l'instant)
    * @param signVerifParams
    *           Les éléments permettant de vérifier la signature électronique du VI
    * @return Des valeurs extraits du VI qui peuvent être exploités pour mettre
    *         en place un contexte de sécurité basé sur l’authentification,
    *         et/ou pour de la traçabilité
    * @throws VIVerificationException
    *            Les informations extraites du VI sont invalides
    */
   public final VIContenuExtrait verifierVIdeServiceWeb(
         Element identification,
         URI serviceVise, 
         String idAppliClient, 
         VISignVerifParams signVerifParams)
      throws VIVerificationException {

      // vérification du jeton SAML
      validateService.validate(identification, signVerifParams);

      // extraction du jeton SAML
      SamlAssertionData data = extractService.extraitDonnees(identification);

      // vérification supplémentaires sur le jeton SAML
      validateService.validate(data, serviceVise, idAppliClient, new Date());

      // instanciation de la valeur retour
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setPagm(data.getAssertionParams().getCommonsParams().getPagm());
      extrait.setIdUtilisateur(data.getAssertionParams().getSubjectId2());

      // l'extraction du CN vient du code
      // http://stackoverflow.com/questions/2914521/how-to-extract-cn-from-x509certificate-in-java
      try {
         X509Principal principal = PrincipalUtil.getSubjectX509Principal(data
               .getClePublique());
         extrait.setCodeAppli((String) principal.getValues(X509Name.CN).get(0));
      } catch (CertificateEncodingException e) {
         throw new IllegalStateException(e);
      }

      return extrait;
   }

}
