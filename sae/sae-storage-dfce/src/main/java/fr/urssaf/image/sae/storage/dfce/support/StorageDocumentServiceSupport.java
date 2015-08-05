package fr.urssaf.image.sae.storage.dfce.support;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.note.Note;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.docubase.dfce.commons.document.StoreOptions;
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
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * 
 * Classe utilitaire de mutualisation du code des implémentations des services
 * DFCE. Cette classe regroupe le code commun au classes :
 * 
 * <li>DeletionServiceImpl</li> <li>InsertionServiceImpl</li> <li>
 * SearchingServiceImpl</li> <li>TransfertServiceImpl</li>
 * 
 * @since 22/10/2014
 * @author MPA
 * 
 */
@Component
public class StorageDocumentServiceSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentServiceSupport.class);

   /**
    * Insertion de document
    * 
    * @param dfceService
    *           Services de DFCE
    * @param cnxParams
    *           Paramétrage DFCE
    * @param typeDocList
    *           Liste de type de documents
    * @param storageDocument
    *           Document à inséré
    * @param log
    *           Logger
    * @param tracesSupport
    *           Support pour l'ecriture des traces
    * @return Le document inséré
    * @throws InsertionServiceEx
    *            Exception levée si erreur lors de l'insertion
    */
   public final StorageDocument insertBinaryStorageDocument(
         ServiceProvider dfceService, DFCEConnection cnxParams,
         DocumentsTypeList typeDocList, StorageDocument storageDocument,
         Logger log, TracesDfceSupport tracesSupport) throws InsertionServiceEx {

      Base base = StorageDocumentServiceSupport.getBaseDFCE(dfceService,
            cnxParams);
      String trcInsert = "insertStorageDocument()";
      try {
         // -- ici on récupère le nom et l'extension du fichier
         String[] file = new String[] {
               FilenameUtils.getBaseName(storageDocument.getFileName()),
               FilenameUtils.getExtension(storageDocument.getFileName()) };

         log.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { trcInsert, file[0], file[1] });

         // -- conversion du storageDoc en DFCE Document
         Document docDfce = BeanMapper.storageDocumentToDfceDocument(base,
               storageDocument, file);

         docDfce.setUuid(storageDocument.getUuid());

         // -- ici on récupère le contenu du fichier.
         DataHandler docContent = storageDocument.getContent();

         log.debug("{} - Début insertion du document dans DFCE", trcInsert);

         return insertDocumentInStorage(dfceService, cnxParams, typeDocList,
               docDfce, docContent, file, storageDocument.getMetadatas(),
               tracesSupport);

      } catch (InsertionServiceEx ex) {
         String messg = StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR);
         throw new InsertionServiceEx(messg, ex.getMessage(), ex);
      } catch (Exception ex) {
         String messg = StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR);
         throw new InsertionServiceEx(messg, ex.getMessage(), ex);
      }
   }

   /**
    * Insertion de document
    * 
    * @param dfceService
    *           Services de DFCE
    * @param cxnParam
    *           Paramétrage DFCE
    * @param typeDocList
    *           Liste de type de documents
    * @param docDfce
    *           Document DFCE
    * @param documentContent
    *           Contenu du document
    * @param file
    *           fichier
    * @param metadatas
    *           Liste des métadonnées
    * @param tracesSupport
    *           Ecriture des traces
    * @return Le document inséré
    * @throws InsertionServiceEx
    *            Exception levée si erreur lors de l'insertion
    */
   public final StorageDocument insertDocumentInStorage(
         ServiceProvider dfceService, DFCEConnection cxnParam,
         DocumentsTypeList typeDocList, Document docDfce,
         DataHandler documentContent, String[] file,
         List<StorageMetadata> metadatas, TracesDfceSupport tracesSupport)
         throws InsertionServiceEx {

      String trcInsert = "insertStorageDocument()";

      // -- Traces debug - entrée méthode
      LOGGER.debug("{} - Début", trcInsert);
      LOGGER.debug("{} - Début de vérification du type de document", trcInsert);

      // -- Vérification du type de document
      checkDocumentType(typeDocList, metadatas);
      LOGGER.debug("{} - Fin de vérification du type de document", trcInsert);

      InputStream inputStream = null;

      try {

         // Récupère le hash et le type de hash des métadonnées
         // Ces 2 informations sont obligatoires à l'archivage
         String hashMeta = StringUtils.trim(StorageMetadataUtils
               .valueMetadataFinder(metadatas, StorageTechnicalMetadatas.HASH
                     .getShortCode()));
         String typeHashMeta = StorageMetadataUtils.valueMetadataFinder(
               metadatas, StorageTechnicalMetadatas.TYPE_HASH.getShortCode());

         // Détermine le hash qu'il faut passer à DFCE pour qu'il le vérifie
         // lors de l'archivage
         String digest = null;
         if (cxnParam.isCheckHash()) {

            // on récupère le paramètre général de l'algorithme de hachage des
            // documents dans DFCE
            String digestAlgo = cxnParam.getDigestAlgo();
            String message = "{} - Algo de hash requis par DFCE pour la vérification du hash à l'archivage : {}";
            LOGGER.debug(message, trcInsert, digestAlgo);

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
                     trcInsert, digest);

            } else {
               // -- on recalcule le hash
               digest = checkHash(documentContent, digestAlgo, file[0]);
            }
         }

         inputStream = documentContent.getInputStream();
         
         // recherche d'une note eventuelle
         String note = "";
         for (StorageMetadata metadata : metadatas) {
            if (metadata.getShortCode().equals(
                  StorageTechnicalMetadatas.NOTE.getShortCode())) {
               note = (String) metadata.getValue();
               break;
            }
         }

         // Appel de l'API DFCE pour l'archivage du document
         Document docArchive = insertStorageDocument(dfceService, docDfce,
               digest, inputStream, hashMeta, typeHashMeta, tracesSupport, note);

         // Trace
         LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})", trcInsert,
               docArchive.getUuid());
         LOGGER.debug("{} - Fin insertion du document dans DFCE", trcInsert);
         LOGGER.debug("{} - Sortie", trcInsert);

         // Mapping d'objet pour passer l'objet Document de DFCE à l'objet
         // StorageDocument
         return BeanMapper.dfceDocumentToStorageDocument(docArchive, null,
               dfceService, false);

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
    * Archivage du document dans DFCE ((Api DFCE)
    * 
    * @param dfceService
    *           Services de DFCE
    * @param document
    * @param digest
    * @param inputStream
    * @param hashPourTrace
    * @param typeHashPourTrace
    * @param note
    * 
    * @return Objet {{@link #Document} DFCE
    * 
    * 
    * @throws TagControlException
    */
   private Document insertStorageDocument(ServiceProvider dfceService,
         Document document, String digest, InputStream inputStream,
         String hashPourTrace, String typeHashPourTrace,
         TracesDfceSupport tracesSupport, String note) throws TagControlException,
         FrozenDocumentException {

      Document doc;

      if (StringUtils.isEmpty(digest)) {
         doc = dfceService.getStoreService().storeDocument(document,
               inputStream);

      } else {
         doc = dfceService.getStoreService().storeDocument(document,
               new StoreOptions.Builder().verifyDigest(digest).build(), null,
               inputStream);
      }

      // Trace l'événement "Dépôt d'un document dans DFCE"
      tracesSupport.traceDepotDocumentDansDFCE(doc.getUuid(), hashPourTrace,
            typeHashPourTrace, doc.getArchivageDate());

      // Ajout de la note au document
      if (StringUtils.isNotEmpty(note)) {
         dfceService.getNoteService().addNote(doc.getUuid(), note);
      }
      return doc;
   }

   private void checkDocumentType(DocumentsTypeList typeDocList,
         List<StorageMetadata> metadatas) {

      String docType = StorageMetadataUtils.valueMetadataFinder(metadatas,
            StorageTechnicalMetadatas.TYPE.getShortCode());

      if (!typeDocList.getTypes().contains(docType)) {
         String message = "Impossible d'insérer le document, "
               + "son type n'est pas valide : {} ne fait pas partie des type référencés";
         throw new DocumentTypeException(StringUtils.replace(message, "{}",
               docType));
      }
   }

   /**
    * Recherche de document par UUID
    * 
    * @param dfceService
    *           Services de DFCE
    * @param cnxParams
    *           Paramétrage DFCE
    * @param uUIDCriteria
    *           UUID du document recherché
    * @param log
    *           Logger
    * @return Le document trouvé
    * @throws SearchingServiceEx
    *            Exception levée lors d'erreur pendant la recherche
    */
   public final StorageDocument searchStorageDocumentByUUIDCriteria(
         ServiceProvider dfceService, DFCEConnection cnxParams,
         UUIDCriteria uUIDCriteria, Logger log) throws SearchingServiceEx {

      // -- Récupération base dfce
      Base baseDfce = StorageDocumentServiceSupport.getBaseDFCE(dfceService,
            cnxParams);

      try {
         // -- Traces debug - entrée méthode
         String prefixeTrc = "searchStorageDocumentByUUIDCriteria()";
         log.debug("{} - Début", prefixeTrc);
         log.debug("{} - UUIDCriteria du document à consulter: {}", prefixeTrc,
               uUIDCriteria.toString());
         // -- Fin des traces debug - entrée méthode
         log.debug("{} - Début de la recherche dans DFCE", prefixeTrc);
         final Document docDfce = dfceService.getSearchService()
               .getDocumentByUUID(baseDfce, uUIDCriteria.getUuid());
         log.debug("{} - Fin de la recherche dans DFCE", prefixeTrc);
         StorageDocument storageDoc = null;

         if (docDfce != null) {
            storageDoc = BeanMapper.dfceDocumentToStorageDocument(docDfce,
                  uUIDCriteria.getDesiredStorageMetadatas(), dfceService, true);
         }
         log.debug("{} - Sortie", prefixeTrc);
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

   /**
    * Suppression de document
    * 
    * @param dfceService
    *           Services de DFCE
    * @param cnxParams
    *           Paramétrage DFCE
    * @param uuid
    *           UUID du document à supprimer
    * @param log
    *           Logger
    * @param tracesSupport
    *           Support pour l'ecriture des traces
    * @throws DeletionServiceEx
    *            Exception levée si erreur lors de la suppression
    */
   public final void deleteStorageDocument(ServiceProvider dfceService,
         DFCEConnection cnxParams, final UUID uuid, Logger log,
         TracesDfceSupport tracesSupport) throws DeletionServiceEx {

      // -- Traces debug - entrée méthode
      String prefixeTrc = "deleteStorageDocument()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID à supprimer : {}", prefixeTrc, uuid);
         dfceService.getStoreService().deleteDocument(uuid);

         // -- Trace l'événement "Suppression d'un document de DFCE"
         tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         log.debug("{} - Sortie", prefixeTrc);

      } catch (FrozenDocumentException frozenExcept) {
         log
               .debug(
                     "{} - Une exception a été levée lors de la suppression du document : {}",
                     prefixeTrc, frozenExcept.getMessage());
         throw new DeletionServiceEx(StorageMessageHandler
               .getMessage(Constants.DEL_CODE_ERROR),
               frozenExcept.getMessage(), frozenExcept);
      }
   }

   /**
    * Vérification du Hash
    * 
    * @param documentContent
    *           Le document à vérifier
    * @param digestAlgo
    *           L'algo utilisé
    * @param fileName
    *           Le nom du fichier
    * @return Le hash
    * @throws IOException
    *            Exception IO
    * @throws NoSuchAlgorithmException
    *            Erreur si algo de calcul inconnu
    */
   private String checkHash(DataHandler documentContent, String digestAlgo,
         String fileName) throws IOException, NoSuchAlgorithmException {

      String digest;
      InputStream stream = null;
      String trcInsert = "insertStorageDocument()";
      try {
         stream = documentContent.getInputStream();
         digest = HashUtils.hashHex(stream, digestAlgo);
         LOGGER.debug("{} - Hash recalculé : {}", trcInsert, digest);
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
            LOGGER.info("{} - Erreur de fermeture du flux {}", new Object[] {
                  trcPrefix, name });
         }
      }
   }

   /**
    * Récupération de la base
    * 
    * @param dfceService
    *           Services de DFCE
    * @param cnxParameters
    *           Paramétrage DFCE
    * @return La base DFCE
    */
   public static Base getBaseDFCE(ServiceProvider dfceService,
         DFCEConnection cnxParameters) {
      return dfceService.getBaseAdministrationService().getBase(
            cnxParameters.getBaseName());
   }

   /**
    * Ajout d’une note à un document
    * 
    * @param dfceService
    *           Fournisseur de Services DFCE
    * @param docUuid
    *           UUID du document parent
    * @param contenu
    *           Contenu de la note
    * @param login
    *           Login utilisateur
    * @param dateCreation
    *           Date de création de la note
    * @param noteUuid
    *           UUID de la note
    * @param log
    *           Logger
    * @throws DocumentNoteServiceEx
    *            Une exception s'est produite lors de l'ajout d'une note à un
    *            document
    */
   public void addDocumentNote(ServiceProvider dfceService, UUID docUuid,
         String contenu, String login, Date dateCreation, UUID noteUuid,
         Logger log) throws DocumentNoteServiceEx {

      // -- Traces debug - entrée méthode
      String prefixeTrc = "addDocumentNote()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID document : {}", prefixeTrc, docUuid);
         log.debug("{} - Contenu note : {}", prefixeTrc, contenu);
         log.debug("{} - Login : {}", prefixeTrc, login);
         log.debug("{} - Date création : {}", prefixeTrc, dateCreation);
         log.debug("{} - UUID note : {}", prefixeTrc, noteUuid);
         
         Note note = new Note();
         // Ne fonctionne pas, DFCE reposissionne _ADMIN
         note.setAuthor(login);
         note.setContent(contenu);
         note.setDocUUID(docUuid);
         note.setCreationDate(dateCreation);
         note.setUuid(noteUuid);

         dfceService.getNoteService().storeNote(note);

         // -- Trace l'événement "Suppression d'un document de DFCE"
         // tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         log.debug("{} - Sortie", prefixeTrc);

      } catch (FrozenDocumentException frozenExcept) {
         log
               .debug(
                     "{} - Une exception a été levée lors de l'ajout d'une note à un document : {}",
                     prefixeTrc, frozenExcept.getMessage());
         throw new DocumentNoteServiceEx(
               "Erreur lors de l'ajout d'une note à un document", frozenExcept);
      } catch (TagControlException e) {
         log
               .debug(
                     "{} - Une exception a été levée lors de l'ajout d'une note à un document : {}",
                     prefixeTrc, e.getMessage());
         throw new DocumentNoteServiceEx(
               "Erreur lors de l'ajout d'une note à un document", e
                     .getMessage(), e);
      }

   }

   /**
    * Récupération de la liste des notes rattachées à un document
    * 
    * @param dfceService
    *           Fournisseur de Services DFCE
    * @param docUuid
    *           Identifiant du document dont on souhaite récupérer les notes
    * @param log
    *           Logger
    * @return La liste des notes rattachées au document
    */
   public List<StorageDocumentNote> getDocumentNotes(
         ServiceProvider dfceService, UUID docUuid, Logger log) {
      // -- Traces debug - entrée méthode
      String prefixeTrc = "getDocumentNotes()";
      log.debug("{} - Début", prefixeTrc);
      List<StorageDocumentNote> listeStorageDocNotes = new ArrayList<StorageDocumentNote>();

      log.debug("{} - UUID document : {}", prefixeTrc, docUuid);
      List<Note> listeNote = dfceService.getNoteService().getNotes(docUuid);

      for (Note note : listeNote) {
         listeStorageDocNotes.add(BeanMapper
               .dfceNoteToStorageDocumentNote(note));
      }

      // -- Trace l'événement "Suppression d'un document de DFCE"
      // tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

      log.debug("{} - Sortie", prefixeTrc);
      return listeStorageDocNotes;

   }
}
