package fr.urssaf.image.sae.client.vi.signature;

import java.security.KeyStore;

/**
 * Interface à implémenter pour configurer un KeyStore utilisable pour la
 * signature électronique du Vecteur d'Identification.
 * 
 */
public interface KeyStoreInterface {

   /**
    * L'alias de la clé publique
    * 
    * @return L'alias de la clé publique
    */
   String getAlias();

   /**
    * Le KeyStore
    * 
    * @return Le KeyStore
    */
   KeyStore getKeystore();

   /**
    * Le mot de passe de la clé privée
    * 
    * @return Le mot de passe de la clé privée
    */
   String getPassword();

}