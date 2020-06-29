package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.activation.DataHandler;

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
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.dfce.utils.MarkableFileDataSource;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;

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
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public StorageDocument insertStorageDocument(
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
         final Document docDfce = BeanMapper.storageDocumentToDfceDocument(
               getBaseDFCE(), storageDocument, file);

         // -- ici on récupère le contenu du fichier.
         final File fileContent = new File(storageDocument.getFilePath());
         // On fait en sorte que le dataHandler puisse avoir accès à un fileInputStream qui supporte les
         // méthodes mark et reset, afin de pouvoir gérer les éventuelles reconnexions à DFCE.

         try (MarkableFileDataSource fis = new MarkableFileDataSource(fileContent)) {
            final DataHandler docContent = new DataHandler(fis);
            LOGGER.debug("{} - Début insertion du document dans DFCE", TRC_INSERT);
            return storageDocumentServiceSupport.insertDocumentInStorage(
                  getDfceServices(),
                  typeList,
                  docDfce,
                  docContent,
                  file,
                  storageDocument.getMetadatas(),
                  tracesSupport);
         }

      } catch (final InsertionIdGedExistantEx ex) {
         throw ex;
      } catch (final Exception except) {

         throw new InsertionServiceEx(
               StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
               except.getMessage(), except);
      }
   }

   /**
    * {@inheritDoc}
    * @throws InsertionIdGedExistantEx
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public StorageDocument insertBinaryStorageDocument(
         final StorageDocument storageDoc) throws InsertionServiceEx, InsertionIdGedExistantEx {

      // -- Insertion du document
      return storageDocumentServiceSupport.insertBinaryStorageDocument(
            getDfceServices(), typeList, storageDoc, LOGGER,
            tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public StorageReferenceFile insertStorageReference(
         final VirtualStorageReference reference) throws InsertionServiceEx {
      final String trcPrefix = "insertStorageReference";
      LOGGER.debug("{} - début", trcPrefix);

      final String name = FilenameUtils.getBaseName(reference.getFilePath());
      final String extension = FilenameUtils.getExtension(reference.getFilePath());
      InputStream stream = null;
      StorageReferenceFile referenceFile = null;

      try {
         stream = new FileInputStream(reference.getFilePath());

         final FileReference fileReference = getDfceServices().createFileReference(name,
               extension, stream);

         referenceFile = BeanMapper
               .fileReferenceToStorageReferenceFile(fileReference);

         LOGGER.debug(
               "{} - insertion réalisée. Fichier inséré : {}{}",
               new Object[] { trcPrefix, IOUtils.LINE_SEPARATOR,
                     referenceFile.toString() });

      } catch (final Exception exception) {
         throw new InsertionServiceEx(
               StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
               exception.getMessage(), exception);

      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (final IOException exception) {
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
   public UUID insertVirtualStorageDocument(
         final VirtualStorageDocument vDocument) throws InsertionServiceEx {
      final String trcPrefix = "insertVirtualStorageDocument";
      LOGGER.debug("{} - début", trcPrefix);

      final Base baseDFCE = getBaseDFCE();
      UUID uuid = null;

      try {
         LOGGER.debug("{} - transformation de l'objet document en objet DFCE",
               trcPrefix);
         final Document document = BeanMapper.virtualStorageDocumentToDfceDocument(
               baseDFCE, vDocument);

         LOGGER.debug(
               "{} - transformation du fichier de référence en objet DFCE",
               trcPrefix);
         final FileReference fileReference = BeanMapper
               .storageReferenceFileToFileReference(vDocument
                     .getReferenceFile());

         LOGGER.debug("{} - insertion du document virtuel", trcPrefix);
         final Document generateDocument = getDfceServices()
               .storeVirtualDocument(document, fileReference,
                     vDocument.getStartPage(), vDocument.getEndPage());

         uuid = generateDocument.getUuid();

      } catch (final Exception exception) {
         throw new InsertionServiceEx(
               StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
               exception.getMessage(), exception);
      }

      LOGGER.debug("{} - fin", trcPrefix);

      return uuid;
   }

}
