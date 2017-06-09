/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle.impl;

import java.io.File;
import java.util.List;

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
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Implémentation du support {@link CaptureMasseControleSupport}
 * 
 */
@Component
public class ModificationMasseControleSupportImpl extends AbstractSAEServices
      implements ModificationMasseControleSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ModificationMasseControleSupportImpl.class);

   @Autowired
   private SAEModificationService modificationService;

   /**
    * {@inheritDoc}
    */
   @Override
   public ModificationMasseControlResult controleSAEDocumentMetadatas(
         final UntypedDocument document, final File ecdeDirectory)
         throws ReferentialRndException, UnknownCodeRndEx,
         ArchiveInexistanteEx, ModificationException, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         NotSpecifiableMetadataEx, RequiredArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx,
         MetadataValueNotInDictionaryEx,
         CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx {
      String trcPrefix = "controleSAEDocumentMetadatas()";
      LOGGER.debug("{} - début", trcPrefix);

      ModificationMasseControlResult result = null;

      try {
         getStorageServiceProvider().openConnexion();

         List<StorageMetadata> storageMetadatasList = modificationService
               .controlerMetaDocumentModifie(document.getUuid(),
                     document.getUMetadatas(), trcPrefix);

         if (storageMetadatasList != null && !storageMetadatasList.isEmpty()) {
            result = new ModificationMasseControlResult();
            result.setStorageMetadatasList(storageMetadatasList);
         }

      } catch (ConnectionServiceEx e) {
         throw new ModificationException(e);
      } finally {
         getStorageServiceProvider().closeConnexion();
      }

      LOGGER.debug("{} - fin", trcPrefix);

      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument controleSAEDocumentModification(UntypedDocument item)
         throws UnknownCodeRndEx, ReferentialRndException,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx, 
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         NotModifiableMetadataEx, MetadataValueNotInDictionaryEx,
         ModificationException, ReferentialException, RetrievalServiceEx {
      String trcPrefix = "controleSAEDocumentModification()";
      LOGGER.debug("{} - début", trcPrefix);

      List<StorageMetadata> listeMetadataDocument = modificationService
            .getListeStorageMetadatas(item.getUuid());

      StorageDocument document = modificationService.separationMetaDocumentModifie(
            item.getUuid(), listeMetadataDocument, item.getUMetadatas(),
            trcPrefix);

      LOGGER.debug("{} - fin", trcPrefix);

      return document;

   }

}
