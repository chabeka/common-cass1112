/**
 * 
 */
package fr.urssaf.image.sae.client.vi.signature;

import java.security.KeyStore;

/**
 * 
 * 
 */
public interface KeyStoreInterface {

   /**
    * 
    * @return alias de la clé publique
    */
   String getAlias();

   /**
    * 
    * @return keystore par défaut
    */
   KeyStore getKeystore();

   /**
    * 
    * @return mot de passe de la clé privée
    */
   String getPassword();

   
}