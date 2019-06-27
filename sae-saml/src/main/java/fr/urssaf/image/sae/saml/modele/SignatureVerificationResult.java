/**
 * 
 */
package fr.urssaf.image.sae.saml.modele;

import java.security.cert.X509Certificate;

/**
 * Classe représentant le résultat de la vérification de la signature transmise
 * 
 */
public class SignatureVerificationResult {

   private X509Certificate pki;
   private X509Certificate certificat;

   /**
    * @return the pki
    */
   public final X509Certificate getPki() {
      return pki;
   }

   /**
    * @param pki
    *           the pki to set
    */
   public final void setPki(X509Certificate pki) {
      this.pki = pki;
   }

   /**
    * @return the certificat
    */
   public final X509Certificate getCertificat() {
      return certificat;
   }

   /**
    * @param certificat
    *           the certificat to set
    */
   public final void setCertificat(X509Certificate certificat) {
      this.certificat = certificat;
   }

}
