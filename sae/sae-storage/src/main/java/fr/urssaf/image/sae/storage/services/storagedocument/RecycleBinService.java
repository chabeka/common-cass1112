package fr.urssaf.image.sae.storage.services.storagedocument;

import java.io.IOException;
import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit les services de gestion de la corbeille.<BR />
 * Ce services est :
 * <ul>
 * <li>moveStorageDocumentToRecycleBin : service qui permet de mettre un 
 * StorageDocument dans la corbeille à partir de l'UUIDCriteria.</li>
 * <li>restoreStorageDocumentFromRecycleBin : service qui permet de restaurer un 
 * StorageDocument de la corbeille à partir de l'UUIDCriteria.</li>
 * <li>deleteStorageDocument : service qui permet de supprimer un
 * StorageDocument de la corbeille à partir de l'UUIDCriteria.</li>
 * </ul>
 */
public interface RecycleBinService {

   /**
    * Permet de deplacer un StorageDocument dans la corbeille à partir du critère « UUIDCriteria
    * ».
    * 
    * @param uuid
    *           : L'identifiant unique du document
    * 
    * 
    * 
    * @throws RecycleBinServiceEx
    *            Runtime exception
    */

   void moveStorageDocumentToRecycleBin(final UUID uuid) throws RecycleBinServiceEx;

   /**
    * Permet de restaurer un StorageDocument de la corbeille à partir du critère « UUIDCriteria
    * ».
    * 
    * @param uuid
    *           : L'identifiant unique du document
    * 
    * 
    * 
    * @throws RecycleBinServiceEx
    *            Runtime exception
    */

   void restoreStorageDocumentFromRecycleBin(final UUID uuid) throws RecycleBinServiceEx;

   /**
    * Permet de supprimer un StorageDocument de la corbeille à partir du critère « UUIDCriteria
    * ».
    * 
    * @param uuid
    *           : L'identifiant unique du document
    * 
    * 
    * 
    * @throws RecycleBinServiceEx
    *            Runtime exception
    */
   void deleteStorageDocumentFromRecycleBin(final UUID uuid) throws RecycleBinServiceEx;

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link RecycleBinService}
    */
   <T> void setRecycleBinServiceParameter(T parameter);

   /**
    * Permet de récupérer un StorageDocument de la corbeille à partir du critère « UUIDCriteria
    * * ».
    * @param uuidCriteria
    * @return
    * @throws IOException 
    * @throws StorageException 
    */
   StorageDocument getStorageDocumentFromRecycleBin(UUIDCriteria uuidCriteria)
         throws StorageException, IOException;

   /**
    * Permet de consulter un StorageDocument de la corbeille à partir du critère
    * « UUIDCriteria * ».
    * 
    * @param uuidCriteria
    * @param forConsultion
    * @return
    * @throws IOException
    * @throws StorageException
    */
   StorageDocument getStorageDocumentFromRecycleBin(UUIDCriteria uuidCriteria,
         boolean forConsultion) throws StorageException, IOException;
}
