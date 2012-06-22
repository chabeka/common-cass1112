package fr.urssaf.image.sae.storage.services.storagedocument;

import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Fournit les services d’insertion de document.<BR />
 * Ces services sont :
 * <ul>
 * <li>insertStorageDocument : service qui permet d'insérer un document unique.</li>
 * </ul>
 * 
 */
public interface InsertionService {

   /**
    * Permet d'insérer un document unique
    * 
    * @param storageDocument
    *           : Le document à stocker
    * 
    * @return Le document
    * 
    * @throws InsertionServiceEx
    *            Exception lévée lorsque l'insertion d'un document ne se déroule
    *            pas bien.
    */
   StorageDocument insertStorageDocument(StorageDocument storageDocument)
         throws InsertionServiceEx;

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link InsertionService}
    */
   <T> void setInsertionServiceParameter(T parameter);

   /**
    * Permet d'insérer en base un document unique fourni sous forme de contenu
    * binaire
    * 
    * @param storageDoc
    *           le storageDocument a persister
    * 
    * @return le document persisté
    * @throws InsertionServiceEx
    *            levée lorsque l'insertion d'un document ne se déroule pas bien
    */
   StorageDocument insertBinaryStorageDocument(StorageDocument storageDoc)
         throws InsertionServiceEx;
}
