package fr.urssaf.image.commons.webservice.ssl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import fr.urssaf.image.commons.webservice.util.SecurityUtil;

/**
 * Classe d'initialisation d'un objet SSLContext
 * {@link javax.net.ssl.SSLContext) on peut l'initialiser avec un certificat
 * protégé par un mot de passe (@see
 * fr.urssaf.image.commons.webservice.ssl.MySSLContextSource) *
 */
public class MySSLContextFactory {

   private static final Logger LOGGER = Logger
         .getLogger(MySSLContextFactory.class);

   private SSLContext ctx;

   private MySSLContextSource contextSource;

   private String keyStoreType = "PKCS12";

   private void initFactory() throws KeyStoreException,
         NoSuchAlgorithmException, CertificateException, IOException,
         UnrecoverableKeyException, KeyManagementException {

      KeyStore keyStore = null;

      keyStore = SecurityUtil.getKeyStore(contextSource.getCertificat(),
            contextSource.getCertifPassword(), keyStoreType);

      // RECUPERATION DU COUPLE CLE PRIVEE/PUBLIQUE ET DU CERTIFICAT
      // PUBLIQUE

      X509Certificate cert = null;
      PrivateKey privatekey = null;
      PublicKey publickey = null;
      String alias = "";
      KeyStore jks = null;

      jks = KeyStore.getInstance("JKS");
      jks.load(null, null);
      Enumeration<String> enumAlias = keyStore.aliases();

      List<String> listAlias = new ArrayList<String>();

      while (enumAlias.hasMoreElements()) {
         String elementAlias = enumAlias.nextElement();
         LOGGER.debug(elementAlias);
         listAlias.add(elementAlias);
      }

      String[] aliases = listAlias.toArray(new String[listAlias.size()]);
      for (int i = 0; i < aliases.length; i++) {
         if (keyStore.isKeyEntry(aliases[i])) {
            LOGGER.debug(aliases[i]);
            alias = aliases[i];
            break;
         }
      }
      char[] password = contextSource.getCertifPassword().toCharArray();

      privatekey = (PrivateKey) keyStore.getKey(alias, password);
      cert = (X509Certificate) keyStore.getCertificate(alias);
      publickey = keyStore.getCertificate(alias).getPublicKey();

      jks.setCertificateEntry(alias, cert);

      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(keyStore, password);

      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(jks);

      ctx = SSLContext.getInstance("SSL");

      ctx.init(kmf.getKeyManagers(),
            new TrustManager[] { new MyTrustManager() }, null);

      LOGGER.debug("alias:" + alias);
      LOGGER.debug(cert.getType());
      LOGGER.debug("private key:" + privatekey);
      LOGGER.debug("public key:" + publickey);

   }

   private void initSSLContext() {

      try {
         ctx = SSLContext.getInstance("SSL");
         ctx.init(null, new TrustManager[] { new MyTrustManager() }, null);

      } catch (Exception e) {
         LOGGER.error(e.getMessage(), e);
         return;
      }

   }

   /**
    * Accesseur pour initialiser SSLContext avec un certificat
    * 
    * @param contextSource
    *           l'objet MySSLContextSource
    */
   public final void setSSLContextSource(MySSLContextSource contextSource) {
      this.contextSource = contextSource;
   }

   /**
    * Par défaut le type de magasin de certificats est du PKCS12 On peut
    * modifier ce type
    * 
    * @param keyStoreType
    *           nouveau type du magasin de certificat
    */
   public final void setKeyStoreType(String keyStoreType) {
      this.keyStoreType = keyStoreType;
   }

   /**
    * Retourne l'objet SSLContext initialisé
    * 
    * @return SSLContext
    */
   public final SSLContext getSSLContext() {

      synchronized (this) {
         if (this.ctx == null) {
            if (contextSource == null) {
               initSSLContext();
            } else {
               try {
                  initFactory();
               } catch (Exception e) {
                  LOGGER.error("Erreur: fichier "
                        + contextSource.getCertificat()
                        + " n'est pas un fichier " + this.keyStoreType
                        + " valide ou passphrase incorrect");
                  LOGGER.error(e.getMessage(), e);
                  throw new IllegalArgumentException(e);
               }
            }

         }
      }

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
