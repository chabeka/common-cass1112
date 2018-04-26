package fr.urssaf.image.commons.webservice.util;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.springframework.core.io.Resource;

/**
 * Classe utilitaire pour SSL
 * 
 * 
 */
public final class SecurityUtil {

   private SecurityUtil() {

   }

   /**
    * Méthode pour instancier un magasin de certificat et charger un certificat
    * 
    * @param certificat
    *           ressource
    * @param password
    *           mot de passe du magasin de certificats
    * @param keyStoreType
    *           type du magasin de certificat
    * @return magasin de certificats
    * @throws KeyStoreException
    *            exception pour instancier un magasin de certificat
    * @throws NoSuchAlgorithmException
    *            exception pour chargement du certificat
    * @throws CertificateException
    *            exception pour chargement du certificat
    * @throws IOException
    *            exception pour chargement du certificat
    */
   public static KeyStore getKeyStore(Resource certificat, String password,
         String keyStoreType) throws KeyStoreException,
         NoSuchAlgorithmException, CertificateException, IOException {

      KeyStore keyStore = KeyStore.getInstance(keyStoreType);
      keyStore.load(certificat.getInputStream(), password.toCharArray());

      return keyStore;
   }

}
