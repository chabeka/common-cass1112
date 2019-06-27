package fr.urssaf.image.sae.services.transfert.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
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
import fr.urssaf.image.sae.trace.service.impl.JournalEvtServiceImpl;
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
   * Objet de manipulation des métadonnées du reférentiel des meta
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
  private JournalEvtServiceImpl journalEvtService;

  /**
   * Service pour le mapping de document
   */
  @Autowired
  private MappingDocumentService mappingService;

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

  /**
   * @param idArchive
   * @throws ReferentialException
   * @throws RetrievalServiceEx
   * @throws InvalidSAETypeException
   * @throws MappingFromReferentialException
   *           Permet de vérifier les droits avant le transfert
   */
  @Override
  public final void controleDroitTransfert(final UUID idArchive)
      throws ReferentialException, RetrievalServiceEx, InvalidSAETypeException, MappingFromReferentialException {

    // On récupère les métadonnées du document à partir de l'UUID, avec
    // toutes les
    // métadonnées du référentiel sauf la note qui n'est pas utilise pour
    // les droits

    final List<StorageMetadata> allMeta = new ArrayList<>();
    final Map<String, MetadataReference> listeAllMeta = metadataReferenceDAO
                                                                            .getAllMetadataReferencesPourVerifDroits();
    for (final String mapKey : listeAllMeta.keySet()) {
      allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey).getShortCode()));
    }
    final UUIDCriteria uuidCriteriaDroit = new UUIDCriteria(idArchive, allMeta);

    final List<StorageMetadata> listeStorageMeta = storageDocumentService
                                                                         .retrieveStorageDocumentMetaDatasByUUID(uuidCriteriaDroit);

    if (listeStorageMeta.size() != 0) {
      final List<UntypedMetadata> listeUMeta = mappingService.storageMetadataToUntypedMetadata(listeStorageMeta);

      // Vérification des droits
      LOG.debug("{} - Récupération des droits", "transfertDoc");
      final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext()
                                                                                   .getAuthentication();
      final List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert_masse");
      LOG.debug("{} - Vérification des droits", "transfertDoc");
      final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

      if (!isPermitted) {
        throw new AccessDeniedException("Le document est refusé au transfert car les droits sont insuffisants");
      }
    }
  }

  /**
   * @param allMeta
   * @throws ReferentialException
   * @throws RetrievalServiceEx
   * @throws InvalidSAETypeException
   * @throws MappingFromReferentialException
   *           Permet de vérifier les droits avant le transfert de masse
   */
  @Override
  public final void controleDroitTransfertMasse(final List<StorageMetadata> allMeta)
      throws ReferentialException, RetrievalServiceEx, InvalidSAETypeException, MappingFromReferentialException {

    if (allMeta.size() != 0) {
      final List<UntypedMetadata> listeUMeta = mappingService.storageMetadataToUntypedMetadata(allMeta);

      // Vérification des droits
      LOG.debug("{} - Récupération des droits", "transfertDoc");
      final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext()
                                                                                   .getAuthentication();
      final List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert_masse");
      LOG.debug("{} - Vérification des droits", "transfertDoc");
      final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

      if (!isPermitted) {
        throw new AccessDeniedException("Le document est refusé au transfert car les droits sont insuffisants");
      }
    }
  }

  @Override
  public final StorageDocument transfertControlePlateforme(StorageDocument document, final UUID idArchive,
                                                           final boolean isReprise, final UUID idTraitementMasse)
      throws ArchiveAlreadyTransferedException, SearchingServiceEx,
      ReferentialException, ArchiveInexistanteEx, TraitementRepriseAlreadyDoneException {
    String message = null;
    // On récupère le document avec uniquement les méta transférables
    final List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

    // -- Le document n'existe pas sur la GNT
    if (document == null) {
      // -- On recherche le document sur la GNS
      document = storageTransfertService.searchStorageDocumentByUUIDCriteria(uuidCriteria);

      final String uuid = idArchive.toString();

      // -- Le document n'existe pas sur la GNS (et sur la GNT)
      if (document == null) {
        message = "Le document {0} n'existe pas. Transfert impossible.";
        throw new ArchiveInexistanteEx(StringUtils.replace(message, "{0}", uuid));
      } else {
        if (isReprise) {
          message = "Le document {0} a déjà été transféré par le traitement de masse en cours {1}";
          if (idTraitementMasse == null) {
            message = "L'identifiant du traitement de masse est inexistant pour le contrôle du document {0}";
            throw new TransfertMasseRuntimeException(StringUtils.replace(message, "{0}", uuid));
          }
          if (ckeckIdTraitementExiste(document.getMetadatas(), idTraitementMasse)) {
            // -- Le document a déjà été transféré dans la GNS
            final String messageFormat = StringUtils.replaceEach(message,
                                                                 new String[] {"{0}", "{1}"},
                                                                 new String[] {uuid, idTraitementMasse.toString()});
            throw new TraitementRepriseAlreadyDoneException(messageFormat);
          }

        }

        // -- Le document existe sur la GNS
        message = "Le document {0} a déjà été transféré.";
        throw new ArchiveAlreadyTransferedException(StringUtils.replace(message, "{0}", uuid));
      }
    } else {
      // -- Le document existe en GNT
      // -- On recherche le document sur la GNS
      final StorageDocument documentGNS = storageTransfertService
                                                                 .searchStorageDocumentByUUIDCriteria(uuidCriteria);
      return documentGNS;
    }
  }

  /**
   * Vérifie que l'identifiant du traitement existe dans la liste des
   * metadonnées
   * 
   * @param metadatas
   *          Liste des métadonnées
   * @param idTraitementMasse
   *          Identifiant de traitement.
   * @return True si l'identifiant du traitement existe dans la liste des
   *         metadonnées, false sinon.
   */
  private boolean ckeckIdTraitementExiste(final List<StorageMetadata> metadatas, final UUID idTraitementMasse) {
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
   * @param document
   * @param idArchive
   * @throws TransfertException
   *           Fonction de transfert du document avec notes et doc attachés
   */
  @Override
  public final void transfertDocument(StorageDocument document) throws TransfertException {
    final String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

    try {

      document = storageTransfertService.insertBinaryStorageDocument(document);

      // -- Récupération des notes associées au document transféré
      final List<StorageDocumentNote> listeNotes = storageDocumentService.getDocumentsNotes(document.getUuid());
      // -- Ajout des notes sur le document archivés en GNS
      for (final StorageDocumentNote note : listeNotes) {
        try {
          storageTransfertService.addDocumentNote(document.getUuid(),
                                                  note.getContenu(),
                                                  note.getAuteur(),
                                                  note.getDateCreation(),
                                                  note.getUuid());
        }
        catch (final DocumentNoteServiceEx e) {
          // Les notes n'ont pas pu être transférées, on annule le
          // transfert (suppression du document en GNS)
          try {
            storageTransfertService.deleteStorageDocument(document.getUuid());
          }
          catch (final DeletionServiceEx erreurSupprGNS) {
            throw new TransfertException(erreurSupprGNS);
          }
          throw new TransfertException(erreur, e);
        }
      }

      // -- Récupération du document attaché éventuel
      StorageDocumentAttachment docAttache;
      try {
        docAttache = storageDocumentService.getDocumentAttachment(document.getUuid());
        // -- Ajout du document attaché sur le document archivés en
        // GNS
        if (docAttache != null) {
          storageTransfertService.addDocumentAttachment(document.getUuid(),
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
        throw new TransfertException(erreur, e);
      }

    }
    catch (final InsertionServiceEx ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final InsertionIdGedExistantEx e1) {
      throw new TransfertException(erreur, e1);
    }
  }

  /**
   * @param idArchive
   * @throws SearchingServiceEx
   * @throws ReferentialException
   * @throws TransfertException
   *           Fonction permet suppression doc sur GNT après le transfert
   */
  @Override
  public final void deleteDocApresTransfert(final UUID idArchive)
      throws SearchingServiceEx, ReferentialException, TransfertException {
    // -- Suppression du document transféré de la GNT
    final String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

    final List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

    try {
      storageDocumentService.deleteStorageDocumentTraceTransfert(idArchive);
    }
    catch (final DeletionServiceEx erreurSupprGNT) {
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
      throw new TransfertException(erreur, erreurSupprGNT);
    }

  }

  /**
   * @param idArchive
   * @return
   * @throws ReferentialException
   * @throws SearchingServiceEx
   *           Permet de récupérer le document avec les métadonnées
   *           transférables
   */
  @Override
  public final StorageDocument recupererDocMetaTransferable(final UUID idArchive)
      throws ReferentialException, SearchingServiceEx {
    // On récupère le document avec uniquement les méta transférables
    final List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

    return storageDocumentService.searchStorageDocumentByUUIDCriteria(uuidCriteria);
  }

  @Override
  public final void transfertDocMasse(final StorageDocument document)
      throws TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx, ReferentialException,
      RetrievalServiceEx, InvalidSAETypeException, MappingFromReferentialException {

    // -- On trace le début du transfert
    final String trcPrefix = "transfertDoc";
    LOG.debug("{} - début", trcPrefix);
    LOG.debug("{} - Début de transfert du document {}", new Object[] {trcPrefix, document.getUuid().toString()});

    final String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible : ";

    final String uuid = document.getUuid().toString();
    final ZookeeperMutex mutex = new ZookeeperMutex(zookeeperCfactory.getClient(), "/Transfert/" + uuid);

    try {

      // Lock du document
      ZookeeperUtils.acquire(mutex, uuid);

      transfertDocument(document);

      deleteDocApresTransfert(document.getUuid());

      // A la fin on vérifie qu'on à toujours le lock
      if (!ZookeeperUtils.isLock(mutex)) {
        // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne
        // devrait jamais arriver.
        final String message = "Erreur lors de la tentative d'acquisition du lock pour le transfert de masse"
            + uuid + ". Problème de connexion zookeeper ?";
        LOG.warn(message);
      }
      LOG.debug("{} - Fin de transfert du document {}",
                new Object[] {trcPrefix, document.getUuid().toString()});

    }
    catch (final SearchingServiceEx ex) {
      LOG.error(erreur + ex.getCause() + " - " + ex.getMessage());
      throw new TransfertException(ex.getMessage(), ex);
    }
    catch (final ReferentialException ex) {
      LOG.error(erreur + ex.getCause() + " - " + ex.getMessage());
      throw new TransfertException(ex.getMessage(), ex);
    }
    finally {
      mutex.release();
    }
  }

  /**
   * Récupération de la liste des métadonnées transférables, depuis le
   * référentiel des métadonnées.
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

    // -- La métadonnée DateArchivage ne fait pas partie des métadonnées
    // transférable, mais nous en avons besoin pour
    // calculer la date d'archivage en GNT
    // on l'ajoute donc a la liste
    metas.add(new StorageMetadata(StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode()));

    return metas;
  }

  /**
   * Mise à jour des métadonnées du documents à transférer :
   * <li>DateArchivageGNT</li>
   * <li>Supp. meta non transférables</li>
   * <li>TracePreArchivage</li>
   *
   * @param document
   *          Document à transférer
   * @throws ReferentialException
   *           En cas d'erreur de récupérarion de la liste des méta
   *           transférables
   * @throws TransfertException
   *           En cas d'erreur de création des traces json
   */
  private void updateMetaDocumentForTransfert(final StorageDocument document)
      throws ReferentialException, TransfertException {

    // -- Ajout métadonnée "DateArchivageGNT"
    final Object dateArchivage = StorageMetadataUtils.valueObjectMetadataFinder(document.getMetadatas(),
                                                                                StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode());
    final StorageMetadata dateArchivageGNT = new StorageMetadata(
                                                                 StorageTechnicalMetadatas.DATE_ARCHIVE_GNT.getShortCode(),
                                                                 dateArchivage);
    document.getMetadatas().add(dateArchivageGNT);

    // -- Suppression des métadonnées vides (impératif api dfce)
    // Supprime aussi la métadonnée DateArchivage qui est non transférable
    final List<StorageMetadata> metadata = document.getMetadatas();
    for (int i = 0; i < metadata.size(); i++) {
      if (metadata.get(i).getValue() == null || metadata.get(i).getValue().equals("")
          || metadata.get(i).getShortCode().equals(StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode())) {
        metadata.remove(i);
        i--;
      }
    }

    // -- Ajout traces (preachivage) au document
    final String traces = getTracePreArchivageAsJson(document);
    final String shortCode = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE.getShortCode();
    document.getMetadatas().add(new StorageMetadata(shortCode, traces));
  }

  /**
   * {@inheritDoc}
   * 
   * @throws UnknownCodeRndEx
   */
  @Override
  public final StorageDocument updateMetaDocumentForTransfertMasse(final StorageDocument document,
                                                                   final List<StorageMetadata> listeMeta, final UUID idTraitementMasse)
      throws ReferentialException, TransfertException, UnknownCodeRndEx {

    // -- Ajout métadonnée "DateArchivageGNT"
    final Object dateArchivage = StorageMetadataUtils.valueObjectMetadataFinder(document.getMetadatas(),
                                                                                StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode());
    final StorageMetadata dateArchivageGNT = new StorageMetadata(
                                                                 StorageTechnicalMetadatas.DATE_ARCHIVE_GNT.getShortCode(),
                                                                 dateArchivage);

    // Récupération de la date de début de conservation
    final StorageMetadata dateDebutConservationMeta = getDateDebutConservation(document.getUuid());

    document.getMetadatas().add(dateArchivageGNT);

    // modification des métadonnées avant transfert
    if (!CollectionUtils.isEmpty(listeMeta)) {
      final List<StorageMetadata> metadataMasse = new ArrayList<>();
      Boolean bool = false;
      for (final StorageMetadata meta : document.getMetadatas()) {
        for (final StorageMetadata meta2 : listeMeta) {
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
      // Gestion des règles concernant le code RND
      metadataMasse.add(dateDebutConservationMeta);
      completeMetadatas(metadataMasse);

      document.setMetadatas(metadataMasse);
    }

    // -- Suppression des métadonnées vides (impératif api dfce). Vide = non modifié ou à supprimer
    // Supprime aussi la métadonnée DateArchivage qui est non transférable
    final List<StorageMetadata> metadata = document.getMetadatas();
    for (int i = 0; i < metadata.size(); i++) {
      if (metadata.get(i).getValue() == null || metadata.get(i).getValue().equals(StringUtils.EMPTY)
          || metadata.get(i).getShortCode().equals(StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode())) {
        metadata.remove(i);
        i--;
      }
    }

    // Ajout de la metadonnées idTransfertMasseInterne
    final String idTransfertMasseInterne = StorageMetadataUtils.valueMetadataFinder(document.getMetadatas(),
                                                                                    StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode());
    if (idTransfertMasseInterne == null || StringUtils.isEmpty(idTransfertMasseInterne)) {
      document.getMetadatas()
              .add(new StorageMetadata(
                                       StorageTechnicalMetadatas.ID_TRANSFERT_MASSE_INTERNE.getShortCode(),
                                       idTraitementMasse.toString()));
    } else if (StringUtils.isNotEmpty(idTransfertMasseInterne)
        // Gestion du cas particulier de suppression de doc avec un iti
        && !idTransfertMasseInterne.equals(idTraitementMasse.toString())) {
      final String erreur = "La métadonnée idTransfertMasseInterne ne peut être alimenté alors que le document n'a pas été transféré";
      throw new TransfertException(erreur);
    }

    // -- Ajout traces (preachivage) au document
    final String traces = getTracePreArchivageAsJson(document);
    final String shortCode = StorageTechnicalMetadatas.TRACABILITE_PRE_ARCHIVAGE.getShortCode();
    document.getMetadatas().add(new StorageMetadata(shortCode, traces));

    return document;
  }

  /**
   * Gestion de la modification du codeRND dans le transfert
   * 
   * @param modifiedMetadatas
   *          la liste des métadonnées à modifier
   * @return la nouvelle liste enrichie des métadonnées
   * @throws UnknownCodeRndEx
   * @throws ReferentialException
   * @throws CodeRndInexistantException
   */
  private List<StorageMetadata> completeMetadatas(final List<StorageMetadata> metadataMasse)
      throws UnknownCodeRndEx, ReferentialException {

    final String trcPrefix = "completeMetadatas";
    LOG.debug("{} - début", trcPrefix);

    final String codeRnd = (String) getValueMetaByCode(StorageTechnicalMetadatas.TYPE.getShortCode(),
                                                       metadataMasse);

    try {
      if (StringUtils.isNotBlank(codeRnd)) {

        final Date date = (Date) getValueMetaByCode(
                                                    StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode(),
                                                    metadataMasse);
        final String codeActivite = rndService.getCodeActivite(codeRnd);
        final String codeFonction = rndService.getCodeFonction(codeRnd);

        final MetadataReference codeActiviteRef = metadataReferenceDAO
                                                                      .getByLongCode(SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode());
        final MetadataReference codeFonctionRef = metadataReferenceDAO
                                                                      .getByLongCode(SAEArchivalMetadatas.CODE_FONCTION.getLongCode());
        final MetadataReference dateFinConservationRef = metadataReferenceDAO
                                                                             .getByLongCode(SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode());

        final int duration = rndService.getDureeConservation(codeRnd);
        if (date != null) {
          final Date dateFin = DateUtils.addDays(date, duration);
          // MAJ de la date de fin de conservation
          final Integer indexDateFinConservation = getIndexMetaByCode(dateFinConservationRef.getShortCode(),
                                                                      metadataMasse);
          if (indexDateFinConservation != null) {
            metadataMasse.get(indexDateFinConservation).setValue(dateFin);
          } else {
            metadataMasse.add(new StorageMetadata(dateFinConservationRef.getShortCode(), dateFin));
          }
        }

        final Integer indexCodeActivite = getIndexMetaByCode(codeActiviteRef.getShortCode(), metadataMasse);

        if (StringUtils.isNotEmpty(codeActivite)) {
          if (indexCodeActivite != null) {
            metadataMasse.get(indexCodeActivite).setValue(codeActivite);
          } else {
            // check si valeur null et alimenté par defaut
            metadataMasse.add(new StorageMetadata(codeActiviteRef.getShortCode(), codeActivite));
          }
        } else {
          if (indexCodeActivite != null) {
            metadataMasse.remove(indexCodeActivite.intValue());
          }
        }

        // Maj du codeFonction
        final Integer indexCodeFonction = getIndexMetaByCode(codeFonctionRef.getShortCode(), metadataMasse);
        if (StringUtils.isNotEmpty(codeFonction)) {
          if (indexCodeFonction != null) {
            metadataMasse.get(indexCodeFonction).setValue(codeFonction);
          } else {
            metadataMasse.add(new StorageMetadata(codeFonctionRef.getShortCode(), codeFonction));
          }
        } else if (indexCodeFonction != null) {
          metadataMasse.remove(indexCodeFonction.intValue());
        }
      }
    }
    catch (final CodeRndInexistantException e) {
      throw new UnknownCodeRndEx(e.getMessage(), e);
    }
    catch (final ReferentialException e) {
      throw new ReferentialException(e.getMessage(), e);
    }

    LOG.debug("{} - fin", trcPrefix);
    return metadataMasse;
  }

  private String getTracePreArchivageAsJson(final StorageDocument document) throws TransfertException {
    final Map<String, String> mapTraces = new HashMap<>();

    // -- Get json de la liste des events du cycle de vie DFCE
    final String eventsDfce = getCycleVieDfceEventsAsJson(document.getUuid());
    mapTraces.put("traceDfce", eventsDfce);

    // -- Get json de la liste des events du jounal des events SAE
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
    catch (final JsonGenerationException e) {
      throw new TransfertException(e);
    }
    catch (final JsonMappingException e) {
      throw new TransfertException(e);
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
    catch (final JsonGenerationException e) {
      throw new TransfertException(e);
    }
    catch (final JsonMappingException e) {
      throw new TransfertException(e);
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
    catch (final JsonGenerationException e) {
      throw new TransfertException(e);
    }
    catch (final JsonMappingException e) {
      throw new TransfertException(e);
    }
    catch (final IOException e) {
      throw new TransfertException(e);
    }
  }

  @Override
  public final StorageDocument controleDocumentTransfertMasse(final UUID idArchive,
                                                              final List<StorageMetadata> storageMetas, final boolean isReprise, final UUID idTraitementMasse)
      throws TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
      TraitementRepriseAlreadyDoneException, UnknownCodeRndEx {
    final String erreur = "Une erreur interne à l'application est survenue lors du controle du transfert. Transfert impossible";
    StorageDocument document = new StorageDocument();

    try {

      document = recupererDocMetaTransferable(idArchive);

      final StorageDocument documentGNS = transfertControlePlateforme(document,
                                                                      idArchive,
                                                                      isReprise,
                                                                      idTraitementMasse);

      if (documentGNS != null) {
        final String uuid = idArchive.toString();
        String message = "Le document {0} est anormalement présent en GNT et en GNS. Une intervention est nécessaire.";
        if (isReprise) {
          // -- Pour la reprise, on supprime le document de la GNS et
          // on
          // reprend le transfert uniquement si le document contient
          // un
          // idTransfertMasseInterne possédant la même valeur
          // d'identifiant
          // que le traitement de masse en cours d'execution.
          if (ckeckIdTraitementExiste(documentGNS.getMetadatas(), idTraitementMasse)) {
            try {
              storageTransfertService.deleteStorageDocument(idArchive);
              LOG.info("{} - Reprise - Suppression du document {} de la GNS.",
                       "transfertDoc",
                       idArchive.toString());
            }
            catch (final DeletionServiceEx ex) {
              message = "Reprise transfert de masse - La suppression du document {0} de la GNS a échoué.";
              throw new TransfertMasseRuntimeException(
                                                       StringUtils.replace(message, "{0}", idArchive.toString()));
            }
          } else {
            message = "Reprise transfert de masse - le document {0} a été transféré dans la GNS par un autre traitement de masse que le traitement en cours d'exécution";
            throw new ArchiveAlreadyTransferedException(StringUtils.replace(message, "{0}", uuid));
          }
        } else {
          // -- Le document existe sur la GNS et sur la GNT
          throw new ArchiveAlreadyTransferedException(StringUtils.replace(message, "{0}", uuid));
        }
      }

      document = updateMetaDocumentForTransfertMasse(document, storageMetas, idTraitementMasse);

      controleDroitTransfertMasse(document.getMetadatas());

    }
    catch (final SearchingServiceEx ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final ReferentialException ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final RetrievalServiceEx ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final InvalidSAETypeException ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final MappingFromReferentialException ex) {
      throw new TransfertException(erreur, ex);
    }

    return document;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws InsertionIdGedExistantEx
   */
  @Override
  public final void transfertDoc(final UUID idArchive) throws TransfertException, ArchiveAlreadyTransferedException,
      ArchiveInexistanteEx, InsertionIdGedExistantEx {

    // -- On trace le début du transfert
    final String trcPrefix = "transfertDoc";
    LOG.debug("{} - début", trcPrefix);
    LOG.debug("{} - Début de transfert du document {}", new Object[] {trcPrefix, idArchive.toString()});

    final String erreur = "Une erreur interne à l'application est survenue lors du transfert. Transfert impossible";

    try {
      // On récupère les métadonnées du document à partir de l'UUID, avec
      // toutes les
      // métadonnées du référentiel sauf la note qui n'est pas utilise
      // pour
      // les droits
      final List<StorageMetadata> allMeta = new ArrayList<>();
      final Map<String, MetadataReference> listeAllMeta = metadataReferenceDAO
                                                                              .getAllMetadataReferencesPourVerifDroits();
      for (final String mapKey : listeAllMeta.keySet()) {
        allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey).getShortCode()));
      }
      final UUIDCriteria uuidCriteriaDroit = new UUIDCriteria(idArchive, allMeta);

      final List<StorageMetadata> listeStorageMeta = storageDocumentService
                                                                           .retrieveStorageDocumentMetaDatasByUUID(uuidCriteriaDroit);

      if (listeStorageMeta.size() != 0) {
        final List<UntypedMetadata> listeUMeta = mappingService
                                                               .storageMetadataToUntypedMetadata(listeStorageMeta);

        // Vérification des droits
        LOG.debug("{} - Récupération des droits", trcPrefix);
        final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext()
                                                                                     .getAuthentication();
        final List<SaePrmd> saePrmds = token.getSaeDroits().get("transfert");
        LOG.debug("{} - Vérification des droits", trcPrefix);
        final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

        if (!isPermitted) {
          throw new AccessDeniedException(
                                          "Le document est refusé au transfert car les droits sont insuffisants");
        }
      }

      LOG.debug("{} - recherche du document", trcPrefix);
      // On récupère le document avec uniquement les méta transférables
      final List<StorageMetadata> desiredMetas = getTransferableStorageMeta();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, desiredMetas);

      StorageDocument document = storageDocumentService.searchStorageDocumentByUUIDCriteria(uuidCriteria);
      final String hashDocGNT = getHashDocument(document);

      // -- Le document n'existe pas en GNT
      if (document == null) {
        // -- On recherche le document sur la GNS
        document = storageTransfertService.searchStorageDocumentByUUIDCriteria(uuidCriteria);

        final String uuid = idArchive.toString();

        // -- Le document n'existe pas sur la GNS (et sur la GNT)
        if (document == null) {
          final String message = "Le document {0} n'existe pas. Transfert impossible.";
          throw new ArchiveInexistanteEx(StringUtils.replace(message, "{0}", uuid));
        } else {
          // -- Le document existe sur la GNS
          final String message = "Le document {0} a déjà été transféré.";
          throw new ArchiveAlreadyTransferedException(StringUtils.replace(message, "{0}", uuid));
        }
      } else {
        // -- Le document existe en GNT

        final String docUUID = document.getUuid().toString();
        final ZookeeperMutex mutex = new ZookeeperMutex(zookeeperCfactory.getClient(), "/Transfert/" + docUUID);

        try {

          // Lock du document
          ZookeeperUtils.acquire(mutex, docUUID);

          // -- On vérifie si le document n'est pas gelé
          if (idArchive != null) {
            final String frozenDocMsgException = "Le document {0} est gelé et ne peut pas être traité.";
            final List<StorageMetadata> listeMetadataDocument = getListeStorageMetadatasWithGel(idArchive);
            if (isFrozenDocument(listeMetadataDocument)) {
              throw new TransfertException(
                                           StringUtils.replace(frozenDocMsgException, "{0}", idArchive.toString()));
            }
          }

          // -- On recherche le document sur la GNS
          StorageDocument documentGNS = storageTransfertService.searchStorageDocumentByUUIDCriteria(uuidCriteria);

          // -- On s'assure que le document n'existe pas en GNS
          // -- OU si il existe en GNT et GNS, on le supprime de la GNS
          if (documentGNS == null
              || getHashDocument(documentGNS) != null && getHashDocument(documentGNS).equals(hashDocGNT)) {

            if (getHashDocument(documentGNS) != null && getHashDocument(documentGNS).equals(hashDocGNT)) {
              try {
                storageTransfertService.deleteStorageDocument(idArchive);
                LOG.info("{} - Transfert - Suppression du document {} de la GNS.",
                         "transfertDoc",
                         idArchive.toString());
              }
              catch (final DeletionServiceEx ex) {
                final String message = "Transfert - La suppression du document {0} de la GNS a échoué.";
                throw new TransfertException(StringUtils.replace(message, "{0}", idArchive.toString()));
              }
            }

            // -- Modification des métadonnées du document pour le
            // transfert
            updateMetaDocumentForTransfert(document);

            // -- Archivage du document en GNS
            try {
              documentGNS = storageTransfertService.insertBinaryStorageDocument(document);

              // -- Récupération des notes associées au document
              // transféré
              final List<StorageDocumentNote> listeNotes = storageDocumentService
                                                                                 .getDocumentsNotes(document.getUuid());
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
                  // annule le
                  // transfert (suppression du document en GNS)
                  try {
                    storageTransfertService.deleteStorageDocument(idArchive);
                  }
                  catch (final DeletionServiceEx erreurSupprGNS) {
                    throw new TransfertException(erreurSupprGNS);
                  }
                  throw new TransfertException(erreur, e);
                }
              }

              // -- Récupération du document attaché éventuel
              StorageDocumentAttachment docAttache;
              try {
                docAttache = storageDocumentService.getDocumentAttachment(document.getUuid());
                // -- Ajout du document attaché sur le document
                // archivés en
                // GNS
                if (docAttache != null) {
                  storageTransfertService.addDocumentAttachment(documentGNS.getUuid(),
                                                                docAttache.getName(),
                                                                docAttache.getExtension(),
                                                                docAttache.getContenu());
                }
              }
              catch (final StorageDocAttachmentServiceEx e) {
                // Le document attaché n'a pas pu être transféré, on
                // annule
                // le transfert (suppression du document en GNS)
                try {
                  storageTransfertService.deleteStorageDocument(idArchive);
                }
                catch (final DeletionServiceEx erreurSupprGNS) {
                  throw new TransfertException(erreurSupprGNS);
                }
                throw new TransfertException(erreur, e);
              }

            }
            catch (final InsertionServiceEx ex) {
              throw new TransfertException(erreur, ex);
            }
            catch (final InsertionIdGedExistantEx e1) {
              throw new TransfertException(erreur, e1);
            }
          } else if (getHashDocument(documentGNS) != null && !getHashDocument(documentGNS).equals(hashDocGNT)) {
            // -- L'idGed du document à transférer existe déjà en GNS
            final String msg = "L'identifiant ged spécifié '%s' existe déjà en GNS et ne peut être utilisé. Transfert impossible.";
            throw new InsertionIdGedExistantEx(String.format(msg, idArchive.toString()));
          }

          // -- Suppression du document transféré de la GNT
          try {
            storageDocumentService.deleteStorageDocumentTraceTransfert(idArchive);
          }
          catch (final DeletionServiceEx erreurSupprGNT) {
            final StorageDocument documentGNT = storageDocumentService
                                                                      .searchMetaDatasByUUIDCriteria(uuidCriteria);
            if (documentGNT != null) {
              // -- Le document existe toujours dans la GNT
              try {
                storageTransfertService.deleteStorageDocument(idArchive);
              }
              catch (final DeletionServiceEx erreurSupprGNS) {
                throw new TransfertException(erreurSupprGNS);
              }
            }
            throw new TransfertException(erreur, erreurSupprGNT);
          }

          if (!ZookeeperUtils.isLock(mutex)) {
            // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne
            // devrait jamais arriver.
            final String message = "Erreur lors de la tentative d'acquisition du lock pour le transfert "
                + idArchive + ". Problème de connexion zookeeper ?";
            LOG.warn(message);
          }
        }
        finally {
          mutex.release();
        }
        LOG.debug("{} - Fin de transfert du document {}", new Object[] {trcPrefix, idArchive.toString()});
      }
    }
    catch (final SearchingServiceEx ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final ReferentialException ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final InvalidSAETypeException ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final MappingFromReferentialException ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final RetrievalServiceEx ex) {
      throw new TransfertException(erreur, ex);
    }
    catch (final InsertionIdGedExistantEx ex) {
      throw new TransfertException(erreur, ex);
    }
  }

  /**
   * Retourne le hash du storageDocument passé en paramètre
   * 
   * @param storageDocument
   *          document
   * @return
   */
  private String getHashDocument(final StorageDocument document) {
    String hashDocument = null;
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
   * Retourne l'index de la métadonnée dans la liste à partir de son code
   * 
   * @param indexMetaData
   * @param metaDataList
   * @return la valeur de la métadonnée à partir de son shortCode
   */
  private Integer getIndexMetaByCode(final String codeMetaData, final List<StorageMetadata> metaDataList) {
    Integer indexMetaData = null;
    if (metaDataList != null) {
      for (final StorageMetadata metadata : metaDataList) {
        if (codeMetaData.equals(metadata.getShortCode())) {
          indexMetaData = metaDataList.indexOf(metadata);
          break;
        }
      }
    }
    return indexMetaData;
  }

  /**
   * Retourne la date de début de conservation du document passé en paramètre
   * 
   * @param uuidDoc
   * @return
   * @throws TransfertException
   */
  private StorageMetadata getDateDebutConservation(final UUID uuidDoc) throws TransfertException {

    StorageMetadata dateDebutConservationMeta = null;
    final List<StorageMetadata> desiredStorageMetadatas = new ArrayList<>();
    desiredStorageMetadatas
                           .add(new StorageMetadata(StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode()));
    try {
      final List<StorageMetadata> metadatasFind = storageDocumentService
                                                                        .retrieveStorageDocumentMetaDatasByUUID(new UUIDCriteria(uuidDoc,
                                                                                                                                 desiredStorageMetadatas));
      final Object dateDebutConservation = StorageMetadataUtils.valueObjectMetadataFinder(metadatasFind,
                                                                                          StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode());
      dateDebutConservationMeta = new StorageMetadata(
                                                      StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode(),
                                                      dateDebutConservation);
    }
    catch (final RetrievalServiceEx e) {
      throw new TransfertException(e);
    }
    return dateDebutConservationMeta;
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

    // Création des critéres
    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

    // Recherche du document par critére
    return getStorageDocumentService().retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
  }

}