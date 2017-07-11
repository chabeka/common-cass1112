package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;

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
    * @throws InsertionIdGedExistantEx
    *            Exception levée lorsqu'un IdGed existe déjà à l'insertion
    */
   StorageDocument insertStorageDocument(StorageDocument storageDocument)
         throws InsertionServiceEx, InsertionIdGedExistantEx;

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
    * @throws InsertionIdGedExistantEx
    *            Exception levée lorsqu'un IdGed existe déjà à l'insertion
    */
   StorageDocument insertBinaryStorageDocument(StorageDocument storageDoc)
         throws InsertionServiceEx, InsertionIdGedExistantEx;

   /**
    * Réalise l'appel afin d'insérer le fichier de référence pour des documents
    * virtuels
    * 
    * @param reference
    *           référence pour les documents virtuels
    * @return le fichier de référence inséré
    * @throws InsertionServiceEx
    *            Une erreur s'est produite lors de l'insertion du fichier de
    *            référence
    */
   StorageReferenceFile insertStorageReference(VirtualStorageReference reference)
         throws InsertionServiceEx;

   /**
    * Réalise l'appel afin d'insérer le document virtuel
    * 
    * @param document
    *           le document virtuel à archiver
    * @return l'identifiant unique de l'archive
    * @throws InsertionServiceEx
    *            Une erreur s'est produite lors de l'insertion du document
    *            virtuel
    */
   UUID insertVirtualStorageDocument(VirtualStorageDocument document)
         throws InsertionServiceEx;
}
