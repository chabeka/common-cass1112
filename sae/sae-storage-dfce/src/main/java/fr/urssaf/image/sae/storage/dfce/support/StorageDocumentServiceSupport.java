package fr.urssaf.image.sae.storage.dfce.support;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.docubase.dfce.commons.document.StoreOptions;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.NoSuchAttachmentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.exception.DocumentTypeException;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Attachment;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.note.Note;

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

   @Value("${sae.nom.instance.plateforme}")
   private String nomPlateforme;

   /**
    * Insertion de document
    *
    * @param dfceServices
    *           Services de DFCE
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
    * @throws InsertionIdGedExistantEx
    */
   public StorageDocument insertBinaryStorageDocument(final DFCEServices dfceServices,
                                                      final DocumentsTypeList typeDocList, final StorageDocument storageDocument,
                                                      final Logger log, final TracesDfceSupport tracesSupport)
                                                            throws InsertionServiceEx, InsertionIdGedExistantEx {

      final Base base = dfceServices.getBase();
      final String trcInsert = "insertStorageDocument()";
      try {
         // -- ici on récupère le nom et l'extension du fichier
         final String[] file = new String[] {
                                             FilenameUtils.getBaseName(storageDocument.getFileName()),
                                             FilenameUtils.getExtension(storageDocument.getFileName()) };

         log.debug("{} - Enrichissement des métadonnées : "
               + "ajout de la métadonnée NomFichier valeur : {}.{}",
               new Object[] { trcInsert, file[0], file[1] });

         // -- conversion du storageDoc en DFCE Document
         final Document docDfce = BeanMapper.storageDocumentToDfceDocument(base,
                                                                           storageDocument, file);

         // -- ici on récupère le contenu du fichier.
         final DataHandler docContent = storageDocument.getContent();

         log.debug("{} - Début insertion du document dans DFCE", trcInsert);

         return insertDocumentInStorage(dfceServices, typeDocList,
                                        docDfce, docContent, file, storageDocument.getMetadatas(),
                                        tracesSupport);

      } catch (final InsertionServiceEx ex) {
         final String messg = StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR);
         throw new InsertionServiceEx(messg, ex.getMessage(), ex);
      } catch (final InsertionIdGedExistantEx ex) {
         throw ex;
      } catch (final Exception ex) {
         final String messg = StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR);
         throw new InsertionServiceEx(messg, ex.getMessage(), ex);
      }
   }

   /**
    * Insertion de document
    *
    * @param dfceServices
    *           Services de DFCE
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
    * @throws InsertionIdGedExistantEx
    */
   public StorageDocument insertDocumentInStorage(final DFCEServices dfceServices,
                                                  final DocumentsTypeList typeDocList, final Document docDfce,
                                                  final DataHandler documentContent, final String[] file,
                                                  final List<StorageMetadata> metadatas, final TracesDfceSupport tracesSupport)
                                                        throws InsertionServiceEx, InsertionIdGedExistantEx {

      final String trcInsert = "insertStorageDocument()";

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
         final String hashMeta = StringUtils.trim(StorageMetadataUtils
                                                  .valueMetadataFinder(metadatas,
                                                                       StorageTechnicalMetadatas.HASH.getShortCode()));
         final String typeHashMeta = StorageMetadataUtils.valueMetadataFinder(
                                                                              metadatas, StorageTechnicalMetadatas.TYPE_HASH.getShortCode());

         // Détermine le hash qu'il faut passer à DFCE pour qu'il le vérifie
         // lors de l'archivage
         String digest = null;
         if (dfceServices.getCnxParams().isCheckHash()) {

            // on récupère le paramètre général de l'algorithme de hachage des
            // documents dans DFCE
            final String digestAlgo = dfceServices.getCnxParams().getDigestAlgo();
            final String message = "{} - Algo de hash requis par DFCE pour la vérification du hash à l'archivage : {}";
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

         // recherche d'une note éventuelle
         String note = "";
         for (final StorageMetadata metadata : metadatas) {
            if (metadata.getShortCode().equals(
                                               StorageTechnicalMetadatas.NOTE.getShortCode())) {
               note = (String) metadata.getValue();
               break;
            }
         }

         // Appel de l'API DFCE pour l'archivage du document
         final Document docArchive = insertStorageDocument(dfceServices, docDfce,
                                                           digest, inputStream, hashMeta, typeHashMeta, tracesSupport,
                                                           note);

         // Trace
         LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})", trcInsert,
                      docArchive.getUuid());
         LOGGER.debug("{} - Fin insertion du document dans DFCE", trcInsert);
         LOGGER.debug("{} - Sortie", trcInsert);

         // Mapping d'objet pour passer l'objet Document de DFCE à l'objet
         // StorageDocument
         return BeanMapper.dfceDocumentToStorageDocument(docArchive, null,
                                                         dfceServices, false);

      } catch (final TagControlException tagCtrlEx) {

         throw new InsertionServiceEx(
                                      StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
                                      tagCtrlEx.getMessage(), tagCtrlEx);

      } catch (final InsertionIdGedExistantEx ex) {
         throw ex;
      } catch (final Exception except) {

         throw new InsertionServiceEx(
                                      StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
                                      except.getMessage(), except);
      } finally {
         // rien à faire
      }

   }

   /**
    *
    * Archivage du document dans DFCE ((Api DFCE)
    *
    * @param dfceServices
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
    * @throws InsertionServiceEx
    * @throws InsertionIdGedExistantEx
    */
   private Document insertStorageDocument(final DFCEServices dfceServices,
                                          final Document document, final String digest, final InputStream inputStream,
                                          final String hashPourTrace, final String typeHashPourTrace,
                                          final TracesDfceSupport tracesSupport, final String note)
                                                throws TagControlException, FrozenDocumentException,
                                                InsertionIdGedExistantEx {

      // On vérifie que le document n'existe pas déjà en base
      if (document.getUuid() != null) {
         if (dfceServices.getDocumentByUUID(document.getUuid()) != null) {
            final String mssg = "L'identifiant ged spécifié '%s' existe déjà et ne peut être utilisé.";
            throw new InsertionIdGedExistantEx(String.format(mssg,
                                                             document.getUuid()));
         }
      }

      Document doc;

      if (StringUtils.isEmpty(digest)) {
         doc = dfceServices.storeDocument(document, inputStream);

      } else {
         doc = dfceServices.storeDocument(document,
                                          new StoreOptions.Builder().verifyDigest(digest).build(), null,
                                          inputStream);
      }

      // Trace l'événement "Dépôt d'un document dans DFCE"
      tracesSupport.traceDepotDocumentDansDFCE(doc.getUuid(), hashPourTrace,
                                               typeHashPourTrace, doc.getArchivageDate());

      // Ajout de la note au document
      if (StringUtils.isNotEmpty(note)) {
         // Récupération du login fourni dans l'appel du service
         String login = "";
         if (SecurityContextHolder.getContext().getAuthentication() != null) {
            final VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
                  .getContext().getAuthentication().getPrincipal();
            login = extrait.getIdUtilisateur();
         }
         final Note noteDfce = new Note();
         noteDfce.setAlias(login);
         noteDfce.setContent(note);
         noteDfce.setDocUUID(doc.getUuid());
         dfceServices.storeNote(noteDfce);
      }
      return doc;
   }

   private void checkDocumentType(final DocumentsTypeList typeDocList,
                                  final List<StorageMetadata> metadatas) {

      final String docType = StorageMetadataUtils.valueMetadataFinder(metadatas,
                                                                      StorageTechnicalMetadatas.TYPE.getShortCode());

      if (!typeDocList.getTypes().contains(docType)) {
         final String message = "Impossible d'insérer le document, "
               + "son type n'est pas valide : {} ne fait pas partie des type référencés";
         throw new DocumentTypeException(StringUtils.replace(message, "{}",
                                                             docType));
      }
   }

   /**
    * Recherche de document par UUID
    *
    * @param dfceServices
    *           Services de DFCE
    * @param uUIDCriteria
    *           UUID du document recherché
    * @param log
    *           Logger
    * @return Le document trouvé
    * @throws SearchingServiceEx
    *            Exception levée lors d'erreur pendant la recherche
    */
   public StorageDocument searchStorageDocumentByUUIDCriteria(final DFCEServices dfceServices,
                                                              final UUIDCriteria uUIDCriteria, final boolean forConsultation, final Logger log)
                                                                    throws SearchingServiceEx {

      return searchStorageDocumentByUUIDCriteria(dfceServices,
                                                 uUIDCriteria,
                                                 forConsultation, true, log);
   }

   /**
    * Recherche de document par UUID
    *
    * @param dfceServices
    *           Services de DFCE
    * @param uUIDCriteria
    *           UUID du document recherché
    * @param log
    *           Logger
    * @param forConsultation
    *           : Paramètre pour remonter l'instance de la plateforme sur
    *           laquelle est réalisée la recherche
    * @param isDocContentAdd
    *           : Paramètre pour récupérer le contenue des documents pour la
    *           consultation.
    * @return Le document trouvé
    * @throws SearchingServiceEx
    *            Exception levée lors d'erreur pendant la recherche
    */
   public StorageDocument searchStorageDocumentByUUIDCriteria(final DFCEServices dfceServices,
                                                              final UUIDCriteria uUIDCriteria, final boolean forConsultation,
                                                              final boolean isDocContentAdd, final Logger log) throws SearchingServiceEx {
      try {

         // -- Traces debug - entrée méthode
         final String prefixeTrc = "searchStorageDocumentByUUIDCriteria()";
         log.debug("{} - Début", prefixeTrc);
         log.debug("{} - UUIDCriteria du document à consulter: {}", prefixeTrc,
                   uUIDCriteria.toString());
         // -- Fin des traces debug - entrée méthode
         log.debug("{} - Début de la recherche dans DFCE", prefixeTrc);
         final Document docDfce = dfceServices.getDocumentByUUID(uUIDCriteria.getUuid());
         log.debug("{} - Fin de la recherche dans DFCE", prefixeTrc);
         StorageDocument storageDoc = null;

         if (docDfce != null) {
            storageDoc = BeanMapper.dfceDocumentToStorageDocument(docDfce,
                                                                  uUIDCriteria.getDesiredStorageMetadatas(), dfceServices,
                                                                  nomPlateforme, forConsultation, isDocContentAdd);
         }
         log.debug("{} - Sortie", prefixeTrc);
         return storageDoc;

      } catch (final StorageException srcSerEx) {
         throw new SearchingServiceEx(
                                      StorageMessageHandler.getMessage(Constants.SRH_CODE_ERROR),
                                      srcSerEx.getMessage(), srcSerEx);
      } catch (final IOException ioExcept) {
         throw new SearchingServiceEx(
                                      StorageMessageHandler.getMessage(Constants.SRH_CODE_ERROR),
                                      ioExcept.getMessage(), ioExcept);
      } catch (final Exception except) {
         throw new SearchingServiceEx(
                                      StorageMessageHandler.getMessage(Constants.SRH_CODE_ERROR),
                                      except.getMessage(), except);
      }
   }

   /**
    * Recherche de document par UUID sans ajout du document binaire
    *
    * @param dfceService
    *           Services de DFCE
    * @param cnxParams
    *           Paramétrage DFCE
    * @param uUIDCriteria
    *           UUID du document recherché
    * @param log
    *           Logger
    * @param forConsultation
    *           : Paramètre pour remonter l'instance de la plateforme sur
    *           laquelle est réalisée la recherche
    * @return Le document trouvé
    * @throws SearchingServiceEx
    *            Exception levée lors d'erreur pendant la recherche
    */
   public StorageDocument searchStorageDocumentByUUIDCriteriaWithoutDocContent(final DFCEServices dfceServices,
                                                                               final UUIDCriteria uUIDCriteria, final boolean forConsultation, final Logger log)
                                                                                     throws SearchingServiceEx {
      return searchStorageDocumentByUUIDCriteria(dfceServices,
                                                 uUIDCriteria, forConsultation, false, log);
   }

   /**
    * Suppression de document
    *
    * @param dfceService
    *           Services de DFCE
    * @param uuid
    *           UUID du document à supprimer
    * @param log
    *           Logger
    * @param tracesSupport
    *           Support pour l'ecriture des traces
    * @throws DeletionServiceEx
    *            Exception levée si erreur lors de la suppression
    */
   public void deleteStorageDocument(final DFCEServices dfceServices,
                                     final UUID uuid, final Logger log,
                                     final TracesDfceSupport tracesSupport) throws DeletionServiceEx {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "deleteStorageDocument()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID à supprimer : {}", prefixeTrc, uuid);
         dfceServices.deleteDocument(uuid);

         // -- Trace l'événement "Suppression d'un document de DFCE"
         tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         log.debug("{} - Sortie", prefixeTrc);

      } catch (final FrozenDocumentException frozenExcept) {
         log.debug(
                   "{} - Une exception a été levée lors de la suppression du document : {}",
                   prefixeTrc, frozenExcept.getMessage());
         throw new DeletionServiceEx(
                                     StorageMessageHandler.getMessage(Constants.DEL_CODE_ERROR),
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
   private String checkHash(final DataHandler documentContent, final String digestAlgo,
                            final String fileName) throws IOException, NoSuchAlgorithmException {

      String digest;
      InputStream stream = null;
      final String trcInsert = "insertStorageDocument()";
      try {
         stream = documentContent.getInputStream();
         stream.mark(Integer.MAX_VALUE);
         digest = HashUtils.hashHex(stream, digestAlgo);
         stream.reset();
         LOGGER.debug("{} - Hash recalculé : {}", trcInsert, digest);
      } finally {
         // rien à faire
      }
      return digest;
   }

   


   /**
    * Ajout d’une note à un document
    *
    * @param dfceServices
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
   public void addDocumentNote(final DFCEServices dfceServices, final UUID docUuid,
                               final String contenu, final String login, final Date dateCreation, final UUID noteUuid,
                               final Logger log) throws DocumentNoteServiceEx {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "addDocumentNote()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID document : {}", prefixeTrc, docUuid);
         log.debug("{} - Contenu note : {}", prefixeTrc, contenu);
         log.debug("{} - Login : {}", prefixeTrc, login);
         log.debug("{} - Date création : {}", prefixeTrc, dateCreation);
         log.debug("{} - UUID note : {}", prefixeTrc, noteUuid);

         final Note note = new Note();
         note.setAlias(login);
         note.setContent(contenu);
         note.setDocUUID(docUuid);
         note.setCreationDate(dateCreation);
         note.setUuid(noteUuid);

         dfceServices.storeNote(note);

         // -- Trace l'événement "Suppression d'un document de DFCE"
         // tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         log.debug("{} - Sortie", prefixeTrc);

      } catch (final FrozenDocumentException frozenExcept) {
         log.debug(
                   "{} - Une exception a été levée lors de l'ajout d'une note à un document : {}",
                   prefixeTrc, frozenExcept.getMessage());
         throw new DocumentNoteServiceEx(
                                         "Erreur lors de l'ajout d'une note à un document", frozenExcept);
      } catch (final TagControlException e) {
         log.debug(
                   "{} - Une exception a été levée lors de l'ajout d'une note à un document : {}",
                   prefixeTrc, e.getMessage());
         throw new DocumentNoteServiceEx(
                                         "Erreur lors de l'ajout d'une note à un document",
                                         e.getMessage(), e);
      }

   }

   /**
    * Récupération de la liste des notes rattachées à un document
    *
    * @param dfceServices
    *           Fournisseur de Services DFCE
    * @param docUuid
    *           Identifiant du document dont on souhaite récupérer les notes
    * @param log
    *           Logger
    * @return La liste des notes rattachées au document
    */
   public List<StorageDocumentNote> getDocumentNotes(final DFCEServices dfceServices, final UUID docUuid, final Logger log) {
      // -- Traces debug - entrée méthode
      final String prefixeTrc = "getDocumentNotes()";
      log.debug("{} - Début", prefixeTrc);
      final List<StorageDocumentNote> listeStorageDocNotes = new ArrayList<StorageDocumentNote>();

      log.debug("{} - UUID document : {}", prefixeTrc, docUuid);
      final List<Note> listeNote = dfceServices.getNotes(docUuid);

      for (final Note note : listeNote) {
         listeStorageDocNotes.add(BeanMapper
                                  .dfceNoteToStorageDocumentNote(note));
      }

      log.debug("{} - Sortie", prefixeTrc);
      return listeStorageDocNotes;

   }

   /**
    * Permet de rajouter un « document attaché » à un document
    *
    * @param dfceServices
    *           Fournisseur de Services DFCE
    * @param docUuid
    *           identifiant du document auquel on souhaite ajouter une pièce
    *           jointe
    * @param docName
    *           le nom de la pièce jointe
    * @param extension
    *           l’extension de la pièce jointe
    * @param hash
    *           Empreinte de contrôle du fichier joint
    * @param contenu
    *           Contenu du doc à attacher
    * @param log
    *           Logger
    * @throws StorageDocAttachmentServiceEx
    *            Une erreur s’est produite lors de l’ajout d’un document attaché
    *            au document
    */
   public void addDocumentAttachment(final DFCEServices dfceServices, final UUID docUuid, final String docName,
                                     final String extension, final DataHandler contenu, final Logger log,
                                     final TracesDfceSupport tracesSupport) throws StorageDocAttachmentServiceEx {
      // -- Traces debug - entrée méthode
      final String prefixeTrc = "addDocumentAttachment()";
      log.debug("{} - Début", prefixeTrc);

      try {
         // Calcul du hash
         final String digestAlgo = dfceServices.getCnxParams().getDigestAlgo();
         final String hash = checkHash(contenu, digestAlgo, docName);

         final InputStream docStream = contenu.getInputStream();
         final Document doc = dfceServices.addAttachment(docUuid,
                                                         docName, extension, false, hash, docStream);

         // Trace l'événement "Dépôt d'un document attaché dans DFCE"
         final Set<Attachment> listeAttach = doc.getAttachments();
         for (final Attachment attachment : listeAttach) {
            tracesSupport.traceDepotAttachmentDansDFCE(docUuid,
                                                       attachment.getDigest(), attachment.getDigestAlgorithm(),
                                                       attachment.getArchivageDate());
            break;
         }

      } catch (final FrozenDocumentException e) {
         log.debug(
                   "{} - Une exception a été levée lors de l'ajout d'un document attaché : {}",
                   prefixeTrc, e.getMessage());
         throw new StorageDocAttachmentServiceEx(
                                                 "Erreur lors de l'ajout d'un document attaché", e.getMessage(),
                                                 e);
      } catch (final TagControlException e) {
         log.debug(
                   "{} - Une exception a été levée lors de l'ajout d'un document attaché : {}",
                   prefixeTrc, e.getMessage());
         throw new StorageDocAttachmentServiceEx(
                                                 "Erreur lors de l'ajout d'un document attaché", e.getMessage(),
                                                 e);
      } catch (final IOException e) {
         log.debug(
                   "{} - Une exception a été levée lors de l'ajout d'un document attaché : {}",
                   prefixeTrc, e.getMessage());
         throw new StorageDocAttachmentServiceEx(
                                                 "Erreur lors de l'ajout d'un document attaché", e.getMessage(),
                                                 e);
      } catch (final NoSuchAlgorithmException e) {
         log.debug(
                   "{} - Une exception a été levée lors de l'ajout d'un document attaché : {}",
                   prefixeTrc, e.getMessage());
         throw new StorageDocAttachmentServiceEx(
                                                 "Erreur lors de l'ajout d'un document attaché", e.getMessage(),
                                                 e);
      }

      log.debug("{} - Sortie", prefixeTrc);

   }

   /**
    * Méthode de récupération d’un document attaché (binaire)
    *
    * @param dfceServices
    *           Fournisseur de Services DFCE
    * @param docUuid
    *           UUID du document concerné
    * @param log
    *           Logger
    * @return la liste des documents attachés
    * @throws StorageDocAttachmentServiceEx
    */
   public StorageDocumentAttachment getDocumentAttachment(final DFCEServices dfceServices, final UUID docUuid,
                                                          final Logger log) throws StorageDocAttachmentServiceEx {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "getDocumentAttachment()";
      log.debug("{} - Début", prefixeTrc);

      try {
         final Document docDfce = dfceServices.getDocumentByUUID(docUuid);

         if (docDfce != null) {
            final Set<Attachment> listeAttachement = docDfce.getAttachments();

            StorageDocumentAttachment storageDocAtt = null;
            // Si le document possède un document attaché
            if (listeAttachement.size() > 0) {
               Attachment attachment = new Attachment();
               for (final Attachment att : listeAttachement) {
                  attachment = att;
                  break;
               }

               final InputStream inputStream = dfceServices.getAttachmentFile(docDfce, attachment);

               final InputStreamSource source = new InputStreamSource(inputStream);
               final DataHandler contenu = new DataHandler(source);

               storageDocAtt = new StorageDocumentAttachment(docUuid,
                                                             attachment.getFilename(), attachment.getExtension(),
                                                             attachment.getDigest(), attachment.getArchivageDate(),
                                                             contenu);

               log.debug("{} - Sortie", prefixeTrc);
            }
            return storageDocAtt;
         } else {
            log.debug(
                      "{} - Une exception a été levée lors de la récupération du document au format d'origine : le document parent {} n'existe pas",
                      prefixeTrc, docUuid);
            final String message = "Erreur lors de la récupération du document au format d'origine : le document parent "
                  + docUuid + " n'existe pas";
            throw new StorageDocAttachmentServiceEx(message);
         }

      } catch (final NoSuchAttachmentException e) {
         log.debug(
                   "{} - Une exception a été levée lors de la récupération d'un document attaché : {}",
                   prefixeTrc, e.getMessage());
         throw new StorageDocAttachmentServiceEx(
                                                 "Erreur lors de la récupération du document au format d'origine",
                                                 e.getMessage(), e);
      }

   }

   /**
    * Déplacement de document dans la corbeille
    *
    * @param dfceServices
    *           Services de DFCE
    * @param uuid
    *           UUID du document à déplacer
    * @param log
    *           Logger
    * @param tracesSupport
    *           Support pour l'ecriture des traces
    * @throws RecycleBinServiceEx
    *            Exception levée si erreur lors de la mise a la corbeille
    */
   public void moveStorageDocumentToRecycleBin(final DFCEServices dfceServices,
                                               final UUID uuid, final Logger log, final TracesDfceSupport tracesSupport)
                                                     throws RecycleBinServiceEx {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "moveStorageDocumentToRecycleBin()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID à mettre dans la corbeille : {}", prefixeTrc,
                   uuid);
         final Document docInRB = dfceServices.throwAwayDocument(uuid);

         // -- Trace l'événement "Mise en corbeille d'un document de DFCE"
         tracesSupport.traceCorbeilleDocDansDFCE(uuid, docInRB.getDigest(),
                                                 docInRB.getDigestAlgorithm(), docInRB.getArchivageDate());

         log.debug("{} - Sortie", prefixeTrc);

      } catch (final FrozenDocumentException frozenExcept) {
         log.debug(
                   "{} - Une exception a été levée lors de la suppression du document : {}",
                   prefixeTrc, frozenExcept.getMessage());
         throw new RecycleBinServiceEx(
                                       StorageMessageHandler.getMessage(Constants.COR_CODE_ERROR),
                                       frozenExcept.getMessage(), frozenExcept);
      }
   }

   /**
    * Restore de document de la corbeille
    *
    * @param dfceServices
    *           Services de DFCE
    * @param uuid
    *           UUID du document à restaurer
    * @param log
    *           Logger
    * @param tracesSupport
    *           Support pour l'ecriture des traces
    * @throws RecycleBinServiceEx
    *            Exception levée si erreur lors de la restore de la corbeille
    */
   public void restoreStorageDocumentFromRecycleBin(final DFCEServices dfceServices,
                                                    final UUID uuid, final Logger log, final TracesDfceSupport tracesSupport)
                                                          throws RecycleBinServiceEx {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "restoreStorageDocumentFromRecycleBin()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID à restaurer de la corbeille : {}", prefixeTrc,
                   uuid);
         final Document docRestore = dfceServices.restoreDocument(uuid);

         // -- Trace l'événement "Restore d'un document de la corbeille de DFCE"
         tracesSupport.traceRestoreDocDansDFCE(uuid, docRestore.getDigest(),
                                               docRestore.getDigestAlgorithm(), docRestore.getArchivageDate());

         log.debug("{} - Sortie", prefixeTrc);

      } catch (final TagControlException frozenExcept) {
         log.debug(
                   "{} - Une exception a été levée lors de la suppression du document : {}",
                   prefixeTrc, frozenExcept.getMessage());
         throw new RecycleBinServiceEx(
                                       StorageMessageHandler.getMessage(Constants.RST_CODE_ERROR),
                                       frozenExcept.getMessage(), frozenExcept);
      }
   }

   /**
    * Récupérer le document de la corbeille
    * @param dfceServices
    * @param uuid
    * @param log
    * @param tracesSupport
    * @return
    */
   public Document getDocumentFromRecycleBin(final DFCEServices dfceServices, final UUID uuid,
                                             final Logger log, final TracesDfceSupport tracesSupport) {
      // -- Traces debug - entrée méthode
      final String prefixeTrc = "getStorageDocumentFromRecycleBin()";
      log.debug("{} - Début", prefixeTrc);

      final Document document = dfceServices.getDocumentByUUIDFromRecycleBin(uuid);

      return document;
   }


   /**
    * Suppression de document de la corbeille
    *
    * @param dfceServices
    *           Services de DFCE
    * @param uuid
    *           UUID du document à supprimer
    * @param log
    *           Logger
    * @param tracesSupport
    *           Support pour l'ecriture des traces
    * @throws RecycleBinServiceEx
    *            Exception levée si erreur lors de la restore de la corbeille
    */
   public void deleteStorageDocumentFromRecycleBin(final DFCEServices dfceServices,
                                                   final UUID uuid, final Logger log, final TracesDfceSupport tracesSupport)
                                                         throws RecycleBinServiceEx {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "deleteStorageDocumentFromRecycleBin()";
      log.debug("{} - Début", prefixeTrc);

      try {
         log.debug("{} - UUID à supprimer de la corbeille : {}", prefixeTrc,
                   uuid);
         dfceServices.deleteDocumentFromRecycleBin(uuid);

         // -- Trace l'événement "Suppression d'un document de DFCE"
         tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         log.debug("{} - Sortie", prefixeTrc);

      } catch (final FrozenDocumentException frozenExcept) {
         log.debug(
                   "{} - Une exception a été levée lors de la suppression du document : {}",
                   prefixeTrc, frozenExcept.getMessage());
         throw new RecycleBinServiceEx(
                                       StorageMessageHandler.getMessage(Constants.DEL_CODE_ERROR),
                                       frozenExcept.getMessage(), frozenExcept);
      }
   }

   /**
    * Methode permettant de transformer un {@link Document} en
    * {@link StorageDocument}
    *
    * @param doc
    *           Document
    * @param desiredStorageMetadatas
    *           metadonnées désirées
    * @param dfceServices
    *           Service DFCE
    * @return Le document de type {@link StorageDocument}.
    * @throws IOException
    * @{@link IOException}
    * @throws StorageException
    * @{@link StorageException}
    */
   public StorageDocument getStorageDocument(final Document doc,
                                             final List<StorageMetadata> desiredStorageMetadatas,
                                             final DFCEServices dfceServices, final boolean b) throws StorageException,
   IOException {
      // -- Traces debug - entrée méthode
      final String prefixeTrc = "getStorageDocument()";
      return BeanMapper.dfceDocumentFromRecycleBinToStorageDocument(doc,
                                                                    desiredStorageMetadatas, dfceServices, false, false);
   }

}
