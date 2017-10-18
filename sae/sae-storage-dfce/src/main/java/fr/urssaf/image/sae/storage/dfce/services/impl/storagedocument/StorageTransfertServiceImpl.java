package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.model.AbstractStorageServices;
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
import fr.urssaf.image.sae.storage.services.storagedocument.TransfertService;

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
    * Service de transfert
    */
   private TransfertService transfertService;

   /**
    * Constructeur
    * 
    * @param dfceSercivesMgr
    *           Manager des services DFCE
    * @param transfertService
    *           service de transfert
    */
   public StorageTransfertServiceImpl(DFCEServicesManager dfceSercivesMgr,
         TransfertService transfertService) {

      this.dfceServicesManager = dfceSercivesMgr;
      this.transfertService = transfertService;
   }

   /**
    * {@inheritDoc}
    * @throws InsertionIdGedExistantEx 
    */
   @Override
   public StorageDocument insertBinaryStorageDocument(
         StorageDocument storageDocument) throws InsertionServiceEx, InsertionIdGedExistantEx {
      return transfertService.insertBinaryStorageDocument(storageDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument searchStorageDocumentByUUIDCriteria(
         UUIDCriteria uUIDCriteria) throws SearchingServiceEx {
      return transfertService.searchStorageDocumentByUUIDCriteria(uUIDCriteria);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteStorageDocument(UUID uuid) throws DeletionServiceEx {
      transfertService.deleteStorageDocument(uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentNote(UUID docUuid, String contenu, String login,
         Date dateCreation, UUID noteUuid) throws DocumentNoteServiceEx {
      transfertService.addDocumentNote(docUuid, contenu, login, dateCreation,
            noteUuid);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageDocumentNote> getDocumentNotes(UUID docUuid) {
      return transfertService.getDocumentNotes(docUuid);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void openConnexion() throws ConnectionServiceEx {
      dfceServicesManager.getConnection();
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
      transfertService.addDocumentAttachment(docUuid, docName, extension,
            contenu);
   }

   @Override
   public StorageDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      return transfertService.getDocumentAttachment(docUuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final DFCEServicesManager getDfceServicesManager() {
      return dfceServicesManager;
   }

}
