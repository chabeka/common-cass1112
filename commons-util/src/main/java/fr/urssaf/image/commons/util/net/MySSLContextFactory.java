package fr.urssaf.image.commons.util.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Classe d'initialisation d'un objet SSLContext
 * {@link javax.net.ssl.SSLContext) on peut l'initialiser avec un certificat
 * protégé par un mot de passe (@see
 * fr.urssaf.image.commons.webservice.ssl.MySSLContextSource) *
 */
public class MySSLContextFactory {

   private SSLContext ctx;

   /**
    * Méthode de création du certificat de type P12
    * 
    * @param keystoreFile
    *           chemin du keystore p12
    * @param password
    *           mot du passe du keystore
    * @throws KeyManagementException exception
    * @throws KeyStoreException exception
    * @throws NoSuchAlgorithmException exception
    * @throws UnrecoverableKeyException exception
    * @throws CertificateException exception
    * @throws IOException exception
    */
   public MySSLContextFactory(String keystoreFile, String password)
         throws KeyManagementException, KeyStoreException,
         NoSuchAlgorithmException, UnrecoverableKeyException,
         CertificateException, IOException {

      this(keystoreFile, password, "PKCS12");
   }

   /**
    * Méthode création du certificat
    * 
    * @param keystoreFile
    *           chemin du keystore
    * @param password
    *           mot de passe
    * @param keyStoreType
    *           type du keystore
    * @throws KeyManagementException exception
    * @throws KeyStoreException exception
    * @throws NoSuchAlgorithmException exception
    * @throws UnrecoverableKeyException exception
    * @throws CertificateException exception
    * @throws IOException exception
    */
   public MySSLContextFactory(String keystoreFile, String password,
         String keyStoreType) throws KeyManagementException, KeyStoreException,
         NoSuchAlgorithmException, UnrecoverableKeyException,
         CertificateException, IOException {

      KeyStore keyStore = KeyStore.getInstance(keyStoreType);

      InputStream input = new FileInputStream(keystoreFile);
      try {
         keyStore.load(input, password.toCharArray());

         KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
         kmf.init(keyStore, password.toCharArray());

         ctx = SSLContext.getInstance("SSL");
         ctx.init(kmf.getKeyManagers(),
               new TrustManager[] { new MyTrustManager() }, null);
      } finally {
         input.close();
      }

   }

   /**
    * Retourne l'objet SSLContext initialisé
    * 
    * @return SSLContext
    */
   public final SSLContext getSSLContext() {
      return this.ctx;
   }

   private static class MyTrustManager implements X509TrustManager {

      @Override
      public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
         // aucune implémentation
      }

      @Override
      public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
         // aucune implémentation
      }

      @Override
      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }

   }

}
