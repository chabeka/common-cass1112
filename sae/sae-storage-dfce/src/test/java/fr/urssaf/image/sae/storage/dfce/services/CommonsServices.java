package fr.urssaf.image.sae.storage.dfce.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.xml.XmlDataService;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.services.storagedocument.RecycleBinService;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;
import fr.urssaf.image.sae.storage.services.storagedocument.UpdateService;

/**
 * Classe de base pour les tests unitaires.
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CommonsServices {

   private StorageDocuments storageDocuments;
   private StorageDocument storageDocument;
   private List<StorageMetadata> storageMetadatas;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   @Autowired
   @Qualifier("xmlDataService")
   private XmlDataService xmlDataService;

   @Autowired
   @Qualifier("dfceServicesManager")
   private DFCEServicesManager dfceServicesManager;

   @Autowired
   @Qualifier("insertionService")
   private InsertionService insertionService;
   @Autowired
   @Qualifier("retrievalService")
   private RetrievalService retrievalService;
   @Autowired
   @Qualifier("searchingService")
   private SearchingService searchingService;
   @Autowired
   @Qualifier("deletionService")
   private DeletionService deletionService;
   @Autowired
   @Qualifier("recycleBinService")
   private RecycleBinService recycleBinService;
   @Autowired
   private UpdateService updateService;

   /**
    * @return : Le service d'insertion.
    */
   public final InsertionService getInsertionService() {
      return insertionService;
   }

   /**
    * @return Le service de suppression.
    */
   public final DeletionService getDeletionService() {
      return deletionService;
   }

   /**
    * Ferme la connexion. {@inheritDoc}
    */

   /**
    * @return Le service de récupération de document DFCE.
    */
   public final RetrievalService getRetrievalService() {
      return retrievalService;
   }

   /**
    * @return Le service de recherche.
    */
   public final SearchingService getSearchingService() {
      return searchingService;
   }

   /**
    * 
    * @return Les services DFCE
    */
   public final DFCEServicesManager getDfceServicesManager() {
      return dfceServicesManager;
   }

   /**
    * @return Le service de gestion du fichier xml.
    */
   public final XmlDataService getXmlDataService() {
      return xmlDataService;
   }

   /**
    * Initialisation des tests. <br>{@inheritDoc}
    */
   public final StorageDocument getMockData(
         final InsertionService insertionService) throws IOException,
         ParseException, InsertionServiceEx {
      // Injection de jeu de donnée.
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      return insertionService.insertStorageDocument(storageDocument);
   }

   /**
    * Suppression du jeu de donnée.<br>{@inheritDoc}
    */
   public final void destroyMockTest(final UUID uuid,
         final DeletionService deletionService) throws DeletionServiceEx {
      final List<StorageMetadata> desiredStorageMetadatas = new ArrayList<StorageMetadata>();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(uuid,
            desiredStorageMetadatas);
      deletionService.deleteStorageDocument(uuidCriteria.getUuid());
   }
   
   /**
    * Initialisation des tests. <br>{@inheritDoc}
    */
   public final StorageDocument getMockDataForRecycleBin(
         final InsertionService insertionService, final RecycleBinService recycleBinService) throws IOException,
         ParseException, InsertionServiceEx, RecycleBinServiceEx {
      // Injection de jeu de donnée.
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      StorageDocument doc = insertionService.insertStorageDocument(storageDocument);
      recycleBinService.moveStorageDocumentToRecycleBin(doc.getUuid());
      return doc;
   }

   /**
    * Suppression du jeu de donnée.<br>{@inheritDoc}
    */
   public final void destroyMockForRecycleBinTest(final UUID uuid,
         final RecycleBinService recycleBinService) throws RecycleBinServiceEx {
      final List<StorageMetadata> desiredStorageMetadatas = new ArrayList<StorageMetadata>();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(uuid,
            desiredStorageMetadatas);
      recycleBinService.deleteStorageDocumentFromRecycleBin(uuidCriteria.getUuid());
   }

   /**
    * @return the updateService
    */
   public final UpdateService getUpdateService() {
      return updateService;
   }
   
   /**
    * @return the updateService
    */
   public final RecycleBinService getRecycleBinService() {
      return recycleBinService;
   }

   /**
    * Initialise les paramètres pour les services.
    * 
    * @throws ConnectionServiceEx
    *            Exception lévée lorsque la connexion n'aboutie pas.
    */
   public final void initServicesParameters() throws ConnectionServiceEx {
      getDfceServicesManager().getConnection();
      getInsertionService().setInsertionServiceParameter(
            getDfceServicesManager().getDFCEService());
      getRetrievalService().setRetrievalServiceParameter(
            getDfceServicesManager().getDFCEService());
      getDeletionService().setDeletionServiceParameter(
            getDfceServicesManager().getDFCEService());
      getSearchingService().setSearchingServiceParameter(
            getDfceServicesManager().getDFCEService());
      getUpdateService().setUpdateServiceParameter(
            getDfceServicesManager().getDFCEService());
      getRecycleBinService().setRecycleBinServiceParameter(
            getDfceServicesManager().getDFCEService());
   }

   /**
    * Libère les ressources
    */
   public final void closeServicesParameters() {
      getDfceServicesManager().closeConnection();
   }

   /**
    * @return La façade de services
    */
   public final StorageServiceProvider getServiceProvider() {
      return serviceProvider;
   }

   /**
    * Initialisation du StorageDocument
    * 
    * @throws IOException
    *            Exception IO
    * @throws ParseException
    *            Exception lors du parsing XML
    */
   public final void initStorageDocumens() throws IOException, ParseException {
      setStorageDocuments(getStorageDocumentsFromXml());
      setStorageDocument(getStorageDocumentFromXml());
   }

   /**
    * 
    * @return La liste des storageDocuments à partir des fichier de xml.
    * @throws IOException
    *            Exception levée lorsque le fichier xml n'existe pas.
    * @throws ParseException
    *            Exception levée lorsque le parsing n'abouti pas.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private StorageDocuments getStorageDocumentsFromXml() throws IOException,
         ParseException {
      List<StorageDocument> storageDoc = new ArrayList<StorageDocument>();
      File files[] = new File[Constants.XML_PATH_DOC_WITHOUT_ERROR.length];
      int numFile = 0;
      for (String pathFile : Constants.XML_PATH_DOC_WITHOUT_ERROR) {
         files[numFile] = new File(pathFile);
         numFile++;
      }
      // Récupération des fichiers de tests désérialisé.
      final List<SaeDocument> saeDocuments = getXmlDataService()
            .saeDocumentsReader(files);
      // Mapping entre les fichiers de tests et les StorageDocument
      for (SaeDocument saeDocument : Utils.nullSafeIterable(saeDocuments)) {
         storageDoc.add(DocumentForTestMapper
               .saeDocumentXmlToStorageDocument(saeDocument));
      }
      StorageDocuments storDocuments = new StorageDocuments(storageDoc);

      return storDocuments;
   }

   /**
    * 
    * @return
    * @throws IOException
    *            Exception levée lorsque le fichier xml n'existe pas.
    * @throws ParseException
    *            Exception levée lorsque le parsing n'abouti pas.
    */
   private StorageDocument getStorageDocumentFromXml() throws IOException,
         ParseException {
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      return DocumentForTestMapper.saeDocumentXmlToStorageDocument(saeDocument);
   }

   /**
    * @param storageDocuments
    *           : Un document de test.
    */
   public final void setStorageDocuments(final StorageDocuments storageDocuments) {
      this.storageDocuments = storageDocuments;
   }

   /**
    * @return Un document de test.
    */
   public final StorageDocuments getStorageDocuments() {
      return storageDocuments;
   }

   /**
    * @param storageDocument
    *           : Le storageDocument à partir du fichier xml.
    */
   public final void setStorageDocument(final StorageDocument storageDocument) {
      this.storageDocument = storageDocument;
   }

   /**
    * @return Le storageDocument à partir du fichier xml.
    */
   public final StorageDocument getStorageDocument() {
      return storageDocument;
   }

   /**
    * @param storageMetadatas
    *           the storageMetadatas to set
    */
   public final void setStorageMetadatas(
         final List<StorageMetadata> storageMetadatas) {
      this.storageMetadatas = storageMetadatas;
   }

   /**
    * @return the storageMetadatas
    */
   public final List<StorageMetadata> getStorageMetadatas() {
      return storageMetadatas;
   }

   /**
    * Copie du stream dans les flux indiqués
    * 
    * @param inputStream
    *           inputstream
    * @param outputStream
    *           liste des outputstream
    * @throws IOException
    *            Exception IO
    */
   public final void copyStream(InputStream inputStream,
         OutputStream... outputStreams) throws IOException {
      int val;

      while ((val = inputStream.read()) != -1) {
         for (OutputStream outputStream : outputStreams) {
            outputStream.write(val);
         }
      }

   }
}
