package fr.urssaf.image.sae.mapping.services.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentAttachment;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Classe qui fournit des services de conversion entre objet du modèle et objet
 * technique de stockage.
 */
@Service
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class MappingDocumentServiceImpl implements MappingDocumentService {

  @Autowired
  private MetadataReferenceDAO referenceDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops",
                     "PMD.DataflowAnomalyAnalysis"})
  public StorageDocument saeDocumentToStorageDocument(final SAEDocument saeDoc)
      throws InvalidSAETypeException {

    final StorageDocument storageDoc = new StorageDocument();

    final List<StorageMetadata> sMetadata = saeMetadatasToStorageMetadatas(saeDoc
                                                                                 .getMetadatas());

    storageDoc.setContent(saeDoc.getContent());

    storageDoc.setFileName(saeDoc.getFileName());

    storageDoc.setFilePath(saeDoc.getFilePath());

    storageDoc.setMetadatas(sMetadata);

    return storageDoc;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public SAEDocument storageDocumentToSaeDocument(
                                                  final StorageDocument storageDoc)
      throws InvalidSAETypeException,
      MappingFromReferentialException {
    final SAEDocument saeDoc = new SAEDocument();
    final List<SAEMetadata> metadatas = new ArrayList<>();
    saeDoc.setContent(storageDoc.getContent());
    saeDoc.setFilePath(storageDoc.getFilePath());
    saeDoc.setFileName(storageDoc.getFileName());
    for (final StorageMetadata sMetadata : Utils.nullSafeIterable(storageDoc
                                                                            .getMetadatas())) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByShortCode(sMetadata.getShortCode());
        metadatas.add(new SAEMetadata(reference.getLongCode(),
                                      reference
                                               .getShortCode(),
                                      sMetadata.getValue()));
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }

    }
    saeDoc.setMetadatas(metadatas);
    return saeDoc;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public UntypedDocument saeDocumentToUntypedDocument(final SAEDocument saeDoc)
      throws InvalidSAETypeException, MappingFromReferentialException {
    final List<UntypedMetadata> metadatas = new ArrayList<>();
    for (final SAEMetadata metadata : Utils.nullSafeIterable(saeDoc.getMetadatas())) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByLongCode(metadata.getLongCode());

        metadatas.add(new UntypedMetadata(metadata.getLongCode(),
                                          Utils
                                               .convertToString(metadata.getValue(), reference)));
      }
      catch (final ParseException parseExcept) {
        throw new InvalidSAETypeException(parseExcept);
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }
    }

    return new UntypedDocument(saeDoc.getContent(),
                               saeDoc.getFilePath(),
                               saeDoc.getFileName(),
                               metadatas);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public SAEDocument untypedDocumentToSaeDocument(final UntypedDocument untyped)
      throws InvalidSAETypeException, MappingFromReferentialException {
    final List<SAEMetadata> metadatas = untypedMetadatasToSaeMetadatas(untyped
                                                                              .getUMetadatas());

    return new SAEDocument(untyped.getFilePath(),
                           untyped.getContent(),
                           untyped.getFileName(),
                           metadatas);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public UntypedDocument storageDocumentToUntypedDocument(
                                                          final StorageDocument storage)
      throws InvalidSAETypeException,
      MappingFromReferentialException {
    final List<UntypedMetadata> metadatas = new ArrayList<>();
    for (final StorageMetadata metadata : Utils.nullSafeIterable(storage
                                                                        .getMetadatas())) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByShortCode(metadata.getShortCode());
        metadatas.add(new UntypedMetadata(reference.getLongCode(),
                                          Utils
                                               .convertToString(metadata.getValue(), reference)));
      }
      catch (final ParseException parseExcept) {
        throw new InvalidSAETypeException(parseExcept);
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }
    }
    String name = null;
    if (StringUtils.isBlank(storage.getFilePath())) {
      name = storage.getFileName();
    } else {
      name = FilenameUtils.getBaseName(storage.getFilePath());
    }
    final UntypedDocument untypedDocument = new UntypedDocument(
                                                                storage.getContent(),
                                                                storage.getFilePath(),
                                                                name,
                                                                metadatas);
    untypedDocument.setUuid(storage.getUuid());
    return untypedDocument;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VirtualStorageDocument saeVirtualDocumentToVirtualStorageDocument(
                                                                           final SAEVirtualDocument document)
      throws InvalidSAETypeException {

    final VirtualStorageDocument storageDocument = new VirtualStorageDocument();
    final List<StorageMetadata> metadatas = new ArrayList<>();

    storageDocument.setEndPage(document.getEndPage());
    storageDocument.setStartPage(document.getStartPage());

    final String filePath = document.getReference().getFilePath();
    String fileName = FilenameUtils.getBaseName(filePath);
    fileName = fileName.concat("_")
                       .concat(String.valueOf(document.getStartPage()))
                       .concat("_")
                       .concat(String.valueOf(document.getEndPage()));
    storageDocument.setFileName(fileName);

    for (final SAEMetadata metadata : Utils.nullSafeIterable(document
                                                                     .getMetadatas())) {
      metadatas.add(new StorageMetadata(metadata.getShortCode(),
                                        metadata
                                                .getValue()));
    }
    storageDocument.setMetadatas(metadatas);

    return storageDocument;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SAEMetadata> untypedMetadatasToSaeMetadatas(
                                                          final List<UntypedMetadata> metadatas)
      throws InvalidSAETypeException,
      MappingFromReferentialException {
    final List<SAEMetadata> saeMetadatas = new ArrayList<>();
    for (final UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByLongCode(metadata.getLongCode());
        saeMetadatas.add(new SAEMetadata(reference.getLongCode(),
                                         reference
                                                  .getShortCode(),
                                         Utils.conversionToObject(
                                                                  metadata.getValue(),
                                                                  reference)));
      }
      catch (final ParseException parseExcept) {
        throw new InvalidSAETypeException(parseExcept);
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }
    }

    return saeMetadatas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SAEMetadata> nullSafeUntypedMetadatasToSaeMetadatas(
                                                                  final List<UntypedMetadata> metadatas)
      throws InvalidSAETypeException,
      MappingFromReferentialException {
    final List<SAEMetadata> saeMetadatas = new ArrayList<>();
    for (final UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByLongCode(metadata.getLongCode());
        if (StringUtils.isEmpty(metadata.getValue())) {
          saeMetadatas.add(new SAEMetadata(reference.getLongCode(),
                                           reference.getShortCode(),
                                           null));
        } else {
          saeMetadatas.add(new SAEMetadata(reference.getLongCode(),
                                           reference.getShortCode(),
                                           Utils.conversionToObject(
                                                                    metadata.getValue(),
                                                                    reference)));
        }
      }
      catch (final ParseException parseExcept) {
        throw new InvalidSAETypeException(parseExcept);
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }
    }

    return saeMetadatas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SAEVirtualDocument untypedVirtualDocumentToSaeVirtualDocument(
                                                                       final UntypedVirtualDocument document)
      throws InvalidSAETypeException,
      MappingFromReferentialException {

    final SAEVirtualDocument virtualDocument = new SAEVirtualDocument();
    virtualDocument.setEndPage(document.getEndPage());
    virtualDocument.setIndex(document.getIndex());
    final List<SAEMetadata> metadatas = untypedMetadatasToSaeMetadatas(document
                                                                               .getuMetadatas());
    virtualDocument.setMetadatas(metadatas);
    virtualDocument.setStartPage(document.getStartPage());
    virtualDocument.setReference(document.getReference());

    return virtualDocument;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StorageMetadata> saeMetadatasToStorageMetadatas(
                                                              final List<SAEMetadata> metadatas) {

    final List<StorageMetadata> sMetadata = new ArrayList<>();
    for (final SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
      sMetadata.add(new StorageMetadata(metadata.getShortCode(),
                                        metadata
                                                .getValue()));
    }

    return sMetadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<StorageMetadata> untypedMetadatasToStorageMetadatas(final List<UntypedMetadata> metadatas)
      throws InvalidSAETypeException, MappingFromReferentialException {
    final List<StorageMetadata> saeStorageMetadatas = new ArrayList<>();
    for (final UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByLongCode(metadata.getLongCode());
        if (metadata.getValue() != null && StringUtils.isNotBlank(metadata.getValue().toString())) {
          saeStorageMetadatas.add(new StorageMetadata(reference.getShortCode(), Utils.conversionToObject(metadata.getValue(), reference)));
        } else {
          // Correspond à une métadonnée à supprimer, on met donc volontairement une chaine vide.
          saeStorageMetadatas.add(new StorageMetadata(reference.getShortCode(), StringUtils.EMPTY));
        }
      }
      catch (final ParseException parseExcept) {
        throw new InvalidSAETypeException(parseExcept);
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }
    }

    return saeStorageMetadatas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UntypedMetadata> storageMetadataToUntypedMetadata(
                                                                final List<StorageMetadata> storageMetas)
      throws InvalidSAETypeException,
      MappingFromReferentialException {

    final List<UntypedMetadata> untypedMetadatas = new ArrayList<>();
    for (final StorageMetadata metadata : Utils.nullSafeIterable(storageMetas)) {
      try {
        final MetadataReference reference = referenceDAO
                                                        .getByShortCode(metadata.getShortCode());

        untypedMetadatas.add(new UntypedMetadata(reference.getLongCode(),
                                                 Utils.convertToString(metadata.getValue(), reference)));

      }
      catch (final ParseException parseExcept) {
        throw new InvalidSAETypeException(parseExcept);
      }
      catch (final ReferentialException refExcpt) {
        throw new MappingFromReferentialException(refExcpt);
      }
    }

    return untypedMetadatas;

  }

  @Override
  public UntypedDocumentAttachment storageDocumentAttachmentToUntypedDocumentAttachment(
                                                                                        final StorageDocumentAttachment storage)
      throws InvalidSAETypeException,
      MappingFromReferentialException {

    final List<UntypedMetadata> metadatas = new ArrayList<>();

    storage.getContenu();

    try {
      /**
       * Afin de retourner une liste de métadonnées lors de la consultation
       * d'un document attaché, on construit une liste de métadonnées à
       * partir des attributs du document attaché
       */
      // Date d'archivage
      final Date dateArchivage = storage.getDateArchivage();
      MetadataReference reference;

      reference = referenceDAO.getByShortCode("SM_ARCHIVAGE_DATE");
      metadatas.add(new UntypedMetadata(reference.getLongCode(),
                                        Utils
                                             .convertToString(dateArchivage, reference)));

      // Nom du fichier
      final String nom = storage.getName();
      final String extension = storage.getExtension();
      reference = referenceDAO.getByShortCode("nfi");
      metadatas.add(new UntypedMetadata(reference.getLongCode(),
                                        nom.concat(
                                                   ".")
                                           .concat(extension)));

      // Hash
      final String hash = storage.getHash();
      reference = referenceDAO.getByShortCode("SM_DIGEST");
      metadatas.add(new UntypedMetadata(reference.getLongCode(),
                                        Utils
                                             .convertToString(hash, reference)));

      final UntypedDocumentAttachment untypedDocument = new UntypedDocumentAttachment(
                                                                                      storage.getDocUuid(),
                                                                                      storage.getContenu(),
                                                                                      metadatas);

      return untypedDocument;
    }
    catch (final ReferentialException e) {
      throw new MappingFromReferentialException(e);
    }
    catch (final ParseException e) {
      throw new InvalidSAETypeException(e);
    }
  }

  /**
   * Setter pour referenceDAO
   * 
   * @param referenceDAO
   *          the referenceDAO to set
   */
  public void setReferenceDAO(final MetadataReferenceDAO referenceDAO) {
    this.referenceDAO = referenceDAO;
  }

}
