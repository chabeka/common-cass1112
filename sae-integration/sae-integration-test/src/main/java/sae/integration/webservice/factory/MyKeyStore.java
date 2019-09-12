package sae.integration.webservice.factory;

import java.io.InputStream;
import java.security.KeyStore;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.client.vi.util.ResourceUtils;

/**
 * Classe permettant de fournir au composant sae-client-vi les éléments de
 * certificats électroniques pour la signature du VI
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
   public MyKeyStore(final String p12Name, final String password) {

      this.password = password;

      try {
         keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
         final InputStream inputStream = ResourceUtils.loadResource(this, p12Name);
         try {
            keystore.load(inputStream, password.toCharArray());

         }
         finally {
            if (inputStream != null) {
               inputStream.close();
            }
         }

         alias = keystore.aliases().nextElement();

      }
      catch (final Exception e) {
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
