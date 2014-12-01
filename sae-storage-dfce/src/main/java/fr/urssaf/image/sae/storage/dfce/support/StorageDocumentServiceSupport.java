package fr.urssaf.image.sae.storage.dfce.support;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.exception.DocumentTypeException;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * 
 * Classe utilitaire de mutualisation du code des implémentations des services DFCE.
 * Cette classe regroupe le code commeun au classes : 
 * 
 * <li>DeletionServiceImpl</li>
 * <li>InsertionServiceImpl</li>
 * <li>SearchingServiceImpl</li>
 * <li>TransfertServiceImpl</li>
 * 
 * @since 22/10/2014
 * @author MPA
 *
 */
@Component
public class StorageDocumentServiceSupport {
   
   private static final Logger LOGGER = LoggerFactory .getLogger(StorageDocumentServiceSupport.class);
   
   public StorageDocument insertBinaryStorageDocument(ServiceProvider dfceService, DFCEConnection cnxParams, 
         DocumentsTypeList typeDocList, StorageDocument storageDocument, Logger LOG, 
         TracesDfceSupport tracesSupport) throws InsertionServiceEx {
      
      Base base = StorageDocumentServiceSupport.getBaseDFCE(dfceService, cnxParams);
      String TRC_INSERT = "insertStorageDocument()";
      try {
         //-- conversion du storageDoc en DFCE Document
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(base, storageDocument);

         docDfce.setUuid(storageDocument.getUuid());

         //-- ici on récupère le contenu du fichier.
         DataHandler docContent = storageDocument.getContent();

         //-- ici on récupère le nom et l'extension du fichier
         String[] file = new String[] {
               FilenameUtils.getBaseName(storageDocument.getFileName()),
               FilenameUtils.getExtension(storageDocument.getFileName()) };

         LOGGER.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { TRC_INSERT, file[0], file[1] });
         LOGGER.debug("{} - Début insertion du document dans DFCE", TRC_INSERT);

         return insertDocumentInStorage(dfceService, cnxParams, typeDocList,
                  docDfce, docContent, file, storageDocument.getMetadatas(), tracesSupport);

      }
      catch (InsertionServiceEx ex) {
         String messg = StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR);
         throw new InsertionServiceEx(messg, ex.getMessage(), ex);
      }
      catch (Exception ex) {
         String messg = StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR);
         throw new InsertionServiceEx(messg, ex.getMessage(), ex);
      }
   }
   
   public StorageDocument insertDocumentInStorage(ServiceProvider dfceService, 
         DFCEConnection cxnParam, DocumentsTypeList typeDocList, Document docDfce,
         DataHandler documentContent, String[] file, List<StorageMetadata> metadatas, TracesDfceSupport tracesSupport)
         throws InsertionServiceEx {
      
      String TRC_INSERT = "insertStorageDocument()";
      
      //-- Traces debug - entrée méthode
      LOGGER.debug("{} - Début", TRC_INSERT);
      LOGGER.debug("{} - Début de vérification du type de document", TRC_INSERT);
      
      //-- Vérification du type de document
      checkDocumentType(typeDocList, metadatas);
      LOGGER.debug("{} - Fin de vérification du type de document", TRC_INSERT);

      InputStream inputStream = null;

      try {

         // Récupère le hash et le type de hash des métadonnées
         // Ces 2 informations sont obligatoires à l'archivage
         String hashMeta = StringUtils.trim(StorageMetadataUtils
               .valueMetadataFinder(metadatas, StorageTechnicalMetadatas.HASH
                     .getShortCode()));
         String typeHashMeta = StorageMetadataUtils.valueMetadataFinder(metadatas,
               StorageTechnicalMetadatas.TYPE_HASH.getShortCode());

         // Détermine le hash qu'il faut passer à DFCE pour qu'il le vérifie
         // lors de l'archivage
         String digest = null;
         if (cxnParam.isCheckHash()) {

            // on récupère le paramètre général de l'algorithme de hachage des
            // documents dans DFCE
            String digestAlgo = cxnParam.getDigestAlgo();
            String message = "{} - Algo de hash requis par DFCE pour la vérification du hash à l'archivage : {}";
            LOGGER.debug(message, TRC_INSERT, digestAlgo);

            // Soit on utilise le hash présent dans les métadonnées, car l'algo
            // de hash
            // présent dans les métadonnées est identique à l'algo de hash
            // requis par DFCE,
            // soit on doit recalculer le hash avec l'algo requis par DFCE
            if (StringUtils.isNotEmpty(digestAlgo)
                  && StringUtils.equals(typeHashMeta, digestAlgo)
                  && StringUtils.isNotEmpty(hashMeta)) {

               // on récupère la valeur du hash contenu dans les métadonnées
               digest = hashMeta;
               LOGGER.debug("{} - Hash récupéré des métadonnées : {}",
                     TRC_INSERT, digest);

            } else {
               //-- on recalcule le hash
               digest = checkHash(documentContent, digestAlgo, file[0]);
            }
         }

         inputStream = documentContent.getInputStream();

         // Appel de l'API DFCE pour l'archivage du document
         Document docArchive = insertStorageDocument(dfceService, docDfce, file[0], file[1],
               digest, inputStream, hashMeta, typeHashMeta, tracesSupport);

         // Trace
         LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})", TRC_INSERT,
               docArchive.getUuid());
         LOGGER.debug("{} - Fin insertion du document dans DFCE", TRC_INSERT);
         LOGGER.debug("{} - Sortie", TRC_INSERT);

         // Mapping d'objet pour passer l'objet Document de DFCE à l'objet
         // StorageDocument
         return BeanMapper.dfceDocumentToStorageDocument(docArchive, null, dfceService, false);

      } catch (TagControlException tagCtrlEx) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), tagCtrlEx.getMessage(),
               tagCtrlEx);

      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      } finally {
         close(inputStream, file[0]);
      }

   }
   
   /**
    * 
    * Archibage du document dans DFCE ((Api DFCE)
    * 
    * @param dfceService
    * @param document
    * @param originalFilename
    * @param extension
    * @param digest
    * @param inputStream
    * @param hashPourTrace
    * @param typeHashPourTrace
    * 
    * @return Objet {{@link #Document} DFCE
    * 
    * 
    * @throws TagControlException
    */
   private Document insertStorageDocument(ServiceProvider dfceService, Document document,
         String originalFilename, String extension, String digest,
         InputStream inputStream, String hashPourTrace, String typeHashPourTrace, TracesDfceSupport tracesSupport)
         throws TagControlException {

      Document doc;

      if (StringUtils.isEmpty(digest)) {
         doc = dfceService.getStoreService().storeDocument(document,
               originalFilename, extension, inputStream);

      } else {
         doc = dfceService.getStoreService().storeDocument(document,
               originalFilename, extension, digest, inputStream);
      }
      
      // Trace l'événement "Dépôt d'un document dans DFCE"
      tracesSupport.traceDepotDocumentDansDFCE(doc.getUuid(), hashPourTrace,
            typeHashPourTrace, doc.getArchivageDate());

      return doc;
   }
   
   private void checkDocumentType(DocumentsTypeList typeDocList, List<StorageMetadata> metadatas)
      throws DocumentTypeException {
   
      String docType = StorageMetadataUtils.valueMetadataFinder(metadatas,
            StorageTechnicalMetadatas.TYPE.getShortCode());
      
      if (!typeDocList.getTypes().contains(docType)) {
         String message = "Impossible d'insérer le document, " +
         		"son type n'est pas valide : {} ne fait pas partie des type référencés";
         throw new DocumentTypeException(StringUtils.replace(message, "{}", docType));
      }
   }
   
   public StorageDocument searchStorageDocumentByUUIDCriteria(ServiceProvider dfceService,
         DFCEConnection cnxParams, UUIDCriteria uUIDCriteria, Logger LOG) throws SearchingServiceEx {
      
      //-- Récupération base dfce
      Base baseDfce = StorageDocumentServiceSupport.getBaseDFCE(dfceService, cnxParams);
      
      try {
         //-- Traces debug - entrée méthode
         String prefixeTrc = "searchStorageDocumentByUUIDCriteria()";
         LOG.debug("{} - Début", prefixeTrc);
         LOG.debug("{} - UUIDCriteria du document à consulter: {}", prefixeTrc,
               uUIDCriteria.toString());
         //-- Fin des traces debug - entrée méthode
         LOG.debug("{} - Début de la recherche dans DFCE", prefixeTrc);
         final Document docDfce = dfceService.getSearchService()
               .getDocumentByUUID(baseDfce, uUIDCriteria.getUuid());
         LOG.debug("{} - Fin de la recherche dans DFCE", prefixeTrc);
         StorageDocument storageDoc = null;

         if (docDfce != null) {
            storageDoc = BeanMapper.dfceDocumentToStorageDocument(docDfce,
                  uUIDCriteria.getDesiredStorageMetadatas(), dfceService, true);
         }
         LOG.debug("{} - Sortie", prefixeTrc);
         return storageDoc;

      } catch (StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), srcSerEx.getMessage(),
               srcSerEx);
      } catch (IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), ioExcept.getMessage(),
               ioExcept);
      } catch (Exception except) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), except.getMessage(),
               except);
      }
   }
   
   public final void deleteStorageDocument(ServiceProvider dfceService,
         DFCEConnection cnxParams, final UUID uuid, Logger LOG, TracesDfceSupport tracesSupport)
      throws DeletionServiceEx {
      
      //-- Traces debug - entrée méthode
      String prefixeTrc = "deleteStorageDocument()";
      LOG.debug("{} - Début", prefixeTrc);

      try {
         LOG.debug("{} - UUID à supprimer : {}", prefixeTrc, uuid);
         dfceService.getStoreService().deleteDocument(uuid);

         //-- Trace l'événement "Suppression d'un document de DFCE"
         tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         LOG.debug("{} - Sortie", prefixeTrc);

      } catch (FrozenDocumentException frozenExcept) {
         LOG
         .debug(
               "{} - Une exception a été levée lors de la suppression du document : {}",
               prefixeTrc, frozenExcept.getMessage());
         throw new DeletionServiceEx(StorageMessageHandler
               .getMessage(Constants.DEL_CODE_ERROR),
               frozenExcept.getMessage(), frozenExcept);
      }
   }
   
   private String checkHash(DataHandler documentContent, String digestAlgo, String fileName) 
      throws IOException, NoSuchAlgorithmException {
      
      String digest;
      InputStream stream = null;
      String TRC_INSERT = "insertStorageDocument()";
      try {
         stream = documentContent.getInputStream();
         digest = HashUtils.hashHex(stream, digestAlgo);
         LOGGER.debug("{} - Hash recalculé : {}", TRC_INSERT, digest);
      } finally {
         close(stream, fileName);
      }
      return digest;
   }

   private void close(Closeable closeable, String name) {
      String trcPrefix = "close()";
      if (closeable != null) {
         try {
            closeable.close();
         } catch (IOException e) {
            LOGGER.info("{} - Erreur de fermeture du flux {}", new Object[]{trcPrefix, name});
         }
      }
   }
   
   public static Base getBaseDFCE(ServiceProvider dfceService, DFCEConnection cnxParameters){
      return dfceService.getBaseAdministrationService().getBase(
           cnxParameters.getBaseName());
   }

}
