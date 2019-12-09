package fr.urssaf.image.sae.services.transfert.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.service.RndService;
import fr.urssaf.image.sae.services.controles.SAEControlesModificationService;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertMasseRuntimeException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
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
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.service.CycleVieService;
import fr.urssaf.image.sae.trace.service.implthrift.JournalEvtServiceThriftImpl;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe implémentant l'interface {@link SAETransfertService}. Cette classe est
 * un singleton et peut être accessible par le système d'injection IOC avec
 * l'annotation @Autowired
 */
@Service
public class SAETransfertServiceImpl extends AbstractSAEServices implements SAETransfertService {

   /**
    * Logger
    */
   private static final Logger LOG = LoggerFactory.getLogger(SAETransfertServiceImpl.class);

   /**
    * Provider de service pour l'archivage de document
    */
   @Autowired
   private StorageDocumentService storageDocumentService;

   /**
    * Provider de service pour la connexion DFCE de la GNS
    */
   @Autowired
   private StorageTransfertService storageTransfertService;

   /**
    * Objet de manipulation des métadonnées du référentiel des meta
    */
   @Autowired
   private MetadataReferenceDAO metadataReferenceDAO;

   @Autowired
   private RndService rndService;

   /**
    * Liste des évents du cycle de vie des docs par id du doc
    */
   @Autowired
   private CycleVieService cycleVieService;

   /**
    * Liste des events du SAE par id du doc
    */
   @Autowired
  private JournalEvtServiceThriftImpl journalEvtService;

   /**
    * Service pour le mapping de document
    */
   @Autowired
   private MappingDocumentService mappingService;

   /**
    * ControleModificationService
    */
   @Autowired
   private SAEControlesModificationService controleModification;

   /**
    *  
    */
   @Autowired
   ZookeeperClientFactory zookeeperCfactory;

   /**
    * Service pour la verifications des PRMD
    */
   @Autowired
   private PrmdService prmdService;

   private static final String GENERIC_ERROR_STRING = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

   /**
    * Permet de vérifier les droits avant le transfert de masse
    * 
    * @param allMeta
    * @throws ReferentialException
    * @throws RetrievalServiceEx
    * @throws InvalidSAETypeException
    * @throws MappingFromReferentialException
    */
   private final void controleDroitTransfertMasse(final List<StorageMetadata> allMeta)
         throws InvalidSAETypeException, MappingFromReferentialException {

      if (!allMeta.isEmpty()) {
         // Vérification des droits
         LOG.debug("{} - Récupération des droits", "controleDroitTransfertMasse");
         final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext()
               .getAuthentication();
         final List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert_masse");
         LOG.debug("{} - Vérification des droits", "controleDroitTransfertMasse");
         final List<UntypedMetadata> listeUMeta = mappingService.storageMetadataToUntypedMetadata(allMeta);
         final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

         if (!isPermitted) {
            throw new AccessDeniedException("Le document est refusé au transfert car les droits sont insuffisants");
         }
      }
   }

   /**
    * Dans le cadre d'un traitement de transfert de masse.
    * Renvoie des exceptions sur le documents ne se trouve pas en GNT.
    * 
    * @param documentGNT
    *           document GNT, ou null s'il n'est pas en GNT
    * @param documentGNS
    *           document GNS, ou null s'il n'est pas en GNS
    * @param idArchive
    *           identifiant du document
    * @param isReprise
    *           Mode reprise actif
    * @param idTraitementMasse
    *           Identifiant du traitement de masse en cours
    */
   private final void handleErrorsIfDocNotInGNT(final StorageDocument documentGNT, final StorageDocument documentGNS, final UUID idArchive,
         final boolean isReprise, final UUID idTraitementMasse)
               throws ArchiveAlreadyTransferedException, ArchiveInexistanteEx, TraitementRepriseAlreadyDoneException {

      // Pas de contrôle à faire si le document est bien en GNT
      if (documentGNT != null) {
         return;
      }

      // On est dans le cas ou le document n'existe pas en GNT
      final String uuid = idArchive.toString();

      if (documentGNS == null) {
         // -- Le document n'existe pas sur la GNS (et sur la GNT non plus)
         final String message = format("Le document {} n'existe pas. Transfert impossible.", uuid);
         throw new ArchiveInexistanteEx(message);
      }

      // Le document existe en GNS, et pas en GNT
      if (isReprise) {
         if (idTraitementMasse == null) {
            // Ça n'est pas censé arriver, car on est dans le cadre d'un transfert de masse
            final String message = format("L'identifiant du traitement de masse est inexistant pour le contrôle du document {}", uuid);
            throw new TransfertMasseRuntimeException(message);
         }
         if (ckeckIdTraitement(documentGNS.getMetadatas(), idTraitementMasse)) {
            // -- Le document a déjà été transféré dans la GNS par le bon traitement de masse
            final String message = format("Le document {} a déjà été transféré par le traitement de masse en cours {}", uuid, idTraitementMasse);
            throw new TraitementRepriseAlreadyDoneException(message);
         }
         // -- Le document existe sur la GNS, mais n'a pas été transféré par le traitement en cours
         final String message = format("Le document {} a déjà été transféré.", uuid);
         throw new ArchiveAlreadyTransferedException(message);
      } else {
         // -- Le document existe sur la GNS, et on n'est pas en mode reprise. Ce n'est pas normal
         final String message = format("Le document {} a déjà été transféré.", uuid);
         throw new ArchiveAlreadyTransferedException(message);
      }
   }

   /**
    * Renvoie le document GNS, s'il existe, avec uniquement les métadonnées utiles aux contrôles
    * 
    * @param idArchive
    *           Id du document à chercher
    * @return
    *         Le StorageDocument en GNS, ou null s'il n'y est pas
    */
   private StorageDocument getDocInGNS(final UUID idArchive) throws SearchingServiceEx {
      final List<StorageMetadata> metas = new ArrayList<>();
      metas.add(new StorageMetadata(StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode()));
      metas.add(new StorageMetadata(StorageTechnicalMetadatas.HASH.getShortCode()));
      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, metas);
      final StorageDocument documentGNS = storageTransfertService.searchStorageDocumentByUUIDCriteria(uuidCriteria);
      return documentGNS;
   }

   private static String format(final String msg, final Object... objs) {
      return MessageFormatter.arrayFormat(msg, objs).getMessage();
   }

   /**
    * Vérifie que l'identifiant du traitement existe dans la liste des métadonnées,
    * et que sa valeur est égale à idTraitementMasse
    * 
    * @param metadatas
    *           Liste des métadonnées
    * @param idTraitementMasse
    *           Identifiant de traitement qu'on s'attend à avoir
    * @return True si l'identifiant du traitement a été trouvé dans la liste des métadonnées, false sinon.
    */
   private boolean ckeckIdTraitement(final List<StorageMetadata> metadatas, final UUID idTraitementMasse) {
      for (final StorageMetadata storageMetadata : Utils.nullSafeIterable(metadatas)) {
         if (Constantes.CODE_COURT_META_ID_TRANSFERT.equals(storageMetadata.getShortCode())) {
            if (storageMetadata.getValue() != null
                  && idTraitementMasse.toString().equals(storageMetadata.getValue().toString())) {
               return true;
            }
            break;
         }
      }
      return false;
   }

   /**
    * Permet de récupérer le document en GNT, avec presque l'ensemble des métadonnées, sauf la date
    * de conservation et les notes
    * 
    * @param idArchive
    * @return le document trouvé
    */
   private final StorageDocument recupererDocWithAlmostAllMetaAndGel(final UUID idArchive)
         throws ReferentialException, SearchingServiceEx {
      final List<StorageMetadata> desiredMetas = getAlmostAllMetaWithGel();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

      return storageDocumentService.searchStorageDocumentByUUIDCriteria(uuidCriteria);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void transfertDocMasse(final StorageDocument document)
         throws TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx, ReferentialException,
         RetrievalServiceEx, InvalidSAETypeException, MappingFromReferentialException {

      // -- On trace le début du transfert
      final String trcPrefix = "transfertDocMasse";
      LOG.debug("{} - Début de transfert du document {}", new Object[] {trcPrefix, document.getUuid()});

      final String uuid = document.getUuid().toString();
      final ZookeeperMutex mutex = new ZookeeperMutex(zookeeperCfactory.getClient(), "/Transfert/" + uuid);

      try {

         // Lock du document
         ZookeeperUtils.acquire(mutex, uuid);

         sendToGNS(document);

         deleteFromGNT(document.getUuid());

         // A la fin on vérifie qu'on à toujours le lock
         if (!ZookeeperUtils.isLock(mutex)) {
            // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne
            // devrait jamais arriver.
            LOG.warn("Erreur lors de la tentative d'acquisition du lock pour le transfert de masse {}. Problème de connexion zookeeper ?",
                  uuid);
         }
         LOG.debug("{} - Fin de transfert du document {}", new Object[] {trcPrefix, document.getUuid()});

      }
      catch (final SearchingServiceEx | InsertionServiceEx | InsertionIdGedExistantEx ex) {
         final String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible : ";
         LOG.error("{} {} - {}", new Object[] {erreur, ex.getCause(), ex.getMessage()});
         throw new TransfertException(ex.getMessage(), ex);
      }
      finally {
         mutex.release();
      }
   }

   /**
    * Récupération de la liste des métadonnées transférables, depuis le référentiel des métadonnées.
    *
    * @return Liste des métadonnées
    * @throws ReferentialException
    */
   private List<StorageMetadata> getTransferableStorageMeta() throws ReferentialException {
      // -- Récupération de la liste des méta transférables du référentiel
      final Map<String, MetadataReference> transferables = metadataReferenceDAO.getTransferableMetadataReference();

      final Set<Entry<String, MetadataReference>> data = transferables.entrySet();
      final List<StorageMetadata> metas = new ArrayList<>();

      for (final Map.Entry<String, MetadataReference> entry : data) {
         metas.add(new StorageMetadata(entry.getValue().getShortCode()));
      }
      return metas;
   }

   /**
    * Récupération une liste de métadonnées utilisée uniquement pour test d'existence d'un document
    *
    * @return Liste des métadonnées
    * @throws ReferentialException
    */
   private List<StorageMetadata> getMetadatasForExistenceCheck() {
      final List<StorageMetadata> metas = new ArrayList<>();
      metas.add(new StorageMetadata(StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode()));
      return metas;
   }

   /**
    * Récupération de la liste de presque toutes les métadonnées depuis le
    * référentiel des métadonnées.
    *
    * @return Liste des métadonnées
    * @throws ReferentialException
    */
   private List<StorageMetadata> getAlmostAllMetaWithGel() throws ReferentialException {
      // -- Récupération de la liste des méta transférables du référentiel
      final Map<String, MetadataReference> transferables = metadataReferenceDAO.getAllMetadataReferencesPourVerifDroits();

      final Set<Entry<String, MetadataReference>> data = transferables.entrySet();
      final List<StorageMetadata> metas = new ArrayList<>();

      for (final Map.Entry<String, MetadataReference> entry : data) {
         metas.add(new StorageMetadata(entry.getValue().getShortCode()));
      }

      // On rajoute la méta GEL dont on a besoin
      metas.add(new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode()));

      return metas;
   }

   /**
    * Ajout de métadonnées sur le document à transférer :
    * <li>DateArchivageGNT</li>
    * <li>TracePreArchivage</li>
    *
    * @param document
    *           Document à transférer
    * @throws ReferentialException
    *            En cas d'erreur de récupération de la liste des méta
    *            transférables
    * @throws TransfertException
    *            En cas d'erreur de création des traces json
    */
   private void addMetaDocumentForTransfert(final StorageDocument document)
         throws TransfertException {

      // -- Ajout métadonnée "DateArchivageGNT"
      final Object dateArchivage = StorageMetadataUtils.valueObjectMetadataFinder(document.getMetadatas(),
            StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode());
      final StorageMetadata dateArchivageGNT = new StorageMetadata(StorageTechnicalMetadatas.DATE_ARCHIVE_GNT.getShortCode(),
            dateArchivage);
      document.getMetadatas().add(dateArchivageGNT);

      // -- Ajout traces (preachivage) au document
      final String traces = getTracePreArchivageAsJson(document);
      final String shortCode = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE.getShortCode();
      document.getMetadatas().add(new StorageMetadata(shortCode, traces));

   }

   /**
    * Méthode permettant de modifier les métadonnées du document avant transfert.
    * 
    * @param document
    *           Document à transférer
    * @param listeMeta
    *           Liste des métadonnées fournies par le client. Il s'agit de méta que le client veut modifier ou supprimer
    * @param idTraitementMasse
    *           Identifiant du traitement qui réalise le transfert
    * @return Le document transférable.
    * @throws TransfertException
    * @{@link TransfertException}
    */
   private final StorageDocument updateMetaDocumentForTransfertMasse(final StorageDocument document, final List<UntypedMetadata> listeMetaClient)
         throws TransfertException {

      try {
         if (!CollectionUtils.isEmpty(listeMetaClient)) {
            // On vérifie que les métadonnées supprimées par le client ne sont pas requises au stockage
            final List<UntypedMetadata> deletedMetadatas = getDeteledMetadatas(listeMetaClient);
            controleModification.checkNonRequisStockages(deletedMetadatas);

            // On modifie/supprime les métadonnées à la demande du client
            final List<StorageMetadata> storageMetasClient = mappingService.untypedMetadatasToStorageMetadatas(listeMetaClient);

            final List<StorageMetadata> docMetadatas = document.getMetadatas();
            for (final StorageMetadata clientMetadata : storageMetasClient) {
               if (clientMetadata.getValue() == null) {
                  deleteMetadata(docMetadatas, clientMetadata.getShortCode());
               } else {
                  modifyMetadata(docMetadatas, clientMetadata);
               }
            }
         }
      }
      catch (InvalidSAETypeException | MappingFromReferentialException | RequiredStorageMetadataEx e) {
         throw new TransfertException(e);
      }

      return document;
   }

   /**
    * Modifie la valeur d'une métadonnée dans une liste. Si la méta n'est pas présente dans la liste, on l'ajoute
    * 
    * @param docMetadatas
    *           La liste des métadonnées
    * @param clientMetadata
    *           La métadonnée à ajouter/modifier
    */
   private void modifyMetadata(final List<StorageMetadata> docMetadatas, final StorageMetadata clientMetadata) {
      // On cherche la métadonnée et on la modifie si on la trouve
      for (int i = 0; i < docMetadatas.size(); i++) {
         if (docMetadatas.get(i).getShortCode().equals(clientMetadata.getShortCode())) {
            docMetadatas.get(i).setValue(clientMetadata.getValue());
            return;
         }
      }
      // La métadonnée n'a pas été trouvée. On l'ajoute
      docMetadatas.add(new StorageMetadata(clientMetadata.getShortCode(), clientMetadata.getValue()));
   }

   /**
    * Supprime une métadonnée de la liste
    * 
    * @param docMetadatas
    *           La liste
    * @param longCode
    *           Le code long de la méta à supprimer
    */
   private void deleteMetadata(final List<StorageMetadata> docMetadatas, final String metaShortCodeToRemove) {
      for (int i = 0; i < docMetadatas.size(); i++) {
         if (docMetadatas.get(i).getShortCode().equals(metaShortCodeToRemove)) {
            docMetadatas.remove(i);
         }
      }
   }

   /**
    * Parmi les métadonnées fournies par le client, renvoie celles qui correspondent à des demandes de suppression
    * 
    * @param listeMetaClient
    *           Les métadonnées fournies par le client
    * @return
    */
   private List<UntypedMetadata> getDeteledMetadatas(final List<UntypedMetadata> listeMetaClient) {
      final List<UntypedMetadata> result = new ArrayList<>();
      for (final UntypedMetadata metadata : Utils.nullSafeIterable(listeMetaClient)) {
         if (metadata.getValue() == null || metadata.getValue().isEmpty()) {
            result.add(metadata);
         }
      }
      return result;
   }

   /**
    * Parmi les métadonnées fournies par le client, renvoie celles qui correspondent à des demandes de modification
    * 
    * @param listeMetaClient
    *           Les métadonnées fournies par le client
    * @return
    */
   private List<UntypedMetadata> getModifiedMetadatas(final List<UntypedMetadata> listeMetaClient) {
      final List<UntypedMetadata> result = new ArrayList<>();
      for (final UntypedMetadata metadata : Utils.nullSafeIterable(listeMetaClient)) {
         if (metadata.getValue() != null && !metadata.getValue().isEmpty()) {
            result.add(metadata);
         }
      }
      return result;
   }

   /**
    * Gestion des métadonnées enrichie par la GED dans le transfert
    * 
    * @param document
    *           Document à transférer
    * @param idTraitementMasse
    *           Identifiant du traitement de masse
    * @throws UnknownCodeRndEx
    * @{@link UnknownCodeRndEx}
    * @throws ReferentialException
    * @{@link ReferentialException}
    * @throws TransfertException
    * @{@link TransfertException}
    */
   private void completeMetadatas(final StorageDocument document, final UUID idTraitementMasse)
         throws UnknownCodeRndEx, ReferentialException, TransfertException {

      final String trcPrefix = "completeMetadatas";
      LOG.debug("{} - début", trcPrefix);

      final List<StorageMetadata> metadatas = document.getMetadatas();

      final String codeRnd = (String) getValueMetaByCode(StorageTechnicalMetadatas.TYPE.getShortCode(),
            metadatas);

      try {
         if (StringUtils.isNotBlank(codeRnd)) {
            enrichissementRND(document, codeRnd);
         }

         // -- Ajout métadonnée "DateArchivageGNT" et "TracePreArchivage"
         addMetaDocumentForTransfert(document);

         // Ajout de la métadonnée idTransfertMasseInterne
         final String idTransfertMasseInterne = StorageMetadataUtils.valueMetadataFinder(metadatas,
               StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode());
         if (idTransfertMasseInterne == null || StringUtils.isEmpty(idTransfertMasseInterne)) {
            metadatas.add(new StorageMetadata(StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode(),
                  idTraitementMasse.toString()));
         } else if (StringUtils.isNotEmpty(idTransfertMasseInterne)
               // Gestion du cas particulier de suppression de doc avec un iti
               && !idTransfertMasseInterne.equals(idTraitementMasse.toString())) {
            final String erreur = "La métadonnée idTransfertMasseInterne ne peut être alimenté alors que le document n'a pas été transféré";
            throw new TransfertException(erreur);
         }

      }
      catch (final CodeRndInexistantException e) {
         throw new UnknownCodeRndEx(e.getMessage(), e);
      }
      catch (final ReferentialException e) {
         throw new ReferentialException(e.getMessage(), e);
      }

      LOG.debug("{} - fin", trcPrefix);
   }

   /**
    * Ajoute les métadonnées dans les cas d'une mise à la corbeille :
    * IdTransfertMasseInterne
    * DateMiseEnCorbeille
    * 
    * @param document
    * @param idTraitementMasse
    */
   private void completeMetadatasForDeletion(final StorageDocument document, final UUID idTraitementMasse) {

      final String trcPrefix = "completeMetadatasForDeletion";
      LOG.debug("{} - début", trcPrefix);

      final List<StorageMetadata> metadatas = document.getMetadatas();

      // Ajout de la métadonnée IdTransfertMasseInterne
      final String idTransfertMasseInterne = StorageMetadataUtils.valueMetadataFinder(metadatas,
            StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode());
      if (idTransfertMasseInterne == null || StringUtils.isEmpty(idTransfertMasseInterne)) {
         metadatas.add(new StorageMetadata(StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode(),
               idTraitementMasse.toString()));
      }

      // Ajout de la métadonnée DateMiseEnCorbeille
      final Object dateCorbeille = StorageMetadataUtils.valueObjectMetadataFinder(metadatas,
            StorageTechnicalMetadatas.DATE_MISE_EN_CORBEILLE.getShortCode());
      if (dateCorbeille == null) {
         metadatas.add(new StorageMetadata(StorageTechnicalMetadatas.DATE_MISE_EN_CORBEILLE.getShortCode(),
               new Date()));
      }

      LOG.debug("{} - fin", trcPrefix);
   }

   /**
    * Enrichissement des métadonnées du document à partir du code RND
    * 
    * @param document
    *           Le document à enrichir
    * @param codeRnd
    *           Le code RND
    */
   private void enrichissementRND(final StorageDocument document, final String codeRnd)
         throws CodeRndInexistantException, ReferentialException {

      final List<StorageMetadata> metadatas = document.getMetadatas();
      final String codeActivite = rndService.getCodeActivite(codeRnd);
      final String codeFonction = rndService.getCodeFonction(codeRnd);

      final MetadataReference codeActiviteRef = metadataReferenceDAO.getByLongCode(SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode());
      final MetadataReference codeFonctionRef = metadataReferenceDAO.getByLongCode(SAEArchivalMetadatas.CODE_FONCTION.getLongCode());
      final MetadataReference dateFinConservationRef = metadataReferenceDAO.getByLongCode(SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode());

      // Récupération de la date de début de conservation
      final Date dateDebutConservation = getDateDebutConservation(document);
      if (dateDebutConservation != null) {
         final int duration = rndService.getDureeConservation(codeRnd);
         final Date dateFin = DateUtils.addDays(dateDebutConservation, duration);
         // MAJ de la date de fin de conservation
         modifyMetadata(metadatas, new StorageMetadata(dateFinConservationRef.getShortCode(), dateFin));
      }

      // Mise à jour code activité
      if (StringUtils.isNotEmpty(codeActivite)) {
         modifyMetadata(metadatas, new StorageMetadata(codeActiviteRef.getShortCode(), codeActivite));
      } else {
         deleteMetadata(metadatas, codeActiviteRef.getShortCode());
      }

      // Mise à jour du codeFonction
      if (StringUtils.isNotEmpty(codeFonction)) {
         modifyMetadata(metadatas, new StorageMetadata(codeFonctionRef.getShortCode(), codeFonction));
      } else {
         deleteMetadata(metadatas, codeFonctionRef.getShortCode());
      }
   }

   /**
    * Renvoie la date de début de conservation du document, inscrite dans ses métadonnées
    * 
    * @param document
    * @return
    */
   private Date getDateDebutConservation(final StorageDocument document) {
      return (Date) getValueMetaByCode(SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode(), document.getMetadatas());
   }

   private String getTracePreArchivageAsJson(final StorageDocument document) throws TransfertException {
      final Map<String, String> mapTraces = new HashMap<>();

      // -- Get json de la liste des events du cycle de vie DFCE
      final String eventsDfce = getCycleVieDfceEventsAsJson(document.getUuid());
      mapTraces.put("traceDfce", eventsDfce);

      // -- Get json de la liste des events du journal des events SAE
      final String eventsSae = getJournalSaeEventsAsJson(document.getUuid());
      mapTraces.put("traceSae", eventsSae);

      // -- Get json de la traçabilité pré-archivage si présente
      for (int i = 0; i < document.getMetadatas().size(); i++) {
         final StorageMetadata meta = document.getMetadatas().get(i);
         final String codeCourt = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE.getShortCode();
         if (meta.getShortCode() == codeCourt) {
            mapTraces.put("tracePreArchivage", meta.getValue().toString());
            break;
         }
      }

      try {
         return new ObjectMapper().writeValueAsString(mapTraces);
      }
      catch (final IOException e) {
         throw new TransfertException(e);
      }
   }

   /**
    * Méthode conversion en json de la liste des événements du cycle de vie
    * DFCE par identifiant de document. La liste est renvoyée au format Json.
    *
    * @param idArchive
    * @return
    * @throws TransfertException
    */
   private String getCycleVieDfceEventsAsJson(final UUID idArchive) throws TransfertException {
      final List<DfceTraceDoc> evtCycleVie = cycleVieService.lectureParDocument(idArchive);

      final ObjectMapper mapper = new ObjectMapper();
      try {
         return mapper.writeValueAsString(evtCycleVie);
      }
      catch (final IOException e) {
         throw new TransfertException(e);
      }

   }

   /**
    * Récupération de la liste des événements du journal SAE par identifiant de
    * document. La liste est renvoyée au format Json.
    *
    * @param idArchive
    * @return
    * @throws TransfertException
    */
   private String getJournalSaeEventsAsJson(final UUID idArchive) throws TransfertException {
      final List<TraceJournalEvtIndexDoc> evtSae = journalEvtService.getTraceJournalEvtByIdDoc(idArchive);
      final ObjectMapper mapper = new ObjectMapper();
      try {
         return mapper.writeValueAsString(evtSae);
      }
      catch (final IOException e) {
         throw new TransfertException(e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument controleDocumentTransfertMasse(final UUID idArchive,
         final List<UntypedMetadata> listeMetaClient, final boolean isReprise,
         final UUID idTraitementMasse,
         final boolean isSuppression)
               throws TransfertException, ArchiveAlreadyTransferedException, TraitementRepriseAlreadyDoneException {
      final String erreur = "Une erreur interne à l'application est survenue lors du controle du transfert. Transfert impossible :";
      StorageDocument documentGNT = new StorageDocument();

      try {

         documentGNT = recupererDocWithAlmostAllMetaAndGel(idArchive);

         // Vérifie que le document n'est pas gelé
         if (documentGNT != null && isFrozenDocument(documentGNT.getMetadatas())) {
            final String message = "Le document " + idArchive + " est gelé et ne peut pas être traité.";
            throw new TransfertMasseRuntimeException(message);
         }

         // Récupération du document en GNS s'il existe
         final StorageDocument documentGNS = getDocInGNS(idArchive);
         // Vérifie que le document se trouve bien en GNT, et renvoie des exceptions sinon
         handleErrorsIfDocNotInGNT(documentGNT, documentGNS, idArchive, isReprise, idTraitementMasse);
         Assert.notNull(documentGNT);

         if (documentGNS != null) {
            // Le document existe aussi en GNS, ce qui n'est pas vraiment normal. Selon, le cas, on gère ou on s'arrête
            handleUnexpectedDocInGNS(idArchive, isReprise, idTraitementMasse, documentGNS);
         }

         if (!isSuppression) {
            // Contrôle des métadonnées que le client veut modifier
            controleModification.checkSaeMetadataForTransfertMasse(getModifiedMetadatas(listeMetaClient));
            // Mise à jour de la liste des métadonnées en tenant compte des demandes du client (modification ou suppression de méta)
            documentGNT = updateMetaDocumentForTransfertMasse(documentGNT, listeMetaClient);
            // Enrichissement des métadonnées
            completeMetadatas(documentGNT, idTraitementMasse);
         }
         else {
            // Dans le cas d'une mise en corbeille : il y a aussi des métas à modifier
            completeMetadatasForDeletion(documentGNT, idTraitementMasse);
         }

         // Contrôle des droits de transfert
         controleDroitTransfertMasse(documentGNT.getMetadatas());

         if (!isSuppression) {
            // On ne garde que les métadonnées transférables
            filterTransfertableMetadatas(documentGNT.getMetadatas());
         }

      }
      catch (final SearchingServiceEx | ReferentialException | RetrievalServiceEx | InvalidSAETypeException | MappingFromReferentialException |
            ReferentialRndException | InvalidValueTypeAndFormatMetadataEx | UnknownMetadataEx | DuplicatedMetadataEx | NotSpecifiableMetadataEx |
            RequiredArchivableMetadataEx | UnknownHashCodeEx | NotModifiableMetadataEx | MetadataValueNotInDictionaryEx | UnknownCodeRndEx |
            ArchiveInexistanteEx ex) {
         throw new TransfertException(erreur + Objects.toString(ex.getMessage(), ""), ex);
      }

      return documentGNT;
   }

   /**
    * Filtre la liste des métadonnées pour en garder que celles qui sont transférables et non vides
    * 
    * @param metadatas
    *           La liste à filtrer
    * @throws ReferentialException
    */
   private void filterTransfertableMetadatas(final List<StorageMetadata> metadatas) throws ReferentialException {
      final Map<String, MetadataReference> transferables = metadataReferenceDAO.getTransferableMetadataReferenceByShortCode();
      metadatas.removeIf(new Predicate<StorageMetadata>() {
         /**
          * {@inheritDoc}
          */
         @Override
         public boolean test(final StorageMetadata meta) {
            // Suppression si méta vide
            if (meta.getValue() == null || meta.getValue().equals("")) {
               return true;
            }
            // Suppression si méta non transférable
            return !transferables.containsKey(meta.getShortCode());
         }
      });
   }

   /**
    * Dans le cas du traitement de transfert de masse : gère le cas où on détecte que le document à transférer
    * se trouve déjà en GNS
    * 
    * @param idArchive
    *           id du document concerné
    * @param isReprise
    *           vrai s'il s'agit d'un traitement de reprise
    * @param idTraitementMasse
    *           id du traitement de masse
    * @param documentGNS
    *           document trouvé en GNS
    * @throws ArchiveAlreadyTransferedException
    */
   private void handleUnexpectedDocInGNS(final UUID idArchive, final boolean isReprise, final UUID idTraitementMasse, final StorageDocument documentGNS)
         throws ArchiveAlreadyTransferedException {
      final String uuid = idArchive.toString();
      if (isReprise) {
         // -- Pour la reprise, on supprime le document de la GNS et
         // on reprend le transfert uniquement si le document contient
         // un idTransfertMasseInterne possédant la même valeur
         // d'identifiant que le traitement de masse en cours d'exécution.
         if (ckeckIdTraitement(documentGNS.getMetadatas(), idTraitementMasse)) {
            try {
               storageTransfertService.deleteStorageDocument(idArchive);
               LOG.info("{} - Reprise - Suppression du document {} de la GNS.", "transfertDoc", idArchive);
            }
            catch (final DeletionServiceEx ex) {
               final String message = format("Reprise transfert de masse - La suppression du document {} de la GNS a échoué.", uuid);
               throw new TransfertMasseRuntimeException(message);
            }
         } else {
            final String message = format("Reprise transfert de masse - le document {} a été transféré dans la GNS par un autre traitement de masse que le traitement en cours d'exécution",
                  uuid);
            throw new ArchiveAlreadyTransferedException(message);
         }
      } else {
         // -- Le document existe sur la GNS et sur la GNT, et on n'est pas en mode reprise. Ce n'est pas normal.
         final String message = format("Le document {} est anormalement présent en GNT et en GNS. Une intervention est nécessaire.", uuid);
         throw new ArchiveAlreadyTransferedException(message);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void transfertDoc(final UUID idArchive) throws TransfertException, ArchiveAlreadyTransferedException,
   ArchiveInexistanteEx, InsertionIdGedExistantEx {

      // -- On trace le début du transfert
      final String trcPrefix = "transfertDoc";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de transfert du document {}", new Object[] {trcPrefix, idArchive});

      final String docUUID = idArchive.toString();
      final ZookeeperMutex mutex = new ZookeeperMutex(zookeeperCfactory.getClient(), "/Transfert/" + docUUID);
      try {
         // Lock du document
         ZookeeperUtils.acquire(mutex, docUUID);
         final StorageDocument document = recupererDocWithAlmostAllMetaAndGel(idArchive);
         if (document != null) {
            verifieDroitsEtGel(document);
         }
         doTransfertDoc(idArchive, document);
         if (!ZookeeperUtils.isLock(mutex)) {
            // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne devrait jamais arriver.
            LOG.warn("Erreur lors de la tentative d'acquisition du lock pour le transfert du doc {}. Problème de connexion zookeeper ?", idArchive);
         }
      }
      catch (final SearchingServiceEx | ReferentialException | InvalidSAETypeException | MappingFromReferentialException | RetrievalServiceEx |
            InsertionIdGedExistantEx | InsertionServiceEx ex) {
         throw new TransfertException(GENERIC_ERROR_STRING, ex);
      }
      finally {
         mutex.release();
      }
   }

   /**
    * Dans le cadre d'un transfert unitaire : procède au transfert du document, soit copie en GNS et suppression en GNT.
    * Cette méthode est protégée en amont par un mutex.
    * 
    * @param idArchive
    * @param document
    * @param errorString
    * @throws InsertionServiceEx
    */
   private void doTransfertDoc(final UUID idArchive, final StorageDocument document) throws ReferentialException, SearchingServiceEx,
   ArchiveInexistanteEx, ArchiveAlreadyTransferedException, TransfertException, InsertionIdGedExistantEx, InsertionServiceEx {
      final String trcPrefix = "doTransfertDoc";

      LOG.debug("{} - recherche du document en GNS", trcPrefix);
      // On récupère le document en GNS avec uniquement les méta transférables
      final List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);
      final StorageDocument documentGNS = storageTransfertService.searchStorageDocumentByUUIDCriteria(uuidCriteria);

      final String hashDocGNT = getHashDocument(document);

      // -- Le document n'existe pas en GNT
      if (document == null) {

         final String uuid = idArchive.toString();

         if (documentGNS == null) {
            // -- Le document n'existe pas non plus sur la GNS
            final String message = format("Le document {} n'existe pas. Transfert impossible.", uuid);
            throw new ArchiveInexistanteEx(message);
         } else {
            // -- Le document existe sur la GNS
            final String message = format("Le document {} a déjà été transféré.", uuid);
            throw new ArchiveAlreadyTransferedException(message);
         }
      } else {
         // -- Le document existe en GNT
         if (documentGNS != null) {
            // Le doc existe en GNS.
            // On regarde s'il s'agit du même document qu'en GNT. Si oui, on le supprime de la GNS
            final String hashDocumentGNS = getHashDocument(documentGNS);
            if (hashDocumentGNS.equals(hashDocGNT)) {
               // Il s'agit bien du même document
               try {
                  storageTransfertService.deleteStorageDocument(idArchive);
                  LOG.info("{} - Transfert - Suppression du document {} de la GNS.", trcPrefix, idArchive);
               }
               catch (final DeletionServiceEx ex) {
                  final String message = format("Transfert - La suppression du document {} de la GNS a échoué.", idArchive);
                  throw new TransfertException(message);
               }
            } else {
               // -- L'idGed du document à transférer existe déjà en GNS
               final String message = format("L'identifiant ged spécifié {} existe déjà en GNS et ne peut être utilisé. Transfert impossible.", idArchive);
               throw new InsertionIdGedExistantEx(message);
            }
         }

         // -- Modification des métadonnées du document pour le transfert
         addMetaDocumentForTransfert(document);
         filterTransfertableMetadatas(document.getMetadatas());
         // -- Archivage du document en GNS
         sendToGNS(document);
         // -- Suppression du document transféré de la GNT
         deleteFromGNT(idArchive);

         LOG.debug("{} - Fin de transfert du document {}", trcPrefix, idArchive);
      }
   }

   private void deleteFromGNT(final UUID idArchive) throws SearchingServiceEx, TransfertException {
      try {
         storageDocumentService.deleteStorageDocumentTraceTransfert(idArchive);
      }
      catch (final DeletionServiceEx erreurSupprGNT) {
         final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, getMetadatasForExistenceCheck());
         final StorageDocument documentGNT = storageDocumentService.searchMetaDatasByUUIDCriteria(uuidCriteria);
         if (documentGNT != null) {
            // -- Le document existe toujours dans la GNT
            try {
               storageTransfertService.deleteStorageDocument(idArchive);
            }
            catch (final DeletionServiceEx erreurSupprGNS) {
               throw new TransfertException(erreurSupprGNS);
            }
         }
         throw new TransfertException(GENERIC_ERROR_STRING, erreurSupprGNT);
      }
   }

   /**
    * Envoie le document en GNS, ainsi que le document attaché et les notes
    * 
    * @param document
    */
   private void sendToGNS(final StorageDocument document)
         throws InsertionServiceEx, InsertionIdGedExistantEx, TransfertException {

      final StorageDocument documentGNS = storageTransfertService.insertBinaryStorageDocument(document);

      // -- Récupération des notes associées au document
      // transféré
      final List<StorageDocumentNote> listeNotes = storageDocumentService.getDocumentsNotes(document.getUuid());
      // -- Ajout des notes sur le document archivés en GNS
      for (final StorageDocumentNote note : listeNotes) {
         try {
            storageTransfertService.addDocumentNote(documentGNS.getUuid(),
                  note.getContenu(),
                  note.getAuteur(),
                  note.getDateCreation(),
                  note.getUuid());
         }
         catch (final DocumentNoteServiceEx e) {
            // Les notes n'ont pas pu être transférées, on
            // annule le transfert (suppression du document en GNS)
            try {
               storageTransfertService.deleteStorageDocument(document.getUuid());
            }
            catch (final DeletionServiceEx erreurSupprGNS) {
               throw new TransfertException(erreurSupprGNS);
            }
            throw new TransfertException(GENERIC_ERROR_STRING, e);
         }
      }

      // -- Récupération du document attaché éventuel
      StorageDocumentAttachment docAttache;
      try {
         docAttache = storageDocumentService.getDocumentAttachment(document.getUuid());
         // -- Ajout du document attaché sur le document archivé en GNS
         if (docAttache != null) {
            storageTransfertService.addDocumentAttachment(documentGNS.getUuid(),
                  docAttache.getName(),
                  docAttache.getExtension(),
                  docAttache.getContenu());
         }
      }
      catch (final StorageDocAttachmentServiceEx e) {
         // Le document attaché n'a pas pu être transféré, on annule
         // le transfert (suppression du document en GNS)
         try {
            storageTransfertService.deleteStorageDocument(document.getUuid());
         }
         catch (final DeletionServiceEx erreurSupprGNS) {
            throw new TransfertException(erreurSupprGNS);
         }
         throw new TransfertException(GENERIC_ERROR_STRING, e);
      }
   }

   /**
    * Vérifie que le document peut bien être transféré, et envoie une exception sinon
    * 
    * @param idArchive
    */
   private void verifieDroitsEtGel(final StorageDocument document)
         throws RetrievalServiceEx, InvalidSAETypeException, MappingFromReferentialException, TransfertException {

      final String trcPrefix = "verifieDroitsEtGel";

      final List<StorageMetadata> listeStorageMeta = document.getMetadatas();

      // -- On vérifie si le document n'est pas gelé
      if (isFrozenDocument(listeStorageMeta)) {
         throw new TransfertException(String.format("Le document %s est gelé et ne peut pas être transféré", document.getUuid().toString()));
      }

      // Vérification des droits
      LOG.debug("{} - Récupération des droits", trcPrefix);
      final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
      final List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert");
      LOG.debug("{} - Vérification des droits", trcPrefix);
      final List<UntypedMetadata> listeUMeta = mappingService.storageMetadataToUntypedMetadata(listeStorageMeta);
      final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

      if (!isPermitted) {
         throw new AccessDeniedException("Le document est refusé au transfert car les droits sont insuffisants");
      }

   }

   /**
    * Retourne le hash du storageDocument passé en paramètre
    * 
    * @param storageDocument
    *           document
    * @return
    */
   private String getHashDocument(final StorageDocument document) {
      String hashDocument = "";
      if (document != null) {
         for (final StorageMetadata metadata : document.getMetadatas()) {
            if (metadata.getShortCode().equals(StorageTechnicalMetadatas.HASH.getShortCode())) {
               hashDocument = metadata.getValue().toString();
               break;
            }
         }
      }
      return hashDocument;
   }

   /**
    * Retourne la valeur de la métadonnée à partir de son code passé en
    * paramètre
    * 
    * @param codeMetaData
    * @param metaDataList
    * @return la valeur de la métadonnée à partir de son shortCode
    */
   private Object getValueMetaByCode(final String codeMetaData, final List<StorageMetadata> metaDataList) {
      Object valueMetaData = null;
      if (metaDataList != null) {
         for (final StorageMetadata metadata : metaDataList) {
            if (codeMetaData.equals(metadata.getShortCode())) {
               if (StringUtils.isNotBlank(metadata.getShortCode())) {
                  valueMetaData = metadata.getValue();
               } else {
                  valueMetaData = null;
               }
               break;
            }
         }
      }
      return valueMetaData;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageMetadata> getListeStorageMetadatasWithGel(final UUID idArchive)
         throws ReferentialException, RetrievalServiceEx {
      // On récupère la liste de toutes les méta du référentiel sauf la
      // Note, le Gel et la durée de conservation inutile pour les droits
      // et générant des accès DFCE inutiles
      final List<StorageMetadata> allMeta = new ArrayList<>();
      final Map<String, MetadataReference> listeAllMeta = metadataReferenceDAO.getAllMetadataReferencesPourVerifDroits();

      for (final String mapKey : listeAllMeta.keySet()) {
         allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey).getShortCode()));
      }

      // Ajout de la meta GEL puisque non récupéré avant
      allMeta.add(new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode()));

      // Création des critères
      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

      // Recherche du document par critère
      return getStorageDocumentService().retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
   }

}