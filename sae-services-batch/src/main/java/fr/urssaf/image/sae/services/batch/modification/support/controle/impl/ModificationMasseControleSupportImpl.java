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
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.modification.support.controle.ModificationMasseControleSupport;
import fr.urssaf.image.sae.services.batch.modification.support.controle.model.ModificationMasseControlResult;
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
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Implémentation du support {@link CaptureMasseControleSupport}
 * 
 */
@Component
public class ModificationMasseControleSupportImpl implements ModificationMasseControleSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ModificationMasseControleSupportImpl.class);

   private static final String PREFIXE_TRC = "controleFichierNonVide()";

   @Autowired
   private SAEModificationService modificationService;

   /**
    * {@inheritDoc}
    */
   @Override
   public ModificationMasseControlResult controleSAEDocumentMetadatas(
         final UntypedDocument document, final File ecdeDirectory) throws ReferentialRndException, UnknownCodeRndEx, ArchiveInexistanteEx, ModificationException, DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, NotSpecifiableMetadataEx, RequiredArchivableMetadataEx, UnknownHashCodeEx, NotModifiableMetadataEx, MetadataValueNotInDictionaryEx, CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx {
      String trcPrefix = "controleSAEDocumentMetadatas()";
      LOGGER.debug("{} - début", trcPrefix);

      ModificationMasseControlResult result = new ModificationMasseControlResult();
      final File file = getFichierSiExiste(document, ecdeDirectory);

      controleFichierNonVide(file);

      List<StorageMetadata> storageMetadatasList = modificationService.controlerMetaDocumentModifie(document.getUuid(), document.getUMetadatas(), trcPrefix);
      
      if (storageMetadatasList != null && !storageMetadatasList.isEmpty()) {
         result.setStorageMetadatasList(storageMetadatasList);
      }
      
      LOGGER.debug("{} - fin", trcPrefix);
      
      return result;
   }

   /**
    * Vérifie si le fichier existe. Si c'est le cas, le retourne. Sinon, levée
    * d'une exception
    * 
    * @param document
    *           document dont il faut vérifier l'existence
    * @param ecdeDirectory
    *           répertoire de traitement
    * @throws CaptureMasseSommaireDocumentNotFoundException
    *            exception si le fichier n'existe pas
    */
   private File getFichierSiExiste(final UntypedDocument document,
         final File ecdeDirectory)
         throws CaptureMasseSommaireDocumentNotFoundException {

      return getFichierSiExiste(document.getFilePath(), ecdeDirectory);
   }

   /**
    * Vérifie si le fichier existe. Si c'est le cas, le retourne. Sinon, levée
    * d'une exception
    * 
    * @param filePath
    *           chemin du fichier dont il faut vérifier l'existence
    * @param ecdeDirectory
    *           répertoire de traitement
    * @throws CaptureMasseSommaireDocumentNotFoundException
    *            exception si le fichier n'existe pas
    */
   private File getFichierSiExiste(String filePath, File ecdeDirectory)
         throws CaptureMasseSommaireDocumentNotFoundException {
      final String path = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + filePath;

      final File documentFile = new File(path);

      if (!documentFile.exists()) {
         throw new CaptureMasseSommaireDocumentNotFoundException(filePath);
      }

      return documentFile;
   }

   /**
    * Vérifie que le fichier passé en paramètre est non vide
    * 
    * @param document
    *           document à vérifier
    * @param ecdeDirectory
    *           répertoire de traitement
    * @throws EmptyDocumentEx
    *            exception levée si le fichier est vide
    */
   private void controleFichierNonVide(final File documentFile)
         throws EmptyDocumentEx {

      LOGGER.debug("{} - Début", PREFIXE_TRC);

      if (documentFile.length() == 0) {
         LOGGER.debug("{} - Le fichier à archiver est vide ({})", PREFIXE_TRC,
               documentFile.getName());
         throw new EmptyDocumentEx(String.format(
               "Le fichier à archiver est vide (%s)", documentFile.getName()));
      }

      LOGGER.debug("{} - Sortie", PREFIXE_TRC);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument controleSAEDocumentModification(UntypedDocument item,
         List<StorageMetadata> listeMetadataDocument) throws UnknownCodeRndEx, ReferentialRndException, 
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx, 
         RequiredArchivableMetadataEx, UnknownHashCodeEx, NotModifiableMetadataEx, MetadataValueNotInDictionaryEx, ModificationException {
      String trcPrefix = "controleSAEDocumentModification()";
      LOGGER.debug("{} - début", trcPrefix);
      
      StorageDocument document = modificationService.separationMetaDocumentModifie(item.getUuid(), listeMetadataDocument, item.getUMetadatas(), trcPrefix);
      
      LOGGER.debug("{} - fin", trcPrefix);
      
      return document;
        
   }

}
