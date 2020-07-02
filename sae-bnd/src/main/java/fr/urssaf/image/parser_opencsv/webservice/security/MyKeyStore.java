package fr.urssaf.image.parser_opencsv.webservice.security;

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

   private KeyStore keystore;

   private String alias;

   /**
    * Le mot de passe d'ouverture du p12, ainsi que de sa clé privée,
    */
   private final String passphrase;

   /**
    * Le type de clé ici PKCS12
    */
   private static final String KEY_TYPE = "PKCS12";

   /**
    * Le provider de la clé
    */
   private static final String KEY_PROVIDER = "SunJSSE";

   public MyKeyStore(final String passphrase, final String keyFileName) {

      // Le mot de passe d'ouverture du p12, ainsi que de sa clé privée,
      // est écrit dans un fichier properties
      this.passphrase = passphrase;
      try {
         keystore = KeyStore.getInstance(KEY_TYPE, KEY_PROVIDER);
         final InputStream inputStream = ResourceUtils.loadResource(this, keyFileName);
         try {
            keystore.load(inputStream, passphrase.toCharArray());
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
      return alias;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public KeyStore getKeystore() {
      return keystore;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getPassword() {
      return passphrase;
   }

}
