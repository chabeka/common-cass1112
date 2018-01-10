/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.model.AbstractTransfertServices;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
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
import fr.urssaf.image.sae.storage.services.storagedocument.TransfertService;

/**
 * Classe d'implémentation de l'interface {@link TransfertService}. Cette classe
 * est un singleton et peut être accessible via le mécanisme d'injection IOC et
 * l'annotation @Autowired
 * 
 */
@Component
public class TransfertServiceImpl extends AbstractTransfertServices implements
TransfertService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TransfertServiceImpl.class);

   /**
    * On injecte DocumentsTypeList mannuellement(xml) paramétré avec la
    * connexion DFCE de transfert pour éviter les conflits avec celle existante.
    */
   private DocumentsTypeList typeList;

   private TracesDfceSupport tracesSupport;

   /**
    * Constructeur
    */
   public TransfertServiceImpl(DocumentsTypeList typeList,
         TracesDfceSupport tracesSupport, DFCEConnection cnxParameters) {
      this.typeList = typeList;
      this.tracesSupport = tracesSupport;
      this.cnxParameters = cnxParameters;
   }

   /**
    * Constructeur
    */
   public TransfertServiceImpl() {
      super();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument searchStorageDocumentByUUIDCriteria(
         UUIDCriteria uUIDCriteria) throws SearchingServiceEx {
      ServiceProvider dfceService = getDfceService();
      DFCEConnection cnxParams = getCnxParameters();
      return storageDocumentServiceSupport.searchStorageDocumentByUUIDCriteria(
            dfceService, cnxParams, uUIDCriteria, false, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument insertBinaryStorageDocument(
         StorageDocument storageDocument) throws InsertionServiceEx,
         InsertionIdGedExistantEx {
      ServiceProvider dfceService = getDfceService();
      DFCEConnection cnxParams = getCnxParameters();
      return storageDocumentServiceSupport.insertBinaryStorageDocument(
            dfceService, cnxParams, typeList, storageDocument, LOGGER,
            tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteStorageDocument(UUID uuid) throws DeletionServiceEx {
      ServiceProvider dfceService = getDfceService();
      DFCEConnection cnxParams = getCnxParameters();
      // -- Suppression du ducument
      storageDocumentServiceSupport.deleteStorageDocument(dfceService,
            cnxParams, uuid, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentNote(UUID docUuid, String contenu, String login,
         Date dateCreation, UUID noteUuid) throws DocumentNoteServiceEx {
      ServiceProvider dfceService = getDfceService();
      storageDocumentServiceSupport.addDocumentNote(dfceService, docUuid,
            contenu, login, dateCreation, noteUuid, LOGGER);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageDocumentNote> getDocumentNotes(UUID docUuid) {
      ServiceProvider dfceService = getDfceService();
      return storageDocumentServiceSupport.getDocumentNotes(dfceService,
            docUuid, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentAttachment(UUID docUuid, String docName,
         String extension, DataHandler contenu)
               throws StorageDocAttachmentServiceEx {
      ServiceProvider dfceService = getDfceService();
      DFCEConnection cnxParams = getCnxParameters();
      storageDocumentServiceSupport.addDocumentAttachment(dfceService,
            cnxParams, docUuid, docName, extension, contenu, LOGGER,
            tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      ServiceProvider dfceService = getDfceService();
      DFCEConnection cnxParams = getCnxParameters();
      return storageDocumentServiceSupport.getDocumentAttachment(dfceService,
            cnxParams, docUuid, LOGGER);
   }

}
