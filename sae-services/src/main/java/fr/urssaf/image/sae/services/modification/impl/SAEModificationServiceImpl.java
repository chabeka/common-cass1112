/**
 * 
 */
package fr.urssaf.image.sae.services.modification.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.constants.Constants;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.service.RndService;
import fr.urssaf.image.sae.services.controles.SAEControlesModificationService;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.ModificationRuntimeException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe d'implémentation de l'interface {@link SAEModificationService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired
 * 
 */
@Service
public class SAEModificationServiceImpl extends AbstractSAEServices implements
      SAEModificationService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAEModificationServiceImpl.class);

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService documentService;

   @Autowired
   private SAEControlesModificationService controlesModificationService;

   @Autowired
   private MappingDocumentService mappingDocumentService;

   @Autowired
   private RndService rndService;

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   private MappingDocumentService mappingService;
   
   @Autowired
   private PrmdService prmdService;

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void modification(UUID idArchive,
         List<UntypedMetadata> metadonnees)
         throws InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, ReferentialRndException,
         UnknownCodeRndEx, UnknownHashCodeEx, NotModifiableMetadataEx,
         ModificationException, ArchiveInexistanteEx,
         MetadataValueNotInDictionaryEx {
      String trcPrefix = "modification";
      LOG.debug("{} - début", trcPrefix);

      LOG.debug("{} - recherche du document", trcPrefix);

      try {
         this.getStorageServiceProvider().openConnexion();

         StorageDocument document;
         
         try {
            
            // On récupère la liste de toutes les méta du référentiel
            List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
            Map<String, MetadataReference> listeAllMeta = referenceDAO
                  .getAllMetadataReferences();
            for (String mapKey : listeAllMeta.keySet()) {
               allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
                     .getShortCode()));
            }

            UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

          
            // On récupère le document à partir de l'UUID, avec toutes les
            // métadonnées du référentiel
            document = documentService
                  .searchStorageDocumentByUUIDCriteria(uuidCriteria);
            if (document == null) {
               String message = StringUtils
                     .replace(
                           "Il n'existe aucun document pour l'identifiant d'archivage '{0}'",
                           "{0}", idArchive.toString());
               throw new ArchiveInexistanteEx(message);
            } else {

               UntypedDocument untypedDocument = this.mappingService
                     .storageDocumentToUntypedDocument(document);

               LOG.debug("{} - Récupération des droits", trcPrefix);
               AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                     .getContext().getAuthentication();
               List<SaePrmd> saePrmds = token.getSaeDroits()
                     .get("modification");
               LOG.debug("{} - Vérification des droits", trcPrefix);
               boolean isPermitted = prmdService.isPermitted(
                     untypedDocument.getUMetadatas(), saePrmds);

               if (!isPermitted) {
                  throw new AccessDeniedException(
                        "Le document est refusé à la modification car les droits sont insuffisants");
               }

            }

         } catch (SearchingServiceEx exception) {
            throw new ModificationException(exception);
         } catch (InvalidSAETypeException exception) {
            throw new ModificationException(exception);
         } catch (MappingFromReferentialException exception) {
            throw new ModificationException(exception);
         } catch (ReferentialException exception) {
            throw new ModificationException(exception);
         }

         LOG.debug("{} - vérification non dupplication des métadonnées",
               trcPrefix);
         if (!CollectionUtils.isEmpty(metadonnees)) {
            controlesModificationService
                  .checkSaeMetadataForModification(metadonnees);
         }

         LOG.debug(
               "{} - Séparation des métadonnées en modifiées et supprimées",
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

         }
         if (!CollectionUtils.isEmpty(deletedMetadatas)) {
            controlesModificationService
                  .checkSaeMetadataForDelete(deletedMetadatas);
         }

         // Application des règles de gestion pour compléter les métadonnées (ex
         // :
         // enrichissement du code fonction, activité et
         // date de fin si modification du code RND)
         List<List<UntypedMetadata>> completedMetadatas = completeMetadatas(
               modifiedMetadatas, deletedMetadatas, document.getMetadatas());

         modifiedMetadatas = completedMetadatas.get(0);
         deletedMetadatas = completedMetadatas.get(1);

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
                     .nullSafeUntypedMetadatasToSaeMetadatas(deletedMetadatas);
               deletedStorageMetas = mappingDocumentService
                     .saeMetadatasToStorageMetadatas(deletedSaeMetadatas);
            }

            documentService.updateStorageDocument(idArchive,
                  modifiedStorageMetas, deletedStorageMetas);

         } catch (InvalidSAETypeException exception) {
            throw new ModificationRuntimeException(exception);

         } catch (MappingFromReferentialException exception) {
            throw new ModificationRuntimeException(exception);

         } catch (UpdateServiceEx exception) {
            throw new ModificationRuntimeException(exception);
         }

      } catch (ConnectionServiceEx e) {

         throw new ModificationException(e);
      }

      LOG.debug("{} - fin", trcPrefix);
   }

   /**
    * Vérifie les règles de gestion des métadonnées
    * 
    * @param modifiedMetadatas
    *           la liste des métadonnées à modifier
    * @return la nouvelle liste enrichie des métadonnées
    * @throws UnknownCodeRndEx
    * @throws CodeRndInexistantException
    */
   private List<List<UntypedMetadata>> completeMetadatas(
         List<UntypedMetadata> modifiedMetadatas,
         List<UntypedMetadata> deletedMetadatas, List<StorageMetadata> list)
         throws UnknownCodeRndEx {
      String trcPrefix = "completeMetadatas";
      LOG.debug("{} - début", trcPrefix);

      List<UntypedMetadata> returnedModifiedList = new ArrayList<UntypedMetadata>(
            modifiedMetadatas);
      List<UntypedMetadata> returnedDeletedList = new ArrayList<UntypedMetadata>(
            deletedMetadatas);
      String codeRnd = getValue(SAEArchivalMetadatas.CODE_RND.getLongCode(),
            modifiedMetadatas);

      try {
         if (StringUtils.isNotBlank(codeRnd)) {

            Date date = (Date) getValue("SM_LIFE_CYCLE_REFERENCE_DATE", list);
            String codeActivite;

            codeActivite = rndService.getCodeActivite(codeRnd);

            String codeFonction = rndService.getCodeFonction(codeRnd);

            int duration = rndService.getDureeConservation(codeRnd);
            Date dateFin = DateUtils.addDays(date, duration);
            String sDateFin = DateFormatUtils.format(dateFin,
                  Constants.DATE_PATTERN, Constants.DEFAULT_LOCAL);
            if (StringUtils.isNotEmpty(codeActivite)) {
               returnedModifiedList.add(new UntypedMetadata(
                     SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode(),
                     codeActivite));
            } else {
               returnedDeletedList.add(new UntypedMetadata(
                     SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode(),
                     codeActivite));
            }

            returnedModifiedList.add(new UntypedMetadata(
                  SAEArchivalMetadatas.CODE_FONCTION.getLongCode(),
                  codeFonction));
            returnedModifiedList.add(new UntypedMetadata(
                  SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode(),
                  sDateFin));

         }
      } catch (CodeRndInexistantException e) {
         throw new UnknownCodeRndEx(e.getMessage(), e);
      }

      List<List<UntypedMetadata>> returnedList = new ArrayList<List<UntypedMetadata>>();
      returnedList.add(returnedModifiedList);
      returnedList.add(returnedDeletedList);

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
