package fr.urssaf.image.sae.vi.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


/**
 * Outils de création de java.security.KeyStore
 */
public final class KeyStoreFactory {
   
   private KeyStoreFactory() {

   }
   
   protected static KeyStore createKeystore() throws KeyStoreException,
         NoSuchAlgorithmException, CertificateException, IOException {

      return createKeystore("src/test/resources/Portail_Image.p12",
            "hiUnk6O3QnRN");

   }

   protected static KeyStore createKeystore(String file, String password)
         throws KeyStoreException, NoSuchAlgorithmException,
         CertificateException, IOException {

      KeyStore keystore = KeyStore.getInstance("PKCS12");

      FileInputStream inputStream = new FileInputStream(file);
      try {
         keystore.load(inputStream, password.toCharArray());

      } finally {
         inputStream.close();
      }

      return keystore;

   }
}
