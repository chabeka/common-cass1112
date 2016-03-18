package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * Implémente les services de l'interface {@link InsertionService}.
 * 
 */
@Service
@Qualifier("insertionService")
public class InsertionServiceImpl extends AbstractServices implements
      InsertionService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(InsertionServiceImpl.class);

   @Autowired
   @Qualifier("deletionService")
   private DeletionService deletionService;

   @Autowired
   private DocumentsTypeList typeList;

   @Autowired
   private TracesDfceSupport tracesSupport;

   @Autowired
   private StorageDocumentServiceSupport storageDocumentServiceSupport;

   /**
    * @return : Le service de suppression
    */
   public final DeletionService getDeletionService() {
      return deletionService;
   }

   /**
    * @param deletionService
    *           : Le service de suppression.
    */
   public final void setDeletionService(final DeletionService deletionService) {
      this.deletionService = deletionService;
   }

   private static final String TRC_INSERT = "insertStorageDocument()";

   /**
    * {@inheritDoc}
    * @throws InsertionIdGedExistantEx 
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx, InsertionIdGedExistantEx {

      try {
         // -- ici on récupère le nom et l'extension du fichier
         final String[] file = BeanMapper.findFileNameAndExtension(
               storageDocument, StorageTechnicalMetadatas.NOM_FICHIER
                     .getShortCode().toString());
         LOGGER.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { TRC_INSERT, file[0], file[1] });

         // -- conversion du storageDocument en DFCE Document
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(
               getBaseDFCE(), storageDocument, file);

         // -- ici on récupère le contenu du fichier.
         File fileContent = new File(storageDocument.getFilePath());
         final DataHandler docContent = new DataHandler(new FileDataSource(
               fileContent));

         LOGGER.debug("{} - Début insertion du document dans DFCE", TRC_INSERT);

         return storageDocumentServiceSupport.insertDocumentInStorage(
               getDfceService(), getCnxParameters(), typeList, docDfce,
               docContent, file, storageDocument.getMetadatas(), tracesSupport);

      } catch (InsertionIdGedExistantEx ex) {
         throw ex;
      } catch (Exception except) {

         throw new InsertionServiceEx(
               StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
               except.getMessage(), except);
      }
   }

   /**
    * {@inheritDoc}
    * @throws InsertionIdGedExistantEx 
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument insertBinaryStorageDocument(
         StorageDocument storageDoc) throws InsertionServiceEx, InsertionIdGedExistantEx {

      // -- Insertion du document
      return storageDocumentServiceSupport.insertBinaryStorageDocument(
            getDfceService(), getCnxParameters(), typeList, storageDoc, LOGGER,
            tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   public final <T> void setInsertionServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageReferenceFile insertStorageReference(
         VirtualStorageReference reference) throws InsertionServiceEx {
      String trcPrefix = "insertStorageReference";
      LOGGER.debug("{} - début", trcPrefix);

      String name = FilenameUtils.getBaseName(reference.getFilePath());
      String extension = FilenameUtils.getExtension(reference.getFilePath());
      InputStream stream = null;
      StorageReferenceFile referenceFile = null;

      try {
         stream = new FileInputStream(reference.getFilePath());

         FileReference fileReference = getDfceService()
               .getStorageAdministrationService().createFileReference(name,
                     extension, stream);

         referenceFile = BeanMapper
               .fileReferenceToStorageReferenceFile(fileReference);

         LOGGER.debug(
               "{} - insertion réalisée. Fichier inséré : {}{}",
               new Object[] { trcPrefix, IOUtils.LINE_SEPARATOR,
                     referenceFile.toString() });

      } catch (Exception exception) {
         throw new InsertionServiceEx(
               StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
               exception.getMessage(), exception);

      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException exception) {
               LOGGER.info("Impossible de fermer le flux du contenu", exception);
            }
         }
      }

      LOGGER.debug("{} - fin", trcPrefix);

      return referenceFile;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final UUID insertVirtualStorageDocument(
         VirtualStorageDocument vDocument) throws InsertionServiceEx {
      String trcPrefix = "insertVirtualStorageDocument";
      LOGGER.debug("{} - début", trcPrefix);

      Base baseDFCE = getBaseDFCE();
      UUID uuid = null;

      try {
         LOGGER.debug("{} - transformation de l'objet document en objet DFCE",
               trcPrefix);
         Document document = BeanMapper.virtualStorageDocumentToDfceDocument(
               baseDFCE, vDocument);

         LOGGER.debug(
               "{} - transformation du fichier de référence en objet DFCE",
               trcPrefix);
         FileReference fileReference = BeanMapper
               .storageReferenceFileToFileReference(vDocument
                     .getReferenceFile());

         LOGGER.debug("{} - insertion du document virtuel", trcPrefix);
         Document generateDocument = getDfceService().getStoreService()
               .storeVirtualDocument(document, fileReference,
                     vDocument.getStartPage(), vDocument.getEndPage());

         uuid = generateDocument.getUuid();

      } catch (Exception exception) {
         throw new InsertionServiceEx(
               StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
               exception.getMessage(), exception);
      }

      LOGGER.debug("{} - fin", trcPrefix);

      return uuid;
   }

}
