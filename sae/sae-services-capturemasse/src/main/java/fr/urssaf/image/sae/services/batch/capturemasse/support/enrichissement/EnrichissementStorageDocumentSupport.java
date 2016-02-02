/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Composant pour l'enrichissement des métadonnées d'un document dans les
 * traitements de capture de masse
 * 
 */
public interface EnrichissementStorageDocumentSupport {

   /**
    * Ajout de données au StorageDocument avant archivage
    * 
    * @param document
    *           le document à archiver
    * @param uuid
    *           identifiant unique à ajouter
    * @return le document avec les données mises à jour
    */
   StorageDocument enrichirDocument(StorageDocument document, String uuid);

   /**
    * Ajout de données au document virtuel avant stockage
    * 
    * @param document
    *           Le document à enrichir
    * @param uuid
    *           identifiant unique à ajouter
    * @return Le document avec les informations ajoutées
    */
   VirtualStorageDocument enrichirVirtualDocument(
         VirtualStorageDocument document, String uuid);
}
