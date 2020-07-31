/**
 *
 */
package fr.urssaf.image.sae.services.modification.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.constants.Constants;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
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
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.ModificationRuntimeException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe d'implémentation de l'interface {@link SAEModificationService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired
 *
 */
@Service
public class SAEModificationServiceImpl extends AbstractSAEServices implements SAEModificationService {

  private static final Logger LOG = LoggerFactory.getLogger(SAEModificationServiceImpl.class);

  @Autowired
  @Qualifier("storageDocumentService")
  private StorageDocumentService documentService;

  @Autowired
  private SAEControlesModificationService controlesModificationService;

  @Autowired
  private MappingDocumentService mappingDocumentService;

  @Autowired
  private RndService rndService;

  @Autowired
  private MetadataReferenceDAO referenceDAO;

  @Autowired
  private MappingDocumentService mappingService;

  @Autowired
  private PrmdService prmdService;

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void modification(final UUID idArchive, final List<UntypedMetadata> metadonnees)
      throws InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, DuplicatedMetadataEx,
      NotSpecifiableMetadataEx, RequiredArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
      UnknownHashCodeEx, NotModifiableMetadataEx, ModificationException, ArchiveInexistanteEx,
      MetadataValueNotInDictionaryEx, ReferentialException, RetrievalServiceEx {
    final String trcPrefix = "modification";
    LOG.debug("{} - début", trcPrefix);

    LOG.debug("{} - recherche du document", trcPrefix);

    try {
      final List<StorageMetadata> listeStorageMetaDocument = controlerMetaDocumentModifie(idArchive,
                                                                                          metadonnees, trcPrefix, "modification");

      // Gestion de document gelé
      if (idArchive != null) {
        final String frozenDocMsgException = "Le document {0} est gelé et ne peut pas être traité.";
        final List<StorageMetadata> listeMetadataDocument = getListeStorageMetadatasWithGel(idArchive);
        if (isFrozenDocument(listeMetadataDocument)) {
          throw new ModificationException(
                                          StringUtils.replace(frozenDocMsgException, "{0}", idArchive.toString()));
        }
      }

      final StorageDocument document = separationMetaDocumentModifie(idArchive, listeStorageMetaDocument,
                                                                     metadonnees, trcPrefix);

      modificationMetaDocument(document, trcPrefix);

    } catch (final UpdateServiceEx exception) {
      throw new ModificationRuntimeException(exception);
    }

    LOG.debug("{} - fin", trcPrefix);
  }

  /**
   * Vérifie les règles de gestion des métadonnées
   *
   * @param modifiedMetadatas
   *            la liste des métadonnées à modifier
   * @return la nouvelle liste enrichie des métadonnées
   * @throws UnknownCodeRndEx
   * @throws CodeRndInexistantException
   */
  private List<List<UntypedMetadata>> completeMetadatas(final List<UntypedMetadata> modifiedMetadatas,
                                                        final List<UntypedMetadata> deletedMetadatas, final List<StorageMetadata> list) throws UnknownCodeRndEx {
    final String trcPrefix = "completeMetadatas";
    LOG.debug("{} - début", trcPrefix);

    final List<UntypedMetadata> returnedModifiedList = new ArrayList<>(modifiedMetadatas);
    final List<UntypedMetadata> returnedDeletedList = new ArrayList<>(deletedMetadatas);
    final String codeRnd = getValueMeta(SAEArchivalMetadatas.CODE_RND, modifiedMetadatas);

    try {
      if (StringUtils.isNotBlank(codeRnd)) {

        final Date date = (Date) getValueMeta("SM_LIFE_CYCLE_REFERENCE_DATE", list);
        String codeActivite;

        codeActivite = rndService.getCodeActivite(codeRnd);

        final String codeFonction = rndService.getCodeFonction(codeRnd);

        final int duration = rndService.getDureeConservation(codeRnd);
        final Date dateFin = DateUtils.addDays(date, duration);
        final String sDateFin = DateFormatUtils.format(dateFin, Constants.DATE_PATTERN,
                                                       Constants.DEFAULT_LOCAL);
        if (StringUtils.isNotEmpty(codeActivite)) {
          returnedModifiedList
          .add(new UntypedMetadata(SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode(), codeActivite));
        } else {
          returnedDeletedList
          .add(new UntypedMetadata(SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode(), codeActivite));
        }

        returnedModifiedList
        .add(new UntypedMetadata(SAEArchivalMetadatas.CODE_FONCTION.getLongCode(), codeFonction));
        returnedModifiedList
        .add(new UntypedMetadata(SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode(), sDateFin));

      }
    } catch (final CodeRndInexistantException e) {
      throw new UnknownCodeRndEx(e.getMessage(), e);
    }

    final List<List<UntypedMetadata>> returnedList = new ArrayList<>();
    returnedList.add(returnedModifiedList);
    returnedList.add(returnedDeletedList);

    LOG.debug("{} - fin", trcPrefix);
    return returnedList;
  }

  /**
   * Donne la valeur de la metadonnees en fonction de son code
   * ({@link SAEArchivalMetadatas}) à partir d'une liste de metadonnees.
   * 
   * @param codeMeta
   *            Code metadonnees.
   * @param metadatas
   *            Liste de metadonnees.
   * @return La valeur de la metadonnees en fonction de son code.
   */
  private String getValueMeta(final SAEArchivalMetadatas codeMeta, final List<UntypedMetadata> metadatas) {
    String value = null;
    int index = 0;

    while (StringUtils.isBlank(value) && index < metadatas.size()) {

      if (codeMeta.getLongCode().equals(metadatas.get(index).getLongCode())) {
        value = metadatas.get(index).getValue();
      }
      index++;
    }

    return value;
  }

  /**
   * Donne la valeur de la metadonnees en fonction de son code (String) à
   * partir d'une liste de metadonnees.
   * 
   * @param shortCode
   *            Short code metadonnees.
   * @param metadatas
   *            Liste de metadonnees.
   * @return La valeur de la metadonnees en fonction de son code.
   */
  private Object getValueMeta(final String shortCode, final List<StorageMetadata> metadatas) {
    Object value = null;
    int index = 0;

    while (value == null && index < metadatas.size()) {

      if (shortCode.equals(metadatas.get(index).getShortCode())) {
        value = metadatas.get(index).getValue();
      }
      index++;
    }

    return value;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public List<StorageMetadata> controlerMetaDocumentModifie(final UUID idArchive,
                                                            final List<UntypedMetadata> metadonnees, final String trcPrefix, final String actionUnitaire)
                                                                throws ArchiveInexistanteEx, ModificationException, DuplicatedMetadataEx {
    List<StorageMetadata> listeStorageMeta;

    try {

      listeStorageMeta = getListeStorageMetadatas(idArchive);

      if (listeStorageMeta.size() == 0) {
        final String message = StringUtils.replace(
                                                   "Il n'existe aucun document pour l'identifiant d'archivage '{0}'", "{0}", idArchive.toString());
        throw new ArchiveInexistanteEx(message);
      }
      final List<UntypedMetadata> listeUMeta = mappingService.storageMetadataToUntypedMetadata(listeStorageMeta);

      // Vérification des droits
      LOG.debug("{} - Récupération des droits", trcPrefix);
      final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder.getContext()
          .getAuthentication();
      final List<SaePrmd> saePrmds = token.getSaeDroits().get(actionUnitaire);
      LOG.debug("{} - Vérification des droits", trcPrefix);
      final boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

      if (!isPermitted) {
        throw new AccessDeniedException(
            "Le document est refusé à la modification car les droits sont insuffisants");

      }

    } catch (final InvalidSAETypeException exception) {
      throw new ModificationException(exception);
    } catch (final MappingFromReferentialException exception) {
      throw new ModificationException(exception);
    } catch (final ReferentialException exception) {
      throw new ModificationException(exception);
    } catch (final RetrievalServiceEx exception) {
      throw new ModificationException(exception);
    }

    LOG.debug("{} - vérification non dupplication des métadonnées", trcPrefix);
    if (!CollectionUtils.isEmpty(metadonnees)) {
      controlesModificationService.checkSaeMetadataForModification(metadonnees);
    }

    return listeStorageMeta;

  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public List<StorageMetadata> getListeStorageMetadatas(final UUID idArchive)
      throws ReferentialException, RetrievalServiceEx {
    // On récupère la liste de toutes les méta du référentiel sauf la
    // Note, le Gel et la durée de conservation inutile pour les droits
    // et générant des accès DFCE inutiles
    final List<StorageMetadata> allMeta = new ArrayList<>();
    final Map<String, MetadataReference> listeAllMeta = referenceDAO.getAllMetadataReferencesPourVerifDroits();

    for (final Map.Entry<String, MetadataReference> entry : listeAllMeta.entrySet()) {
      allMeta.add(new StorageMetadata(entry.getValue().getShortCode()));
    }

    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

    return getStorageDocumentService().retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void modificationMetaDocument(final StorageDocument document, final String trcPrefix)
      throws ModificationException, UpdateServiceEx {
    if (document != null && document.getUuid() != null
        && (document.getMetadatas() != null || document.getMetadatasToDelete() != null)) {
      documentService.updateStorageDocument(document.getUuid(), document.getMetadatas(),
                                            document.getMetadatasToDelete());
    } else {
      throw new ModificationException("Une erreur interne à l'application est survenue lors de la modification");
    }
  }

  @Override
  public StorageDocument separationMetaDocumentModifie(final UUID idArchive,
                                                       final List<StorageMetadata> listeStorageMetaDocument, final List<UntypedMetadata> metadonnees,
                                                       final String trcPrefix) throws UnknownCodeRndEx, ReferentialRndException, InvalidValueTypeAndFormatMetadataEx,
  UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
  UnknownHashCodeEx, NotModifiableMetadataEx, MetadataValueNotInDictionaryEx, ModificationException {

    LOG.debug("{} - Séparation des métadonnées en modifiées et supprimées", trcPrefix);
    List<UntypedMetadata> modifiedMetadatas = new ArrayList<>();
    List<UntypedMetadata> deletedMetadatas = new ArrayList<>();
    for (final UntypedMetadata metadata : metadonnees) {
      if (StringUtils.isNotBlank(metadata.getValue())) {
        modifiedMetadatas.add(metadata);
      } else {
        deletedMetadatas.add(metadata);
      }
    }

    LOG.debug("{} - vérification des métadonnées", trcPrefix);
    if (!CollectionUtils.isEmpty(modifiedMetadatas)) {
      controlesModificationService.checkSaeMetadataForUpdate(modifiedMetadatas);

    }
    if (!CollectionUtils.isEmpty(deletedMetadatas)) {
      controlesModificationService.checkSaeMetadataForDelete(deletedMetadatas);
    }

    // Application des règles de gestion pour compléter les métadonnées (ex
    // :
    // enrichissement du code fonction, activité et
    // date de fin si modification du code RND)
    final List<List<UntypedMetadata>> completedMetadatas = completeMetadatas(modifiedMetadatas, deletedMetadatas,
                                                                             listeStorageMetaDocument);

    modifiedMetadatas = completedMetadatas.get(0);
    deletedMetadatas = completedMetadatas.get(1);

    final StorageDocument storageDocument = new StorageDocument();
    storageDocument.setUuid(idArchive);
    try {
      List<StorageMetadata> modifiedStorageMetas = new ArrayList<>();
      if (!CollectionUtils.isEmpty(modifiedMetadatas)) {
        final List<SAEMetadata> modifiedSaeMetadatas = mappingDocumentService
            .untypedMetadatasToSaeMetadatas(modifiedMetadatas);
        modifiedStorageMetas = mappingDocumentService.saeMetadatasToStorageMetadatas(modifiedSaeMetadatas);
        storageDocument.setMetadatas(modifiedStorageMetas);
      }

      List<StorageMetadata> deletedStorageMetas = new ArrayList<>();
      if (!CollectionUtils.isEmpty(deletedMetadatas)) {
        final List<SAEMetadata> deletedSaeMetadatas = mappingDocumentService
            .nullSafeUntypedMetadatasToSaeMetadatas(deletedMetadatas);
        deletedStorageMetas = mappingDocumentService.saeMetadatasToStorageMetadatas(deletedSaeMetadatas);
        storageDocument.setMetadatasToDelete(deletedStorageMetas);
      }

    } catch (final InvalidSAETypeException exception) {
      throw new ModificationRuntimeException(exception);

    } catch (final MappingFromReferentialException exception) {
      throw new ModificationRuntimeException(exception);

    }

    return storageDocument;
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
    final Map<String, MetadataReference> listeAllMeta = referenceDAO.getAllMetadataReferencesPourVerifDroits();

    for (final Map.Entry<String, MetadataReference> entry : listeAllMeta.entrySet()) {
      allMeta.add(new StorageMetadata(entry.getValue().getShortCode()));
    }
    // Ajout de la meta GEL puisque non récupéré avant
    allMeta.add(new StorageMetadata(StorageTechnicalMetadatas.GEL.getShortCode()));

    // Création des critéres
    final UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

    // Recherche du document par critére
    return getStorageDocumentService().retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
  }

}
