package fr.urssaf.image.commons.dfce.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.commons.document.StoreOptions;
import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.NoSuchAttachmentException;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.exception.batch.DfceJobParametersInvalidException;
import com.docubase.dfce.exception.batch.UnexpectedDfceJobExecutionException;
import com.docubase.dfce.exception.batch.launch.DfceJobInstanceAlreadyExistsException;
import com.docubase.dfce.exception.batch.launch.DfceJobParametersNotFoundException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobException;
import com.docubase.dfce.exception.batch.launch.NoSuchDfceJobExecutionException;
import com.docubase.dfce.exception.batch.repository.DfceJobExecutionAlreadyRunningException;
import com.docubase.dfce.exception.batch.repository.DfceJobInstanceAlreadyCompleteException;
import com.docubase.dfce.exception.batch.repository.DfceJobRestartException;

import fr.urssaf.image.commons.dfce.exception.DFCEConnectionServiceException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Attachment;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.index.IndexInformation;
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
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;

/**
 * {@inheritDoc}
 */
@Component
public class DFCEServicesImpl implements DFCEServices {

   /**
    * LOGGER
    */
   private static final Logger LOG = LoggerFactory.getLogger(DFCEServicesImpl.class);

   private final DFCEConnection dfceConnection;

   private ServiceProvider dfceService;

   /**
    * Correspond à la base DFCE qui nous intéresse
    * Cette base est mise en cache à chaque reconnexion
    */
   private Base base;

   /**
    * Constructeur.
    * 
    * @param dfceConnectionParameters
    *           Les paramètres de connexions à DFCE
    */
   @Autowired
   public DFCEServicesImpl(final DFCEConnection dfceConnectionParameters) {
      this.dfceConnection = dfceConnectionParameters;
   }

   /**
    * Méthode permettant de se connecter à DFCE.
    * On met également en cache l'objet "base" pour éviter de faire appel au serveur DFCE à chaque
    * fois qu'on en a besoin
    */
   private void connect() {
      dfceService = ServiceProvider.newServiceProvider();
      final String serverUrl = ObjectUtils.toString(this.dfceConnection.getServerUrl());
      dfceService.connect(this.dfceConnection.getLogin(),
                          this.dfceConnection.getPassword(),
                          serverUrl,
                          this.dfceConnection.getTimeout());

      base = dfceService.getBaseAdministrationService().getBase(dfceConnection.getBaseName());
      if (base == null) {
         throw new DFCEConnectionServiceException("Base " + dfceConnection.getBaseName() +
               " non trouvée sur le serveur " + serverUrl);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void connectTheFistTime() throws DFCEConnectionServiceException {
      if (dfceService == null) {
         reconnect();
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void reconnect() throws DFCEConnectionServiceException {
      synchronized (this) {
         if (dfceService == null || !dfceService.isSessionActive()) {
            final int maxTentatives = dfceConnection.getNbtentativecnx();
            openConnectionDFCe(1, maxTentatives);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void closeConnexion() {
      if (dfceService != null) {
         final String LOG_PREFIX = "closeConnexion";
         LOG.debug("{} - Fermeture connexion à DFCE (url : {})", new Object[] {LOG_PREFIX, dfceConnection.getServerUrl()});
         try {
            dfceService.disconnect();
         }
         catch (final Exception e) {
            LOG.debug("{} - La fermeture de la connexion à DFCE (url : {}) a provoqué une erreur : {}",
                      new Object[] {LOG_PREFIX, dfceConnection.getServerUrl(), e.getMessage()});
         }
         dfceService = null;
      }
   }

   /**
    * Méthode permettant d'ouvrir une connexion vers DFCE. On essaye plusieurs fois.
    *
    * @param currentTentative
    *           Numéro de la tentative courante
    * @param maxTentatives
    *           Nombre max de tentatives
    * @throws Throwable
    *            Exception de connection
    */
   private void openConnectionDFCe(final int currentTentative, final int maxTentatives) throws DFCEConnectionServiceException {
      final String LOG_PREFIX = "openConnectionDFCe";

      try {
         LOG.debug("{} - Tentative n°{}/{} de connexion à DFCE (url : {})",
                   new Object[] {LOG_PREFIX, currentTentative, maxTentatives, dfceConnection.getServerUrl()});
         connect();
         LOG.debug("{} - Réussite de la tentative n°{}/{} de connexion à DFCE", new Object[] {LOG_PREFIX, currentTentative, maxTentatives});
         LOG.info("{} - Connexion aux services DFCe réussie (url : {})", new Object[] {LOG_PREFIX, dfceConnection.getServerUrl()});
      }
      catch (final Throwable connex) {
         LOG.warn("{} - Echec de la tentative n°{}/{} de connexion à DFCE ", new Object[] {LOG_PREFIX, currentTentative, maxTentatives});
         if (currentTentative < maxTentatives) {
            // On retente...
            openConnectionDFCe(currentTentative + 1, maxTentatives);
         } else {
            // On abandonne
            LOG.error("{} - Le nombre max de tentatives de connexion à DFCE est atteint {}/{} (url : {} - erreur : {})",
                      new Object[] {LOG_PREFIX, currentTentative, maxTentatives, dfceConnection.getServerUrl(),
                                    connex.getMessage()});
            throw new DFCEConnectionServiceException(connex);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public DFCEConnection getCnxParams() {
      return dfceConnection;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Base getBase() {
      // On n'appelle pas DFCE à chaque appel à getBase : on renvoie l'instance mise en cache
      // L'instance est mise en cache au moment de la connexion. On s'assure donc d'avoir initié la connexion.
      connectTheFistTime();
      return base;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document getDocumentByUUID(final UUID uuid) {
      return dfceService.getSearchService().getDocumentByUUID(base, uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document getDocumentByUUID(final Base base, final UUID uuid) {
      return dfceService.getSearchService().getDocumentByUUID(base, uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document getDocumentByUUIDFromRecycleBin(final UUID uuid) {
      return dfceService.getRecycleBinService().getDocumentByUUID(base, uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public SearchResult search(final SearchQuery searchQuery) throws ExceededSearchLimitException, SearchQueryParseException {
      final SearchService searchService = dfceService.getSearchService();
      return searchService.search(searchQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Base updateBase(final Base base) {
      return dfceService.getBaseAdministrationService().updateBase(base);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Category findOrCreateCategory(final String metadataCode, final CategoryDataType metadataType) {
      return dfceService.getStorageAdministrationService().findOrCreateCategory(metadataCode, metadataType);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document addAttachment(final UUID paramUUID, final String paramString1, final String paramString2, final boolean paramBoolean,
                                 final String paramString3, final InputStream paramInputStream)
         throws FrozenDocumentException, TagControlException {
      return dfceService.getStoreService().addAttachment(paramUUID, paramString1, paramString2, paramBoolean, paramString3, paramInputStream);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Set<LifeCycleRule> getAllLifeCycleRules() {
      return dfceService.getStorageAdministrationService().getAllLifeCycleRules();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public InputStream getAttachmentFile(final Document paramDocument, final Attachment paramAttachment) throws NoSuchAttachmentException {
      return dfceService.getStoreService().getAttachmentFile(paramDocument, paramAttachment);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document throwAwayDocument(final UUID paramUUID) throws FrozenDocumentException {
      return dfceService.getRecycleBinService().throwAwayDocument(paramUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document restoreDocument(final UUID paramUUID) throws TagControlException {
      return dfceService.getRecycleBinService().restoreDocument(paramUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<Note> getNotes(final UUID paramUUID) {
      return dfceService.getNoteService().getNotes(paramUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document storeDocument(final Document paramDocument, final InputStream paramInputStream) throws TagControlException {
      return dfceService.getStoreService().storeDocument(paramDocument, paramInputStream);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document storeDocument(final Document paramDocument, final StoreOptions paramStoreOptions, final byte[] paramArrayOfByte,
                                 final InputStream paramInputStream)
         throws TagControlException {
      return dfceService.getStoreService().storeDocument(paramDocument, paramStoreOptions, paramArrayOfByte, paramInputStream);
   }

   @Deprecated
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document storeDocument(final Document paramDocument, final String paramString1, final String paramString2, final InputStream paramInputStream)
         throws TagControlException {
      return dfceService.getStoreService().storeDocument(paramDocument, paramString1, paramString2, paramInputStream);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public LifeCycleRule getLifeCycleRule(final String paramString) {
      return dfceService.getStorageAdministrationService().getLifeCycleRule(paramString);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public InputStream getDocumentFile(final Document paramDocument) {
      return dfceService.getStoreService().getDocumentFile(paramDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public InputStream getDocumentFileFromRecycleBin(final Document paramDocument) {
      return dfceService.getRecycleBinService().getDocumentFile(paramDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public boolean isFrozen(final Document paramDocument) {
      return dfceService.getStoreService().isFrozen(paramDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public void freezeDocument(final Document paramDocument) {
      dfceService.getStoreService().freezeDocument(paramDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public void unfreezeDocument(final Document paramDocument) {
      dfceService.getStoreService().unfreezeDocument(paramDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public void deleteDocument(final UUID paramUUID) throws FrozenDocumentException {
      dfceService.getStoreService().deleteDocument(paramUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public void deleteDocumentFromRecycleBin(final UUID paramUUID) throws FrozenDocumentException {
      dfceService.getRecycleBinService().deleteDocument(paramUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Note storeNote(final Note paramNote) throws FrozenDocumentException, TagControlException {
      return dfceService.getNoteService().storeNote(paramNote);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public FileReference createFileReference(final String paramString1, final String paramString2, final InputStream paramInputStream) {
      return dfceService.getStorageAdministrationService().createFileReference(paramString1, paramString2, paramInputStream);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document storeVirtualDocument(final Document paramDocument, final FileReference paramFileReference, final int paramInt1, final int paramInt2)
         throws TagControlException {
      return dfceService.getStoreService().storeVirtualDocument(paramDocument, paramFileReference, paramInt1, paramInt2);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Iterator<Document> createDocumentIterator(final SearchQuery paramSearchQuery) throws SearchQueryParseException {
      return dfceService.getSearchService().createDocumentIterator(paramSearchQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Iterator<Document> createDocumentIteratorFromRecycleBin(final SearchQuery paramSearchQuery) throws SearchQueryParseException {
      return dfceService.getRecycleBinService().createDocumentIterator(paramSearchQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document updateDocument(final Document paramDocument)
         throws TagControlException, FrozenDocumentException {
      return dfceService.getStoreService().updateDocument(paramDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public void updateDocumentType(final Document paramDocument, final String paramString)
         throws FrozenDocumentException {
      dfceService.getStoreService().updateDocumentType(paramDocument, paramString);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Document updateDocumentLifeCycleReferenceDate(final Document paramDocument, final Date paramDate)
         throws FrozenDocumentException {
      return dfceService.getStoreService().updateDocumentLifeCycleReferenceDate(paramDocument, paramDate);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public RMSystemEvent createCustomSystemEventLog(final RMSystemEvent paramRMSystemEvent) {
      return dfceService.getRecordManagerService().createCustomSystemEventLog(paramRMSystemEvent);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Set<CompositeIndex> fetchAllCompositeIndex() {
      return dfceService.getStorageAdministrationService().fetchAllCompositeIndex();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Category getCategory(final String paramString) {
      return dfceService.getStorageAdministrationService().getCategory(paramString);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public CompositeIndex findOrCreateCompositeIndex(final Category... paramVarArgs) {
      return dfceService.getStorageAdministrationService().findOrCreateCompositeIndex(paramVarArgs);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public RMDocEvent createCustomDocumentEventLog(final RMDocEvent paramRMDocEvent) {
      return dfceService.getRecordManagerService().createCustomDocumentEventLog(paramRMDocEvent);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<RMDocEvent> getDocumentEventLogsByDates(final Date paramDate1, final Date paramDate2) {
      return dfceService.getRecordManagerService().getDocumentEventLogsByDates(paramDate1, paramDate2);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<RMDocEvent> getDocumentEventLogsByUUID(final UUID paramUUID) {
      return dfceService.getRecordManagerService().getDocumentEventLogsByUUID(paramUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<RMSystemEvent> getSystemEventLogsByDates(final Date paramDate1, final Date paramDate2) {
      return dfceService.getRecordManagerService().getSystemEventLogsByDates(paramDate1, paramDate2);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Base getLogsArchiveBase() {
      return dfceService.getArchiveService().getLogsArchiveBase();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<RMLogArchiveReport> createSystemLogArchiveChainingReport(final Date paramDate1, final Date paramDate2) {
      return dfceService.getArchiveService().createSystemLogArchiveChainingReport(paramDate1, paramDate2);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<RMLogArchiveReport> createDocumentLogArchiveChainingReport(final Date paramDate1, final Date paramDate2) {
      return dfceService.getArchiveService().createDocumentLogArchiveChainingReport(paramDate1, paramDate2);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public LifeCycleRule createNewLifeCycleRule(final LifeCycleRule paramLifeCycleRule) throws ObjectAlreadyExistsException {
      return dfceService.getStorageAdministrationService().createNewLifeCycleRule(paramLifeCycleRule);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public LifeCycleRule updateLifeCycleRule(final LifeCycleRule paramLifeCycleRule) {
      return dfceService.getStorageAdministrationService().updateLifeCycleRule(paramLifeCycleRule);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Note addNote(final UUID paramUUID, final String paramString) throws FrozenDocumentException, TagControlException {
      return dfceService.getNoteService().addNote(paramUUID, paramString);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public boolean isServerUp() {
      // Ce service ne renvoie pas d'exception si DFCE est déconnecté.
      boolean result = dfceService.isServerUp();
      if (result == false) {
         // On se reconnecte et on retente
         reconnect();
         result = dfceService.isServerUp();
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Long startNextInstance(final String jobName)
         throws NoSuchDfceJobException, DfceJobParametersNotFoundException, DfceJobRestartException, DfceJobExecutionAlreadyRunningException,
         DfceJobInstanceAlreadyCompleteException, UnexpectedDfceJobExecutionException, DfceJobParametersInvalidException {
      return dfceService.getJobAdministrationService().startNextInstance(jobName);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Long start(final String jobName, final String parameters)
         throws NoSuchDfceJobException, DfceJobInstanceAlreadyExistsException, DfceJobParametersInvalidException {
      return dfceService.getJobAdministrationService().start(jobName, parameters);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<IndexInformation> getIndexesOverLimitInBase(final Integer limit, final UUID baseUUID) {
      return dfceService.getIndexAdministrationService().getIndexesOverLimitInBase(limit, baseUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public List<IndexInformation> getIndexesInBase(final UUID baseUUID) {
      return dfceService.getIndexAdministrationService().getIndexesInBase(baseUUID);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Map<String, String> getSummaryAsMap(final long executionId) throws NoSuchDfceJobExecutionException {
      return dfceService.getJobAdministrationService().getSummaryAsMap(executionId);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Long restart(final long executionId) throws DfceJobInstanceAlreadyCompleteException, NoSuchDfceJobExecutionException, NoSuchDfceJobException,
         DfceJobRestartException, DfceJobParametersInvalidException {
      return dfceService.getJobAdministrationService().restart(executionId);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public boolean isDocumentLogsArchiveRunning() {
      return dfceService.getArchiveService().isDocumentLogsArchiveRunning();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public UUID getLastDocumentLogsArchiveUUID() {
      return dfceService.getArchiveService().getLastDocumentLogsArchiveUUID();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Date getLastSucessfulDocumentLogsArchiveRunDate() {
      return dfceService.getArchiveService().getLastSucessfulDocumentLogsArchiveRunDate();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public UUID getLastSystemLogsArchiveUUID() {
      return dfceService.getArchiveService().getLastSystemLogsArchiveUUID();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public Date getLastSucessfulSystemLogsArchiveRunDate() {
      return dfceService.getArchiveService().getLastSucessfulSystemLogsArchiveRunDate();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @AutoReconnectDfceServiceAnnotation
   public boolean isSystemLogsArchiveRunning() {
      return dfceService.getArchiveService().isSystemLogsArchiveRunning();
   }

}
