package fr.urssaf.image.sae.mapping.services.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
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
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Classe qui fournit des services de conversion entre objet du modèle et objet
 * technique de stockage.
 * 
 */
@Service
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class MappingDocumentServiceImpl implements MappingDocumentService {

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops",
         "PMD.DataflowAnomalyAnalysis" })
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
    * 
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public SAEDocument storageDocumentToSaeDocument(
         final StorageDocument storageDoc) throws InvalidSAETypeException,
         MappingFromReferentialException {
      final SAEDocument saeDoc = new SAEDocument();
      final List<SAEMetadata> metadatas = new ArrayList<SAEMetadata>();
      saeDoc.setContent(storageDoc.getContent());
      saeDoc.setFilePath(storageDoc.getFilePath());
      saeDoc.setFileName(storageDoc.getFileName());
      for (StorageMetadata sMetadata : Utils.nullSafeIterable(storageDoc
            .getMetadatas())) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByShortCode(sMetadata.getShortCode());
            metadatas.add(new SAEMetadata(reference.getLongCode(), reference
                  .getShortCode(), sMetadata.getValue()));
         } catch (ReferentialException refExcpt) {
            throw new MappingFromReferentialException(refExcpt);
         }

      }
      saeDoc.setMetadatas(metadatas);
      return saeDoc;

   }

   /**
    * {@inheritDoc}
    * 
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public UntypedDocument saeDocumentToUntypedDocument(final SAEDocument saeDoc)
         throws InvalidSAETypeException, MappingFromReferentialException {
      final List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      for (SAEMetadata metadata : Utils.nullSafeIterable(saeDoc.getMetadatas())) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());

            metadatas.add(new UntypedMetadata(metadata.getLongCode(), Utils
                  .convertToString(metadata.getValue(), reference)));
         } catch (ParseException parseExcept) {
            throw new InvalidSAETypeException(parseExcept);
         } catch (ReferentialException refExcpt) {
            throw new MappingFromReferentialException(refExcpt);
         }
      }

      return new UntypedDocument(saeDoc.getContent(), saeDoc.getFilePath(),
            saeDoc.getFileName(), metadatas);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public SAEDocument untypedDocumentToSaeDocument(final UntypedDocument untyped)
         throws InvalidSAETypeException, MappingFromReferentialException {
      final List<SAEMetadata> metadatas = untypedMetadatasToSaeMetadatas(untyped
            .getUMetadatas());

      return new SAEDocument(untyped.getFilePath(), untyped.getContent(),
            untyped.getFileName(), metadatas);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public UntypedDocument storageDocumentToUntypedDocument(
         final StorageDocument storage) throws InvalidSAETypeException,
         MappingFromReferentialException {
      final List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      for (StorageMetadata metadata : Utils.nullSafeIterable(storage
            .getMetadatas())) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByShortCode(metadata.getShortCode());
            metadatas.add(new UntypedMetadata(reference.getLongCode(), Utils
                  .convertToString(metadata.getValue(), reference)));
         } catch (ParseException parseExcept) {
            throw new InvalidSAETypeException(parseExcept);
         } catch (ReferentialException refExcpt) {
            throw new MappingFromReferentialException(refExcpt);
         }
      }
      String name = null;
      if (StringUtils.isBlank(storage.getFilePath())) {
         name = storage.getFileName();
      } else {
         name = FilenameUtils.getBaseName(storage.getFilePath());
      }
      UntypedDocument untypedDocument = new UntypedDocument(storage
            .getContent(), storage.getFilePath(), name, metadatas);
      untypedDocument.setUuid(storage.getUuid());
      return untypedDocument;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VirtualStorageDocument saeVirtualDocumentToVirtualStorageDocument(
         SAEVirtualDocument document) throws InvalidSAETypeException {

      VirtualStorageDocument storageDocument = new VirtualStorageDocument();
      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();

      storageDocument.setEndPage(document.getEndPage());
      storageDocument.setStartPage(document.getStartPage());

      String filePath = document.getReference().getFilePath();
      String fileName = FilenameUtils.getBaseName(filePath);
      fileName = fileName.concat("_").concat(
            String.valueOf(document.getStartPage())).concat("_").concat(
            String.valueOf(document.getEndPage()));
      storageDocument.setFileName(fileName);

      for (SAEMetadata metadata : Utils.nullSafeIterable(document
            .getMetadatas())) {
         metadatas.add(new StorageMetadata(metadata.getShortCode(), metadata
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
         List<UntypedMetadata> metadatas) throws InvalidSAETypeException,
         MappingFromReferentialException {
      final List<SAEMetadata> saeMetadatas = new ArrayList<SAEMetadata>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            saeMetadatas.add(new SAEMetadata(reference.getLongCode(), reference
                  .getShortCode(), Utils.conversionToObject(
                  metadata.getValue(), reference)));
         } catch (ParseException parseExcept) {
            throw new InvalidSAETypeException(parseExcept);
         } catch (ReferentialException refExcpt) {
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
         List<UntypedMetadata> metadatas) throws InvalidSAETypeException,
         MappingFromReferentialException {
      final List<SAEMetadata> saeMetadatas = new ArrayList<SAEMetadata>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (StringUtils.isEmpty(metadata.getValue())) {
               saeMetadatas.add(new SAEMetadata(reference.getLongCode(),
                     reference.getShortCode(), null));
            } else {
               saeMetadatas.add(new SAEMetadata(reference.getLongCode(),
                     reference.getShortCode(), Utils.conversionToObject(
                           metadata.getValue(), reference)));
            }
         } catch (ParseException parseExcept) {
            throw new InvalidSAETypeException(parseExcept);
         } catch (ReferentialException refExcpt) {
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
         UntypedVirtualDocument document) throws InvalidSAETypeException,
         MappingFromReferentialException {

      SAEVirtualDocument virtualDocument = new SAEVirtualDocument();
      virtualDocument.setEndPage(document.getEndPage());
      virtualDocument.setIndex(document.getIndex());
      List<SAEMetadata> metadatas = untypedMetadatasToSaeMetadatas(document
            .getuMetadatas());
      virtualDocument.setMetadatas(metadatas);
      virtualDocument.setStartPage(document.getStartPage());
      virtualDocument.setReference(document.getReference());

      return virtualDocument;
   }

   @Override
   public List<StorageMetadata> saeMetadatasToStorageMetadatas(
         List<SAEMetadata> metadatas) {

      List<StorageMetadata> sMetadata = new ArrayList<StorageMetadata>();
      for (SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         sMetadata.add(new StorageMetadata(metadata.getShortCode(), metadata
               .getValue()));
      }

      return sMetadata;
   }

}
