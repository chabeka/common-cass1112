package fr.urssaf.image.sae.services.controles.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
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
import fr.urssaf.image.sae.services.util.FormatUtils;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;

/**
 * Classe de contrôle des métadonnées.
 * 
 * @author rhofir.
 */
@Service
@Qualifier("saeControlesCaptureService")
public class SAEControlesCaptureServiceImpl implements
      SAEControlesCaptureService {
   private static final Logger LOGGER = Logger
         .getLogger(SAEControlesCaptureServiceImpl.class);
   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices metadataCS;

   /*
    * (non-Javadoc)
    * 
    * @see
    * fr.urssaf.image.sae.services.document.controles.SAEControlesCaptureService
    * #checkSaeMetadataForCapture(fr.urssaf.image.sae.bo.model.bo.SAEDocument)
    */
   @Override
   public final void checkSaeMetadataForCapture(SAEDocument saeDocument)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx {
      List<MetadataError> errorsList = metadataCS
            .checkArchivableMetadata(saeDocument);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         throw new NotSpecifiableMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.interdites",buildLongCodeError(errorsList)));
      }
      errorsList = metadataCS.checkRequiredForArchivalMetadata(saeDocument);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         throw new RequiredArchivableMetadataEx(ResourceMessagesUtils
               .loadMessage("capture.metadonnees.archivage.obligatoire",
                     buildLongCodeError(errorsList)));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkSaeMetadataForStorage(SAEDocument sAEDocument)
         throws RequiredStorageMetadataEx {
      List<MetadataError> errorsList = metadataCS
            .checkRequiredForStorageMetadata(sAEDocument);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         LOGGER.error(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.stockage.obligatoire", buildLongCodeError(errorsList)));
         throw new RequiredStorageMetadataEx(ResourceMessagesUtils
               .loadMessage("erreur.technique.capture.unitaire"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkHashCodeMetadataForStorage(SAEDocument saeDocument)
         throws UnknownHashCodeEx {
      String hashCodeValue = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeDocument.getMetadatas(), SAEArchivalMetadatas.HASH_CODE
                  .getLongCode());
      String algoHashCode = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeDocument.getMetadatas(), SAEArchivalMetadatas.TYPE_HASH
                  .getLongCode());
      // FIXME vérifier que l'algorithme passer fait partie d'une liste
      // pré-définit.
      if (!"SHA-1".equals(algoHashCode)) {
         throw new UnknownHashCodeEx(ResourceMessagesUtils.loadMessage(
               "capture.hash.erreur",  new File(saeDocument.getFilePath()).getName()));
      }
      // FIXME à partie de l'algorithme calculer le hashCode.
      if (!DigestUtils.shaHex(saeDocument.getContent()).equals(
            hashCodeValue.trim())) {
         throw new UnknownHashCodeEx(ResourceMessagesUtils.loadMessage(
               "capture.hash.erreur",  new File(saeDocument.getFilePath()).getName()));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkUntypedDocument(UntypedDocument untypedDocument)
         throws EmptyDocumentEx {
      if (untypedDocument.getContent() == null
            || untypedDocument.getContent().length == 0) {
         throw new EmptyDocumentEx(ResourceMessagesUtils.loadMessage(
               "capture.fichier.vide", new File(untypedDocument.getFilePath()).getName()));
      }

   }

   /**
    * {@inheritDoc} 
    */
   @Override
   public final void checkUntypedMetadata(UntypedDocument untypedDocument)
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx {
      List<MetadataError> errorsList = metadataCS
            .checkExistingMetadata(untypedDocument);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         throw new UnknownMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", buildLongCodeError(errorsList)));
      }
      errorsList = metadataCS.checkDuplicateMetadata(untypedDocument
            .getUMetadatas());
      if (CollectionUtils.isNotEmpty(errorsList)) {
         throw new DuplicatedMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.doublon", buildLongCodeError(errorsList)));
      }
      errorsList = metadataCS.checkMetadataRequiredValue(untypedDocument);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         throw new RequiredArchivableMetadataEx(ResourceMessagesUtils
               .loadMessage("capture.metadonnees.archivage.obligatoire",
                     buildLongCodeError(errorsList)));
      }
      errorsList = metadataCS.checkMetadataValueTypeAndFormat(untypedDocument);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         throw new InvalidValueTypeAndFormatMetadataEx(ResourceMessagesUtils
               .loadMessage("capture.metadonnees.format.type.non.valide",
                     buildLongCodeError(errorsList)));
      }
   }

   /**
    * Construire une list de code long.
    * 
    * @param errorsList
    *           : Liste de de type {@link MetadataError}
    * @return Liste de code long à partir d'une liste de de type
    *         {@link MetadataError}
    */
   private String buildLongCodeError(List<MetadataError> errorsList) {
      List<String> codeLongErrors = new ArrayList<String>();
      for (MetadataError metadataError : Utils.nullSafeIterable(errorsList)) {
         codeLongErrors.add(metadataError.getLongCode());
      }
      
      return FormatUtils.formattingDisplayList(codeLongErrors);
   }
}
