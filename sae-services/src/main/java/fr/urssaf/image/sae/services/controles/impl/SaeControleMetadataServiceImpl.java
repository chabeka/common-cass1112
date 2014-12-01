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
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.services.controles.SaeControleMetadataService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.util.MetadataErrorUtils;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe d'implémentation de l'interface {@link SaeControleMetadataService}.
 * Cette classe est un singleton accessible via l'injection IOC et l'annotation @Autowired
 * 
 */
@Service
public class SaeControleMetadataServiceImpl implements
      SaeControleMetadataService {

   private static final String META_OBLIGATOIRE = "capture.metadonnees.archivage.obligatoire";

   private static final Logger LOG = LoggerFactory
         .getLogger(SaeControleMetadataServiceImpl.class);

   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices metadataCS;

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SAEMetadata> checkMetadataForStorage(
         List<SAEMetadata> metadatas) throws RequiredStorageMetadataEx {
      String trcPrefix = "checkMetadataForStorage";
      LOG.debug("{} - début", trcPrefix);

      // Fin des traces debug - entrée méthode
      String listeCodeLong = null;
      LOG.debug("{} - Début de la vérification : "
            + "Les métadonnées obligatoires lors du stockage sont présentes",
            trcPrefix);
      List<MetadataError> errorsList = metadataCS
            .checkRequiredForStorageMetadataList(metadatas);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.stockage.obligatoire", listeCodeLong));
         throw new RequiredStorageMetadataEx(ResourceMessagesUtils
               .loadMessage("erreur.technique.capture.unitaire"));
      }
      LOG.debug("{} - Fin de la vérification : "
            + "Les métadonnées obligatoire lors du stockage sont présentes",
            trcPrefix);

      // Trim des métadonnées
      LOG.debug("{} - Début du trim des métadonnées concernées", trcPrefix);
      metadatas = metadataCS.trimMetadata(metadatas);
      LOG.debug("{} - Fin du trim des métadonnées concernées", trcPrefix);

      LOG.debug("{} - fin", trcPrefix);
      return metadatas;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkSaeMetadataForCapture(List<SAEMetadata> metadatas)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx {
      String trcPrefix = "checkSaeMetadataForCapture";
      LOG.debug("{} - début", trcPrefix);

      List<MetadataError> errorsList = metadataCS
            .checkArchivableMetadataList(metadatas);
      String listeCodeLong = null;
      LOG
            .debug(
                  "{} - Début de la vérification : "
                        + "Les métadonnées fournies par l'application cliente sont spécifiables à l'archivage",
                  trcPrefix);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.interdites", listeCodeLong));
         throw new NotSpecifiableMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.interdites", listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : "
                        + "Les métadonnées fournies par l'application cliente sont spécifiables à l'archivage",
                  trcPrefix);

      errorsList = metadataCS.checkRequiredForArchivalMetadataList(metadatas);
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées obligatoires à l'archivage sont renseignées",
                  trcPrefix);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               META_OBLIGATOIRE, listeCodeLong));
         throw new RequiredArchivableMetadataEx(ResourceMessagesUtils
               .loadMessage(META_OBLIGATOIRE, listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées obligatoires à l'archivage sont renseignées",
                  trcPrefix);

      LOG.debug("{} - fin", trcPrefix);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkUntypedMetadatas(List<UntypedMetadata> metadatas)
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx,
         MetadataValueNotInDictionaryEx {
      String trcPrefix = "checkUntypedMetadatas";
      LOG.debug("{} - début", trcPrefix);

      String listeCodeLong = null;
      LOG
            .debug(
                  "{} - Début de la vérification :"
                        + " Les métadonnées fournies par l'application cliente existent dans le référentiel des métadonnées",
                  trcPrefix);
      List<MetadataError> errorsList = metadataCS
            .checkExistingMetadataList(metadatas);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", listeCodeLong));
         throw new UnknownMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.inconnu", listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : "
                        + "Les métadonnées fournies par l'application cliente existent dans le référentiel des métadonnées",
                  trcPrefix);
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées ne sont pas multi-valuées",
                  trcPrefix);
      errorsList = metadataCS.checkDuplicateMetadata(metadatas);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.doublon", listeCodeLong));
         throw new DuplicatedMetadataEx(ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.doublon", listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées ne sont pas multi-valuées",
                  trcPrefix);
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées obligatoires à l'archivage sont renseignées",
                  trcPrefix);
      errorsList = metadataCS.checkMetadataListRequiredValue(metadatas);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               META_OBLIGATOIRE, listeCodeLong));
         throw new RequiredArchivableMetadataEx(ResourceMessagesUtils
               .loadMessage(META_OBLIGATOIRE, listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées obligatoires à l'archivage sont renseignées",
                  trcPrefix);
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées respectent leurs contraintes de format",
                  trcPrefix);
      errorsList = metadataCS.checkMetadataListValueTypeAndFormat(metadatas);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "capture.metadonnees.format.type.non.valide", listeCodeLong));
         throw new InvalidValueTypeAndFormatMetadataEx(ResourceMessagesUtils
               .loadMessage("capture.metadonnees.format.type.non.valide",
                     listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées respectent leurs contraintes de format",
                  trcPrefix);
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées font bien parties du dictionnaire",
                  trcPrefix);
      errorsList = metadataCS.checkMetadataListValueFromDictionary(metadatas);
      if (CollectionUtils.isNotEmpty(errorsList)) {
         listeCodeLong = MetadataErrorUtils.buildLongCodeError(errorsList);
         LOG.debug("{} - {}", trcPrefix, ResourceMessagesUtils.loadMessage(
               "metadata.not.in.dictionary", listeCodeLong));
         throw new MetadataValueNotInDictionaryEx(ResourceMessagesUtils
               .loadMessage("metadata.not.in.dictionary", listeCodeLong));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées font bien parties du dictionnaire",
                  trcPrefix);

      LOG.debug("{} - fin", trcPrefix);
   }

}
