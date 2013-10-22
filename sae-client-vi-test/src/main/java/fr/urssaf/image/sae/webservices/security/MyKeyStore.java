package fr.urssaf.image.sae.webservices.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ResourceBundle;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.client.vi.util.ResourceUtils;

/**
 * Classe permettant de fournir au composant sae-client-vi les éléments de
 * certificats électroniques pour la signature du VI
 * 
 * L'idée est d'instancier un singleton de cette classe au démarrage de
 * l'application.
 */
public final class MyKeyStore implements KeyStoreInterface {

   private final KeyStore keystore;
   private final String alias;
   private final String password;

   /**
    * Constructeur
    */
   public MyKeyStore() {

      // Le fichier ApplicationTestSAE.p12 est embarqué dans les ressources,
      // dans src/main/resources/

      // Le mot de passe d'ouverture du p12, ainsi que de sa clé privée,
      // est écrit dans un fichier properties
      // sae-client-vi-test-security.properties,
      // qui se trouve dans src/main/resources/

      ResourceBundle securityData = ResourceBundle
            .getBundle("sae-client-vi-test-security");
      password = securityData.getString("password");

      try {
         keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
         InputStream inputStream = ResourceUtils.loadResource(this,
               "ApplicationTestSAE.p12");
         try {
            keystore.load(inputStream, password.toCharArray());

         } finally {
            if (inputStream != null) {
               inputStream.close();
            }
         }

         alias = keystore.aliases().nextElement();

      } catch (GeneralSecurityException e) {
         throw new ViSignatureException(e);

      } catch (IOException e) {
         throw new ViSignatureException(e);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getAlias() {
      return this.alias;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public KeyStore getKeystore() {
      return this.keystore;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getPassword() {
      return this.password;
   }

}
