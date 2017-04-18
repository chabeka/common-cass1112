package fr.urssaf.image.sae.services.transfert.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.service.CycleVieService;
import fr.urssaf.image.sae.trace.service.impl.JournalEvtServiceImpl;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe implémentant l'interface {@link SAETransfertService}. Cette classe est
 * un singleton et peut être accessible par le système d'injection IOC avec
 * l'annotation @Autowired
 * 
 */

@Service
public class SAETransfertServiceImpl extends AbstractSAEServices implements
      SAETransfertService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAETransfertServiceImpl.class);

   @Autowired
   private StorageDocumentService storageDocumentService;

   /**
    * Provider de service pour la connexion DFCE de la GNS
    */
   @Autowired
   private StorageTransfertService storageTransfertService;

   /**
    * Provider de service pour la connexion DFCE de la GNT
    */
   @Autowired
   private StorageServiceProvider storageServiceProvider;

   /**
    * Objet de manipulation des métadonnées du reférentiel des meta
    */
   @Autowired
   private MetadataReferenceDAO metadataReferenceDAO;

   /**
    * Liste des évents du cycle de vie des docs par id du doc
    */
   @Autowired
   private CycleVieService cycleVieService;

   @Autowired
   private ServiceProviderSupport traceServiceSupport;

   /**
    * Liste des events du SAE par id du doc
    */
   @Autowired
   private JournalEvtServiceImpl journalEvtService;

   @Autowired
   private MappingDocumentService mappingService;

   @Autowired
   private PrmdService prmdService;

   /**
    * @param idArchive
    * @throws ReferentialException
    * @throws RetrievalServiceEx
    * @throws InvalidSAETypeException
    * @throws MappingFromReferentialException
    *            Permet de vérifier les droits avant le transfert
    */
   public final void controleDroitTransfert(UUID idArchive)
         throws ReferentialException, RetrievalServiceEx,
         InvalidSAETypeException, MappingFromReferentialException {

      // On récupère les métadonnées du document à partir de l'UUID, avec
      // toutes les
      // métadonnées du référentiel sauf la note qui n'est pas utilise pour
      // les droits

      List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
      Map<String, MetadataReference> listeAllMeta = metadataReferenceDAO
            .getAllMetadataReferencesPourVerifDroits();
      for (String mapKey : listeAllMeta.keySet()) {
         allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
               .getShortCode()));
      }
      UUIDCriteria uuidCriteriaDroit = new UUIDCriteria(idArchive, allMeta);

      List<StorageMetadata> listeStorageMeta = storageDocumentService
            .retrieveStorageDocumentMetaDatasByUUID(uuidCriteriaDroit);

      if (listeStorageMeta.size() != 0) {
         List<UntypedMetadata> listeUMeta = mappingService
               .storageMetadataToUntypedMetadata(listeStorageMeta);

         // Vérification des droits
         LOG.debug("{} - Récupération des droits", "transfertDoc");
         AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
               .getContext().getAuthentication();
         List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert");
         LOG.debug("{} - Vérification des droits", "transfertDoc");
         boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

         if (!isPermitted) {
            throw new AccessDeniedException(
                  "Le document est refusé au transfert car les droits sont insuffisants");
         }
      }
   }

   /**
    * @param document
    * @param idArchive
    * @return
    * @throws ArchiveAlreadyTransferedException
    * @throws SearchingServiceEx
    * @throws ReferentialException
    * @throws ArchiveInexistanteEx
    * 
    *            Permet plusieurs controles avant le transfert : Document en GNT
    *            ? Document en GNS ?
    * @throws ConnectionServiceEx 
    */
   public final StorageDocument transfertControlePlateforme(
         StorageDocument document, UUID idArchive)
         throws ArchiveAlreadyTransferedException, SearchingServiceEx,
         ReferentialException, ArchiveInexistanteEx {

      // On récupère le document avec uniquement les méta transférables
      List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
      UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

      // -- Le document n'existe pas sur la GNT
      if (document == null) {
         // -- On recherche le document sur la GNS
         document = storageTransfertService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);

         String uuid = idArchive.toString();

         // -- Le document n'existe pas sur la GNS (et sur la GNT)
         if (document == null) {
            String message = "Le document {0} n'existe pas. Transfert impossible.";
            throw new ArchiveInexistanteEx(StringUtils.replace(message, "{0}",
                  uuid));
         } else {
           
            // -- Le document existe sur la GNS
            String message = "Le document {0} a déjà été transféré.";
            throw new ArchiveAlreadyTransferedException(StringUtils.replace(
                  message, "{0}", uuid));
         }
      } else {
         // -- Le document existe en GNT
         // -- On recherche le document sur la GNS
         StorageDocument documentGNS = storageTransfertService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);
         return documentGNS;
      }
   }

   /**
    * @param document
    * @param idArchive
    * @throws TransfertException
    * 
    *            Fonction de transfert du document avec notes et doc attachés
    */
   public final void transfertDocument(StorageDocument document)
         throws TransfertException {
      String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

      try {
         
         document = storageTransfertService
               .insertBinaryStorageDocument(document);

      
         
         // -- Récupération des notes associées au document transféré
         List<StorageDocumentNote> listeNotes = storageDocumentService
               .getDocumentsNotes(document.getUuid());
         // -- Ajout des notes sur le document archivés en GNS
         for (StorageDocumentNote note : listeNotes) {
            try {
               storageTransfertService.addDocumentNote(document.getUuid(),
                     note.getContenu(), note.getAuteur(),
                     note.getDateCreation(), note.getUuid());
            } catch (DocumentNoteServiceEx e) {
               // Les notes n'ont pas pu être transférées, on annule le
               // transfert (suppression du document en GNS)
               try {
                  storageTransfertService.deleteStorageDocument(document.getUuid());
               } catch (DeletionServiceEx erreurSupprGNS) {
                  throw new TransfertException(erreurSupprGNS);
               }
               throw new TransfertException(erreur, e);
            }
         }

         // -- Récupération du document attaché éventuel
         StorageDocumentAttachment docAttache;
         try {
            docAttache = storageDocumentService.getDocumentAttachment(document
                  .getUuid());
            // -- Ajout du document attaché sur le document archivés en
            // GNS
            if (docAttache != null) {
               storageTransfertService.addDocumentAttachment(
                     document.getUuid(), docAttache.getName(),
                     docAttache.getExtension(), docAttache.getContenu());
            }
         } catch (StorageDocAttachmentServiceEx e) {
            // Le document attaché n'a pas pu être transféré, on annule
            // le transfert (suppression du document en GNS)
            try {
               storageTransfertService.deleteStorageDocument(document.getUuid());
            } catch (DeletionServiceEx erreurSupprGNS) {
               throw new TransfertException(erreurSupprGNS);
            }
            throw new TransfertException(erreur, e);
         }

      } catch (InsertionServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (InsertionIdGedExistantEx e1) {
         throw new TransfertException(erreur, e1);
      }
   }

   /**
    * @param idArchive
    * @throws SearchingServiceEx
    * @throws ReferentialException
    * @throws TransfertException
    * 
    *            Fonction permet suppression doc sur GNT apres le transfert
    */
   public final void deleteDocApresTransfert(UUID idArchive)
         throws SearchingServiceEx, ReferentialException, TransfertException {
      // -- Suppression du document transféré de la GNT
      String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

      List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
      UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

      try {
         storageDocumentService.deleteStorageDocumentTraceTransfert(idArchive);
      } catch (DeletionServiceEx erreurSupprGNT) {
         StorageDocument documentGNT = storageDocumentService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);
         if (documentGNT != null) {
            // -- Le document existe toujours dans la GNT
            try {
               storageTransfertService.deleteStorageDocument(idArchive);
            } catch (DeletionServiceEx erreurSupprGNS) {
               throw new TransfertException(erreurSupprGNS);
            }
         }
         throw new TransfertException(erreur, erreurSupprGNT);
      }

   }

   /**
    * @param idArchive
    * @return
    * @throws ReferentialException
    * @throws SearchingServiceEx
    * 
    *            Permet de récupérer le document avec les metadonnées
    *            transférables
    */
   public final StorageDocument recupererDocMetaTransferable(UUID idArchive)
         throws ReferentialException, SearchingServiceEx {
      // On récupère le document avec uniquement les méta transférables
      List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
      UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

      StorageDocument document = storageDocumentService
            .searchStorageDocumentByUUIDCriteria(uuidCriteria);

      return document;
   }

   public final void transfertDocMasse(StorageDocument document) throws TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         ReferentialException, RetrievalServiceEx, InvalidSAETypeException,
         MappingFromReferentialException {

      // -- On trace le début du transfert
      String trcPrefix = "transfertDoc";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de transfert du document {}", new Object[] {
            trcPrefix, document.getUuid().toString() });

      String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

      try {
         this.transfertDocument(document);

         this.deleteDocApresTransfert(document.getUuid());


         LOG.debug("{} - Fin de transfert du document {}", new Object[] {
               trcPrefix, document.getUuid().toString() });

      } catch (SearchingServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (ReferentialException ex) {
         throw new TransfertException(erreur, ex);
      }
   }


   /**
    * Récupération de la liste des métadonnées transférables, depuis le
    * référentiel des métadonnées.
    * 
    * @return Liste des métadonnées
    * @throws ReferentialException
    * 
    */
   private List<StorageMetadata> getTransferableStorageMeta()
         throws ReferentialException {
      // -- Récupération de la liste des méta transférables du référentiel
      Map<String, MetadataReference> transferables = metadataReferenceDAO
            .getTransferableMetadataReference();

      Set<Entry<String, MetadataReference>> data = transferables.entrySet();
      List<StorageMetadata> metas = new ArrayList<StorageMetadata>();

      for (Map.Entry<String, MetadataReference> entry : data) {
         metas.add(new StorageMetadata(entry.getValue().getShortCode()));
      }

      // -- La métadonnée DateArchivage ne fait pas partie des métadonnées
      // transférable, mais nous en avons besoin pour
      // calculer la date d'archivage en GNT
      // on l'ajoute donc a la liste
      metas.add(new StorageMetadata(StorageTechnicalMetadatas.DATE_ARCHIVE
            .getShortCode()));

      return metas;
   }

   /**
    * Mise à jour des métadonnées du documents à transférer : <li>
    * DateArchivageGNT</li> <li>Supp. meta non transférables</li> <li>
    * TracePreArchivage</li>
    * 
    * @param document
    *           Document à transférer
    * @throws ReferentialException
    *            En cas d'erreur de récupérarion de la liste des méta
    *            transférables
    * @throws TransfertException
    *            En cas d'erreur de création des traces json
    */
   private void updateMetaDocumentForTransfert(StorageDocument document)
         throws ReferentialException, TransfertException {

      // -- Ajout métadonnée "DateArchivageGNT"
      Object dateArchivage = StorageMetadataUtils.valueObjectMetadataFinder(
            document.getMetadatas(),
            StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode());
      StorageMetadata dateArchivageGNT = new StorageMetadata(
            StorageTechnicalMetadatas.DATE_ARCHIVE_GNT.getShortCode(),
            dateArchivage);
      document.getMetadatas().add(dateArchivageGNT);

      // -- Suppression des métadonnées vides (impératif api dfce)
      // Supprime aussi la métadonnée DateArchivage qui est non transférable
      List<StorageMetadata> metadata = document.getMetadatas();
      for (int i = 0; i < metadata.size(); i++) {
         if (metadata.get(i).getValue() == null
               || metadata.get(i).getValue().equals("")
               || metadata
                     .get(i)
                     .getShortCode()
                     .equals(
                           StorageTechnicalMetadatas.DATE_ARCHIVE
                                 .getShortCode())) {
            metadata.remove(i);
            i--;
         }
      }

      // -- Ajout traces (preachivage) au document
      String traces = getTracePreArchivageAsJson(document);
      String shortCode = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE
            .getShortCode();
      document.getMetadatas().add(new StorageMetadata(shortCode, traces));
   }

   /**
    * @param document
    * @param listeMeta
    * @return
    * @throws ReferentialException
    * @throws TransfertException
    * 
    *            Methode permettant la modification de métadonnées avant le
    *            transfert pour le traitement de transfert de masse
    */
   public final StorageDocument updateMetaDocumentForTransfertMasse(
         StorageDocument document, List<StorageMetadata> listeMeta)
         throws ReferentialException, TransfertException {

      // -- Ajout métadonnée "DateArchivageGNT"
      Object dateArchivage = StorageMetadataUtils.valueObjectMetadataFinder(
            document.getMetadatas(),
            StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode());
      StorageMetadata dateArchivageGNT = new StorageMetadata(
            StorageTechnicalMetadatas.DATE_ARCHIVE_GNT.getShortCode(),
            dateArchivage);
      document.getMetadatas().add(dateArchivageGNT);

      // -- Suppression des métadonnées vides (impératif api dfce)
      // Supprime aussi la métadonnée DateArchivage qui est non transférable
      List<StorageMetadata> metadata = document.getMetadatas();
      for (int i = 0; i < metadata.size(); i++) {
         if (metadata.get(i).getValue() == null
               || metadata.get(i).getValue().equals("")
               || metadata
                     .get(i)
                     .getShortCode()
                     .equals(
                           StorageTechnicalMetadatas.DATE_ARCHIVE
                                 .getShortCode())) {
            metadata.remove(i);
            i--;
         }
      }

      // modification des métadonnées avant transfert
      if (!CollectionUtils.isEmpty(listeMeta)) {
         List<StorageMetadata> metadataMasse = new ArrayList<StorageMetadata>();
         Boolean bool = false;
         for (StorageMetadata meta : document.getMetadatas()) {
            for (StorageMetadata meta2 : listeMeta) {
               if (meta.getShortCode().equals(meta2.getShortCode())) {
                  metadataMasse.add(meta2);
                  bool = true;
               }
            }
            if (bool.equals(false)) {
               metadataMasse.add(meta);
            }
            bool = false;
         }
         document.setMetadatas(metadataMasse);
      }

      // -- Ajout traces (preachivage) au document
      String traces = getTracePreArchivageAsJson(document);
      String shortCode = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE
            .getShortCode();
      document.getMetadatas().add(new StorageMetadata(shortCode, traces));

      return document;
   }

   private String getTracePreArchivageAsJson(StorageDocument document)
         throws TransfertException {
      Map<String, String> mapTraces = new HashMap<String, String>();

      // -- Get json de la liste des events du cycle de vie DFCE
      String eventsDfce = getCycleVieDfceEventsAsJson(document.getUuid());
      mapTraces.put("traceDfce", eventsDfce);

      // -- Get json de la liste des events du jounal des events SAE
      String eventsSae = getJournalSaeEventsAsJson(document.getUuid());
      mapTraces.put("traceSae", eventsSae);

      // -- Get json de la traçabilité pré-archivage si présente
      for (int i = 0; i < document.getMetadatas().size(); i++) {
         StorageMetadata meta = document.getMetadatas().get(i);
         String codeCourt = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE
               .getShortCode();
         if (meta.getShortCode() == codeCourt) {
            mapTraces.put("tracePreArchivage", meta.getValue().toString());
            break;
         }
      }

      try {
         return new ObjectMapper().writeValueAsString(mapTraces);
      } catch (JsonGenerationException e) {
         throw new TransfertException(e);
      } catch (JsonMappingException e) {
         throw new TransfertException(e);
      } catch (IOException e) {
         throw new TransfertException(e);
      }
   }

   /**
    * Méthode conversion en json de la liste des évènements du cycle de vie DFCE
    * par identifiant de document. La liste est renvoyée au format Json.
    * 
    * @param idArchive
    * @return
    * @throws TransfertException
    */
   private String getCycleVieDfceEventsAsJson(final UUID idArchive)
         throws TransfertException {
      List<DfceTraceDoc> evtCycleVie = cycleVieService
            .lectureParDocument(idArchive);

      ObjectMapper mapper = new ObjectMapper();
      try {
         return mapper.writeValueAsString(evtCycleVie);
      } catch (JsonGenerationException e) {
         throw new TransfertException(e);
      } catch (JsonMappingException e) {
         throw new TransfertException(e);
      } catch (IOException e) {
         throw new TransfertException(e);
      }

   }

   /**
    * Récupératrion de la liste des évènements du jounal SAE des évents par
    * identifiant de document. La liste est renvoyée au format Json.
    * 
    * @param idArchive
    * @return
    * @throws TransfertException
    */
   private String getJournalSaeEventsAsJson(final UUID idArchive)
         throws TransfertException {
      List<TraceJournalEvtIndexDoc> evtSae = journalEvtService
            .getTraceJournalEvtByIdDoc(idArchive);
      ObjectMapper mapper = new ObjectMapper();
      try {
         return mapper.writeValueAsString(evtSae);
      } catch (JsonGenerationException e) {
         throw new TransfertException(e);
      } catch (JsonMappingException e) {
         throw new TransfertException(e);
      } catch (IOException e) {
         throw new TransfertException(e);
      }
   }
   
   public final StorageDocument controleDocumentTransfertMasse(UUID idArchive,
         List<StorageMetadata> StorageMetas) throws TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx {
      String erreur = "Une erreur interne à l'application est survenue lors du controle du transfert. Transfert impossible";
      StorageDocument document = new StorageDocument();
      
      try {
      document = recupererDocMetaTransferable(idArchive);

      StorageDocument documentGNS = transfertControlePlateforme(document, idArchive);

      if (documentGNS == null) {
         document = updateMetaDocumentForTransfertMasse(
               document, StorageMetas);
         document.setBatchTypeAction("TRANSFERT");
      } else {
         // -- Le document existe sur la GNS et sur la GNT
         String uuid = idArchive.toString();
         String message = "Le document {0} est anormalement présent en GNT et en GNS. Une intervention est nécessaire.";
         throw new ArchiveAlreadyTransferedException(StringUtils.replace(
               message, "{0}", uuid));
      }
      return document;
      
      } catch (SearchingServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (ReferentialException ex) {
         throw new TransfertException(erreur, ex);
      }
   }
   
   /**
    * {@inheritDoc}
    */
   public final void transfertDoc(UUID idArchive) throws TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx {

      // -- On trace le début du transfert
      String trcPrefix = "transfertDoc";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de transfert du document {}", new Object[] {
            trcPrefix, idArchive.toString() });

      String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

      try {
         // -- Ouverture des connections DFCE
         storageServiceProvider.openConnexion();
         storageTransfertService.openConnexion();
         traceServiceSupport.connect();

         // On récupère les métadonnées du document à partir de l'UUID, avec
         // toutes les
         // métadonnées du référentiel sauf la note qui n'est pas utilise pour les droits
         List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
         Map<String, MetadataReference> listeAllMeta = metadataReferenceDAO
               .getAllMetadataReferencesPourVerifDroits();
         for (String mapKey : listeAllMeta.keySet()) {
               allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
                     .getShortCode()));
         }
         UUIDCriteria uuidCriteriaDroit = new UUIDCriteria(idArchive, allMeta);

         List<StorageMetadata> listeStorageMeta = storageDocumentService
               .retrieveStorageDocumentMetaDatasByUUID(uuidCriteriaDroit);

         if (listeStorageMeta.size() != 0) {
            List<UntypedMetadata> listeUMeta = mappingService
                  .storageMetadataToUntypedMetadata(listeStorageMeta);

            // Vérification des droits
            LOG.debug("{} - Récupération des droits", trcPrefix);
            AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                  .getContext().getAuthentication();
            List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert");
            LOG.debug("{} - Vérification des droits", trcPrefix);
            boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

            if (!isPermitted) {
               throw new AccessDeniedException(
                     "Le document est refusé au transfert car les droits sont insuffisants");
            }
         }

         LOG.debug("{} - recherche du document", trcPrefix);
         // On récupère le document avec uniquement les méta transférables
         List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
         UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

         StorageDocument document = storageDocumentService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);

         // -- Le document n'existe pas sur la GNT
         if (document == null) {
            // -- On recherche le document sur la GNS
            document = storageTransfertService
                  .searchStorageDocumentByUUIDCriteria(uuidCriteria);

            String uuid = idArchive.toString();

            // -- Le document n'existe pas sur la GNS (et sur la GNT)
            if (document == null) {
               String message = "Le document {0} n'existe pas. Transfert impossible.";
               throw new ArchiveInexistanteEx(StringUtils.replace(message,
                     "{0}", uuid));
            } else {
               // -- Le document existe sur la GNS
               String message = "Le document {0} a déjà été transféré.";
               throw new ArchiveAlreadyTransferedException(StringUtils.replace(
                     message, "{0}", uuid));
            }
         } else {
            // -- Le document existe en GNT
            // -- On recherche le document sur la GNS
            StorageDocument documentGNS = storageTransfertService
                  .searchStorageDocumentByUUIDCriteria(uuidCriteria);

            // -- On s'assure que le document qu'on va transférer n'existe pas
            // sur la GNS
            if (documentGNS == null) {

               // -- Modification des métadonnées du document pour le transfert
               updateMetaDocumentForTransfert(document);

               // -- Archivage du document en GNS
               try {
                  documentGNS = storageTransfertService
                        .insertBinaryStorageDocument(document);

                  // -- Récupération des notes associées au document transféré
                  List<StorageDocumentNote> listeNotes = storageDocumentService
                        .getDocumentsNotes(document.getUuid());
                  // -- Ajout des notes sur le document archivés en GNS
                  for (StorageDocumentNote note : listeNotes) {
                     try {
                        storageTransfertService.addDocumentNote(
                              documentGNS.getUuid(), note.getContenu(),
                              note.getAuteur(), note.getDateCreation(),
                              note.getUuid());
                     } catch (DocumentNoteServiceEx e) {
                        // Les notes n'ont pas pu être transférées, on annule le
                        // transfert (suppression du document en GNS)
                        try {
                           storageTransfertService
                                 .deleteStorageDocument(idArchive);
                        } catch (DeletionServiceEx erreurSupprGNS) {
                           throw new TransfertException(erreurSupprGNS);
                        }
                        throw new TransfertException(erreur, e);
                     }
                  }

                  // -- Récupération du document attaché éventuel
                  StorageDocumentAttachment docAttache;
                  try {
                     docAttache = storageDocumentService
                           .getDocumentAttachment(document.getUuid());
                     // -- Ajout du document attaché sur le document archivés en
                     // GNS
                     if (docAttache != null) {
                        storageTransfertService.addDocumentAttachment(
                              documentGNS.getUuid(), docAttache.getName(),
                              docAttache.getExtension(),
                              docAttache.getContenu());
                     }
                  } catch (StorageDocAttachmentServiceEx e) {
                     // Le document attaché n'a pas pu être transféré, on annule
                     // le transfert (suppression du document en GNS)
                     try {
                        storageTransfertService
                              .deleteStorageDocument(idArchive);
                     } catch (DeletionServiceEx erreurSupprGNS) {
                        throw new TransfertException(erreurSupprGNS);
                     }
                     throw new TransfertException(erreur, e);
                  }

               } catch (InsertionServiceEx ex) {
                  throw new TransfertException(erreur, ex);
               } catch (InsertionIdGedExistantEx e1) {
                  throw new TransfertException(erreur, e1);
               }
            } else {
               // -- Le document existe sur la GNS et sur la GNT
               String uuid = idArchive.toString();
               String message = "Le document {0} est anormalement présent en GNT et en GNS. Une intervention est nécessaire.";
               throw new ArchiveAlreadyTransferedException(StringUtils.replace(
                     message, "{0}", uuid));
            }

            // -- Suppression du document transféré de la GNT
            try {
               storageDocumentService
                     .deleteStorageDocumentTraceTransfert(idArchive);
            } catch (DeletionServiceEx erreurSupprGNT) {
               StorageDocument documentGNT = storageDocumentService
                     .searchStorageDocumentByUUIDCriteria(uuidCriteria);
               if (documentGNT != null) {
                  // -- Le document existe toujours dans la GNT
                  try {
                     storageTransfertService.deleteStorageDocument(idArchive);
                  } catch (DeletionServiceEx erreurSupprGNS) {
                     throw new TransfertException(erreurSupprGNS);
                  }
               }
               throw new TransfertException(erreur, erreurSupprGNT);
            }

            LOG.debug("{} - Fin de transfert du document {}", new Object[] {
                  trcPrefix, idArchive.toString() });
         }
      } catch (ConnectionServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (SearchingServiceEx ex) {
         throw new TransfertException(erreur, ex);
      } catch (ReferentialException ex) {
         throw new TransfertException(erreur, ex);
      } catch (InvalidSAETypeException ex) {
         throw new TransfertException(erreur, ex);
      } catch (MappingFromReferentialException ex) {
         throw new TransfertException(erreur, ex);
      } catch (RetrievalServiceEx ex) {
         throw new TransfertException(erreur, ex);
      }
   }
}