package fr.urssaf.image.sae.services.batch.transfert.support.controle.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.batch.transfert.support.controle.TransfertMasseControleSupport;
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
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Implémentation du support {@link TransfertMasseControleSupport}
 */
@Component
public class TransfertMasseControleSupportImpl implements TransfertMasseControleSupport {

  /**
   * Logger
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(TransfertMasseControleSupportImpl.class);

  /**
   * SAETransfertService
   */
  @Autowired
  private SAETransfertService transfertService;

  /**
   * MappingDocumentService
   */
  @Autowired
  private MappingDocumentService mappingDocumentService;

  /**
   * Provider de service pour la connexion DFCE de la GNT
   */
  @Autowired
  private StorageServiceProvider storageServiceProvider;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean controleSAEDocumentSuppression(final UntypedDocument item)
      throws SearchingServiceEx, ConnectionServiceEx {

    final StorageDocument storageDocument = storageServiceProvider.getStorageDocumentService()
                                                                  .searchMetaDatasByUUIDCriteria(new UUIDCriteria(item.getUuid(), null));

    if (storageDocument != null && storageDocument.getUuid() != null) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageDocument controleSAEDocumentTransfert(final UntypedDocument item, final UUID idTraitementMasse)
      throws ReferentialException, SearchingServiceEx, ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
      TransfertException, InvalidSAETypeException, MappingFromReferentialException, UnknownMetadataEx,
      DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx,
      MetadataValueNotInDictionaryEx, NotModifiableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
      NotSpecifiableMetadataEx, UnknownHashCodeEx, TraitementRepriseAlreadyDoneException {

    final String trcPrefix = "controleSAEDocumentTransfert()";
    LOGGER.debug("{} - début", trcPrefix);

    return controleSAEDocumentTransfertCommon(item, false, idTraitementMasse);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageDocument controleSAEDocumentRepriseTransfert(final UntypedDocument item, final UUID idTraitementMasse)
      throws ReferentialException, SearchingServiceEx, ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
      TransfertException, InvalidSAETypeException, MappingFromReferentialException, UnknownMetadataEx,
      DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx,
      MetadataValueNotInDictionaryEx, ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
      UnknownHashCodeEx, NotModifiableMetadataEx, TraitementRepriseAlreadyDoneException {

    final String trcPrefix = "controleSAEDocumentRepriseTransfert()";
    LOGGER.debug("{} - début", trcPrefix);

    return controleSAEDocumentTransfertCommon(item, true, idTraitementMasse);
  }

  /**
   * Controle du document pour le transfert de masse.
   * 
   * @param untypedDoc
   *          document
   * @param isReprise
   *          Mode reprise, true actif, false sinon
   * @param idTraitementMasse
   *          Identifiant traitement de masse
   * @return Le document controlé pour le transfert.
   * @throws TransfertException
   * @{@link TransfertException}
   * @throws ArchiveAlreadyTransferedException
   * @{@link ArchiveAlreadyTransferedException}
   * @throws TraitementRepriseAlreadyDoneException
   * @{@link TraitementRepriseAlreadyDoneException}
   */
  private StorageDocument controleSAEDocumentTransfertCommon(final UntypedDocument untypedDoc, final boolean isReprise,
                                                             final UUID idTraitementMasse)
      throws ArchiveAlreadyTransferedException,
      TransfertException, TraitementRepriseAlreadyDoneException {

    final String trcPrefix = "controleSAEDocumentTransfert()";
    LOGGER.debug("{} - début", trcPrefix);

    final StorageDocument document = transfertService.controleDocumentTransfertMasse(untypedDoc.getUuid(),
                                                                                     untypedDoc.getUMetadatas(),
                                                                                     isReprise,
                                                                                     idTraitementMasse,
                                                                                     untypedDoc.getBatchActionType().equals("SUPPRESSION"));
    // Charger le typeAction dans le storageDoc
    document.setBatchTypeAction(untypedDoc.getBatchActionType());
    return document;

  }

  /**
   * {@inheritDoc}
   * 
   * @throws RetrievalServiceEx
   * @throws ReferentialException
   */
  @Override
  public List<StorageMetadata> getListeStorageMetadatasWithGel(final UUID idArchive)
      throws ReferentialException, RetrievalServiceEx {

    return transfertService.getListeStorageMetadatasWithGel(idArchive);
  }

}
