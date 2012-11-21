package fr.urssaf.image.sae.client.vi.signature;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ResourceBundle;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.util.ResourceUtils;

/**
 * Configuration d'un {@link keyStore} par défaut à partir d'un .p12<br>
 * <br>
 * <ul>
 * <li><b>p12</b>: PNR_Application_Test.p12</li>
 * <li><b>password</b>: QEtDiGuGuEnZ</li>
 * </ul>
 * 
 * Cette classe est un singleton<br>
 * l'unique instance est accessible avec la méthode {@link #getInstance()}
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
    * 
    * @return instance du keystore
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
