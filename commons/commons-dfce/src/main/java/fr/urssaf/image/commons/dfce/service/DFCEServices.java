package fr.urssaf.image.commons.dfce.service;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.docubase.dfce.commons.document.StoreOptions;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.NoSuchAttachmentException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.exception.DFCEConnectionServiceException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Attachment;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.note.Note;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.model.recordmanager.RMLogArchiveReport;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;

/**
 * Classe exposant l'ensemble des services de DFCE, et gérant la connexion/reconnexion vers DFCE
 * en cas d'exception.
 * Une instance de DFCEServices est thread-safe : elle peut être partagée par plusieurs threads. La reconnexion
 * est gérée de manière à ne pas perturber les autres threads.
 * Dans l'application SAE : sur les plateformes GNT, on gère deux instance de cette classe :
 *   - une instance vers le serveur DFCE GNT local
 *   - une instance vers un serveur DFCE de la plateforme GNS associée, pour le transfert
 *
 */
public interface DFCEServices {
   /**
    * Permet de se connecter si ce n'est pas déjà fait
    */
   void connectTheFistTime() throws DFCEConnectionServiceException;

   /**
    * Méthode thread safe permettant de se reconnecter et cas de déconnexion. Méthode appelée automatiquement : ne
    * pas appeler directement cette méthode, sauf pour des tests.
    */
   void reconnect() throws DFCEConnectionServiceException;
   /**
    * Permet de fermer la connexion vers DFCE. Ne pas utiliser si la connexion est partagée par plusieurs threads !
    */
   void closeConnexion();
   /**
    * Renvoie les paramètres de connexions vers DFCE
    */
   DFCEConnection getCnxParams();
   /**
    * Renvoie la base DFCE courante (spécifiée dans les paramètres de connexion)
    */

   Base getBase();
   /**
    * Récupère un document par son uuid sur la base courante
    * @see net.docubase.toolkit.service.ged.SearchService#getDocumentByUUID
    */
   Document getDocumentByUUID(final UUID uuid);
   /**
    * @see net.docubase.toolkit.service.ged.SearchService#getDocumentByUUID(Base, UUID)
    */
   Document getDocumentByUUID(final Base base, final UUID uuid);
   /**
    * Récupère un document de la corbeille par son uuid sur la base courante
    * @see net.docubase.toolkit.service.ged.RecycleBinService#getDocumentByUUID(Base, UUID)
    */
   Document getDocumentByUUIDFromRecycleBin(final UUID uuid);
   /**
    * @see net.docubase.toolkit.service.ged.SearchService#search(SearchQuery)
    */
   SearchResult search(SearchQuery searchQuery) throws ExceededSearchLimitException, SearchQueryParseException;
   /**
    * @see net.docubase.toolkit.service.administration.BaseAdministrationService#updateBase(Base)
    */
   Base updateBase(Base base);
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#findOrCreateCategory(String, CategoryDataType)
    */
   Category findOrCreateCategory(String metadataCode, CategoryDataType metadataType);
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#addAttachment(UUID, String, String, boolean, String, InputStream)
    */
   Document addAttachment(UUID paramUUID, String paramString1, String paramString2, boolean paramBoolean, String paramString3, InputStream paramInputStream)
         throws FrozenDocumentException, TagControlException;
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#getAllLifeCycleRules()
    */
   Set<LifeCycleRule> getAllLifeCycleRules();
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#getAttachmentFile(Document, Attachment)
    */
   InputStream getAttachmentFile(Document paramDocument, Attachment paramAttachment) throws NoSuchAttachmentException;
   /**
    * @see net.docubase.toolkit.service.ged.RecycleBinService#throwAwayDocument(UUID)
    */
   Document throwAwayDocument(UUID paramUUID) throws FrozenDocumentException;
   /**
    * @see net.docubase.toolkit.service.ged.RecycleBinService#restoreDocument(UUID)
    */
   Document restoreDocument(UUID paramUUID) throws TagControlException;
   /**
    * @see net.docubase.toolkit.service.ged.NoteService#getNotes(UUID)
    */
   List<Note> getNotes(UUID paramUUID);
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#storeDocument(Document, InputStream)
    */
   Document storeDocument(Document paramDocument, InputStream paramInputStream) throws TagControlException;
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#storeDocument(Document, StoreOptions, byte[], InputStream)
    */
   Document storeDocument(Document paramDocument, StoreOptions paramStoreOptions, byte[] paramArrayOfByte, InputStream paramInputStream) throws TagControlException;
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#storeDocument(Document, String, String, InputStream)
    */
   Document storeDocument(Document paramDocument, String paramString1, String paramString2, InputStream paramInputStream) throws TagControlException;
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#getLifeCycleRule(String)
    */
   LifeCycleRule getLifeCycleRule(String paramString);
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#getDocumentFile(Document)
    */
   InputStream getDocumentFile(Document paramDocument);
   /**
    * Récupère le fichier d'un document dans la corbeille, par son uuid sur la base courante
    * @see net.docubase.toolkit.service.ged.RecycleBinService#getDocumentByUUID(Base, UUID)
    */
   InputStream getDocumentFileFromRecycleBin(Document paramDocument);
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#isFrozen(Document)
    */
   boolean isFrozen(Document paramDocument);
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#deleteDocument(UUID)
    */
   void deleteDocument(UUID paramUUID) throws FrozenDocumentException;
   /**
    * Supprime un document se trouvant dans la corbeille, par son uuid
    * @see net.docubase.toolkit.service.ged.RecycleBinService#deleteDocument(UUID)
    */
   void deleteDocumentFromRecycleBin(UUID paramUUID) throws FrozenDocumentException;
   /**
    * @see net.docubase.toolkit.service.ged.NoteService#storeNote(Note)
    */
   Note storeNote(Note paramNote) throws FrozenDocumentException, TagControlException;
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#createFileReference(String, String, InputStream)
    */
   FileReference createFileReference(String paramString1, String paramString2, InputStream paramInputStream);
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#storeVirtualDocument(Document, FileReference, int, int)
    */
   Document storeVirtualDocument(Document paramDocument, FileReference paramFileReference, int paramInt1, int paramInt2)
         throws TagControlException;
   /**
    * @see net.docubase.toolkit.service.ged.SearchService#createDocumentIterator(SearchQuery)
    */
   Iterator<Document> createDocumentIterator(SearchQuery paramSearchQuery)
         throws SearchQueryParseException;
   /**
    * @see net.docubase.toolkit.service.ged.RecycleBinService#createDocumentIterator(SearchQuery)
    */
   Iterator<Document> createDocumentIteratorFromRecycleBin(SearchQuery paramSearchQuery)
         throws SearchQueryParseException;
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#updateDocument(Document)
    */
   Document updateDocument(Document paramDocument)
         throws TagControlException, FrozenDocumentException;
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#updateDocumentType(Document, String)
    */
   void updateDocumentType(Document paramDocument, String paramString)
         throws FrozenDocumentException;
   /**
    * @see net.docubase.toolkit.service.ged.StoreService#updateDocumentLifeCycleReferenceDate(Document, Date)
    */
   Document updateDocumentLifeCycleReferenceDate(Document paramDocument, Date paramDate)
         throws FrozenDocumentException;
   /**
    * @see net.docubase.toolkit.service.ged.RecordManagerService#createCustomSystemEventLog(RMSystemEvent)
    */
   RMSystemEvent createCustomSystemEventLog(RMSystemEvent paramRMSystemEvent);
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#fetchAllCompositeIndex()
    */
   Set<CompositeIndex> fetchAllCompositeIndex();
   /**
    * @deprecated
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#getCategory(String)
    */
   @Deprecated
   Category getCategory(String paramString);
   /**
    * @deprecated
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#findOrCreateCompositeIndex(Category...)
    */
   @Deprecated
   CompositeIndex findOrCreateCompositeIndex(Category... paramVarArgs);
   /**
    * @see net.docubase.toolkit.service.ged.RecordManagerService#createCustomDocumentEventLog(RMDocEvent)
    */
   RMDocEvent createCustomDocumentEventLog(RMDocEvent paramRMDocEvent);
   /**
    * @see net.docubase.toolkit.service.ged.RecordManagerService#getDocumentEventLogsByDates(Date, Date)
    */
   List<RMDocEvent> getDocumentEventLogsByDates(Date paramDate1, Date paramDate2);
   /**
    * @see net.docubase.toolkit.service.ged.RecordManagerService#getDocumentEventLogsByUUID(UUID)
    */
   List<RMDocEvent> getDocumentEventLogsByUUID(UUID paramUUID);
   /**
    * @see net.docubase.toolkit.service.ged.RecordManagerService#getSystemEventLogsByDates(Date, Date)
    */
   List<RMSystemEvent> getSystemEventLogsByDates(Date paramDate1, Date paramDate2);
   /**
    * @see net.docubase.toolkit.service.ged.ArchiveService#getLogsArchiveBase()
    */
   Base getLogsArchiveBase();
   /**
    * @see net.docubase.toolkit.service.ged.ArchiveService#createSystemLogArchiveChainingReport(Date, Date)
    */
   List<RMLogArchiveReport> createSystemLogArchiveChainingReport(Date paramDate1, Date paramDate2);
   /**
    * @see net.docubase.toolkit.service.ged.ArchiveService#createDocumentLogArchiveChainingReport(Date, Date)
    */
   List<RMLogArchiveReport> createDocumentLogArchiveChainingReport(Date paramDate1, Date paramDate2);
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#createNewLifeCycleRule(LifeCycleRule)
    */
   LifeCycleRule createNewLifeCycleRule(LifeCycleRule paramLifeCycleRule) throws ObjectAlreadyExistsException;
   /**
    * @see net.docubase.toolkit.service.administration.StorageAdministrationService#updateLifeCycleRule(LifeCycleRule)
    */
   LifeCycleRule updateLifeCycleRule(LifeCycleRule paramLifeCycleRule);
   /**
    * @see net.docubase.toolkit.service.ged.NoteService#addNote(UUID, String)
    */
   Note addNote(UUID paramUUID, String paramString) throws FrozenDocumentException, TagControlException;
   /**
    * @see net.docubase.toolkit.service.ServiceProvider#isServerUp()
    */
   boolean isServerUp();
}
