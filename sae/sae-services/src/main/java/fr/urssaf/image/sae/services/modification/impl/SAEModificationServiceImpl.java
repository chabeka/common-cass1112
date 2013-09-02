/**
 * 
 */
package fr.urssaf.image.sae.services.modification.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.constants.Constants;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.service.RndService;
import fr.urssaf.image.sae.services.controles.SAEControlesModificationService;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.ModificationRuntimeException;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.services.modification.exception.NotModifiableMetadataEx;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe d'implémentation de l'interface {@link SAEModificationService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired
 * 
 */
@Service
public class SAEModificationServiceImpl implements SAEModificationService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAEModificationServiceImpl.class);

   @Autowired
   private StorageDocumentService documentService;

   @Autowired
   private SAEControlesModificationService controlesModificationService;

   @Autowired
   private MappingDocumentService mappingDocumentService;

   @Autowired
   private RndService rndService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void modification(UUID idArchive,
         List<UntypedMetadata> metadonnees)
         throws InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         NotModifiableMetadataEx, ModificationException {
      String trcPrefix = "modification";
      LOG.debug("{} - début", trcPrefix);

      LOG.debug("{} - recherche du document", trcPrefix);
      UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, Arrays.asList(
            new StorageMetadata(
                  StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION
                        .getShortCode()), new StorageMetadata(
                  StorageTechnicalMetadatas.TYPE.getShortCode())));
      StorageDocument document;
      try {
         document = documentService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);

      } catch (SearchingServiceEx exception) {
         String message = StringUtils.replace(
               "le document {0} n'existe pas. Modification impossible", "{0}",
               idArchive.toString());
         throw new ModificationException(message);
      }

      LOG.debug("{} - vérification non dupplication des métadonnées", trcPrefix);
      if (!CollectionUtils.isEmpty(metadonnees)) {
         controlesModificationService.checkSaeMetadataForModification(metadonnees);
      }
      
      LOG.debug("{} - Séparation des métadonnées en modifiées et supprimées",
            trcPrefix);
      List<UntypedMetadata> modifiedMetadatas = new ArrayList<UntypedMetadata>();
      List<UntypedMetadata> deletedMetadatas = new ArrayList<UntypedMetadata>();
      for (UntypedMetadata metadata : metadonnees) {
         if (StringUtils.isNotBlank(metadata.getValue())) {
            modifiedMetadatas.add(metadata);
         } else {
            deletedMetadatas.add(metadata);
         }
      }

      LOG.debug("{} - vérification des métadonnées", trcPrefix);
      if (!CollectionUtils.isEmpty(modifiedMetadatas)) {
         controlesModificationService
               .checkSaeMetadataForUpdate(modifiedMetadatas);
         modifiedMetadatas = completeMetadatas(modifiedMetadatas, document
               .getMetadatas());
      }
      if (!CollectionUtils.isEmpty(deletedMetadatas)) {
         controlesModificationService
               .checkSaeMetadataForDelete(deletedMetadatas);
      }

      try {
         List<StorageMetadata> modifiedStorageMetas = new ArrayList<StorageMetadata>();
         if (!CollectionUtils.isEmpty(modifiedMetadatas)) {
            List<SAEMetadata> modifiedSaeMetadatas = mappingDocumentService
                  .untypedMetadatasToSaeMetadatas(modifiedMetadatas);
            modifiedStorageMetas = mappingDocumentService
                  .saeMetadatasToStorageMetadatas(modifiedSaeMetadatas);
         }

         List<StorageMetadata> deletedStorageMetas = new ArrayList<StorageMetadata>();
         if (!CollectionUtils.isEmpty(deletedMetadatas)) {
            List<SAEMetadata> deletedSaeMetadatas = mappingDocumentService
                  .untypedMetadatasToSaeMetadatas(deletedMetadatas);
            deletedStorageMetas = mappingDocumentService
                  .saeMetadatasToStorageMetadatas(deletedSaeMetadatas);
         }
         documentService.updateStorageDocument(idArchive, modifiedStorageMetas,
               deletedStorageMetas);

      } catch (InvalidSAETypeException exception) {
         throw new ModificationRuntimeException(exception);

      } catch (MappingFromReferentialException exception) {
         throw new ModificationRuntimeException(exception);

      } catch (UpdateServiceEx exception) {
         throw new ModificationRuntimeException(exception);
      }

      LOG.debug("{} - fin", trcPrefix);
   }

   /**
    * Vérifie les règles de gestion des métadonnées
    * 
    * @param modifiedMetadatas
    *           la liste des métadonnées à supprimer
    * @return la nouvelle liste enrichie des métadonnées
    */
   private List<UntypedMetadata> completeMetadatas(
         List<UntypedMetadata> modifiedMetadatas, List<StorageMetadata> list) {
      String trcPrefix = "completeMetadatas";
      LOG.debug("{} - début", trcPrefix);

      List<UntypedMetadata> returnedList = new ArrayList<UntypedMetadata>(
            modifiedMetadatas);
      String codeRnd = getValue(SAEArchivalMetadatas.CODE_RND.getLongCode(),
            modifiedMetadatas);

      try {
         if (StringUtils.isNotBlank(codeRnd)) {
            Date date = (Date) getValue("SM_LIFE_CYCLE_REFERENCE_DATE", list);

            String codeActivite = rndService.getCodeActivite(codeRnd);
            String codeFonction = rndService.getCodeFonction(codeRnd);
            int duration = rndService.getDureeConservation(codeRnd);
            Date dateFin = DateUtils.addDays(date, duration);
            String sDateFin = DateFormatUtils.format(dateFin,
                  Constants.DATE_PATTERN, Constants.DEFAULT_LOCAL);
            returnedList.add(new UntypedMetadata(
                  SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode(),
                  codeActivite));
            returnedList.add(new UntypedMetadata(
                  SAEArchivalMetadatas.CODE_FONCTION.getLongCode(),
                  codeFonction));
            returnedList.add(new UntypedMetadata(
                  SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode(),
                  sDateFin));

         }
      } catch (CodeRndInexistantException exception) {
         throw new ModificationRuntimeException(exception);
      }

      LOG.debug("{} - fin", trcPrefix);
      return returnedList;
   }

   private String getValue(String longCode, List<UntypedMetadata> metadatas) {
      String value = null;
      int index = 0;

      while (StringUtils.isBlank(value) && index < metadatas.size()) {

         if (longCode.equals(metadatas.get(index).getLongCode())) {
            value = metadatas.get(index).getValue();
         }
         index++;
      }

      return value;
   }

   private Object getValue(String shortCode, List<StorageMetadata> metadatas) {
      Object value = null;
      int index = 0;

      while (value == null && index < metadatas.size()) {

         if (shortCode.equals(metadatas.get(index).getShortCode())) {
            value = metadatas.get(index).getValue();
         }
         index++;
      }

      return value;
   }

}
