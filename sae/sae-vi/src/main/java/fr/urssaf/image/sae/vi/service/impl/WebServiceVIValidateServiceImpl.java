package fr.urssaf.image.sae.vi.service.impl;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

   private static final Logger LOG_VALIDITY = LoggerFactory.getLogger("certificates_validity");

   private final SamlAssertionVerificationService checkService = new SamlAssertionVerificationService();

   private final List<String> listClientCS = new ArrayList<>();
   /**
    * {@inheritDoc}
    */
   @Override
   public final SignatureVerificationResult validate(final Element identification,
         final VISignVerifParams signVerifParams, final boolean shouldValidateCertificates) throws VIFormatTechniqueException,
   VISignatureException {

      try {

         final SamlSignatureVerifParams samlVerifSignPrms = convertSignParams(signVerifParams, shouldValidateCertificates);

         return checkService.verifierAssertion(identification,
               samlVerifSignPrms);

      } catch (final SamlFormatException e) {
         throw new VIFormatTechniqueException(e);
      } catch (final SamlSignatureException e) {
         throw new VISignatureException(e);
      }

   }

   protected final SamlSignatureVerifParams convertSignParams(
         final VISignVerifParams viSignParams, final boolean shouldValidateCertificates) {

      final SamlSignatureVerifParams samlSignParams = new SamlSignatureVerifParams();

      samlSignParams.setCertifsACRacine(viSignParams.getCertifsACRacine());
      samlSignParams.setCrls(viSignParams.getCrls());
      samlSignParams.setPatternsIssuer(viSignParams.getPatternsIssuer());
      samlSignParams.setValidateCerticates(shouldValidateCertificates);

      return samlSignParams;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void validate(final SamlAssertionData data, final URI serviceVise, final Date systemDate) throws VIInvalideException,
   VIAppliClientException, VINivAuthException, VIPagmIncorrectException,
   VIServiceIncorrectException {

      final String prefixeTrc = "validate()";

      // la date systeme doit être postérieure à NotOnBefore
      final Date notOnBefore = data.getAssertionParams().getCommonsParams()
            .getNotOnBefore();

      if (systemDate.compareTo(notOnBefore) < 0) {

         final Map<String, String> args = new HashMap<>();
         args.put("0", DateFormatUtils.format(notOnBefore, DATE_PATTERN));
         args.put("1", DateFormatUtils.format(systemDate, DATE_PATTERN));

         final String message = "L'assertion n'est pas encore valable: elle ne sera active qu'à partir de ${0} alors que nous sommes le ${1}";

         throw new VIInvalideException(StrSubstitutor.replace(message, args));
      }

      // la date systeme doit être strictement antérieure à NotOnOrAfter
      final Date notOnOrAfter = data.getAssertionParams().getCommonsParams()
            .getNotOnOrAfter();
      if (systemDate.compareTo(notOnOrAfter) >= 0) {

         final Map<String, String> args = new HashMap<>();
         args.put("0", DateFormatUtils.format(notOnOrAfter, DATE_PATTERN));
         args.put("1", DateFormatUtils.format(systemDate, DATE_PATTERN));

         final String message = "L'assertion a expirée : elle n'était valable que jusqu’au ${0}, hors nous sommes le ${1}";

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


   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void validateCertificates(final ServiceContract contract,
         final SignatureVerificationResult result) throws VICertificatException {

      // Traces debug
      final String prefixeTrc = "validateCertificates()";
      LOGGER.debug("{} - Début", prefixeTrc);

      if (contract.isVerifNommage()) {

         LOGGER
         .debug(
               "{} - Vérifie que la PKI dont est issu le certificat applicatif correspond à la PKI déclaré dans le contrat de service",
               prefixeTrc);
         final String patternPki = contract.getIdPki();
         final List<String> patternPkis = contract.getListPki();

         final X509Certificate pki = result.getPki();
         final String pkiName = pki.getSubjectX500Principal()
               .getName(
                     X500Principal.RFC2253);

         if (CollectionUtils.isNotEmpty(patternPkis)) {
            LOGGER
            .debug(
                  "{} - Le certificat applicatif de signature du VI est issu de la PKI \"{}\", pour un contrat de service {} s'appuyant sur la PKI dont le pattern de nommage fait partie de \"{}\"",
                  prefixeTrc,
                  pkiName,
                  contract.getCodeClient(),
                  patternPkis);
            checkPatterns(patternPkis, pkiName, ERREUR_PKI);

         } else {
            LOGGER
            .debug(
                  "{} - Le certificat applicatif de signature du VI est issu de la PKI \"{}\", pour un contrat de service {} s'appuyant sur la PKI dont le pattern de nommage est \"{}\"",
                  prefixeTrc,
                  pkiName,
                  contract.getCodeClient(),
                  patternPki);
            checkPattern(patternPki, pkiName, ERREUR_PKI);
         }

         LOGGER
         .debug(
               "{} - Vérifie que le nom du certificat applicatif correspond au nom déclaré dans le contrat de service",
               prefixeTrc);

         final List<String> patternCerts = contract.getListCertifsClient();
         final String patternCert = contract.getIdCertifClient();

         final X509Certificate cert = result.getCertificat();
         final String certName = cert.getSubjectX500Principal().getName(
               X500Principal.RFC2253);

         if (CollectionUtils.isNotEmpty(patternCerts)) {
            LOGGER
            .debug(
                  "{} - Le certificat applicatif de signature du VI porte le nom \"{}\", pour un contrat de service {} attendant un certificat nommé selon un des patterns \"{}\"",
                  prefixeTrc,
                  certName,
                  contract.getCodeClient(),
                  patternCerts);
            checkPatterns(patternCerts, certName, ERREUR_CERT);

         } else {
            LOGGER
            .debug(
                  "{} - Le certificat applicatif de signature du VI porte le nom \"{}\", pour un contrat de service {} attendant un certificat nommé selon le pattern \"{}\"",
                  prefixeTrc,
                  certName,
                  contract.getCodeClient(),
                  patternCert);
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

   private void checkPatterns(final List<String> regexps, final String value,
         final String messageErreur) throws VICertificatException {

      boolean found = false;
      int index = 0;
      Pattern pattern;
      Matcher matcher;
      while (!found && index < regexps.size()) {
         pattern = Pattern.compile(regexps.get(index));
         matcher = pattern.matcher(value);
         found = matcher.find();
         index++;
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
   private void checkPattern(final String regexp, final String value, final String messageErreur)
         throws VICertificatException {
      final Pattern pattern = Pattern.compile(regexp);
      final Matcher matcher = pattern.matcher(value);
      if (!matcher.find()) {
         throw new VICertificatException(messageErreur);
      }

   }

  @Override
  public void checkCertificateValidityDays(final SignatureVerificationResult result, final ServiceContract contract, final int validityMinDays) {
    // Surveillance fin de validité des certificats

    final X509Certificate cert = result.getCertificat();
    final Date notOnOrAfter = cert.getNotAfter();
    final LocalDate localDateNotOnBefore = notOnOrAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    final String csCodeClient = contract.getCodeClient();
    final Long days = ChronoUnit.DAYS.between(LocalDate.now(), localDateNotOnBefore);

    if (!listClientCS.contains(csCodeClient)) {
      listClientCS.add(csCodeClient);

      if (days.intValue() < 0) {
        LOG_VALIDITY.warn("Le certificat du client {} est périmé depuis {} jours", new Object[] {csCodeClient, Math.abs(days)});
      } else if (days.intValue() < validityMinDays) {
        LOG_VALIDITY.warn("Le certificat du client {} est périmé dans {} jours", new Object[] {csCodeClient, days});
      }
    }

  }
}
