package fr.urssaf.image.sae.saml.testutils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

/**
 * Outils de création de java.security.KeyStore
 */
public final class KeyStoreFactory {

   private KeyStoreFactory() {

   }

   // CHECKSTYLE:OFF
   public static KeyStore createKeystore() throws KeyStoreException,
         NoSuchAlgorithmException, CertificateException, IOException,
         NoSuchProviderException {

      return createKeystore(
            "src/test/resources/certif_test_std/Portail_Image.p12",
            "hiUnk6O3QnRN");

   }

   // CHECKSTYLE:ON

   protected static KeyStore createKeystore(String file, String password)
         throws KeyStoreException, NoSuchAlgorithmException,
         CertificateException, IOException, NoSuchProviderException {

      KeyStore keystore = KeyStore.getInstance("PKCS12", "SunJSSE");

      FileInputStream inputStream = new FileInputStream(file);
      try {
         keystore.load(inputStream, password.toCharArray());

      } finally {
         inputStream.close();
      }

      return keystore;

   }
}
