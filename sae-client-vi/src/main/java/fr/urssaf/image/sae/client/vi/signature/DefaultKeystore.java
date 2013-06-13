package fr.urssaf.image.sae.client.vi.signature;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ResourceBundle;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.util.ResourceUtils;

/**
 * Configuration d'un {@link KeyStore} à partir d'un PKCS#12 par défaut fourni
 * dans le projet (convient uniquement pour des tests).<br>
 * <br>
 * Cette classe est un singleton<br>
 * L'unique instance est accessible avec la méthode {@link #getInstance()}
 * 
 */
public final class DefaultKeystore implements KeyStoreInterface {

   private static String p12 = "PNR_Application_Test.p12";

   private final KeyStore keystore;
   private final String alias;
   private final String password;

   private DefaultKeystore() {

      ResourceBundle securityData = ResourceBundle.getBundle("security");
      p12 = securityData.getString("fileName");
      password = securityData.getString("password");

      try {
         keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
         InputStream inputStream = ResourceUtils.loadResource(this, p12);
         try {
            keystore.load(inputStream, password.toCharArray());

         } finally {
            inputStream.close();
         }

         alias = keystore.aliases().nextElement();

      } catch (GeneralSecurityException e) {
         throw new ViSignatureException(e);

      } catch (IOException e) {
         throw new ViSignatureException(e);
      }
   }

   private static final class DefaultKeyStoreHolder {
      private DefaultKeyStoreHolder() {
      }

      private static final KeyStoreInterface INSTANCE = new DefaultKeystore();
   }

   /**
    * Renvoie l'unique instance du KeyStore
    * 
    * @return l'instance (unique) du keystore
    */
   public static KeyStoreInterface getInstance() {

      return DefaultKeyStoreHolder.INSTANCE;
   }

   /**
    * {@inheritDoc}
    */
   public String getAlias() {
      return this.alias;
   }

   /**
    * {@inheritDoc}
    */
   public KeyStore getKeystore() {
      return this.keystore;
   }

   /**
    * {@inheritDoc}
    */
   public String getPassword() {
      return this.password;
   }
}
