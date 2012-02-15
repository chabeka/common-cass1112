package sae.client.demo.webservice.security.signature;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import sae.client.demo.util.ResourceUtils;

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
public final class DefaultKeystore {

   private static final String P12 = "certificat/ApplicationTestSAE.p12";

   private final KeyStore keystore;

   private final String alias;

   private final String password;

   private DefaultKeystore() {

      password = "dKQmz8NwyO4d";
      try {
         keystore = KeyStore.getInstance("PKCS12");
         InputStream inputStream = ResourceUtils.loadResource(this, P12);
         try {
            keystore.load(inputStream, password.toCharArray());

         } finally {
            inputStream.close();
         }

         alias = keystore.aliases().nextElement();

      } catch (GeneralSecurityException e) {
         throw new IllegalStateException(e);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   private static final DefaultKeystore INSTANCE = new DefaultKeystore();

   /**
    * 
    * @return instance du keystore
    */
   public static DefaultKeystore getInstance() {

      return INSTANCE;
   }

   /**
    * 
    * @return alias de la clé publique
    */
   public String getAlias() {
      return this.alias;
   }

   /**
    * 
    * @return keystore par défaut
    */
   public KeyStore getKeystore() {
      return this.keystore;
   }

   /**
    * 
    * @return mot de passe de la clé privée
    */
   public String getPassword() {
      return this.password;
   }
}
