package fr.urssaf.image.sae.vi.service.impl;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.exception.signature.SamlSignatureException;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;
import fr.urssaf.image.sae.saml.params.SamlSignatureVerifParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionVerificationService;
import fr.urssaf.image.sae.vi.exception.VIAppliClientException;
import fr.urssaf.image.sae.vi.exception.VICertificatException;
import fr.urssaf.image.sae.vi.exception.VIFormatTechniqueException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VINivAuthException;
import fr.urssaf.image.sae.vi.exception.VIPagmIncorrectException;
import fr.urssaf.image.sae.vi.exception.VIServiceIncorrectException;
import fr.urssaf.image.sae.vi.exception.VISignatureException;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.service.VIConfiguration;
import fr.urssaf.image.sae.vi.service.WebServiceVIValidateService;

/**
 * Classe de validation d'une assertion SAML 2.0
 * 
 * 
 */
@Component
public class WebServiceVIValidateServiceImpl implements
      WebServiceVIValidateService {

   private static final String ERREUR_PKI = "Le certificat utilisé pour signer le VI n'est pas "
         + "issu de l'IGC définie dans le Contrat de Service";
   private static final String ERREUR_CERT = "Le nom du certificat utilisé pour signer le VI ne "
         + "correspond pas au nom défini dans le Contrat de Service";
   private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(WebServiceVIValidateServiceImpl.class);

   private final SamlAssertionVerificationService checkService = new SamlAssertionVerificationService();

   /**
    * {@inheritDoc}
    */
   public final SignatureVerificationResult validate(Element identification,
         VISignVerifParams signVerifParams) throws VIFormatTechniqueException,
         VISignatureException {

      try {

         SamlSignatureVerifParams samlVerifSignPrms = convertSignParams(signVerifParams);

         return checkService.verifierAssertion(identification,
               samlVerifSignPrms);

      } catch (SamlFormatException e) {
         throw new VIFormatTechniqueException(e);
      } catch (SamlSignatureException e) {
         throw new VISignatureException(e);
      }

   }

   protected final SamlSignatureVerifParams convertSignParams(
         VISignVerifParams viSignParams) {

      SamlSignatureVerifParams samlSignParams = new SamlSignatureVerifParams();

      samlSignParams.setCertifsACRacine(viSignParams.getCertifsACRacine());
      samlSignParams.setCrls(viSignParams.getCrls());
      samlSignParams.setPatternsIssuer(viSignParams.getPatternsIssuer());

      return samlSignParams;

   }

   /**
    * {@inheritDoc}
    */
   public final void validate(SamlAssertionData data, URI serviceVise,
         String idAppliClient, Date systemDate) throws VIInvalideException,
         VIAppliClientException, VINivAuthException, VIPagmIncorrectException,
         VIServiceIncorrectException {

      String prefixeTrc = "validate()";

      // la date systeme doit être postérieure à NotOnBefore
      Date notOnBefore = data.getAssertionParams().getCommonsParams()
            .getNotOnBefore();

      if (systemDate.compareTo(notOnBefore) < 0) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", DateFormatUtils.format(notOnBefore, DATE_PATTERN));
         args.put("1", DateFormatUtils.format(systemDate, DATE_PATTERN));

         String message = "L'assertion n'est pas encore valable: elle ne sera active qu'à partir de ${0} alors que nous sommes le ${1}";

         throw new VIInvalideException(StrSubstitutor.replace(message, args));
      }

      // la date systeme doit être strictement antérieure à NotOnOrAfter
      Date notOnOrAfter = data.getAssertionParams().getCommonsParams()
            .getNotOnOrAfter();
      if (systemDate.compareTo(notOnOrAfter) >= 0) {

         Map<String, String> args = new HashMap<String, String>();
         args.put("0", DateFormatUtils.format(notOnOrAfter, DATE_PATTERN));
         args.put("1", DateFormatUtils.format(systemDate, DATE_PATTERN));

         String message = "L'assertion a expirée : elle n'était valable que jusqu’au ${0}, hors nous sommes le ${1}";

         throw new VIInvalideException(StrSubstitutor.replace(message, args));

      }

      // serviceVise doit être égal à Audience

      if (!serviceVise.equals(data.getAssertionParams().getCommonsParams()
            .getAudience())) {

         throw new VIServiceIncorrectException(serviceVise, data
               .getAssertionParams().getCommonsParams().getAudience());
      }

      // MethodAuth2 doit être égal à
      // 'urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified'
      if (!VIConfiguration.METHOD_AUTH2.equals(data.getAssertionParams()
            .getMethodAuthn2())) {

         throw new VINivAuthException(data.getAssertionParams()
               .getMethodAuthn2());
      }

      // Au moins 1 PAGM
      LOGGER.debug(
            "{} -Vérification qu'au moins un PAGM est présent dans le VI",
            prefixeTrc);
      if (CollectionUtils.isEmpty(data.getAssertionParams().getCommonsParams()
            .getPagm())) {
         throw new VIPagmIncorrectException(
               "Aucun PAGM n'est spécifié dans le VI, or il est obligatoire d'en spécifier au moins un");
      }

      // FIXME - A décommenter si une application cliente a un et un seul CS
      // idAppliClient doit être égal à Issuer
      // if (!idAppliClient.equals(data.getAssertionParams().getCommonsParams()
      // .getIssuer())) {
      //
      // throw new VIAppliClientException(data.getAssertionParams()
      // .getCommonsParams().getIssuer());
      // }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void validateCertificates(ServiceContract contract,
         SignatureVerificationResult result) throws VICertificatException {

      // Traces debug
      String prefixeTrc = "validateCertificates()";
      LOGGER.debug("{} - Début", prefixeTrc);

      // on vérifie tout le temps pa PKI et optionnellement le certificat
      LOGGER
            .debug(
                  "{} - Vérifie que la PKI dont est issu le certificat applicatif correspond à la PKI déclaré dans le contrat de service",
                  prefixeTrc);
      String patternPki = contract.getIdPki();
      List<String> patternPkis = contract.getListPki();

      X509Certificate pki = result.getPki();
      String pkiName = pki.getSubjectX500Principal().getName(
            X500Principal.RFC2253);

      if (CollectionUtils.isNotEmpty(patternPkis)) {
         LOGGER
               .debug(
                     "{} - Le certificat applicatif de signature du VI est issu de la PKI \"{}\", pour un contrat de service {} s'appuyant sur la PKI dont le pattern de nommage fait partie de \"{}\"",
                     new String[] { prefixeTrc, pkiName,
                           contract.getCodeClient(), patternPkis.toString() });
         checkPatterns(patternPkis, pkiName, ERREUR_PKI);

      } else {
         LOGGER
               .debug(
                     "{} - Le certificat applicatif de signature du VI est issu de la PKI \"{}\", pour un contrat de service {} s'appuyant sur la PKI dont le pattern de nommage est \"{}\"",
                     new String[] { prefixeTrc, pkiName,
                           contract.getCodeClient(), patternPki });
         checkPattern(patternPki, pkiName, ERREUR_PKI);
      }

      if (contract.isVerifNommage()) {

         LOGGER
               .debug(
                     "{} - Vérifie que le nom du certificat applicatif correspond au nom déclaré dans le contrat de service",
                     prefixeTrc);

         List<String> patternCerts = contract.getListCertifsClient();
         String patternCert = contract.getIdCertifClient();

         X509Certificate cert = result.getCertificat();
         String certName = cert.getSubjectX500Principal().getName(
               X500Principal.RFC2253);

         if (CollectionUtils.isNotEmpty(patternCerts)) {
            LOGGER
                  .debug(
                        "{} - Le certificat applicatif de signature du VI porte le nom \"{}\", pour un contrat de service {} attendant un certificat nommé selon un des patterns \"{}\"",
                        new String[] { prefixeTrc, certName,
                              contract.getCodeClient(), patternCerts.toString() });
            checkPatterns(patternCerts, certName, ERREUR_CERT);

         } else {
            LOGGER
                  .debug(
                        "{} - Le certificat applicatif de signature du VI porte le nom \"{}\", pour un contrat de service {} attendant un certificat nommé selon le pattern \"{}\"",
                        new String[] { prefixeTrc, certName,
                              contract.getCodeClient(), patternCert });
            checkPattern(patternCert, certName, ERREUR_CERT);
         }

      } else {
         LOGGER
               .debug(
                     "{} - La vérification du nom du certificat applicatif n'est pas activée pour le contrat de service {}",
                     prefixeTrc, contract.getCodeClient());
      }

      // Traces debug
      LOGGER.debug("{} - Fin", prefixeTrc);

   }

   private void checkPatterns(List<String> regexps, String value,
         String messageErreur) throws VICertificatException {

      boolean found = false;
      int i = 0;
      Pattern pattern;
      Matcher matcher;
      while (!found && i < regexps.size()) {
         pattern = Pattern.compile(regexps.get(i));
         matcher = pattern.matcher(value);
         found = matcher.find();
         i++;
      }

      if (!found) {
         throw new VICertificatException(messageErreur);
      }

   }

   /**
    * @param regexp
    * @param value
    * @throws VICertificatException
    */
   private void checkPattern(String regexp, String value, String messageErreur)
         throws VICertificatException {
      Pattern pattern = Pattern.compile(regexp);
      Matcher matcher = pattern.matcher(value);
      if (!matcher.find()) {
         throw new VICertificatException(messageErreur);
      }

   }
}
