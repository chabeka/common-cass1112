package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;

/**
 * Fournit les services de suppression document.<BR />
 * Ce services est :
 * <ul>
 * <li>deleteStorageDocument : service qui permet de supprimer un
 * StorageDocument à partir de l'UUIDCriteria.</li>
 * </ul>
 */
public interface DeletionService {

   /**
    * Permet de supprimer un StorageDocument à partir du critère « UUIDCriteria
    * ».
    * 
    * @param uuid
    *           : L'identifiant unique du document
    * 
    * 
    * 
    * @throws DeletionServiceEx
    *            Runtime exception
    */

   void deleteStorageDocument(final UUID uuid) throws DeletionServiceEx;

   /**
    * Permet de faire un rollback à partir d'un identifiant de traitement.
    * 
    * 
    * @param processId
    *           : L'identifiant du traitement
    * 
    * 
    * 
    * @throws DeletionServiceEx
    *            Runtime exception
    */

   void rollBack(final String processId) throws DeletionServiceEx;

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link DeletionService}
    */
   <T> void setDeletionServiceParameter(T parameter);

   /**
    * Réalise suppresion d'un StorageDocument, suite à un transfert,
    * à partir du critère UUID.
    * 
    * @param uuid
    *           identifiant unique du document
    *           
    * @throws DeletionServiceEx
    *            Exception levée en cas d'erreur de suppression de l'archive
    */
   void deleteStorageDocForTransfert(UUID uuid) throws DeletionServiceEx;
   
}
