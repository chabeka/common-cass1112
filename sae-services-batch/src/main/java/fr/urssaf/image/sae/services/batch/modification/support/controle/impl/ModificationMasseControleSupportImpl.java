/**
 *
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.modification.support.controle.ModificationMasseControleSupport;
import fr.urssaf.image.sae.services.batch.modification.support.controle.model.ModificationMasseControlResult;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * Implémentation du support {@link CaptureMasseControleSupport}
 *
 */
@Component
public class ModificationMasseControleSupportImpl extends AbstractSAEServices
implements ModificationMasseControleSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModificationMasseControleSupportImpl.class);

  @Autowired
  private SAEModificationService modificationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public ModificationMasseControlResult controleSAEDocumentMetadatas(final UntypedDocument document,
                                                                     final File ecdeDirectory) throws ReferentialRndException, UnknownCodeRndEx, ArchiveInexistanteEx,
  ModificationException, DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
  NotSpecifiableMetadataEx, RequiredArchivableMetadataEx, UnknownHashCodeEx, NotModifiableMetadataEx,
  MetadataValueNotInDictionaryEx, CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx {
    final String trcPrefix = "controleSAEDocumentMetadatas()";
    LOGGER.debug("{} - début", trcPrefix);

    ModificationMasseControlResult result = null;

    try {

      final List<StorageMetadata> storageMetadatasList = modificationService.controlerMetaDocumentModifie(
                                                                                                          document.getUuid(), document.getUMetadatas(), trcPrefix, "modification_masse");

      if (storageMetadatasList != null && !storageMetadatasList.isEmpty()) {
        result = new ModificationMasseControlResult();
        result.setStorageMetadatasList(storageMetadatasList);
      }

    } catch (final ConnectionServiceEx e) {
      throw new ModificationException(e);
    }

    LOGGER.debug("{} - fin", trcPrefix);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageDocument controleSAEDocumentModification(final UUID uuidJob, final UntypedDocument item)
      throws UnknownCodeRndEx, ReferentialRndException, InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
      DuplicatedMetadataEx, NotSpecifiableMetadataEx, RequiredArchivableMetadataEx, UnknownHashCodeEx,
      NotModifiableMetadataEx, MetadataValueNotInDictionaryEx, ModificationException, ReferentialException,
      RetrievalServiceEx, TraitementRepriseAlreadyDoneException, SearchingServiceEx, ArchiveInexistanteEx {

    final String trcPrefix = "controleSAEDocumentModification()";
    LOGGER.debug("{} - début", trcPrefix);
    StorageDocument document = null;
    // Gestion de document gelé
    if (item != null && item.getUuid() != null) {
      final String frozenDocMsgException = "Le document {0} est gelé et ne peut pas être traité.";
      final UUID idArchive = item.getUuid();
      final List<StorageMetadata> listeMetadataDocumentWithGel = modificationService
          .getListeStorageMetadatasWithGel(idArchive);
      if (isFrozenDocument(listeMetadataDocumentWithGel)) {
        throw new ModificationException(
                                        StringUtils.replace(frozenDocMsgException, "{0}", idArchive.toString()));
      }

      try {
        final List<StorageMetadata> listeMetadataDocument = modificationService.controlerMetaDocumentModifie(
                                                                                                             item.getUuid(), item.getUMetadatas(), trcPrefix, "modification_masse");

        if (listeMetadataDocument != null && !listeMetadataDocument.isEmpty()) {
          document = modificationService.separationMetaDocumentModifie(item.getUuid(), listeMetadataDocument,
                                                                       item.getUMetadatas(), trcPrefix);

          final String idModifMasseInterne = StorageMetadataUtils.valueMetadataFinder(listeMetadataDocument,
                                                                                      StorageTechnicalMetadatas.ID_MODIFICATION_MASSE_INTERNE.getShortCode());

          if (StringUtils.isNotEmpty(idModifMasseInterne) && idModifMasseInterne.equals(uuidJob.toString())) {
            final String message = "Le document {0} a déjà été modifié par le traitement de masse en cours ({1})";
            final String messageFormat = StringUtils.replaceEach(message, new String[] { "{0}", "{1}" },
                                                                 new String[] { item.getUuid().toString(), uuidJob.toString() });
            LOGGER.warn(messageFormat);
            throw new TraitementRepriseAlreadyDoneException(messageFormat);
          } else {
            document.getMetadatas()
            .add(new StorageMetadata(
                                     StorageTechnicalMetadatas.ID_MODIFICATION_MASSE_INTERNE.getShortCode(),
                                     uuidJob.toString()));
          }
        }

      } catch (final ConnectionServiceEx e) {
        throw new ModificationException(e);
      }
    } else {
      throw new ModificationException("item or uuid  null");
    }
    LOGGER.debug("{} - fin", trcPrefix);
    return document;
  }

}
