package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.FacadePattern;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServiceProvider;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.DocumentAttachmentService;
import fr.urssaf.image.sae.storage.services.storagedocument.DocumentNoteService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.storage.services.storagedocument.UpdateService;

/**
 * Façade des services de gestions de StorageDocument :
 * <ul>
 * <li>{@link InsertionServiceImpl}</li>
 * <li>{@link RetrievalServiceImpl}</li>
 * <li>{@link SearchingServiceImpl}</li>
 * <li>{@link DeletionServiceImpl}</li>
 * </ul>
 * 
 */
@Service("storageDocumentService")
@FacadePattern(participants = { InsertionServiceImpl.class,
      RetrievalServiceImpl.class, SearchingServiceImpl.class,
      DeletionServiceImpl.class }, comment = "Fournit les services des classes participantes")
public class StorageDocumentServiceImpl extends AbstractServiceProvider
      implements StorageDocumentService {
   @Autowired
   @Qualifier("insertionService")
   private InsertionService insertionService;
   @Autowired
   @Qualifier("searchingService")
   private SearchingService searchingService;
   @Autowired
   @Qualifier("retrievalService")
   private RetrievalService retrievalService;
   @Autowired
   @Qualifier("deletionService")
   private DeletionService deletionService;
   @Autowired
   private UpdateService updateService;
   @Autowired
   @Qualifier("documentNoteService")
   private DocumentNoteService documentNoteService;
   @Autowired
   @Qualifier("documentAttachmentService")
   private DocumentAttachmentService documentAttachmentService;

   /**
    * @return les services de suppression
    */
   public final DeletionService getDeletionService() {
      return deletionService;
   }

   /**
    * @param deletionService
    *           : les services de suppression
    */
   public final void setDeletionService(final DeletionService deletionService) {
      this.deletionService = deletionService;
   }

   /**
    * {@inheritDoc}
    */

   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {
      insertionService.setInsertionServiceParameter(getDfceService());
      return insertionService.insertStorageDocument(storageDocument);
   }

   /**
    * {@inheritDoc}
    */
   public final StorageDocument insertBinaryStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {
      insertionService.setInsertionServiceParameter(getDfceService());
      return insertionService.insertBinaryStorageDocument(storageDocument);
   }

   /**
    * 
    * @return Les services d'insertions
    */
   public final InsertionService getInsertionService() {
      return insertionService;
   }

   /**
    * 
    * @return les services de recherche
    */
   public final SearchingService getSearchingService() {
      return searchingService;
   }

   /**
    * 
    * @return les services de récupération
    */
   public final RetrievalService getRetrievalService() {
      return retrievalService;
   }

   /**
    * {@inheritDoc}
    */

   public final StorageDocuments searchStorageDocumentByLuceneCriteria(
         final LuceneCriteria luceneCriteria) throws SearchingServiceEx,
         QueryParseServiceEx {
      searchingService.setSearchingServiceParameter(getDfceService());
      return searchingService
            .searchStorageDocumentByLuceneCriteria(luceneCriteria);
   }

   /**
    * {@inheritDoc}
    */

   public final StorageDocument searchStorageDocumentByUUIDCriteria(
         final UUIDCriteria uUIDCriteria) throws SearchingServiceEx {
      searchingService.setSearchingServiceParameter(getDfceService());
      return searchingService.searchStorageDocumentByUUIDCriteria(uUIDCriteria);
   }

   /**
    * {@inheritDoc}
    */
   public final StorageDocument retrieveStorageDocumentByUUID(
         final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
      retrievalService.setRetrievalServiceParameter(getDfceService());
      return retrievalService.retrieveStorageDocumentByUUID(uUIDCriteria);
   }

   /**
    * {@inheritDoc}
    */

   public final byte[] retrieveStorageDocumentContentByUUID(
         final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
      retrievalService.setRetrievalServiceParameter(getDfceService());
      return retrievalService
            .retrieveStorageDocumentContentByUUID(uUIDCriteria);
   }

   /**
    * {@inheritDoc}
    */

   public final List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
         final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
      retrievalService.setRetrievalServiceParameter(getDfceService());
      return retrievalService
            .retrieveStorageDocumentMetaDatasByUUID(uUIDCriteria);
   }

   /**
    * Initialise les services d'insertion
    * 
    * @param insertionService
    *           : les services d'insertions
    */
   public final void setInsertionService(final InsertionService insertionService) {
      this.insertionService = insertionService;
   }

   /**
    * Initialise les services de recherche
    * 
    * @param searchingService
    *           : Le service de recherche
    */
   public final void setSearchingService(final SearchingService searchingService) {
      this.searchingService = searchingService;
   }

   /**
    * Initialise les services de récupération
    * 
    * @param retrievalService
    *           : les services de récupération
    */
   public final void setRetrievalService(final RetrievalService retrievalService) {
      this.retrievalService = retrievalService;
   }

   /**
    * {@inheritDoc}
    */
   public final void deleteStorageDocument(final UUID uuid)
         throws DeletionServiceEx {
      deletionService.setDeletionServiceParameter(getDfceService());
      this.deletionService.deleteStorageDocument(uuid);
   }

   /**
    * {@inheritDoc}
    */
   public final StorageDocument searchMetaDatasByUUIDCriteria(
         final UUIDCriteria uuidCriteria) throws SearchingServiceEx {
      searchingService.setSearchingServiceParameter(getDfceService());
      return searchingService.searchMetaDatasByUUIDCriteria(uuidCriteria);
   }

   /**
    * {@inheritDoc}
    */
   public final void rollBack(final String processId) throws DeletionServiceEx {
      deletionService.setDeletionServiceParameter(getDfceService());
      deletionService.rollBack(processId);
   }

   /**
    * {@inheritDoc}
    */
   public final <T> void setStorageDocumentServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);
   }

   @Override
   public final StorageReferenceFile insertStorageReference(
         VirtualStorageReference reference) throws InsertionServiceEx {
      insertionService.setInsertionServiceParameter(getDfceService());
      return insertionService.insertStorageReference(reference);
   }

   @Override
   public final UUID insertVirtualStorageDocument(
         VirtualStorageDocument document) throws InsertionServiceEx {
      insertionService.setInsertionServiceParameter(getDfceService());
      return insertionService.insertVirtualStorageDocument(document);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateStorageDocument(UUID uuid,
         List<StorageMetadata> modifiedMetadatas,
         List<StorageMetadata> deletedMetadatas) throws UpdateServiceEx {
      updateService.setUpdateServiceParameter(getDfceService());
      updateService.updateStorageDocument(uuid, modifiedMetadatas,
            deletedMetadatas);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void deleteStorageDocumentTraceTransfert(final UUID uuid)
         throws DeletionServiceEx {
      deletionService.setDeletionServiceParameter(getDfceService());
      deletionService.deleteStorageDocForTransfert(uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final PaginatedStorageDocuments searchPaginatedStorageDocuments(
         PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx {
      searchingService.setSearchingServiceParameter(getDfceService());
      return searchingService
            .searchPaginatedStorageDocuments(paginatedLuceneCriteria);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addDocumentNote(UUID docUuid, String contenu, String login)
         throws DocumentNoteServiceEx {
      documentNoteService.setDocumentNoteServiceParameter(getDfceService());
      documentNoteService.addDocumentNote(docUuid, contenu, login, null, null);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<StorageDocumentNote> getDocumentsNotes(UUID docUuid) {
      documentNoteService.setDocumentNoteServiceParameter(getDfceService());
      return documentNoteService.getDocumentNotes(docUuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addDocumentAttachment(UUID docUuid, String docName,
         String extension, DataHandler contenu)
         throws StorageDocAttachmentServiceEx {
      documentAttachmentService
            .setDocumentAttachmentServiceParameter(getDfceService());
      documentAttachmentService.addDocumentAttachment(docUuid, docName,
            extension, contenu);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      documentAttachmentService
            .setDocumentAttachmentServiceParameter(getDfceService());
      return documentAttachmentService.getDocumentAttachments(docUuid);
   }

}
