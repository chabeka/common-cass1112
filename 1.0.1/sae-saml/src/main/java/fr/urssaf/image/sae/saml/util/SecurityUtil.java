package fr.urssaf.image.sae.saml.util;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

/**
 * Classe de manipulation de l'objet {@link KeyStore}
 * 
 * 
 */
public final class SecurityUtil {

   private SecurityUtil() {

   }

   /**
    * Récupère une clé privée dans un magasin de certificats
    * 
    * @param keystore
    *           magasin de certificats
    * @param alias
    *           alias du ceriticat recherché
    * @param password
    *           mot de passe de la clé privée
    * @return clé privée recherchée
    * @throws UnrecoverableKeyException
    *            exception levé par le keystore
    * @throws KeyStoreException
    *            exception levé par le keystore
    * @throws NoSuchAlgorithmException
    *            exception levé par le keystore
    */
   public static PrivateKey loadPrivateKey(KeyStore keystore, String alias,
         String password) throws UnrecoverableKeyException, KeyStoreException,
         NoSuchAlgorithmException {

      return (PrivateKey) keystore.getKey(alias, password.toCharArray());

   }

   /**
    * Récupère le certifcat X.509 dans un magasin de certificats
    * 
    * @param keystore
    *           magasin de certificats
    * @param alias
    *           alias du certificat recherché
    * @return certificat X.509 recherché
    * @throws KeyStoreException
    *            xception levé par le keystore
    */
   public static X509Certificate loadX509Certificate(KeyStore keystore,
         String alias) throws KeyStoreException {

      return (X509Certificate) keystore.getCertificate(alias);

   }

}
