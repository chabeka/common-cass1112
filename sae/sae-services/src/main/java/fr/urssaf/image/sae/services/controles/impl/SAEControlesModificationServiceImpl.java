/**
 * 
 */
package fr.urssaf.image.sae.services.controles.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.services.controles.SAEControlesModificationService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationRuntimeException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.util.MetadataErrorUtils;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Service implémentant l'interface {@link SAEControlesModificationService}.
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Service
public class SAEControlesModificationServiceImpl implements
      SAEControlesModificationService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAEControlesModificationServiceImpl.class);

   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices metadataCS;

   @Autowired
   private MappingDocumentService mappingService;

   @Override
   public final void checkSaeMetadataForModification(
         List<UntypedMetadata> metadatas) throws DuplicatedMetadataEx {
      String trcPrefix = "checkSaeMetadataForModification()";
      LOG.debug("{} - début", trcPrefix);

      String listeCodeLong;

      LOG.debug("{} - vérification de la duplication des métadonnées",
            trcPrefix);
      List<MetadataError> errors = metadataCS.checkDuplicateMetadata(metadatas);
      if (CollectionUtils.isNotEmpty(errors)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.doublon", listeCodeLong));
         throw new DuplicatedMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.doublon", listeCodeLong));
      }

      LOG.debug("{} - fin", trcPrefix);
   }

   /**
    * {@inheritDoc}
    * 
    * @throws UnknownMetadataEx
    * @throws RequiredArchivableMetadataEx
    */
   @Override
   public final void checkSaeMetadataForDelete(List<UntypedMetadata> metadatas)
         throws NotModifiableMetadataEx, UnknownMetadataEx,
         RequiredArchivableMetadataEx {
      String trcPrefix = "checkSaeMetadataForDelete()";
      LOG.debug("{} - début", trcPrefix);

      String listeCodeLong;

      LOG.debug("{} - vérification de l'existence des métadonnées", trcPrefix);
      List<MetadataError> errors = metadataCS
            .checkExistingMetadataList(metadatas);
      if (CollectionUtils.isNotEmpty(errors)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", listeCodeLong));
         throw new UnknownMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", listeCodeLong));
      }

      LOG.debug(
            "{} - vérification de la possibilité de supprimer les métadonnées",
            trcPrefix);
      errors = metadataCS.checkSupprimableMetadatas(metadatas);
      if (CollectionUtils.isNotEmpty(errors)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "modification.metadonnees.non.supprimable", listeCodeLong));
         throw new RequiredArchivableMetadataEx(ResourceMessagesUtils
               .loadMessage("modification.metadonnees.non.supprimable",
                     listeCodeLong));
      }

      checkModifiables(metadatas);

      LOG.debug("{} - fin", trcPrefix);

   }

   /**
    * {@inheritDoc}
    * 
    * @throws MetadataValueNotInDictionaryEx
    */
   @Override
   public final void checkSaeMetadataForUpdate(List<UntypedMetadata> metadatas)
         throws InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, ReferentialRndException,
         UnknownCodeRndEx, UnknownHashCodeEx, NotModifiableMetadataEx,
         MetadataValueNotInDictionaryEx {
      String trcPrefix = "checkSaeMetadataForUpdate";
      LOG.debug("{} - début", trcPrefix);

      String listeCodeLong = null;

      LOG.debug("{} - vérification de l'existence des métadonnées", trcPrefix);
      List<MetadataError> errors = metadataCS
            .checkExistingMetadataList(metadatas);
      if (CollectionUtils.isNotEmpty(errors)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", listeCodeLong));
         throw new UnknownMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", listeCodeLong));
      }

      checkModifiables(metadatas);

      try {
         
         LOG.debug("{} - vérification des types et formats des métadonnées",
               trcPrefix);
         errors = metadataCS.checkMetadataListValueTypeAndFormat(metadatas);
         if (CollectionUtils.isNotEmpty(errors)) {
            listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
            LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
                  "capture.metadonnees.format.type.non.valide", listeCodeLong));
            throw new InvalidValueTypeAndFormatMetadataEx(ResourceMessagesUtils
                  .loadMessage("capture.metadonnees.format.type.non.valide",
                        listeCodeLong));
         }
         
         LOG
               .debug(
                     "{} - vérification de la possibilité d'archiver les métadonnées",
                     trcPrefix);
         List<SAEMetadata> saeMetadatas = mappingService
               .untypedMetadatasToSaeMetadatas(metadatas);
         errors = metadataCS.checkArchivableMetadataList(saeMetadatas);

         if (CollectionUtils.isNotEmpty(errors)) {
            listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
            LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
                  "modification.metadonnees.non.modifiable", listeCodeLong));
            throw new NotModifiableMetadataEx(ResourceMessagesUtils
                  .loadMessage("modification.metadonnees.non.modifiable",
                        listeCodeLong));
         }
      } catch (MappingFromReferentialException exception) {
         throw new ModificationRuntimeException(exception);

      } catch (InvalidSAETypeException exception) {
         throw new InvalidValueTypeAndFormatMetadataEx(exception.getMessage());
      }


      LOG.debug(
            "{} - vérification des valeurs des métadonnées avec dictionnaire",
            trcPrefix);
      errors = metadataCS.checkMetadataListValueFromDictionary(metadatas);
      if (CollectionUtils.isNotEmpty(errors)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "metadata.not.in.dictionary", listeCodeLong));
         throw new MetadataValueNotInDictionaryEx(ResourceMessagesUtils
               .loadMessage("metadata.not.in.dictionary", listeCodeLong));
      }

      LOG.debug("{} - fin", trcPrefix);
   }

   private void checkModifiables(List<UntypedMetadata> metadatas)
         throws NotModifiableMetadataEx {
      String trcPrefix = "checkModifiables";
      LOG.debug(
            "{} - vérification de la possibilité de modifier les métadonnées",
            trcPrefix);

      String listeCodeLong;
      List<MetadataError> errors = metadataCS
            .checkModifiableMetadataList(metadatas);
      if (CollectionUtils.isNotEmpty(errors)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errors);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "modification.metadonnees.non.modifiable", listeCodeLong));
         throw new NotModifiableMetadataEx(ResourceMessagesUtils.loadMessage(
               "modification.metadonnees.non.modifiable", listeCodeLong));
      }

   }
}
