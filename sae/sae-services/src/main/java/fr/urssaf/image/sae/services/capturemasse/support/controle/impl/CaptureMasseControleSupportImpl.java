/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseDocumentFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.enrichment.dao.RNDReferenceDAO;
import fr.urssaf.image.sae.services.enrichment.dao.impl.SAEMetatadaFinderUtils;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Implémentation du support {@link CaptureMasseControleSupport}
 * 
 */
@Component
public class CaptureMasseControleSupportImpl implements
      CaptureMasseControleSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CaptureMasseControleSupportImpl.class);

   private static final String PREFIXE_TRC = "controleFichierNonVide()";

   @Autowired
   private SAEControlesCaptureService controleService;

   @Autowired
   private MappingDocumentService mappingService;

   @Autowired
   private RNDReferenceDAO rndReferenceDAO;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void controleSAEDocument(final UntypedDocument document,
         final File ecdeDirectory)
         throws CaptureMasseDocumentFileNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx, UnknownCodeRndEx {

      final File file = getFichierSiExiste(document, ecdeDirectory);

      controleFichierNonVide(file);

      controleService.checkUntypedMetadata(document);

      SAEDocument saeDocument;
      // Les deux catch sont inaccessibles car toutes les vérifications des
      // metadonnees sont réalisées avant
      try {
         saeDocument = mappingService.untypedDocumentToSaeDocument(document);
      } catch (InvalidSAETypeException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (MappingFromReferentialException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      controleService.checkSaeMetadataForCapture(saeDocument);

      // set du chemin de fichier complet dans le saeDocument
      String path = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + saeDocument.getFilePath();
      saeDocument.setFilePath(path);

      controleService.checkHashCodeMetadataForStorage(saeDocument);

      final String valeurMetadata = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeDocument.getMetadatas(), SAEArchivalMetadatas.CODE_RND
                  .getLongCode());

      try {
         rndReferenceDAO.getTypeDocument(valeurMetadata);
      } catch (ReferentialRndException e) {
         throw new CaptureMasseRuntimeException(e);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void controleSAEDocumentStockage(final SAEDocument document)
         throws RequiredStorageMetadataEx {

      controleService.checkSaeMetadataForStorage(document);

   }

   /**
    * Vérifie si le fichier existe. Si c'est le cas, le retourne. Sinon, levée
    * d'une exception
    * 
    * @param document
    *           document dont il faut vérifier l'existence
    * @param ecdeDirectory
    *           répertoire de traitement
    * @throws CaptureMasseDocumentFileNotFoundException
    *            exception si le fichier n'existe pas
    */
   private File getFichierSiExiste(final UntypedDocument document,
         final File ecdeDirectory)
         throws CaptureMasseDocumentFileNotFoundException {

      final String path = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + document.getFilePath();

      final File documentFile = new File(path);

      if (!documentFile.exists()) {
         throw new CaptureMasseDocumentFileNotFoundException(path);
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
         LOGGER.debug("{} - {}", PREFIXE_TRC, ResourceMessagesUtils
               .loadMessage("capture.fichier.vide", documentFile.getName()));
         throw new EmptyDocumentEx("Le fichier à archiver est vide ("
               + documentFile.getName() + ")");
      }

      LOGGER.debug("{} - Sortie", PREFIXE_TRC);

   }

}
