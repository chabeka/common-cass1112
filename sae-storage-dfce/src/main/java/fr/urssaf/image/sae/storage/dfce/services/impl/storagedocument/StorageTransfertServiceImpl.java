package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.model.AbstractStorageServices;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;

/**
 * {@inheritDoc}
 */
public class StorageTransfertServiceImpl extends AbstractStorageServices
implements StorageTransfertService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageTransfertServiceImpl.class);

   /**
    * On injecte DFCEServicesManager mannuellement(xml) paramétré avec la
    * connexion DFCE de transfert pour éviter les conflits avec celle existante.
    */
   private DFCEServicesManager dfceServicesManager;

   /**
    * On injecte DocumentsTypeList mannuellement(xml) paramétré avec la
    * connexion DFCE de transfert pour éviter les conflits avec celle existante.
    */
   private DocumentsTypeList typeList;

   private TracesDfceSupport tracesSupport;

   /**
    * Constructeur
    * 
    * @param dfceSercivesMgr
    *           Manager des services DFCE
    * @param typeList
    *           Liste de type de documents
    * @param tracesSupport
    *           Support d'écriture des traces
    */
   public StorageTransfertServiceImpl(DFCEServicesManager dfceSercivesMgr,
         DocumentsTypeList typeList, TracesDfceSupport tracesSupport) {

      this.typeList = typeList;
      this.tracesSupport = tracesSupport;
      this.dfceServicesManager = dfceSercivesMgr;
   }

   /**
    * {@inheritDoc}
    * @throws InsertionIdGedExistantEx 
    */
   @Override
   public StorageDocument insertBinaryStorageDocument(
         StorageDocument storageDocument) throws InsertionServiceEx, InsertionIdGedExistantEx {

      ServiceProvider dfceService = dfceServicesManager.getDFCEService();
      DFCEConnection cnxParams = dfceServicesManager.getCnxParameters();
      return storageDocumentServiceSupport.insertBinaryStorageDocument(
            dfceService,
            cnxParams, typeList, storageDocument, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument searchStorageDocumentByUUIDCriteria(
         UUIDCriteria uUIDCriteria) throws SearchingServiceEx {

      ServiceProvider dfceService = dfceServicesManager.getDFCEService();
      DFCEConnection cnxParams = dfceServicesManager.getCnxParameters();
      return storageDocumentServiceSupport.searchStorageDocumentByUUIDCriteria(
            dfceService, cnxParams, uUIDCriteria, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteStorageDocument(UUID uuid) throws DeletionServiceEx {

      ServiceProvider dfceService = dfceServicesManager.getDFCEService();
      DFCEConnection cnxParams = dfceServicesManager.getCnxParameters();

      // -- Suppression du ducument
      storageDocumentServiceSupport.deleteStorageDocument(dfceService,
            cnxParams, uuid,
            LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentNote(UUID docUuid, String contenu, String login,
         Date dateCreation, UUID noteUuid) throws DocumentNoteServiceEx {

      ServiceProvider dfceService = dfceServicesManager.getDFCEService();

      storageDocumentServiceSupport.addDocumentNote(dfceService, docUuid,
            contenu,
            login, dateCreation, noteUuid, LOGGER);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageDocumentNote> getDocumentNotes(UUID docUuid) {
      ServiceProvider dfceService = dfceServicesManager.getDFCEService();

      return storageDocumentServiceSupport.getDocumentNotes(dfceService,
            docUuid,
            LOGGER);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void openConnexion() throws ConnectionServiceEx {
      dfceServicesManager.openConnection();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void closeConnexion() {
      dfceServicesManager.closeConnection();
   }

   @Override
   public void addDocumentAttachment(UUID docUuid, String docName,
         String extension, DataHandler contenu)
               throws StorageDocAttachmentServiceEx {

      ServiceProvider dfceService = dfceServicesManager.getDFCEService();
      DFCEConnection cnxParams = dfceServicesManager.getCnxParameters();
      storageDocumentServiceSupport.addDocumentAttachment(dfceService,
            cnxParams,
            docUuid, docName, extension, contenu, LOGGER, tracesSupport);

   }

   @Override
   public StorageDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      ServiceProvider dfceService = dfceServicesManager.getDFCEService();
      DFCEConnection cnxParams = dfceServicesManager.getCnxParameters();
      return storageDocumentServiceSupport.getDocumentAttachment(dfceService,
            cnxParams, docUuid, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final DFCEServicesManager getDfceServicesManager() {
      return dfceServicesManager;
   }

}
