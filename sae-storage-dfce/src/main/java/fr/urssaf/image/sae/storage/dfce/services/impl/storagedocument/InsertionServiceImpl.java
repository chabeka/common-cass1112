package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * Implémente les services de l'interface {@link InsertionService}.
 * 
 * @author Akenore, Rhofir
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
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {

      try {
         // conversion du storageDocument en DFCE Document
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(
               getBaseDFCE(), storageDocument);

         // ici on récupère le contenu du fichier.
         byte[] docContentByte = FileUtils.readFileToByteArray(new File(
               storageDocument.getFilePath()));
         final InputStream docContent = new ByteArrayInputStream(docContentByte);

         // ici on récupère le nom et l'extension du fichier
         final String[] file = BeanMapper.findFileNameAndExtension(
               storageDocument, StorageTechnicalMetadatas.NOM_FICHIER
                     .getShortCode().toString());
         LOGGER.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { TRC_INSERT, file[0], file[1] });
         LOGGER.debug("{} - Début insertion du document dans DFCE", TRC_INSERT);

         StorageDocument retour = insertDocumentInStorage(docDfce,
               docContentByte, docContent, file, storageDocument.getMetadatas());

         return retour;
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument insertBinaryStorageDocument(
         StorageDocument storageDoc) throws InsertionServiceEx {

      try {
         // conversion du storageDoc en DFCE Document
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(
               getBaseDFCE(), storageDoc);

         // ici on récupère le contenu du fichier.
         byte[] docContentByte = storageDoc.getContent();
         InputStream docContent = new ByteArrayInputStream(docContentByte);

         // ici on récupère le nom et l'extension du fichier
         String[] file = new String[] {
               FilenameUtils.getBaseName(storageDoc.getFileName()),
               FilenameUtils.getExtension(storageDoc.getFileName()) };

         LOGGER.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { TRC_INSERT, file[0], file[1] });
         LOGGER.debug("{} - Début insertion du document dans DFCE", TRC_INSERT);

         StorageDocument retour = insertDocumentInStorage(docDfce,
               docContentByte, docContent, file, storageDoc.getMetadatas());
         return retour;
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }
   }

   protected final Document insertStorageDocument(Document document,
         String originalFilename, String extension, String digest,
         InputStream inputStream) throws TagControlException {

      Document doc;

      if (StringUtils.isEmpty(digest)) {

         doc = getDfceService().getStoreService().storeDocument(document,
               originalFilename, extension, inputStream);

      } else {

         doc = getDfceService().getStoreService().storeDocument(document,
               originalFilename, extension, digest, inputStream);
      }

      return doc;
   }

   /**
    * {@inheritDoc}
    */
   public final <T> void setInsertionServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);

   }

   private StorageDocument insertDocumentInStorage(Document docDfce,
         byte[] docContentByte, InputStream documentContent, String[] file,
         List<StorageMetadata> datas) throws InsertionServiceEx {

      // Traces debug - entrée méthode

      LOGGER.debug("{} - Début", TRC_INSERT);
      // Fin des traces debug - entrée méthode
      try {

         // TODO passer 'isCheckHash' en argument de la méthode d'insertion
         if (this.getCnxParameters().isCheckHash()) {

            // on récupère le paramètre général de l'algorithme de hachage des
            // documents dans DFCE

            String digest = null;

            // on récupère l'algorithme de hachage passé dans les métadonnées
            String typeHash = StorageMetadataUtils.valueMetadataFinder(datas,
                  StorageTechnicalMetadatas.TYPE_HASH.getShortCode());

            String digestAlgo = this.getCnxParameters().getDigestAlgo();

            LOGGER.debug("{} - Vérification du hash '" + digestAlgo
                  + "' du document dans DFCE", TRC_INSERT);

            if (StringUtils.isNotEmpty(digestAlgo)
                  && StringUtils.isNotEmpty(typeHash)
                  && digestAlgo.equals(typeHash)) {

               // on récupère la valeur du hash contenu dans les métadonnées
               digest = StringUtils.trim(StorageMetadataUtils
                     .valueMetadataFinder(datas, StorageTechnicalMetadatas.HASH
                           .getShortCode()));

               LOGGER.debug("{} - Récupération du hash '" + digest
                     + "' des métadonnées", TRC_INSERT);

            } else {

               // on recalcule le hash
               digest = HashUtils.hashHex(docContentByte, digestAlgo);
               LOGGER
                     .debug("{} - Calcule du hash '" + digest + "'", TRC_INSERT);
            }

            docDfce = insertStorageDocument(docDfce, file[0], file[1], digest,
                  documentContent);
         } else {

            docDfce = insertStorageDocument(docDfce, file[0], file[1], null,
                  documentContent);
         }

         LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})", TRC_INSERT,
               docDfce.getUuid());
         LOGGER.debug("{} - Fin insertion du document dans DFCE", TRC_INSERT);
         LOGGER.debug("{} - Sortie", TRC_INSERT);
         return BeanMapper.dfceDocumentToStorageDocument(docDfce, null,
               getDfceService(), false);
      } catch (TagControlException tagCtrlEx) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), tagCtrlEx.getMessage(),
               tagCtrlEx);
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }

   }

}
