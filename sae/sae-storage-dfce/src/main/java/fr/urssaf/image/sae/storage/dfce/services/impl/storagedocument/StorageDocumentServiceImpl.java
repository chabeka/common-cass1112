package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.FacadePattern;
import fr.urssaf.image.sae.storage.dfce.bo.IndexCompositeComponent;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.DeletionServiceImpl;
import fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.InsertionServiceImpl;
import fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.SearchingServiceImpl;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
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
import fr.urssaf.image.sae.storage.services.storagedocument.RecycleBinService;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.storage.services.storagedocument.UpdateService;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Façade des services de gestions de StorageDocument :
 * <ul>
 * <li>{@link InsertionServiceImpl}</li>
 * <li>{@link RetrievalServiceImpl}</li>
 * <li>{@link SearchingServiceImpl}</li>
 * <li>{@link DeletionServiceImpl}</li>
 * </ul>
 */
@Service("storageDocumentService")
@FacadePattern(participants = {InsertionServiceImpl.class,
                               RetrievalServiceImpl.class, SearchingServiceImpl.class,
                               DeletionServiceImpl.class},
               comment = "Fournit les services des classes participantes")
public class StorageDocumentServiceImpl extends AbstractServices
                                        implements
                                        StorageDocumentService {
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

  @Autowired
  @Qualifier("recycleBinService")
  private RecycleBinService recycleBinService;

  @Autowired
  private IndexCompositeComponent indexCompositeService;

  /**
   * @return les services de suppression
   */
  public final DeletionService getDeletionService() {
    return deletionService;
  }

  /**
   * @param deletionService
   *          : les services de suppression
   */
  public final void setDeletionService(final DeletionService deletionService) {
    this.deletionService = deletionService;
  }

  /**
   * {@inheritDoc}
   *
   * @throws InsertionIdGedExistantEx
   */

  @Override
  public StorageDocument insertStorageDocument(
                                               final StorageDocument storageDocument)
      throws InsertionServiceEx,
      InsertionIdGedExistantEx {
    return insertionService.insertStorageDocument(storageDocument);
  }

  /**
   * {@inheritDoc}
   *
   * @throws InsertionIdGedExistantEx
   */
  @Override
  public StorageDocument insertBinaryStorageDocument(
                                                     final StorageDocument storageDocument)
      throws InsertionServiceEx,
      InsertionIdGedExistantEx {
    return insertionService.insertBinaryStorageDocument(storageDocument);
  }

  /**
   * @return Les services d'insertions
   */
  public final InsertionService getInsertionService() {
    return insertionService;
  }

  /**
   * @return les services de recherche
   */
  public final SearchingService getSearchingService() {
    return searchingService;
  }

  /**
   * @return les services de récupération
   */
  public final RetrievalService getRetrievalService() {
    return retrievalService;
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public StorageDocuments searchStorageDocumentByLuceneCriteria(
                                                                final LuceneCriteria luceneCriteria)
      throws SearchingServiceEx,
      QueryParseServiceEx {
    return searchingService
                           .searchStorageDocumentByLuceneCriteria(luceneCriteria);
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public StorageDocument searchStorageDocumentByUUIDCriteria(
                                                             final UUIDCriteria uUIDCriteria)
      throws SearchingServiceEx {
    return searchingService.searchStorageDocumentByUUIDCriteria(uUIDCriteria,
                                                                false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final StorageDocument retrieveStorageDocumentByUUID(
                                                             final UUIDCriteria uUIDCriteria)
      throws RetrievalServiceEx {
    return retrievalService.retrieveStorageDocumentByUUID(uUIDCriteria);
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public final byte[] retrieveStorageDocumentContentByUUID(
                                                           final UUIDCriteria uUIDCriteria)
      throws RetrievalServiceEx {
    return retrievalService
                           .retrieveStorageDocumentContentByUUID(uUIDCriteria);
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
                                                                      final UUIDCriteria uUIDCriteria)
      throws RetrievalServiceEx {
    return retrievalService
                           .retrieveStorageDocumentMetaDatasByUUID(uUIDCriteria);
  }

  /**
   * Initialise les services d'insertion
   *
   * @param insertionService
   *          : les services d'insertions
   */
  public final void setInsertionService(final InsertionService insertionService) {
    this.insertionService = insertionService;
  }

  /**
   * Initialise les services de recherche
   *
   * @param searchingService
   *          : Le service de recherche
   */
  public final void setSearchingService(final SearchingService searchingService) {
    this.searchingService = searchingService;
  }

  /**
   * Initialise les services de récupération
   *
   * @param retrievalService
   *          : les services de récupération
   */
  public final void setRetrievalService(final RetrievalService retrievalService) {
    this.retrievalService = retrievalService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteStorageDocument(final UUID uuid)
      throws DeletionServiceEx {
    deletionService.deleteStorageDocument(uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageDocument searchMetaDatasByUUIDCriteria(
                                                       final UUIDCriteria uuidCriteria)
      throws SearchingServiceEx {
    return searchingService.searchMetaDatasByUUIDCriteria(uuidCriteria);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollBack(final String processId) throws DeletionServiceEx {
    deletionService.rollBack(processId);
  }

  @Override
  public StorageReferenceFile insertStorageReference(
                                                     final VirtualStorageReference reference)
      throws InsertionServiceEx {
    return insertionService.insertStorageReference(reference);
  }

  @Override
  public UUID insertVirtualStorageDocument(
                                           final VirtualStorageDocument document)
      throws InsertionServiceEx {
    return insertionService.insertVirtualStorageDocument(document);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void updateStorageDocument(final UUID uuid,
                                          final List<StorageMetadata> modifiedMetadatas,
                                          final List<StorageMetadata> deletedMetadatas)
      throws UpdateServiceEx {
    updateService.updateStorageDocument(null,
                                        uuid,
                                        modifiedMetadatas,
                                        deletedMetadatas);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateStorageDocument(final UUID uuidJob, final UUID uuid,
                                    final List<StorageMetadata> modifiedMetadatas,
                                    final List<StorageMetadata> deletedMetadatas)
      throws UpdateServiceEx {
    if (uuidJob == null) {
      updateStorageDocument(uuid, modifiedMetadatas, deletedMetadatas);
    } else {
      updateService.updateStorageDocument(uuidJob,
                                          uuid,
                                          modifiedMetadatas,
                                          deletedMetadatas);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteStorageDocumentTraceTransfert(final UUID uuid)
      throws DeletionServiceEx {
    deletionService.deleteStorageDocForTransfert(uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PaginatedStorageDocuments searchPaginatedStorageDocuments(
                                                                   final PaginatedLuceneCriteria paginatedLuceneCriteria)
      throws SearchingServiceEx, QueryParseServiceEx {
    return searchingService
                           .searchPaginatedStorageDocuments(paginatedLuceneCriteria);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PaginatedStorageDocuments searchPaginatedStorageDocumentsWithBestIndex(final PaginatedLuceneCriteria paginatedLuceneCriteria,
                                                                                final List<String> bestIndex)
      throws SearchingServiceEx, QueryParseServiceEx {
    return searchingService.searchPaginatedStorageDocumentsWithBestIndex(paginatedLuceneCriteria, bestIndex);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addDocumentNote(final UUID docUuid, final String contenu, final String login)
      throws DocumentNoteServiceEx {
    documentNoteService.addDocumentNote(docUuid, contenu, login, null, null);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StorageDocumentNote> getDocumentsNotes(final UUID docUuid) {
    return documentNoteService.getDocumentNotes(docUuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addDocumentAttachment(final UUID docUuid, final String docName,
                                    final String extension, final DataHandler contenu)
      throws StorageDocAttachmentServiceEx {
    documentAttachmentService.addDocumentAttachment(docUuid,
                                                    docName,
                                                    extension,
                                                    contenu);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageDocumentAttachment getDocumentAttachment(final UUID docUuid)
      throws StorageDocAttachmentServiceEx {
    return documentAttachmentService.getDocumentAttachments(docUuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveStorageDocumentToRecycleBin(final UUID uuid)
      throws RecycleBinServiceEx {
    recycleBinService.moveStorageDocumentToRecycleBin(uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void restoreStorageDocumentFromRecycleBin(final UUID uuid)
      throws RecycleBinServiceEx {
    recycleBinService.restoreStorageDocumentFromRecycleBin(uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteStorageDocumentFromRecycleBin(final UUID uuid)
      throws RecycleBinServiceEx {
    recycleBinService.deleteStorageDocumentFromRecycleBin(uuid);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IOException
   * @throws StorageException
   */
  @Override
  public StorageDocument getStorageDocumentFromRecycleBin(final UUIDCriteria uuidCriteria)
      throws StorageException, IOException {
    return recycleBinService.getStorageDocumentFromRecycleBin(uuidCriteria);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PaginatedStorageDocuments searchStorageDocumentsInRecycleBean(
                                                                       final PaginatedLuceneCriteria paginatedLuceneCriteria)
      throws SearchingServiceEx, QueryParseServiceEx {
    return searchingService
                           .searchStorageDocumentsInRecycleBean(paginatedLuceneCriteria);
  }

  /**
   * Setter pour recycleBinService
   *
   * @param recycleBinService
   *          the recycleBinService to set
   */
  public final void setRecycleBinService(final RecycleBinService recycleBinService) {
    this.recycleBinService = recycleBinService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<CompositeIndex> getAllCompositeIndex() {
    return dfceServices.fetchAllCompositeIndex();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<CompositeIndex> getAllIndexComposite() {
    return indexCompositeService.getIndexCompositeList();
  }

}
