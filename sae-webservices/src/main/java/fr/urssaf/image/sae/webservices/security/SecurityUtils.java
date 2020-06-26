package fr.urssaf.image.sae.webservices.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.axis2.extensions.spring.receivers.ApplicationContextHolder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.springframework.core.io.Resource;

/**
 * Classe utilitaires pour la mise en place du contexte de sécurité
 */
public final class SecurityUtils {

   private SecurityUtils() {

   }

   /**
    * Chargement d'un certificat X509 depuis un fichier de ressource
    * 
    * @param inputStream
    *           le stream représentant le certificat X509
    * 
    * @return l'objet certificat X509
    * @throws CertificateException
    *            exception lors du chargement du certificat X509
    */
   public static X509Certificate loadCertificat(InputStream inputStream)
         throws CertificateException {

      CertificateFactory certifFactory = CertificateFactory
            .getInstance("X.509");

      X509Certificate cert = (X509Certificate) certifFactory
            .generateCertificate(inputStream);
      return cert;

   }

   /**
    * Chargement d'une CRL depuis un fichier de ressource
    * 
    * @param inputStream
    *           le stream représentant la CRL
    * 
    * @return l'objet CRL
    * @throws CertificateException
    *            exception lors du chargement du CRL
    * @throws CRLException
    *            exception lors du chargement du CRL
    */
   public static X509CRL loadCRL(InputStream inputStream)
         throws CertificateException, CRLException {

      CertificateFactory certifFactory = CertificateFactory
            .getInstance("X.509");

      return (X509CRL) certifFactory.generateCRL(inputStream);

   }

   /**
    * Chargement des patterns d'issuer pour la vérification de la signature
    * électronique du VI, depuis le fichier de ressource
    * /security/pattern_issuer.txt
    * 
    * @return les patterns d'issuer
    */
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   public static List<String> loadIssuerPatterns() {

      Resource resource = ApplicationContextHolder.getContext().getResource(
            "classpath:security/pattern_issuer.txt");
      try {

         InputStream inStream = resource.getInputStream();
         try {

            List<String> patterns = IOUtils.readLines(inStream,
                  CharEncoding.UTF_8);

            return patterns;

         } finally {
            if (inStream != null) {
               inStream.close();
            }
         }

      } catch (IOException ex) {
         throw new RuntimeException(ex);
      }

   }

}
