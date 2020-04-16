package fr.urssaf.image.sae.saml.opensaml.signature;

import java.security.cert.CertificateException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NullArgumentException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.validation.ValidationException;

import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509ConvertException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlAutoSignedCertificateException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlIssuerPatternException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlNotAutoSignedCertificateException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureCryptoException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureKeyInfoException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureNonConformeAuProfilException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureNotFoundException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureValidateException;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;
import fr.urssaf.image.sae.saml.params.SamlSignatureVerifParams;
import fr.urssaf.image.sae.saml.signature.SamlSignatureConfianceService;

/**
 * Classe de service pour la vérification de la signature XML d'une assertion
 * SAML 2.0
 * 
 */
public class SamlSignatureValidateService {

   /**
    * 
    * Vérification de la signature d'une assertion SAML
    * 
    * @param assertion
    *           assertion SAML contenant la signature à vérifier
    * @param signVerifParams
    *           Les éléments nécessaires à la vérification de la signature de
    *           l'assertion
    * @return un objet représentant les certificats intervenants dans la
    *         vérification de la signature
    * @throws SamlSignatureValidateException
    *            la signature est invalide
    */
   public final SignatureVerificationResult verifierSignature(
         final Assertion assertion, final SamlSignatureVerifParams signVerifParams)
               throws SamlSignatureValidateException {

      // Vérifications des paramètres d'entrée
      if (assertion == null) {
         throw new NullArgumentException("assertion");
      }
      if (signVerifParams == null) {
         throw new NullArgumentException("signatureVerifParams");
      }

      // Vérifie que les certificats racines transmis dans signVerifParams
      // sont bien des certificats auto-signés
      verifierCertifRacinesAutoSignes(signVerifParams.getListeCertifsACRacine());

      // Récupération de la signature de l'assertion
      final Signature signature = assertion.getSignature();
      if (signature == null) {
         throw new SamlSignatureNotFoundException();
      }

      // Récupération de la clé publique dont la clé privée a servi pour
      // la signature.
      // En tant que règle de gestion, cette clé publique est le premier
      // certificat X509 trouvé dans la partie <KeyInfo> de la signature
      X509Certificate publicKeyOpenSaml;
      try {
         publicKeyOpenSaml = SamlSignatureUtils.getPublicKey(assertion
               .getSignature());
      } catch (final SamlKeyInfoException e) {
         throw new SamlSignatureKeyInfoException(e);
      }

      // Conversion de l'objet certificat X509 en un type natif de l'API Java
      java.security.cert.X509Certificate publicKeyNatif;
      try {
         publicKeyNatif = SamlSignatureUtils.convertX509(publicKeyOpenSaml);
      } catch (final SamlX509ConvertException e) {
         throw new SamlSignatureValidateException(e);
      }

      // Vérifications sur le certificat lui-même
      verifierCertificatClePublique(publicKeyNatif);

      // Vérifications de la partie cryptographie de la signature
      verifierCryptographie(signature, publicKeyNatif);

      // Vérifications de la confiance dans le certificat de signature
      final java.security.cert.X509Certificate pki = verifierConfiance(signature,
            signVerifParams);

      // Vérification du nom de l'AC qui a délivré le certificat du signataire.
      // On ne fait la vérification dans le cas où il faut vérifier la validité des certificats
      if (signVerifParams.shouldValidateCerticates()) {
         final String idPki = signVerifParams.getCertifsACRacine().get(pki);
         final String issuer = publicKeyNatif.getIssuerX500Principal()
               .getName(
                     X500Principal.RFC2253);
         verifierIssuerPki(idPki, signVerifParams, issuer);
      }

      // création de l'objet de retour
      final SignatureVerificationResult result = new SignatureVerificationResult();
      result.setCertificat(publicKeyNatif);
      result.setPki(pki);

      return result;

   }

   protected void verifierIssuerPki(final String idPki,
         final SamlSignatureVerifParams signVerifParams, final String issuer)
               throws SamlIssuerPatternException {

      boolean match = false;
      List<String> patternList = null;
      if (signVerifParams.getPatternsIssuer() != null
            && CollectionUtils.isNotEmpty(signVerifParams.getPatternsIssuer()
                  .get(idPki))) {

         patternList = signVerifParams.getPatternsIssuer().get(idPki);

         Pattern pattern;
         Matcher matcher;
         for (final String regex : patternList) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(issuer);
            if (matcher.find()) {
               match = true;
               break;
            }
         }
      }

      if (!match) {
         throw new SamlIssuerPatternException(issuer, patternList);
      }

   }

   private void verifierCertificatClePublique(
         final java.security.cert.X509Certificate certificat)
               throws SamlSignatureValidateException {

      // Vérifie que le certificat ne soit pas auto-signé
      verifierCertificatClePubliqueAutoSigne(certificat);

   }

   private void verifierCertificatClePubliqueAutoSigne(
         final java.security.cert.X509Certificate certificat)
               throws SamlSignatureValidateException {

      // Regarde s'il s'agit d'un certificat auto-signé.
      // Si oui, on lève une exception, car ils ne sont pas autorisés
      if (certificat.getSubjectDN().equals(certificat.getIssuerDN())) {
         throw new SamlAutoSignedCertificateException();
      }

   }

   // protected final void verifierCertificatClePubliqueIssuerPattern(
   // SamlSignatureVerifParams signVerifParams,
   // java.security.cert.X509Certificate certifClePubSign)
   // throws SamlIssuerPatternException {
   //
   // // On ne traite que s'il y a au moins 1 pattern
   // if ((signVerifParams.getPatternsIssuer() != null)
   // && (signVerifParams.getPatternsIssuer().size() > 0)) {
   //
   // // Récupération de l'Issuer au format décrit dans le RFC 2253
   // String issuer = certifClePubSign.getIssuerX500Principal().getName(
   // X500Principal.RFC2253);
   //
   // // Appel de la méthode de vérification
   // verifierCertificatClePubliqueIssuerPattern(issuer, signVerifParams
   // .getPatternsIssuer());
   //
   // }
   //
   // }
   //
   // protected final void verifierCertificatClePubliqueIssuerPattern(
   // String issuer, Map<String, List<String>> patterns)
   // throws SamlIssuerPatternException {
   //
   // Boolean unMatch = false;
   // List<String> patternList = new ArrayList<String>();
   // if (patterns != null) {
   //
   // for (List<String> values : patterns.values()) {
   // if (CollectionUtils.isNotEmpty(values)) {
   // patternList.addAll(values);
   // }
   // }
   //
   // Pattern pattern;
   // Matcher matcher;
   // for (String regex : patternList) {
   // pattern = Pattern.compile(regex);
   // matcher = pattern.matcher(issuer);
   // if (matcher.find()) {
   // unMatch = true;
   // break;
   // }
   // }
   // }
   //
   // if (!unMatch) {
   // throw new SamlIssuerPatternException(issuer, patternList);
   // }
   //
   // }

   private void verifierCryptographie(final Signature signature,
         final java.security.cert.X509Certificate certSignature)
               throws SamlSignatureValidateException {

      final BasicX509Credential x509Credential = new BasicX509Credential();
      x509Credential.setEntityCertificate(certSignature);

      final SAMLSignatureProfileValidator profilvalidator = new SAMLSignatureProfileValidator();
      try {
         profilvalidator.validate(signature);
      } catch (final ValidationException e) {
         throw new SamlSignatureNonConformeAuProfilException(e);
      }

      final SignatureValidator validator = new SignatureValidator(x509Credential);
      try {
         validator.validate(signature);
      } catch (final ValidationException e) {
         throw new SamlSignatureCryptoException(e);
      }

   }

   private java.security.cert.X509Certificate verifierConfiance(
         final Signature signature, final SamlSignatureVerifParams signVerifParams)
               throws SamlSignatureValidateException {

      // Vérifications des paramètres d'entrée
      if (signature == null) {
         throw new NullArgumentException("signature");
      }
      if (signVerifParams == null) {
         throw new NullArgumentException("signatureVerifParams");
      }

      // Extraction du certificat de signature et sa chaîne de certification
      // de la partie <KeyInfo> de la signature
      List<java.security.cert.X509Certificate> chaineCertif;
      try {
         chaineCertif = KeyInfoHelper.getCertificates(signature.getKeyInfo());
      } catch (final CertificateException e) {
         throw new SamlSignatureValidateException(e);
      }

      // Appel du service de vérification, qui utilise les API natives de Java
      return SamlSignatureConfianceService.verifierConfiance(signVerifParams,
            chaineCertif);

   }

   /**
    * Vérifie que les certificats des AC racines transmises dans les paramètres
    * de vérification de la signature soient bien des certificats auto-signés
    * 
    * @param certifRacines
    *           la liste des certificats de AC racine
    * @throws SamlNotAutoSignedCertificateException
    *            si un des certificats n'est pas auto-signés
    */
   protected final void verifierCertifRacinesAutoSignes(
         final List<java.security.cert.X509Certificate> certifRacines)
               throws SamlNotAutoSignedCertificateException {

      if (certifRacines != null && !certifRacines.isEmpty()) {
         for (final java.security.cert.X509Certificate cert : certifRacines) {
            if (!cert.getSubjectX500Principal().equals(
                  cert.getIssuerX500Principal())) {
               final String subject = cert.getSubjectX500Principal().getName(
                     X500Principal.RFC2253);
               final String issuer = cert.getIssuerX500Principal().getName(
                     X500Principal.RFC2253);
               throw new SamlNotAutoSignedCertificateException(subject, issuer);
            }
         }
      }

   }

}
