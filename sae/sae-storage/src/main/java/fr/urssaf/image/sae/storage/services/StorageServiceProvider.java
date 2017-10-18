package fr.urssaf.image.sae.storage.services;

import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * 
 * Fournit l’ensemble des services pour Manipuler les Objets DFCE.<BR />
 * 
 * 
 */
public interface StorageServiceProvider {

   /**
    * 
    * @return les services d'insertion ,de recherche,récupération.
    */
   StorageDocumentService getStorageDocumentService();

   /**
    * Permet d'ouvrir une connexion
    * 
    */

   void openConnexion();

   /**
    * Permet de fermer la connexion
    * 
    * 
    */
   void closeConnexion();

}
