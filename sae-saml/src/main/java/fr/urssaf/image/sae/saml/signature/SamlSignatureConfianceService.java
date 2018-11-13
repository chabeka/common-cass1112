package fr.urssaf.image.sae.saml.signature;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;

import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureValidateException;
import fr.urssaf.image.sae.saml.params.SamlSignatureVerifParams;

/**
 * Vérification d'une chaîne de certification (Algorithme PKIX)<br>
 * <br>
 * Les API utilisées sont les API natives de la JDK<br>
 * <br>
 * Pour plus d'informations : <br>
 * <ul>
 * <li>
 * <a href="http://www.ietf.org/rfc/rfc3280.txt">RFC 3280</a> (spécifie
 * l'algorithme de vérification d'une chaîne de certification X.509)</li>
 * <li>
 * <a href="http://download.oracle.com/javase/6/docs/technotes/guides/security/certpath/CertPathProgGuide.html"
 * > Java PKI Programmer's Guide</a></li>
 * </ul>
 * 
 */
public final class SamlSignatureConfianceService {

   private SamlSignatureConfianceService() {

   }

   /**
    * Vérification de la confiance dans le certificat de signature passé en
    * paramètre
    * 
    * @param signVerifParams
    *           Les éléments nécessaires à la vérification de la confiance
    * 
    * @param chaineCertif
    *           La chaîne de certification à vérifier
    * 
    * @return l'identifiant de la PKI
    * 
    * @throws SamlSignatureValidateException
    *            si la vérification de la confiance a échoué
    * 
    * 
    */
   public static X509Certificate verifierConfiance(
         SamlSignatureVerifParams signVerifParams,
         List<java.security.cert.X509Certificate> chaineCertif)
         throws SamlSignatureValidateException {

      // Vérifications des paramètres d'entrée
      if (signVerifParams == null) {
         throw new NullArgumentException("signatureVerifParams");
      }
      if (chaineCertif == null) {
         throw new NullArgumentException("chaineCertif");
      }

      // Début du traitement
      try {

         // Instantiation d'une Factory de fonctions sur les certificats X509
         CertificateFactory certifFactory = CertificateFactory
               .getInstance("X.509");

         // Collections.reverse(chaineCertif);

         // Construction de la chaîne de certification au format attendue par
         // la méthode de validation de la signature
         CertPath certPath = certifFactory.generateCertPath(chaineCertif);

         // Création de l'objet validateur de la chaîne de certification
         // Cette validation se fera selon la définition de la PKIX (cf. RFC
         // 2380)
         CertPathValidator cpv = CertPathValidator.getInstance("PKIX");

         // Construction de la liste des certificats de confiance
         // Il s'agit des certificats des AC racine
         Set<TrustAnchor> trustAnchors = buildTrustAnchors(signVerifParams);

         // Création de l'objet contenant les paramètres de la validation PKIX
         // Cet objet est initialisé avec la liste des certificats de confiance
         PKIXParameters parameters = new PKIXParameters(trustAnchors);

         // Active la vérification des CRL, et ajouter les CRL fournies à la
         // méthode
         // dans l'objet de paramétrage de la validation PKIX
         parameters.setRevocationEnabled(true);
         addCRL(signVerifParams, parameters);

         // Lance la validation de la chaîne de certification
         // Si la validation échoue, une exception est levée
         try {

            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) cpv
                  .validate(certPath, parameters);
            TrustAnchor anchor = result.getTrustAnchor();
            X509Certificate certificate = anchor.getTrustedCert();

            return certificate;

         } catch (CertPathValidatorException e) {

            throw new SamlSignatureValidateException(e);

         }

      } catch (CertificateException e) {
         throw new SamlSignatureValidateException(e);
      } catch (NoSuchAlgorithmException e) {
         throw new SamlSignatureValidateException(e);
      } catch (InvalidAlgorithmParameterException e) {
         throw new SamlSignatureValidateException(e);
      }

   }

   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private static Set<TrustAnchor> buildTrustAnchors(
         SamlSignatureVerifParams signVerifParams) {

      Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();

      for (X509Certificate cert : signVerifParams.getListeCertifsACRacine()) {
         TrustAnchor anchor = new TrustAnchor(cert, null);
         trustAnchors.add(anchor);
      }

      return trustAnchors;

   }

   private static void addCRL(SamlSignatureVerifParams signVerifParams,
         PKIXParameters parameters) throws InvalidAlgorithmParameterException,
         NoSuchAlgorithmException {

      CollectionCertStoreParameters ccsp = new CollectionCertStoreParameters(
            signVerifParams.getCrls());
      CertStore store = CertStore.getInstance("Collection", ccsp);
      parameters.addCertStore(store);

   }

}
