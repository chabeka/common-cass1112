package fr.urssaf.image.commons.webservice.ssl;

import org.springframework.core.io.Resource;

/**
 * Classe de propriétés d'un certificat protégé par mot de passe pour
 * SSLContext (@see fr.urssaf.image.commons.webservice.ssl.MySSLContextSource)
 * 
 */
public class MySSLContextSource {

   private Resource certificat;

   private String certifPassword;

   /**
    * getter d'un objet Ressource pour le certificat
    * @return certificat
    */
   public Resource getCertificat() {
      return certificat;
   }

   /***
    * setter du certificat 
    * @param certificat objet certificat
    */
   public void setCertificat(Resource certificat) {
      this.certificat = certificat;
   }

   /**
    * getter du mot de passe
    * @return mot de passe
    */
   public String getCertifPassword() {
      return certifPassword;
   }

   /**
    * setter pour le mot de passe du certificat
    * @param certifPassword mot de passe
    */
   public void setCertifPassword(String certifPassword) {
      this.certifPassword = certifPassword;
   }

}
