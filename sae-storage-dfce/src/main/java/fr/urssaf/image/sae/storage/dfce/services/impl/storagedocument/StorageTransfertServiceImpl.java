package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.UUID;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;

/**
 * {@inheritDoc}
 */
public class StorageTransfertServiceImpl implements StorageTransfertService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageTransfertServiceImpl.class);

   /**
    * On injecte DFCEServicesManager mannuellement(xml) paramétré avec la
    * connexion DFCE de transfert pour éviter les conflits avec celle existante.
    */
   private DFCEServicesManager dfceServiceManager;

   /**
    * On injecte DocumentsTypeList mannuellement(xml) paramétré avec la
    * connexion DFCE de transfert pour éviter les conflits avec celle existante.
    */
   private DocumentsTypeList typeList;

   private TracesDfceSupport tracesSupport;

   @Autowired
   private StorageDocumentServiceSupport storageServiceSupport;

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
      this.dfceServiceManager = dfceSercivesMgr;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument insertBinaryStorageDocument(
         StorageDocument storageDocument) throws InsertionServiceEx {

      ServiceProvider dfceService = dfceServiceManager.getDFCEService();
      DFCEConnection cnxParams = dfceServiceManager.getCnxParameters();
      return storageServiceSupport.insertBinaryStorageDocument(dfceService,
            cnxParams, typeList, storageDocument, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument searchStorageDocumentByUUIDCriteria(
         UUIDCriteria uUIDCriteria) throws SearchingServiceEx {

      ServiceProvider dfceService = dfceServiceManager.getDFCEService();
      DFCEConnection cnxParams = dfceServiceManager.getCnxParameters();
      return storageServiceSupport.searchStorageDocumentByUUIDCriteria(
            dfceService, cnxParams, uUIDCriteria, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void deleteStorageDocument(UUID uuid) throws DeletionServiceEx {

      ServiceProvider dfceService = dfceServiceManager.getDFCEService();
      DFCEConnection cnxParams = dfceServiceManager.getCnxParameters();

      // -- Suppression du ducument
      storageServiceSupport.deleteStorageDocument(dfceService, cnxParams, uuid,
            LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void openConnexion() throws ConnectionServiceEx {
      dfceServiceManager.getConnection();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void closeConnexion() {
      dfceServiceManager.closeConnection();
   }
}
