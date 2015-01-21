package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit l’ensemble des services : <BR />
 * <ul>
 * <li>
 * {@link InsertionService } : services d’insertion.</li>
 * <li>
 * {@link SearchingService} : services de recherche.</li>
 * <li>
 * {@link RetrievalService} : services de récupération.</li>
 * <li>
 * {@link DeletionService} : services de suppression.</li>
 * </ul>
 */
public interface StorageDocumentService {
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
   StorageDocument insertStorageDocument(final StorageDocument storageDocument)
         throws InsertionServiceEx;

   /**
    * Permet d'insérer un document unique avec piece jointe
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
   StorageDocument insertBinaryStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx;

   /**
    * Permet de faire une recherche des métadonnées par UUID.
    * 
    * @param uuidCriteria
    *           : L'UUID du document à rechercher
    * 
    * @return Le resultat de la recherche
    * 
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    */
   StorageDocument searchMetaDatasByUUIDCriteria(final UUIDCriteria uuidCriteria)
         throws SearchingServiceEx;

   /**
    * Permet de faire une recherche par une requête lucene.
    * 
    * @param luceneCriteria
    *           : La requête Lucene
    * 
    * @return Les résultats de la recherche
    * 
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    * @throws QueryParseServiceEx
    *            Exception levée lorsque du parsing de la requête.
    */
   StorageDocuments searchStorageDocumentByLuceneCriteria(
         final LuceneCriteria luceneCriteria) throws SearchingServiceEx,
         QueryParseServiceEx;

   /**
    * Permet de faire une recherche de document par UUID.
    * 
    * @param uUIDCriteria
    *           : L'UUID du document à rechercher
    * 
    * @return un strorageDocument
    * 
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    */
   StorageDocument searchStorageDocumentByUUIDCriteria(
         final UUIDCriteria uUIDCriteria) throws SearchingServiceEx;

   /**
    * Permet de récupérer un document à partir du critère « UUIDCriteria ».
    * 
    * @param uUIDCriteria
    *           : L'identifiant universel unique du document
    * 
    * @return Le document et ses métadonnées
    * 
    * @throws RetrievalServiceEx
    *            Exception lévée lorsque la consultation ne se déroule pas bien.
    */
   StorageDocument retrieveStorageDocumentByUUID(final UUIDCriteria uUIDCriteria)
         throws RetrievalServiceEx;

   /**
    * Permet de récupérer le contenu d’un document à partir du critère «
    * UUIDCriteria ».
    * 
    * @param uUIDCriteria
    *           : L'identifiant unique du document
    * 
    * @return Le contenu du document
    * 
    * @throws RetrievalServiceEx
    *            Exception lévée lorsque la consultation ne se déroule pas bien
    * 
    */
   byte[] retrieveStorageDocumentContentByUUID(final UUIDCriteria uUIDCriteria)
         throws RetrievalServiceEx;

   /**
    * Permet de récupérer les métadonnées d’un document à partir du critère «
    * UUIDCriteria »
    * 
    * @param uUIDCriteria
    *           : L'identifiant unique du document
    * 
    * @return Une liste de metadonnées
    * 
    * @throws RetrievalServiceEx
    *            Exception lévée lorsque la consultation ne se déroule pas bien
    */
   List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
         final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx;

   /**
    * Permet de supprimer un StorageDocument à partir du critère « UUIDCriteria
    * 
    * 
    * @param uuid
    *           : L'identifiant unique du document
    * 
    * 
    * 
    * @throws DeletionServiceEx
    *            Exception lévée lorsque la suppression ne se réalise pas
    *            correctement
    */
   void deleteStorageDocument(final UUID uuid) throws DeletionServiceEx;

   /**
    * Permet de faire un rollback à partir d'un identifiant de traitement.
    * 
    * 
    * @param processId
    *           : L'identifiant du traitement
    * 
    * @throws DeletionServiceEx
    *            Exception lévée lorsque la suppression ne se réalise pas
    *            correctement
    * 
    */

   void rollBack(final String processId) throws DeletionServiceEx;

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link StorageDocumentService}
    */
   <T> void setStorageDocumentServiceParameter(T parameter);

   /**
    * Insère le fichier de référence pour des documents virtuels virtuels
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
    * Insère le document virtuel
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

   /**
    * Réalise la mise à jour du document dans DFCE
    * 
    * @param uuid
    *           identifiant unique du document
    * @param modifiedMetadatas
    *           Liste des métadonnées à modifier
    * @param deletedMetadatas
    *           Liste des métadonnées à supprimer
    * @throws UpdateServiceEx
    *            Exception levée lorsque la modification du document est en
    *            erreur
    */
   void updateStorageDocument(UUID uuid,
         List<StorageMetadata> modifiedMetadatas,
         List<StorageMetadata> deletedMetadatas) throws UpdateServiceEx;

   /**
    * Réalise suppresion d'un StorageDocument, suite à un transfert, à partir du
    * critère UUID. Idem {@link #deleteStorageDocument(UUID)} en rajoutant une
    * trace correspondant au transfert de document.
    * 
    * @param uuid
    *           identifiant unique du document
    * 
    * @throws DeletionServiceEx
    *            Exception levée en cas d'erreur de suppression de l'archive
    */
   void deleteStorageDocumentTraceTransfert(UUID uuid) throws DeletionServiceEx;

   /**
    * Permet de faire une recherche paginée.
    * 
    * @param paginatedLuceneCriteria
    *           Objet contenant les critères de recherche
    * @return La liste des documents trouvés
    * @throws SearchingServiceEx
    *            Une exception est levée lors de la recherche
    * @throws QueryParseServiceEx
    *            Une exception est levée lors de la recherche
    */
   PaginatedStorageDocuments searchPaginatedStorageDocuments(
         PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx;
}
